package com.example.admin.natink_trial;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class emergencylist extends AppCompatActivity {
    Button button, send;
    ListView listView;
    TextView text, firstline1, secondline1, thirdline1, fourthline1;
    int count, i, initial = 0,number=0;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private static final int MY_PERMISSIONS_REQUESTED_SEND_SMS = 0;
    String contacts = "";
    double latitude, longitude;
    ArrayList<String> selectcontacts;
    StringBuilder sb = new StringBuilder();
    ArrayList<String> sendmessage = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencylist);



        listView = (ListView) findViewById(R.id.idlist);
        send = (Button) findViewById(R.id.send);
        firstline1 = (TextView) findViewById(R.id.firstline1);
        secondline1 = (TextView) findViewById(R.id.secondline1);
        thirdline1 = (TextView) findViewById(R.id.thirdline1);
        fourthline1 = (TextView) findViewById(R.id.fourthline1);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(emergencylist.this,NotificationView.class);
                startActivity(i);
            }
        });
        /*locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                //    Toast.makeText(getApplicationContext(), "Location got ", Toast.LENGTH_SHORT).show();
                if(number==0) {
                    youcansend();
                }
                number++;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(emergencylist.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(emergencylist.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                int permissioncheck = ContextCompat.checkSelfPermission(emergencylist.this, Manifest.permission.SEND_SMS);
                if(permissioncheck== PackageManager.PERMISSION_GRANTED)
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 3000, locationListener);
                    //youcansend();
                }
                else
                {
                    ActivityCompat.requestPermissions(emergencylist.this,new String[]{Manifest.permission.SEND_SMS},MY_PERMISSIONS_REQUESTED_SEND_SMS);
                }
            }
        });*/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(emergencylist.this,contact.class);
                startActivity(intent);
                finish();

            }
        });
        loaddata();
        selectcontacts = (ArrayList<String>) getIntent().getSerializableExtra("mylist");


        if ((selectcontacts==null))
        {
            Toast.makeText(getApplicationContext(),"No contacts selected",Toast.LENGTH_SHORT).show();
        }
        else {

            count = selectcontacts.size();
            Toast.makeText(getApplicationContext(), count + " count", Toast.LENGTH_SHORT).show();

            for (i = 0; i < count; i++) {
                sendmessage.add(selectcontacts.get(i));
                Log.d("emergency",selectcontacts.get(i));

            }
            selectcontacts.clear();
            count=0;
            savedata();


            //     Toast.makeText(getApplicationContext(),sendmessage+" send message",Toast.LENGTH_SHORT).show();
        }
        validateit();

        //text.append(selectcontacts.get(1));
        //sendmessage.addAll(selectcontacts);
        //Toast.makeText(getApplicationContext(),sendmessage+" called",Toast.LENGTH_SHORT).show();
        /* if((sendmessage.isEmpty())||(sendmessage==null)) {
       Intent intent = new Intent(MainActivity.this,contact.class);
        startActivity(intent);

       }
       else{*/
        final ArrayAdapter adapter1 = new ArrayAdapter(
                this, R.layout.contacts_list_view, sendmessage);
        listView.setAdapter(adapter1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //selectcontacts.add(contacts.get(i));
                AlertDialog.Builder builder = new AlertDialog.Builder(emergencylist.this);
                builder.setCancelable(false);
                //  Toast.makeText(getApplicationContext(),position + " position",Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),sendmessage.get(position) + "list selected ",Toast.LENGTH_SHORT).show();
                builder.setTitle("Remove..,");
                builder.setMessage("Are you sure you want to Remove this number as Emergency contact number.");
                builder.setPositiveButton("yes.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        sendmessage.remove(position);

                        //  Toast.makeText(getApplicationContext(),position+" after positon",Toast.LENGTH_SHORT).show();
                        //sendmessage.remove(new String(sendmessage.get(position)));


                        //    Toast.makeText(getApplicationContext(), sendmessage.get(position)+"  removed",Toast.LENGTH_SHORT).show();
//                           listView.setAdapter(adapter1);

                        savedata();

                        validateit();

                        //finish();
                    }
                })
                        .setNegativeButton("No.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //  Toast.makeText(getApplicationContext(),"No was Clicked",Toast.LENGTH_SHORT).show();
                                //      finish();
                            }
                        });

                // Create the AlertDialog object and return it
                builder.create().show();
            }


        });
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                },10);
                return;
            }
        }
        else
        {
            configureButton();
        }
    }

    private void configureButton() {
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case  10:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    configureButton();
                return;
        }
    }

    private void validateit() {
        NotificationManager notificationManager1 = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if(sendmessage.isEmpty())
        {
            PendingIntent pendingintent = PendingIntent.getActivity(this, 0,
                    new Intent(this, contact.class), PendingIntent.FLAG_UPDATE_CURRENT);

            Notification noti1 = new Notification.Builder(this)
                    .setContentTitle("Emergency contact is Empty")
                    .setContentText("Press To add emergency number")
                    .setSmallIcon(R.drawable.engin)
                    .setContentIntent(pendingintent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setOngoing(true) // Again, THIS is the important line

                    .build();


            notificationManager1.notify(1,noti1);

            send.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);
            firstline1.setVisibility(View.VISIBLE);
            secondline1.setVisibility(View.VISIBLE);
            thirdline1.setVisibility(View.VISIBLE);
            fourthline1.setVisibility(View.VISIBLE);

        }
        else
        {
            notificationManager1.cancel(1);
            send.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            firstline1.setVisibility(View.INVISIBLE);
            secondline1.setVisibility(View.INVISIBLE);
            thirdline1.setVisibility(View.INVISIBLE);
            fourthline1.setVisibility(View.INVISIBLE);

        }
    }


    public void youcansend() {

        //getlatitude();
        //getlongitude();
        try {
            String text = " is to inform you that your Friend is in need of your help. this link genrated by Natink. http://maps.google.com/maps?q=loc:";//+""+ latitude + "," + ""+longitude;
            String message = text + latitude + "," + longitude;
            //    Toast.makeText(getApplicationContext(), latitude + "   " + longitude + "", Toast.LENGTH_SHORT).show();
            //  Toast.makeText(getApplicationContext(), message + "", Toast.LENGTH_SHORT).show();
            for (int k = 0; k < sendmessage.size(); k++) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(sendmessage.get(k), null, "This" + message, null, null);
                //    Toast.makeText(getApplicationContext(), "send hasbeen called", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Message has not been send do report us..!", Toast.LENGTH_SHORT).show();
        }

    }



    public void onRequestPermissionResult()
    {

    }
    private void setvalue() {
        initial++;
        SharedPreferences settings = getSharedPreferences("YOUR_PREF_NAME", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("SNOW_DENSITY",initial);
        editor.commit();

    }

    private void checkvalue() {


        SharedPreferences settings = getSharedPreferences("YOUR_PREF_NAME", 0);
        initial = settings.getInt("SNOW_DENSITY", 0);
    }



    private void savedata() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(sendmessage);
        ArrayAdapter adapter1 = new ArrayAdapter(
                this, R.layout.contacts_list_view, sendmessage);
        listView.setAdapter(adapter1);
        //  Toast.makeText(getApplicationContext(),"content saved ",Toast.LENGTH_SHORT).show();
        editor.putString("task list",json);
        editor.commit();
    }
    private void loaddata() {
        if(sendmessage==null)
        {

        }
        else {

            SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("task list", null);
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            sendmessage = gson.fromJson(json, type);
            if (sendmessage==null) {
                sendmessage = new ArrayList<String>();

            }


            // Toast.makeText(getApplicationContext(), contacts+" contacts", Toast.LENGTH_SHORT).show();
            // Toast.makeText(getApplicationContext(), sendmessage + " sendmessage", Toast.LENGTH_SHORT).show();

        }

    }
}
