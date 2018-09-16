package com.musipo.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.musipo.dao.Dao;
import com.musipo.dao.IDao;
import com.musipo.dao.tables.ChatRoomTbl;
import com.musipo.dao.tables.LastSeenTbl;
import com.musipo.model.ChatRoom;
import com.musipo.model.LastSeen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by G510 on 18-06-2017.
 */

public class LastSeenDAO extends Dao<LastSeen> implements IDao<LastSeen> {

    public long saveList(ArrayList<LastSeen> lastSeenArrayList) {
        Log.d(TAG, "lastSeenArrayList::" + lastSeenArrayList.size());
        long count = 0;
        for (LastSeen lastSeen : lastSeenArrayList) {
            long rowid = save(lastSeen);
            if (rowid > 0)
                count++;
        }
        Log.d(TAG, "Total lastSeen in database" + count);
        return count;
    }

    public LastSeenDAO() {
        super(LastSeenTbl.TABLE_NAME);
    }

    @Override
    public long save(LastSeen lastSeen) {
        openDB();
        long rowId = 0;
        ContentValues contentValues = getContentValues(lastSeen);
        try {
            rowId = sqLiteDb.insertWithOnConflict(LastSeenTbl.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);

        } catch (Exception e) {
            Log.e(TAG, "Exception :: save(chatRoom) " + e.getMessage());
        } finally {
            closeDB();
        }
        return rowId;
    }

    @Override
    public int update(LastSeen t) {
        return 0;
    }

    @Override
    public int delete(LastSeen t) {
        return 0;
    }

    @Override
    public LastSeen find(String id) {
        return null;
    }

    @Override
    public LastSeen find(String[] arg) {
        return null;
    }

    @Override
    public List<LastSeen> findAll() {
        return null;
    }

    @Override
    public ContentValues getContentValues(LastSeen lastSeen) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(LastSeenTbl.LASTSEEN_ID,lastSeen.getId());
        contentValues.put(LastSeenTbl.CREATE_DATE, lastSeen.getCreatedDate());
        contentValues.put(LastSeenTbl.DELETED_FLAG, lastSeen.getDeletedFlag());
        contentValues.put(LastSeenTbl.UPDATED_DATE, lastSeen.getUpdatedDate());
        contentValues.put(LastSeenTbl.USERID, lastSeen.getUserId());

        return contentValues;
    }

    @Override
    public LastSeen cursorToModel(Cursor cursor) {
        LastSeen lastSeen = new LastSeen();

        lastSeen.setId(cursor.getString(cursor.getColumnIndex(LastSeenTbl.LASTSEEN_ID)));
        lastSeen.setUpdatedDate(cursor.getString(cursor.getColumnIndex(LastSeenTbl.UPDATED_DATE)));
        lastSeen.setCreatedDate(cursor.getString(cursor.getColumnIndex(LastSeenTbl.CREATE_DATE)));
        lastSeen.setUserId(cursor.getString(cursor.getColumnIndex(LastSeenTbl.USERID)));
        lastSeen.setDeletedFlag(cursor.getInt(cursor.getColumnIndex(LastSeenTbl.DELETED_FLAG)));

        return lastSeen;
    }

    @Override
    public List<LastSeen> cursorToModelList(Cursor cursor) {
        List<LastSeen> list = null;

        while (cursor.moveToNext()) {

            if (list == null) {
                list = new ArrayList<LastSeen>();
            }
            LastSeen lastSeen = cursorToModel(cursor);
            list.add(lastSeen);
        }
        return list;
    }
}
