package com.example.expirationdateapp.recipe;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.expirationdateapp.AppContainer;
import com.example.expirationdateapp.AppContainerViewModelFactory;
import com.example.expirationdateapp.MyApplication;
import com.example.expirationdateapp.R;
import com.example.expirationdateapp.db.RecipeInfo;
import com.example.expirationdateapp.viewing.ViewTabViewModel;

import java.util.ArrayList;
import java.util.List;

// 입력, 보기, 레시피, 커뮤니티, 푸드뱅크 관련 중
// 레시피 추천해주는 프래그먼트
public class RecipeFragment extends Fragment {
    private RecipeListViewModel viewModel;

    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Viewmodel 가져오기
        AppContainer appContainer = MyApplication.getInstance().appContainer;
        ViewModelProvider.Factory factory = new AppContainerViewModelFactory(appContainer);
        viewModel = new ViewModelProvider(this, factory).get(RecipeListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Toolbar 세팅
        Toolbar toolbar = view.findViewById(R.id.recipeFrag_toolbar_top);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        // RecyclerView 세팅
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecipeListRecyclerViewAdapter adapter = new RecipeListRecyclerViewAdapter(requireContext(), new ArrayList<>());

        RecyclerView recyclerView = view.findViewById(R.id.recipeFrag_recyclerview_recipes);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        viewModel.getTopFive().observe(this, new Observer<List<RecipeInfo>>() {
            @Override
            public void onChanged(List<RecipeInfo> recipeInfo) {
                adapter.changeData(recipeInfo);
            }
        });

        // live data 로 세팅하기
        // adapter data 변경
    }
}
