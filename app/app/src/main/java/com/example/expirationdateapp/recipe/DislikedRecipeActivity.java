package com.example.expirationdateapp.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expirationdateapp.AppContainerViewModelFactory;
import com.example.expirationdateapp.MyApplication;
import com.example.expirationdateapp.R;
import com.example.expirationdateapp.db.RecipeInfo;

import java.util.ArrayList;
import java.util.List;

public class DislikedRecipeActivity extends AppCompatActivity implements RecipeListRecyclerViewAdapter.ItemClickedListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recipe);

        SearchView searchView = findViewById(R.id.recipeFrag_searchview_top);
        searchView.setVisibility(View.GONE);
        Toolbar toolbar = findViewById(R.id.recipeFrag_toolbar_top);
        toolbar.setTitle(R.string.text_disliked_title);

        // viewmodel 초기화
        AppContainerViewModelFactory factory = new AppContainerViewModelFactory(MyApplication.getInstance().appContainer);
        DislikedListViewModel viewModel = new ViewModelProvider(this, factory).get(DislikedListViewModel.class);

        // 리사이클뷰 초기화
        TextView emptyText = findViewById(R.id.recipeFrag_text_empty_text);
        RecipeListRecyclerViewAdapter adapter = new RecipeListRecyclerViewAdapter(this, new ArrayList<>(), this, emptyText);

        RecyclerView recyclerView = findViewById(R.id.recipeFrag_recyclerview_recipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        viewModel.getDislikedRecipes().observe(this, new Observer<List<RecipeInfo>>() {
            @Override
            public void onChanged(List<RecipeInfo> recipeInfo) {
                adapter.changeData(recipeInfo);
            }
        });
    }

    // RecipeListRecyclerViewAdapter.ItemClickedListener 구현
    @Override
    public void onItemClicked(RecipeInfo clickedRecipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.SENT_RECIPE_CODE, clickedRecipe.recipeCode);
        intent.putExtra(RecipeDetailActivity.DEFAULT_DISLIKED_STATE, true);
        startActivity(intent);
    }
}
