package com.example.martin.omatomaatti.BankIdClass;

/**
 * Created by Martin on 9.2.2016.
 */
public class Account {
    private String name;
    private String number;


    public Account(String n, String num){
        if(n != " " && num != " ") {
            name = n;
            number =num;
        }
        //TODO: 9.2.2016  throwing exception?
    }

    public String getName(){
        return name;
    }
    public String getNum(){
        return number;
    }

    public void setName(String name){
        this.name = name;
    }
    public void setNum(String num){
        this.number = num;
    }

    public String toString(){
        return getName() + " " + getNum();
    }

}
