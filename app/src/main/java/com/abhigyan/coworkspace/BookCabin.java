package com.abhigyan.coworkspace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

public class BookCabin extends AppCompatActivity {

    String user;

    EditText etCabin;
    Button bAddcabin;
    String cabin;
    TextView login;
    int n;
    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_cabin);

        user=getIntent().getStringExtra("user");

        etCabin = (EditText)findViewById(R.id.cabin);
        bAddcabin = (Button)findViewById(R.id.addcabin);
        login = (TextView)findViewById(R.id.login);

        Firebase.setAndroidContext(this);
        final Firebase userreference = new Firebase("https://coworkspace-48085-default-rtdb.firebaseio.com/requests");
        final Firebase inforeference = new Firebase("https://coworkspace-48085-default-rtdb.firebaseio.com/info");

        final Firebase reference = new Firebase("https://coworkspace-48085-default-rtdb.firebaseio.com/users/"+user);


        inforeference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String newMeeting=dataSnapshot.child("CabinInfo").child("Total").getValue().toString();
                n = Integer.parseInt(newMeeting);
                Toast.makeText(BookCabin.this, "Inside value listener..n="+n, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(BookCabin.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BookCabin.this, UserDashboard.class);
                i.putExtra("user",user);
                startActivity(i);
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
                else if(Integer.parseInt(cabin)>n){
                    Toast.makeText(BookCabin.this, "Can't Process, cabin request is more than available cabins", Toast.LENGTH_SHORT).show();
                }
                else {
                    final ProgressDialog pd = new ProgressDialog(BookCabin.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    String url = "https://coworkspace-48085-default-rtdb.firebaseio.com/requests.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {

                            if(s.equals("null")) {
                                Long tsLong = System.currentTimeMillis()/1000;
                                String ts = tsLong.toString();
                                userreference.child(ts).child("user").setValue(user);
                                userreference.child(ts).child("type").setValue("cabin");
                                userreference.child(ts).child("value").setValue(cabin);
                                userreference.child(ts).child("status").setValue("false");

                                reference.child("requests").child(ts).child("user").setValue(user);
                                reference.child("requests").child(ts).child("type").setValue("cabin");
                                reference.child("requests").child(ts).child("value").setValue(cabin);
                                reference.child("requests").child(ts).child("status").setValue("false");

                                Toast.makeText(BookCabin.this, "Cabin Booking request sent", Toast.LENGTH_SHORT).show();

//                                NotificationCompat.Builder builder = new NotificationCompat.Builder(BookCabin.this).setSmallIcon(R.drawable.ic_message).setContentTitle("New Notification")
//                                        .setContentText("New Cabin Booking Request").setAutoCancel(true);
//                                Intent intent=new Intent(BookCabin.this,AdminDashboard.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                PendingIntent pendingIntent=PendingIntent.getActivity(BookCabin.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//                                builder.setContentIntent(pendingIntent);
//                                NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//                                notificationManager.notify(0,builder.build());
                                NotificationCompat.Builder mBuilder =
                                        new NotificationCompat.Builder(BookCabin.this, "notify_001");
                                Intent ii = new Intent(BookCabin.this, AdminDashboard.class);
                                PendingIntent pendingIntent = PendingIntent.getActivity(BookCabin.this, 0, ii, 0);

                                NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                                bigText.bigText("Accept Cabin Booking Request");
                                bigText.setBigContentTitle("New Booking Request");
                                bigText.setSummaryText("You have a new Cabin Booking Request");

                                mBuilder.setContentIntent(pendingIntent);
                                mBuilder.setSmallIcon(R.drawable.ic_message);
                                mBuilder.setContentTitle("Request");
                                mBuilder.setContentText("Cabin Booking Request");
                                mBuilder.setPriority(Notification.PRIORITY_MAX);
                                mBuilder.setStyle(bigText);

                                mNotificationManager =
                                        (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

// === Removed some obsoletes
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                {
                                    String channelId = "Your_channel_id";
                                    NotificationChannel channel = new NotificationChannel(
                                            channelId,
                                            "Channel human readable title",
                                            NotificationManager.IMPORTANCE_HIGH);
                                    mNotificationManager.createNotificationChannel(channel);
                                    mBuilder.setChannelId(channelId);
                                }

                                mNotificationManager.notify(0, mBuilder.build());



                            }
                            else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has(user)) {
                                        Long tsLong = System.currentTimeMillis()/1000;
                                        String ts = tsLong.toString();
                                        userreference.child(ts).child("user").setValue(user);
                                        userreference.child(ts).child("type").setValue("cabin");
                                        userreference.child(ts).child("value").setValue(cabin);
                                        userreference.child(ts).child("status").setValue("false");

                                        reference.child("requests").child(ts).child("user").setValue(user);
                                        reference.child("requests").child(ts).child("type").setValue("cabin");
                                        reference.child("requests").child(ts).child("value").setValue(cabin);
                                        reference.child("requests").child(ts).child("status").setValue("false");

                                        Toast.makeText(BookCabin.this, "Booking request sent", Toast.LENGTH_SHORT).show();

                                        NotificationCompat.Builder mBuilder =
                                                new NotificationCompat.Builder(BookCabin.this, "notify_001");
                                        Intent ii = new Intent(BookCabin.this, AdminDashboard.class);
                                        PendingIntent pendingIntent = PendingIntent.getActivity(BookCabin.this, 0, ii, 0);

                                        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                                        bigText.bigText("Accept Cabin Booking Request");
                                        bigText.setBigContentTitle("New Booking Request");
                                        bigText.setSummaryText("You have a new Cabin Booking Request");

                                        mBuilder.setContentIntent(pendingIntent);
                                        mBuilder.setSmallIcon(R.drawable.ic_message);
                                        mBuilder.setContentTitle("Request");
                                        mBuilder.setContentText("Cabin Booking Request");
                                        mBuilder.setPriority(Notification.PRIORITY_MAX);
                                        mBuilder.setStyle(bigText);

                                        mNotificationManager =
                                                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

// === Removed some obsoletes
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                        {
                                            String channelId = "Your_channel_id";
                                            NotificationChannel channel = new NotificationChannel(
                                                    channelId,
                                                    "Channel human readable title",
                                                    NotificationManager.IMPORTANCE_HIGH);
                                            mNotificationManager.createNotificationChannel(channel);
                                            mBuilder.setChannelId(channelId);
                                        }

                                        mNotificationManager.notify(0, mBuilder.build());

                                    } else {

                                        Long tsLong = System.currentTimeMillis()/1000;
                                        String ts = tsLong.toString();
                                        userreference.child(ts).child("user").setValue(user);
                                        userreference.child(ts).child("type").setValue("cabin");
                                        userreference.child(ts).child("value").setValue(cabin);
                                        userreference.child(ts).child("status").setValue("false");

                                        reference.child("requests").child(ts).child("user").setValue(user);
                                        reference.child("requests").child(ts).child("type").setValue("cabin");
                                        reference.child("requests").child(ts).child("value").setValue(cabin);
                                        reference.child("requests").child(ts).child("status").setValue("false");

                                        Toast.makeText(BookCabin.this, "Booking request sent", Toast.LENGTH_SHORT).show();

                                        NotificationCompat.Builder mBuilder =
                                                new NotificationCompat.Builder(BookCabin.this, "notify_001");
                                        Intent ii = new Intent(BookCabin.this, AdminDashboard.class);
                                        PendingIntent pendingIntent = PendingIntent.getActivity(BookCabin.this, 0, ii, 0);

                                        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                                        bigText.bigText("Accept Cabin Booking request");
                                        bigText.setBigContentTitle("New Booking Request");
                                        bigText.setSummaryText("You have a new Cabin Booking Request");

                                        mBuilder.setContentIntent(pendingIntent);
                                        mBuilder.setSmallIcon(R.drawable.ic_message);
                                        mBuilder.setContentTitle("Request");
                                        mBuilder.setContentText("Cabin Booking Request");
                                        mBuilder.setPriority(Notification.PRIORITY_MAX);
                                        mBuilder.setStyle(bigText);

                                        mNotificationManager =
                                                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

// === Removed some obsoletes
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                        {
                                            String channelId = "Your_channel_id";
                                            NotificationChannel channel = new NotificationChannel(
                                                    channelId,
                                                    "Channel human readable title",
                                                    NotificationManager.IMPORTANCE_HIGH);
                                            mNotificationManager.createNotificationChannel(channel);
                                            mBuilder.setChannelId(channelId);
                                        }

                                        mNotificationManager.notify(0, mBuilder.build());

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

                    RequestQueue rQueue = Volley.newRequestQueue(BookCabin.this);
                    rQueue.add(request);
                }
            }
        });



    }
}