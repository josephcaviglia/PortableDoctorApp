package com.cabo.portabledoctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    Button login;
    TextView error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        error = findViewById(R.id.error);

        RequestQueue queue = Volley.newRequestQueue(this);
        //METTERE DELAY BOTTONE
        login.setOnClickListener(view -> {
            String email = username.getText().toString();
            String pass = password.getText().toString();
            String url ="http://portable-doctor.herokuapp.com/utente?email="+email+"&password="+pass;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
                if(email.equals("")||pass.equals(""))
                    error.setText(getResources().getString(R.string.all_fields));
                        else{
                            if(response.equals("Credenziali Errate")){
                                error.setText(getResources().getString(R.string.incorrect));
                            }else{
                                SharedPreferences preferences = getSharedPreferences("keepLogged", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("remember", "true");
                                editor.putString("email", email);
                                editor.putString("password", pass);
                                editor.apply();
                                Intent intent  = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    }, err -> error.setText(getResources().getString(R.string.not_available)));
            queue.add(stringRequest);
        });
    }

        @Override
        public void onBackPressed() {
            moveTaskToBack(true);
    }
}