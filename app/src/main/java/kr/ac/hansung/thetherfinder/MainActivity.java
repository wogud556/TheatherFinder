package kr.ac.hansung.thetherfinder;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mfirebaseAuth;
    private Fragment fragment;
    private FirebaseUser mfirebaseUser;
    private TextView txtname, txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mfirebaseAuth = FirebaseAuth.getInstance();
        mfirebaseUser = mfirebaseAuth.getCurrentUser();

        if(mfirebaseUser == null){
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
            finish();
            return;
        }

        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headherView = navigationView.getHeaderView(0);
        MapFragment mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.mainFragment, mapFragment, "main").commit();
        txtEmail = (TextView)headherView.findViewById(R.id.txtEmail);
        txtname = (TextView)headherView.findViewById(R.id.txtname);
        navigationView.setNavigationItemSelectedListener(this);
        profileUpdate();

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

    @Override
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
            Toast.makeText(MainActivity.this, "TheatherFinder\n 컴퓨터공학과 1492059 박재형, 1492076 장주영, 1492080 조준혁\n 사용 플랫폼 : Java, Firebase Auth, Firebase Realtime Database\n Google Map Api", Toast.LENGTH_LONG).show();
        }else if(id == R.id.action_logout){

            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        View v;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.best_movie) {

        }else if(id == R.id.now_movie){

        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void profileUpdate(){
        txtEmail.setText(mfirebaseUser.getEmail());
        txtname.setText(mfirebaseUser.getDisplayName());
    }
    public void logout(){
        mfirebaseAuth.signOut();
        finish();
        startActivity(new Intent(MainActivity.this, AuthActivity.class));
    }


}
