package com.codilar.empattendencetrack;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private TextView addressLabel;
    TextView txtLong, txtLat;

    Button btnStart,btnStop,btnRead,btnClearScreen;

    TextView txtData;

    public static final String FILE_NAME = "Location_log.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //        txtLat = (TextView) findViewById(R.id.txtLong);
        //        txtLong = (TextView) findViewById(R.id.txtLat);


        btnStart = (Button) findViewById(R.id.btn_start);
        btnStop = (Button) findViewById(R.id.btn_stop);
        btnRead = (Button) findViewById(R.id.btn_read);
//        btnClearScreen = (Button) findViewById(R.id.btnClearScreen);

        txtData = (TextView) findViewById(R.id.txtData);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent intent=new Intent(getApplicationContext(),Notification_Receiver.class);
                //PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);


                startService(new Intent(getApplicationContext(),LocationService.class));
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(getApplicationContext(),LocationService.class));
            }
        });



        btnRead.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // write on SD card file data in the text box
                try {
                    File root = new File(String.valueOf(Environment.getExternalStorageDirectory()));
                    if (!root.exists()) {
                        root.mkdirs();
                    }
                    File gpxfile = new File(root, FILE_NAME);
                    FileInputStream fIn = new FileInputStream(gpxfile);
                    BufferedReader myReader = new BufferedReader(
                            new InputStreamReader(fIn));
                    String aDataRow = "";
                    String aBuffer = "";
                    while ((aDataRow = myReader.readLine()) != null) {
                        aBuffer += aDataRow + "\n";
                    }
                    txtData.setText(aBuffer);
                    myReader.close();
                    Toast.makeText(getBaseContext(),
                            "Done reading SD 'Location_Log.txt'",
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }// onClick
        }); // btnReadSDFile

//        btnClearScreen.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                // clear text box
//                txtData.setText("");
//            }
//        }); // btnClearScreen

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.location_details, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        super.onOptionsItemSelected(item);
//
//        switch(item.getItemId())
//        {
//            case R.id.action_details:
//
//                Intent intent = new Intent(getApplicationContext(),DetailsActivity.class);
//                startActivity(intent);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

}
