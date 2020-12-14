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
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class AddCabins extends AppCompatActivity {

    EditText etCabin;
    Button bAddcabin;
    String cabin;
    TextView login;
    int n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cabins);

        etCabin = (EditText)findViewById(R.id.cabin);
        bAddcabin = (Button)findViewById(R.id.addcabin);
        login = (TextView)findViewById(R.id.login);

        Firebase.setAndroidContext(this);
        final Firebase reference = new Firebase("https://coworkspace-48085-default-rtdb.firebaseio.com/info");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String newMeeting=dataSnapshot.child("CabinInfo").child("Total").getValue().toString();
                n = Integer.parseInt(newMeeting);
//                                                int nCabin= n + Integer.parseInt(cabin);
//                                                reference.child("Cabins").setValue(String.valueOf(nCabin));
                Toast.makeText(AddCabins.this, "Inside value listener..n="+n, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(AddCabins.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddCabins.this, AdminDashboard.class));
                finish();
            }
        });

        bAddcabin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cabin = etCabin.getText().toString();

                if(cabin.equals("")){
                    etCabin.setError("can't be blank");
                }
                else {
                    final ProgressDialog pd = new ProgressDialog(AddCabins.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    String url = "https://coworkspace-48085-default-rtdb.firebaseio.com/info.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {

                            if(s.equals("null")) {
                                reference.child("CabinInfo").child("Total").setValue(cabin);
                                Toast.makeText(AddCabins.this, "Cabins Added", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has("CabinInfo")) {
                                        reference.child("CabinInfo").child("Total").setValue(cabin);
//                                        reference.child(user).child("Name").setValue(nam);
//                                        reference.child(user).child("Company").setValue(comp);
                                        Toast.makeText(AddCabins.this, "Cabin obj Added", Toast.LENGTH_SHORT).show();
                                    } else {



                                        reference.child("CabinInfo").child("Total").setValue(String.valueOf(n + Integer.parseInt(cabin)));
                                        Toast.makeText(AddCabins.this, "Cabins Updated", Toast.LENGTH_SHORT).show();
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

                    RequestQueue rQueue = Volley.newRequestQueue(AddCabins.this);
                    rQueue.add(request);
                }
            }
        });
    }
}