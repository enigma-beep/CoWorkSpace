package com.abhigyan.coworkspace;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    TextView registerUser;
    EditText username, password;
    Button loginButton;
    String user, pass;
    String mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("name", MODE_PRIVATE);
        boolean isLoggedIn= prefs.getBoolean("isLoggedIn", false);
        mUser= prefs.getString("user",user);

//        if(mUser.equals("admin")){
//            if(isLoggedIn){
//                Intent i = new Intent(MainActivity.this, AdminDashboard.class);
//                startActivity(i);
//                finish();
//                return;
//            }
//        }
        if(isLoggedIn){
            if(mUser.equals("admin")){
                Intent i = new Intent(MainActivity.this, AdminDashboard.class);
                startActivity(i);
                finish();
            }
            else {
                Intent i = new Intent(MainActivity.this, UserDashboard.class);
                i.putExtra("user", mUser);
                startActivity(i);
                finish();
            }
            return;

        }


        setContentView(R.layout.activity_main);



        registerUser = (TextView)findViewById(R.id.register);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        loginButton = (Button)findViewById(R.id.loginButton);

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Register.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if(user.equals("")){
                    username.setError("can't be blank");
                }
                else if(pass.equals("")){
                    password.setError("can't be blank");
                }
                else{
                    String url = "https://coworkspace-48085-default-rtdb.firebaseio.com/users.json";
                    final ProgressDialog pd = new ProgressDialog(MainActivity.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            if(s.equals("null")){
                                Toast.makeText(MainActivity.this, "user not found", Toast.LENGTH_LONG).show();
                            }
                            else{
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if(!obj.has(user)){
                                        Toast.makeText(MainActivity.this, "user not found", Toast.LENGTH_LONG).show();
                                    }

                                    else if(obj.getJSONObject(user).getString("password").equals(pass)){
                                        UserDetails.username = user;
                                        UserDetails.password = pass;
                                        if((UserDetails.username.equals("admin"))&&(UserDetails.password.equals("admin"))){

                                            SharedPreferences.Editor editor = getSharedPreferences("name", MODE_PRIVATE).edit();
                                            editor.putString("user", user);
                                            editor.putString("pass", pass);
                                            editor.putBoolean("isLoggedIn", true);
                                            editor.apply();
                                            startActivity(new Intent(MainActivity.this, AdminDashboard.class));
                                            finish();
                                        }
                                        else{

                                            SharedPreferences.Editor editor = getSharedPreferences("name", MODE_PRIVATE).edit();
                                            editor.putString("user", user);
                                            editor.putString("pass", pass);
                                            editor.putBoolean("isLoggedIn", true);
                                            editor.apply();

                                            Intent i = new Intent(MainActivity.this, UserDashboard.class);
                                            i.putExtra("user",user);
                                            startActivity(i);
                                            finish();
//                                            startActivity(new Intent(MainActivity.this, UserDashboard.class));
                                        }

                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, "incorrect password", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            pd.dismiss();
                        }
                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError);
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
                    rQueue.add(request);
                }

            }
        });
    }


}