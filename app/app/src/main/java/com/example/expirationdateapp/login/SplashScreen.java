package com.example.expirationdateapp.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expirationdateapp.R;

public class SplashScreen extends AppCompatActivity implements LoginModule.ResponseHandler {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String username = sharedPreferences.getString(getString(R.string.key_id), null);
        long token = sharedPreferences.getLong(getString(R.string.key_token), 0);

//        username = "aaa";
//        token = 1;
        if (username == null || token == 0){
            Log.d("SPLASH_SCREEN", "user is null");
            onLoginFailure(null, 0);
        }else{
            Log.d("SPLASH_SCREEN", "found token");
            LoginModule loginModule = new LoginModule(this);
            loginModule.login(username, null, token, this);
        }
    }

    @Override
    public void onLoginSuccess(String userID, long userToken) {
        Log.d("SPLASH_SCREEN", "login success");
        Intent intent = new Intent(this, LoginMainActivity.class);
        intent.putExtra("userID", userID);
        this.startActivity(intent);
        finish(); // 뒤로 가기 방지
    }

    @Override
    public void onLoginFailure(String userId, long userToken) {
        Log.d("SPLASH_SCREEN", "login fail");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(getString(R.string.key_id))
            .remove(getString(R.string.key_token))
            .apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
