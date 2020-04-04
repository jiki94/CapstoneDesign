package com.example.expirationdateapp.add.stt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expirationdateapp.R;
import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SttActivity extends AppCompatActivity {
    static final int PERMISSION_REQUEST_CODE = 1;

    private EditText result;
    private TextView audioLevelText;
    private TextView stateText;
    private TextView candidatesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stt);

        SpeechRecognizerManager.getInstance().initializeLibrary(this);

        SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().
                setServiceType(SpeechRecognizerClient.SERVICE_TYPE_WORD).
                setUserDictionary("아이스크림\n배고파\n초코바\n");

        SpeechRecognizerClient client = builder.build();
        client.setSpeechRecognizeListener(new SpeechRecognizeListener() {
            @Override
            public void onReady() {
                Log.v("STT_TEST", "onReady");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stateText.setText("듣는 중");
                    }
                });
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.v("STT_TEST", "onBeginningOfSpeech");
            }

            @Override
            public void onEndOfSpeech() {
                Log.v("STT_TEST", "onEndOfSpeech");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stateText.setText("듣기 끝");
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                Log.v("STT_TEST", "onError: " + errorMsg);
            }

            @Override
            public void onPartialResult(String partialResult) {
                Log.v("STT_TEST", "onPartialResult");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        result.setText(partialResult);
                    }
                });
            }

            @Override
            public void onResults(Bundle results) {
                Log.v("STT_TEST", "onResults");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<String> texts = results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
                        ArrayList<Integer> confs = results.getIntegerArrayList(SpeechRecognizerClient.KEY_CONFIDENCE_VALUES);
                        result.setText(texts.get(0));

                        StringBuilder sb = new StringBuilder("후보들\n");
                        for (int i = 0; i < texts.size(); i++){
                            sb.append(texts.get(i));
                            sb.append(" --> ");
                            sb.append(confs.get(i));
                            sb.append("\n");
                        }

                        candidatesText.setText(sb.toString());
                    }
                });
            }

            @Override
            public void onAudioLevel(float audioLevel) {
                Log.v("STT_TEST", "onAudioLevel");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        audioLevelText.setText("" + audioLevel);
                    }
                });
            }

            @Override
            public void onFinished() {
                Log.v("STT_TEST", "onFinished");
            }
        });

        Button button = findViewById(R.id.sttAct_button_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("STT_TEST", "startRecording");
                client.startRecording(true);
            }
        });

        audioLevelText = findViewById(R.id.sttAct_text_audio_level);
        result = findViewById(R.id.sttAct_edittext_result);
        stateText = findViewById(R.id.sttAct_text_state);
        candidatesText = findViewById(R.id.sttAct_text_candidates);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            startStt();
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE){
            if (permissions.length > 0){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    startStt();
                }else{
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)){
                        Toast.makeText(this, "Needs to Record Audio to use", Toast.LENGTH_SHORT).show();
                    }else{
                        finish();
                    }
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        Toast.makeText(this, "Needs to write to external storage to use", Toast.LENGTH_SHORT).show();
                    }else{
                        finish();
                    }
                }
            }else{
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SpeechRecognizerManager.getInstance().finalizeLibrary();
    }

    private void startStt(){
    }
}
