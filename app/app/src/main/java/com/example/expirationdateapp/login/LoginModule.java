package com.example.expirationdateapp.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class LoginModule {
    @NonNull
    Context context;

    public LoginModule(@NonNull Context context) {
        this.context = context;
    }

    public void login(String userID, String userPassword, ResponseHandler responseHandler){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            //인터넷에 접속한 뒤 Response가 건너오면 Response를 저장할 수 있게해줌
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success){
                        String userID = jsonResponse.getString("userID");
                        String userPassword = jsonResponse.getString("userPassword");
                        responseHandler.onLoginSuccess(userID, userPassword);
                    }
                    else{
                        responseHandler.onLoginFailure(userID, userPassword);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

        };
        LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(loginRequest);
    }

    interface ResponseHandler{
        void onLoginSuccess(String userID, String userPassword);
        void onLoginFailure(String userId, String userPassword);
    }
}
