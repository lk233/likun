package com.example.y430.apptest40;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

/**
 * Created by lenovo on 2016/3/14.
 */
public class teacherWindow extends Activity{
    Button tReturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tReturn=(Button)findViewById(R.id.teacher_return);
    }
}
