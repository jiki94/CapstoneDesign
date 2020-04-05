package com.example.expirationdateapp.add.basket;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expirationdateapp.AppContainer;
import com.example.expirationdateapp.MyApplication;
import com.example.expirationdateapp.db.Product;
import com.example.expirationdateapp.R;
import com.example.expirationdateapp.AppContainerViewModelFactory;

import java.util.ArrayList;
import java.util.List;

// 장바구니 보여주는 화면
public class BasketActivity extends AppCompatActivity implements BasketRecyclerViewAdapter.DBRelatedListener, View.OnClickListener {
    private BasketRecyclerViewAdapter adapter;
    private BasketViewModel basketViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        // ViewModel 세팅
        AppContainer container = ((MyApplication) getApplication()).appContainer;
        ViewModelProvider.Factory factory = new AppContainerViewModelFactory(container);
        basketViewModel = new ViewModelProvider(this, factory).get(BasketViewModel.class);

        // RecyclerView 세팅
        RecyclerView recyclerView = findViewById(R.id.basketAct_recyclerview_basket_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new BasketRecyclerViewAdapter(this, new ArrayList<Product>(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        basketViewModel.getBasketItems().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> newData) {
                adapter.setData(newData);
            }
        });

        // 버튼들 세팅
        Button delAll = findViewById(R.id.basketAct_button_delete_all);
        delAll.setOnClickListener(this);

        Button add = findViewById(R.id.basketAct_button_add);
        add.setOnClickListener(this);
    }

    // RecyclerView 에 각각 아이템에서 클릭시 일어나는 이벤트
    // BasketRecyclerViewAdapter.DBRelatedListener 인터페이스 구현
    @Override
    public void onDeletedClicked(Product clicked) {
        basketViewModel.deleteBasketItem(clicked);
    }

    // View.OnClickListener 인터페이스 구현
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.basketAct_button_delete_all)
            basketViewModel.deleteAllItems();
        else if (v.getId() == R.id.basketAct_button_add){
            basketViewModel.moveBasketToProducts();
            finish();
        }
    }
}
