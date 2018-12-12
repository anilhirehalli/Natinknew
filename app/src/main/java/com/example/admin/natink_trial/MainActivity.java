package com.example.admin.natink_trial;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.provider.Settings;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.pddstudio.easyflashlight.EasyFlashlight;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Integer time, longer = 3500, shorter = 1000;
    MediaPlayer siren, whistl, malevoice, fevoice;
    String uriString;
    String channelId = "channel-01";
    ProgressDialog progressDialog;
    RelativeLayout layout;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    double latitude, longitude;
    AudioManager am;
    ArrayList<String> sendmessage = new ArrayList<String>();
    Boolean siren_on = false, whist = false, male = false, female = false, sos = false;
    static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }


            @Override
            public void onFinish() {

            }
        }.start();
        permissionchecker();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude=location.getLatitude();
                longitude=location.getLongitude();
                Intent ShareingIntent = new Intent(Intent.ACTION_SEND);
                ShareingIntent.setType("text/plain");
                ShareingIntent.putExtra(Intent.EXTRA_TEXT, "This is to inform you that your Friend is in need of your help. this link genrated by Natink\n" +
                        "to locate your friend. " + "\n" + "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude);
                startActivity(ShareingIntent);
                //Toast.makeText(getApplicationContext(),"Its changed",Toast.LENGTH_SHORT).show();
                progressDialog.setCancelable(false);
                progressDialog.dismiss();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 2000.0f, locationListener);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
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
           //
            // locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 300, locationListener);
        }
        instance = this;
        layout = (RelativeLayout) findViewById(R.id.mainer);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        CustomNotification();
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.customnotifications);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        /* EasyFlashlight.init(this);*/
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case  10:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                   // locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 300, locationListener);
                return;
        }
    }
    /*  @Override
      public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
          //For Android M Support, call this in your onRequestPermissionsResult method
          EasyFlashlight.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
      }
  */
    @Override
    public void onStart() {

        CustomNotification();
        super.onStart();
    }

    @Override
    public void onDestroy() {
        try {
            siren.stop();
            siren.release();

        } catch (Exception e) {

        }
        try {
            whistl.stop();
            whistl.release();

        } catch (Exception e) {

        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Overridejbkjbkj
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
    public void shareit(View view) {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait..."); // Setting Message
        progressDialog.setTitle("Getting location"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 300, locationListener);




    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_share) {
            Intent ShareingIntent = new Intent(Intent.ACTION_SEND);
            ShareingIntent.setType("text/plain");
            ShareingIntent.putExtra(Intent.EXTRA_TEXT, "What to do while you are in Danger? " + "\n" + "Use our S.O.S app to get yourself out of danger with the Help of our app Natink" + "\n" + " To download" + "\n" + "https://drive.google.com/open?id=13arPcJphlH1J7ffJdV5FSfHRkiGcvbyT");
            startActivity(ShareingIntent);
        } else if (id == R.id.nav_feedback) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.setPackage("com.google.android.gm");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"engin.mysuru@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Feed back");
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_bug) {
           Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.setPackage("com.google.android.gm");
            // i.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"engin.mysuru@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Reporting a bug .(do include a screen shot)");
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_like) {

            try {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                uriString = "http://m.facebook.com/EngINIndia/";
                sharingIntent.putExtra(Intent.EXTRA_TEXT, uriString);
                sharingIntent.setPackage("com.facebook.katana");
                startActivity(sharingIntent);

//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/FXpB/U9j/xZ"));
                //    Toast.makeText(getApplicationContext(),"try is running",Toast.LENGTH_SHORT).show();

//                startActivity(intent);
            } catch (Exception e) {
                String url = "m.facebook.com/EngINIndia/";
                Uri u = Uri.parse("http://" + url);
                Intent i = new Intent(Intent.ACTION_VIEW, u);
                startActivity(i);
            }
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this, about.class);
            startActivity(intent);
        } else if (id == R.id.nav_addcontacts) {
            Intent intent = new Intent(MainActivity.this, reader.class);
            startActivity(intent);
        } else if (id == R.id.nav_contacts) {
            Intent intent = new Intent(MainActivity.this, emergencylist.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void emergency(View view) {
        Log.d("gps", "clicked");
        Toast.makeText(getApplicationContext(), "SOS pressed", Toast.LENGTH_SHORT).show();
        Intent to = new Intent(MainActivity.this, NotificationView.class);
        startActivity(to);
        locator();
    }

    public void siren(View view) {
        am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        try {
            whistl.stop();
            whistl.release();
        } catch (Exception e) {

        }
        whist = false;
        if (siren_on) {
            try {
                siren.stop();
                siren.release();
            } catch (Exception e) {

            }
            siren_on = false;
        } else {
            siren = MediaPlayer.create(this, R.raw.siren);
            siren.start();
            siren.setLooping(true);
            siren_on = true;
        }

    }

    public void malevoi(View view) {
       am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        try {
            siren.stop();
            siren.release();

        } catch (Exception e) {

        }
        siren_on = false;
        try {
            whistl.stop();
            whistl.release();

        } catch (Exception e) {

        }
        whist = false;
        try {
            fevoice.stop();
            fevoice.release();

        } catch (Exception e) {

        }
        female = false;
        if (male) {
            try {
                malevoice.stop();
                malevoice.release();
            } catch (Exception e) {

            }
            male = false;
        } else {
            malevoice = MediaPlayer.create(this, R.raw.siren);
            malevoice.start();

            male = true;
        }


    }

    public void femalevoi(View view) {
        am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        try {
            siren.stop();
            siren.release();

        } catch (Exception e) {

        }
        siren_on = false;
        try {
            whistl.stop();
            whistl.release();

        } catch (Exception e) {

        }
        whist = false;
        try {
            malevoice.stop();
            malevoice.release();

        } catch (Exception e) {

        }
        male = false;
        if (female) {
            try {
                fevoice.stop();
                fevoice.release();
            } catch (Exception e) {

            }
            female = false;
        } else {
            fevoice = MediaPlayer.create(this, R.raw.whitsle);
            fevoice.start();

            female = true;
        }


    }

    public void whitsle(View view) {
        am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        try {
            siren.stop();
            siren.release();

        } catch (Exception e) {

        }
        siren_on = false;
        try {
            malevoice.stop();
            malevoice.release();

        } catch (Exception e) {

        }
        male = false;
        try {
            fevoice.stop();
            fevoice.release();

        } catch (Exception e) {

        }
        female = false;
        if (whist) {
            try {
                whistl.stop();
                whistl.release();
            } catch (Exception e) {

            }
            whist = false;
        } else {
            whistl = MediaPlayer.create(this, R.raw.whitsle);
            whistl.start();

            whist = true;
        }

    }


    public void flash(View view) {
       /* if(EasyFlashlight.getInstance().canAccessFlashlight()) {
            Thread t = new Thread() {
                public void run() {
                    if (!sos) {
                        try {
                            EasyFlashlight.getInstance().turnOn();
                            sleep(750);
                            EasyFlashlight.getInstance().turnOff();
                            sleep(750);
                            EasyFlashlight.getInstance().turnOn();
                            sleep(750);
                            EasyFlashlight.getInstance().turnOff();
                            sleep(750);
                            EasyFlashlight.getInstance().turnOn();
                            sleep(750);
                            EasyFlashlight.getInstance().turnOff();
                            sleep(750);
                            EasyFlashlight.getInstance().turnOn();
                            sleep(1500);
                            EasyFlashlight.getInstance().turnOff();
                            sleep(1500);
                            EasyFlashlight.getInstance().turnOn();
                            sleep(1500);
                            EasyFlashlight.getInstance().turnOff();
                            sleep(1500);
                            EasyFlashlight.getInstance().turnOn();
                            sleep(1500);
                            EasyFlashlight.getInstance().turnOff();
                            sleep(1500);
                            EasyFlashlight.getInstance().turnOn();
                            sleep(750);
                            EasyFlashlight.getInstance().turnOff();
                            sleep(750);
                            EasyFlashlight.getInstance().turnOn();
                            sleep(750);
                            EasyFlashlight.getInstance().turnOff();
                            sleep(750);
                            EasyFlashlight.getInstance().turnOn();
                            sleep(750);
                            EasyFlashlight.getInstance().turnOff();
                            sleep(750);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        sos = true;
                    } else {
                        EasyFlashlight.getInstance().turnOff();
                        sos=false;
                    }
                }


            };
            t.start();

        }else{
            Snackbar snackbar = Snackbar
                    .make(layout, "Sorry,But we cannot find Flashlight function in your phone", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
*/
    }

    public void emergencyCont(View view) {
        Intent intent = new Intent(MainActivity.this, emergencylist.class);
        startActivity(intent);
    }

    public void CustomNotification() {
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.customnotifications);


        // Open NotificationView Class on Notification Click
       /* Intent intent = new Intent(this, NotificationView.class);*/
        // Send data to NotificationView Class
        // Open NotificationView.java Activity
       /* PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);*/

        Notification notification = new Notification(R.drawable.engin, null, System.currentTimeMillis());

        notification.flags |= Notification.FLAG_NO_CLEAR;

        //this is the intent that is supposed to be called when the button is clicked
        Intent switchIntent = new Intent(this, switchButtonListener.class);
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(this, 0, switchIntent, 0);

        remoteViews.setOnClickPendingIntent(R.id.imagenotiright, pendingSwitchIntent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.engin)
                // .setContentIntent(pIntent)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setChannelId(channelId)
                .setContent(remoteViews);


        // Locate and set the Image into customnotificationtext.xml ImageViews

        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());


    }


    public void permissionchecker() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.GET_ACCOUNTS,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.CHANGE_NETWORK_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            setSnackBar(layout);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
        loaddata();
        if(sendmessage.isEmpty())
        {
            Intent i = new Intent(MainActivity.this,emergencylist.class);
            startActivity(i);
        }
    }

    public void setSnackBar(View coordinatorLayout) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "If permissions are not enabled then we won't be able work", Snackbar.LENGTH_INDEFINITE)
                .setAction("ACCEPT", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        permissionchecker();
                    }
                });

        snackbar.show();

    }

    /*public void locationlocator() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                locator();
                /*try {
                    sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
/*
            }
        };
        thread.start();
    }*/

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    public void locator() {
      /*  if (checkPermissions()) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            Log.d("gps", "inside");
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {
                    Location location = new Location(LocationManager.GPS_PROVIDER);
                    Log.d("gps", "checking ");
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    //  Toast.makeText(getApplicationContext(),"on provide enabled is called",Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), latitude + " latitdue " + longitude + "longitude", Toast.LENGTH_SHORT).show();
                    Log.d("gps", latitude + " latitdue " + longitude + "longitude");

                }

                @Override
                public void onProviderDisabled(String provider) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            };

        } else {
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            locator();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            // check for permanent denial of permission
                            if (response.isPermanentlyDenied()) {

                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        }
*/
    }

    public void bugger(View view) {
       Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.setPackage("com.google.android.gm");
        // i.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"engin.mysuru@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Reporting a bug .(do include a screen shot)");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }



    public void feedback(View view) {
       Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.setPackage("com.google.android.gm");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"engin.mysuru@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Feed back");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public static class switchButtonListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("test","test");
            instance.tonoti();
        }
    }
    public void tonoti(){

       Intent to = new Intent(MainActivity.this, NotificationView.class);
        startActivity(to);
       // finish();
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


