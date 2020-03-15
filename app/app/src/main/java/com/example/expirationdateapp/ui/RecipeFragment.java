package com.example.expirationdateapp.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.expirationdateapp.R;

// 입력, 보기, 레시피, 커뮤니티, 푸드뱅크 관련 중
// 레시피 추천해주는 프래그먼트
public class RecipeFragment extends Fragment {
    public RecipeFragment() {
        // Required empty public constructor
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
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }
}
