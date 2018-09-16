package com.musipo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.musipo.R;
import com.musipo.constant.Config;
import com.musipo.constant.Constant;
import com.musipo.constant.RequestEnum;
import com.musipo.fcm.MyFirebaseInstanceIDService;
import com.musipo.helper.MyApplication;
import com.musipo.model.User;
import com.musipo.model.json.ResponceJson;
import com.musipo.restcall.IServiceListener;
import com.musipo.restcall.JsonController;
import com.musipo.restcall.Request;
import com.musipo.restcall.RequestMethod;
import com.musipo.restcall.ServiceFrontController;
import com.musipo.service.ServerSyncServices;
import com.musipo.util.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;


public class OTPVerifyActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtNumber;
    private Button btnVerify;
    private Button txtResend;
    private EditText _edtVerifyNo;
    private String _otp = "";
    private Context _context;
    private String TAG = OTPVerifyActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverify);
        processBundleExtras(savedInstanceState);
        init();
        initListner();
    }

    private void init() {
        _context = this;
        _edtVerifyNo = (EditText) findViewById(R.id.edtVerifyNo);
        btnVerify = (Button) findViewById(R.id.btnVerify);
        txtResend = (Button) findViewById(R.id.btnResend);
        //  textView = (TextView)findViewById(R.id.textView);
        //  textView.setText("We've sent an OTP via sms to "+VerifyCredentialsActivity._edtMobileNo.getText().toString()+", Please wait while we fetch it for you.");

    }

    final public void processBundleExtras(Bundle extras) {

        Bundle bundle = getIntent().getExtras();
        User user = (User) bundle.getSerializable(Constant.BUNDLE_ARGUMENT_USER_DATA);
        validateMobileNumber(user);
        Log.e(TAG, "USER:" + user);
    }


    private void initListner() {
        btnVerify.setOnClickListener(this);
        txtResend.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnVerify:

                if (!_otp.equals(_edtVerifyNo.getText().toString())) {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    UiUtils.showMessage(this, "Invalide OTP");
                }
                break;
            default:
                break;
        }

    }

    private void validateMobileNumber(final User user) {
        JSONObject json = new JSONObject();
        JSONObject jparams = new JSONObject();
        JSONObject restData = new JSONObject();

        try {

            json.put("api_key", Constant.API_KEY);
            jparams.put("mobile_no", user);

            restData.put(Request.params, jparams);
            restData.put(Request.methodName, "user/verifymobile");

            Log.i(TAG, "Insert User Request\n" + restData);

            ServiceFrontController.getInstance().addMethod(
                    RequestEnum.verifymobilenumber.toString(),
                    Request.class);

            ServiceFrontController.getInstance().fireCommand(this,
                    RequestEnum.verifymobilenumber.toString(), restData, new IServiceListener() {
                        @Override
                        public void result(String jsonResult) {
                            ResponceJson responceObj = null;
                            responceObj = JsonController.getRespnceJson(jsonResult);

                            if (responceObj.isStatus()) {

                                try {
                                    _otp = responceObj.getData().getString("otp");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                getUser(user);

                            } else {
                                Log.d(TAG, responceObj.getMessage());
                            }

                            UiUtils.setText(_edtVerifyNo, _otp);
                        }

                        @Override
                        public void fault(String message) {

                        }
                    }, RequestMethod.GET);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getUser(final User user1) {

        JSONObject json = new JSONObject();
        JSONObject jparams = new JSONObject();
        JSONObject restData = new JSONObject();

        try {

            json.put("api_key", Constant.API_KEY);
            jparams.put("mobile_no", user1.getMobile());
            jparams.put("name", user1.getName());

            restData.put(Request.params, jparams);
            restData.put(Request.methodName, "user/login");

            Log.i(TAG, "Insert User Request\n" + restData);

            ServiceFrontController.getInstance().addMethod(
                    RequestEnum.login.toString(),
                    Request.class);

            ServiceFrontController.getInstance().fireCommand(this,
                    RequestEnum.login.toString(), restData, new IServiceListener() {
                        @Override
                        public void result(String jsonResult) {

                            ResponceJson responceObj = null;
                            responceObj = JsonController.getRespnceJson(jsonResult);
                            JsonController<User> jsonController = new JsonController<User>();
                            User user = jsonController
                                      .getModelFromData(responceObj.getData().toString(), User.class);

                            user.setMobile(user1.getMobile());
                            if (responceObj.isStatus()) {
                                MyApplication.getInstance().getPrefManager().storeUser(user);
                                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                                String regId = pref.getString("regId", null);

                             /*   ServerSyncServices  serverSyncServices = new ServerSyncServices(getApplicationContext());
                             //   serverSyncServices.createChatRoom();*/
                                try {
                                    MyFirebaseInstanceIDService.sendRegistrationToServer(regId, _context);
                                } catch (Exception e) {
                                    Log.i(TAG, e.getMessage());
                                }
                            }
                        }

                        @Override
                        public void fault(String message) {
                           Log.i(TAG, message);
                        }
                    }, RequestMethod.POST);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
