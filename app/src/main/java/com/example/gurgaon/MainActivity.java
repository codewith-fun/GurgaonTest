package com.example.gurgaon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gurgaon.model.UserModel;
import com.example.gurgaon.repository.UserInfoDao;
import com.example.gurgaon.viewmodel.UserViewModel;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private UserViewModel viewModel;
    ProgressDialog progressDialog;
    Button button;
    EditText mob,name;
    TextView prompt,promptAll;
    UserInfoDao userInfoDao;
    List<UserModel> data = new ArrayList<>();
    /*
    *
    * */
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude, longitude;
    TextView txtLat;
    private GpsTracker gpsTracker;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions( this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        viewModel = ViewModelProviders.of(MainActivity.this).get(UserViewModel.class);
        initView();

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        actionWithData();
//        doStuff();

        this.mHandler = new Handler();
        this.mHandler.postDelayed(runnable,40000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            doStuff();
        }
    }

    private final Runnable runnable = () -> {doStuff();};

    private void doStuff() {

        gpsTracker = new GpsTracker(MainActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            txtLat.setText("latitude "+String.valueOf(latitude)+" and "+"longitude "+String.valueOf(longitude));
//            tvLongitude.setText(String.valueOf(longitude));
            writeToFile(txtLat.getText().toString().trim(),MainActivity.this);
        }else{
            gpsTracker.showSettingsAlert();
        }

    }

    private void actionWithData() {
        progressDialog = ProgressDialog.show(MainActivity.this,"Loading....","Please wait",true);
        viewModel.getUAllInfo().observe(this,userModels -> {

//            for (UserModel userModel :userModels){
//                ObserveData(userModel);
//            }
            if (userModels.size()>0){
                data = userModels;
            ObserveData();
            }
//            prompt.setText(userModels.toString()+"\n");
            if (progressDialog !=null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        });
    }

    private void ObserveData() {
        button.setOnClickListener(view -> {
            String mobile, userName, mdata = "";

            mobile = mob.getText().toString().trim();
            userName = name.getText().toString().trim();
            UserModel userModel = new UserModel();


            if (mobile.isEmpty() || userName.isEmpty()
                    || mobile != null || !mobile.equals("")
            || userName != null || !userName.equals("")) {




                if (data != null) {

                    for (UserModel model :data){
                        for(int i=0; i<data.size(); i++){
                            boolean isIt = model.getMob().equals(mobile);
                            Log.d("TAG", "ObserveData: "+isIt);
                            if (isIt){
                                if (model.getMob().equalsIgnoreCase(mobile)){
                                    prompt.setText("Already enrolled next enrol after five minutes or terminate app open again, all data saved in config.txt file ");
                                    prompt.setVisibility(View.VISIBLE);
                                    Log.d("TAG", "Already have:  ");
                                    break;

                                }else {
                                    userModel.setMob(mobile);
                                    userModel.setName(userName);
                                    viewModel.insert(userModel);
                                    prompt.setText("Register" + "\n ");
                                    prompt.setVisibility(View.VISIBLE);
                                    Log.d("TAG", "Added: ");
                                }

                                break;
                            }


                        }
                     }

                  /*  String str = mobile;
                    List<UserModel> strings = Arrays.asList(data);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        boolean allMatch = strings.stream().allMatch(s -> s.equals(str));
                        if (allMatch){
                            Log.d("TAG", "Its matchhhhh: ");
                            prompt.setText("Already");
                            prompt.setVisibility(View.VISIBLE);
                        }else{
                            userModel.setMob(mobile);
                            userModel.setName(userName);
                            viewModel.insert(userModel);
                            prompt.setText("Register" + "\n ");
                            prompt.setVisibility(View.VISIBLE);
                            Log.d("TAG", "Its not matchhhh: ");

                        }
                    }*/
//                    mdata = Arrays.asList(data);
                    promptAll.setText(data.toString());
                }

            }else {
                Toast.makeText(MainActivity.this, "Please attempt all", Toast.LENGTH_SHORT).show();
            }

//            else {
//                userModel.setMob(Integer.parseInt(mobile));
//                userModel.setName(userName);
//                viewModel.insert(userModel);
//                prompt.setText("Register");
//                prompt.setVisibility(View.VISIBLE);
//            }



        });

    }

    private void initView() {
        button = findViewById(R.id.actionLogin);
        mob = findViewById(R.id.mob);
        name = findViewById(R.id.name);
        prompt = findViewById(R.id.msg_prompt);
        promptAll = findViewById(R.id.msg_promptAll);
        txtLat = findViewById(R.id.msg_latlang);
    }

    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }




}