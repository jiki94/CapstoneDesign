package com.example.expirationdateapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;


// 식품 정보 입려하는 프레그먼트
public class AddFragment extends Fragment implements NESDialogFragment.NoticeDialogListener,
        FavoriteRecyclerViewAdapter.DBRelatedListener, View.OnClickListener {

    private FavoriteRecyclerViewAdapter recyclerViewAdapter;
    private AddViewModel addViewModel;
    private Observer loadingDataObserver = new Observer<ArrayList<Favorite>>() {
        @Override
        public void onChanged(ArrayList<Favorite> newData) {
            if (newData != null) {
                recyclerViewAdapter.changeData(newData);
            }else{
                throw new IllegalArgumentException("newData should not be null");
            }
        }
    };

    private AddFragmentDialogManager dialogManager;

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        AppContainer appContainer = MyApplication.getInstance().appContainer;
        ViewModelProvider.Factory factory = new AppContainerViewModelFactory(appContainer);
        addViewModel = new ViewModelProvider(this, factory).get(AddViewModel.class);

        dialogManager = new AddFragmentDialogManager(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add_to_favorite){
            Toast.makeText(getContext(), "Add to favorite", Toast.LENGTH_SHORT).show();
            DialogFragment dialog = dialogManager.getAddFavoriteDialogFragment();
            dialog.show(getFragmentManager(), "AddFavoriteDialog");

            return true;
        }else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        Toolbar toolbar = view.findViewById(R.id.addFrag_toolbar_top);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        RecyclerView recyclerView = view.findViewById(R.id.addFrag_recyclerview_favorite);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Favorite> data = new ArrayList<>();

        recyclerViewAdapter = new FavoriteRecyclerViewAdapter(getContext(), data, this, dialogManager);
        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        addViewModel.getFavorites().observe(this, loadingDataObserver);

        Button ocrButton = view.findViewById(R.id.addFrag_button_ocr);
        ocrButton.setOnClickListener(this);

        Button sttButton = view.findViewById(R.id.addFrag_button_stt);
        sttButton.setOnClickListener(this);

        Button manualButton = view.findViewById(R.id.addFrag_button_manual);
        manualButton.setOnClickListener(this);
    }

    // 즐겨 찾기 추가 버튼 누르면 나오는 다이얼로그 결과
    @Override
    public void onDialogPositiveClick(int requestCode, String name, String expiryDate, StoredType storedType) {
        switch (requestCode){
            case AddFragmentDialogManager.FAVORITE_REQUEST:
                Favorite newData = new Favorite(name, storedType);
                addViewModel.insertFavorite(newData);
                Toast.makeText(getContext(), "Add favorite return " + newData, Toast.LENGTH_SHORT).show();
                break;
            case AddFragmentDialogManager.BASKET_REQUEST:
                Toast.makeText(getContext(), "Got " + name + " " + expiryDate + " " + storedType, Toast.LENGTH_SHORT).show();
                BasketItem newBasketItem = new BasketItem(name, expiryDate, storedType);
                addViewModel.insertBasketItem(newBasketItem);
                break;
            default:
                throw new IllegalArgumentException("Not supported requestCode: " + requestCode);
        }
    }

    @Override
    public void onDialogNegativeClick(int requestCode) {
        Toast.makeText(getContext(), "Add favorite return no with requestCode: " + requestCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeletedClicked(Favorite clickedFavorite) {
        addViewModel.deleteByName(clickedFavorite.name);
    }

    @Override
    public void onStoredChanged(Favorite changedFavorite) {
        addViewModel.updateFavorite(changedFavorite);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addFrag_button_ocr){
            Toast.makeText(getContext(), "Add new OCR", Toast.LENGTH_SHORT).show();
        }else if (v.getId() == R.id.addFrag_button_stt){
            Toast.makeText(getContext(), "Add new STT", Toast.LENGTH_SHORT).show();
        }else if (v.getId() == R.id.addFrag_button_manual){
            DialogFragment dialog = dialogManager.getAddManualDialogFragment();
            dialog.show(dialogManager.getFragmentManager(), "ManualInputDialog");
            Toast.makeText(getContext(), "Add new Manual", Toast.LENGTH_SHORT).show();
        }else{
            throw new IllegalArgumentException("There is no view matching supported id");
        }

    }
}
