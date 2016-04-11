package com.example.y430.apptest40;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2016/3/8.
 */
public class Register extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);


        //给注册按钮设置点击侦听
        Button bn_register = (Button) findViewById(R.id.register_register);
        bn_register.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //获取输入的用户名以及密码
                EditText ed_mail = (EditText) findViewById(R.id.register_email);
                EditText ed_password = (EditText) findViewById(R.id.register_password);
                EditText ed_checkpsw = (EditText) findViewById(R.id.register_checkpsw);

                String name = ed_mail.getText().toString();
                String password = ed_password.getText().toString();
                String checkpsw = ed_checkpsw.getText().toString();

                //满足如下条件即可注册
                if(isEmail(name) == false)
                    Toast.makeText(Register.this,"邮箱的格式错误",Toast.LENGTH_SHORT).show();
                else if(password.length() < 6)
                    Toast.makeText(Register.this,"密码长度要超过六位",Toast.LENGTH_SHORT).show();
                else if(!(password.equals(checkpsw)))
                    Toast.makeText(Register.this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
                else {
                    //注册过程
                    AVUser user = new AVUser();
                    user.setUsername(name);
                    user.setPassword(password);
                    user.setEmail(name);

                    user.signUpInBackground(new SignUpCallback() {
                        public void done(AVException e) {
                            if (e == null) {
                                // successfully
                                Toast.makeText(Register.this,"注册成功",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.setClass(Register.this,Today.class);
                                startActivity(intent);
                            } else {
                                // failed
                                Toast.makeText(Register.this, "您的用户名已被注册", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        //给跳过标签设置监听器
        TextView tv_skip = (TextView) findViewById(R.id.register_skip);
        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent();
                intent.setClass(Register.this,Today.class);
                startActivity(intent);
            }
        });

        //给返回登录界面设置监听器
        TextView tv_login = (TextView) findViewById(R.id.register_login);
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //判断邮箱的格式是否正确
    public static boolean isEmail(String strEmail) {
        String strPattern = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }
}
