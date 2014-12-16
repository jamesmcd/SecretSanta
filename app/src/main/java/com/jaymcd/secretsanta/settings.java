package com.jaymcd.secretsanta;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;


public class settings extends ActionBarActivity {
    DB data;
    EditText addText;
    ArrayList<String> names;
    ArrayList <Integer> getIDs;
    ListView list;
    String selected;
    Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Typeface face =Typeface.createFromAsset(getAssets(),"santa.otf");

        getIDs = new ArrayList<Integer>();
        data  = new DB(this);
        names = new ArrayList<String>();
        list = (ListView)findViewById(R.id.lvNames);
        TextView title = (TextView)findViewById(R.id.txtAddSanta);
        title.setTypeface(face);
        addText = (EditText)findViewById(R.id.edAdd);
        Button addButton = (Button)findViewById(R.id.btnAdd);
        addButton.setOnClickListener(new addListener());
        c = this;
        data.open();
        names = data.getNames();
        getIDs= data.getIDs();
        data.close();
        CustomArrayAdapter my_adapter = new CustomArrayAdapter(this, names);
        list.setAdapter(my_adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {


                selected = list.getItemAtPosition(position).toString();

                new AlertDialog.Builder(c)
                        .setMessage("Delete "+selected+"?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                data.open();
                                data.delRecord(selected);
                                data.close();

                                names.remove(selected); //remove the name from the list
                                CustomArrayAdapter my_adapter = new CustomArrayAdapter(getApplicationContext(), names);
                                list.setAdapter(my_adapter);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });



    }



    private class addListener implements View.OnClickListener {
        public void onClick(View v){
            String name = addText.getText().toString();
        if (name.equals("")){
            Toast.makeText(getApplicationContext(),"Please enter a name", Toast.LENGTH_SHORT).show();

        }else {
            data.open();
            data.addName(name);
            data.close();
            names.add(addText.getText().toString());
            CustomArrayAdapter my_adapter = new CustomArrayAdapter(getApplicationContext(), names);
            list.setAdapter(my_adapter);
            addText.setText("");
        }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
            save();
        }else if (id == R.id.clear){
            data.open();
            data.delAllRecords();
            data.close();
            names.clear();

            CustomArrayAdapter my_adapter = new CustomArrayAdapter(getApplicationContext(), names);
            list.setAdapter(my_adapter);
            addText.setText("");
        }

        return super.onOptionsItemSelected(item);
    }

    private void save(){
        data.open();
        getIDs= data.getIDs();
        data.close();
        int length = getIDs.size();
        getIDs = Rand(getIDs);
        Log.i("IDS: ", getIDs.toString());
        for(int i = 0; i<length; i++){
            data.open();
            data.setSanta(i+1,getIDs.get(i));
            data.close();
        }


        Intent i = new Intent(getApplicationContext(),Main.class);
        startActivity(i);
        finish();
    }

    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
       save();
    }

    private ArrayList<Integer> Rand (ArrayList<Integer> x){
        ArrayList<Integer> result = new ArrayList<Integer>();
        for(int i = 0; i<x.size(); i++){
            int y = i+1;
            if(y<x.size()){
            result.add(x.get(y));}
            else{
                result.add(x.get(0));
            }
        }

        return result;
    }

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
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
