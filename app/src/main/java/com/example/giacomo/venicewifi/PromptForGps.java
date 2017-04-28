package com.example.giacomo.venicewifi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;

/**
 * Created by Giacomo on 30/11/2016.
 */

public class PromptForGps {
    Activity mActivity;
    public PromptForGps( Activity activity){
        super();
        mActivity=activity;
    }
    String lingua="";
    String riga1="";
    String riga2="";
    String riga3="";
    String accetta="";
    String continua="";
    public void setTitleActivity(){
        lingua = loadDataDefautl("lingua");
        switch (lingua){
            case "inglese":
                riga1="Enable either GPS or any other location";
                riga2=" service to find current location.  Click OK to go to";
                riga3=" location services settings to let you do so.";
                accetta="OK";
                continua="Continue Anyway";
                break;
            case "italiano":
                riga1="Attivare GPS o altro servizio di";
                riga2=" localizzazione per trovare la posizione corrente. Premi su OK per";
                riga3=" andare ad attivare i servizi di localizzazione.";
                accetta="OK";
                continua="Continua";
                break;
            case "spagnolo":
                riga1="Permitido GPS o cualquier otro servicio de localización para encontrar ";
                riga2="la localización actual. Pulsa OK para ir a Ajustes de servicios de localización";
                riga3=" para permitirle hacerlo.";
                accetta="OK";
                continua="Continuar igualmente";
                break;
            case "portoghese":
                riga1="Permitir GPS ou quaisquer outros serviços de localização para determinar";
                riga2=" a localização atual. Primir OK para ir à definições de partilha de localização";
                riga3=" e alterar as permissões.";
                accetta="OK";
                continua="Ignorar";
                break;


        }
    }
    public String loadDataDefautl(String stringa){
        SharedPreferences sharedPref= mActivity.getSharedPreferences("mypref", 0);
        return  sharedPref.getString(stringa, "");
    }
    public void askForGpsActivation(){
        setTitleActivity();
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(mActivity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        String message = riga1+riga2+riga3;


        builder.setMessage(message)
                .setPositiveButton(accetta,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                mActivity.startActivity(new Intent(action));
                                d.dismiss();
                            }
                        })
                .setNegativeButton(continua,
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface d,int id) {

                                d.dismiss();
                            }
                        });

        builder.create().show();
    }
}
