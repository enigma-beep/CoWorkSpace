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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserDashboard extends AppCompatActivity {

    RecyclerView uRecyclerView;
    MyAdapter myAdapter;

    private FloatingActionButton fab_main, fabmeeting, fabseat, fabcabin;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    TextView textview_mail, textview_share, textview_meet;
    Boolean isOpen = false;

    public String user;

    public String mUser, mType, mValue, mStatus;
    int children;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.logout,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        fab_main = findViewById(R.id.fab);
        fabmeeting = findViewById(R.id.fabmeeting);
        fabseat = findViewById(R.id.fabseat);
        fabcabin = findViewById(R.id.fabcabin);

        user=getIntent().getStringExtra("user");

        Toast.makeText(getApplicationContext(), "User :"+user, Toast.LENGTH_SHORT).show();

        Firebase.setAndroidContext(this);

        uRecyclerView =findViewById(R.id.urview);
        uRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);

        textview_mail = (TextView) findViewById(R.id.textview_mail);
        textview_share = (TextView) findViewById(R.id.textview_share);
        textview_meet = (TextView) findViewById(R.id.textview_meet);

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen) {

                    textview_mail.setVisibility(View.INVISIBLE);
                    textview_share.setVisibility(View.INVISIBLE);
                    textview_meet.setVisibility(View.INVISIBLE);

                    fabmeeting.startAnimation(fab_close);
                    fabcabin.startAnimation(fab_close);
                    fabseat.startAnimation(fab_close);
                    fab_main.startAnimation(fab_anticlock);
                    fabmeeting.setClickable(false);
                    fabseat.setClickable(false);
                    fabcabin.setClickable(false);
                    isOpen = false;
                } else {
                    textview_mail.setVisibility(View.VISIBLE);
                    textview_share.setVisibility(View.VISIBLE);
                    textview_meet.setVisibility(View.VISIBLE);
                    fabmeeting.startAnimation(fab_open);
                    fabseat.startAnimation(fab_open);
                    fabcabin.startAnimation(fab_open);
                    fab_main.startAnimation(fab_clock);
                    fabmeeting.setClickable(true);
                    fabseat.setClickable(true);
                    fabcabin.setClickable(true);
                    isOpen = true;
                }

            }
        });


        fabmeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(UserDashboard.this, BookMeeting.class));
                Intent i = new Intent(UserDashboard.this, BookMeeting.class);
                i.putExtra("user",user);
                startActivity(i);

                finish();
                Toast.makeText(getApplicationContext(), "Share", Toast.LENGTH_SHORT).show();

            }
        });

        fabseat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(UserDashboard.this, BookSeats.class));
                Intent i = new Intent(UserDashboard.this, BookSeats.class);
                i.putExtra("user",user);
                startActivity(i);
                finish();
                Toast.makeText(getApplicationContext(), "Share", Toast.LENGTH_SHORT).show();

            }
        });
        fabcabin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(UserDashboard.this, BookCabin.class));
                Intent i = new Intent(UserDashboard.this, BookCabin.class);
                i.putExtra("user",user);
                startActivity(i);
                finish();
                Toast.makeText(getApplicationContext(), "Share", Toast.LENGTH_SHORT).show();

            }
        });

        final Firebase reference1 = new Firebase("https://coworkspace-48085-default-rtdb.firebaseio.com/requests/"+user);

        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users").child(user).child("requests"), Model.class)
                        .build();

        myAdapter=new MyAdapter(options);
        uRecyclerView.setAdapter(myAdapter);






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