package com.musipo.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.musipo.dao.Dao;
import com.musipo.dao.IDao;
import com.musipo.dao.tables.ChatRoomTbl;
import com.musipo.dao.tables.StatusTbl;
import com.musipo.dao.tables.UserTable;
import com.musipo.model.ChatRoom;
import com.musipo.model.Status;
import com.musipo.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Himanshu on 18-06-2017.
 */

public class ChatRoomDao extends Dao<ChatRoom> implements IDao<ChatRoom> {


    public ChatRoomDao() {
        super(ChatRoomTbl.TABLE_NAME);
    }

    public long saveList(ArrayList<ChatRoom> chatRoomArrayList) {
        Log.d(TAG, "statusArrayList::" + chatRoomArrayList.size());
        long count = 0;
        for (ChatRoom chatRoom : chatRoomArrayList) {
            long rowid = save(chatRoom);
            if (rowid > 0)
                count++;
        }
        Log.d(TAG, "Total ChatRoom in database" + count);
        return count;
    }


    @Override
    public long save(ChatRoom chatRoom) {
        openDB();
        long rowId = 0;
        ContentValues contentValues = getContentValues(chatRoom);
        try {
            rowId = sqLiteDb.insertWithOnConflict(ChatRoomTbl.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);

        } catch (Exception e) {
            Log.e(TAG, "Exception :: save(chatRoom) " + e.getMessage());
        } finally {
            closeDB();
        }
        Log.e(TAG, "Exception :: save(chatRoom) " + chatRoom);

        return rowId;
    }

    @Override
    public int update(ChatRoom t) {
        return 0;
    }

    @Override
    public int delete(ChatRoom t) {
        return 0;
    }

    @Override
    public ChatRoom find(String id) {
        ChatRoom chatRoom = null;

        openDB();
        String query = ChatRoomTbl.STATEMENT_SELECT + " WHERE " + ChatRoomTbl.CHATROOM_ID + " = '" + id + "'";

        try {

            Cursor cursor = sqLiteDb.rawQuery(query, null);
            int count = cursor.getCount();
            if (count > 0) {
                while (cursor.moveToNext()) {
                    chatRoom = cursorToModel(cursor);
                }
            }
            cursor.close();
            Log.d(TAG, "Record Count :" + count);
        } catch (Exception e) {
            Log.e(TAG, "Exception :: status " + e.getMessage());
        } finally {
            closeDB();
        }
        return chatRoom;
    }

    @Override
    public ChatRoom find(String[] arg) {
        return null;
    }

