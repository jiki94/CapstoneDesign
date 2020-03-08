package com.example.expirationdateapp;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class AddFragmentDialogManager {
    private Fragment callingFragment;

    static final int FAVORITE_REQUEST = 1;
    static final int BASKET_REQUEST = 2;

    AddFragmentDialogManager(Fragment callingFragment){
        this.callingFragment = callingFragment;
    }

    FragmentManager getFragmentManager(){
        return callingFragment.getFragmentManager();
    }

    DialogFragment getAddManualDialogFragment(String givenName, String givenExpiryDate, StoredType givenStoredType){
        NESDialogFragment dialog = new NESDialogFragment.Builder()
                .setDefaultName(givenName)
                .setDefaultExpiryDate(givenExpiryDate)
                .setDefaultStoredType(givenStoredType)
                .build();

        dialog.setTargetFragment(callingFragment, BASKET_REQUEST);
        return dialog;
    }

    DialogFragment getAddManualDialogFragment(){
        return getAddManualDialogFragment(null, null, null);
    }

    DialogFragment getAddFavoriteDialogFragment(){
        NESDialogFragment dialog = new NESDialogFragment.Builder().setUsingExpiryDate(false).build();
        dialog.setTargetFragment(callingFragment, FAVORITE_REQUEST);
        return dialog;
    }
}
