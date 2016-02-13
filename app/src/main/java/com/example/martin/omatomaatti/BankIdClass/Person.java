package com.example.martin.omatomaatti.BankIdClass;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class Person {

    private String name;
    private ArrayList accountList = new ArrayList();

    public Person(String n){
        name = n;
    }

    public String getName(){
        return name;
    }
    public ArrayList getArray(){
        return accountList;
    }
    public void addAccont(Account ac){
        accountList.add(ac);
    }

    public void setAccount(Account ac){
        ArrayList list = getArray();
        boolean wasOnList = false;
        for(int i = 0; i<list.size(); i++){
            Account a = (Account)list.get(i);
            if(a.getName().equals(ac.getName())){
                list.remove(i);
                list.add(i, ac);
                wasOnList= true;
            }
        }
        if(!wasOnList){
            addAccont(ac);
        }

    }

    public String toString(){
        String text;
        text = this.getName() + "\n";
        ArrayList array = this.getArray();
        if( accountList != null){
            for(int i=0; i<array.size(); i++){
                Account a = (Account) array.get(i);
                text += a.toString() +"\n";
            }
        }
        text += "-END-\n";
        return text;
    }
}