    @Override
    public List<ChatRoom> findAll() {
        List<ChatRoom> ChatRoomList = null;

        try {

            openDB();
            String query = ChatRoomTbl.STATEMENT_SELECT;

            Log.d(TAG, "find() Query : " + query);

            Cursor cursor = sqLiteDb.rawQuery(query, null);

            int count = cursor.getCount();
            Log.d(TAG, "Record Count :" + count);

            if (count > 0) {
                ChatRoomList = cursorToModelList(cursor);
                Log.d(TAG, "Record  : " + ChatRoomList);
            }
            cursor.close();
        } catch (Exception e) {

        } finally {
            try {
                closeDB();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return ChatRoomList;
    }

    final public int updateMesssagAndCount(String chatroomId, String message) {

        ContentValues contentValues = new ContentValues();
        openDB();
        int updatedRowCount = 0;

        try {
            int unreadMsgCount = 0;
            String query = ChatRoomTbl.STATEMENT_SELECT + " WHERE " + ChatRoomTbl.CHATROOM_ID + " = '" + chatroomId + "'";
            Cursor cursor = sqLiteDb.rawQuery(query, null);
            int count = cursor.getCount();

            if (count > 0) {
                while (cursor.moveToNext()) {
                    unreadMsgCount = cursor.getInt(cursor.getColumnIndex(ChatRoomTbl.MESSAGE_COUNT));
                }
            }
            cursor.close();
            contentValues.put(ChatRoomTbl.LAST_MESSAGE, message);
            contentValues.put(ChatRoomTbl.MESSAGE_COUNT, unreadMsgCount + 1);

            updatedRowCount = sqLiteDb.update(ChatRoomTbl.TABLE_NAME, contentValues, ChatRoomTbl.CHATROOM_ID + "=?", new String[]{String.valueOf(chatroomId)});
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeDB();
        return updatedRowCount;

    }

    public int cleaMsgReadCount(String chatroomId) {
        ContentValues contentValues = new ContentValues();
        openDB();
        int updatedRowCount = 0;

        try {
            contentValues.put(ChatRoomTbl.MESSAGE_COUNT, 0);
            updatedRowCount = sqLiteDb.update(ChatRoomTbl.TABLE_NAME, contentValues, ChatRoomTbl.CHATROOM_ID + "=?", new String[]{String.valueOf(chatroomId)});
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDB();

        return updatedRowCount;
    }


    @Override
    public ContentValues getContentValues(ChatRoom chatRoom) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(ChatRoomTbl.CHATROOM_ID, chatRoom.getId());
        contentValues.put(ChatRoomTbl.NAME, chatRoom.getName());
        contentValues.put(ChatRoomTbl.CREATE_DATE, chatRoom.getCreatedDate());
        contentValues.put(ChatRoomTbl.DELETED_FLAG, chatRoom.getDeletedFlag());
        contentValues.put(ChatRoomTbl.UPDATED_DATE, chatRoom.getUpdatedDate());
        contentValues.put(ChatRoomTbl.USERID, chatRoom.getUserId());
        contentValues.put(ChatRoomTbl.ASSOCIATED_USER_ID, chatRoom.getAssociatedUserId());

        return contentValues;
    }

    @Override
    public ChatRoom cursorToModel(Cursor cursor) {

        ChatRoom chatRoom = new ChatRoom();

        chatRoom.setId(cursor.getString(cursor.getColumnIndex(ChatRoomTbl.CHATROOM_ID)));
        chatRoom.setName(cursor.getString(cursor.getColumnIndex(ChatRoomTbl.NAME)));
        chatRoom.setUpdatedDate(cursor.getString(cursor.getColumnIndex(ChatRoomTbl.UPDATED_DATE)));
        chatRoom.setCreatedDate(cursor.getString(cursor.getColumnIndex(ChatRoomTbl.CREATE_DATE)));
        chatRoom.setUserId(cursor.getString(cursor.getColumnIndex(ChatRoomTbl.USER_ID)));
        chatRoom.setDeletedFlag(cursor.getInt(cursor.getColumnIndex(ChatRoomTbl.DELETED_FLAG)));
        chatRoom.setUnreadCount(cursor.getInt(cursor.getColumnIndex(ChatRoomTbl.MESSAGE_COUNT)));
        chatRoom.setLastMessage(cursor.getString(cursor.getColumnIndex(ChatRoomTbl.LAST_MESSAGE)));
        chatRoom.setAssociatedUserId(cursor.getString(cursor.getColumnIndex(ChatRoomTbl.ASSOCIATED_USER_ID)));

        Log.d(TAG, "chatRoom() chatRoom : " + chatRoom);

        return chatRoom;
    }

    @Override
    public List<ChatRoom> cursorToModelList(Cursor cursor) {
        List<ChatRoom> list = new ArrayList<>();

        while (cursor.moveToNext()) {

            if (list == null) {
                list = new ArrayList<ChatRoom>();
            }
            ChatRoom chatRoom = cursorToModel(cursor);
            list.add(chatRoom);
        }
        Log.d(TAG, "list() list : " + list);
        return list;
    }

    public ChatRoom getChatRoomByUser(User user) {
        ChatRoom chatRoom = null;

        openDB();
        String query = ChatRoomTbl.STATEMENT_SELECT + " WHERE " + ChatRoomTbl.USERID + " = '" + user.getId() + "'";

        try {

            Cursor cursor = sqLiteDb.rawQuery(query, null);
            int count = cursor.getCount();
            if (count > 0) {
                while (cursor.moveToNext()) {
                    chatRoom = cursorToModel(cursor);
                    Log.d(TAG, "Record  : " + user);
                }
            }
            cursor.close();
            Log.d(TAG, "Record Count :" + count);
        } catch (Exception e) {
            Log.e(TAG, "Exception :: status " + e.getMessage());
        } finally {
            closeDB();
        }
        return chatRoom;
    }
}
