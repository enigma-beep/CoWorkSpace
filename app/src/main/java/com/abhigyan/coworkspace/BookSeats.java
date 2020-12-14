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

public class BookSeats extends AppCompatActivity {

    String user;

    EditText etSeat, etType;
    Button bAddSeat, bType;
    String seat, seattype;
    TextView login;
    int n,m;
    private NotificationManager mNotificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_seats);
        user=getIntent().getStringExtra("user");

        etSeat = (EditText)findViewById(R.id.seat);
        etType = findViewById(R.id.seattype);
        bAddSeat = (Button)findViewById(R.id.addseat);
        login = (TextView)findViewById(R.id.login);
        bType = findViewById(R.id.btSeat);

        Firebase.setAndroidContext(this);
        final Firebase userreference = new Firebase("https://coworkspace-48085-default-rtdb.firebaseio.com/requests");
        final Firebase inforeference = new Firebase("https://coworkspace-48085-default-rtdb.firebaseio.com/info");

        final Firebase reference = new Firebase("https://coworkspace-48085-default-rtdb.firebaseio.com/users/"+user);


            inforeference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String newMeeting=dataSnapshot.child("SeatInfo").child("Flexi").child("Total").getValue().toString();
                    n = Integer.parseInt(newMeeting);
                    Toast.makeText(BookSeats.this, "Inside value listener..Flexi="+n, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Toast.makeText(BookSeats.this, "Error", Toast.LENGTH_LONG).show();

                }
            });


            inforeference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String newMeeting=dataSnapshot.child("SeatInfo").child("Fixed").child("Total").getValue().toString();
                    m = Integer.parseInt(newMeeting);
                    Toast.makeText(BookSeats.this, "Inside value listener..Fixed="+m, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Toast.makeText(BookSeats.this, "Error", Toast.LENGTH_LONG).show();

                }
            });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BookSeats.this, UserDashboard.class);
                i.putExtra("user",user);
                startActivity(i);
                finish();
            }
        });

        bType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etType.setText("");
