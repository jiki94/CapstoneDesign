package com.example.expirationdateapp.recipe;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.expirationdateapp.AppContainer;
import com.example.expirationdateapp.AppContainerViewModelFactory;
import com.example.expirationdateapp.MyApplication;
import com.example.expirationdateapp.R;
import com.example.expirationdateapp.db.RecipeInfo;
import com.example.expirationdateapp.db.RecipeInfoAndAlmost;

import java.util.ArrayList;
import java.util.List;

// 입력, 보기, 레시피, 커뮤니티, 푸드뱅크 관련 중
// 레시피 추천해주는 프래그먼트
public class RecipeFragment extends Fragment implements RecipeListRecyclerViewAdapter.ItemClickedListener {
    private RecipeListViewModel viewModel;

    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

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
        TextView emptyText = view.findViewById(R.id.recipeFrag_text_empty_text);
        RecipeListRecyclerViewAdapter adapter = new RecipeListRecyclerViewAdapter(requireContext(), new ArrayList<>(), this, emptyText);

        RecyclerView recyclerView = view.findViewById(R.id.recipeFrag_recyclerview_recipes);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayout.VERTICAL));

        viewModel.getShowingRecipes().observe(this, recipeInfo -> adapter.changeData(recipeInfo));


        // 검색창 관련
        SearchView searchView = view.findViewById(R.id.recipeFrag_searchview_top);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.changeSearchWord(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null || newText.isEmpty()) {
                    viewModel.changeSearchWord(newText);
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.recipe_list_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_show_disliked){
            Intent intent = new Intent(getContext(), DislikedRecipeActivity.class);
            startActivity(intent);
        }else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    // RecipeListRecyclerViewAdapter.ItemClickedListener 구현
    @Override
    public void onItemClicked(RecipeInfoAndAlmost clickedRecipe) {
        Intent intent = new Intent(requireContext(), RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.SENT_RECIPE_CODE, clickedRecipe.recipeCode);
        startActivity(intent);
    }
}
