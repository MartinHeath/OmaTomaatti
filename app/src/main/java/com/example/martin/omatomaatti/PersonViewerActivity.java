package com.example.martin.omatomaatti;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.martin.omatomaatti.BankIdClass.Account;
import com.example.martin.omatomaatti.BankIdClass.Person;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PersonViewerActivity extends AppCompatActivity {
    //same old list variable
    ArrayList list;
    ArrayAdapter<String> listAdapter ;
    Person per = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_viewer);

        Intent intent = getIntent();
        String name = intent.getStringExtra(MainScreen.EXTRA_INDEX);
        read();
        fillContent(name);
    }

    public void read(){
        try{
            list= new ArrayList();
            FileInputStream fis = openFileInput("data.txt");
            BufferedReader br =  new BufferedReader(new InputStreamReader(fis));
            String  line = null;
            while ((line = br.readLine()) != null) {
                //reading one person at a time
                //the first line SHOULD be the name of the new person created
                Person p = new Person(line);
                while ((line = br.readLine()) != null && !(line.equals("-END-"))){
                    //creating accounts by splitting line into two and creating person
                    String parts[] = line.split(" ");
                    Account ac = new Account(parts[0].trim(), parts[1].trim());
                    //adding account to person
                    p.addAccont(ac);
                }
                //adding finished person to list
                list.add(p);
            }
            fis.close();
            br.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void fillContent(String name){
        //finding the right person
        for(int i = 0; i<list.size(); i++){
            Person dude = (Person)list.get(i);
            if(dude.getName().equals(name)){
                per = dude;

                TextView n = (TextView)findViewById(R.id.perName);
                ListView ac = (ListView)findViewById(R.id.acList);
                listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow);

                n.setText(dude.getName());
                ArrayList l = dude.getArray();
                //TODO 12.2.2016 fix array printing
                for(int y = 0; y < l.size(); y++){
                    Account a = (Account)l.get(y);
                    listAdapter.add(a.getName() + "\t - \t" + a.getNum());
                }
                ac.setAdapter(listAdapter);
                break;
            }
        }
    }
    public void goBack(View v){
        Intent it = new Intent(v.getContext(), MainScreen.class);
        startActivity(it);
    }

    public void deletePerson(View v){
        //finding the person from list and deleting them.
        for(int i =0; i<list.size(); i++){
            Person d = (Person)list.get(i);
            if(d.getName().equals(per.getName())){
                list.remove(i);
            }
        }
        //after whole list has been checked, save it to file.
        saveList();
    }
    public void saveList(){
        String filename = "data.txt";
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            for(int i = 0; i< list.size(); i++){
                Person p = (Person)list.get(i);
                outputStream.write(p.toString().getBytes());
            }
            outputStream.close();
            byebye(this.findViewById(android.R.id.content));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void byebye(View view){
        final View v = view;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert
                .setTitle("Posto onnistui")
                .setMessage("Henkilön poisto onnistui!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(v.getContext(), MainScreen.class);
                        startActivity(i);
                    }
                })
                .show();
    }
}
