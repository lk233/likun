package com.example.y430.apptest40;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.File;
import java.util.Calendar;
import java.util.Random;

public class CompleteActivity extends Activity {
    /*展示姓名的EditText*/
    EditText displayName;
    /*展示性别的EditText*/
    TextView gender;
    /*展示生日的TextView*/
    TextView birthday;
    /*展示家乡的TextView*/
    TextView hometown;
    /*显示日期的日历对象*/
    Calendar calender = Calendar.getInstance();
    /*用来显示家乡信息的spinner*/
    Spinner spinner1;
    Spinner spinner2;
    /*完成按钮*/
    Button compeletion;
    /*用来设置头像的ImageView*/
    ImageView image;
    /*保存照片时候的图片名称*/
    String imageName;


    /*以回调的方式得到照相，本地相册选取，剪切Activity所返回的结果*/
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0) {//拍照
            try {
                File temp = new File(Environment.getExternalStorageDirectory()+"/yamii",imageName);
                if(!temp.exists())
                    return;
                startPhotoZoom(Uri.fromFile(temp));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }else if(requestCode==1){//本地获取
            try {
                startPhotoZoom(data.getData());
            } catch (NullPointerException e) {
                e.printStackTrace();// 用户点击取消操作
            }
        }else if(requestCode==2){//剪裁后的图片
            if (data != null) {
                setPicToView(data);
            }
        }
    }
  /*用来设置圆形头像*/
    public void setPicToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = extras.getParcelable("data");
            Bitmap copyPhoto=Bitmap.createScaledBitmap(photo, 300, 300, true);
            Bitmap bitmap=Bitmap.createBitmap(300,300,Bitmap.Config.ARGB_4444);
            Canvas canvas =new Canvas(bitmap);//源src
            Paint paint =new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(150,150,150, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(copyPhoto,new Rect(0,0,300,300),new RectF(0,0,300,300),paint);//目标
            image.setImageBitmap(bitmap);
        }
    }
   /*用来剪切图片*/
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/png");
    // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", true);
    // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
    // outputX outputY 是裁剪图片宽高
      //  intent.putExtra("outputX", 300);
       // intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);//剪裁图片响应码2
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completedata);//清除EditText的焦点，好看点
       /*获取所需的组件*/
        displayName = (EditText) findViewById(R.id.comdata_name);
        gender = (TextView) findViewById(R.id.comdata_sex);
        birthday = (TextView) findViewById(R.id.comdata_birthday);
        hometown = (TextView) findViewById(R.id.comdata_hometown);
        compeletion = (Button) findViewById(R.id.comdata_complete);
        image = (ImageView) findViewById(R.id.comdata_user_face);

        image.setOnClickListener(new View.OnClickListener() {
            String[] items = {"拍照", "相册"};

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CompleteActivity.this)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {//拍照
                                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    Random r=new Random();//用来区分每次产生的不同照片
                                    int i=r.nextInt(1000);
                                    imageName="userImage"+i+".png";//文件名
                                    File file=new File(Environment.getExternalStorageDirectory().getPath() + "/yamii");
                                    if(!file.exists())
                                    file.mkdir();
                                    File imageFile=new File(file.getPath(),imageName);
                                    //下面这句指定调用相机拍照后的照片存储的路径
                                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                                    startActivityForResult(takeIntent, 0);//改请求的请求码为0
                                } else {
                                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                                    // 如果朋友们要限制上传到服务器的图片类型时可以直接写如：image/jpeg 、 image/png等的类型
                                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/png");
                                    startActivityForResult(pickIntent, 1);//请求码为1
                                }
                            }
                        });
                builder.create().show();
            }
        });


        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayName.clearFocus();
                final String items[] = {"男", "女", "未知"};
                Builder builder = new Builder(CompleteActivity.this)
                        .setTitle("性别选择")
                        .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                gender.setText(items[which]);
                            }
                        }).setPositiveButton("确定", null)
                        .setNegativeButton("取消", null);
                builder.show();
            }
        });

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayName.clearFocus();
                DatePickerDialog dialog = new DatePickerDialog(CompleteActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calender.set(year, monthOfYear, dayOfMonth);
                        birthday.setText(DateFormat.format("yyy-MM-dd", calender));
                    }
                }, calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        hometown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayName.clearFocus();
                /*加载对话框布局文件*/
                final TableLayout hometownForm = (TableLayout) getLayoutInflater().inflate(R.layout.dialog_layout, null);
                  new Builder(CompleteActivity.this)
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
                        hometown.setText(item);
                        if (item.equals("上海市")) {
                            String[] cities = getResources().getStringArray(R.array.shanghai);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("天津市")){
                            String[] cities = getResources().getStringArray(R.array.tianjing);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("河北省")){
                            String[] cities = getResources().getStringArray(R.array.hebei);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("山西省")){
                            String[] cities = getResources().getStringArray(R.array.shanghai);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("内蒙古自治区")){
                            String[] cities = getResources().getStringArray(R.array.neimeng);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("辽宁省")){
                            String[] cities = getResources().getStringArray(R.array.liaoning);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("吉林省")){
                            String[] cities = getResources().getStringArray(R.array.jiling);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("黑龙江省")){
                            String[] cities = getResources().getStringArray(R.array.heilongjiang);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("江苏省")){
                            String[] cities = getResources().getStringArray(R.array.jiangsu);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("浙江省")){
                            String[] cities = getResources().getStringArray(R.array.zhejiang);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("安徽省")){
                            String[] cities = getResources().getStringArray(R.array.anhui);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("福建省")){
                            String[] cities = getResources().getStringArray(R.array.fujian);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("江西省")){
                            String[] cities = getResources().getStringArray(R.array.jiangxi);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("山东省")){
                            String[] cities = getResources().getStringArray(R.array.shandong);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("河南省")){
                            String[] cities = getResources().getStringArray(R.array.henan);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("湖北省")){
                            String[] cities = getResources().getStringArray(R.array.hubei);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("湖南省")){
                            String[] cities = getResources().getStringArray(R.array.hunan);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("广东省")){
                            String[] cities = getResources().getStringArray(R.array.guangdong);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("广西壮族自治区")){
                            String[] cities = getResources().getStringArray(R.array.guangxi);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("海南省")){
                            String[] cities = getResources().getStringArray(R.array.hainan);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("重庆市")){
                            String[] cities = getResources().getStringArray(R.array.chongqing);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("四川省")){
                            String[] cities = getResources().getStringArray(R.array.sichuan);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("贵州省")){
                            String[] cities = getResources().getStringArray(R.array.guizhou);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("云南省")){
                            String[] cities = getResources().getStringArray(R.array.yunnan);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("西藏自治区")){
                            String[] cities = getResources().getStringArray(R.array.xizang);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("陕西省")){
                            String[] cities = getResources().getStringArray(R.array.sangxi);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("甘肃省")){
                            String[] cities = getResources().getStringArray(R.array.gansu);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("青海省")){
                            String[] cities = getResources().getStringArray(R.array.qinghai);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("宁夏回族自治区")){
                            String[] cities = getResources().getStringArray(R.array.ningxia);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("新疆维吾尔自治区")){
                            String[] cities = getResources().getStringArray(R.array.xingjiang);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("香港特别行政区")){
                            String[] cities = getResources().getStringArray(R.array.hangkang);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("澳门特别行政区")){
                            String[] cities = getResources().getStringArray(R.array.aomen);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("台湾省")){
                            String[] cities = getResources().getStringArray(R.array.taiwan);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }else if(item.equals("北京市")){
                            String[] cities = getResources().getStringArray(R.array.beijing);
                            ArrayAdapter<String> adpter = new ArrayAdapter<String>(CompleteActivity.this, android.R.layout.simple_gallery_item, cities);
                            spinner2.setAdapter(adpter);
                        }

                    }
                    /*无用*/
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
                        hometown.setText(hometown.getText().toString() + item);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
        compeletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入框中的数据
                String name=displayName.getText().toString();
                String sex=gender.getText().toString();
                String date=birthday.getText().toString();
                String home=hometown.getText().toString();
            }
        });
    }

}
