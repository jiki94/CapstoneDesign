package com.example.expirationdateapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

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
