package com.livelihood.voidmain.livelihood;

public class Group  {

    private int id;
    private int membersCount;
    private String group_name;
    private String brgy;
    private String city;

    public Group(int id, String group_name, String brgy, String city, int membersCount) {
        this.id = id;
        this.group_name = group_name;
        this.brgy = brgy;
        this.city = city;
        this.membersCount = membersCount;
    }

    public int getId() {
        return id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public String getBrgy() {
        return brgy;
    }

    public String getCity() {
        return city;
    }

    public int getMembersCount(){
        return membersCount;
    }
}
