package com.musipo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.musipo.R;
import com.musipo.constant.Constant;
import com.musipo.model.User;
import com.musipo.util.UiUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button _btnRegiser;
    private EditText _edtMobileNo;
    private EditText _edtName;

    private String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        _btnRegiser = (Button) findViewById(R.id.btnRegister);
        _edtMobileNo = (EditText) findViewById(R.id.edtMobileNo);
        _edtName = (EditText) findViewById(R.id.edtEmail);
        initUIListeners();
    }

    final public void initUIListeners() {
        _btnRegiser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnRegister:
                String mobileNo = _edtMobileNo.getText().toString();
                String name = _edtName.getText().toString();

                User user = new User();
                user.setName(name);
                user.setMobile(mobileNo);
                if (mobileNo.length() == 10) {
                    Intent intent = new Intent(this, OTPVerifyActivity.class);
                    intent.putExtra(Constant.BUNDLE_ARGUMENT_USER_DATA, user);
                    startActivity(intent);
                    finish();
                } else {
                    UiUtils.showMessage(this, "Enter the valid mobile number");
                    UiUtils.setText(_edtMobileNo, "");
                }
                break;
            default:
                break;
        }

    }


}
