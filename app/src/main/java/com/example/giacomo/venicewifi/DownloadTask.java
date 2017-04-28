package com.example.giacomo.venicewifi;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Giacomo on 30/11/2016.
 */

    public class DownloadTask extends AsyncTask<String, Void, String> {
    Activity mActivity;
        public DownloadTask( Activity activity) {
            super();
            mActivity=activity;

        }

        protected String doInBackground(String... url) {
            String data = "";

            try {
                DownloadUrl getUrl = new DownloadUrl();
                data = getUrl.downloadUrl(url[0]);
            } catch (Exception var4) {
                Log.d("Background Task", var4.toString());
            }

            return data;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask(mActivity);
            parserTask.execute(new String[]{result});
        }
    }

