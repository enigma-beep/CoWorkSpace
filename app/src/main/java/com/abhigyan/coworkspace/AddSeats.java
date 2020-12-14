package com.abhigyan.coworkspace;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
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

public class AddSeats extends AppCompatActivity {

    EditText etSeat, etType;
    Button bAddSeat, bType;
    String seat, type;
    TextView login;
    int n,m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_seats);

        etSeat = (EditText)findViewById(R.id.seat);
        etType = findViewById(R.id.seattype);
        bAddSeat = (Button)findViewById(R.id.addseat);
        login = (TextView)findViewById(R.id.login);
        bType = findViewById(R.id.btSeat);

        Firebase.setAndroidContext(this);
        final Firebase reference = new Firebase("https://coworkspace-48085-default-rtdb.firebaseio.com/info");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String newflexi=dataSnapshot.child("SeatInfo").child("Flexi").child("Total").getValue().toString();
                n = Integer.parseInt(newflexi);
//                                                int nCabin= n + Integer.parseInt(cabin);
//                                                reference.child("Cabins").setValue(String.valueOf(nCabin));
//                                                Toast.makeText(AddCabins.this, "Cabins Updated", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(AddSeats.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String newflexi=dataSnapshot.child("SeatInfo").child("Fixed").child("Total").getValue().toString();
                m = Integer.parseInt(newflexi);
//                                                int nCabin= n + Integer.parseInt(cabin);
//                                                reference.child("Cabins").setValue(String.valueOf(nCabin));
//                                                Toast.makeText(AddCabins.this, "Cabins Updated", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(AddSeats.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddSeats.this, AdminDashboard.class));
                finish();
            }
        });

        bType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etType.setText("");
//                etType.setFocusable(true);
//                etSeat.setFocusable(false);
                final PopupMenu popupMenu=new PopupMenu(AddSeats.this,bType);
                popupMenu.getMenuInflater().inflate(R.menu.seattypes,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        //Toast.makeText(getApplicationContext(),""+ menuItem.getTitle(),Toast.LENGTH_LONG).show();
                        etType.setText(menuItem.getTitle());
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        bAddSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seat = etSeat.getText().toString();
                type = etType.getText().toString();

                if(seat.equals("")){
                    etSeat.setError("can't be blank");
                }
                else if(type.equals("")){
                    etType.setError("choose one");
                }
                else if(type.equals("Fixed")){
                    final ProgressDialog pd = new ProgressDialog(AddSeats.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    String url = "https://coworkspace-48085-default-rtdb.firebaseio.com/info.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            final Firebase reference = new Firebase("https://coworkspace-48085-default-rtdb.firebaseio.com/info");

                            if(s.equals("null")) {
                                reference.child("SeatInfo").child("Fixed").child("Total").setValue(seat);
                                Toast.makeText(AddSeats.this, "Fixed Seats Added", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has("SeatInfo")) {
                                        reference.child("SeatInfo").child("Fixed").child("Total").setValue(seat);
                                        Toast.makeText(AddSeats.this, "Fixed Seats Added", Toast.LENGTH_SHORT).show();
                                    } else {



                                        reference.child("SeatInfo").child("Fixed").child("Total").setValue(String.valueOf(m + Integer.parseInt(seat)));
                                        Toast.makeText(AddSeats.this, "Fixed Seats Updated", Toast.LENGTH_SHORT).show();
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

                    RequestQueue rQueue = Volley.newRequestQueue(AddSeats.this);
                    rQueue.add(request);
                }
                else if(type.equals("Flexi")){
                    final ProgressDialog pd = new ProgressDialog(AddSeats.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    String url = "https://coworkspace-48085-default-rtdb.firebaseio.com/info.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {


                            if(s.equals("null")) {
                                reference.child("SeatInfo").child("Flexi").child("Total").setValue(seat);
                                Toast.makeText(AddSeats.this, "Flexi Seats Added", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has("SeatInfo")) {
                                        reference.child("SeatInfo").child("Flexi").child("Total").setValue(seat);
//                                        reference.child(user).child("Name").setValue(nam);
//                                        reference.child(user).child("Company").setValue(comp);
                                        Toast.makeText(AddSeats.this, "Flexi Seats Added", Toast.LENGTH_SHORT).show();
                                    } else {



                                        reference.child("SeatInfo").child("Flexi").child("Total").setValue(String.valueOf(n + Integer.parseInt(seat)));
                                        Toast.makeText(AddSeats.this, "Flexi Seats Updated", Toast.LENGTH_SHORT).show();
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

                    RequestQueue rQueue = Volley.newRequestQueue(AddSeats.this);
                    rQueue.add(request);
                }
            }
        });
    }
}