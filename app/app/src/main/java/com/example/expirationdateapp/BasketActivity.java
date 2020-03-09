package com.example.expirationdateapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BasketActivity extends AppCompatActivity implements BasketRecyclerViewAdapter.DBRelatedListener {
    private AppContainer container;
    private BasketRecyclerViewAdapter adapter;
    private BasketViewModel basketViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        container = ((MyApplication) getApplication()).appContainer;
        ViewModelProvider.Factory factory = new AppContainerViewModelFactory(container);
        basketViewModel = new ViewModelProvider(this, factory).get(BasketViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.basketAct_recyclerview_basket_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new BasketRecyclerViewAdapter(this, new ArrayList<BasketItem>(), this);
        recyclerView.setAdapter(adapter);

        basketViewModel.getBasketItems().observe(this, new Observer<List<BasketItem>>() {
            @Override
            public void onChanged(List<BasketItem> newData) {
                adapter.setData(newData);
            }
        });


        Button delAll = findViewById(R.id.basketAct_button_delete_all);
        delAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basketViewModel.deleteAllItems();
            }
        });

        Button add = findViewById(R.id.basketAct_button_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<BasketItem> data = adapter.getData();
                basketViewModel.deleteAllItems();

                // 받은 데이터 가지고 추가하기
                
                finish();
            }
        });
    }

    @Override
    public void onDeletedClicked(BasketItem clicked) {
        basketViewModel.deleteBasketItem(clicked);
    }
}
