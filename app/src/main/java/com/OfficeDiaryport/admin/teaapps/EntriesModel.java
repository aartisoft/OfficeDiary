package com.OfficeDiaryport.admin.teaapps;

public class EntriesModel {

    String vname,utname,quantity,time,date,uname,prize;

    public EntriesModel(String vname, String utname, String quantity, String time,String date) {
        this.vname = vname;
        this.utname = utname;
        this.quantity = quantity;
        this.time = time;
        this.date=date;
    }

    public EntriesModel(String vname, String utname, String quantity, String time, String date, String uname,String prize) {
        this.vname = vname;
        this.utname = utname;
        this.quantity = quantity;
        this.time = time;
        this.date = date;
        this.uname = uname;
        this.prize=prize;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getUtname() {
        return utname;
    }

    public void setUtname(String utname) {
        this.utname = utname;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }
}
