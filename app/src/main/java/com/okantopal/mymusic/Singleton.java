package com.okantopal.mymusic;

public class Singleton {

    private String userName;
    private static Singleton singleton;

    private Singleton(){

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static Singleton getInstance()
    {
        if (singleton == null)
            singleton = new Singleton();

        return singleton;
    }
}
