package com.cabo.portabledoctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class TestActivity extends AppCompatActivity {
    Button insert;
    TextView error, date;
    EditText result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        MyEditTextDatePicker da = new MyEditTextDatePicker(this, R.id.date);
        insert = findViewById(R.id.insert);
        error = findViewById(R.id.error4);
        date= findViewById(R.id.date);
        result = findViewById(R.id.result);


        SharedPreferences preferences = getSharedPreferences("keepLogged", MODE_PRIVATE);
        String token = preferences.getString("token", "");

        RequestQueue queue = Volley.newRequestQueue(this);

        insert.setOnClickListener(view -> {
            String result2 = result.getText().toString();
            String date2 = date.getText().toString();

            if("".equals(date2) || "".equals(result2))
                error.setText(getResources().getString(R.string.all_fields));
            else {
                String url = "http://portable-doctor.herokuapp.com/test?token="+token+"&valore="+result2+"&medicinale=Warfarin&data="+date2;
                StringRequest postRequest = new StringRequest(Request.Method.POST, url, response -> {
                    if(response.equals("Fuori intervallo"))
                        error.setText(getResources().getString(R.string.out_of_bounds));
                    else {
                        finish();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }, err -> error.setText(getResources().getString(R.string.not_available)));
                queue.add(postRequest);
            }
    });

    }
}
