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
    RadioButton range;
    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        answer = findViewById(R.id.answer);
        tests = findViewById(R.id.tests);
        range = findViewById(R.id.radio1);
        error = findViewById(R.id.error3);

        SharedPreferences preferences = getSharedPreferences("keepLogged", MODE_PRIVATE);
        String token = preferences.getString("token", "");

        RequestQueue queue = Volley.newRequestQueue(this);

        answer.setOnClickListener(view -> {
            String tests2 = tests.getText().toString();
            if("".equals(tests2))
                error.setText(getResources().getString(R.string.all_fields));
            else {
                double min, max;
                if(range.isChecked()) {
                    min = 1.5;
                    max = 2;
                }
                else {
                    min = 2;
                    max = 3;
                }
                String url = "http://portable-doctor.herokuapp.com/utente?token="+token+"&valMin="+min+"&valMax="+max+"&num="+tests2;
                StringRequest postRequest = new StringRequest(Request.Method.POST, url, response -> {
                    if(response.equals("Errore"))
                        error.setText(getResources().getString(R.string.error));
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