package com.cabo.portabledoctor;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TestActivity extends AppCompatActivity {
    Button insert;
    TextView error;
    String date;
    EditText result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        MyEditTextDatePicker da = new MyEditTextDatePicker(this, R.id.date);
        insert = findViewById(R.id.insert);
        error = findViewById(R.id.error4);
        date= da.getDate();
        result = findViewById(R.id.result);

        RequestQueue queue = Volley.newRequestQueue(this);

        insert.setOnClickListener(view -> {
            if("".equals(date) || "".equals(result))
                error.setText(getResources().getString(R.string.all_fields));
            else {
                String url = "http://portable-doctor.herokuapp.com/test?";
                StringRequest postRequest = new StringRequest(Request.Method.POST, url, response -> {
                    if(response.equals("Errore"))
                        error.setText(getResources().getString(R.string.already));
                    else
                        finish();
                }, err -> error.setText(getResources().getString(R.string.not_available)));
                queue.add(postRequest);
            }
    });

    }
}
