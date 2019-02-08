package com.OfficeDiaryport.admin.teaapps;

public class UtilityModel {

    String vname,utname,price,utility_id;

    public UtilityModel(String vname, String utname, String price,String utility_id) {
        this.vname = vname;
        this.utname = utname;
        this.price = price;
        this.utility_id=utility_id;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUtility_id() {
        return utility_id;
    }

    public void setUtility_id(String utility_id) {
        this.utility_id = utility_id;
    }
}
