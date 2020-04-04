package com.example.expirationdateapp.add.ocr;

import com.example.expirationdateapp.add.ocr.TextExtractionResponse;
import com.example.expirationdateapp.add.ocr.TextRecognizeResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface KakaoOcrService {
    @Multipart
    @POST("v1/vision/text/detect")
    Call<TextExtractionResponse> getRects(
            @Part MultipartBody.Part filePart,
            @Header("Authorization") String auth
    );

    @Multipart
    @POST("v1/vision/text/recognize")
    Call<TextRecognizeResponse> recognize(
            @Part MultipartBody.Part filePart,
            @Header("Authorization") String auth,
            @Query("boxes") String boxes
    );
}
