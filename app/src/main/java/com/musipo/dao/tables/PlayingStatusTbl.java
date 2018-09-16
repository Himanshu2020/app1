package com.musipo.dao.tables;

/**
 * Created by G510 on 18-06-2017.
 */

public class PlayingStatusTbl extends CommanFiled{


    public static final String TABLE_NAME = "paying_status_tbl";

    public static final String PLAY_ID = "PLAY_ID";
    public static final String STATUS = "STATUS";
    public static final String PLAYING_SRC = "PLAYING_SRC";
    public static final String PLAYING_INFO = "PLAYING_INFO";

    public static final String STATEMENT_CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + PLAY_ID  + " VARCHAR,"
            + CREATE_DATE  + " VARCHAR,"
            + UPDATED_DATE  + " VARCHAR,"
            + USERID + " VARCHAR,"
            + PLAYING_SRC + " VARCHAR,"
            + PLAYING_INFO + " VARCHAR,"
            + DELETED_FLAG + " INTEGER,"
            + STATUS + " VARCHAR,"
            + SYNC_ID + " VARCHAR,"
            + "unique(" + PLAY_ID + ")"
            + ")";

    public static final String STATEMENT_SELECT = "select * from "
            + PlayingStatusTbl.TABLE_NAME;
}
