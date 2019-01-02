package com.livelihood.voidmain.livelihood;

public class Person {

    private String name;
    private String address;
    private int group_id;
    private int person_id, sync_status;

    public Person(int person_id, String name, String address, int group_id, int sync_status) {
        this.person_id = person_id;
        this.name = name;
        this.address = address;
        this.group_id = group_id;
        this.sync_status = sync_status;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getGroupID(){
        return group_id;
    }

    public int getPersonID(){
        return person_id;
    }

    public int getSyncStatus(){return  sync_status;}
}
