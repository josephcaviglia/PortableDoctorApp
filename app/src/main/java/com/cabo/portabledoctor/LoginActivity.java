package com.cabo.portabledoctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    Button login;
    TextView error, account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        error = findViewById(R.id.error);
        account = findViewById(R.id.account);
        String register = account.getText().toString();
        String part = getResources().getString(R.string.sign_up);

        setClickableString(part, register, account);


        RequestQueue queue = Volley.newRequestQueue(this);
        //METTERE DELAY BOTTONE
        login.setOnClickListener(view -> {
            String email = username.getText().toString();
            String pass = password.getText().toString();
            String url ="http://portable-doctor.herokuapp.com/utente?email="+email+"&password="+pass;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
                if(email.equals("")||pass.equals(""))
                    error.setText(getResources().getString(R.string.all_fields));
                        else{
                            if(response.equals("Credenziali Errate")){
                                error.setText(getResources().getString(R.string.incorrect));
                            }else{
                                JSONObject obj;
                                try {
                                    obj = new JSONObject(response);
                                    String token =  obj.getString("token");
                                    SharedPreferences preferences = getSharedPreferences("keepLogged", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("remember", "true");
                                    editor.putString("token", token);
                                    editor.apply();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Intent intent  = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    }, err -> error.setText(getResources().getString(R.string.not_available)));
            queue.add(stringRequest);
        });
    }

    @Override
    public void onBackPressed() {
            moveTaskToBack(true);
    }

    public void setClickableString(String clickableValue, String wholeValue, TextView yourTextView){
        String value = wholeValue;
        SpannableString spannableString = new SpannableString(value);
        int startIndex = value.indexOf(clickableValue);
        int endIndex = startIndex + clickableValue.length();
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(View widget) {
                Intent intent  = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        }, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        yourTextView.setText(spannableString);
        yourTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}