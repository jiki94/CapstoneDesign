package com.example.expirationdateapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

// 화면 밑에 네비게이션 담당
// 5개 버튼이 있는데 해당 버튼들을 누르면 메인 엑티비티에서 해당하는 프레그멘트를 보여줌
public class NavigationFragment extends Fragment implements View.OnClickListener {
    private NavigationChangedListener navListener = null;

    public NavigationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation, container, false);
    }

    @Override
    public void onViewCreated(View view,
                              Bundle savedInstanceState){
        int [] buttonIds = {R.id.navigationFrag_button_add, R.id.navigationFrag_button_view,
                R.id.navigationFrag_button_recipe, R.id.navigationFrag_button_forum,
                R.id.navigationFrag_button_food_bank};

        for (int buttonId : buttonIds){
            Button button = view.findViewById(buttonId);
            button.setOnClickListener(this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof NavigationChangedListener){
            navListener = (NavigationChangedListener) context;
        }else{
            throw new RuntimeException(context.toString() + "must implement"
                    + NavigationChangedListener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (navListener != null)
            navListener = null;
    }

    public interface NavigationChangedListener{
        void onNavigationChanged(NavigationType newNav);
    }

    // View.OnClickListener 인터페이스 구현
    @Override
    public void onClick(View v) {
        if (navListener != null)
            navListener.onNavigationChanged(NavigationType.getFromButtonId(v.getId()));
        else
            throw new RuntimeException("It should be not possible to reach here under normal stance.");
    }
}
