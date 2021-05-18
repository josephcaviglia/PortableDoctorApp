package com.cabo.portabledoctor;

import android.app.TimePickerDialog;
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

public class NewDrugActivity extends AppCompatActivity {

    EditText drug, dose, hour, date;
    TextView error;
    Button insert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_drug);

        MyEditTextDatePicker da = new MyEditTextDatePicker(this, R.id.endDate);
        drug = findViewById(R.id.drug);
        dose = findViewById(R.id.dose);
        hour = findViewById(R.id.hour);
        date = findViewById(R.id.endDate);
        error = findViewById(R.id.error5);
        insert = findViewById(R.id.insert3);

        hour.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(NewDrugActivity.this, (timePicker, hourOfDay, minutes) -> hour.setText(String.format("%02d:%02d",hourOfDay, minutes)), 0, 0, true);
            timePickerDialog.show();
        });

        SharedPreferences preferences = getSharedPreferences("keepLogged", MODE_PRIVATE);
        String token = preferences.getString("token", "");

        RequestQueue queue = Volley.newRequestQueue(this);

        insert.setOnClickListener(view -> {
            String drug2 = drug.getText().toString();
            String date2 = date.getText().toString();
            String hour2 = hour.getText().toString();
            String dose2 = dose.getText().toString();
            String time = date2+"T"+hour2+":00.000Z";

            System.out.println(time);

            if("".equals(date2) || "".equals(drug2) || "".equals(hour2) || "".equals(dose2))
                error.setText(getResources().getString(R.string.all_fields));
            else {
                String url = "http://portable-doctor.herokuapp.com/evento?token="+token+"&data="+time+"&medicinale="+drug2+"&dosaggio="+dose2;
                StringRequest postRequest = new StringRequest(Request.Method.POST, url, response -> {
                    if(response.equals("Data"))
                        error.setText(getResources().getString(R.string.date_error));
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
