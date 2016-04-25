package com.example.y430.apptest40;

import com.zhy.android.percent.support.PercentRelativeLayout.LayoutParams;

import dishInformation.YMDish;
import getAndParse.GetHotDishes;
import getAndParse.GetTodayDishes;
import image.*;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * http://blog.csdn.net/lmj623565791/article/details/41531475
 * @author zhy
 *
 */
public class DrawerActivity extends FragmentActivity
{

    ViewPager viewPager;   //viewPager组件实现轮播图片的效果
    ArrayList<View> list = new ArrayList<>();   //定义一个放轮播器view的数组

    ViewGroup group;      //小圆点所在的linearlayout布局
    TextView textView;    //作为小圆点的容器
    TextView[] textViews;  //小圆点容器的数组

    List<YMDish> objects_hot;  //YMDish  从后台拉取数据,获取热门菜式
    GetHotDishes hotDishes;//使用hotDishes里的线程

    List<YMDish> objects_today;  //YMDish  从后台拉取数据，获取今日菜式
    GetTodayDishes todayDishes;//使用todayDishes里的线程

    Bitmap []today_image;  //今日菜式的图片数组
    String []today_name;   //今日菜式的名字
    String []today_price;   //今日菜式的价格
    String []today_desc;   //今日菜式的描述介绍
    String []today_addr;   //今日菜式的位置信息
    int    []today_lovedno;   //今日菜式的点赞数*/

    Bitmap []all_image;  //全部菜式的图片数组
    String []all_name;   //全部菜式的名字
    String []all_price;   //全部菜式的价格
    String []all_addr;   //全部菜式的位置信息
    int    []all_lovedno;   //全部菜式的点赞数*/

    //popupWindow对象
    PopupWindow popup_canteen;
    PopupWindow popup_dish;
    PopupWindow popup_taste;
    PopupWindow popup_price;

