package com.musipo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 *
 * Writes/reads an object to/from a private local file
 *
 *
 */
public class SessionManager {

	public static final String SESSION_PREFERENCES = "logged_in_user_preferences";
	private static final String SESSION_RUNNING = "session_running";
	private static final String SESSION_ENDED = "session_ended";
	public static String sessionId;

    public static String getSessionId() {
		return sessionId;
	}

	public static void setSessionId(String sessionId) {
		SessionManager.sessionId = sessionId;
	}

	public static void startSession(Context context, String sessionId) {

		SessionManager.sessionId = sessionId;

		SharedPreferences sharedpreferences;
		sharedpreferences = context.getSharedPreferences(
				SESSION_PREFERENCES, Context.MODE_PRIVATE);

		Editor editor = sharedpreferences.edit();
		editor.putBoolean(SESSION_RUNNING, true);
    	editor.putBoolean(SESSION_ENDED, false);
		editor.commit();

	}

    public static void endSession(Context context) {

    	sessionId = null;

    	SharedPreferences sharedpreferences;
    	sharedpreferences = context.getSharedPreferences(
    			SESSION_PREFERENCES, Context.MODE_PRIVATE);

    	Editor editor = sharedpreferences.edit();
    	editor.putBoolean(SESSION_RUNNING, false);
    	editor.putBoolean(SESSION_ENDED, true);
    	editor.commit();
    }

    public static boolean isSessionRunning(Context context) {

    	boolean isSessionRunning = false;
    	SharedPreferences sharedpreferences;
    	sharedpreferences = context.getSharedPreferences(SESSION_PREFERENCES, Context.MODE_PRIVATE);

		if (sharedpreferences.contains(SESSION_RUNNING)) {
			isSessionRunning = sharedpreferences.getBoolean(SESSION_RUNNING, false);

		}
		return isSessionRunning;
    }

}