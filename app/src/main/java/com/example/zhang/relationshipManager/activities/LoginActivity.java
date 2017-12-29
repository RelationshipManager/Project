package com.example.zhang.relationshipManager.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zhang.relationshipManager.Helper.ToastHelper;
import com.example.zhang.relationshipManager.R;
import com.example.zhang.relationshipManager.models.Contact;
import com.example.zhang.relationshipManager.models.Neo4jManager;
import com.example.zhang.relationshipManager.models.User;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.editText_password)
    EditText editTextPassword;
    @BindView(R.id.buttonLogin)
    Button buttonLogin;
    @BindView(R.id.buttonRegister)
    Button buttonRegister;
    @BindView(R.id.editText_confirmPwd)
    EditText editTextConfirmPwd;
    @BindView(R.id.textInputLayout_password)
    TextInputLayoutFixedErrorSpace textInputLayoutPassword;
    @BindView(R.id.textInputLayout_confirmPwd)
    TextInputLayoutFixedErrorSpace textInputLayoutConfirmPwd;
    @BindView(R.id.textInputLayout_phoneNumber)
    TextInputLayoutFixedErrorSpace textInputLayoutPhoneNumber;
    @BindView(R.id.editText_phoneNumber)
    EditText editTextPhoneNumber;


    private LocalBroadcastManager localBroadcastManager;
    @BindView(R.id.textView_switch)
    TextView textViewSwitch;

    private String action = "URLCallback_Login_Activity";

    Neo4jManager n = Neo4jManager.getInstance(getApplicationContext());

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {
        SetListener();
    }

    private void SetListener() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // @todo Just for start Main
                if (textInputLayoutPhoneNumber.getError() == null && textInputLayoutPassword.getError() == null) {
                    String phoneNum = editTextPhoneNumber.getText().toString();
                    String pwd = editTextPassword.getText().toString();
                    String password = null;
                    if (phoneNum.length() == 0 || pwd.length() == 0)
                        return;
                    try {
                        MessageDigest mdEnc;
                        mdEnc = MessageDigest.getInstance("MD5");
                        mdEnc.update(pwd.getBytes(), 0, pwd.length());
                        pwd = new BigInteger(1, mdEnc.digest()).toString(16);
                        while (pwd.length() < 32) {
                            pwd = "0" + pwd;
                        }
                        password = pwd;
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }

                    String finalPassword = password;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                int id;
                                if ((id = n.logIn(phoneNum, finalPassword)) == -1){
                                    ToastHelper.show(getApplicationContext(), "用户名或密码错误！");
                                    return;
                                }else {
                                    User.getInstance(getApplicationContext()).setUserId(id);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            MainActivity.startActivity(getApplicationContext());
                        }
                    }).start();
                }
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // @todo Just for start Main
                if (textInputLayoutConfirmPwd.getError() == null && textInputLayoutPassword.getError() == null
                        && textInputLayoutPhoneNumber.getError() == null) {
                    String pwd = editTextPassword.getText().toString();
                    String confirm_pwd = editTextConfirmPwd.getText().toString();
                    String phoneNumber = editTextPhoneNumber.getText().toString();
                    String password = null;
                    if (pwd.length() == 0 || phoneNumber.length() == 0)
                        return;
                    if (!pwd.equals(confirm_pwd))
                        return;
                    try {
                        MessageDigest mdEnc;
                        mdEnc = MessageDigest.getInstance("MD5");
                        mdEnc.update(pwd.getBytes(), 0, pwd.length());
                        pwd = new BigInteger(1, mdEnc.digest()).toString(16);
                        while (pwd.length() < 32) {
                            pwd = "0" + pwd;
                        }
                        password = pwd;
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }

                    // @todo Send data for register verification
                    Contact c = new Contact();
                    c.setPhoneNumber(phoneNumber);
                    String finalPassword = password;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (n.getUserId(phoneNumber).size() == 0) {
                                    ToastHelper.show(getApplicationContext(), "该手机已被注册！");
                                    return;
                                }
                                Neo4jManager.getInstance(getApplicationContext()).registerUser(c, finalPassword);
                            } catch (Exception e) {
                                e.printStackTrace();
                                if (e.getMessage() != null && e.getMessage().equals("rest error")) {
                                    ToastHelper.show(getApplicationContext(), "无法连接远程数据库");
                                }
                                return;
                            }
                            MainActivity.startActivity(getApplicationContext());
                        }
                    }).start();
                }
            }
        });

        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    textInputLayoutPassword.setError(null);

                    String pwd = editTextPassword.getText().toString();
                    if ("".equals(pwd))
                        textInputLayoutPassword.setError(getString(R.string.emptyPwd));
                    else if (pwd.length() < 6)
                        textInputLayoutPassword.setError(getString(R.string.tooShortPwd));
                }
            }
        });

        editTextConfirmPwd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                textInputLayoutConfirmPwd.setError(null);

                if (!editTextConfirmPwd.getText().toString().equals(editTextPassword.getText().toString()))
                    textInputLayoutConfirmPwd.setError(getString(R.string.pwdNotMatch));
                return false;
            }
        });

        editTextPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    textInputLayoutPhoneNumber.setError(null);

                    String phoneNumber = editTextPhoneNumber.getText().toString();
                    if ("".equals(phoneNumber))
                        textInputLayoutPhoneNumber.setError(getString(R.string.emptyPhone));
                    else if (phoneNumber.length() != 11)
                        textInputLayoutPhoneNumber.setError(getString(R.string.wrongPhonePattern));
                }
            }
        });

        textViewSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOrigin();
                if (getString(R.string.switchToLogin).equals(((TextView) v).getText().toString())) {
                    textInputLayoutConfirmPwd.setVisibility(View.GONE);
                    buttonRegister.setVisibility(View.GONE);
                    buttonLogin.setVisibility(View.VISIBLE);
                    ((TextView) v).setText(R.string.switchToRegister);
                } else {
                    textInputLayoutConfirmPwd.setVisibility(View.VISIBLE);
                    buttonRegister.setVisibility(View.VISIBLE);
                    buttonLogin.setVisibility(View.GONE);
                    ((TextView) v).setText(R.string.switchToLogin);
                }
            }
        });
    }

    private void setOrigin() {
        textInputLayoutConfirmPwd.setError(null);
        textInputLayoutPassword.setError(null);
        textInputLayoutPhoneNumber.setError(null);
    }
}