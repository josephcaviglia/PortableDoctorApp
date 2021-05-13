package com.cabo.portabledoctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button logout;
    TextView name, welcome;
    FloatingActionButton add;
    ExtendedFloatingActionButton test, med;
    Animation rotateOpen, rotateClose, fromBottom, toBottom;
    boolean clicked = false;
    boolean patient;
    ListView list;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = findViewById(R.id.add_btn);
        test = findViewById(R.id.test_btn);
        med = findViewById(R.id.med_btn);
        welcome = findViewById(R.id.welcome);
        name = findViewById(R.id.name);
        logout = findViewById(R.id.logout);
        list = (ListView) findViewById(R.id.listView);

        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
        list.setAdapter(adapter);

        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);

        SharedPreferences preferences = getSharedPreferences("keepLogged", MODE_PRIVATE);
        String check = preferences.getString("remember", "");
        String token = preferences.getString("token", "");

        if (!check.equals("true")) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        add.setOnClickListener(view -> onAddButtonClicked());

        test.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), TestActivity.class);
            startActivity(intent);
        });

        med.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), NewDrugActivity.class);
            startActivity(intent);
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://portable-doctor.herokuapp.com/utente?token=" + token;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            JSONObject obj;
            try {
                obj = new JSONObject(response);
                name.setText(obj.getString("nome"));
                patient = obj.getBoolean("pazienteWarfarin");
                JSONObject obj2 = obj.getJSONObject("warfarin");
                double warfarin = obj2.getDouble("valoreMax");
                    if (check.equals("true") && patient && warfarin==-1) {
                        Intent intent = new Intent(getApplicationContext(), SurveyActivity.class);
                        startActivity(intent);
                    }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, err -> welcome.setText(getResources().getString(R.string.not_available)));

        queue.add(stringRequest);

        String url2 = "http://portable-doctor.herokuapp.com/evento/personale?token=" + token;
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2, response -> {
            //JSONParser parser = new JSONParser();
            JSONObject obj;
            try {

                obj = new JSONObject(response);
                //JSONArray jlist = (JSONArray) obj;
                String data = obj.getString("data");
                JSONObject obj2 = obj.getJSONObject("descrizione");
                String medicinale = obj2.getString("medicinale");
                double dosaggio = obj2.getDouble("dosaggio");

                for(int i = 0; i < 10; i++) {
                    arrayList.add("");
                    adapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, err -> welcome.setText(getResources().getString(R.string.not_available)));

        queue.add(stringRequest2);

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

    private void onAddButtonClicked() {
        setVisibility(clicked);
        setAnimation(clicked);
        clicked = !clicked;
    }

    private void setVisibility(boolean clicked) {
        if (!clicked) {
            if(patient)
                test.setVisibility(View.VISIBLE);
            else
                test.setVisibility(View.INVISIBLE);
            med.setVisibility(View.VISIBLE);
        } else {
            test.setVisibility(View.INVISIBLE);
            med.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimation(boolean clicked) {
        if (!clicked) {
            if(patient)
                test.startAnimation(fromBottom);
            med.startAnimation(fromBottom);
            add.startAnimation(rotateOpen);
        } else {
            if(patient)
                test.startAnimation(toBottom);
            med.startAnimation(toBottom);
            add.startAnimation(rotateClose);
        }
    }
}