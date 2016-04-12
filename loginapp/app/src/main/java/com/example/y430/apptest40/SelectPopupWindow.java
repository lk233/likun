package com.example.y430.apptest40;


import android.app.Activity;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.Random;

public class SelectPopupWindow extends Activity implements OnClickListener{
   /*选择头像，取消，照相的TextView*/
    private TextView   btn_pick_photo, btn_cancel ,btn_take_photo;
    /*拍照的到的照片名字*/
    String imageName;

    /*用回调的方式得到SelectPopupWindow传来的数据*/
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0) {//拍照
            try {
                File temp = new File(Environment.getExternalStorageDirectory()+"/yamii", imageName);
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
   /*剪切照片*/
    public void setPicToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = extras.getParcelable("data");
            Bitmap copyPhoto=Bitmap.createScaledBitmap(photo, 100, 100, true);
            Bitmap bitmap=Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_4444);
            Canvas canvas =new Canvas(bitmap);//源src
            Paint paint =new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(50, 50, 45, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(copyPhoto, new Rect(0, 0, 100, 100), new RectF(0, 0, 100, 100), paint);//目标
            Intent intent=getIntent();
            Bundle bundle=new Bundle();
            bundle.putParcelable("bitmap",bitmap);
            intent.putExtras(bundle);
            SelectPopupWindow.this.setResult(0, intent);
            SelectPopupWindow.this.finish();
        }
    }
    /*剪切图片*/
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
        setContentView(R.layout.popup_window);
        btn_take_photo = (TextView) this.findViewById(R.id.take_photo);
        btn_pick_photo = (TextView) this.findViewById(R.id.select_photo);
        btn_cancel = (TextView) this.findViewById(R.id.popup_cancel);


        /*点击取消按钮函数销毁Activity*/
        btn_cancel.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });
        //添加按钮监听
        btn_pick_photo.setOnClickListener(this);
        btn_take_photo.setOnClickListener(this);
    }

    /*实现onTouchEvent触屏函数但点击屏幕时销毁本Activity*/
    @Override
    public boolean onTouchEvent(MotionEvent event){
        finish();
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photo:
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
                break;
            case R.id.select_photo:
                Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                // 如果朋友们要限制上传到服务器的图片类型时可以直接写如：image/jpeg 、 image/png等的类型
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/png");
                startActivityForResult(pickIntent, 1);//请求码为1
                break;
            default:
                break;
        }
    }

}