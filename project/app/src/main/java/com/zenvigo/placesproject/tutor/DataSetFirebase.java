package com.project.placesproject.tutor;

import android.net.Uri;

public class DataSetFirebase {


    private String Mode;
    private Long Price;
    private String UID;
    private String DemoVid;

    public String getUri() {
        return DemoVid;
    }

    public void setUri(String uri) {
        this.DemoVid = uri;
    }

    public String getMode() {
        return Mode;
    }

    public void setMode(String Mode) {
        this.Mode = Mode;
    }

    public Long getPrice() {
        return Price;
    }

    public void setPrice(Long Price) {
        this.Price = Price;
    }
    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public DataSetFirebase() {
    }

    public DataSetFirebase(String Mode,Long Price, String UID) {
        this.UID = UID;
        this.Mode = Mode;
        this.Price = Price;
    }
}