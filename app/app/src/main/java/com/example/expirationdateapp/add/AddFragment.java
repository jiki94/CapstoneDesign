package com.example.expirationdateapp.add;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.example.expirationdateapp.AppContainer;
import com.example.expirationdateapp.add.basket.BasketActivity;
import com.example.expirationdateapp.add.ocr.OcrActivity;
import com.example.expirationdateapp.add.stt.SttActivity;
import com.example.expirationdateapp.db.Favorite;
import com.example.expirationdateapp.MyApplication;
import com.example.expirationdateapp.db.Product;
import com.example.expirationdateapp.R;
import com.example.expirationdateapp.db.StoredType;
import com.example.expirationdateapp.AppContainerViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

// 입력, 보기, 레시피, 커뮤니티, 푸드뱅크 관련 중
// 입력 하는 프레그먼트
public class AddFragment extends Fragment implements NESDialogFragment.NoticeDialogListener,
        FavoriteRecyclerViewAdapter.DBRelatedListener, View.OnClickListener, AddFavoriteDialogFragment.AddFavoriteResult {
    static int REQUEST_CODE_OCR_ACT = 1;
    static int REQUEST_CODE_STT_ACT = 2;

    private FavoriteRecyclerViewAdapter recyclerViewAdapter;
    private AddViewModel addViewModel;
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
            // getFragmentManager() deprecated 인데 사용됨, 이거 알아봐야 할 듯
            DialogFragment dialog = dialogManager.getAddFavoriteDialogFragment();
            dialog.show(getFragmentManager(), "AddFavoriteDialog");

            // 클릭 이벤트 처리됬으니까
            return true;
        }else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        // Toolbar 세팅
        Toolbar toolbar = view.findViewById(R.id.addFrag_toolbar_top);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        // RecyclerView 세팅
        RecyclerView recyclerView = view.findViewById(R.id.addFrag_recyclerview_favorite);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Favorite> data = new ArrayList<>();
        recyclerViewAdapter = new FavoriteRecyclerViewAdapter(getContext(), data, this, dialogManager, this);
        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        addViewModel.getFavorites().observe(this, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(List<Favorite> favorites) {
                if (favorites != null) {
                    recyclerViewAdapter.changeData(favorites);
                }else{
                    throw new IllegalArgumentException("newData should not be null");
                }
            }
        });

        // 버튼들 세팅
        Button ocrButton = view.findViewById(R.id.addFrag_button_ocr);
        ocrButton.setOnClickListener(this);

        Button sttButton = view.findViewById(R.id.addFrag_button_stt);
        sttButton.setOnClickListener(this);

        Button manualButton = view.findViewById(R.id.addFrag_button_manual);
        manualButton.setOnClickListener(this);

        FloatingActionButton fab = view.findViewById(R.id.addFrag_floatActionBar_basket);
        fab.setOnClickListener(this);
    }

    // NESDialogFragment.NoticeDialogListener 인터페이스 구현
    // 즐겨찾기 추가, 상품 추가 다이얼로그 관련
    @Override
    public void onDialogPositiveClick(String name, LocalDate expiryDate, StoredType storedType) {
        Product newBasketItem = Product.getBasketItem(name, expiryDate, storedType);
        addViewModel.insertBasketItem(newBasketItem);
    }

    @Override
    public void onDialogNegativeClick() {
        Toast.makeText(getContext(), "Add product return no", Toast.LENGTH_SHORT).show();
    }

    // AddFavoriteDialogFragment.AddFavoriteResult 인터페이스 구현
    @Override
    public void onAddFavoritePositiveClick(Favorite newFavorite) {
        addViewModel.insertFavorite(newFavorite);
    }

    @Override
    public void onAddFavoriteNegativeClick() {
        Toast.makeText(getContext(), "Add favorite return no", Toast.LENGTH_SHORT).show();
    }

    // FavoriteRecyclerViewAdapter.DBRelatedListener 인터페이스 구현
    // RecyclerView 에서 각각 아이템 db 관련 처리하는 함수
    @Override
    public void onDeletedClicked(Favorite clickedFavorite) {
        addViewModel.deleteFavoriteByName(clickedFavorite.name);
    }

    @Override
    public void onStoredChanged(Favorite changedFavorite) {
        addViewModel.updateFavorite(changedFavorite);
    }

    @Override
    public void onDefaultExpiryDateChanged(Favorite changedFavorite) {
        addViewModel.updateFavorite(changedFavorite);
    }

    // View.OnClickListener 인터페이스 구현
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addFrag_button_ocr){
//            Toast.makeText(getContext(), "Add new OCR", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), OcrActivity.class);
            intent.putExtra(getString(R.string.key_get_type), GetType.NAME);
            startActivityForResult(intent, REQUEST_CODE_OCR_ACT);
        }else if (v.getId() == R.id.addFrag_button_stt){
//            Toast.makeText(getContext(), "Add new STT", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), SttActivity.class);
            intent.putExtra(getString(R.string.key_get_type), GetType.NAME);
            startActivityForResult(intent, REQUEST_CODE_STT_ACT);
        }else if (v.getId() == R.id.addFrag_button_manual) {
            DialogFragment dialog = dialogManager.getAddManualDialogFragment();
            dialog.show(dialogManager.getFragmentManager(), "ManualInputDialog");
//            Toast.makeText(getContext(), "Add new Manual", Toast.LENGTH_SHORT).show();
        }else if (v.getId() == R.id.addFrag_floatActionBar_basket){
//            Toast.makeText(getContext(), "Float Action Bar Basket", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getContext(), BasketActivity.class);
            startActivity(intent);
        }else{
            throw new IllegalArgumentException("There is no view matching supported id");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            if (requestCode == REQUEST_CODE_OCR_ACT) {
                if (data != null && data.getBooleanExtra("NO_IMAGE", false)) {
                    Snackbar.make(getView(), R.string.no_image_cancel, Snackbar.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_OCR_ACT || requestCode == REQUEST_CODE_STT_ACT) {
                String name = data.getStringExtra(getString(R.string.key_name_data));
                LocalDate expiryDate = (LocalDate) data.getSerializableExtra(getString(R.string.key_expiry_data));
                StoredType storedType = (StoredType) data.getSerializableExtra(getString(R.string.key_stored_type));

                DialogFragment dialog = dialogManager.getAddManualDialogFragment(name, expiryDate, storedType);
                dialog.show(dialogManager.getFragmentManager(), "AutoInputDialog");
            }
        }
    }
}
