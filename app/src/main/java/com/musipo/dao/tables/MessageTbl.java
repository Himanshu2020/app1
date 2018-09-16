package com.musipo.dao.tables;

import com.musipo.model.User;

/**
 * Created by G510 on 09-05-2017.
 */

public class MessageTbl extends CommanFiled{

    public static final String TABLE_NAME = "message_tbl";

    public static final String SERVER_ID = "SERVER_ID";
    public static final String MESSAGE_ID = "MESSAGE_ID";
    public static final String MESSAGE = "MESSAGE";
    public static final String USERID = "USER_ID";
    public static final String SYNC_ID = "SYNC_ID";
    public static final String CHATROOM_ID = "CHATROOM_ID";
    public static final String MESSAGE_STATUS = "MESSAGE_STATUS";

    public static final String STATEMENT_CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + MESSAGE  + " VARCHAR,"
            + CREATE_DATE  + " VARCHAR,"
            + UPDATED_DATE  + " VARCHAR,"
            + MESSAGE_ID + " VARCHAR,"
            + USERID + " VARCHAR,"
            + CHATROOM_ID + " VARCHAR,"
            + DELETED_FLAG + " INTEGER,"
            + MESSAGE_STATUS + " VARCHAR,"
            + SYNC_ID + " VARCHAR,"
            + "unique(" + SYNC_ID + ")"
            + ")";

    public static final String STATEMENT_SELECT = "select * from "
            + MessageTbl.TABLE_NAME;

}
