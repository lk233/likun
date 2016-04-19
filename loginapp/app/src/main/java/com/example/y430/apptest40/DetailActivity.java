package com.example.y430.apptest40;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import image.SmartImageView;

/**
 * Created by admin on 2016/4/14.
 */
public class DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        //获取数据
         String today_image = bundle.getString("image");
        String today_name = bundle.getString("name");
        String today_ingredients = bundle.getString("ingredient");
        String today_location = bundle.getString("location");
        String today_des = bundle.getString("des");
        int today_taste = bundle.getInt("taste");
        int today_like = bundle.getInt("like");
        int today_favorite = bundle.getInt("favorite");

        SmartImageView detail_img = (SmartImageView) findViewById(R.id.detail_image);
        detail_img.setImageUrl(today_image);

        TextView detail_name = (TextView) findViewById(R.id.detail_name);
        detail_name.setText(today_name);

        TextView detail_ingredient = (TextView) findViewById(R.id.detail_material);
        detail_ingredient.setText(today_ingredients);

        TextView detail_location = (TextView) findViewById(R.id.detail_place);
        detail_location.setText(today_location);

        TextView detail_taste = (TextView) findViewById(R.id.detail_taste);
        detail_taste.setText(String.valueOf(today_taste));

        TextView detail_des = (TextView) findViewById(R.id.detail_description);
        detail_des.setText(today_des);

        TextView detail_like = (TextView) findViewById(R.id.detail_loveno);
        detail_like.setText(String.valueOf(today_like));

        TextView detail_favorite = (TextView) findViewById(R.id.detail_collectno);
        detail_favorite.setText(String.valueOf(today_favorite));

        //返回按钮
        ImageView detail_return =(ImageView) findViewById(R.id.detail_return);
        detail_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
