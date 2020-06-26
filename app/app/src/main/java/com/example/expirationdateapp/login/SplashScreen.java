package com.example.expirationdateapp.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

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

        if (username == null || token == 0){
            onLoginFailure(null, null);
        }else{
            LoginModule loginModule = new LoginModule(this);
            loginModule.login(username, "aaa", this);
        }
    }

    @Override
    public void onLoginSuccess(String userID, String userPassword) {
        Intent intent = new Intent(this, LoginMainActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("userPassword", userPassword);
        this.startActivity(intent);
        finish(); // 뒤로 가기 방지
    }

    @Override
    public void onLoginFailure(String userId, String userPassword) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
