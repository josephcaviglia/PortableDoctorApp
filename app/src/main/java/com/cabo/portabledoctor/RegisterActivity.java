package com.cabo.portabledoctor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity  extends AppCompatActivity {
    Button signup;
    EditText email, password, name, surname;
    TextView error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signup = findViewById(R.id.signup);
        name = findViewById(R.id.name2);
        surname = findViewById(R.id.surname2);
        password = findViewById(R.id.password2);
        email = findViewById(R.id.email2);
        error = findViewById(R.id.error2);

        RequestQueue queue = Volley.newRequestQueue(this);
        signup.setOnClickListener(view -> {
            String name2 = name.getText().toString();
            String surname2 = surname.getText().toString();
            String email2 = email.getText().toString();
            String password2 = password.getText().toString();
            if("".equals(email2) || "".equals(password2) || "".equals(name2) || "".equals(surname2))
                error.setText(getResources().getString(R.string.all_fields));
            else {
                String url = "http://portable-doctor.herokuapp.com/utente?nome="+name2+"&cognome="+surname2+"&email="+email2+"&password="+password2;
                StringRequest postRequest = new StringRequest(Request.Method.POST, url, response -> {
                    error.setText(getResources().getString(R.string.success));
                }, err -> error.setText(getResources().getString(R.string.not_available)));
                queue.add(postRequest);
            }
        });
    }
}