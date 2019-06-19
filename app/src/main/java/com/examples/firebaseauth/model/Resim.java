package com.examples.firebaseauth.model;

public class Resim {
    private String name;
    private String url;

    public Resim(){}
    public Resim(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
