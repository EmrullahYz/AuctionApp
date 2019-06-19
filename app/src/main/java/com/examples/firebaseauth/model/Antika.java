package com.examples.firebaseauth.model;

import android.net.Uri;

import java.sql.Time;
import java.util.Date;

public class Antika {

    private String Antikaid;
    private String url1;
    private String url2;
    private String url3;
    private String baslik;
    private String aciklama;
    private String saat;
    private Double minFiyat;
    private String sahipId;
    private boolean expertiz;// ekspertiz durumunu gostermektedir false henuz ekspertizden gecmemis demektir.
    private String teklifVerenId; // en son teklif veren kisinin kullnaici iD sini verir
    private boolean aktif;     // antikanin ilan aktifligi durumu
    private String experGorusu;
    public Antika(){}

    public Antika(String aciklama, String id, String baslik, boolean expertiz,boolean aktif, Double minFiyat,
                  String saat,String sahipId,String teklifVerenId,String url1,String url2,String url3,String experGorusu) {
        this.Antikaid = id;
        this.url1 = url1;
        this.url2 = url2;
        this.url3 = url3;
        this.baslik = baslik;
        this.aciklama = aciklama;
        this.saat = saat;
        this.minFiyat = minFiyat;
        this.sahipId = sahipId;
        this.aktif = aktif;
        this.expertiz = expertiz;
        this.teklifVerenId = teklifVerenId;
        this.experGorusu = experGorusu;
    }

    public String getExperGorusu() {
        return experGorusu;
    }

    public void setExperGorusu(String experGorusu) {
        this.experGorusu = experGorusu;
    }

    public boolean isAktif() {
        return aktif;
    }

    public void setAktif(boolean aktif) {
        this.aktif = aktif;
    }

    public String getAntikaid() {
        return Antikaid;
    }

    public void setAntikaid(String antikaid) {
        Antikaid = antikaid;
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public String getUrl3() {
        return url3;
    }

    public void setUrl3(String url3) {
        this.url3 = url3;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }


    public String getSaat() {
        return saat;
    }

    public void setSaat(String saat) {
        this.saat = saat;
    }

    public Double getMinFiyat() {
        return minFiyat;
    }

    public void setMinFiyat(Double minFiyat) {
        this.minFiyat = minFiyat;
    }

    public String getSahipId() {
        return sahipId;
    }

    public void setSahipId(String sahipId) {
        this.sahipId = sahipId;
    }

    public boolean isExpertiz() {
        return expertiz;
    }

    public void setExpertiz(boolean expertiz) {
        this.expertiz = expertiz;
    }

    public String getTeklifVerenId() {
        return teklifVerenId;
    }

    public void setTeklifVerenId(String teklifVerenId) {
        this.teklifVerenId = teklifVerenId;
    }
}
