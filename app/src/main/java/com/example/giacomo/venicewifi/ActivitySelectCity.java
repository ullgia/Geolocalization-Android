package com.example.giacomo.venicewifi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import static com.example.giacomo.venicewifi.ActivityMaps.currentCity;


public class ActivitySelectCity extends AppCompatActivity implements View.OnClickListener {
    String defaultCity="";
    String titolo ="";
    String lingua ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        Button bottoneGenova  =  (Button)findViewById(R.id.Genova);
        Button bottoneRoma =(Button)findViewById(R.id.Roma);
        Button bottoneVenezia = (Button)findViewById(R.id.Venice);
        Button bottoneReggio = (Button)findViewById(R.id.Reggio);
        Button bottoneMilano = (Button)findViewById(R.id.Milano);
        Button bottonePerugia = (Button)findViewById(R.id.Perugia);
        Button bottoneLecce = (Button)findViewById(R.id.Lecce);
        bottoneGenova.setOnClickListener(this);
        bottoneLecce.setOnClickListener(this);
        bottoneRoma.setOnClickListener(this);
        bottoneVenezia.setOnClickListener(this);
        bottoneReggio.setOnClickListener(this);
        bottoneMilano.setOnClickListener(this);
        bottonePerugia.setOnClickListener(this);
        setTitleActivity();

    }
    public void setTitleActivity(){
        lingua = loadDataDefautl("lingua");
        switch (lingua){
            case "inglese":
                titolo = "Where do you want to go?";
                this.setTitle(titolo);
                break;
            case "italiano":
                titolo = "Dove vuoi andare?";
                this.setTitle(titolo);
                break;
            case "spagnolo":
                titolo = "¿Dónde quieres ir?";
                this.setTitle(titolo);
                break;
            case "portoghese":
                titolo = "Onde você quer ir?";
                this.setTitle(titolo);
                break;


        }
    }
    public String loadDataDefautl(String stringa){
        SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
        return  sharedPref.getString(stringa, "");
    }

    public void setIntent() {
        Intent i = new Intent(ActivitySelectCity.this, ActivityMaps.class);
        i.putExtra("city", defaultCity);
        currentCity = defaultCity;
        //saveDefaultCity();
        ActivitySelectCity.this.startActivity(i);
        //ActivityDefaultCity.this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Venice:
                defaultCity = "venice";
                setIntent();
                break;
            case R.id.Milano:
                defaultCity = "milan";
                setIntent();
                break;
            case R.id.Lecce:
                defaultCity = "lecce";
                setIntent();
                break;
            case R.id.Genova:
                defaultCity = "genova";
                setIntent();
                break;
            case R.id.Perugia:
                defaultCity = "perugia";
                setIntent();
                break;
            case R.id.Reggio:
                defaultCity = "reggio";
                setIntent();
                break;
            case R.id.Roma:
                defaultCity = "rome";
                setIntent();
                break;

        }
    }
    public void saveDefaultCity(){
        // Create object of SharedPreferences.
        SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
        //now get Editor
        SharedPreferences.Editor editor= sharedPref.edit();
        //put your value
        editor.putString("city", defaultCity);
        //commits your edits
        editor.commit();
    }
}
