package com.examples.firebaseauth.model;

public class Ekspertiz {
    private String adres;
    private String mail;
    private String sifre;
    private String tel;
    private String firmaAdi;
    public Ekspertiz(){}
    public Ekspertiz(String adres, String mail, String sifre, String tel, String firmaAdi) {
        this.adres = adres;
        this.mail = mail;
        this.sifre = sifre;
        this.tel = tel;
        this.firmaAdi = firmaAdi;
    }

    public String getAdres() {
        return adres;
    }

    public String getMail() {
        return mail;
    }

    public String getSifre() {
        return sifre;
    }

    public String getTel() {
        return tel;
    }

    public String getFirmaAdi() {
        return firmaAdi;
    }
}
