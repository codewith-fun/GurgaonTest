package com.example.gurgaon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gurgaon.model.UserModel;
import com.example.gurgaon.repository.UserInfoDao;
import com.example.gurgaon.viewmodel.UserViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    private UserViewModel viewModel;
    ProgressDialog progressDialog;
    Button button;
    EditText mob,name;
    TextView prompt;
    UserInfoDao userInfoDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(MainActivity.this).get(UserViewModel.class);
        initView();
        actionWithData();


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
    }
}