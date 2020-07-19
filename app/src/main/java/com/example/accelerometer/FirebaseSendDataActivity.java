package com.example.accelerometer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.accelerometer.model.PotholeData;
import com.example.accelerometer.model.PotholeLatLong;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseSendDataActivity extends AppCompatActivity {
    private List<PotholeData> potholeDataList;
    private double[] data_y;
    private List<PotholeLatLong> potholeLatLongList;
    private FirebaseDatabase database ;
    private DatabaseReference myRef ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_send_data);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().getRoot();
        potholeDataList = new ArrayList<>();
        potholeLatLongList = new ArrayList<>();
        readData();

    }
    private void readData() {
        InputStream inputStream = getResources().openRawResource(R.raw.pothole8);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        String line = "";
        try {
            //step over first line
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                //split by  ,
                String tokens[] = line.split(",");
                //read the data
                PotholeData potholeData = new PotholeData();
                potholeData.setSr_no(Integer.parseInt(tokens[0]));
                potholeData.setX_axis(Double.parseDouble(tokens[1]));
                potholeData.setY_axis(Double.parseDouble(tokens[2]));
                potholeData.setZ_axis(Double.parseDouble(tokens[3]));
                potholeData.setLatitude(Double.parseDouble(tokens[4]));
                potholeData.setLongitude(Double.parseDouble(tokens[5]));
                potholeData.setPotholeExist(Integer.parseInt(tokens[6]));

                potholeDataList.add(potholeData);
                Log.d("shreyash","potholeData object:"+potholeData);
                if(Integer.parseInt(tokens[6])==1){
                    PotholeLatLong potholeLatLong = new PotholeLatLong();
                    potholeLatLong.setLatitude(Double.parseDouble(tokens[4]));
                    potholeLatLong.setLongitude(Double.parseDouble(tokens[5]));
                    potholeLatLongList.add(potholeLatLong);
                }




            }

        }
        catch (Exception e){
            Log.d("shreyash","Error reading file:"+e);
            e.printStackTrace();
        }

        for(int i =0;i<potholeLatLongList.size();i++){

            DatabaseReference pushedPostRef = myRef.push();
            String key = pushedPostRef.getKey();
            myRef.child("potholes").child(key).setValue(potholeLatLongList.get(i));

            Log.d("shreyash","sending:"+i+": "+potholeLatLongList.get(i).toString());

        }
        DatabaseReference pushedPostRef = myRef.push();
        String key = pushedPostRef.getKey();
        myRef.child("potholes").child(key).setValue(potholeLatLongList.get(0));

 /*       DatabaseReference pushedPostRef = myRef.push();
        String key = pushedPostRef.getKey();
       myRef.child("potholes").child(key).setValue(potholeLatLongList.get(0));
*/










       /* Log.d("shreyash","first element:"+potholeDataList.get(0).getY_axis());
        data_y = new double[potholeDataList.size()];
        //create temp array of size list and fill it with 0s
        int temp[] = new int[potholeDataList.size()];
        Arrays.fill(temp,0);

        Log.d("shreyash","data_y created with size:"+data_y.length);
        for(int i = 0 ;i<potholeDataList.size();i++){
            data_y[i] = potholeDataList.get(i).getY_axis();
            Log.d("shreyash","data at:"+i+" "+data_y[i]);

        }

        //main logic
        boolean maxPoint, minPoint;
        int setSize = 5;
        for(int i =0;i<potholeDataList.size();i++){
            maxPoint = false;
            minPoint = false;
            Log.d("shreyash","inside i loop index:"+i);
            for(int start = i;start<i+setSize;start++){
                Log.d("shreyash","inside start loop index:"+start);
                if(data_y[start]>=12)
                    maxPoint = true;
                if(data_y[start]<=6)
                    minPoint = true;
                if(maxPoint ==true && minPoint == true){
                    temp[start] = 1;
                    Log.d("shreyash","Max min found at index:"+start);
                }


            }
            if(i == potholeDataList.size()-setSize){
                break;
            }
        }


        //display temp array which will show where is pothole
        Log.d("shreyashh","Display Temp array:");
        for(int i =0;i<temp.length;i++){
            Log.d("shreyash",i+":"+temp[i]);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/





    }
}
