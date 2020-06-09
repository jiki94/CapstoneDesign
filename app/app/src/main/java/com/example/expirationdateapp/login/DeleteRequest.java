package com.example.expirationdateapp.login;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

//실제 삭제 요청을 보낼 수 있는 클래스, 관리자는 이 클래스와 연동해서 회원 삭제 가능
public class DeleteRequest extends StringRequest {

    final static private String URL = "http://expapp.dothome.co.kr/Delete.php";
    private Map<String, String> parameters;

    public DeleteRequest(String userID, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
