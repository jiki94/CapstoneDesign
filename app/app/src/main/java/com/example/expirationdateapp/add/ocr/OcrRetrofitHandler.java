package com.example.expirationdateapp.add.ocr;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.expirationdateapp.R;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OcrRetrofitHandler {
    private KakaoOcrService service;
    @NonNull private OcrResponseHandler ocrResponseHandler;
    final String authKey;

    public OcrRetrofitHandler(@NonNull Context context, @NonNull OcrResponseHandler ocrResponseHandler){
        this.ocrResponseHandler = ocrResponseHandler;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kapi.kakao.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(KakaoOcrService.class);

        authKey = "KakaoAK " + context.getString(R.string.kakao_rest_api_key);
    }

    public void send(byte[] byteArray){
        MultipartBody.Part imgPart = MultipartBody.Part.createFormData("file", "img.jpg",
                RequestBody.create(MediaType.parse("multipart/form-data"), byteArray));

        // 텍스트 상자 감지 시도
        Call<TextExtractionResponse> call = service.getRects(imgPart, authKey);
        call.enqueue(new Callback<TextExtractionResponse>()
        {
            @Override
            public void onResponse(Call < TextExtractionResponse > call, Response< TextExtractionResponse > response){
                ocrResponseHandler.onExtractTextAreaSuccess(call, response);

                // 감지된 텍스트 상자로 텍스트 인식 시도
                Gson gson = new Gson();
                String jsonString = gson.toJson(response.body().result.boxes);
                Call<TextRecognizeResponse> rcall =
                        service.recognize(imgPart, authKey, jsonString);
                rcall.enqueue(new Callback<TextRecognizeResponse>() {
                    @Override
                    public void onResponse(Call<TextRecognizeResponse> call, Response<TextRecognizeResponse> response) {
                        ocrResponseHandler.onRecognizeTextSuccess(call, response);
                    }

                    @Override
                    public void onFailure(Call<TextRecognizeResponse> call, Throwable t) {
                        ocrResponseHandler.onRecognizeTextFailure(call, t);
                    }
                });
            }

            @Override
            public void onFailure (Call < TextExtractionResponse > call, Throwable t){
                ocrResponseHandler.onExtractTextAreaFailure(call, t);
            }
        });
    }

    public interface OcrResponseHandler{
        // 텍스트 상자 감지 성공시 호출하는 함수
        // 이 함수 실행 다음에 얻은 상자로 바로 텍스트 인식 시도
        void onExtractTextAreaSuccess(Call < TextExtractionResponse > call, Response< TextExtractionResponse > response);

        // 텍스트 상자 감지 실패시 호출하는 함수
        void onExtractTextAreaFailure(Call < TextExtractionResponse > call, Throwable t);

        // 텍스트 인식 성공시 호출하는 함수
        void onRecognizeTextSuccess(Call<TextRecognizeResponse> call, Response<TextRecognizeResponse> response);

        // 텍스트 인식 실패시 호출하는 함수
        void onRecognizeTextFailure(Call<TextRecognizeResponse> call, Throwable t);
    }
}
