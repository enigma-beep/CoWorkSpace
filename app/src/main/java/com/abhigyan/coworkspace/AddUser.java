package com.abhigyan.coworkspace;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

public class AddUser extends AppCompatActivity {

    EditText username, password, name, company, address, designation, phone, numEmp;
    Button registerButton;
    String user, pass, nam, comp, addr, desig, phno, numemp;
    TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        name = (EditText)findViewById(R.id.name);
        company = (EditText)findViewById(R.id.comapany);
        address = (EditText)findViewById(R.id.address);
        designation = (EditText)findViewById(R.id.designation);
        phone = (EditText)findViewById(R.id.phone);
        numEmp = (EditText)findViewById(R.id.numEmp);
        registerButton = (Button)findViewById(R.id.registerButton);
        login = (TextView)findViewById(R.id.login);

        Firebase.setAndroidContext(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddUser.this, AdminDashboard.class));
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();
                nam = name.getText().toString();
                comp = company.getText().toString();
                addr = address.getText().toString();
                desig = designation.getText().toString();
                phno = phone.getText().toString();
                numemp = numEmp.getText().toString();

                if(user.equals("")){
                    username.setError("can't be blank");
                }
                else if(pass.equals("")){
                    password.setError("can't be blank");
                }
                else if(!user.matches("[A-Za-z0-9]+")){
                    username.setError("only alphabet or number allowed");
                }
                else if(user.length()<5){
                    username.setError("at least 5 characters long");
                }
                else if(pass.length()<5){
                    password.setError("at least 5 characters long");
                }
                else if(nam.equals("")){
                    name.setError("can't be blank");
                }
                else if(comp.equals("")){
                    company.setError("can't be blank");
                }
                else if(addr.equals("")){
                    address.setError("can't be blank");
                }
                else if(desig.equals("")){
                    designation.setError("can't be blank");
                }
                else if(phno.equals("")){
                    phone.setError("can't be blank");
                }
                else if(numemp.equals("")){
                    numEmp.setError("can't be blank");
                }
                else {
                    final ProgressDialog pd = new ProgressDialog(AddUser.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    String url = "https://coworkspace-48085-default-rtdb.firebaseio.com/users.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            Firebase reference = new Firebase("https://coworkspace-48085-default-rtdb.firebaseio.com/users");

                            if(s.equals("null")) {
                                reference.child(user).child("password").setValue(pass);
                                Toast.makeText(AddUser.this, "registration successful", Toast.LENGTH_LONG).show();
                            }
                            else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has(user)) {
                                        reference.child(user).child("password").setValue(pass);
                                        reference.child(user).child("Name").setValue(nam);
                                        reference.child(user).child("Company").setValue(comp);
                                        reference.child(user).child("Address").setValue(addr);
                                        reference.child(user).child("Designation").setValue(desig);
                                        reference.child(user).child("Phone").setValue(phno);
                                        reference.child(user).child("NumEmp").setValue(numemp);

                                        Toast.makeText(AddUser.this, "registration successful", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(AddUser.this, "username already exists", Toast.LENGTH_LONG).show();
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
                            System.out.println("" + volleyError );
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(AddUser.this);
                    rQueue.add(request);
                }
            }
        });
    }
}