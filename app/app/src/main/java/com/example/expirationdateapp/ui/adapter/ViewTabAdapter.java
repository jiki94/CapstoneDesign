package com.example.expirationdateapp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.expirationdateapp.db.StoredType;
import com.example.expirationdateapp.db.StoredTypeConverter;
import com.example.expirationdateapp.ui.FilteredByStoredTypeFragment;

// ViewPager2 에서 어떤 Fragment 보여줄지에 사용
public class ViewTabAdapter extends FragmentStateAdapter {
    public ViewTabAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        StoredType show = StoredTypeConverter.intToStoredType(position);
        return FilteredByStoredTypeFragment.newInstance(show);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
