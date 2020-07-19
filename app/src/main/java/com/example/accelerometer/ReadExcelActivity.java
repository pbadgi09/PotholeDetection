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

public class ReadExcelActivity extends AppCompatActivity {
    private List<PotholeData> potholeDataList;
    private double[] data_y;
    private double[] data_z;
    private List<PotholeLatLong> potholeLatLongList;
    private FirebaseDatabase database ;
    private DatabaseReference myRef ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_excel);
        potholeLatLongList = new ArrayList<>();
        potholeDataList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().getRoot();
        readData();
    }

    private void readData() {
       InputStream inputStream = getResources().openRawResource(R.raw.pothole23);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        String line = "";
        try {
            //step over first line
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
                Log.d("shreyash","potholeData:"+potholeData);




            }

        }
        catch (Exception e){
            Log.d("shreyash","Error reading file:"+e);
            e.printStackTrace();
        }

        Log.d("shreyash","first element:"+potholeDataList.get(0).getY_axis());
        data_y = new double[potholeDataList.size()];
        data_z = new double[potholeDataList.size()];
        //create temp array of size list and fill it with 0s
        int temp[] = new int[potholeDataList.size()];
        Arrays.fill(temp,0);

        Log.d("shreyash","data_y created with size:"+data_y.length);
        Log.d("shreyash","data_z created with size:"+data_z.length);
        for(int i = 0 ;i<potholeDataList.size();i++){
            data_y[i] = potholeDataList.get(i).getY_axis();
            data_z[i] = potholeDataList.get(i).getZ_axis();
            Log.d("shreyash","data at:"+i+" "+data_y[i]);

        }

        //main logic
        boolean maxPoint, minPoint;
        int setSize = 50;
        for(int i =0;i<potholeDataList.size();i++){
            maxPoint = false;
            minPoint = false;
            Log.d("shreyash","inside i loop index:"+i);
            for(int start = i;start<i+setSize;start++){
                Log.d("shreyash","inside start loop index:"+start);
                if(data_y[start]>=13 &&  data_z[start]>=3)
                    maxPoint = true;
                if(data_y[start]<=7 && data_z[start]<=-2)
                    minPoint = true;


                if(maxPoint ==true && minPoint == true){
                    temp[start] = 1;
                    Log.d("shreyash","Max min found at index:"+start);
                }


            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(i == potholeDataList.size()-setSize){
                break;
            }
        }


        //display temp array which will show where is pothole
        Log.d("shreyashh","Display Temp array:");
        for(int i =0;i<temp.length;i++){
            if(temp[i]==1) {
                Log.d("shreyash Data in temp array", i + ":" + temp[i]);

                DatabaseReference pushedPostRef = myRef.push();
                String key = pushedPostRef.getKey();



                PotholeLatLong potholeLatLong = new PotholeLatLong();
                potholeLatLong.setLatitude(potholeDataList.get(i).getLatitude());
                potholeLatLong.setLongitude(potholeDataList.get(i).getLongitude());
//                potholeLatLongList.add(potholeLatLong);



                myRef.child("potholes").child(key).setValue(potholeLatLong);

                Log.d("shreyash","sending:"+i+": "+potholeLatLong);

            }
            /*try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }






    }
}
