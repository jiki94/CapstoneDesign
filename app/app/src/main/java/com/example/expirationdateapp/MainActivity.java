package com.example.expirationdateapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.expirationdateapp.add.AddFragment;
import com.example.expirationdateapp.alarm.NotificationSetter;
import com.example.expirationdateapp.foodbank.FoodBankFragment;
import com.example.expirationdateapp.forum.ForumFragment;
import com.example.expirationdateapp.forum.activity.main.ForumMainActivity;
import com.example.expirationdateapp.recipe.RecipeFragment;
import com.example.expirationdateapp.viewing.ViewFragment;

// 위에는 입력, 보기, 레시피, 커뮤니티, 푸드뱅크 Fragment 보여주고
// 밑에는 저거 5개 네비게이션 지원
public class MainActivity extends AppCompatActivity implements NavigationFragment.NavigationChangedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Fragment startFragment = getAssociatedFragment(NavigationType.VIEW);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainAct_layout_fragment_placeholder, startFragment).commit();
        }

        //  안드로이드 O 버전 이상은 Notification channel 등록 필요
        NotificationSetter notificationSetter = new NotificationSetter(this);
        notificationSetter.createNotificationChannel();
    }

    // NavigationFragment.NavigationChangedListener 인터페이스 구현
    // 밑에 선택한 메뉴로 이동
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
                return new ForumMainActivity();
            case FOOD_BANK:
                return new FoodBankFragment();
            default:
                throw new RuntimeException("Unsupported " + NavigationType.class.getName());
        }
    }
}
