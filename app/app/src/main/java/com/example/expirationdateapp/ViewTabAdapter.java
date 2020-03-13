package com.example.expirationdateapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

// ViewPager2 에서 어떤 Fragment 보여줄지에 사용
public class ViewTabAdapter extends FragmentStateAdapter {
    ViewTabAdapter(@NonNull Fragment fragment) {
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
