package com.musipo.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.musipo.dao.Dao;
import com.musipo.dao.IDao;
import com.musipo.dao.tables.StatusTbl;
import com.musipo.dao.tables.UserTable;
import com.musipo.model.Message;
import com.musipo.model.Status;
import com.musipo.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by G510 on 18-06-2017.
 */

public class StatusDAO extends Dao<Status> implements IDao<Status> {

    public StatusDAO() {
        super(StatusTbl.TABLE_NAME);
    }

    @Override
    public long save(Status status) {

        openDB();
        long rowId = 0;
        ContentValues contentValues = getContentValues(status);
        Log.d(TAG, "contentValues save" + status);
        try {
            rowId = sqLiteDb.insertWithOnConflict(StatusTbl.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            Log.d(TAG, "Status save" + status);
        } catch (Exception e) {
            Log.e(TAG, "Exception :: status " + e.getMessage());
        } finally {
            closeDB();
        }
        return rowId;
    }

    public long saveList(ArrayList<Status> statusArrayList) {
        Log.d(TAG, "statusArrayList::" + statusArrayList.size());
        long count = 0;
        for (Status status : statusArrayList) {
            long rowid = save(status);
            if (rowid > 0)
                count++;
        }
        Log.d(TAG, "Total User Status in database" + count);
        return count;
    }

    @Override
    public int update(Status t) {
        return 0;
    }

    @Override
    public int delete(Status t) {
        return 0;
    }

    @Override
    public Status find(String id) {
        return null;
    }

    @Override
    public Status find(String[] arg) {
        return null;
    }

    public Status findByUserID(String id) {
        Status status = null;
        openDB();

        String query = StatusTbl.STATEMENT_SELECT + " WHERE "+ StatusTbl.USERID + " = '" + id + "'";

        Log.d(TAG, "find() Query : " + query);

        try {

            Cursor cursor = sqLiteDb.rawQuery(query, null);
            int count = cursor.getCount();
            if (count > 0) {
                while (cursor.moveToNext()) {
                    status = cursorToModel(cursor);
                    Log.d(TAG, "Record  : " + status);
                }
            }
            cursor.close();
            Log.d(TAG, "Record Count :" + count);
        } catch (Exception e) {
            Log.e(TAG, "Exception :: status " + e.getMessage());
        } finally {
            closeDB();
        }

        return status;
    }

    @Override
    public List<Status> findAll() {
        List<Status> list = null;

        try {

            openDB();
            String query = StatusTbl.STATEMENT_SELECT;

            Log.d(TAG, "find() Query : " + query);

            Cursor cursor = sqLiteDb.rawQuery(query, null);
            int count = cursor.getCount();
            Log.d(TAG, "Record Count :" + count);

            if (count > 0) {
                list = cursorToModelList(cursor);
                Log.d(TAG, "Record  : " + list);
            }
            cursor.close();
        } catch (Exception e) {

        }finally{
            try {
                closeDB();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public ContentValues getContentValues(Status status) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(StatusTbl.STATUS_ID, status.getId());
        contentValues.put(StatusTbl.STATUS_MSG, status.getStatusMsg());
        contentValues.put(StatusTbl.CREATE_DATE, status.getCreatedDate());
        contentValues.put(StatusTbl.DELETED_FLAG, status.getDeletedFlag());
        contentValues.put(StatusTbl.UPDATED_DATE, status.getUpdatedDate());
        contentValues.put(StatusTbl.USERID, status.getUserId());

        return contentValues;
    }

    @Override
    public Status cursorToModel(Cursor cursor) {

        Status status = new Status();

        status.setId(cursor.getString(cursor.getColumnIndex(StatusTbl.STATUS_ID)));
        status.setUserId(cursor.getString(cursor.getColumnIndex(StatusTbl.USERID)));
        status.setStatusMsg(cursor.getString(cursor.getColumnIndex(StatusTbl.STATUS_MSG)));
        status.setCreatedDate(cursor.getString(cursor.getColumnIndex(StatusTbl.CREATE_DATE)));
        status.setUpdatedDate(cursor.getString(cursor.getColumnIndex(StatusTbl.UPDATED_DATE)));
        status.setDeletedFlag(cursor.getInt(cursor.getColumnIndex(StatusTbl.DELETED_FLAG)));

        return status;
    }

    @Override
    public List<Status> cursorToModelList(Cursor cursor) {
        List<Status> list = null;

        while (cursor.moveToNext()) {

            if (list == null) {
                list = new ArrayList<Status>();
            }
            Status status = cursorToModel(cursor);

            list.add(status);
        }
        return list;
    }
}
