package com.example.y430.apptest40;

import com.zhy.android.percent.support.PercentRelativeLayout.LayoutParams;

import dishInformation.YMDish;
import dishInformation.YMDishLocation;
import getAndParse.GetHotDishes;
import getAndParse.GetTodayDishes;
import image.*;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import com.zhy.android.percent.support.PercentLinearLayout;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import com.nineoldandroids.view.ViewHelper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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

    List<YMDish> objects;  //YMDish  从后台拉取数据
    GetHotDishes hotDishes;//使用hotDishes里的线程

    Bitmap []today_hot_image;  //今日菜式的图片数组
    String []today_hot_name;   //今日菜式的名字
    String []today_hot_price;   //今日菜式的价格
    String []today_hot_desc;   //今日菜式的描述介绍
    String []today_hot_addr;   //今日菜式的位置信息
    int    [] today_hot_lovedno;   //今日菜式的点赞数*/

    private DrawerLayout mDrawerLayout;
    private TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.drawer);

        //从后台获取菜式的方法
        objects=new ArrayList<YMDish>();
        hotDishes=new GetHotDishes(objects);
        hotDishes.start();
        //加上try这个一段，让主线程等待GetTodayDishes这个线程结束。
        try{
            hotDishes.join();
        }catch (Exception e){
            e.printStackTrace();
        }


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



      init_top_images();  //初始化图片轮播界面
       init_today_dish();  //初始化今日菜式

    }
    //标签格式
    private void updateTab(TabHost tabHost){
        for(int i=0;i<tabHost.getTabWidget().getChildCount();++i) {
            tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_selector);
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundResource(R.drawable.clr_green);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(16);
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
      for(int i=0;i<objects.size();++i) {
           //将view_pages布局转化成对象
        LayoutInflater inflater=getLayoutInflater();
         View hot_dish = inflater.inflate(R.layout.view_pages, null);
           //为今日菜式的图片设置其url,并从网络上下载
           String image1_url = objects.get(i).getDishLargeImage();
           SmartImageView imageView = (SmartImageView)hot_dish.findViewById(R.id.hot_dish);
           imageView.setImageUrl(image1_url);


           //设置今日菜式的文字
           TextView text_name = (TextView)hot_dish.findViewById(R.id.hotdish_name);
           text_name.setText("—" + objects.get(i).getName() + "—");

          // 设置今日菜式的位置
           TextView text_location = (TextView)hot_dish.findViewById(R.id.hotdish_location);
           text_location.setText(objects.get(i).getYmDishLocations()[0].getCanteen() + "饭" +
                   objects.get(i).getYmDishLocations()[0].getFloor() + "楼" +
                   objects.get(i).getYmDishLocations()[0].getWindow() + "号窗口");

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
        today_hot_image = new Bitmap[objects.size()];
        today_hot_name = new String[objects.size()];
        today_hot_price = new String [objects.size()];
        today_hot_desc = new String[objects.size()];
        today_hot_addr = new String[objects.size()];
        today_hot_lovedno = new int [objects.size()];

        //给动态数组赋值，使得这些数组能够存放数据
     for(int i =0;i<objects.size();++i){
         // SmartImageView img = (SmartImageView)findViewById(R.id.today_hot_img);
          Resources r = getResources();
         Bitmap bmp=BitmapFactory.decodeResource(r, R.drawable.login_icon_face);
         String path = "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2546610023,3120506294&fm=111&gp=0.jpg";
         SmartImageView img = new SmartImageView(this);

          today_hot_image[i] = img.setImageUrl(objects.get(i).getDishSmallImage());
         System.out.println(objects.get(i).getDishSmallImage());

          today_hot_name[i] = objects.get(i).getName();
          today_hot_price[i] = "￥" + objects.get(i).getPrice();
          today_hot_desc[i] = objects.get(i).getDescription();
          today_hot_addr[i] = objects.get(i).getYmDishLocations()[0].getCanteen() + "饭" +
                  objects.get(i).getYmDishLocations()[0].getFloor() + "楼" +
                  objects.get(i).getYmDishLocations()[0].getWindow() + "号窗口";
          today_hot_lovedno[i] = objects.get(i).getLikeNumber();
      }

        //创建一个list集合，集合的元素是Map
        List<Map<String,Object>> listItems = new ArrayList<Map<String, Object>>();

        for(int i =0;i<objects.size();++i){
            Map<String,Object> listItem =new HashMap<String,Object>();
            listItem.put("image_url",today_hot_image[i]);
            listItem.put("today_hot_name",today_hot_name[i]);
            listItem.put("today_hot_price",today_hot_price[i]);
            listItem.put("today_hot_desc",today_hot_desc[i]);
            listItem.put("today_hot_addr",today_hot_addr[i]);
            listItem.put("today_hot_lovedno",today_hot_lovedno[i]);


            listItems.add(listItem);
        }

        //创建一个SimpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(DrawerActivity.this,listItems,
                R.layout.today_listmode,
                new  String[]{"image_url","today_hot_name","today_hot_price","today_hot_desc","today_hot_addr","today_hot_lovedno"},
                new int[]{R.id.today_hot_img,R.id.today_hot_name,R.id.today_hot_price,R.id.today_hot_desc,R.id.today_hot_addr,R.id.today_hot_lovedno});


        ListView list = (ListView) findViewById(R.id.today_hotlist);
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

