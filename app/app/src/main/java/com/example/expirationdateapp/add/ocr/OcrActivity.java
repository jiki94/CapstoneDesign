package com.example.expirationdateapp.add.ocr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expirationdateapp.R;
import com.example.expirationdateapp.add.GetType;
import com.example.expirationdateapp.db.LocalDateConverter;
import com.example.expirationdateapp.db.StoredType;
import com.google.android.material.snackbar.Snackbar;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.threeten.bp.LocalDate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import retrofit2.Call;
import retrofit2.Response;

import static java.lang.Math.floor;
import static java.lang.Math.min;

// Ocr 입력 담당하는 액티비티
// 여기서 카카오 vision api 호출함
public class OcrActivity extends AppCompatActivity implements View.OnClickListener, OcrRetrofitHandler.OcrResponseHandler {
    public static final int CAMERA_REQUEST_CODE = 0;
    public static final int GALLERY_REQUEST_CODE = 1;

    private String keyGetType;
    private GetType getType;
    private EditText resultEditText;
    private Button cropButton;
    private Button addButton;
    private Button add2Button;
    private TextView title;
    private TextView cropDesc;
    private CropImageView cropImageView;
    private ImageButton calendar;
    private TextView resultText;
    private EditText predictedDate;

    private Group nameGroup;
    private Group expiryGroup;

    private OcrRetrofitHandler ocrRetrofitHandler;

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

