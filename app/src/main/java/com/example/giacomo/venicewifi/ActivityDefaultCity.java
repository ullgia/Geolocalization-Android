package com.example.giacomo.venicewifi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static com.example.giacomo.venicewifi.ActivityMaps.currentCity;


public class ActivityDefaultCity extends AppCompatActivity implements View.OnClickListener {
    String defaultCity="";
    String lingua ="";
    String titolo="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_city);
        setTitleActivity();
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

    }

    public void setIntent() {
        Intent i = new Intent(ActivityDefaultCity.this, ActivityMaps.class);
        i.putExtra("city", defaultCity);
        currentCity = defaultCity;
        saveDefaultCity();
        ActivityDefaultCity.this.startActivity(i);
        ActivityDefaultCity.this.finish();
    }
    public void setTitleActivity(){
        lingua = loadDataDefautl("lingua");
        switch (lingua){
            case "inglese":
                titolo = "Choose the Default City";
                this.setTitle(titolo);
                break;
            case "italiano":
                titolo = "Scegliere la città predefinita";
                this.setTitle(titolo);
                break;
            case "spagnolo":
                titolo = "Elegir lugar predeterminado";
                this.setTitle(titolo);
                break;
            case "portoghese":
                titolo = "Escolha a cidade padrão";
                this.setTitle(titolo);
                break;


        }
    }
    public String loadDataDefautl(String stringa){
        SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
        return  sharedPref.getString(stringa, "");
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
