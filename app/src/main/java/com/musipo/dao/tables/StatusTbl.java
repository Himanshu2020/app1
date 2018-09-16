package com.musipo.dao.tables;

/**
 * Created by G510 on 18-06-2017.
 */

public class StatusTbl extends CommanFiled{


    public static final String TABLE_NAME = "status_tbl";

    public static final String STATUS_ID = "status_id";
    public static final String STATUS_MSG = "status_msg";
    public static final String SYNC_ID = "SYNC_ID";

    public static final String STATEMENT_CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + STATUS_ID  + " VARCHAR,"
            + CREATE_DATE  + " VARCHAR,"
            + UPDATED_DATE  + " VARCHAR,"
            + USERID + " VARCHAR,"
            + STATUS_MSG + " VARCHAR,"
            + DELETED_FLAG + " INTEGER,"
            + SYNC_ID + " VARCHAR,"
            + "unique(" + SYNC_ID + ")"
            + ")";

    public static final String STATEMENT_SELECT = "select * from "
            + StatusTbl.TABLE_NAME;
}
