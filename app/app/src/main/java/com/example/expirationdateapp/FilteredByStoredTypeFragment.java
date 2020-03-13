package com.example.expirationdateapp;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class FilteredByStoredTypeFragment extends Fragment implements ViewRecyclerViewAdapter.DBRelatedListener {
    public static final String SHOW_KEY = "SHOW_KEY";
    private StoredType show;
    private FilteredByStoredTypeViewModel viewModel;
    private ViewRecyclerViewAdapter adapter;

    public FilteredByStoredTypeFragment(){
        // constructor must be empty
    }

    public static FilteredByStoredTypeFragment newInstance(StoredType show) {
        Bundle args = new Bundle();
        args.putSerializable(SHOW_KEY, show);
        FilteredByStoredTypeFragment fragment = new FilteredByStoredTypeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        show = (StoredType) getArguments().getSerializable(SHOW_KEY);

        AppContainer appContainer = MyApplication.getInstance().appContainer;
        ViewModelProvider.Factory factory = new AppContainerViewModelFactory(appContainer);
        viewModel = new ViewModelProvider(getParentFragment(), factory).get(FilteredByStoredTypeViewModel.class);
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

        viewModel.getItemsByStoredType(show).observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                adapter.changeData(products);
            }
        });
    }

    @Override
    public void onDeletedClicked(Product clicked) {
        viewModel.deleteProduct(clicked);
    }
}
