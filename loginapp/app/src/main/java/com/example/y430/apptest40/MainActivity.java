package com.example.y430.apptest40;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import java.util.ArrayList;
import java.util.List;

import dishInformation.YMDish;
import getAndParse.GetHotDishes;
import getAndParse.GetTodayDishes;


public class MainActivity extends ActionBarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //设置leancloud 的 app id和key
        AVOSCloud.initialize(this, "tmAX26HrS79nP9m4MWIdNpd9-gzGzoHsz", "GRvOBv9r390Lwcd4QuzupmXu");
       /*获取菜式的新方法
        List<YMDish> objects_hot=new ArrayList<YMDish>();
        GetTodayDishes hotDishes=new GetTodayDishes(objects_hot);
        hotDishes.start();
        加上try这个一段，让主线程等待GetTodayDishes这个线程结束。
        try{
            hotDishes.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("拿到数据了");*/

        //给注册设置事件监听
        TextView tv_register = (TextView) findViewById(R.id.login_register);
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,PersonalAcitvity.class);
                startActivity(intent);
            }
        });

        //给跳过设置监听器
        TextView tv_skip = (TextView) findViewById(R.id.login_skip);
        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,DrawerActivity.class);//
                startActivity(intent);
            }
        });

        //给登录设置监听器
        Button bn_login = (Button) findViewById(R.id.login_login);
        bn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText ed_name = (EditText) findViewById(R.id.login_email);
                EditText ed_password = (EditText) findViewById(R.id.login_password);

                String username = ed_name.getText().toString();
                String password = ed_password.getText().toString();


                if (username.length() == 0)
                    Toast.makeText(MainActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                else if (password.length() == 0)
                    Toast.makeText(MainActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                else {
                    AVUser.logInInBackground(username, password, new LogInCallback() {
                        public void done(AVUser user, AVException e) {
                            if (e == null) {
                                // 登录成功
                                finish();
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, Today.class);
                                startActivity(intent);
                            } else {
                                // 登录失败
                                Toast.makeText(MainActivity.this, "用户名和密码错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });


    }

}
