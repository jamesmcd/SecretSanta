package com.jaymcd.secretsanta;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;


public class Main extends ActionBarActivity {

    ArrayList<String> names;
    ArrayList<Integer> recievers;
    ListView list;
    String selected;
    DB data;
    Context c;
    int santaID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        c = this;
        Typeface face = Typeface.createFromAsset(getAssets(), "santa.otf");
        recievers = new ArrayList<Integer>();
        names = new ArrayList<String>();
        data = new DB(this);
        data.open();
        names = data.getNames();
        data.close();

        list = (ListView) findViewById(R.id.lvNames);
        TextView title = (TextView) findViewById(R.id.txtTitle);
        TextView noSantas = (TextView) findViewById(R.id.txtNoSanta);
        title.setTypeface(face);
        if (!names.isEmpty()) {
            noSantas.setText("");

            CustomArrayAdapter my_adapter = new CustomArrayAdapter(this, names);
            list.setAdapter(my_adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                    selected = list.getItemAtPosition(position).toString();
                    data.open();
                    santaID = data.getID(selected);
                    int count = data.dbSize();
                    data.close();
                    int RecID = -1;

                    if (count > 0) { //only works if there is more than one santa
                        data.open();
                        RecID = data.getSanta(data.getID(selected));
                        data.close();
                        Intent i = new Intent(getApplicationContext(), result.class);
                        i.putExtra("santa", santaID);
                        i.putExtra("rec", RecID);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please add more Santas", Toast.LENGTH_SHORT).show();
                    }


                }
            });


        } else {
            title.setText("");
        }//if there are users in the list hide instruction


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent i = new Intent(getApplicationContext(), settings.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
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
