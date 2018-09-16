package com.musipo.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.musipo.dao.Dao;
import com.musipo.dao.IDao;
import com.musipo.dao.tables.PlayingStatusTbl;
import com.musipo.dao.tables.StatusTbl;
import com.musipo.model.PlayingStatus;
import com.musipo.model.Status;
import com.musipo.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by G510 on 18-06-2017.
 */

public class PlayStatusDAO extends Dao<PlayingStatus> implements IDao<PlayingStatus> {

    public PlayStatusDAO() {
        super(PlayingStatusTbl.TABLE_NAME);
    }

    @Override
    public long save(PlayingStatus playingStatus) {

        openDB();
        long rowId = 0;
        ContentValues contentValues = getContentValues(playingStatus);
        Log.d(TAG, "contentValues save" + playingStatus);
        try {
            rowId = sqLiteDb.insertWithOnConflict(PlayingStatusTbl.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            Log.d(TAG, "Status save" + playingStatus);
        } catch (Exception e) {
            Log.e(TAG, "Exception :: status " + e.getMessage());
        } finally {
            closeDB();
        }
        return rowId;
    }

    public long saveList(ArrayList<PlayingStatus> statusArrayList) {
        Log.d(TAG, "statusArrayList::" + statusArrayList.size());
        long count = 0;
        for (PlayingStatus status : statusArrayList) {
            long rowid = save(status);
            if (rowid > 0)
                count++;
        }
        Log.d(TAG, "Total User Playing Status in database" + count);
        return count;
    }

    @Override
    public int update(PlayingStatus t) {
        return 0;
    }

    @Override
    public int delete(PlayingStatus t) {
        return 0;
    }

    @Override
    public PlayingStatus find(String id) {
        return null;
    }

    @Override
    public PlayingStatus find(String[] arg) {
        return null;
    }

    public PlayingStatus findByUserID(String id) {
        PlayingStatus status = null;
        openDB();

        String query = PlayingStatusTbl.STATEMENT_SELECT + " WHERE "+ StatusTbl.USERID + " = '" + id + "'";

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
    public List<PlayingStatus> findAll() {
        List<PlayingStatus> list = null;

        try {

            openDB();
            String query = PlayingStatusTbl.STATEMENT_SELECT;

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

    public ContentValues getContentValues(PlayingStatus status) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(PlayingStatusTbl.PLAY_ID, status.getId());
        contentValues.put(PlayingStatusTbl.STATUS, status.getStatus());
        contentValues.put(PlayingStatusTbl.CREATE_DATE, status.getCreatedDate());
        contentValues.put(PlayingStatusTbl.DELETED_FLAG, status.getDeletedFlag());
        contentValues.put(PlayingStatusTbl.UPDATED_DATE, status.getUpdatedDate());
        contentValues.put(PlayingStatusTbl.USERID, status.getUserId());

        contentValues.put(PlayingStatusTbl.PLAYING_INFO, status.getPlayingInfo());
        contentValues.put(PlayingStatusTbl.PLAYING_SRC, status.getPlayingSrc());

        return contentValues;
    }

    @Override
    public PlayingStatus cursorToModel(Cursor cursor) {

        PlayingStatus playingStatus = new PlayingStatus();

        playingStatus.setId(cursor.getString(cursor.getColumnIndex(PlayingStatusTbl.PLAY_ID)));
        playingStatus.setUserId(cursor.getString(cursor.getColumnIndex(PlayingStatusTbl.USERID)));
        playingStatus.setStatus(cursor.getString(cursor.getColumnIndex(PlayingStatusTbl.STATUS)));
        playingStatus.setCreatedDate(cursor.getString(cursor.getColumnIndex(PlayingStatusTbl.CREATE_DATE)));
        playingStatus.setUpdatedDate(cursor.getString(cursor.getColumnIndex(PlayingStatusTbl.UPDATED_DATE)));
        playingStatus.setDeletedFlag(cursor.getInt(cursor.getColumnIndex(PlayingStatusTbl.DELETED_FLAG)));

        playingStatus.setPlayingInfo(cursor.getString(cursor.getColumnIndex(PlayingStatusTbl.PLAYING_INFO)));
        playingStatus.setPlayingSrc(cursor.getString(cursor.getColumnIndex(PlayingStatusTbl.PLAYING_SRC)));
        return playingStatus;
    }

    @Override
    public List<PlayingStatus> cursorToModelList(Cursor cursor) {
        List<PlayingStatus> list = null;

        UserDao userDao =  new UserDao();

        while (cursor.moveToNext()) {

            if (list == null) {
                list = new ArrayList<>();
            }

            PlayingStatus status = cursorToModel(cursor);

            Log.d(TAG, "PlayingStatus  : " + status);

            User user = userDao.find(status.getUserId());
            Log.d(TAG, "user  : " + user);
            status.setUser(user);
            list.add(status);
        }
        return list;
    }
}
