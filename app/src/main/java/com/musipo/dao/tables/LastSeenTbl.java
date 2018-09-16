package com.musipo.dao.tables;

/**
 * Created by G510 on 18-06-2017.
 */

public class LastSeenTbl extends CommanFiled{


    public static final String TABLE_NAME = "lastseen_tbl";

    public static final String LASTSEEN_ID = "lastseen_id";
    public static final String SEEN_AT = "SEEN_AT";
    public static final String SYNC_ID = "SYNC_ID";

    public static final String STATEMENT_CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + LASTSEEN_ID  + " VARCHAR,"
            + CREATE_DATE  + " VARCHAR,"
            + UPDATED_DATE  + " VARCHAR,"
            + USERID + " VARCHAR,"
            + SEEN_AT + " VARCHAR,"
            + DELETED_FLAG + " INTEGER,"
            + SYNC_ID + " VARCHAR,"
            + "unique(" + SYNC_ID + ")"
            + ")";

    public static final String STATEMENT_SELECT = "select * from "
            + MessageTbl.TABLE_NAME;
}
