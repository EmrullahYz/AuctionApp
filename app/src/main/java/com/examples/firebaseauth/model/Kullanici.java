package com.examples.firebaseauth.model;

import java.util.List;

public class Kullanici {
    private String k_kimlikNo;
    private String k_adres;
    private String k_mail;
    private String k_sifre;
    private String k_tel;
    private List<String> favoriListeId;
    public Kullanici(){

    }

    public Kullanici(String k_kimlikNo, String k_adres, String k_mail,String k_tel, String k_sifre,List<String> favoriListeId) {
        this.k_kimlikNo = k_kimlikNo;
        this.k_adres = k_adres;
        this.k_mail = k_mail;
        this.k_sifre = k_sifre;
        this.k_tel = k_tel;
        this.favoriListeId = favoriListeId;
    }

    public String getK_tel() {
        return k_tel;
    }

    public void setK_tel(String k_tel) {
        this.k_tel = k_tel;
    }

    public List<String> getFavoriListe() {
        return favoriListeId;
    }

    public void setFavoriListe(List<String> favoriListe) {
        this.favoriListeId = favoriListe;
    }

    public String getK_kimlikNo() {
        return k_kimlikNo;
    }

    public void setK_kimlikNo(String k_kimlikNo) {
        this.k_kimlikNo = k_kimlikNo;
    }

    public String getK_adres() {
        return k_adres;
    }

    public void setK_adres(String k_adres) {
        this.k_adres = k_adres;
    }

    public String getK_mail() {
        return k_mail;
    }

    public void setK_mail(String k_mail) {
        this.k_mail = k_mail;
    }

    public String getK_sifre() {
        return k_sifre;
    }

    public void setK_sifre(String k_sifre) {
        this.k_sifre = k_sifre;
    }
}
