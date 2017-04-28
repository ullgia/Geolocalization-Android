package com.example.giacomo.venicewifi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import static com.example.giacomo.venicewifi.ActivityMaps.currentCity;
import static com.example.giacomo.venicewifi.ActivityMaps.languageDefault;


public class ActivitySelectLanguage extends AppCompatActivity implements View.OnClickListener {
    String lingua ="";
    String defaultCity ="";
    String titolo ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);
        setTitleActivity();
        Button bottoneITA =(Button)findViewById(R.id.Italian);
        Button bottoneES = (Button)findViewById(R.id.Spanish);
        Button bottonePT = (Button)findViewById(R.id.Portuguese);
        Button bottoneENG = (Button)findViewById(R.id.English);
        bottoneITA.setOnClickListener(this);
        bottoneES.setOnClickListener(this);
        bottonePT.setOnClickListener(this);
        bottoneENG.setOnClickListener(this);

    }

    public void setIntent() {
        defaultCity = loadDataDefautl("city");
        saveDefaultLanguage();

        if (defaultCity == "") {
            Intent i = new Intent(ActivitySelectLanguage.this, ActivityDefaultCity.class);
            ActivitySelectLanguage.this.startActivity(i);
            ActivitySelectLanguage.this.finish();
        }else {
            Intent defaultInte = new Intent(ActivitySelectLanguage.this, ActivityMaps.class);
            if(currentCity=="")
                defaultInte.putExtra("city",defaultCity);
            else
                defaultInte.putExtra("city",currentCity);
            ActivitySelectLanguage.this.startActivity(defaultInte);
            ActivitySelectLanguage.this.finish();

        }
    }
    public String loadDataDefautl(String stringa){
        SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
        return  sharedPref.getString(stringa, "");
    }
    public void setTitleActivity(){
        lingua = loadDataDefautl("lingua");
        switch (lingua){
            case "":
                titolo = "Choose the Default Language";
                this.setTitle(titolo);
                break;
            case "inglese":
                titolo = "Choose the Default Language";
                this.setTitle(titolo);
                break;
            case "italiano":
                titolo = "Scegliere la lingua predefinita";
                this.setTitle(titolo);
                break;
            case "spagnolo":
                titolo = "Seleccionar el idioma por defecto";
                this.setTitle(titolo);
                break;
            case "portoghese":
                titolo = "Escolha o idioma padrão";
                this.setTitle(titolo);
                break;


        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Italian:
                lingua = "italiano";
                setIntent();
                break;
            case R.id.Portuguese:
                lingua = "portoghese";
                setIntent();
                break;
            case R.id.Spanish:
                lingua = "spagnolo";
                setIntent();
                break;
            case R.id.English:
                lingua = "inglese";
                setIntent();
                break;
        }
    }
    public void saveDefaultLanguage(){
        // Create object of SharedPreferences.
        SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
        //now get Editor
        SharedPreferences.Editor editor= sharedPref.edit();
        languageDefault = lingua;
        //put your value
        editor.putString("lingua", lingua);
        //commits your edits
        editor.commit();
    }
}
