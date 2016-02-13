package com.example.martin.omatomaatti;

import com.example.martin.omatomaatti.BankIdClass.Account;
import com.example.martin.omatomaatti.BankIdClass.Person;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainScreen extends AppCompatActivity {
    private ArrayList list;
    public final static String EXTRA_ARRAY = "com.example.martin.omatomaatti.ARRAY";
    public final static String EXTRA_INDEX = "com.martin.omatomaatti.INDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //load possible saved data
        File file = new File(this.getFilesDir(), "data.txt");
        //file.delete();
        if(file.exists()){
            read();
            printList();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        File file = new File(this.getFilesDir(), "data.txt");
        if(file.exists()){
            read();
            printList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addNewPerson(View view){
        Intent newPerson = new Intent(this, EditorViewActivity.class);
        startActivity(newPerson);
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
    public void printList(){
        if(list != null){
            TableLayout tbl = (TableLayout)findViewById(R.id.table);
            //clear layout of old icons
            tbl.removeAllViews();
            for (int i = 0; i < list.size(); i++) {
                TableRow row = new TableRow(this);
                Person p = (Person)list.get(i);
                Button btn = new Button (MainScreen.this);
                btn.setWidth(80);
                btn.setHeight(40);
                btn.setText(p.getName());

                btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Button b = (Button)v;
                        String name =  b.getText().toString();
                        Intent intent = new Intent(v.getContext(), PersonViewerActivity.class);
                        intent.putExtra(EXTRA_INDEX, name);
                        startActivity(intent);
                    }
                });

                row.addView(btn);
                tbl.addView(row);
            }
        }
    }
}
