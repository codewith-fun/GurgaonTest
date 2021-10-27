package com.example.gurgaon;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gurgaon.model.UserModel;
import com.example.gurgaon.repository.UserInfoDao;
import com.example.gurgaon.viewmodel.UserViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity  {


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
    Button btnGetLocation;
    TextView showLocation;
    LocationManager locationManager;
    String latitude, longitude;
    TextView txtLat;

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
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {

            getLocation();//
        }
    }

    private void actionWithData() {
        progressDialog = ProgressDialog.show(MainActivity.this,"Loading....","Please wait",true);
        viewModel.getUAllInfo().observe(this,userModels -> {
            ObserveData(userModels);
            if (progressDialog !=null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        });
    }

    private void ObserveData(List<UserModel> data) {
//        Log.d("TAG", "users: "+data.toString());
        button.setOnClickListener(view -> {
            UserModel userModel = new UserModel();
            String mobile,userName,mdata;
            mobile = mob.getText().toString().trim();
            userName = name.getText().toString().trim();
            mdata = String.valueOf(data);
                if (!mobile.contains(mdata)) {
                    for (UserModel user : data) {
                        if (mobile.equals(user.getMob())) {
                            Log.d("TAG", "ObserveData: ");
                            prompt.setText("Already exist!");
                            prompt.setVisibility(View.VISIBLE);
                            Log.d("TAG", "ObserveData: Already");
                            return;
                        }else {
                            userModel.setMob(Integer.parseInt(mobile));
                            userModel.setName(userName);
                            prompt.setVisibility(View.GONE);
                            viewModel.insert(userModel);
                            Log.d("TAG", "ObserveData: added ");

                        }
                    }
//
//                    if (userModel.equals(mobile)){
//
//
//                    }else {
//
//
//                    }

                }else {
                    prompt.setText("Already exist!");
                    prompt.setVisibility(View.VISIBLE);
                    Log.d("TAG", "ObserveData: Already");

                }

        });
    }

    private void initView() {
        button = findViewById(R.id.actionLogin);
        mob = findViewById(R.id.mob);
        name = findViewById(R.id.name);
        prompt = findViewById(R.id.msg_prompt);
        txtLat = findViewById(R.id.msg_latlang);
    }


    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                txtLat.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}