package com.example.vseek.connection;

import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PhpConnection {



    private String getContType(String path) {
        String extension= MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }


    public void uploadFileToServer(File videoFile){
        String content_type= getContType(videoFile.getPath());

        OkHttpClient okHttpClient=new OkHttpClient();

        RequestBody fileBody=RequestBody.create(MediaType.parse("multipart/form-data"),videoFile);

        RequestBody requestBody= new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("type","multipart/form-data")
                .addFormDataPart("uploaded_file","file2.mp4",fileBody)
                .build();

        Request request=new Request.Builder().url("http://192.168.137.1/VideoUpload/upload.php")
                .post(requestBody)
                .build();

        try {
            final Response response = okHttpClient.newCall(request).execute();
            String resp= response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
