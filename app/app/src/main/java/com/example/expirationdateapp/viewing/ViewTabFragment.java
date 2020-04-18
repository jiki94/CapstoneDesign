package com.example.expirationdateapp.viewing;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expirationdateapp.AppContainer;
import com.example.expirationdateapp.MyApplication;
import com.example.expirationdateapp.db.Product;
import com.example.expirationdateapp.R;
import com.example.expirationdateapp.AppContainerViewModelFactory;

import java.util.ArrayList;
import java.util.List;

// ViewFragment 에서 ViewPager2 에 사용될 Fragment
// 해당 StoredType 에 맞는 등록된 Product 를 보여준다.
public class ViewTabFragment extends Fragment implements ViewRecyclerViewAdapter.DBRelatedListener {
    private static final String SHOW_KEY = "SHOW_KEY";
    private ViewCategory category;
    private ViewTabViewModel viewModel;
    private ViewRecyclerViewAdapter adapter;

    public ViewTabFragment(){
        // constructor must be empty
    }

    public static ViewTabFragment newInstance(ViewCategory category) {
        Bundle args = new Bundle();
        args.putSerializable(SHOW_KEY, category);
        ViewTabFragment fragment = new ViewTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        category = (ViewCategory) getArguments().getSerializable(SHOW_KEY);

        AppContainer appContainer = MyApplication.getInstance().appContainer;
        ViewModelProvider.Factory factory = new AppContainerViewModelFactory(appContainer);
        viewModel = new ViewModelProvider(getParentFragment(), factory).get(ViewTabViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.tabViewFrag_recyclerView_show);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        List<Product> data = new ArrayList<Product>();
        adapter = new ViewRecyclerViewAdapter(getContext(), data, this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));

        viewModel.getItemsByCategory(category).observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                adapter.changeData(products);
            }
        });

        viewModel.getFilterString().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                adapter.setFilterString(s);
            }
        });

        viewModel.getSortFlag().observe(this, new Observer<SortingType>() {
            @Override
            public void onChanged(SortingType sortingType) {
                adapter.setSortFlag(sortingType);
            }
        });
    }

    // ViewRecyclerViewAdapter.DBRelatedListener 인터페이스 구현
    @Override
    public void onDeletedClicked(Product clicked) {
        viewModel.deleteProduct(clicked);
    }
}
