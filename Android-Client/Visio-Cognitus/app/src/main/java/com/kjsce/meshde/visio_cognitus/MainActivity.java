package com.kjsce.meshde.visio_cognitus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQ = 8080;
    ImageView imview;
    Bitmap captured;
    Bitmap newpic;
    EditText ip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imview = (ImageView) findViewById(R.id.img);
        ip = (EditText) findViewById(R.id.iptxt);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String IP = ip.getText().toString();
                Thread t = new Thread(){
                    @Override
                    public void run(){
                        try {
                            Socket s  = new Socket(IP,8080);
                            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                            DataInputStream dis = new DataInputStream(s.getInputStream());
                            //String pic = getStringFromBitmap(captured);
                            //dos.writeUTF(Integer.toString(pic.length()));
                            byte[] pic = getByteArrayFromBitmap(captured);
                            System.out.println(pic.length);
                            dos.writeUTF(Integer.toString(pic.length));
                            dos.flush();
                            dos.write(pic,0,pic.length);
                            dos.flush();
                            //int x = dis.read(res);
                            /*for(int i=0;i<res.length;i++){
                                System.out.println(res[i]);
                            }*/

                            //System.out.println(x);
                            byte[] res = getPicture(dis);
                            newpic = getBitmapFromByteArray(res);
                            System.out.println(newpic.getByteCount());
                            s.close();
                            handler.sendEmptyMessage(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                t.start();
                Toast.makeText(view.getContext(), "The message has been sent", Toast.LENGTH_SHORT).show();
            }
        });
        FloatingActionButton cam = (FloatingActionButton) findViewById(R.id.cam);
        cam.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent,CAMERA_REQ);

            }
        });
    }

    private Handler handler = new Handler(){
      public void handleMessage(Message m){
          super.handleMessage(m);
          System.out.println("Here now");
          imview.setImageBitmap(newpic);
      }
    };

    @Override
    protected void onActivityResult(int req,int res,Intent data){
        if(req == CAMERA_REQ && res == Activity.RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            captured = photo;
            imview.setImageBitmap(photo);
        }

    }

    protected String getStringFromBitmap(Bitmap img){
        byte[] b = getByteArrayFromBitmap(img);
        String encoded = Base64.encodeToString(b,Base64.DEFAULT);
        return encoded;
    }

    protected byte[] getByteArrayFromBitmap(Bitmap img){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] b = baos.toByteArray();
        return b;
    }

    protected Bitmap getBitmapFromByteArray(byte[] res){
        Bitmap bmp = BitmapFactory.decodeByteArray(res, 0, res.length);
        return bmp;
    }

    public byte[] getPicture(DataInputStream in) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int length = 0;
            while ((length = in.read(data))!=-1) {
                out.write(data,0,length);
            }
            return out.toByteArray();
        } catch(IOException ioe) {
            //handle it
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