    List<Map<String,Object>> listItems_all;//用来和all_dish的adapter绑定
    SimpleAdapter simpleAdapter;//all_dish的adapter
    private DrawerLayout mDrawerLayout;
    private TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.drawer);


        initView();
        initEvents();
        //今日推荐和全部菜式
        tabHost = (TabHost)findViewById(R.id.tabhost);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("today_today")
                .setIndicator("今日推荐")
                .setContent(R.id.today_today);
        tabHost.addTab(tab1);
        TabHost.TabSpec tab2 = tabHost.newTabSpec("today_all")
                .setIndicator("全部菜式")
                .setContent(R.id.today_all);
        tabHost.addTab(tab2);
        updateTab(tabHost);//调用updateTab  修改字体格式


        //从后台获取热门菜式的方法
        objects_hot =new ArrayList<YMDish>();
        hotDishes=new GetHotDishes(objects_hot);
        hotDishes.start();
        //加上try这个一段，让主线程等待GetHotDishes这个线程结束。
        try{
            hotDishes.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        ///从后台获取今日菜式的方法
        objects_today =new ArrayList<YMDish>();
        todayDishes=new GetTodayDishes(objects_today);
        todayDishes.start();
        //加上try这个一段，让主线程等待GetTodayDishes这个线程结束。
        try{
            todayDishes.join();
        }catch (Exception e){
            e.printStackTrace();
        }


        init_top_images();  //初始化图片轮播界面
       init_today_dish();  //初始化今日菜式
       init_all_dish();   //初始化全部菜式

        //筛选popWindow的实现
        final View pop_all_canteen = this.getLayoutInflater().inflate(R.layout.all_catin_popwindow,null);
        final View pop_all_dish = this.getLayoutInflater().inflate(R.layout.all_dish_popwindow,null);
        View pop_all_taste = this.getLayoutInflater().inflate(R.layout.all_taste_popwindow,null);
        View pop_all_price = this.getLayoutInflater().inflate(R.layout.all_price_popwindow,null);

        popup_canteen = new PopupWindow(pop_all_canteen,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popup_dish = new PopupWindow(pop_all_dish,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popup_taste = new PopupWindow(pop_all_taste,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popup_price = new PopupWindow(pop_all_price,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //四个筛选标签
        final TextView tv_all_canteen = (TextView)findViewById(R.id.all_catin);
        tv_all_canteen.setText("全部饭堂");
        final TextView tv_all_dish = (TextView)findViewById(R.id.all_dish_type);
        tv_all_dish.setText("所有菜式");
        final TextView tv_all_taste = (TextView)findViewById(R.id.all_other1);
        tv_all_taste.setText("菜式口味");
        final TextView tv_all_price = (TextView)findViewById(R.id.all_other2);
        tv_all_price.setText("菜式价格");

        //四个标签的点击侦听
        tv_all_canteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //是否关闭其他的
                if (popup_dish.isShowing())
                    popup_dish.dismiss();
                else if(popup_taste.isShowing())
                    popup_taste.dismiss();
                else if(popup_price.isShowing())
                    popup_price.dismiss();

                //是否打开自己的
                if (popup_canteen.isShowing()) {
                    popup_canteen.dismiss();
                }
                else
                    popup_canteen.showAsDropDown(v);
            }
        });

        tv_all_dish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //是否关闭其他的
                if (popup_canteen.isShowing())
                    popup_canteen.dismiss();
                else if(popup_taste.isShowing())
                    popup_taste.dismiss();
                else if(popup_price.isShowing())
                    popup_price.dismiss();

                //是否打开自己的
                if (popup_dish.isShowing())
                    popup_dish.dismiss();
                else
                    popup_dish.showAsDropDown(v);
            }
        });

        tv_all_taste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //是否关闭其他的
                if (popup_dish.isShowing())
                    popup_dish.dismiss();
                else if(popup_canteen.isShowing())
                    popup_canteen.dismiss();
                else if(popup_price.isShowing())
                    popup_price.dismiss();

                //是否打开自己的
                if (popup_taste.isShowing())
                    popup_taste.dismiss();
                else
                    popup_taste.showAsDropDown(v);
            }
        });
        tv_all_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //是否关闭其他的
                if (popup_dish.isShowing())
                    popup_dish.dismiss();
                else if(popup_taste.isShowing())
                    popup_taste.dismiss();
                else if(popup_canteen.isShowing())
                    popup_canteen.dismiss();

                //是否打开自己的
                if (popup_price.isShowing())
                    popup_price.dismiss();
                else
                    popup_price.showAsDropDown(v);
            }
        });

        //每个标签里面popupWindow内容的点击事件
        //all_canteen的PopupWindow
        TableRadioGroup select_canteen = (TableRadioGroup)pop_all_canteen.findViewById(R.id.select_catin);
        select_canteen.setOnCheckedChangeListener(new TableRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(TableRadioGroup group, int checkedId) {
                if(checkedId == R.id.item_allcatin) {
                    tv_all_canteen.setText("全部饭堂");
                    popup_canteen.dismiss();
                    //更新listitems_all
                    listItems_all.clear();
                    for (int i = 0; i < objects_today.size(); ++i) {
                        Map<String, Object> listItem_all = new HashMap<String, Object>();
                            listItem_all.put("image_url", all_image[i]);
                            listItem_all.put("all_name", all_name[i]);
                            listItem_all.put("all_price", all_price[i]);
                            listItem_all.put("all_addr", all_addr[i]);
                            listItem_all.put("all_lovedno", all_lovedno[i]);

                            listItems_all.add(listItem_all);
                        }

                    simpleAdapter.notifyDataSetChanged();
                }
                else if(checkedId == R.id.item_1st) {
                    tv_all_canteen.setText("一饭");
                    popup_canteen.dismiss();
                    //更新listitems_all
                    listItems_all.clear();
                    for (int i = 0; i < objects_today.size(); ++i) {
                        Map<String, Object> listItem_all = new HashMap<String, Object>();

                        if (objects_today.get(i).getYmDishLocations()[0].getCanteen().equals("一")) {
                            listItem_all.put("image_url", all_image[i]);
                            listItem_all.put("all_name", all_name[i]);
                            listItem_all.put("all_price", all_price[i]);
                            listItem_all.put("all_addr", all_addr[i]);
                            listItem_all.put("all_lovedno", all_lovedno[i]);

                            listItems_all.add(listItem_all);
                        }
                    }
                    simpleAdapter.notifyDataSetChanged();
                }
                else if(checkedId == R.id.item_2nd) {
                    tv_all_canteen.setText("二饭");
                    popup_canteen.dismiss();
                    //更新listitems_all
                    listItems_all.clear();
                    for (int i = 0; i < objects_today.size(); ++i) {
                        Map<String, Object> listItem_all = new HashMap<String, Object>();

                        if (objects_today.get(i).getYmDishLocations()[0].getCanteen().equals("二")) {
                            listItem_all.put("image_url", all_image[i]);
                            listItem_all.put("all_name", all_name[i]);
                            listItem_all.put("all_price", all_price[i]);
                            listItem_all.put("all_addr", all_addr[i]);
                            listItem_all.put("all_lovedno", all_lovedno[i]);

                            listItems_all.add(listItem_all);
                        }
                    }
                    simpleAdapter.notifyDataSetChanged();
                }

            }
        });


        //all_dish的PopupWindow
        TableRadioGroup select_dish = (TableRadioGroup)pop_all_dish.findViewById(R.id.select_dish);
        select_dish.setOnCheckedChangeListener(new TableRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(TableRadioGroup group, int checkedId) {
                if (checkedId == R.id.item_alldish) {
                    tv_all_dish.setText("所有菜式");
                    popup_dish.dismiss();
                    //更新listitems_all
                    listItems_all.clear();
                    for (int i = 0; i < objects_today.size(); ++i) {
                        Map<String, Object> listItem_all = new HashMap<String, Object>();

                            listItem_all.put("image_url", all_image[i]);
                            listItem_all.put("all_name", all_name[i]);
                            listItem_all.put("all_price", all_price[i]);
                            listItem_all.put("all_addr", all_addr[i]);
                            listItem_all.put("all_lovedno", all_lovedno[i]);

                            listItems_all.add(listItem_all);

                    }
                    simpleAdapter.notifyDataSetChanged();

                }
                else if(checkedId == R.id.item_dessert){
                    tv_all_dish.setText("甜点");
                    popup_dish.dismiss();
                    //更新listitems_all
                    listItems_all.clear();
                    for (int i = 0; i < objects_today.size(); ++i) {
                        Map<String, Object> listItem_all = new HashMap<String, Object>();

                        if (objects_today.get(i).getDishType() == 1) {
                            listItem_all.put("image_url", all_image[i]);
                            listItem_all.put("all_name", all_name[i]);
                            listItem_all.put("all_price", all_price[i]);
                            listItem_all.put("all_addr", all_addr[i]);
                            listItem_all.put("all_lovedno", all_lovedno[i]);

                            listItems_all.add(listItem_all);
                        }
                    }
                    simpleAdapter.notifyDataSetChanged();
                }
                else if(checkedId == R.id.item_cake){
                    tv_all_dish.setText("糕点");
                    popup_dish.dismiss();
                    //更新listitems_all
                    listItems_all.clear();
                    for (int i = 0; i < objects_today.size(); ++i) {
                        Map<String, Object> listItem_all = new HashMap<String, Object>();

                        if (objects_today.get(i).getDishType() == 2) {
                            listItem_all.put("image_url", all_image[i]);
                            listItem_all.put("all_name", all_name[i]);
                            listItem_all.put("all_price", all_price[i]);
                            listItem_all.put("all_addr", all_addr[i]);
                            listItem_all.put("all_lovedno", all_lovedno[i]);

                            listItems_all.add(listItem_all);
                        }
                    }
                    simpleAdapter.notifyDataSetChanged();
                }
                else if(checkedId == R.id.item_soup){
                    tv_all_dish.setText("汤");
                    popup_dish.dismiss();
                    //更新listitems_all
                    listItems_all.clear();
                    for (int i = 0; i < objects_today.size(); ++i) {
                        Map<String, Object> listItem_all = new HashMap<String, Object>();

                        if (objects_today.get(i).getDishType() == 3) {
                            listItem_all.put("image_url", all_image[i]);
                            listItem_all.put("all_name", all_name[i]);
                            listItem_all.put("all_price", all_price[i]);
                            listItem_all.put("all_addr", all_addr[i]);
                            listItem_all.put("all_lovedno", all_lovedno[i]);

                            listItems_all.add(listItem_all);
                        }
                    }
                    simpleAdapter.notifyDataSetChanged();
                }
                else if(checkedId == R.id.item_set){
                    tv_all_dish.setText("套餐");
                    popup_dish.dismiss();
                    //更新listitems_all
                    listItems_all.clear();
                    for (int i = 0; i < objects_today.size(); ++i) {
                        Map<String, Object> listItem_all = new HashMap<String, Object>();

                        if (objects_today.get(i).getDishType() == 4) {
                            listItem_all.put("image_url", all_image[i]);
                            listItem_all.put("all_name", all_name[i]);
                            listItem_all.put("all_price", all_price[i]);
                            listItem_all.put("all_addr", all_addr[i]);
                            listItem_all.put("all_lovedno", all_lovedno[i]);

                            listItems_all.add(listItem_all);
                        }
                    }
                    simpleAdapter.notifyDataSetChanged();
                }
                else if(checkedId == R.id.item_normal){
                    tv_all_dish.setText("普通菜式");
                    popup_dish.dismiss();
                    //更新listitems_all
                    listItems_all.clear();
                    for (int i = 0; i < objects_today.size(); ++i) {
                        Map<String, Object> listItem_all = new HashMap<String, Object>();

                        if (objects_today.get(i).getDishType() == 5) {
                            listItem_all.put("image_url", all_image[i]);
                            listItem_all.put("all_name", all_name[i]);
                            listItem_all.put("all_price", all_price[i]);
                            listItem_all.put("all_addr", all_addr[i]);
                            listItem_all.put("all_lovedno", all_lovedno[i]);

                            listItems_all.add(listItem_all);
                        }
                    }
                    simpleAdapter.notifyDataSetChanged();
                }
            }
        });


        //all_taste的PopupWindow
        TableRadioGroup select_taste = (TableRadioGroup)pop_all_taste.findViewById(R.id.select_taste);

        select_taste.setOnCheckedChangeListener(new TableRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(TableRadioGroup group, int checkedId) {
                if (checkedId == R.id.item_alltaste) {
                    tv_all_taste.setText("所有口味");
                    popup_taste.dismiss();
                    //更新listitems_all
                    listItems_all.clear();
                    for (int i = 0; i < objects_today.size(); ++i) {
                        Map<String, Object> listItem_all = new HashMap<String, Object>();

                            listItem_all.put("image_url", all_image[i]);
                            listItem_all.put("all_name", all_name[i]);
                            listItem_all.put("all_price", all_price[i]);
                            listItem_all.put("all_addr", all_addr[i]);
                            listItem_all.put("all_lovedno", all_lovedno[i]);

                            listItems_all.add(listItem_all);

                    }
                    simpleAdapter.notifyDataSetChanged();
                }
                else if(checkedId == R.id.item_sweet){
                    tv_all_taste.setText("甜");
                    popup_taste.dismiss();
                    //更新listitems_all
                    listItems_all.clear();
                    for (int i = 0; i < objects_today.size(); ++i) {
                        Map<String, Object> listItem_all = new HashMap<String, Object>();

                        if (objects_today.get(i).getTaste() == 1) {
                            listItem_all.put("image_url", all_image[i]);
                            listItem_all.put("all_name", all_name[i]);
                            listItem_all.put("all_price", all_price[i]);
                            listItem_all.put("all_addr", all_addr[i]);
                            listItem_all.put("all_lovedno", all_lovedno[i]);

                            listItems_all.add(listItem_all);
                        }
                    }
                    simpleAdapter.notifyDataSetChanged();
                }
                else if(checkedId == R.id.item_peppery){
                    tv_all_taste.setText("辣");
                    popup_taste.dismiss();
                    //更新listitems_all
                    listItems_all.clear();
                    for (int i = 0; i < objects_today.size(); ++i) {
                        Map<String, Object> listItem_all = new HashMap<String, Object>();

                        if (objects_today.get(i).getTaste() == 2) {
                            listItem_all.put("image_url", all_image[i]);
                            listItem_all.put("all_name", all_name[i]);
                            listItem_all.put("all_price", all_price[i]);
                            listItem_all.put("all_addr", all_addr[i]);
                            listItem_all.put("all_lovedno", all_lovedno[i]);

                            listItems_all.add(listItem_all);
                        }
                    }
                    simpleAdapter.notifyDataSetChanged();
                }
                else if(checkedId == R.id.item_sour){
                    tv_all_taste.setText("酸");
                    popup_taste.dismiss();
                    //更新listitems_all
                    listItems_all.clear();
                    for (int i = 0; i < objects_today.size(); ++i) {
                        Map<String, Object> listItem_all = new HashMap<String, Object>();

                        if (objects_today.get(i).getTaste() == 3) {
                            listItem_all.put("image_url", all_image[i]);
                            listItem_all.put("all_name", all_name[i]);
                            listItem_all.put("all_price", all_price[i]);
                            listItem_all.put("all_addr", all_addr[i]);
                            listItem_all.put("all_lovedno", all_lovedno[i]);

                            listItems_all.add(listItem_all);
                        }
                    }
                    simpleAdapter.notifyDataSetChanged();
                }
                else if(checkedId == R.id.item_salty){
                    tv_all_taste.setText("咸");
                    popup_taste.dismiss();
                    //更新listitems_all
                    listItems_all.clear();
                    for (int i = 0; i < objects_today.size(); ++i) {
                        Map<String, Object> listItem_all = new HashMap<String, Object>();

                        if (objects_today.get(i).getTaste() == 4) {
                            listItem_all.put("image_url", all_image[i]);
                            listItem_all.put("all_name", all_name[i]);
                            listItem_all.put("all_price", all_price[i]);
                            listItem_all.put("all_addr", all_addr[i]);
                            listItem_all.put("all_lovedno", all_lovedno[i]);

                            listItems_all.add(listItem_all);
                        }
                    }
                    simpleAdapter.notifyDataSetChanged();
                }

            }
        });

        //all_price的PopupWindow
        TableRadioGroup select_price = (TableRadioGroup)pop_all_price.findViewById(R.id.select_price);

        select_price.setOnCheckedChangeListener(new TableRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(TableRadioGroup group, int checkedId) {
                if (checkedId == R.id.item_allprice) {
                    tv_all_price.setText("所有价格");
                    popup_price.dismiss();
                    //更新listitems_all
                    listItems_all.clear();
                    for (int i = 0; i < objects_today.size(); ++i) {
                        Map<String, Object> listItem_all = new HashMap<String, Object>();

                            listItem_all.put("image_url", all_image[i]);
                            listItem_all.put("all_name", all_name[i]);
                            listItem_all.put("all_price", all_price[i]);
                            listItem_all.put("all_addr", all_addr[i]);
                            listItem_all.put("all_lovedno", all_lovedno[i]);

                            listItems_all.add(listItem_all);

                    }
                    simpleAdapter.notifyDataSetChanged();
                }
                else if (checkedId == R.id.item_0to3) {
                    tv_all_price.setText("0~3元");
                    popup_price.dismiss();
                    //更新listitems_all
                    listItems_all.clear();
                    for (int i = 0; i < objects_today.size(); ++i) {
                        Map<String, Object> listItem_all = new HashMap<String, Object>();

                        if (objects_today.get(i).getPrice()>0&&objects_today.get(i).getPrice()<3) {
                            listItem_all.put("image_url", all_image[i]);
                            listItem_all.put("all_name", all_name[i]);
                            listItem_all.put("all_price", all_price[i]);
                            listItem_all.put("all_addr", all_addr[i]);
                            listItem_all.put("all_lovedno", all_lovedno[i]);

                            listItems_all.add(listItem_all);
                        }
                    }
                    simpleAdapter.notifyDataSetChanged();
                }
                else if (checkedId == R.id.item_3to5) {
                    tv_all_price.setText("3~5元");
                    popup_price.dismiss();
                    //更新listitems_all
                    listItems_all.clear();
                    for (int i = 0; i < objects_today.size(); ++i) {
                        Map<String, Object> listItem_all = new HashMap<String, Object>();

                        if (objects_today.get(i).getPrice()>3&&objects_today.get(i).getPrice()<5) {
                            listItem_all.put("image_url", all_image[i]);
                            listItem_all.put("all_name", all_name[i]);
                            listItem_all.put("all_price", all_price[i]);
                            listItem_all.put("all_addr", all_addr[i]);
                            listItem_all.put("all_lovedno", all_lovedno[i]);

                            listItems_all.add(listItem_all);
                        }
                    }
                    simpleAdapter.notifyDataSetChanged();
                }
                else if (checkedId == R.id.item_5to10) {
                    tv_all_price.setText("5~10元");
                    popup_price.dismiss();
                    //更新listitems_all
                    listItems_all.clear();
                    for (int i = 0; i < objects_today.size(); ++i) {
                        Map<String, Object> listItem_all = new HashMap<String, Object>();

                        if (objects_today.get(i).getPrice()>5&&objects_today.get(i).getPrice()<10) {
                            listItem_all.put("image_url", all_image[i]);
                            listItem_all.put("all_name", all_name[i]);
                            listItem_all.put("all_price", all_price[i]);
                            listItem_all.put("all_addr", all_addr[i]);
                            listItem_all.put("all_lovedno", all_lovedno[i]);

                            listItems_all.add(listItem_all);
                        }
                    }
                    simpleAdapter.notifyDataSetChanged();
                }
                else if (checkedId == R.id.item_above10) {
                    tv_all_price.setText("10元以上");
                    popup_price.dismiss();
                    //更新listitems_all
                    listItems_all.clear();
                    for (int i = 0; i < objects_today.size(); ++i) {
                        Map<String, Object> listItem_all = new HashMap<String, Object>();

                        if (objects_today.get(i).getPrice()>10) {
                            listItem_all.put("image_url", all_image[i]);
                            listItem_all.put("all_name", all_name[i]);
                            listItem_all.put("all_price", all_price[i]);
                            listItem_all.put("all_addr", all_addr[i]);
                            listItem_all.put("all_lovedno", all_lovedno[i]);

                            listItems_all.add(listItem_all);
                        }
                    }
                    simpleAdapter.notifyDataSetChanged();
                }

            }
        });
        //tableHost单项的点击事件
        tabHost.getTabWidget().getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭popupwindow
                if (popup_dish.isShowing())
                    popup_dish.dismiss();
                else if(popup_taste.isShowing())
                    popup_taste.dismiss();
                else if(popup_price.isShowing())
                    popup_price.dismiss();
                else if(popup_canteen.isShowing())
                    popup_canteen.dismiss();

                tabHost.setCurrentTab(0);
            }
        });
    }



    //标签格式
    private void updateTab(TabHost tabHost){
        for(int i=0;i<tabHost.getTabWidget().getChildCount();++i) {
            tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.clr_green);
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.WHITE);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(16);
            Drawable tab_draw=getResources().getDrawable(R.drawable.tab_indicator_selector);
            tab_draw.setBounds(0, 0, tab_draw.getMinimumWidth(), tab_draw.getMinimumHeight());
            tv.setCompoundDrawablesWithIntrinsicBounds(null,null,null,tab_draw);
            tv.setCompoundDrawablePadding(-10);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            tv.setLayoutParams(params);
            final TabWidget tabWidget=tabHost.getTabWidget();
            tabWidget.setDividerDrawable(null);
        }
    }
    //设置按钮的事件监听器
    public void OpenLeftDrawer(View view){
        mDrawerLayout.openDrawer(Gravity.LEFT);
        //解锁手势控制
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                Gravity.LEFT);
    }

    //抽屉滑动效果
            private void initEvents() {
                mDrawerLayout.setDrawerListener(new DrawerListener() {
                    @Override
                    public void onDrawerStateChanged(int newState) {
                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        View mContent = mDrawerLayout.getChildAt(0);
                        View mMenu = drawerView;
                        float scale = 1 - slideOffset;
                        if (drawerView.getTag().equals("LEFT")) {
                            ViewHelper.setTranslationX(mContent,
                                    mMenu.getMeasuredWidth() * (1 - scale));
                            mContent.invalidate();
                        }
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        mDrawerLayout.setDrawerLockMode(
                                DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
                    }
                });
    }
    //初始化Drawlayout
    private void initView()
    {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawer);
        //设置手势控制为锁
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                Gravity.LEFT);
    }

    private void init_top_images()
    {
        //为每个今日菜式设置轮播的内容
        for(int i=0;i< objects_hot.size();++i) {
            //将view_pages布局转化成对象
            LayoutInflater inflater=getLayoutInflater();
            View hot_dish = inflater.inflate(R.layout.view_pages, null);
            //获取人气菜式的信息放在detailActivity里面
            final String today_hot_name = objects_hot.get(i).getName();
            //原料数组借助一个临时变量
            String today_hot_ingredient = objects_hot.get(i).getRawStuff()[0];
            for(int j = 1;j< objects_hot.get(i).getRawStuff().length;++j){
                today_hot_ingredient = today_hot_ingredient + "," + objects_hot.get(i).getRawStuff()[j];
            }
            final String today_hot_material = today_hot_ingredient;
            final String today_hot_location = objects_hot.get(i).getYmDishLocations()[0].toString();
            //taste的get方法需要修改
            final int today_hot_taste = objects_hot.get(i).getTaste();
            final String today_hot_des = objects_hot.get(i).getDescription();
            final int today_hot_like = objects_hot.get(i).getLikeNumber();
            final int today_hot_favorite = objects_hot.get(i).getFavoriteNumber();


            //为今日菜式的图片设置其url,并从网络上下载
            final String image1_url = objects_hot.get(i).getDishLargeImage();
            SmartImageView imageView = (SmartImageView)hot_dish.findViewById(R.id.hot_dish);
            imageView.setImageUrl(image1_url);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(DrawerActivity.this, DetailActivity.class);
                    Bundle bundle = new Bundle();
                    //在Bundle里面放入数据
                    bundle.putString("image",image1_url);
                    bundle.putString("name",today_hot_name);
                    bundle.putString("ingredient",today_hot_material);
                    bundle.putString("location",today_hot_location);
                    bundle.putString("des",today_hot_des);
                    bundle.putInt("taste", today_hot_taste);
                    bundle.putInt("like", today_hot_like);
                    bundle.putInt("favorite", today_hot_favorite);
                    intent.putExtras(bundle);

                    startActivity(intent);
                }
            });

           //设置今日菜式的文字
           TextView text_name = (TextView)hot_dish.findViewById(R.id.hotdish_name);
           text_name.setText("—" + objects_hot.get(i).getName() + "—");

          // 设置今日菜式的位置
           TextView text_location = (TextView)hot_dish.findViewById(R.id.hotdish_location);
           text_location.setText(objects_hot.get(i).getYmDishLocations()[0].getCanteen() + "饭" +
                   objects_hot.get(i).getYmDishLocations()[0].getFloor() + "楼" +
                   objects_hot.get(i).getYmDishLocations()[0].getWindow() + "号窗口");

           list.add(hot_dish);
       }
        //设置小圆点的textview数组
        textViews=new TextView[list.size()];
        //获取group对象(装有所有小圆点)
        group=(ViewGroup)findViewById(R.id.viewGroup);
        //获取viewPager对象
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        //给Textviews设置小圆点背景
        for (int i = 0; i < list.size(); i++)
        {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(15,15);
            lp.setMargins(10,0,10,0);
            textView=new TextView(DrawerActivity.this);
            textView.setLayoutParams(lp);
            textView.setPadding(0, 0, 2, 0);
            textViews[i]=textView;
            if (i==0)
            {

                textViews[i].setBackgroundResource(R.drawable.radio_sel);
            }else {
                textViews[i].setBackgroundResource(R.drawable.radio);
            }
            //将所有textview加入到group中
            group.addView(textViews[i]);

        }

          //设置viewPager的适配器
        viewPager.setAdapter(new MyAdapter());
        //设置viewPager的监听器
        viewPager.setOnPageChangeListener(new MyListener());
    }

    private void init_today_dish(){
        //给菜式信息的数组设置大小
        today_image = new Bitmap[objects_today.size()];
        today_name = new String[objects_today.size()];
        today_price = new String [objects_today.size()];
        today_desc = new String[objects_today.size()];
        today_addr = new String[objects_today.size()];
        today_lovedno = new int[objects_today.size()];


        //给动态数组赋值，使得这些数组能够存放数据
     for(int i =0;i< objects_today.size();++i){

         SmartImageView img = new SmartImageView(this);
          today_image[i] = img.setImageUrl(objects_today.get(i).getDishSmallImage());

          today_name[i] = objects_today.get(i).getName();
          today_price[i] = "￥" + objects_today.get(i).getPrice();
          today_desc[i] = objects_today.get(i).getDescription();
          today_addr[i] = objects_today.get(i).getYmDishLocations()[0].toString();
          today_lovedno[i] = objects_today.get(i).getLikeNumber();
      }

        //创建一个list集合，集合的元素是Map
        List<Map<String,Object>> listItems = new ArrayList<Map<String, Object>>();

        for(int i =0;i< objects_today .size();++i){
            Map<String,Object> listItem =new HashMap<String,Object>();
            listItem.put("image_url",today_image[i]);
            listItem.put("today_name",today_name[i]);
            listItem.put("today_price",today_price[i]);
            listItem.put("today_desc",today_desc[i]);
            listItem.put("today_addr",today_addr[i]);
            listItem.put("today_lovedno",today_lovedno[i]);

            listItems.add(listItem);
        }

        //创建一个SimpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(DrawerActivity.this,listItems,
                R.layout.today_listmode,
                new  String[]{"image_url","today_name","today_price","today_desc","today_addr","today_lovedno"},
                new int[]{R.id.today_img,R.id.today_name,R.id.today_price,R.id.today_desc,R.id.today_addr,R.id.today_lovedno});


        final ListView list = (ListView) findViewById(R.id.today_list);
        //为ListView设置Adapter


        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                //判断是否是我们要处理的对象
                if ((view instanceof ImageView )&&( data instanceof Bitmap)) {
                    ImageView s = (ImageView) view;
                    s.setImageBitmap((Bitmap) data);
                    return true;
                } else
                    return false;
            }
        });
        list.setAdapter(simpleAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(DrawerActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                //在Bundle里面放入数据
                bundle.putString("image", objects_today.get(position).getDishLargeImage());
                bundle.putString("name", objects_today.get(position).getName());

                //原料数组借助一个临时变量
                String today_ingredient = objects_today.get(position).getRawStuff()[0];
                for (int j = 1; j < objects_today.get(position).getRawStuff().length; ++j) {
                    today_ingredient = today_ingredient + "," + objects_today.get(position).getRawStuff()[j];
                }
                String today_material = today_ingredient;

                bundle.putString("ingredient", today_material);
                bundle.putString("location", objects_today.get(position).getYmDishLocations()[0].toString());
                bundle.putString("des", objects_today.get(position).getDescription());
                bundle.putInt("taste", objects_today.get(position).getTaste());
                bundle.putInt("like", objects_today.get(position).getLikeNumber());
                bundle.putInt("favorite", objects_today.get(position).getFavoriteNumber());
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }

    private void init_all_dish(){
        //给菜式信息的数组设置大小
        all_image = new Bitmap[objects_today.size()];
        all_name = new String[objects_today.size()];
        all_price = new String [objects_today.size()];
        all_addr = new String[objects_today.size()];
        all_lovedno = new int[objects_today.size()];


        //给动态数组赋值，使得这些数组能够存放数据
        for(int i =0;i< objects_today.size();++i){

            SmartImageView img = new SmartImageView(this);
            all_image[i] = img.setImageUrl(objects_today.get(i).getDishSmallImage());

            all_name[i] = objects_today.get(i).getName();
            all_price[i] = "￥" + objects_today.get(i).getPrice();
            all_addr[i] = objects_today.get(i).getYmDishLocations()[0].toString();
            all_lovedno[i] = objects_today.get(i).getLikeNumber();
        }

        //创建一个list集合，集合的元素是Map
         listItems_all = new ArrayList<Map<String, Object>>();

        for(int i =0;i< objects_today.size();++i){
            Map<String,Object> listItem_all =new HashMap<String,Object>();
            listItem_all.put("image_url",all_image[i]);
            listItem_all.put("all_name",all_name[i]);
            listItem_all.put("all_price",all_price[i]);
            listItem_all.put("all_addr",all_addr[i]);
            listItem_all.put("all_lovedno",all_lovedno[i]);

            listItems_all.add(listItem_all);
        }

        //创建一个SimpleAdapter
        simpleAdapter = new SimpleAdapter(DrawerActivity.this,listItems_all,
                R.layout.all_listmode,
                new  String[]{"image_url","all_name","all_price","all_addr","all_lovedno"},
                new int[]{R.id.all_img,R.id.all_name,R.id.all_price,R.id.all_addr,R.id.all_lovedno});


        final ListView list_all = (ListView) findViewById(R.id.all_dishlist);
        //为ListView设置Adapter


        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                //判断是否是我们要处理的对象
                if ((view instanceof ImageView )&&( data instanceof Bitmap)) {
                    ImageView s = (ImageView) view;
                    s.setImageBitmap((Bitmap) data);
                    return true;
                } else
                    return false;
            }
        });
        list_all.setAdapter(simpleAdapter);
        list_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //是否关闭其他的popupWindow
                if (popup_dish.isShowing())
                    popup_dish.dismiss();
                else if(popup_taste.isShowing())
                    popup_taste.dismiss();
                else if(popup_price.isShowing())
                    popup_price.dismiss();
                else if(popup_canteen.isShowing())
                    popup_canteen.dismiss();


                //跳转至Detail的Activity
                Intent intent = new Intent();
                intent.setClass(DrawerActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                //在Bundle里面放入数据
                bundle.putString("image", objects_today.get(position).getDishLargeImage());
                bundle.putString("name", objects_today.get(position).getName());

                //原料数组借助一个临时变量
                String today_ingredient = objects_today.get(position).getRawStuff()[0];
                for (int j = 1; j < objects_today.get(position).getRawStuff().length; ++j) {
                    today_ingredient = today_ingredient + "," + objects_today.get(position).getRawStuff()[j];
                }
                String today_material = today_ingredient;

                bundle.putString("ingredient", today_material);
                bundle.putString("location", objects_today.get(position).getYmDishLocations()[0].toString());
                bundle.putString("des", objects_today.get(position).getDescription());
                bundle.putInt("taste", objects_today.get(position).getTaste());
                bundle.putInt("like", objects_today.get(position).getLikeNumber());
                bundle.putInt("favorite", objects_today.get(position).getFavoriteNumber());
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

    }

    //viewPager的adapter
    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount()
        {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(
                View arg0, Object arg1)
        {
            return arg0==arg1;
        }


         @Override
        public void destroyItem(
                ViewGroup container,
                int position, Object object)
        {
            ((ViewPager)container).removeView(list.get(position));

        }


        @Override
        public void finishUpdate(
                ViewGroup container)
        {
            // TODO Auto-generated method stub
            super.finishUpdate(container);
        }


        @Override
        public int getItemPosition(Object object)
        {
            // TODO Auto-generated method stub
            return super.getItemPosition(object);
        }


        @Override
        public CharSequence getPageTitle(
                int position)
        {
            // TODO Auto-generated method stub
            return super.getPageTitle(position);
        }


        @Override
        public Object instantiateItem(
                ViewGroup container, int position)
        {
            ((ViewPager)container).addView(list.get(position));
            return list.get(position);
        }
    }
    //viewPager的监听器
    class MyListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(
                int arg0)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0,
                                   float arg1, int arg2)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0)
        {
            for(int i=0;i<textViews.length;i++) {
                textViews[arg0].setBackgroundResource(R.drawable.radio_sel);
                if (arg0!=i)
                {
                    textViews[i].setBackgroundResource(R.drawable.radio);
                }
            }

        }

    }

}

