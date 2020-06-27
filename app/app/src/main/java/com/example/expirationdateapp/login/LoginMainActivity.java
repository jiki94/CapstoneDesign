package com.example.expirationdateapp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.expirationdateapp.MainActivity;
import com.example.expirationdateapp.R;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        TextView idText = (TextView) findViewById(R.id.idText);
        TextView welcomeMessage = (TextView) findViewById(R.id.welcomeMessage);
        Button managementButton = (Button) findViewById(R.id.managementButton);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String message = "환영합니다," + userID + "님!";

        idText.setText(userID);
        welcomeMessage.setText(message);

        if(!userID.equals("admin")) //userID가 admin일 경우만 해당 버튼 누를 수 있음
        {
            managementButton.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(LoginMainActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();  // 뒤로 가기 방지
                }
            }, 3000);
        }

        managementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundTask().execute();
            }
        });
    }

    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target; //실제 해당 웹페이지에 접속해서 회원리스트를 가져올 수 있게함

        @Override
        protected void onPreExecute(){
            target = "http://expapp.dothome.co.kr/List.php";
        } //초기화 부분

        @Override
        protected String doInBackground(Void... voids){
            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) !=null)
                {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        } //실질적으로 실행이 되는 부분

        @Override
        public void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);
        }//상속만 받고 사용하지는 않음

        @Override
        public void onPostExecute(String result){
            Intent intent = new Intent(LoginMainActivity.this, ManagementActivity.class);
            intent.putExtra("userList", result);
            LoginMainActivity.this.startActivity(intent);
        }//모든 파싱작업이 끝난 이후에 다음 액티비티로 넘어갈 수 있게해줌

    }
}
