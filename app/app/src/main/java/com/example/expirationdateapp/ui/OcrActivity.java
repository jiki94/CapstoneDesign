package com.example.expirationdateapp.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expirationdateapp.R;
import com.example.expirationdateapp.db.StoredType;
import com.example.expirationdateapp.retrofit.KakaoOcrService;
import com.example.expirationdateapp.retrofit.TextExtractionResponse;
import com.example.expirationdateapp.retrofit.TextRecognizeResponse;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.Math.floor;
import static java.lang.Math.min;

// Ocr 입력 담당하는 액티비티
// 여기서 카카오 vision api 호출함
public class OcrActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int CAMERA_REQUEST_CODE = 0;
    public static final int GALLERY_REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;

    private String keyGetType;
    private GetType getType;
    private EditText resultEditText;
    private Button cropButton;
    private Button addButton;
    private TextView title;
    private TextView cropDesc;
    private CropImageView cropImageView;

    private String name = null;
    private String expiryDate = null;
    private StoredType storedType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_acitivity);

        // Toolbar 세팅
        Toolbar toolbar = findViewById(R.id.ocrAct_toolbar_top);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        keyGetType = getString(R.string.key_get_type);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            throw new IllegalArgumentException("activity must have bundle");
        }

        GetType inputGetType = (GetType) bundle.getSerializable(keyGetType);
        if (inputGetType == null){
            throw new IllegalArgumentException("getType cannot be null");
        }

        name = bundle.getString(getString(R.string.key_name_data));
        storedType = (StoredType) bundle.getSerializable(getString(R.string.key_stored_type));

        resultEditText = findViewById(R.id.ocrAct_edittext_adding);
        addButton = findViewById(R.id.ocrAct_button_add);
        cropButton = findViewById(R.id.ocrAct_button_crop_select);
        title = findViewById(R.id.ocrAct_text_toolbar_title);
        cropDesc = findViewById(R.id.ocrAct_text_crop_desc);
        cropImageView = findViewById(R.id.cropImageView);

        addButton.setOnClickListener(this);

        reset(inputGetType);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ocr_toolbar,  menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_rotate){
            cropImageView.rotateImage(90);
            return true;
        }else if (item.getItemId() == R.id.action_reset){
            cropImageView.resetCropRect();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            cropButton.setEnabled(true);
            cropButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bitmap cropped = cropImageView.getCroppedImage();
                    ocrQuery(cropped);
                }
            });

            switch (requestCode){
                case CAMERA_REQUEST_CODE:
                    File img = null;
                    try{
                        img = getCameraTmpFile();
                    } catch (IOException e){
                        Toast.makeText(this, "Failed to make file", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Bitmap bitmap = BitmapFactory.decodeFile(img.getAbsolutePath());
                    img.delete();

                    cropImageView.setImageBitmap(bitmap);
                    break;
                case GALLERY_REQUEST_CODE:
                    Uri uri = data.getData();
                    cropImageView.setImageUriAsync(uri);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private void startChoosingDialog(){
        Bundle bundle = new Bundle();
        bundle.putSerializable(keyGetType, getType);

        DialogFragment dialogFragment = new CameraGalleryPickDialogFragment();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(), "camera gallery pick");
    }

    // 권한 체크 관련
    // 권한 안쓰면 필요 없을듯
    private boolean checkPermissions(String [] permissions){
        if (permissions == null)
            return true;

        for (String permission : permissions)
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED)
                return false;

        return true;
    }

    // 권한 체크 관련
    // 권한 안쓰면 필요 없을듯
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE){
            boolean allGood = true;
            if (permissions.length == 0){
                Toast.makeText(this, "request interaction interrupted", Toast.LENGTH_SHORT).show();
                allGood = false;
            }else{
                for (int i = 0; i < permissions.length; i++){
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                        allGood = false;
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])){
                            Toast.makeText(this, "need read/write permission to get picture", Toast.LENGTH_SHORT).show();
                        }else{
                            // TODO: snackbar로  (아마도 액션으로 세팅 퍼미션 쪽으로 가게)
                            Toast.makeText(this, "cannot use ocr without access to file permission", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }


            if (allGood){
                startChoosingDialog();
            }else{
                finish();
            }

            return;
        }
    }

    @NonNull
    private File getAppCacheFile(String fileName) throws IOException {
        File tmpFilePath = new File(getExternalCacheDir(), "tmp_files");
        tmpFilePath.mkdirs();
        File file = new File(tmpFilePath, fileName);
        file.createNewFile();
        return file;
    }

    @NonNull
    private File getCameraTmpFile() throws  IOException{
        return getAppCacheFile("camera.jpg");
    }

    private void ocrQuery(Bitmap croppedBitmap){
        final int MAX_FILE_SIZE = 2000000;
        final int MAX_WIDTH = 2048;
        final int MAX_HEIGHT = 2048;

        //  이미지 크기 조정
        int cropWidth = croppedBitmap.getWidth();
        int cropHeight = croppedBitmap.getHeight();
        double scaledWidthMultiplier = cropWidth <= MAX_WIDTH ? 1 : (double) MAX_WIDTH / cropWidth;
        double scaledHeightMultiplier = cropHeight <= MAX_HEIGHT ? 1 : (double) MAX_HEIGHT / cropHeight;
        double smallerScale = min(scaledWidthMultiplier, scaledHeightMultiplier);
        int destWidth = (int) floor(cropWidth * smallerScale);
        int destHeight = (int) floor(cropHeight * smallerScale);
        Log.v("SCALE_SIZE", String.format("before: width %d  height: %d", cropWidth, cropHeight));
        Log.v("SCALE_SIZE", String.format("after : width %d  height: %d", destWidth, destHeight));
        Bitmap bitmap = Bitmap.createScaledBitmap(croppedBitmap, destWidth, destHeight, true);

        // 이미지 파일 크기 조정
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // TODO: 여기
        // 압축 결과 boolean으로 나옴
        // 압축 실패하면 어떻게 해야될까
        // 압축 시간 많아 걸리니까 백그라운드로?
        int quality = 105;
        do {
            outputStream.reset();
            quality -= 5;
            if (quality < 0){
                // TODO: 이미지 파일 크기 사이즈가 너무 큼, 다른 거 사용 추천(아니면 scaled 더 작게) 아니면 더 작은 압축?
                return;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            Log.v("COMPRESSING", "" + quality + " : " + outputStream.size());
        }while(outputStream.size() > MAX_FILE_SIZE);

        byte[] byteArray = outputStream.toByteArray();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kapi.kakao.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MultipartBody.Part imgPart = MultipartBody.Part.createFormData("file", "img.jpg",
                RequestBody.create(MediaType.parse("multipart/form-data"), byteArray));

        final AppCompatActivity activity = this;

        final KakaoOcrService service = retrofit.create(KakaoOcrService.class);
        Call<TextExtractionResponse> call = service.getRects(imgPart, "KakaoAK " + getString(R.string.kakao_rest_api_key));
        call.enqueue(new Callback<TextExtractionResponse>() {
            @Override
            public void onResponse(Call<TextExtractionResponse> call, Response<TextExtractionResponse> response) {
                // 현재 여기서 박스 못찾으면 밑에서 터짐(왜냐면 인풋에 박스가 없어 이거 어떻게 할까?
                // 아마도 여기서 막기 새로 찍기 아니면 다이얼로그 보여주기
                if (response.body().result.boxes.isEmpty()){
                    Toast.makeText(activity, "텍스트 없음, 새로운 사진 사용하세요", Toast.LENGTH_SHORT).show();
                    // TODO: 스낵바로, 액션은 다이얼로그 다시 띄우기
                    return;
                }
                Gson gson = new Gson();
                String jsonString = gson.toJson(response.body().result.boxes);
                Call<TextRecognizeResponse> rcall =
                        service.recognize(imgPart, "KakaoAK " + getString(R.string.kakao_rest_api_key), jsonString);
                rcall.enqueue(new Callback<TextRecognizeResponse>() {
                    @Override
                    public void onResponse(Call<TextRecognizeResponse> call, Response<TextRecognizeResponse> response) {
                        String ret = response.body().result.recognition_words.toString();
                        resultEditText.setText(ret);
                        addButton.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<TextRecognizeResponse> call, Throwable t) {
                        Toast.makeText(activity, "Call Recognize Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<TextExtractionResponse> call, Throwable t) {
                Toast.makeText(activity, "First Call Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ocrAct_button_add) {
            switch (getType) {
                case NAME:
                    name = resultEditText.getText().toString();
                    reset(GetType.EXPIRY_DATE);
                    return;
                case EXPIRY_DATE:
                    // 정보 intent에 넣어서 원래 창으로
                    expiryDate = resultEditText.getText().toString();

                    Intent intent = new Intent();
                    intent.putExtra(getString(R.string.key_name_data), name);
                    intent.putExtra(getString(R.string.key_expiry_data), expiryDate);
                    intent.putExtra(getString(R.string.key_stored_type), storedType);

                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private void reset(GetType getType){
        this.getType = getType;
        cropButton.setEnabled(false);
        addButton.setEnabled(false);
        resultEditText.setText("");

        // 액티비티 뷰 초기화
        switch (getType){
            case NAME:
                title.setText(R.string.text_add_name_title);
                cropDesc.setText(R.string.text_add_name_crop_desc);
                break;
            case EXPIRY_DATE:
                title.setText(R.string.text_add_expiry_date_title);
                cropDesc.setText(R.string.text_add_expiry_date_crop_desc);
                break;
            default:
                throw new IllegalArgumentException("Not Allowed Enum value");
        }

        cropImageView.setImageBitmap(null);

        // 권한 체크
        String [] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE };
        if (checkPermissions(null)){
            startChoosingDialog();
        }else{
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        }
    }
}