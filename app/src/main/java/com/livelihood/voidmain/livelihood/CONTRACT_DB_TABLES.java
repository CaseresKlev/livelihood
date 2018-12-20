package com.livelihood.voidmain.livelihood;

public final class CONTRACT_DB_TABLES {

    private CONTRACT_DB_TABLES(){}

    public static class Table_Person{

        public static final String TABLE_NAME = "person";
        public static final String PERSON_ID = "p_id";
        public static final String PERSON_NAME = "name";
        public static final String PERSON_ADDRESS = "address";
        public static final String GROUP_ID = "group_id";
    }

    public static class Table_GROUP{

        public static final String TABLE_NAME = "tbl_group";
        public static final String GROUP_ID = "g_id";
        public static final String GROUP_NAME = "g_name";
        public static final String GROUP_ADDRESS = "g_address";
    }

}
