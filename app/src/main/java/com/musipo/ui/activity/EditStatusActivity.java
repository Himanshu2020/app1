package com.musipo.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.musipo.R;
import com.musipo.constant.RequestEnum;
import com.musipo.helper.MyApplication;
import com.musipo.model.User;
import com.musipo.model.json.ResponceJson;
import com.musipo.restcall.IServiceListener;
import com.musipo.restcall.JsonController;
import com.musipo.restcall.Request;
import com.musipo.restcall.RequestMethod;
import com.musipo.restcall.ServiceFrontController;
import com.musipo.util.UiUtils;

import java.util.HashMap;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class EditStatusActivity extends AppCompatActivity {

    private EmojiconEditText inputMessage;
    private ImageView btnSend;
    private EditStatusActivity _context;
    private Button _btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_status);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update Status");
        toolbar.setTitle("Update Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    @Override
    protected void onResume() {
        init();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        init();
        super.onRestart();
    }

    final private void showEmoji() {

        View rootView = findViewById(R.id.root_view_status);
        EmojIconActions emojIcon = new EmojIconActions(getApplicationContext(), rootView, inputMessage, btnSend);
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard,R.drawable.smiley);
        emojIcon.setUseSystemEmoji(false);
        inputMessage.setUseSystemDefault(false);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Keyboard", "open");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard", "close");
            }
        });
       /* EmojiconEditText emojiconEditText2 = (EmojiconEditText) findViewById(R.id.emojicon_edit_text2);
        emojIcon.addEmojiconEditTextList(emojiconEditText2);*/
    }

    final private void init() {

        _context = this;
        inputMessage = (EmojiconEditText) findViewById(R.id.emojicon_edit_text);
        btnSend = (ImageView) findViewById(R.id.smiley_btn);
        showEmoji();

        _btnOk = (Button) findViewById(R.id.okayBtnStatus);
        _btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                UiUtils.showMessage(_context,"Updating Status..");
                updateStatus();
            }

        });
    }

    public void updateStatus()  {
        User user = MyApplication.getInstance().getPrefManager().getUser();

        ServiceFrontController.getInstance().addMethod(
                RequestEnum.updateStatus.toString(),
                Request.class);

        HashMap<String, String> pramas = new HashMap<>();
        pramas.put(Request.methodName, "status/updateStatus");

        pramas.put("userID", user.getId());
        pramas.put("status", inputMessage.getText().toString());

        try {
            ServiceFrontController.getInstance().fireCommand(_context,
                    RequestEnum.updateStatus.toString(), pramas, new IServiceListener() {
                        @Override
                        public void result(String jsonResult) {

                            ResponceJson responceObj = null;
                            responceObj = JsonController.getRespnceJsonWithDataAsString(jsonResult);

                            if(responceObj.isStatus()) {
                                MyApplication.getInstance().getPrefManager().storeStatus(inputMessage.getText().toString());
                            }
                        }

                        @Override
                        public void fault(String message) {

                        }
                    }, RequestMethod.GET);
        } catch (Exception e) {
        }

    }



}
