package com.cabo.portabledoctor;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
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
import androidx.core.app.NotificationCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";
    Button logout;
    TextView name, welcome;
    FloatingActionButton add, refresh;
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
        list = findViewById(R.id.listView);
        refresh = findViewById(R.id.refresh);

        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.row, arrayList);
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

        refresh.setOnClickListener(view -> refresh(token));

        test.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), TestActivity.class);
            startActivity(intent);
        });

        med.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), NewDrugActivity.class);
            startActivity(intent);
        });

        JSONParser parser = new JSONParser();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://portable-doctor.herokuapp.com/utente?token=" + token;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject obj = (JSONObject) parser.parse(response);
                name.setText((String) obj.get("nome"));
                patient = (boolean) obj.get("pazienteWarfarin");
                JSONObject obj2 = (JSONObject) obj.get("warfarin");
                long warfarin = (long) obj2.get("valoreMax");
                    if (check.equals("true") && patient && warfarin==-1) {
                        Intent intent = new Intent(getApplicationContext(), SurveyActivity.class);
                        startActivity(intent);
                    }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }, err -> welcome.setText(getResources().getString(R.string.not_available)));

        queue.add(stringRequest);

        refresh(token);

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

    private void refresh(String token) {

        JSONParser parser = new JSONParser();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url2 = "http://portable-doctor.herokuapp.com/evento/personale?token=" + token;
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2, response -> {
            try {
                arrayList.clear();
                Object obj =  parser.parse(response);
                JSONArray jsonArray = (JSONArray) obj;
                for(int i=0; i< jsonArray.size(); i++) {
                    JSONObject arrayJsonObject  = (JSONObject) jsonArray.get(i);
                    String date = (String) arrayJsonObject.get("data");
                    String tipo = (String) arrayJsonObject.get("tipo");
                    date = date.substring(0,10);
                    date = date+" "+"16:00";

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-MM HH:mm");
                    Date d = formatter.parse(date);
                    long mills = d.getTime() - System.currentTimeMillis();

                    if(tipo.equals("dose")) {
                        JSONObject a = (JSONObject) arrayJsonObject.get("descrizione");
                        String drug = (String) a.get("medicinale");
                        scheduleNotification(getNotification(drug), mills);
                        String dose = String.valueOf(a.get("dosaggio"));
                        arrayList.add(drug+" | "+dose+" mg | "+date);
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        arrayList.add("Test INR | "+date);
                        scheduleNotification(getNotification("Test INR"), mills);
                        adapter.notifyDataSetChanged();
                    }

                }
            } catch (ParseException | java.text.ParseException e) {
                e.printStackTrace();
            }
        }, err -> welcome.setText(getResources().getString(R.string.not_available)));

        queue.add(stringRequest2);
    }

    private void scheduleNotification (Notification notification , long delay) {
        Intent notificationIntent = new Intent( this, MyNotificationPublisher. class ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , delay , pendingIntent) ;
    }
    private Notification getNotification (String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id );
        builder.setContentTitle("Portable Doctor");
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable.ic_medicine);
        builder.setAutoCancel(true) ;
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build() ;
    }
}