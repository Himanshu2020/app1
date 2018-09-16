package com.musipo.dao.tables;

/**
 * Created by G510 on 18-06-2017.
 */

public class FcmTbl extends CommanFiled{

    public static final String TABLE_NAME = "fcm_tbl";

    public static final String FCM_ID = "FCM_ID";
    public static final String SYNC_ID = "SYNC_ID";
    public static final String FCM_REGISTRATION_ID = "FCM_REGISTRATION_ID";

    public static final String STATEMENT_CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CREATE_DATE  + " VARCHAR,"
            + UPDATED_DATE  + " VARCHAR,"
            + FCM_ID + " VARCHAR,"
            + FCM_REGISTRATION_ID + " VARCHAR,"
            + USERID + " VARCHAR,"
            + DELETED_FLAG + " INTEGER,"
            + SYNC_ID + " VARCHAR,"
            + "unique(" + SYNC_ID + ")"
            + ")";

    public static final String STATEMENT_SELECT = "select * from "
            + MessageTbl.TABLE_NAME;
}
