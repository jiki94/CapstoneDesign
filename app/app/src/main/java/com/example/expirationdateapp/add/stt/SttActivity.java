package com.example.expirationdateapp.add.stt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Group;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expirationdateapp.AppContainerViewModelFactory;
import com.example.expirationdateapp.MyApplication;
import com.example.expirationdateapp.R;
import com.example.expirationdateapp.add.CalendarDialogFragment;
import com.example.expirationdateapp.add.DateReader;
import com.example.expirationdateapp.add.GetType;
import com.example.expirationdateapp.db.LocalDateConverter;
import com.example.expirationdateapp.db.StoredType;
import com.example.expirationdateapp.db.SttFoodData;
import com.google.android.material.snackbar.Snackbar;
import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public class SttActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    static final int PERMISSION_REQUEST_CODE = 1;

    private GetType getType;
    private String name = null;
    private LocalDate expiryDate2 = null;
    private StoredType storedType = null;

    private TextView title;
    private TextView listeningState;
    private TextView sttDesc;

    private Button add;
    private Button retry;

    private EditText result;

    private Group groupResults;
    private TextView[] textResults;

    private Group groupPredict;
    private TextView predictedExpiry;
    private ImageButton calendarButton;

    private SttViewModel viewModel;

    private SpeechRecognizerClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stt);

        // Toolbar 세팅
        Toolbar toolbar = findViewById(R.id.sttAct_toolbar_top);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 어떤 입력 받는지 확인
        String keyGetType = getString(R.string.key_get_type);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            throw new IllegalArgumentException("activity must have bundle");
        }

        GetType inputGetType = (GetType) bundle.getSerializable(keyGetType);
        if (inputGetType == null){
            throw new IllegalArgumentException("getType cannot be null");
        }

        // ui 세팅
        title = findViewById(R.id.sttAct_text_title);
        listeningState = findViewById(R.id.sttAct_text_listening_state);
        sttDesc = findViewById(R.id.sttAct_text_desc);
        result = findViewById(R.id.sttAct_edittext_input);
        add = findViewById(R.id.sttAct_button_add);
        retry = findViewById(R.id.sttAct_button_retry);
        groupResults = findViewById(R.id.sttAct_group_results);
        textResults = new TextView[5];
        textResults[0] = findViewById(R.id.sttAct_text_res1);
        textResults[1] = findViewById(R.id.sttAct_text_res2);
        textResults[2] = findViewById(R.id.sttAct_text_res3);
        textResults[3] = findViewById(R.id.sttAct_text_res4);
        textResults[4] = findViewById(R.id.sttAct_text_res5);

        groupPredict = findViewById(R.id.sttAct_group_predict_expiry_date);
        predictedExpiry = findViewById(R.id.sttAct_text_predict_expiry_date_result);
        calendarButton = findViewById(R.id.sttAct_button_calandar);

        add.setOnClickListener(this);
        retry.setOnClickListener(this);
        calendarButton.setOnClickListener(this);

        // 기본으로 세팅된 정보 얻기
        name = bundle.getString(getString(R.string.key_name_data));
        storedType = (StoredType) bundle.getSerializable(getString(R.string.key_stored_type));

        // stt 관련
        SpeechRecognizerManager.getInstance().initializeLibrary(this);

        Log.d("VIEWMODEL_TEST", "onCreate");
        ViewModelProvider.Factory factory = new AppContainerViewModelFactory(MyApplication.getInstance().appContainer);
        viewModel = new ViewModelProvider(this, factory).get(SttViewModel.class);

        reset(inputGetType);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE){
            if (permissions.length > 0){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    client.startRecording(true);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sttAct_button_add){
            switch (getType) {
                case NAME:
                    name = result.getText().toString();
                    reset(GetType.EXPIRY_DATE);
                    setClient(null);
                    return;
                case EXPIRY_DATE:
                    // 정보 intent에 넣어서 원래 창으로
                    Intent intent = new Intent();
                    intent.putExtra(getString(R.string.key_name_data), name);
                    intent.putExtra(getString(R.string.key_expiry_data), expiryDate2);
                    intent.putExtra(getString(R.string.key_stored_type), storedType);

                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }else if (v.getId() == R.id.sttAct_button_retry){
            result.setText("");
            retry.setEnabled(false);
            add.setEnabled(false);
            groupResults.setVisibility(View.GONE);

            switch (getType){
                case NAME:
                    sttDesc.setText(R.string.text_stt_name_input_desc);
                    break;
                case EXPIRY_DATE:
                    sttDesc.setText(R.string.text_stt_expiry_date_input_desc);
                    predictedExpiry.setText(new String());
                    break;
                default:
                    throw new IllegalArgumentException("Not Allowed Enum value");
            }

            client.cancelRecording();
            client.startRecording(true);
        }else if (v.getId() == R.id.sttAct_button_calandar){
            DialogFragment dialogFragment = new CalendarDialogFragment(this, this, expiryDate2);
            dialogFragment.show(getSupportFragmentManager(), "Calandar Frag");
        }
    }

    private boolean checkPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
            return false;
        }
    }

    private void reset(GetType getType){
        this.getType = getType;
        result.setText("");
        listeningState.setText(R.string.text_waitiing);
        retry.setEnabled(false);
        add.setEnabled(false);

        groupResults.setVisibility(View.GONE);

        if (getType == GetType.NAME) {
            viewModel.getData().observe(this, new Observer<List<SttFoodData>>() {
                @Override
                public void onChanged(List<SttFoodData> sttFoodData) {
                    Log.d("VIEWMODEL_TEST", "" + sttFoodData.size());
                    List<String> data = new ArrayList<>();
                    for (SttFoodData sfd : sttFoodData) {
                        data.add(sfd.name);
                    }

                    setClient(data);
                    viewModel.getData().removeObserver(this);
                }
            });

            groupPredict.setVisibility(View.GONE);
            result.setInputType(InputType.TYPE_CLASS_TEXT);
            result.setFocusable(true);
        }else{
            setClient(null);
            groupPredict.setVisibility(View.VISIBLE);
            result.setInputType(InputType.TYPE_NULL);
            result.setFocusable(false);
        }
    }

    // extraData는 유저 사전에 추가할 단어들
    // null이면 유저 사전 사용 안함
    private void setClient(List<String> extraData){
        // 타입에 맞게 초기화
        SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder();

        // 유저 사전 추가
        if (extraData != null && !extraData.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < extraData.size(); i++) {
                sb.append(extraData.get(i));
                if (i + 1 != extraData.size())
                    sb.append('\n');
            }
            builder.setUserDictionary(sb.toString());
        }

        switch (getType){
            case NAME:
                // ui 관련
                title.setText(R.string.text_add_name_title);
                sttDesc.setText(R.string.text_stt_name_input_desc);

                // stt 관련
                builder.setServer(SpeechRecognizerClient.SERVICE_TYPE_WORD);

                break;
            case EXPIRY_DATE:
                // ui 관련
                title.setText(R.string.text_add_expiry_date_title);
                sttDesc.setText(R.string.text_stt_expiry_date_input_desc);

                // stt 관련
                builder.setServer(SpeechRecognizerClient.SERVICE_TYPE_WORD);

                break;
            default:
                throw new IllegalArgumentException("Not Allowed Enum value");
        }

        client = builder.build();
        client.setSpeechRecognizeListener(new SpeechRecognizeListener() {
            @Override
            public void onReady() {
                Log.v("STT_TEST", "onReady");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listeningState.setText(R.string.text_listening);
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
                        listeningState.setText(R.string.text_listened);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                Log.v("STT_TEST", "onError: " + errorMsg);
                //TODO: 에러에 맞게 화면 보여주기
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
                        sttDesc.setText(R.string.text_stt_select_result);
                        retry.setEnabled(true);
                        add.setEnabled(true);

                        ArrayList<String> texts = results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
                        ArrayList<Integer> confs = results.getIntegerArrayList(SpeechRecognizerClient.KEY_CONFIDENCE_VALUES);

                        if (getType == GetType.NAME) {
                            groupResults.setVisibility(View.VISIBLE);
                            int smaller = Math.min(texts.size(), 5);
                            for (int i = 0; i < smaller; i++) {
                                final String res = texts.get(i);
                                textResults[i].setText(res);
                                textResults[i].setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        result.setText(res);
                                    }
                                });
                            }
                            for (int i = smaller; i < 5; i++) {
                                textResults[i].setText("");
                                textResults[i].setOnClickListener(null);
                            }

                            if (smaller >= 1)
                                textResults[0].callOnClick();
                        }else{
                            groupResults.setVisibility(View.VISIBLE);
                            int smaller = Math.min(texts.size(), 5);
                            for (int i = 0; i < smaller; i++) {
                                final String res = texts.get(i);
                                textResults[i].setText(res);
                                textResults[i].setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        result.setText(res);
                                        SortedSet<LocalDate> predicted = DateReader.readFromString(res);
                                        if (predicted.isEmpty()){
                                            predictedExpiry.setText(R.string.text_predict_date_fail);
                                            expiryDate2 = null;
                                        }else{
                                            LocalDate latest = predicted.last();
                                            expiryDate2 = latest;
                                            predictedExpiry.setText(LocalDateConverter.localDateToString(latest));
                                            if (LocalDate.now().isAfter(latest)){
                                                View layout = findViewById(android.R.id.content);
                                                Snackbar.make(layout, R.string.snackbar_old_expiry_date, Snackbar.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });
                            }
                            for (int i = smaller; i < 5; i++) {
                                textResults[i].setText("");
                                textResults[i].setOnClickListener(null);
                            }

                            if (smaller >= 1)
                                textResults[0].callOnClick();
                        }
                    }
                });
            }

            @Override
            public void onAudioLevel(float audioLevel) {
                Log.v("STT_TEST", "onAudioLevel");
            }

            @Override
            public void onFinished() {
                Log.v("STT_TEST", "onFinished");
            }
        });

        if (checkPermissions()){
            client.startRecording(true);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        expiryDate2 = LocalDate.of(year, month + 1, dayOfMonth);
        predictedExpiry.setText(LocalDateConverter.localDateToString(expiryDate2));
    }
}
