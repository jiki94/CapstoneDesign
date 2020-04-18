package com.example.expirationdateapp.viewing;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.expirationdateapp.AppContainer;
import com.example.expirationdateapp.AppContainerViewModelFactory;
import com.example.expirationdateapp.MyApplication;
import com.example.expirationdateapp.R;
import com.example.expirationdateapp.db.StoredTypeConverter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

// 입력, 보기, 레시피, 커뮤니티, 푸드뱅크 관련 중
// 보기, 저장된 음식 정보 보여주는 프래그먼트
public class ViewFragment extends Fragment {
    private ViewTabViewModel viewModel;

    public ViewFragment() {
        // Required empty public constructor
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        AppContainer appContainer = MyApplication.getInstance().appContainer;
        ViewModelProvider.Factory factory = new AppContainerViewModelFactory(appContainer);
        viewModel = new ViewModelProvider(this, factory).get(ViewTabViewModel.class);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.view_toolbar, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_sort_by_name){
            viewModel.setSortFlag(SortingType.SORT_BY_NAME);
        }else if (item.getItemId() == R.id.action_sort_by_name_reverse){
            viewModel.setSortFlag(SortingType.SORT_BY_NAME_REVERSE);
        }else if (item.getItemId() == R.id.action_sort_by_expiry_date){
            viewModel.setSortFlag(SortingType.SORT_BY_EXPIRY_DATE);
        }else if (item.getItemId() == R.id.action_sort_by_expiry_date_reverse){
            viewModel.setSortFlag(SortingType.SORT_BY_EXPIRY_DATE_REVERSE);
        }else{
            // 다른 경우에는 내가 처리 안함
            return super.onOptionsItemSelected(item);
        }

        return true;
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

        SearchView searchView = view.findViewById(R.id.viewFrag_search_view_top);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.setFilterString(newText);
                return true;
            }
        });

        // Tablayout, ViewPager2 세팅
        TabLayout tabLayout = view.findViewById(R.id.viewFrag_tabLayout_stored);
        ViewPager2 viewPager = view.findViewById(R.id.viewFrag_viewpager2_showing);
        viewPager.setAdapter(new ViewTabAdapter(this));
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(ViewCategory.getByIndex(position).name());
            }
        }).attach();

        // TODO: RecyclerView -> filter sort
        // TODO: get sort data from viewtabfragment
    }
}