        // 어떤 입력 받는지 확인
        keyGetType = getString(R.string.key_get_type);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            throw new IllegalArgumentException("activity must have bundle");
        }

        GetType inputGetType = (GetType) bundle.getSerializable(keyGetType);
        if (inputGetType == null){
            throw new IllegalArgumentException("getType cannot be null");
        }

        // 기본으로 세팅된 정보 얻기
        name = bundle.getString(getString(R.string.key_name_data));
        storedType = (StoredType) bundle.getSerializable(getString(R.string.key_stored_type));

        // View들 찾고 초기 설정
        resultEditText = findViewById(R.id.ocrAct_edittext_adding);
        addButton = findViewById(R.id.ocrAct_button_add);
        add2Button = findViewById(R.id.ocrAct_button_add2);
        cropButton = findViewById(R.id.ocrAct_button_crop_select);
        title = findViewById(R.id.ocrAct_text_toolbar_title);
        cropDesc = findViewById(R.id.ocrAct_text_crop_desc);
        cropImageView = findViewById(R.id.cropImageView);
        calendar = findViewById(R.id.ocrAct_imgButton_calandar);

        nameGroup = findViewById(R.id.ocrAct_group_name);
        expiryGroup = findViewById(R.id.ocrAct_group_expiry);

        resultText = findViewById(R.id.ocrAct_text_recognized_result);
        predictedDate = findViewById(R.id.ocrAct_edittext_predict);

        addButton.setOnClickListener(this);
        add2Button.setOnClickListener(this);
        cropButton.setOnClickListener(this);
        calendar.setOnClickListener(this);

        // 나머지 잡다한거
        resultText.setMovementMethod(new ScrollingMovementMethod());
        ocrRetrofitHandler = new OcrRetrofitHandler(this, this);

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

    private void reset(GetType getType){
        this.getType = getType;
        cropButton.setEnabled(false);
        addButton.setEnabled(false);
        add2Button.setEnabled(false);
        calendar.setFocusable(false);
        calendar.setClickable(false);

        // 액티비티 뷰 초기화
        switch (getType){
            case NAME:
                title.setText(R.string.text_add_name_title);
                cropDesc.setText(R.string.text_add_name_crop_desc);

                nameGroup.setVisibility(View.VISIBLE);
                expiryGroup.setVisibility(View.GONE);

                resultEditText.setText("");
                break;
            case EXPIRY_DATE:
                title.setText(R.string.text_add_expiry_date_title);
                cropDesc.setText(R.string.text_add_expiry_date_crop_desc);

                nameGroup.setVisibility(View.GONE);
                expiryGroup.setVisibility(View.VISIBLE);
                break;
            default:
                throw new IllegalArgumentException("Not Allowed Enum value");
        }

        cropImageView.setImageBitmap(null);

        startChoosingDialog();
    }

    private void startChoosingDialog(){
        Bundle bundle = new Bundle();
        bundle.putSerializable(keyGetType, getType);

        DialogFragment dialogFragment = new CameraGalleryPickDialogFragment();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(), "camera gallery pick");
    }

    // 임시 파일 얻기
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
        ocrRetrofitHandler.send(byteArray);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ocrAct_button_add) {
            name = resultEditText.getText().toString();
            reset(GetType.EXPIRY_DATE);
        }else if (v.getId() == R.id.ocrAct_button_crop_select){
            Bitmap cropped = cropImageView.getCroppedImage();
            ocrQuery(cropped);
        }else if (v.getId() == R.id.ocrAct_button_add2){
            // 정보 intent에 넣어서 원래 창으로
            expiryDate = resultEditText.getText().toString();

            Intent intent = new Intent();
            intent.putExtra(getString(R.string.key_name_data), name);
            intent.putExtra(getString(R.string.key_expiry_data), expiryDate);
            intent.putExtra(getString(R.string.key_stored_type), storedType);

            setResult(Activity.RESULT_OK, intent);
            finish();
        }else if (v.getId() == R.id.ocrAct_imgButton_calandar){

        }
    }

    // OcrRetrofitHandler.OcrResponseHandler 인터페이스 관련
    @Override
    public void onExtractTextAreaSuccess(Call<TextExtractionResponse> call, Response<TextExtractionResponse> response) {
        // 현재 여기서 박스 못찾으면 밑에서 터짐(왜냐면 인풋에 박스가 없어 이거 어떻게 할까?
        // 아마도 여기서 막기 새로 찍기 아니면 다이얼로그 보여주기
        if (response.body().result.boxes.isEmpty()) {
            Toast.makeText(this, "텍스트 없음, 새로운 사진 사용하세요", Toast.LENGTH_SHORT).show();
            // TODO: 스낵바로, 액션은 다이얼로그 다시 띄우기
        }
    }

    @Override
    public void onExtractTextAreaFailure(Call<TextExtractionResponse> call, Throwable t) {
        Toast.makeText(this, "Failed to extract text area", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecognizeTextSuccess(Call<TextRecognizeResponse> call, Response<TextRecognizeResponse> response){
        String ret = join(" ", response.body().result.recognition_words);
        switch (getType){
            case NAME:
                resultEditText.setText(ret);
                addButton.setEnabled(true);
                break;
            case EXPIRY_DATE:
                resultText.setText(ret);
                SortedSet<LocalDate> predicted = DateReader.readFromString(ret);
                if (predicted.isEmpty()){
                    predictedDate.setText(R.string.text_predict_date_fail);
                }else{
                    LocalDate newest = predicted.last();
                    if (LocalDate.now().isAfter(newest)){
                       // Snackbar로 유통기한 지남 또는 제조일자 인지 확인 알림
                        View layout = findViewById(R.id.ocrAct_layout);
                        Snackbar.make(layout, R.string.snackbar_old_expiry_date, Snackbar.LENGTH_SHORT).show();
                    }
                    predictedDate.setText(LocalDateConverter.localDateToString(predicted.last()));
                }

                add2Button.setEnabled(true);
                calendar.setFocusable(true);
                calendar.setClickable(true);
                break;
            default:
                throw new IllegalArgumentException();
        }

    }

    @Override
    public void onRecognizeTextFailure(Call<TextRecognizeResponse> call, Throwable t) {
        Toast.makeText(this, "Failed to recognize text", Toast.LENGTH_SHORT).show();
    }

    String join(String delimiter, List<String> strings){
        StringBuilder sb = new StringBuilder(strings.get(0));
        for (int i = 1; i < strings.size(); i++)
            sb.append(delimiter).append(strings.get(i));

        return sb.toString();
    }
}
