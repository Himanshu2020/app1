package com.musipo.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.musipo.dao.Dao;
import com.musipo.dao.IDao;
import com.musipo.dao.tables.PlayingStatusTbl;
import com.musipo.dao.tables.StatusTbl;
import com.musipo.dao.tables.UserTable;
import com.musipo.helper.MyApplication;
import com.musipo.model.Message;
import com.musipo.model.Status;
import com.musipo.model.User;
import com.musipo.service.ServerSyncServices;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by G510 on 18-06-2017.
 */

public class UserDao extends Dao<User> implements IDao<User> {

    private static final String TAG = UserDao.class.getSimpleName();

    public UserDao() {
        super(UserTable.TABLE_NAME);
    }

    public long saveList(ArrayList<User> userList) {
        Log.d(TAG, "userList::" + userList.size());
        long count = 0;
        for (User user : userList) {

          /*  ServerSyncServices serverSyncServices =  new ServerSyncServices(null);
            serverSyncServices.getStatus(user);
            serverSyncServices.getPlayingStatus(user);
*/
            long rowid = save(user);
            if (rowid > 0)
                count++;
        }
        Log.d(TAG, "Total User save in database" + count);
        return count;
    }

    @Override
    public long save(User user) {
        openDB();
        long rowId = 0;
        ContentValues contentValues = getContentValues(user);
        try {
            rowId = sqLiteDb.insertWithOnConflict(UserTable.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);

        } catch (Exception e) {
            Log.e(TAG, "Exception :: save(user) " + e.getMessage());
        } finally {
            closeDB();
        }
        return rowId;
    }

    @Override
    public int update(User t) {
        return 0;
    }

    @Override
    public int delete(User t) {
        return 0;
    }

    @Override
    public User find(String id) {
        openDB();

        User user = null;
        String query = UserTable.STATEMENT_SELECT + " WHERE " + UserTable.USERID + " = '" + id + "'";

        Log.d(TAG, "find() Query : " + query);

        try {

            Cursor cursor = sqLiteDb.rawQuery(query, null);
            int count = cursor.getCount();
            if (count > 0) {
                while (cursor.moveToNext()) {
                    user = cursorToModel(cursor);
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

        return user;
    }

    @Override
    public User find(String[] arg) {
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> userList = null;

        try {
            User user = MyApplication.getInstance().getPrefManager().getUser();
            openDB();
            //String query = UserTable.STATEMENT_SELECT;
            String query = UserTable.STATEMENT_SELECT + " WHERE " + UserTable.USERID + " != '" + user.getId() + "'";
            Log.d(TAG, "find() Query : " + query);

            Cursor cursor = sqLiteDb.rawQuery(query, null);
            int count = cursor.getCount();
            Log.d(TAG, "Record Count :" + count);

            if (count > 0) {
                userList = cursorToModelList(cursor);
                Log.d(TAG, "Record  : " + userList);
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
        return userList;
    }

    @Override
    public ContentValues getContentValues(User user) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(UserTable.USER_NAME, user.getName());
        contentValues.put(UserTable.USERID, user.getId());
        contentValues.put(UserTable.MOBILE, user.getMobile());
        contentValues.put(UserTable.CREATE_DATE, user.getCreatedDate());
        contentValues.put(UserTable.UPDATED_DATE, user.getUpdatedDate());
        contentValues.put(UserTable.DELETED_FLAG, user.getDeletedFlag());
        contentValues.put(UserTable.FCM_REGISTRATION_ID, user.getFcmId());
        contentValues.put(UserTable.USER_TYPE, user.getUserType());

        return contentValues;
    }

    @Override
    public User cursorToModel(Cursor cursor) {

        User user = new User();

        user.setId(cursor.getString(cursor.getColumnIndex(UserTable.USERID)));
        user.setName(cursor.getString(cursor.getColumnIndex(UserTable.USER_NAME)));
        user.setMobile(cursor.getString(cursor.getColumnIndex(UserTable.MOBILE)));
        user.setCreatedDate(cursor.getString(cursor.getColumnIndex(UserTable.CREATE_DATE)));
        user.setUpdatedDate(cursor.getString(cursor.getColumnIndex(UserTable.UPDATED_DATE)));
        user.setFcmId(cursor.getString(cursor.getColumnIndex(UserTable.FCM_REGISTRATION_ID)));
        user.setUserType(cursor.getString(cursor.getColumnIndex(UserTable.USER_TYPE)));

        StatusDAO statusDAO = new StatusDAO();
        Status status = statusDAO.findByUserID(user.getId());

        if(status!=null)
        user.setStatusMsg(status.getStatusMsg());

        return user;
    }

    @Override
    public List<User> cursorToModelList(Cursor cursor) {

        List<User> userList = null;

        while (cursor.  moveToNext()) {

            if (userList == null) {
                userList = new ArrayList<User>();
            }
            User user = cursorToModel(cursor);

         /*   StatusDAO statusDAO = new StatusDAO();
            Status status = new Status();
            status = statusDAO.findByUserID(user.getId());

            if (status != null)
                user.setStatusMsg(status.getStatusMsg());*/

            userList.add(user);
        }
        return userList;
    }
}
