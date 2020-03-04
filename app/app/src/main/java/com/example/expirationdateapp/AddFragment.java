package com.example.expirationdateapp;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;


// 식품 정보 입려하는 프레그먼트
public class AddFragment extends Fragment {


    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        Toolbar toolbar = view.findViewById(R.id.addFrag_toolbar_top);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        RecyclerView recyclerView = view.findViewById(R.id.addFrag_recyclerview_favorite);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        // 현재 테스트용 임시 데이터 사
        //ArrayList<FavoriteData> data = new ArrayList<>();용
        ArrayList<FavoriteData> data = new ArrayList<>(Arrays.asList(
                new FavoriteData("우유", StoredType.FROZEN),
                new FavoriteData("피자", StoredType.COLD),
                new FavoriteData("물", StoredType.ELSE),
                new FavoriteData("치킨", StoredType.COLD),
                new FavoriteData("배추", StoredType.COLD),
                new FavoriteData("물", StoredType.ELSE),
                new FavoriteData("치킨", StoredType.COLD)
                )
        );
        FavoriteRecyclerViewAdapter adapter = new FavoriteRecyclerViewAdapter(getContext(), data);
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }
}
