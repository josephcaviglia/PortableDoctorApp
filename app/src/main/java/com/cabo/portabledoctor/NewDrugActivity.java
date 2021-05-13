package com.cabo.portabledoctor;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

public class NewDrugActivity extends AppCompatActivity {
    TextView drug, dose, hour;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_drug);

        MyEditTextDatePicker da = new MyEditTextDatePicker(this, R.id.endDate);
        drug = findViewById(R.id.drug);
        dose = findViewById(R.id.dose);
        hour = findViewById(R.id.hour);

        hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(NewDrugActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        hour.setText(String.format("%02d:%02d",hourOfDay, minutes));
                    }
                }, 0, 0, true);
                timePickerDialog.show();
            }
        });
    }
}
