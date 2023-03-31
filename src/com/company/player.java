package com.company;

public class player {
    public int puan;
    public String name;

    public player(){
        this.puan=0;
    }
    public player(String name){
        this.puan=0;
        this.name=name;
    }
    public void puanEkle(){
        puan++;
    }

}
