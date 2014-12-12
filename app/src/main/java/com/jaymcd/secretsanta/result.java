package com.jaymcd.secretsanta;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class result extends ActionBarActivity {

    DB data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        Typeface face =Typeface.createFromAsset(getAssets(),"santa.otf");
        data = new DB(this);
        Bundle i = getIntent().getExtras();
        int rec = i.getInt("rec"); //Integer.valueOf(i.getStringExtra("rec"));
        int santa = i.getInt("santa");
        Log.i("REC ID", String.valueOf(rec));
        Log.i("SANTA ID", String.valueOf(santa));
        data.open();
       String name = data.getName(rec);
        data.close();
        TextView secret = (TextView)findViewById(R.id.txtSecret);
        TextView title = (TextView)findViewById(R.id.txtTitle);
        title.setTypeface(face);
        secret.setTypeface(face);
        secret.setText(name);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.back) {
            goBack();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        Intent i = new Intent(getApplicationContext(), Main.class);

        startActivity(i);
        finish();
    }

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_result, container, false);
            return rootView;
        }
    }

    public static class AdFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_ad, container, false);
        }

        @Override
        public void onActivityCreated(Bundle bundle) {
            super.onActivityCreated(bundle);
            AdView mAdView = (AdView) getView().findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }
    }
}
