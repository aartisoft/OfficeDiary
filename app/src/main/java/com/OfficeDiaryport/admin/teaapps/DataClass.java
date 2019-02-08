package com.OfficeDiaryport.admin.teaapps;

import java.util.ArrayList;

public class DataClass {

    public static String user_id,admin_id,user_name;

    public static ArrayList<EntriesModel> datafio=new ArrayList<EntriesModel>();

    /*public static ArrayList<VendorModel> datavendor=new ArrayList<VendorModel>(){{
        add(new VendorModel("John","9876543210"));
        add(new VendorModel("Smith","9685741023"));
        add(new VendorModel("Roy","9865741203"));
        add(new VendorModel("burns","9784563210"));
    }};*/

    /*public static ArrayList<UtilityModel> datautility=new ArrayList<UtilityModel>(){{
        add(new UtilityModel("John","Tea/Coffee","10"));
        add(new UtilityModel("Smith","Stationary Items","30"));
        add(new UtilityModel("Roy","Milk","12"));
        add(new UtilityModel("burns","Tea Bags","6"));
    }};*/


    public static ArrayList<EntriesModel> dataentry=new ArrayList<EntriesModel>(){{
       add(new EntriesModel("John","Tea","5","10:35 AM","10/09/2018"));
        add(new EntriesModel("Smith","Tea","4","04:15 PM","10/09/2018"));
        add(new EntriesModel("John","Tea","5","10:35 AM","11/09/2018"));
        add(new EntriesModel("Smith","Tea","5","04:15 PM","11/09/2018"));
        add(new EntriesModel("John","Tea","6","10:35 AM","12/09/2018"));
        add(new EntriesModel("Smith","Tea","5","04:15 PM","12/09/2018"));
        add(new EntriesModel("John","Tea","8","10:35 AM","14/09/2018"));
        add(new EntriesModel("Smith","Tea","5","04:15 PM","14/09/2018"));
        add(new EntriesModel("John","Tea","6","10:35 AM","15/09/2018"));
        add(new EntriesModel("Smith","Tea","5","04:18 PM","15/09/2018"));
    }};
}
