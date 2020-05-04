package com.example.expirationdateapp.recipe;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expirationdateapp.AppContainer;
import com.example.expirationdateapp.AppContainerViewModelFactory;
import com.example.expirationdateapp.MyApplication;
import com.example.expirationdateapp.R;
import com.example.expirationdateapp.db.RecipeInfo;
import com.example.expirationdateapp.db.RecipeIngredient;
import com.example.expirationdateapp.db.RecipeProgress;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        int recipeCode = getIntent().getIntExtra(RecipeFragment.SENT_RECIPE_CODE, -1);
        if (recipeCode == -1)
            throw new IllegalArgumentException();

        // Viewmodel 가져오기
        AppContainer appContainer = MyApplication.getInstance().appContainer;
        ViewModelProvider.Factory factory = new AppContainerViewModelFactory(appContainer);
        RecipeDetailViewModel viewModel = new ViewModelProvider(this, factory).get(RecipeDetailViewModel.class);

        final AppCompatActivity activity = this;

        // RecipeInfo 얻어서 하는 초기화
        viewModel.getRecipeInfo(recipeCode).observe(this, new Observer<RecipeInfo>() {
            @Override
            public void onChanged(RecipeInfo recipeInfo) {
                ImageView recipeImg = activity.findViewById(R.id.recipeDetailAct_img_recipe);
                Picasso.get()
                        .load(recipeInfo.mainImgUrl)
                        .placeholder(R.drawable.basket)
                        .into(recipeImg);

                TextView recipeName = activity.findViewById(R.id.recipeDetailAct_text_recipe_name);
                recipeName.setText(recipeInfo.recipeName);
            }
        });

        // 재료 부분 초기화
        final RecyclerView mainRecyclerView = findViewById(R.id.recipeDetailAct_recyclerview_main_ingredients);
        final RecyclerView subRecyclerView = findViewById(R.id.recipeDetailAct_recyclerview_sub_ingredients);
        final RecyclerView seasoningRecyclerView = findViewById(R.id.recipeDetailAct_recyclerview_seasoning_ingredients);

        final Group mainGroup = findViewById(R.id.recipeDetailAct_group_main_ingredients);
        final Group subGroup = findViewById(R.id.recipeDetailAct_group_sub_ingredients);
        final Group seasoningGroup = findViewById(R.id.recipeDetailAct_group_seasoning_ingredients);

        final IngredientRecyclerViewAdapter mainAdapter = new IngredientRecyclerViewAdapter(this);
        final IngredientRecyclerViewAdapter subAdapter = new IngredientRecyclerViewAdapter(this);
        final IngredientRecyclerViewAdapter seasoningAdapter = new IngredientRecyclerViewAdapter(this);

        setRecyclerViewToDefaultMode(mainRecyclerView, mainGroup, mainAdapter);
        setRecyclerViewToDefaultMode(subRecyclerView, subGroup, subAdapter);
        setRecyclerViewToDefaultMode(seasoningRecyclerView, seasoningGroup, seasoningAdapter);

        viewModel.getMainIngredient(recipeCode).observe(this, new Observer<List<RecipeIngredient>>() {
            @Override
            public void onChanged(List<RecipeIngredient> recipeIngredients) {
                setNewData(mainGroup, mainAdapter, recipeIngredients);
            }
        });

        viewModel.getSubIngredient(recipeCode).observe(this, new Observer<List<RecipeIngredient>>() {
            @Override
            public void onChanged(List<RecipeIngredient> recipeIngredients) {
                setNewData(subGroup, subAdapter, recipeIngredients);
            }
        });

        viewModel.getSeasoningIngredient(recipeCode).observe(this, new Observer<List<RecipeIngredient>>() {
            @Override
            public void onChanged(List<RecipeIngredient> recipeIngredients) {
                setNewData(seasoningGroup, seasoningAdapter, recipeIngredients);
            }
        });

        // RecipeProgress 관련
        final RecyclerView progressRecyclerView = findViewById(R.id.recipeDetailAct_recyclerview_progress);
        progressRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        final ProgressRecyclerViewAdapter progressRecyclerViewAdapter = new ProgressRecyclerViewAdapter(this);
        progressRecyclerView.setAdapter(progressRecyclerViewAdapter);
        viewModel.getRecipeProgress(recipeCode).observe(this, new Observer<List<RecipeProgress>>() {
            @Override
            public void onChanged(List<RecipeProgress> recipeProgress) {
                progressRecyclerViewAdapter.setData(recipeProgress);
                Log.v("RECIPE_TEST", "onChanged:progress  data size: " + recipeProgress.size());
            }
        });
    }

    private void setRecyclerViewToDefaultMode(@NonNull RecyclerView recyclerView, @NonNull Group group, RecyclerView.Adapter adapter){
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        group.setVisibility(View.GONE);
    }

    private void setNewData(@NonNull Group group, @NonNull IngredientRecyclerViewAdapter adapter, List<RecipeIngredient> newData){
        if (newData != null && !newData.isEmpty()) {
            group.setVisibility(View.VISIBLE);
            adapter.setData(newData);
        }
    }
}