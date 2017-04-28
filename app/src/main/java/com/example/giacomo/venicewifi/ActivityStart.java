package com.example.giacomo.venicewifi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import static com.example.giacomo.venicewifi.ActivityMaps.LOCATION;

public class ActivityStart extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    String defaultCity ="";
    String lingua = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        this.setTitle("ciao");
     /*   Window window = ActivityStart.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(ActivityStart.this, R.color.colorPrimary));
    */  AskForPermissions ask = new AskForPermissions();
        ask.askForPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, LOCATION, ActivityStart.this);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                setIntent();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
    public void setIntent(){
        defaultCity = loadDataDefautl("city");
        lingua = loadDataDefautl("lingua");
        if (lingua == ""){
            Intent i = new Intent(ActivityStart.this, ActivitySelectLanguage.class);
            ActivityStart.this.startActivity(i);
            ActivityStart.this.finish();
        }else {
            if (defaultCity == "") {
                Intent i = new Intent(ActivityStart.this, ActivityDefaultCity.class);
                ActivityStart.this.startActivity(i);
                ActivityStart.this.finish();
            } else {
                Intent defaultInte = new Intent(ActivityStart.this, ActivityMaps.class);
                defaultInte.putExtra("city", defaultCity);
                ActivityStart.this.startActivity(defaultInte);
                ActivityStart.this.finish();

            }
        }
    }

    public String loadDataDefautl(String defaultLoad){
        SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
        return  sharedPref.getString(defaultLoad, "");
    }
}

