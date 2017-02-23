package dasudian.com.im.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dasudian.im.sdk.Connection;
import com.dasudian.im.sdk.ConnectionCallback;

import dasudian.com.im.demo.util.Util;

public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";
    private EditText et_username;
    private EditText et_signature;
    private EditText et_auc_url;
    private EditText et_appid;
    private EditText et_appsec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_username = (EditText) findViewById(R.id.et_username);
        et_signature = (EditText) findViewById(R.id.et_signature);
        et_auc_url = (EditText) findViewById(R.id.et_server_url);
        et_appid = (EditText) findViewById(R.id.et_appid);
        et_appsec = (EditText) findViewById(R.id.et_appsec);
    }

    public void login(View view) {
        String username = et_username.getText().toString();
        String signature = et_signature.getText().toString();
        String serverUrl = et_auc_url.getText().toString();
        String appid = et_appid.getText().toString();
        String appsec = et_appsec.getText().toString();

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(signature)
                && !TextUtils.isEmpty(appid) && !TextUtils.isEmpty(appsec)) {
            MyApplication.connectIMServer(username, signature, serverUrl, appid, appsec, new ConnectionCallback() {
                @Override
                public void onConnect(Connection connection) {
                    super.onConnect(connection);
                    Log.i(TAG, "onConnect");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.showToast(LoginActivity.this, "onConnect");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }

                @Override
                public void onConnectError(String reason) {
                    super.onConnectError(reason);
                    Log.i(TAG, "onConnectError");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.showToast(LoginActivity.this, "onConnectError");
                        }
                    });
                }

                @Override
                public void onConnectionLost() {
                    super.onConnectionLost();
                    Log.i(TAG, "onConnectionLost");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.showToast(LoginActivity.this, "onConnectionLost");
                        }
                    });
                }
            });
        } else {
            Toast.makeText(this, "username or signature is null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
