package com.example.expirationdateapp;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

// AddFragment 에서 사용되는 다이얼로그 관련 클래스
class AddFragmentDialogManager {
    // callingFragment 는 다이얼로그 생성하고 결과 리터한는 Fragment
    private Fragment callingFragment;

    static final int FAVORITE_REQUEST = 1;
    static final int BASKET_REQUEST = 2;

    AddFragmentDialogManager(Fragment callingFragment){
        this.callingFragment = callingFragment;
    }

    FragmentManager getFragmentManager(){
        return callingFragment.getFragmentManager();
    }

    // 기본 값 있는 수동 입력 다이얼로그 리턴한다.
    DialogFragment getAddManualDialogFragment(String givenName, String givenExpiryDate, StoredType givenStoredType){
        NESDialogFragment dialog = new NESDialogFragment.Builder()
                .setDefaultName(givenName)
                .setDefaultExpiryDate(givenExpiryDate)
                .setDefaultStoredType(givenStoredType)
                .build();

        dialog.setTargetFragment(callingFragment, BASKET_REQUEST);
        return dialog;
    }

    // 기본 값 없는 수동 입력 다이얼로그 리턴한다.
    DialogFragment getAddManualDialogFragment(){
        return getAddManualDialogFragment(null, null, null);
    }

    // 즐겨찾기 추가 다이얼로그 리턴한다.
    DialogFragment getAddFavoriteDialogFragment(){
        NESDialogFragment dialog = new NESDialogFragment.Builder().setUsingExpiryDate(false).build();
        dialog.setTargetFragment(callingFragment, FAVORITE_REQUEST);
        return dialog;
    }
}
