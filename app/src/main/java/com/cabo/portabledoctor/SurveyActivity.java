package com.cabo.portabledoctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class SurveyActivity extends AppCompatActivity {
    Button answer;
    EditText tests;
    RadioButton range1;
    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        answer = findViewById(R.id.answer);
        tests = findViewById(R.id.tests);
        range1 = findViewById(R.id.radio1);
        error = findViewById(R.id.error3);

        SharedPreferences preferences = getSharedPreferences("keepLogged", MODE_PRIVATE);
        String token = preferences.getString("token", "");

        RequestQueue queue = Volley.newRequestQueue(this);

        answer.setOnClickListener(view -> {
            String tests2 = tests.getText().toString();
            if("".equals(tests2))
                error.setText(getResources().getString(R.string.all_fields));
            else {
                boolean check = false;
                if(range1.isChecked())
                    check = true;
                String url = "http://portable-doctor.herokuapp.com/utente?token="+token;
                StringRequest postRequest = new StringRequest(Request.Method.POST, url, response -> {
                    if(response.equals("Errore"))
                        error.setText(getResources().getString(R.string.already));
                    else{
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }, err -> error.setText(getResources().getString(R.string.not_available)));
                queue.add(postRequest);
            }
        });

    }
}
