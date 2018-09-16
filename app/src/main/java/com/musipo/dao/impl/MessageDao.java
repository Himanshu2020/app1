package com.musipo.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.musipo.dao.Dao;
import com.musipo.dao.IDao;
import com.musipo.dao.tables.ChatRoomTbl;
import com.musipo.dao.tables.LastSeenTbl;
import com.musipo.dao.tables.MessageTbl;
import com.musipo.dao.tables.StatusTbl;
import com.musipo.dao.tables.UserTable;
import com.musipo.model.ChatRoom;
import com.musipo.model.LastSeen;
import com.musipo.model.Message;
import com.musipo.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by G510 on 18-06-2017.
 */

public class MessageDao extends Dao<Message> implements IDao<Message> {
    public MessageDao() {
        super(MessageTbl.TABLE_NAME);
    }

    @Override
    public long save(Message message) {
        openDB();
        long rowId = 0;
        ContentValues contentValues = getContentValues(message);
        try {
            rowId = sqLiteDb.insertWithOnConflict(MessageTbl.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            Log.d(TAG, "save(message) " + message);
        } catch (Exception e) {
            Log.e(TAG, "Exception :: save(message) " + e.getMessage());
        } finally {
            closeDB();
        }
        return rowId;
    }

    public long saveList(ArrayList<Message> messageArrayList) {
        Log.d(TAG, "messageArrayList::" + messageArrayList.size());
        long count = 0;
        for (Message message : messageArrayList) {
            long rowid = save(message);
            if (rowid > 0)
                count++;
        }
        Log.d(TAG, "Total message in database" + count);
        return count;
    }

    @Override
    public int update(Message message) {
        ContentValues contentValues = new ContentValues();
        openDB();
        int updatedRowCount = 0;

        try {
            contentValues.put(MessageTbl.CREATE_DATE, message.getCreatedAt());
            contentValues.put(MessageTbl.USERID, message.getUser().getId());
            contentValues.put(MessageTbl.MESSAGE_ID, message.getId());
            updatedRowCount = sqLiteDb.update(MessageTbl.TABLE_NAME, contentValues, MessageTbl.SYNC_ID + "=?", new String[]{String.valueOf(message.getSyncId())});

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (updatedRowCount > 0)
            Log.d(TAG, "update..(message) " + message);

        closeDB();
        return updatedRowCount;
    }

    @Override
    public int delete(Message t) {
        return 0;
    }

    @Override
    public Message find(String id) {
        return null;
    }

    @Override
    public Message find(String[] arg) {
        return null;
    }

    @Override
    public List<Message> findAll() {
        List<Message> messageList = null;

        try {

            openDB();
            String query = MessageTbl.STATEMENT_SELECT;

            Log.d(TAG, "find() Query : " + query);

            Cursor cursor = sqLiteDb.rawQuery(query, null);

            int count = cursor.getCount();
            Log.d(TAG, "Record Count :" + count);

            if (count > 0) {
                messageList = cursorToModelList(cursor);
                Log.d(TAG, "Record  : " + messageList);
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
        return messageList;
    }


    public List<Message> findAllMsgByChatRoomId(String chatRoomId) {
        List<Message> messageList = null;

        try {

            openDB();
            //String query = MessageTbl.STATEMENT_SELECT;

            String query = MessageTbl.STATEMENT_SELECT + " WHERE " + MessageTbl.CHATROOM_ID + " = '" + chatRoomId + "'";

            Log.d(TAG, "find() Query : " + query);

            Cursor cursor = sqLiteDb.rawQuery(query, null);

            int count = cursor.getCount();
            Log.d(TAG, "Record Count :" + count);

            if (count > 0) {
                messageList = cursorToModelList(cursor);
                Log.d(TAG, "Record  : " + messageList);
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
        return messageList;
    }



    @Override
    public ContentValues getContentValues(Message message) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(MessageTbl.MESSAGE_ID, message.getId());
        contentValues.put(MessageTbl.MESSAGE, message.getMessage());
        contentValues.put(MessageTbl.CREATE_DATE, message.getCreatedDate());
        contentValues.put(MessageTbl.DELETED_FLAG, message.getDeletedFlag());
        contentValues.put(MessageTbl.UPDATED_DATE, message.getUpdatedDate());
        contentValues.put(MessageTbl.CHATROOM_ID, message.getChatRoomId());
        contentValues.put(MessageTbl.SYNC_ID, message.getSyncId());
        contentValues.put(MessageTbl.MESSAGE_STATUS, message.getMsgStatus());
        contentValues.put(MessageTbl.DELETED_FLAG, message.getDeletedFlag());
        contentValues.put(MessageTbl.USERID, message.getUser().getId());

        return contentValues;
    }

    @Override
    public Message cursorToModel(Cursor cursor) {
        Message message = new Message();
        User user = new User();
        message.setId(cursor.getString(cursor.getColumnIndex(MessageTbl.MESSAGE_ID)));
        message.setUpdatedDate(cursor.getString(cursor.getColumnIndex(MessageTbl.UPDATED_DATE)));
        message.setCreatedDate(cursor.getString(cursor.getColumnIndex(MessageTbl.CREATE_DATE)));
        message.setDeletedFlag(cursor.getInt(cursor.getColumnIndex(MessageTbl.DELETED_FLAG)));
        message.setChatRoomId(cursor.getString(cursor.getColumnIndex(MessageTbl.CHATROOM_ID)));
        message.setMessage(cursor.getString(cursor.getColumnIndex(MessageTbl.MESSAGE)));
        user.setId(cursor.getString(cursor.getColumnIndex(MessageTbl.USERID)));
        message.setUser(user);

        return message;
    }

    @Override
    public List<Message> cursorToModelList(Cursor cursor) {
        List<Message> list = null;

        while (cursor.moveToNext()) {

            if (list == null) {
                list = new ArrayList<Message>();
            }
            Message message = cursorToModel(cursor);
            list.add(message);
        }
        return list;
    }
}
