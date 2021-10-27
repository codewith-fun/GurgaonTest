package com.example.gurgaon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gurgaon.model.UserModel;
import com.example.gurgaon.repository.UserInfoDao;
import com.example.gurgaon.viewmodel.UserViewModel;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private UserViewModel viewModel;
    ProgressDialog progressDialog;
    Button button;
    EditText mob,name;
    TextView prompt;
    UserInfoDao userInfoDao;

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
        actionWithData();
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        doStuff();

        this.mHandler = new Handler();
        this.mHandler.postDelayed(runnable,40000);

    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            doStuff();
        }
    };

    private void doStuff() {

        gpsTracker = new GpsTracker(MainActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            txtLat.setText("latitude "+String.valueOf(latitude)+" and"+"longitude "+String.valueOf(longitude));
//            tvLongitude.setText(String.valueOf(longitude));
        }else{
            gpsTracker.showSettingsAlert();
        }

    }

    private void actionWithData() {
        progressDialog = ProgressDialog.show(MainActivity.this,"Loading....","Please wait",true);
        viewModel.getUAllInfo().observe(this,userModels -> {

            for (UserModel userModel :userModels){
                ObserveData(userModel);
            }
            prompt.setText(userModels.toString()+"\n");
            if (progressDialog !=null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        });
    }

    private void ObserveData(UserModel data) {
        Log.d("TAG", "users: "+data.getMob());
        button.setOnClickListener(view -> {
            UserModel userModel = new UserModel();
            String mobile,userName,mdata;
            mobile = mob.getText().toString().trim();
            userName = name.getText().toString().trim();
            if (data != null){


            if (mobile.equals(data.getMob())){
                Log.d("TAG", "Yess already: ");
                prompt.setText("Already");
                prompt.setVisibility(View.VISIBLE);
            }else {
                userModel.setMob(Integer.parseInt(mobile));
                userModel.setName(userName);
                viewModel.insert(userModel);
                prompt.setText("Register"+"\n ");
                prompt.setVisibility(View.VISIBLE);
            }
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
        txtLat = findViewById(R.id.msg_latlang);
    }






}