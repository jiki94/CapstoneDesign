package com.example.expirationdateapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

// 저장된 음식 정보 보여주는 프래그먼트
public class ViewFragment extends Fragment {
    public ViewFragment() {
        // Required empty public constructor
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.viewFrag_toolbar_top);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        TabLayout tabLayout = view.findViewById(R.id.viewFrag_tabLayout_stored);
        ViewPager2 viewPager = view.findViewById(R.id.viewFrag_viewpager2_showing);
        viewPager.setAdapter(new ViewTabAdapter(this));
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(StoredTypeConverter.intToStoredType(position).getStringId());
            }
        }).attach();
    }
}
