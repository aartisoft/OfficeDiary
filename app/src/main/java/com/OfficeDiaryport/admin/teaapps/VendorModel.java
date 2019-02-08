package com.OfficeDiaryport.admin.teaapps;

public class VendorModel {

    String name,phone,vendor_id;

    public VendorModel(String name, String phone,String vendor_id) {
        this.name = name;
        this.phone = phone;
        this.vendor_id=vendor_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }
}
