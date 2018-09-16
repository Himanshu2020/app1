package com.musipo.dao.tables;

public class UserTable extends CommanFiled{

	public static final String TABLE_NAME = "user_tbl";

	public static final String USER_NAME = "user_name";
	public static final String USER_TYPE = "user_type";
	public static final String ID = "id";
	public static final String USERID = "USER_ID";
	public static final String FIRSTNAME = "firstname";
	public static final String LASTNAME = "lastname";
	public static final String DOB = "date_of_birth";
	public static final String ADDRESS_LINE_1 = "address_line_1";
	public static final String ADDRESS_LINE_2 = "address_line_2";
	public static final String EMAIL = "email";
	public static final String MOBILE = "mobile";
	public static final String COUNTRY = "country";
	public static final String FACEBOOK_ID = "facebook_id";
	public static final String SUGARCRM_ID = "sugarcrm_id";
	public static final String LOGIN_TYPE = "login_type";
	public static final String DEVICE_ID = "device_id";
	public static final String PASSWORD = "password";
	public static final String POSTALCODE = "postalcode";
	public static final String CONTACT_STATUS = "contact_status";
	public static final String CITY = "city";
	public static final String LATITUDE = "Latitude";
	public static final String LONGITUDE = "Longitude";
	public static final String STATE = "state";
	public static final String BUSSINESS_TYPE = "bussiness_type";
	public static final String BUSSINESS_NAME = "bussiness_name";
	public static final String PROFILE_PIC = "profile_pic";
	public static final String FCM_REGISTRATION_ID = "fcm_registration_id";



	public static final String STATEMENT_CREATE_TABLE = "CREATE TABLE "
			+ TABLE_NAME + "("
			+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ USERID + " VARCHAR,"
			+ CREATE_DATE  + " VARCHAR,"
			+ UPDATED_DATE  + " VARCHAR,"
			+ USER_NAME + " VARCHAR,"
			+ FIRSTNAME  + " VARCHAR,"
			+ LASTNAME  + " VARCHAR,"
			+ FCM_REGISTRATION_ID  + " VARCHAR,"
			+ ADDRESS_LINE_1  + " VARCHAR,"
			+ ADDRESS_LINE_2  + " VARCHAR,"
			+ EMAIL + " VARCHAR,"
			+ MOBILE + " VARCHAR,"
			+ PROFILE_PIC  + " VARCHAR,"
			+ COUNTRY    + " VARCHAR,"
			+ LATITUDE  + " VARCHAR,"
			+ LONGITUDE  + " VARCHAR,"
			+ DEVICE_ID  + " VARCHAR,"
			+ USER_TYPE  + " VARCHAR,"
			+ POSTALCODE+ " VARCHAR,"
			+ CONTACT_STATUS+ " VARCHAR,"
			+ CITY + " VARCHAR,"
			+ STATE  + " VARCHAR,"
			+ BUSSINESS_TYPE  + " VARCHAR,"
			+ BUSSINESS_NAME  + " VARCHAR,"
			+ DELETED_FLAG + " INTEGER,"
			+ SYNC_ID + " INTEGER,"
			+ "unique(" + USERID + ")"
			+ ")";

	public static final String STATEMENT_SELECT = "select * from "
			+ UserTable.TABLE_NAME;
}
