package com.example.giacomo.venicewifi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ActivityTutorialCity extends AppCompatActivity implements View.OnClickListener {
    String url ="";
    String txt ="";
    String lingua ="";
    String titolo ="";
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_city);
        Button bottoneGenova  =  (Button)findViewById(R.id.Genova);
        Button bottoneRoma =(Button)findViewById(R.id.Roma);
        Button bottoneVenezia = (Button)findViewById(R.id.Venice);
        Button bottoneReggio = (Button)findViewById(R.id.Reggio);
        Button bottoneMilano = (Button)findViewById(R.id.Milano);
        Button bottonePerugia = (Button)findViewById(R.id.Perugia);
        Button bottoneLecce = (Button)findViewById(R.id.Lecce);
        textView = (TextView)findViewById(R.id.testoMultiLingua);
        bottoneGenova.setOnClickListener(this);
        bottoneLecce.setOnClickListener(this);
        bottoneRoma.setOnClickListener(this);
        bottoneVenezia.setOnClickListener(this);
        bottoneReggio.setOnClickListener(this);
        bottoneMilano.setOnClickListener(this);
        bottonePerugia.setOnClickListener(this);
        setTitleActivity();

    }

    public void setIntent() {
        final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url));
        ActivityTutorialCity.this.startActivity(intent);
    }
    public String loadDataDefautl(String stringa){
        SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
        return  sharedPref.getString(stringa, "");
    }
    public void setTitleActivity(){
        lingua = loadDataDefautl("lingua");
        switch (lingua){
            case "":
                titolo = "What info do you need?";
                txt = "Please select one the city below to get information on how to connect on its HotSpot WiFi";
                this.setTitle(titolo);
                textView.setText(txt);
                break;
            case "inglese":
                titolo = "What info do you need?";
                txt ="Please select one the city below to get information on how to connect on its HotSpot WiFi";
                this.setTitle(titolo);
                textView.setText(txt);
                break;
            case "italiano":
                titolo = "Di che informazioni hai bisogno?";
                txt ="Si prega di selezionare una città sottostante, per avere informazioni su come collegarsi al corrispondente WiFi HotSpot";
                this.setTitle(titolo);
                textView.setText(txt);
                break;
            case "spagnolo":
                titolo = "¿Qué información se necesita?";
                txt ="Por favor, selecione una de las siguientes ciudades para obtener información sobre como conectar con el punto wifi";
                this.setTitle(titolo);
                textView.setText(txt);
                break;
            case "portoghese":
                titolo = "Que informação necessita?";
                txt ="Por favor, seleccione uma das seguintes cidades para obter as informações de como se conectar ao Hotspot";
                this.setTitle(titolo);
                textView.setText(txt);
                break;


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Venice:
                url = "https://www.cittadinanzadigitale.it/";
                setIntent();
                break;
            case R.id.Milano:
                url = "http://info.openwifimilano.it/it/ComeAccedere.aspx";
                setIntent();
                break;
            case R.id.Lecce:
                url = "http://www.leccecittawireless.it/site/connettersi/";
                setIntent();
                break;
            case R.id.Genova:
                url = "http://www.comune.genova.it/wifi";
                setIntent();
                break;
            case R.id.Perugia:
                url = "http://www.umbriadigitale.it/umbria-wifi-free";
                setIntent();
                break;
            case R.id.Reggio:
                url = "http://www.municipio.re.it/retecivica/urp/retecivi.nsf/PESDocumentID/1BEECF14DC155CCCC1257E0A004A863B?opendocument&FROM=Cmcnnttrt";
                setIntent();
                break;
            case R.id.Roma:
                url = "http://www.mappawifi.cittametropolitanaroma.gov.it/";
                setIntent();
                break;

        }
    }

}