//                etType.setFocusable(true);
//                etSeat.setFocusable(false);
                final PopupMenu popupMenu=new PopupMenu(BookSeats.this,bType);
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
                seattype = etType.getText().toString();

                if(seat.equals("")){
                    etSeat.setError("can't be blank");
                }
                else if(seattype.equals("")){
                    etType.setError("can't be blank");
                }
                else if((seattype.equals("Flexi")&&Integer.parseInt(seat)>n)||seattype.equals("Fixed")&&Integer.parseInt(seat)>m){
                    Toast.makeText(BookSeats.this, "Can't Process, request is more than available Seats Rooms", Toast.LENGTH_LONG).show();
                }
                else {
                    final ProgressDialog pd = new ProgressDialog(BookSeats.this);
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
                                userreference.child(ts).child("type").setValue(seattype);
                                userreference.child(ts).child("value").setValue(seat);
                                userreference.child(ts).child("status").setValue("false");

                                reference.child("requests").child(ts).child("user").setValue(user);
                                reference.child("requests").child(ts).child("type").setValue(seattype);
                                reference.child("requests").child(ts).child("value").setValue(seat);
                                reference.child("requests").child(ts).child("status").setValue("false");

                                NotificationCompat.Builder mBuilder =
                                        new NotificationCompat.Builder(BookSeats.this, "notify_001");
                                Intent ii = new Intent(BookSeats.this, AdminDashboard.class);
                                PendingIntent pendingIntent = PendingIntent.getActivity(BookSeats.this, 0, ii, 0);

                                NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                                bigText.bigText("Accept Seat Booking Request");
                                bigText.setBigContentTitle("New Booking Request");
                                bigText.setSummaryText("You have a new Seat Booking Request");

                                mBuilder.setContentIntent(pendingIntent);
                                mBuilder.setSmallIcon(R.drawable.ic_message);
                                mBuilder.setContentTitle("Request");
                                mBuilder.setContentText("Meeting Seat Request");
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

                                Toast.makeText(BookSeats.this, "Meeting Booking request sent", Toast.LENGTH_LONG).show();
                            }
                            else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has(user)) {
                                        Long tsLong = System.currentTimeMillis()/1000;
                                        String ts = tsLong.toString();
                                        userreference.child(ts).child("user").setValue(user);
                                        userreference.child(ts).child("type").setValue(seattype);
                                        userreference.child(ts).child("value").setValue(seat);
                                        userreference.child(ts).child("status").setValue("false");

                                        reference.child("requests").child(ts).child("user").setValue(user);
                                        reference.child("requests").child(ts).child("type").setValue(seattype);
                                        reference.child("requests").child(ts).child("value").setValue(seat);
                                        reference.child("requests").child(ts).child("status").setValue("false");

                                        NotificationCompat.Builder mBuilder =
                                                new NotificationCompat.Builder(BookSeats.this, "notify_001");
                                        Intent ii = new Intent(BookSeats.this, AdminDashboard.class);
                                        PendingIntent pendingIntent = PendingIntent.getActivity(BookSeats.this, 0, ii, 0);

                                        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                                        bigText.bigText("Accept Seat Booking Request");
                                        bigText.setBigContentTitle("New Booking Request");
                                        bigText.setSummaryText("You have a new Seat Booking Request");

                                        mBuilder.setContentIntent(pendingIntent);
                                        mBuilder.setSmallIcon(R.drawable.ic_message);
                                        mBuilder.setContentTitle("Request");
                                        mBuilder.setContentText("Meeting Seat Request");
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

                                        Toast.makeText(BookSeats.this, "Booking request sent", Toast.LENGTH_LONG).show();
                                    } else {

                                        Long tsLong = System.currentTimeMillis()/1000;
                                        String ts = tsLong.toString();
                                        userreference.child(ts).child("user").setValue(user);
                                        userreference.child(ts).child("type").setValue(seattype);
                                        userreference.child(ts).child("value").setValue(seat);
                                        userreference.child(ts).child("status").setValue("false");

                                        reference.child("requests").child(ts).child("user").setValue(user);
                                        reference.child("requests").child(ts).child("type").setValue(seattype);
                                        reference.child("requests").child(ts).child("value").setValue(seat);
                                        reference.child("requests").child(ts).child("status").setValue("false");

                                        NotificationCompat.Builder mBuilder =
                                                new NotificationCompat.Builder(BookSeats.this, "notify_001");
                                        Intent ii = new Intent(BookSeats.this, AdminDashboard.class);
                                        PendingIntent pendingIntent = PendingIntent.getActivity(BookSeats.this, 0, ii, 0);

                                        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                                        bigText.bigText("Accept Seat Booking Request");
                                        bigText.setBigContentTitle("New Booking Request");
                                        bigText.setSummaryText("You have a new Seat Booking Request");

                                        mBuilder.setContentIntent(pendingIntent);
                                        mBuilder.setSmallIcon(R.drawable.ic_message);
                                        mBuilder.setContentTitle("Request");
                                        mBuilder.setContentText("Meeting Seat Request");
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

                                        Toast.makeText(BookSeats.this, "Booking request sent", Toast.LENGTH_LONG).show();

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                NotificationCompat.Builder builder = new NotificationCompat.Builder(BookSeats.this).setSmallIcon(R.drawable.ic_message).setContentTitle("New Notification")
                                        .setContentText("New Seat Booking Request").setAutoCancel(true);
                                Intent intent=new Intent(BookSeats.this,AdminDashboard.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                PendingIntent pendingIntent=PendingIntent.getActivity(BookSeats.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                builder.setContentIntent(pendingIntent);
                                NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.notify(0,builder.build());
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

                    RequestQueue rQueue = Volley.newRequestQueue(BookSeats.this);
                    rQueue.add(request);
                }
            }
        });


    }
}