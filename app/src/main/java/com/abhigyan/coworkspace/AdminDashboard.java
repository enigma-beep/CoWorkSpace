package com.abhigyan.coworkspace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

public class AdminDashboard extends AppCompatActivity {

    private FloatingActionButton fab_main, fab1_mail, fab2_share, fab3, fab4;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    TextView textview_mail, textview_share, textview_cabin, textview_meeting;
    Boolean isOpen = false;

    TextView noUser, noCabins, noMeeting, noFlexi, noFixed;

    RecyclerView uRecyclerView, aRecyclerView;
    AdAdapter myAdapter;

    DatabaseReference reff;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.logout,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        fab_main = findViewById(R.id.fab);
        fab1_mail = findViewById(R.id.fab1);
        fab2_share = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);
        fab4 = findViewById(R.id.fab4);

        noUser = findViewById(R.id.noUser);
        noCabins = findViewById(R.id.noCabins);
        noMeeting = findViewById(R.id.noMeeting);
        noFlexi = findViewById(R.id.noFlexi);
        noFixed = findViewById(R.id.noFixed);

        Firebase.setAndroidContext(this);

        reff=FirebaseDatabase.getInstance().getReference().child("requests");

//        uRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        aRecyclerView =findViewById(R.id.apRview);
        aRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);

        textview_mail = (TextView) findViewById(R.id.textview_mail);
        textview_share = (TextView) findViewById(R.id.textview_share);
        textview_cabin = (TextView) findViewById(R.id.textview_cabin);
        textview_meeting = (TextView) findViewById(R.id.textview_meeting);

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen) {

                    textview_mail.setVisibility(View.INVISIBLE);
                    textview_share.setVisibility(View.INVISIBLE);
                    textview_cabin.setVisibility(View.INVISIBLE);
                    textview_meeting.setVisibility(View.INVISIBLE);
                    fab2_share.startAnimation(fab_close);
                    fab1_mail.startAnimation(fab_close);
                    fab3.startAnimation(fab_close);
                    fab4.startAnimation(fab_close);
                    fab_main.startAnimation(fab_anticlock);
                    fab2_share.setClickable(false);
                    fab1_mail.setClickable(false);
                    fab3.setClickable(false);
                    fab4.setClickable(false);
                    isOpen = false;
                } else {
                    textview_mail.setVisibility(View.VISIBLE);
                    textview_share.setVisibility(View.VISIBLE);
                    textview_cabin.setVisibility(View.VISIBLE);
                    textview_meeting.setVisibility(View.VISIBLE);
                    fab2_share.startAnimation(fab_open);
                    fab3.startAnimation(fab_open);
                    fab4.startAnimation(fab_open);
                    fab1_mail.startAnimation(fab_open);
                    fab_main.startAnimation(fab_clock);
                    fab2_share.setClickable(true);
                    fab1_mail.setClickable(true);
                    fab3.setClickable(true);
                    fab4.setClickable(true);
                    isOpen = true;
                }

            }
        });


        fab2_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this, AddUser.class));
                finish();
                Toast.makeText(getApplicationContext(), "Share", Toast.LENGTH_SHORT).show();

            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this, AddCabins.class));
                finish();
                Toast.makeText(getApplicationContext(), "Share", Toast.LENGTH_SHORT).show();

            }
        });
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this, AddMeeting.class));
                finish();
                Toast.makeText(getApplicationContext(), "Share", Toast.LENGTH_SHORT).show();

            }
        });

        fab1_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this, AddSeats.class));
                Toast.makeText(getApplicationContext(), "Email", Toast.LENGTH_SHORT).show();

            }
        });

        String url1 = "https://coworkspace-48085-default-rtdb.firebaseio.com/users.json";
        String url2 = "https://coworkspace-48085-default-rtdb.firebaseio.com/info.json";


        final Firebase reference1 = new Firebase("https://coworkspace-48085-default-rtdb.firebaseio.com/users");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long u=dataSnapshot.getChildrenCount();
                String user = String.valueOf(u-1);
                noUser.setText(user);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        final Firebase reference2 = new Firebase("https://coworkspace-48085-default-rtdb.firebaseio.com/info");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String cabin=dataSnapshot.child("CabinInfo").child("Total").getValue().toString();
                noCabins.setText(cabin);

                String meeting=dataSnapshot.child("MeetingInfo").child("Total").getValue().toString();
                noMeeting.setText(meeting);

                String flexi=dataSnapshot.child("SeatInfo").child("Flexi").child("Total").getValue().toString();
                noFlexi.setText(flexi);

                String fixed=dataSnapshot.child("SeatInfo").child("Fixed").child("Total").getValue().toString();
                noFixed.setText(fixed);


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });


        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("requests"), Model.class)
                        .build();


        myAdapter=new AdAdapter(options);

        aRecyclerView.setAdapter(myAdapter);




    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.item1:
                SharedPreferences.Editor editor = getSharedPreferences("name", MODE_PRIVATE).edit();
                editor.putString("password", "");
                editor.putString("email", "");
                editor.putBoolean("isLoggedIn", false);
                editor.apply();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("finish", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                finish();
                Toast.makeText(this,"Logged Out",Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
        myAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myAdapter.stopListening();
    }


}