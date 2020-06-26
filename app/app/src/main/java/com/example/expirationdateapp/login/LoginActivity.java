package com.example.expirationdateapp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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
                loginModule.login(userID, userPassword, LoginActivity.this);
            }
        });
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("로그인에 실패하였습니다.")
                .setNegativeButton("다시 시도", null)
                .create()
                .show();
    }
}
