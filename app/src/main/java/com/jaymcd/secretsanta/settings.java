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
import android.view.KeyEvent;
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
import android.view.View.OnKeyListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Random;


public class settings extends ActionBarActivity {
    DB data;
    EditText addText;
    Button addButton;
    ArrayList<String> names;
    ArrayList <Integer> getIDs;
    ListView list;
    String selected;
    Context c;
    int [] IDs;
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
        addButton = (Button)findViewById(R.id.btnAdd);
        addButton.setTypeface(face);
        addButton.setOnClickListener(new addListener());

        c = this;

        addText.setOnKeyListener(new OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            String name = addText.getText().toString();
                            addName(name);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

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
          addName(name);
        }
        }
    }

    private void addName(String name){

        data.open();
        data.addName(name);
        data.close();
        names.add(addText.getText().toString());
        CustomArrayAdapter my_adapter = new CustomArrayAdapter(getApplicationContext(), names);
        list.setAdapter(my_adapter);
        addText.setText("");

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
        if (id == R.id.save) {
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
        Log.i("ID BEFORE", getIDs.toString());
        IDs = new int[getIDs.size()];
        for (int i = 0; i<getIDs.size(); i++){
            IDs[i]=getIDs.get(i);
        }

        if(getIDs.size() > 1) {
            Log.i("SIZE",String.valueOf(getIDs.size()));
            while (hasDuplicate()) { //Shuffles while there are duplicates
                shuffleList(getIDs);
            }
        }
        for (int x = 0; x < getIDs.size(); x++) {
            data.open();
            data.setSanta(IDs[x], getIDs.get(x));
            data.close();
        }

        Log.i("ID AFTER", getIDs.toString());
        Intent i = new Intent(getApplicationContext(),Main.class);
        startActivity(i);
        finish();
    }

    public void onBackPressed() {
        save();
    }

    private boolean hasDuplicate(){
        for (int i = 0; i < getIDs.size(); i++){
            if(IDs[i] == getIDs.get(i)){ //if the shuffled array matches the original
                return true;
            }
        }

        return false;
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


    /* The code below uses the Fisher-Yates method of shuffling */
    public static void shuffleList(ArrayList<Integer> a) {
        Log.i("SHUFFLE: ","SHUFFLING");
        int n = a.size();
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            swap(a, i, change);
        }
    }

    private static void swap(ArrayList<Integer> a, int i, int change) {
        int helper = a.get(i);
        a.set(i, a.get(change));
        a.set(change, helper);
    }


}
