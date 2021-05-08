package com.cabo.portabledoctor;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        RequestQueue queue = Volley.newRequestQueue(this);
        login.setOnClickListener(view -> {
            //METTERE DELAY BOTTONE
            String user = username.getText().toString();
            String pass = password.getText().toString();
            //LEVARE /login
            String url ="http://portable-doctor.herokuapp.com/utente/login?email="+user+"&password="+pass;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> {
                        if(user.equals("")||pass.equals(""))
                            Toast.makeText(LoginActivity.this, "Inserisci le credenziali", Toast.LENGTH_SHORT).show();
                        else{
                            if(response.equals("Credenziali Errate")){
                                Toast.makeText(LoginActivity.this, "Credenziali errate", Toast.LENGTH_SHORT).show();
                            }else{
                                Intent intent  = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                            }
                        }
                    }, error -> System.out.println("That didn't work!"));
            queue.add(stringRequest);
        });
    }
}
