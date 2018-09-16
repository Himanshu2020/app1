package com.musipo.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;


public class UiUtils {

	final public static void setText(final EditText editText, final String text) {
		if(text != null)
			editText.setText(text);
	}

	final public static void setText(final TextView textView, final String text) {
		if(text != null)
			textView.setText(text);
	}

	final public static void setTypeFace(final TextView textView, final Typeface typeface){
		textView.setTypeface(typeface);
	}

	final public static boolean isEmpty(EditText _editText) {
		if(_editText.getText().toString().equals(""))
			return true;
		return false;
	}



	final public static EditText isEmpty(EditText _editTexts[]) {
		if(_editTexts != null)
		{
			int size = _editTexts.length;
			for (int i = 0; i < size; i++) {

				if(_editTexts[i].getText().toString().equals("")){
					_editTexts[i].requestFocus();
					return _editTexts[i];
				}
			}
		}
		return null;
	}

	final public static EditText isEmpty(Context context, EditText _editTexts[]) {
		if(_editTexts != null)
		{
			int size = _editTexts.length;
			for (int i = 0; i < size; i++) {

				if(_editTexts[i].getText().toString().equals("")){
					_editTexts[i].requestFocus();
					showKeyboard(context, _editTexts[i]);
					return _editTexts[i];
				}
			}
		}
		return null;
	}

	public static void showKeyboard(Context context , EditText _editText) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(_editText, InputMethodManager.SHOW_IMPLICIT);

	}

	public static void hideSoftKeyboard(Context context , EditText _editText) {
		InputMethodManager imm = (InputMethodManager)context.getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(_editText.getWindowToken(), 0);
	}

	/**
	 * Hides the soft keyboard
	 */
	public static void hideSoftKeyboard(Activity activity) {
	    if(activity.getCurrentFocus()!=null) {
	        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	    }
	}

	/**
	 * Shows the soft keyboard
	 */
	public static void showSoftKeyboard(View view, Activity activity) {
	    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    view.requestFocus();
	    inputMethodManager.showSoftInput(view, 0);
	}



	final public static int length(EditText _editText) {
		return _editText.getText().toString().length();
	}

	public static void showMessage(Context context, String msg) {
		Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
	}

	public static void showToastMessage(Context context, String msg) {
		Toast.makeText(context,msg, Toast.LENGTH_LONG).show();
	}
	public static void showShortToastMessage(Context context, String msg) {
		Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
	}

	public static void showToastMessage(Context context, String msg, boolean flag) {
		if (flag) {
			Toast.makeText(context,msg, Toast.LENGTH_LONG).show();
		}
	}

	public static ProgressDialog showButtonProgressDialog(Context mContext) {
		ProgressDialog pDialog = new ProgressDialog(mContext);
		pDialog.setCancelable(false);
		pDialog.setMessage("Please wait...");
		pDialog.show();
		return pDialog;
	}
	public static ProgressDialog showButtonProgressDialog(Context mContext, String message) {
		ProgressDialog pDialog = new ProgressDialog(mContext);
		pDialog.setCancelable(false);
		pDialog.setMessage(message+"...");
		pDialog.show();
		return pDialog;
	}



	public static void setEditTextHint(final EditText editText, final int resourceId)
	{
		editText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				if(hasFocus == true){
					editText.setHint("");
					editText.setCursorVisible(true);
				}
				else if(hasFocus == false){
					editText.setHint(resourceId);
				}
			}
		});
	}

	public static void setErrorOnFocusOut(final EditText editText, final CharSequence errorText, final int length)
	{
		editText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				if(hasFocus == true){
//					editText.setError(null);
//					editText.setCursorVisible(true);
				}
				else if(hasFocus == false){
					if(length(editText) > 0 && length(editText) < length ){
						editText.setError(errorText);
//						editText.requestFocus();
					}
//					else
//						editText.setError(null);
				}
			}
		});
	}

	public static void closeActivity(Context context) {

		Activity activity = (Activity) context ;
		activity.finish();
	}

	public static void closeProgressDialog(ProgressDialog pDialog) {
		try {
			if (pDialog != null)
				pDialog.dismiss();
		} catch (Exception e) {
		}
	}


	public static boolean isEmailValidated(final Context context, final EditText _editText){

		Pattern pattern	 = Patterns.EMAIL_ADDRESS;
		String email = _editText.getText().toString().trim();
		boolean flag = pattern.matcher(email).matches();


		if (flag == false && email.length() > 0) {
			_editText.setError("Invalid Email address");
//			_editText.requestFocus();
			return false;
		}else{
			_editText.setError(null);
		}
		return true;
	}

	public static void setNote(final EditText _editText, final boolean condition) {
		_editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!condition)
					_editText.setVisibility(View.VISIBLE);
				else
					_editText.setVisibility(View.GONE);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

	}

	/**
	 * @param context
	 * @param _editText
	 * @return  filter to prevent special character
	 */
	public static InputFilter getFilter(final Context context, final EditText _editText) {

		final String blockCharacterSet = "~#^|$%&*!?;()+-`";
	    return new InputFilter() {
	        @Override
	        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

	            if (source != null && blockCharacterSet.contains(("" + source))) {
	                return "";
	            }
	            return null;
	        }
	    };
	}

	public static boolean validateMobileNo(String mobileno){
		boolean check = false;
		if(mobileno.length() < 10 || mobileno.length() > 13)
		{
			check = false;
		}
		else if(mobileno.matches("(0|\\+91|91|)[7-9][0-9]{9}")== false)
		{
			check = false;
		}
		else
		{
			check = true;
		}
		return check;
	}

	public static boolean isDeviceOnline(Activity activity) {
		ConnectivityManager connMgr =(ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}




	public static final int PROFILEPIC_CAPTURE_IMAGE_REQUEST_CODE = 300;
	public static final int MEDIA_TYPE_IMAGE = 2;
	public static final String IMAGE_DIRECTORY_NAME = "ibilling";
}