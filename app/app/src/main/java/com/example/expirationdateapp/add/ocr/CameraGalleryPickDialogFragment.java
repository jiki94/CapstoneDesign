package com.example.expirationdateapp.add.ocr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.example.expirationdateapp.R;
import com.example.expirationdateapp.add.GetType;
import com.example.expirationdateapp.add.ocr.OcrActivity;

import java.io.File;
import java.io.IOException;

// 이미지 뭐로 가져올지 고르는 다이얼로그
// 카메라나 갤러리 지원
public class CameraGalleryPickDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final String key = getString(R.string.key_get_type);

        GetType getType = (GetType) getArguments().getSerializable(key);
        if (getType == null){
            throw new IllegalArgumentException("getType cannot be null");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // TODO: 다이어로그 종류에 맞게 가져오기
        switch (getType){
            case NAME:
                builder.setTitle(R.string.text_ask_name_pic);
                break;
            case EXPIRY_DATE:
                builder.setTitle(R.string.text_ask_expiry_date_pic);
                break;
            default:
                throw new IllegalArgumentException("Not Allowed Enum value");
        }

        builder.setPositiveButton(R.string.button_get_gallery, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    getActivity().startActivityForResult(intent, OcrActivity.GALLERY_REQUEST_CODE);
                }else{
                    // 갤러리 없을 경우 실행 코드 아마도 부모 activity한테 문제 있음 알리고 부모에서 핸들하게
                    // 나중에
                }
            }
        });

        final Context context = getContext();
        builder.setNegativeButton(R.string.button_get_camera, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    File tmpFile = null;
                    try{
                        tmpFile = getCameraTmpFile();
                    }catch (IOException e){
                        Toast.makeText(getContext(), "Failed to make file", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (tmpFile != null) {
                        Uri photoUri = FileProvider.getUriForFile(getContext(), "com.example.expirationdateapp.fileprovider", tmpFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        getActivity().startActivityForResult(intent, OcrActivity.CAMERA_REQUEST_CODE);
                    }
                }else{
                    // 카메라 없을 경우
                }
            }
        });

        return builder.create();
    }

    @NonNull
    private File getAppCacheFile(String fileName) throws IOException{
        File tmpFilePath = new File(getContext().getExternalCacheDir(), "tmp_files");
        tmpFilePath.mkdirs();
        File file = new File(tmpFilePath, fileName);
        file.createNewFile();
        return file;
    }

    @NonNull
    private File getCameraTmpFile() throws IOException{
        return getAppCacheFile("camera.jpg");
    }
}
