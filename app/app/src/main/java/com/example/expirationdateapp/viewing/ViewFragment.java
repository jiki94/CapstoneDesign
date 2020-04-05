package com.example.expirationdateapp.viewing;

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

import com.example.expirationdateapp.R;
import com.example.expirationdateapp.db.StoredTypeConverter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

// 입력, 보기, 레시피, 커뮤니티, 푸드뱅크 관련 중
// 보기, 저장된 음식 정보 보여주는 프래그먼트
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
        // Toolbar 세팅
        Toolbar toolbar = view.findViewById(R.id.viewFrag_toolbar_top);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        // Tablayout, ViewPager2 세팅
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
