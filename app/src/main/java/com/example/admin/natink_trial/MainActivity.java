package com.example.admin.natink_trial;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String uriString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    /*@Override
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_share) {
            Intent ShareingIntent = new Intent(Intent.ACTION_SEND);
            ShareingIntent.setType("text/plain");
            ShareingIntent.putExtra(Intent.EXTRA_TEXT, "What to do while you are in Danger? "+"\n"+"Use our S.O.S app to get yourself out of danger with the Help of our app Natink" + "\n" + " To download" + "\n" + "https://drive.google.com/open?id=0B7jzP24_Ndx1YWY4MXliQW4wcU0");
            startActivity(ShareingIntent);
        } else if (id == R.id.nav_feedback) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.setPackage("com.google.android.gm");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"engin.mysuru@gmail.com"});
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
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"engin.mysuru@gmail.com"});
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
                sharingIntent.putExtra(Intent.EXTRA_TEXT,uriString);
                sharingIntent.setPackage("com.facebook.katana");
                startActivity(sharingIntent);

//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/FXpB/U9j/xZ"));
                //    Toast.makeText(getApplicationContext(),"try is running",Toast.LENGTH_SHORT).show();

//                startActivity(intent);
            } catch(Exception e) {
                String url="m.facebook.com/EngINIndia/";
                Uri u= Uri.parse("http://"+url);
                Intent i=new Intent(Intent.ACTION_VIEW,u);
                startActivity(i);
            }
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this, about.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_addcontacts) {
           Intent intent = new Intent(MainActivity.this, reader.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_contacts) {
            Intent intent = new Intent(MainActivity.this, emergencylist.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void emergency(View view) {
        CustomNotification();
    }
    public void CustomNotification() {
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.customnotifications);



        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(this, NotificationView.class);
        // Send data to NotificationView Class
        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.engin)
                .setContentIntent(pIntent)
                .setOngoing(true)

                .setContent(remoteViews);

        // Locate and set the Image into customnotificationtext.xml ImageViews
        remoteViews.setImageViewResource(R.id.imagenotileft,R.drawable.ic_launcher);
        remoteViews.setImageViewResource(R.id.imagenotiright,R.drawable.androidhappy);

        // Locate and set the Text into customnotificationtext.xml TextViews
        remoteViews.setTextViewText(R.id.title,"kiinun  ");
        remoteViews.setTextViewText(R.id.text,"customnotificationtext");

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());

    }
}
