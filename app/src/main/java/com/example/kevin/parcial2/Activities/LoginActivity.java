package com.example.kevin.parcial2.Activities;
;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.kevin.parcial2.GameNewsAPI.GamesService;
import com.example.kevin.parcial2.Data.SharedData;
import com.example.kevin.parcial2.ModelsAndEntities.Login;
import com.example.kevin.parcial2.R;
import com.google.gson.Gson;


import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Esta actividad controla el login

public class LoginActivity extends AppCompatActivity{

    private static final String TAG = LoginActivity.class.getSimpleName();

    //El nombre del usuario
    private EditText mEditUsername;

    //La contraseña del usuario
    private EditText mEditPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);

        SharedData.init(this);
        //Verificamos si el usuario ya est� logeado
        if (SharedData.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        //Referencia al EditText donde el usuario ingresa su username
        mEditUsername = findViewById(R.id.username);

        //Referencia al EditText donde el usuario ingresa su password
        mEditPassword = findViewById(R.id.password);

        //Referencia al bot�n para iniciar sesi�n
        Button btnLogIn = findViewById(R.id.log_in_button);

        //Asign�ndole un Listener al bot�n
        btnLogIn.setOnClickListener(v -> buttonClicked());
    }

    private void buttonClicked() {
        if (checkFields()) {
            startLogin();
        }
    }

    private boolean checkFields() {
        mEditUsername.setError(null);
        mEditPassword.setError(null);
        if (mEditUsername.getText().toString().trim().length() < 4) {
            mEditUsername.setError( getString(R.string.error_min_length));
            mEditUsername.requestFocus();
            return false;
        } else if(mEditPassword.getText().toString().trim().length() < 4) {
            mEditPassword.setError(getString(R.string.error_min_length));
            mEditPassword.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void startLogin() {
        String username = mEditUsername.getText().toString().trim();
        String password = mEditPassword.getText().toString().trim();
        Call<Login> loginCall = GamesService.getApiService().token(username, password);
        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                if (response.isSuccessful() && !response.body().getToken().isEmpty()) {
                    SharedData.init(LoginActivity.this);
                    SharedData.setToken(response.body().getToken());
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    try {
                        Gson gson = new Gson();
                        Login error = gson.fromJson(response.errorBody().string(), Login.class);
                        //MessagesUtils.showErrorDialog(LoginActivity.this,error.getMessage(), null);
                        Snackbar.make(findViewById(android.R.id.content), error.getMessage(), Snackbar.LENGTH_LONG)
                                .show();
                    } catch (IOException e) {
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    //MessagesUtils.showErrorDialog(LoginActivity.this,getString(R.string.error_timed_out), null);
                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_timed_out), Snackbar.LENGTH_LONG)
                            .show();
                } else if (t instanceof IOException) {
                    //MessagesUtils.showErrorDialog(LoginActivity.this, getString(R.string.error_no_internet), null);
                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_no_internet), Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            }
        });
    }


}



