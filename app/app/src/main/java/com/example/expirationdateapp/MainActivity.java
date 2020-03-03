package com.example.expirationdateapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements NavigationFragment.NavigationChangedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment startFragment = getAssociatedFragment(NavigationType.VIEW);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainAct_layout_fragment_placeholder, startFragment).commit();
    }

    @Override
    public void onNavigationChanged(NavigationType newNav) {
        Fragment fragment = getAssociatedFragment(newNav);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainAct_layout_fragment_placeholder, fragment).commit();
    }

    private Fragment getAssociatedFragment(NavigationType navType){
        switch (navType){
            case ADD:
                return new AddFragment();
            case VIEW:
                return new ViewFragment();
            case RECIPE:
                return new RecipeFragment();
            case FORUM:
                return new ForumFragment();
            case FOOD_BANK:
                return new FoodBankFragment();
            default:
                throw new RuntimeException("Unsupported " + NavigationType.class.getName());
        }
    }
}
