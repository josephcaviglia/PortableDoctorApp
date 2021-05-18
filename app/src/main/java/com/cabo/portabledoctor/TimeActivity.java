package com.cabo.portabledoctor;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class TimeActivity extends AppCompatActivity {
    EditText hour;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        SharedPreferences preferences = getSharedPreferences("keepLogged", MODE_PRIVATE);
        String time = preferences.getString("hour", "");

        hour = findViewById(R.id.time);
        save = findViewById(R.id.save);

        hour.setText(time);

        hour.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(TimeActivity.this, (timePicker, hourOfDay, minutes) -> hour.setText(String.format("%02d:%02d",hourOfDay, minutes)), 0, 0, true);
            timePickerDialog.show();
        });

        save.setOnClickListener(view -> {
            String hour2 = hour.getText().toString();
            SharedPreferences preferences1 = getSharedPreferences("keepLogged", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences1.edit();
            editor.putString("hour", hour2);
            editor.apply();
            finish();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

    }
}