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

public class AddMeeting extends AppCompatActivity {

    EditText etMeeting;
    Button bAddmeeting;
    String meeting;
    TextView login;
    int n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);

        etMeeting = (EditText)findViewById(R.id.meeting);
        bAddmeeting = (Button)findViewById(R.id.addmeeting);
        login = (TextView)findViewById(R.id.login);

        Firebase.setAndroidContext(this);

        final Firebase reference = new Firebase("https://coworkspace-48085-default-rtdb.firebaseio.com/info");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String newMeeting=dataSnapshot.child("MeetingInfo").child("Total").getValue().toString();
                n = Integer.parseInt(newMeeting);
//                                                int nCabin= n + Integer.parseInt(cabin);
//                                                reference.child("Cabins").setValue(String.valueOf(nCabin));
                Toast.makeText(AddMeeting.this, "Inside value listener..n="+n, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(AddMeeting.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddMeeting.this, AdminDashboard.class));
                finish();
            }
        });

        bAddmeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meeting = etMeeting.getText().toString();

                if(meeting.equals("")){
                    etMeeting.setError("can't be blank");
                }
                else {
                    final ProgressDialog pd = new ProgressDialog(AddMeeting.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    String url = "https://coworkspace-48085-default-rtdb.firebaseio.com/info.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {


                            if(s.equals("null")) {
                                reference.child("MeetingInfo").child("Total").setValue(meeting);
                                Toast.makeText(AddMeeting.this, "Meeting Rooms Added", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has("MeetingInfo")) {
                                        reference.child("MeetingInfo").child("Total").setValue(meeting);
//                                        reference.child(user).child("Name").setValue(nam);
//                                        reference.child(user).child("Company").setValue(comp);
                                        Toast.makeText(AddMeeting.this, "Meeting Rooms Added", Toast.LENGTH_SHORT).show();
                                    } else {


                                        reference.child("MeetingInfo").child("Total").setValue(String.valueOf(n + Integer.parseInt(meeting)));
                                        Toast.makeText(AddMeeting.this, "Meeting Rooms Updated", Toast.LENGTH_SHORT).show();
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

                    RequestQueue rQueue = Volley.newRequestQueue(AddMeeting.this);
                    rQueue.add(request);
                }
            }
        });
    }
}