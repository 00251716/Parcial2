package com.example.kevin.parcial2.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kevin.parcial2.GameNewsAPI.GamesService;
import com.example.kevin.parcial2.GameNewsAPI.GamesServiceInterface;
import com.example.kevin.parcial2.Persistence.SharedData;
import com.example.kevin.parcial2.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText mEditUsername;
    private EditText mEditPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedData.init(this);
        if (SharedData.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        mEditUsername = findViewById(R.id.email);
        mEditPassword = findViewById(R.id.password);

        Button btnLogIn = findViewById(R.id.email_sign_in_button);
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
            mEditUsername.setError(String.format(getString(R.string.error_min_length),getString(R.string.username), 4));
            mEditUsername.requestFocus();
            return false;
        } else if(mEditPassword.getText().toString().trim().length() < 4) {
            mEditPassword.setError(String.format(getString(R.string.error_min_length),getString(R.string.password), 4));
            mEditPassword.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void startLogin() {
        String username = mEditUsername.getText().toString().trim();
        String password = mEditPassword.getText().toString().trim();
        Gson gson = new GsonBuilder().registerTypeAdapter(String.class, new TokenDeserializer()).create();
        Call<String> loginCall = GamesService.getApiService(gson).login(username, password);
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(R.style.AppTheme_NoActionBar);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        loginCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String[] values = response.body().split(":");
                if (response.isSuccessful() && values[0].equals("token")) {
                    progressDialog.dismiss();
                    SharedData.init(LoginActivity.this);
                    SharedData.setToken(values[1]);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, values[1], Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                t.printStackTrace();
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(LoginActivity.this, "Timed out.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public class TokenDeserializer implements JsonDeserializer<String> {
        @Override
        public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String token = "";

            JsonObject tokenJson = json.getAsJsonObject();
            if (tokenJson != null) {
                if (tokenJson.has("token")) token = "token:" + tokenJson.get("token").getAsString();
                else token = "message:" + tokenJson.get("message").getAsString();
            }

            return token;
        }
    }


}



