package com.livelihood.voidmain.livelihood;

public final class CONTRACT_DB_TABLES {

    private CONTRACT_DB_TABLES(){}

    public static class Table_Person{

        public static final String PERSON_TABLE_NAME = "person";

        public static final String PERSON_ID = "p_id";
        public static final String PERSON_NAME = "name";
        public static final String PERSON_BIRTHDATE = "birthdate";
        public static final String PERSON_CONTACT = "contact";
        public static final String PERSON_ATTAINMENT = "attainment";
        public static final String PERSON_SPOUSE = "spouse";
        public static final String PERSON_NUMBER_FAMILY = "family_member";
        public static final String PERSON_NUMBER_VOTERS = "family_voters";
        public static final String PERSON_SHIRT_SIZE = "shirt_size";
        public static final String PERSON_POSITION = "position";
        public static final String PERSON_GROUP_ID = "group_id";
        public static final String PERSON_SYNC_STATUS = "sync_status";
        public static final String PERSON_SYNC_ACTION = "sync_action";
        public static final String PERSON_ADDED_DATE = "added_date";
        public static final String PERSON_ADDED_BY = "added_by";
        public static final String PERSON_WEB_ID = "server_id";
        public static final String PERSON_REMARKS = "remarks";
    }

    public static class Table_GROUP{

        public static final String TABLE_NAME = "tbl_group";
        public static final String GROUP_ID = "g_id";
        public static final String GROUP_NAME = "g_name";
        public static final String GROUP_PUROK = "g_purok";
        public static final String GROUP_BARANGAY = "g_barangay";
        public static final String GROUP_CITY = "g_city";
    }

    public static  class  SYNC_STATUS{
        public static final int SYNC_FAILED = 0;
        public static final int SYNC_SUCCESS = 1;

    }

    public static  class SYNC_ACTION{
        public static final String ACTION_ADD = "add";
        public static final String ACTION_DELETE = "delete";
        public static final String ACTION_EDIT = "edit";
    }


    public static class CONNECT_STATUS{
        public static int CONNECT_ERROR = 1;
        public static int CONNECT_SUCCESS = 0;
    }

    public static class REQUEST_CODE{
        public static int REQUEST_CODE_DELETE = 100;
        public static int REQUEST_CODE_EDIT = 101;
        public static int REQUEST_SUCCESS = 1;
        public static int REQUEST_ERROR = 0;
        public static int REQUEST_CANCEL = -1;
    }

}
