package com.musipo.dao.tables;

/**
 * Created by G510 on 09-05-2017.
 */

public class ChatRoomTbl extends CommanFiled{

    public static final String TABLE_NAME = "chat_room_tbl";

    public static final String CHATROOM_ID = "CHATROOM_ID";
    public static final String NAME = "NAME";
    public static final String USER_ID = "USER_ID";
    public static final String MESSAGE_COUNT = "MESSAGE_COUNT";
    public static final String LAST_MESSAGE = "LAST_MESSAGE";
    public static final String ASSOCIATED_USER_ID = "associated_user_id";

    public static final String STATEMENT_CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CHATROOM_ID  + " VARCHAR,"
            + CREATE_DATE  + " VARCHAR,"
            + UPDATED_DATE  + " VARCHAR,"
            + MESSAGE_COUNT  + " INTEGER,"
            + LAST_MESSAGE  + " VARCHAR,"
            + NAME + " VARCHAR,"
            + USER_ID + " VARCHAR,"
            + ASSOCIATED_USER_ID + " VARCHAR,"
            + SYNC_ID + " VARCHAR,"
            + DELETED_FLAG + " INTEGER,"
            + "unique(" + CHATROOM_ID + ")"
            + ")";

    public static final String STATEMENT_SELECT = "select * from "
            + ChatRoomTbl.TABLE_NAME;
}
