package com.example.expirationdateapp.viewing;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.expirationdateapp.db.StoredType;
import com.example.expirationdateapp.db.StoredTypeConverter;

// ViewPager2 에서 어떤 Fragment 보여줄지에 사용
public class ViewTabAdapter extends FragmentStateAdapter {
    public ViewTabAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        ViewCategory category = ViewCategory.getByIndex(position);
        return ViewTabFragment.newInstance(category);
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
