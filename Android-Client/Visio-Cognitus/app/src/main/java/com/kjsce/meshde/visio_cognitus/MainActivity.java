package com.kjsce.meshde.visio_cognitus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQ = 8080;
    ImageView imview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imview = (ImageView) findViewById(R.id.img);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread t = new Thread(){
                    @Override
                    public void run(){
                        try {
                            Socket s  = new Socket("192.168.100.5",8080);
                            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                            dos.writeUTF("Yo Bruh");
                            s.close();
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
    @Override
    protected void onActivityResult(int req,int res,Intent data){
        if(req == CAMERA_REQ && res == Activity.RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imview.setImageBitmap(photo);
        }

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
