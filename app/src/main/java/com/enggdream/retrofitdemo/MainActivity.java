package com.enggdream.retrofitdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView tvName, tvCompany, tvLocation, tvEmail, tvMessage;
    Button btnGetData;
    EditText etUsername;
    ProgressDialog progressDialog;
    LinearLayout llUserDataContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvCompany = (TextView) findViewById(R.id.tv_company);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        llUserDataContainer = (LinearLayout) findViewById(R.id.ll_user_data_container);
        btnGetData = (Button) findViewById(R.id.btn_get_data);
        etUsername = (EditText) findViewById(R.id.et_username);
        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserData();
            }
        });
    }

    public void getUserData() {
        //Close Keyboard
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        //Set User data container visibility gone
        llUserDataContainer.setVisibility(View.GONE);

        //Setup ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("We are fetching user data.");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String username = etUsername.getText().toString();

        //Make API Call
        RestClient.getInstance().getWebServices().getUser(username).enqueue(
                new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call,
                                           Response<User> response) {
                        if (!MainActivity.this.isFinishing() && progressDialog.isShowing())
                            progressDialog.dismiss();
                        if (response.isSuccessful()) {
                            if (TextUtils.isEmpty(response.body().getMessage())) {
                                tvMessage.setText("");
                                User user = response.body();
                                llUserDataContainer.setVisibility(View.VISIBLE);
                                tvName.setText(user.getName());
                                tvEmail.setText(user.getEmail());
                                tvLocation.setText(user.getLocation());
                                tvCompany.setText(user.getCompany());
                            } else {
                                tvMessage.setText(response.body().getMessage());

                            }
                        } else {
                            tvMessage.setText("Status:" + response.code() + "\nMessage:" +
                                    response.message());
                        }

                    }

                    @Override
                    public void onFailure(Call<User> call,
                                          Throwable t) {
                        if (MainActivity.this.isFinishing() && progressDialog.isShowing())
                            progressDialog.dismiss();
                        tvMessage.setText(t.getLocalizedMessage());
                    }
                });
        progressDialog.show();
    }
}
