package com.example.y430.apptest40;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by lenovo on 2016/3/15.
 */
public class PersonalAcitvity extends Activity {
    /*用来处理头像改变的RelativeLayout*/
    RelativeLayout headButtonl;
    /*用来处理名字改变的RelativeLayout*/
    RelativeLayout nameButtonl;
    /*用来处理性别改变的RelativeLayout*/
    RelativeLayout sexButtonl;
    /*用来处理生日改变的RelativeLayout*/
    RelativeLayout birthdayButtonl;
    /*用来处理家乡改变的RelativeLayout*/
    RelativeLayout homeButtonl;
    /*用来处理个性签名改变的RelativeLayout*/
    RelativeLayout sentenTextViewl;
    /*作为头像的ImageView*/
    ImageView smallImage;
    /*显示名字的TextView*/
    TextView nameTextView;
    /*显示性别的TextView*/
    TextView sexTextView;
    /*显示生日信息的TextView*/
    TextView birthdayTextView;
    /*显示家乡的TextView*/
    TextView homeTextView;
    /*显示个性签名的TextView*/
    TextView sentenTextView;
    Calendar calender = Calendar.getInstance();
    /*做家乡显示的spinner*/
    Spinner spinner1;
    Spinner spinner2;


   /*用回调的方式得到SelectPopupWindow传来的数据*/
    protected void onActivityResult ( int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null)
            return;
        Bundle bundle = data.getExtras();
        Bitmap bitmap = bundle.getParcelable("bitmap");
        smallImage.setImageBitmap(bitmap);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_information);

        headButtonl = (RelativeLayout) findViewById(R.id.headLinear);
        nameButtonl = (RelativeLayout) findViewById(R.id.nameLinear);
        sexButtonl = (RelativeLayout) findViewById(R.id.sexLinear);
        birthdayButtonl = (RelativeLayout) findViewById(R.id.birthdayLinear);
        homeButtonl = (RelativeLayout) findViewById(R.id.homeLinear);
        sentenTextViewl=(RelativeLayout) findViewById(R.id.sentenceLinear);
        smallImage = (ImageView) findViewById(R.id.smallFaceImage);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        sexTextView = (TextView) findViewById(R.id.sexTextView);
        birthdayTextView = (TextView) findViewById(R.id.birthdayTextView);
        homeTextView = (TextView) findViewById(R.id.homeTextView);
        sentenTextView = (TextView) findViewById(R.id.sentenceTextView);



        headButtonl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*通过开启SelectPopupWindow来完成剩下的逻辑*/
                startActivityForResult(new Intent(PersonalAcitvity.this, SelectPopupWindow.class), 0);//响应码为0
            }
        });

        nameButtonl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TableLayout dialogLayout = (TableLayout) getLayoutInflater().inflate(R.layout.simple_dialog, null);
                final EditText s = (EditText) dialogLayout.findViewById(R.id.changeable_text);
                if(!nameTextView.getText().toString().equals(""))
                    s.setText(nameTextView.getText().toString());
                new AlertDialog.Builder(PersonalAcitvity.this)
                        .setTitle("姓名修改")
                        .setView(dialogLayout)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                nameTextView.setText(s.getText().toString());
                                s.setText("");
                            }
                        })
                        .setNegativeButton("取消", null).create().show();
            }
        });


        sexButtonl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String items[] = {"男", "女", "未知"};
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonalAcitvity.this)
                        .setTitle("性别选择")
                        .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sexTextView.setText(items[which]);
                            }
                        }).setPositiveButton("确定", null)
                        .setNegativeButton("取消", null);
                builder.show();
            }
        });

        birthdayButtonl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(PersonalAcitvity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calender.set(year, monthOfYear, dayOfMonth);
                        birthdayTextView.setText(DateFormat.format("yyy-MM-dd", calender));
                    }
                }, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        homeButtonl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*加载对话框布局文件*/
                final TableLayout hometownForm = (TableLayout) getLayoutInflater().inflate(R.layout.dialog_layout, null);
                new AlertDialog.Builder(PersonalAcitvity.this)
                        .setTitle("")
                        .setView(hometownForm)
                        .setPositiveButton("确定", null)
                        .setNegativeButton("取消", null).create().show();

                spinner1=(Spinner)hometownForm.findViewById(R.id.spin_province);
                spinner2=(Spinner)hometownForm.findViewById(R.id.spin_city);
                /*设置spinner1的点击监听事件*/
                spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        spinner1 = (Spinner) parent;
                        String item = (String) spinner1.getItemAtPosition(position);
                        homeTextView.setText(item);
                        if (item.equals("上海市")) {
                            String[] cities = getResources().getStringArray(R.array.shanghai);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("天津市")){
                            String[] cities = getResources().getStringArray(R.array.tianjing);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("河北省")){
                            String[] cities = getResources().getStringArray(R.array.hebei);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("山西省")){
                            String[] cities = getResources().getStringArray(R.array.shanghai);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("内蒙古自治区")){
                            String[] cities = getResources().getStringArray(R.array.neimeng);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("辽宁省")){
                            String[] cities = getResources().getStringArray(R.array.liaoning);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("吉林省")){
                            String[] cities = getResources().getStringArray(R.array.jiling);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("黑龙江省")){
                            String[] cities = getResources().getStringArray(R.array.heilongjiang);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("江苏省")){
                            String[] cities = getResources().getStringArray(R.array.jiangsu);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("浙江省")){
                            String[] cities = getResources().getStringArray(R.array.zhejiang);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("安徽省")){
                            String[] cities = getResources().getStringArray(R.array.anhui);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("福建省")){
                            String[] cities = getResources().getStringArray(R.array.fujian);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("江西省")){
                            String[] cities = getResources().getStringArray(R.array.jiangxi);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("山东省")){
                            String[] cities = getResources().getStringArray(R.array.shandong);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("河南省")){
                            String[] cities = getResources().getStringArray(R.array.henan);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("湖北省")){
                            String[] cities = getResources().getStringArray(R.array.hubei);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("湖南省")){
                            String[] cities = getResources().getStringArray(R.array.hunan);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("广东省")){
                            String[] cities = getResources().getStringArray(R.array.guangdong);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("广西壮族自治区")){
                            String[] cities = getResources().getStringArray(R.array.guangxi);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("海南省")){
                            String[] cities = getResources().getStringArray(R.array.hainan);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("重庆市")){
                            String[] cities = getResources().getStringArray(R.array.chongqing);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("四川省")){
                            String[] cities = getResources().getStringArray(R.array.sichuan);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("贵州省")){
                            String[] cities = getResources().getStringArray(R.array.guizhou);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("云南省")){
                            String[] cities = getResources().getStringArray(R.array.yunnan);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("西藏自治区")){
                            String[] cities = getResources().getStringArray(R.array.xizang);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("陕西省")){
                            String[] cities = getResources().getStringArray(R.array.sangxi);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("甘肃省")){
                            String[] cities = getResources().getStringArray(R.array.gansu);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("青海省")){
                            String[] cities = getResources().getStringArray(R.array.qinghai);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("宁夏回族自治区")){
                            String[] cities = getResources().getStringArray(R.array.ningxia);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("新疆维吾尔自治区")){
                            String[] cities = getResources().getStringArray(R.array.xingjiang);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("香港特别行政区")){
                            String[] cities = getResources().getStringArray(R.array.hangkang);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("澳门特别行政区")){
                            String[] cities = getResources().getStringArray(R.array.aomen);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("台湾省")){
                            String[] cities = getResources().getStringArray(R.array.taiwan);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("北京市")){
                            String[] cities = getResources().getStringArray(R.array.beijing);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(PersonalAcitvity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                /*设置spinner的点击监听事件*/
                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        spinner2 = (Spinner) parent;
                        String item = (String) spinner2.getItemAtPosition(position);
                        homeTextView.setText(homeTextView.getText().toString() + item);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

        });

        sentenTextViewl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TableLayout dialogLayout = (TableLayout) getLayoutInflater().inflate(R.layout.simple_dialog, null);
                final EditText s = (EditText) dialogLayout.findViewById(R.id.changeable_text);
                if (!sentenTextView.getText().toString().equals(""))
                    s.setText(sentenTextView.getText().toString());
                new AlertDialog.Builder(PersonalAcitvity.this)
                        .setTitle("个性签名")
                        .setView(dialogLayout)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sentenTextView.setText(s.getText().toString());
                                s.setText("");
                            }
                        })
                        .setNegativeButton("取消", null).create().show();
            }
        });

    }
}
