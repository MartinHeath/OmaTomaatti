package com.example.martin.omatomaatti;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.martin.omatomaatti.BankIdClass.Account;
import com.example.martin.omatomaatti.BankIdClass.Person;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class EditorViewActivity extends AppCompatActivity {
    ArrayList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_view);

        File file = new File(this.getFilesDir(), "data.txt");
        if(file.exists()){
            read();
        }
    }

    public void save(View view){
        String filename = "data.txt";
        FileOutputStream outputStream;

        //input variables
        EditText personName = (EditText) findViewById(R.id.PersonName);
        EditText accountName = (EditText) findViewById(R.id.AccountName);
        EditText accountNum = (EditText) findViewById(R.id.AccountNumber);

        // cheking input for errors
        if(checkInput(personName, accountName, accountNum)){
            String pn = personName.getText().toString().trim();
            String an = accountName.getText().toString().replaceAll("\\s","");
            String anu = accountNum.getText().toString().trim();
            //create new Person and Account
            Person p = new Person(pn);
            Account account = new Account (an, anu);
            p.addAccont(account);

            //checking data
            if(dataOk(p)){
                try {
                    //let´s see if the file already exists
                    File f = new File(this.getFilesDir(), "data.txt");
                    if(f.exists()){
                        outputStream = openFileOutput(filename, Context.MODE_PRIVATE| MODE_APPEND);
                    }
                    else{
                        outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    }
                    outputStream.write(p.toString().getBytes());
                    outputStream.close();
                    byebye(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("HÖH");
            }
        }
    }
    public void read(){
        try{
            list = new ArrayList();
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
                    Account ac = new Account(parts[0], parts[1]);
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
    public boolean dataOk( Person p){
        //first we check if the list is empty, or if the person exists
        if(list != null){
            for (int i = 0; i < list.size(); i++){
                Person dude = (Person)list.get(i);
                if(dude.getName().equals(p.getName())){
                    replacePerAlarm("Nimi käytössä", "Korvataanko henkilön tiedot?", p);
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    public void replacePerAlarm(String msg, String title, Person p){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final Person dude = p;
        builder
                .setTitle(title)
                .setMessage(msg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNegativeButton("Ei", null)                        //Do nothing on no
                .setPositiveButton("Kyllä", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        replacePerson(dude);
                    }
                })
                .show();

    }
    public void goBack(View v){
        Intent it = new Intent(v.getContext(), MainScreen.class);
        startActivity(it);
    }

    //method for replacing old data of person with new. VERY similar to Save
    //TODO Refactor into better function
    public void replacePerson(Person p){
        //we replace old data in the list and then save it to the file, replacing any old, out of date, data.
        //finding the person from list and editing said person.
        for(int i =0; i<list.size(); i++){
            Person d = (Person)list.get(i);
            if(d.getName().equals(p.getName())){
                //adding and editing accounts
                ArrayList acs = p.getArray();
                for(int y=0; y<acs.size(); y++){
                    d.setAccount((Account)acs.get(i));
                }
                //remove old, add new
                list.remove(i);
                list.add(i, d);
            }
        }
        //after whole list has been checked, save it to file.
        saveList();

    }
    public boolean checkInput(EditText name, EditText ac, EditText acnu ){
        //first check of any of the inputs are empty
        if( name.getText().toString().trim().equalsIgnoreCase("")){
            name.setError("Osio ei voi olla tyhjä. Syötä esim: Erkki Esimerkki");
            return false;
        }
        else if( ac.getText().toString().trim().equalsIgnoreCase("")){
            ac.setError("Osio ei voi olla tyhjä. Syötä esim: käyttö");
            return false;
        }
        else if(acnu.getText().toString().trim().equalsIgnoreCase("")){
            acnu.setError("Osio ei voi olla tyhjä. Syötä tilitieto esim. muodossa FI11 2233 4455 6677 88");
            return false;
        }
        //none were empty, cheking bank account data
        else if( acnu.getText().toString().trim().length() != 18){
            acnu.setError("Tilinumeron pituus ei ole oikea. Ole hyvä ja tarkista syöte.");
            return false;
        }
        else{
            return true;
        }
    }

    public void byebye(View view){
        final View v = view;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert
                .setTitle("Lisäys onnistui")
            .setMessage("Henkilön lisäys onnistui!")
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(v.getContext(), MainScreen.class);
                    startActivity(i);
                }
            })
            .show();
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
}
