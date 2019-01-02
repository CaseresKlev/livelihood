package com.livelihood.voidmain.livelihood;

public class APPLICATION_SERVER {
    public static String host = "192.168.1.2";
    public static String ADD_PERSON_PACKAGE = "com.klevie.livelihood.addPerson";
    public static final String getGroupURL = "http://" + host + "/livelihood/andriod/services/v1/user/getGroup.php";
    public static final String pingTest = "http://" + host + "/livelihood/andriod/services/v1/user/pingtest.php";
    public  static final String addPerson ="http://" + host + "/livelihood/andriod/services/v1/user/addperson.php?passKey=";
}
