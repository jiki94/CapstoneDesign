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

    public void login(String userID, String userPassword, long userToken, ResponseHandler responseHandler){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            //인터넷에 접속한 뒤 Response가 건너오면 Response를 저장할 수 있게해줌
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    String gotUserID = null;
                    long gotUserToken = 0;
                    boolean success = false;

                    if (jsonResponse.has("userID"))
                        gotUserID = jsonResponse.getString("userID");

                    if (jsonResponse.has("user-token"))
                        gotUserToken = jsonResponse.getLong("user-token");

                    if (jsonResponse.has("success"))
                        success = jsonResponse.getBoolean("success");

                    gotUserToken = 1; // TODO: 테스트용 나중에 지우기
                    if(success && gotUserID != null && gotUserToken != 0){
                        responseHandler.onLoginSuccess(gotUserID, gotUserToken);
                    }
                    else{
                        responseHandler.onLoginFailure(gotUserID, gotUserToken);
                    }
                } catch (Exception e){ //TODO: 나쁜 코드 방식 고치기
                    e.printStackTrace(); //TODO: 나쁜 코드 방식 고치기
                }
            }

        };
        LoginRequest loginRequest = new LoginRequest(userID, userPassword, userToken, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(loginRequest);
    }

    interface ResponseHandler{
        void onLoginSuccess(String userID, long userToken);
        void onLoginFailure(String userId, long userToken);
    }
}
