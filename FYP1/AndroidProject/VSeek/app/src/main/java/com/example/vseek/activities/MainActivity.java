package com.example.vseek.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


//import com.example.vseek.model.Video;
//import com.example.vseek.model.VideoList;
import com.bumptech.glide.RequestBuilder;
import com.example.vseek.R;
//import com.example.vseek.model.VideoList;
import com.example.vseek.adapters.ThumbnailListAdapter;
import com.example.vseek.connection.PhpConnection;
import com.example.vseek.models.Video;
import com.example.vseek.models.VideoLabels;
import com.example.vseek.models.VideoList;
import com.example.vseek.permissions.Permissions;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.*;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.media.MediaMetadataRetriever.OPTION_CLOSEST;
import static android.media.MediaMetadataRetriever.OPTION_CLOSEST_SYNC;

public class MainActivity extends AppCompatActivity implements ThumbnailListAdapter.OnThumbnailClickListener, ThumbnailListAdapter.OnPlayBtnClickListener {

    TextView textView;
    VideoList videoList;
    Permissions permissions;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ThumbnailListAdapter thumbnailListAdapter;
    String labelsStr;
    VideoLabels videoLabels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.text);
        videoList=new VideoList();

        permissions=new Permissions();
        boolean perm=permissions.mediaPermissions(this,this);
        if (perm) {
            videoList.queryVideos(MainActivity.this);

            List <Video>listOfVideos = videoList.getVideoList();
            Toast.makeText(getApplicationContext(),""+listOfVideos.size(),Toast.LENGTH_SHORT).show();
            for (int i = 0; i < listOfVideos.size(); i++) {
             //   textView.setText(textView.getText()+listOfVideos.get(i).getName() + "\n");
            }

            initRecyclerView();

        }


    }

    private void initRecyclerView() {

        RecyclerView rcv=findViewById(R.id.recycler_view);
        thumbnailListAdapter=new ThumbnailListAdapter(videoList.getVideoList(),this,this,this);
        rcv.setAdapter(thumbnailListAdapter);
        rcv.setLayoutManager(new GridLayoutManager(this,2));

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onThumbnailClick(int position) {
//        Intent intent=new Intent(MainActivity.this,VideoActivity.class);
//
//        intent.putExtra("uri",videoList.getVideoList().get(position).getUri().toString());

        //startActivity(intent);

//            Uri vfUri = videoList.getVideoList().get(position).getUri();
//    //        File videoFile=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/videos","sample_mpeg4.mp4");
//        File videoFile = new File (videoList.getVideoList().get(position).getUri().toString());
//        Uri videoFileUri=Uri.parse(videoFile.toString());
//        Toast.makeText(getApplicationContext(),""+videoFile.getPath(),Toast.LENGTH_LONG).show();
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        //retriever.setDataSource(videoFile.getAbsolutePath());
//        retriever.setDataSource(getApplicationContext(),vfUri);
//         ArrayList<Bitmap> rev=new ArrayList<Bitmap>();
////
//       MediaPlayer mp = MediaPlayer.create(getBaseContext(), vfUri);
//
//
//        Object frames = mp.getMetrics().get(MediaPlayer.MetricsConstants.FRAMES);
//        Toast.makeText(getApplicationContext(),""+frames,Toast.LENGTH_LONG).show();
//
//        int millis = mp.getDuration();
//        for(int i=1000000;i<millis*1000;i+=1000000){
//            Bitmap bitmap=retriever.getFrameAtTime(i,OPTION_CLOSEST);
//            rev.add(bitmap);
//        }


        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute(position);


//        try {
//            saveFrames(rev);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }


    @Override
    public void onPlayClick(int position) {


        startVideoActivity(position,labelsStr);


    }



    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



    private class AsyncTaskRunner extends AsyncTask<Integer,String,String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(Integer... params) {


            Uri vfUri = videoList.getVideoList().get(params[0]).getUri();
            //        File videoFile=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/videos","sample_mpeg4.mp4");
            File videoFile = new File (videoList.getVideoList().get(params[0]).getUri().toString());
            try {
                InputStream inputStream = getContentResolver().openInputStream(vfUri);
                Log.d("inpstr", "doInBackground: "+inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            File vf= new File(getPath(vfUri));

            Uri videoFileUri=Uri.parse(videoFile.toString());
            //Toast.makeText(getApplicationContext(),""+videoFile.getPath(),Toast.LENGTH_LONG).show();
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            //retriever.setDataSource(videoFile.getAbsolutePath());
            retriever.setDataSource(getApplicationContext(),vfUri);
            ArrayList<Bitmap> rev=new ArrayList<Bitmap>();
//
            MediaPlayer mp = MediaPlayer.create(getBaseContext(), vfUri);


          //  Object frames = mp.getMetrics().get(MediaPlayer.MetricsConstants.FRAMES);
          //  Toast.makeText(getApplicationContext(),""+frames,Toast.LENGTH_LONG).show();

// CODE TO EXTRACT FRAMES
//            int millis = mp.getDuration();
//        for(int i=1000000;i<millis*1000;i+=1000000){
//            Bitmap bitmap=retriever.getFrameAtTime(i,OPTION_CLOSEST);
//            rev.add(bitmap);
//        }

        connectServer(vf);

        PythonConnectionThread listener = new PythonConnectionThread(params[0]);
        Thread thread = new Thread(listener);
        thread.start();
//        try {
//            saveFrames(rev);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
           // Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();

        }



        @Override
        protected void onPreExecute() {
           // progressDialog = ProgressDialog.show(MainActivity.this,
             //       "ProgressDialog",
               //     "Wait for 0 seconds");
            progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Uploading..");
            progressDialog.show();
        }


        @Override
        protected void onProgressUpdate(String... text) {
            //finalResult.setText(text[0]);

        }
    }





    void connectServer(File videoFile){

        PhpConnection phpConnection = new PhpConnection();
        phpConnection.uploadFileToServer(videoFile);


    }






    public class PythonConnectionThread implements Runnable
    {

        int position;
        public PythonConnectionThread(int position) {
            this.position=position;
        }

        @Override
        public void run() {
            System.out.println("Waiting for the client request");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Waiting for response... ", Toast.LENGTH_SHORT).show();
                }
            });


            ServerSocket serverSocket;

            try {
                Socket socket = null;
                serverSocket = new ServerSocket(7766);
               // serverSocket.setReuseAddress(true);
                //serverSocket.bind(new InetSocketAddress(7766));
                //int timeout= serverSocket.getSoTimeout();
               // serverSocket.setSoTimeout(10000);
                //System.out.println("Timeout: "+ timeout);
                socket = serverSocket.accept();

                DataInputStream dis = null;
                dis = new DataInputStream(
                        new BufferedInputStream(socket.getInputStream()));

                labelsStr = dis.readLine();
                System.out.println(labelsStr);
                Gson g = new Gson();

                videoLabels =g.fromJson(labelsStr, VideoLabels.class);
                if (videoLabels!=null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getApplicationContext(), "Labels Recieved!", Toast.LENGTH_LONG).show();

                        }
                    });
                }
                setVideoLabels(videoLabels,position);
                serverSocket.close();
                socket.close();
               // startVideoActivity(position,labelsStr);


            }
            catch (Exception e){
                System.out.println(e);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(getApplicationContext(),"Message: "+videoLabels.getLabels().get(0),Toast.LENGTH_LONG).show();

                }
            });
        }

    }

    private void setVideoLabels(VideoLabels videoLabels, int position) {
        videoList.getVideoList().get(position).setVideoLabels(videoLabels);
        videoList.getVideoList().get(position).setLabeled(true);
    }


    public void startVideoActivity(int pos, String labelsStr){
        Intent intent=new Intent(MainActivity.this,VideoActivity.class);
        Gson gs=new Gson();
        String videoLabelObjsStr= gs.toJson(videoList.getVideoList().get(pos).getVideoLabels());
        intent.putExtra("VideoLabelsObjObjectStr", videoLabelObjsStr);
        intent.putExtra("uri",videoList.getVideoList().get(pos).getUri().toString());
        //intent.putExtra("labelsStr",labelsStr);
        intent.putExtra("duration",videoList.getVideoList().get(pos).getDuration());
        // Toast.makeText(getApplicationContext(), ""+videoLabelObjsStr, Toast.LENGTH_LONG).show();
        startActivity(intent);
    }





    public void saveFrames(ArrayList<Bitmap> saveBitmapList) throws IOException {
        Random r = new Random();
        int folder_id = r.nextInt(1000) + 1;

        String folder = Environment.getExternalStorageDirectory()+"/videos/frames/"+folder_id+"/";
        File saveFolder=new File(folder);
        if(!saveFolder.exists()){
            saveFolder.mkdirs();
        }

        int i=1;
        for (Bitmap b : saveBitmapList){
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

            File f = new File(saveFolder,("frame"+i+".jpg"));

            f.createNewFile();

            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());

            fo.flush();
            fo.close();

            i++;
        }
        Toast.makeText(getApplicationContext(),"Folder id : "+folder_id, Toast.LENGTH_LONG).show();

    }





}
