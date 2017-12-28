package com.example.zhang.relationshipManager.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zhang.relationshipManager.R;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.editText_userName)
    EditText editTextUserName;
    @BindView(R.id.editText_password)
    EditText editTextPassword;
    @BindView(R.id.buttonLogin)
    Button buttonLogin;
    @BindView(R.id.buttonRegister)
    Button buttonRegister;
    @BindView(R.id.editText_confirmPwd)
    EditText editTextConfirmPwd;
    @BindView(R.id.textInputLayout_userName)
    TextInputLayoutFixedErrorSpace textInputLayoutUserName;
    @BindView(R.id.textInputLayout_password)
    TextInputLayoutFixedErrorSpace textInputLayoutPassword;
    @BindView(R.id.textInputLayout_confirmPwd)
    TextInputLayoutFixedErrorSpace textInputLayoutConfirmPwd;

    private LocalBroadcastManager localBroadcastManager;
    @BindView(R.id.textView_switch)
    TextView textViewSwitch;

    private String action = "URLCallback_Login_Activity";

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
                MainActivity.startActivity(getApplicationContext());
                if (textInputLayoutConfirmPwd.getError() == null && textInputLayoutPassword.getError() == null) {
                    Map<String, String> args = new HashMap<>();
                    String userName = editTextUserName.getText().toString();
                    String pwd = editTextPassword.getText().toString();
                    if (userName.length() == 0 || pwd.length() == 0)
                        return;
                    args.put("userName", userName);
                    try {
                        String password;
                        MessageDigest mdEnc;
                        mdEnc = MessageDigest.getInstance("MD5");
                        mdEnc.update(pwd.getBytes(), 0, pwd.length());
                        pwd = new BigInteger(1, mdEnc.digest()).toString(16);
                        while (pwd.length() < 32) {
                            pwd = "0" + pwd;
                        }
                        password = pwd;
                        args.put("pwd", password);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }

                    // @todo Send data for login verification
                }
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // @todo Just for start Main
                MainActivity.startActivity(getApplicationContext());
                if (textInputLayoutConfirmPwd.getError() == null && textInputLayoutPassword.getError() == null && textInputLayoutUserName.getError() == null) {
                    Map<String, String> args = new HashMap<>();
                    String userName = editTextUserName.getText().toString();
                    String pwd = editTextPassword.getText().toString();
                    String confirm_pwd = editTextConfirmPwd.getText().toString();
                    if (userName.length() == 0 || pwd.length() == 0)
                        return;
                    if (!pwd.equals(confirm_pwd))
                        return;
                    args.put("userName", userName);
                    try {
                        String password;
                        MessageDigest mdEnc;
                        mdEnc = MessageDigest.getInstance("MD5");
                        mdEnc.update(pwd.getBytes(), 0, pwd.length());
                        pwd = new BigInteger(1, mdEnc.digest()).toString(16);
                        while (pwd.length() < 32) {
                            pwd = "0" + pwd;
                        }
                        password = pwd;
                        args.put("pwd", password);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }

                    // @todo Send data for register verification

                    MainActivity.startActivity(getApplicationContext());
                }
            }
        });

        editTextUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    textInputLayoutUserName.setError(null);

                    String userName = editTextUserName.getText().toString();
                    if ("".equals(userName)) {
                        textInputLayoutUserName.setError(getString(R.string.emptyUsername));
                    }
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

        editTextConfirmPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    textInputLayoutConfirmPwd.setError(null);

                    String pwdConfirm = editTextConfirmPwd.getText().toString();
                    if (!editTextPassword.getText().toString().equals(pwdConfirm))
                        textInputLayoutConfirmPwd.setError(getString(R.string.pwdNotMatch));
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
        textInputLayoutUserName.setError(null);
        textInputLayoutPassword.setError(null);
    }
}