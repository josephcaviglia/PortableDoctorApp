package com.cabo.portabledoctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    Button logout;
    TextView name, welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("keepLogged", MODE_PRIVATE);
        String check = preferences.getString("remember", "");
        String email = preferences.getString("email", "");
        String pass = preferences.getString("password", "");

        welcome = findViewById(R.id.welcome);
        name = findViewById(R.id.name);
        logout = findViewById(R.id.logout);

        if(check.equals("false")) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://portable-doctor.herokuapp.com/utente?email="+email+"&password="+pass;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            JSONObject obj = null;
            try {
                obj = new JSONObject(response);
                String sName =  obj.getString("nome")+" "+ obj.getString("cognome");
                name.setText(sName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, err -> welcome.setText(getResources().getString(R.string.not_available)));
        queue.add(stringRequest);



        logout.setOnClickListener(view -> {
            SharedPreferences preferences1 = getSharedPreferences("keepLogged", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences1.edit();
            editor.putString("remember", "false");
            editor.apply();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}