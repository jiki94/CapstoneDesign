package com.example.expirationdateapp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.expirationdateapp.R;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements LoginModule.ResponseHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final Button loginButton = (Button) findViewById(R.id.loginButton);
        final TextView registerButton = (TextView) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
//버튼을 클릭했을 때 registerIntent를 통해서 RegisterActivity 실행
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userID = idText.getText().toString();
                final String userPassword = passwordText.getText().toString();

                LoginModule loginModule = new LoginModule(LoginActivity.this);
                loginModule.login(userID, userPassword, 0,LoginActivity.this);
            }
        });
    }

    @Override
    public void onLoginSuccess(String userID, long userToken) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.key_id), userID)
                .putLong(getString(R.string.key_token), userToken)
                .apply();

        Intent intent = new Intent(this, LoginMainActivity.class);
        intent.putExtra("userID", userID);
        this.startActivity(intent);
        finish(); // 뒤로 가기 방지
    }

    @Override
    public void onLoginFailure(String userId, long userToken) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("로그인에 실패하였습니다.")
                .setNegativeButton("다시 시도", null)
                .create()
                .show();
    }
}
