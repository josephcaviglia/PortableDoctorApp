package com.cabo.portabledoctor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
        login.setOnClickListener(view -> {
            //METTERE DELAY BOTTONE
                String user = username.getText().toString();
                String pass = password.getText().toString();

                String url ="http://portable-doctor.herokuapp.com/utente?email="+user+"&password="+pass;

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        response -> {
                            if(user.equals("")||pass.equals(""))
                                //Toast.makeText(LoginActivity.this, "Inserisci le credenziali", Toast.LENGTH_SHORT).show();
                                error.setText(getResources().getString(R.string.all_fields));
                            else{
                                if(response.equals("Credenziali Errate")){
                                    //Toast.makeText(LoginActivity.this, "Credenziali errate", Toast.LENGTH_SHORT).show();
                                    error.setText(getResources().getString(R.string.incorrect));
                                }else{
                                    Intent intent  = new Intent(getApplicationContext(), HomeActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }, err -> error.setText(getResources().getString(R.string.not_available)));
                queue.add(stringRequest);
        });
    }
}