package getAndParse;

/**
 * Created by lenovo on 2016/3/7.
 */


import android.os.Handler;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;


import java.util.ArrayList;
import java.util.List;

import dishInformation.YMDish;
import dishInformation.YMDishLocation;


public class GetTodayDishes extends Thread {
    /*临时储存带有菜式ID的Avobject对象*/
    List<AVObject> allDishes;
    /*临时储存所要使用的今日菜式的对象*/
    List<YMDish> rDishes;

    /*构造函数param:从外部传进来的List<YMDish>类型的对象的引用*/
    public GetTodayDishes(List<YMDish>rDishes){
        this.rDishes=rDishes;
        this.allDishes=new ArrayList<AVObject>();
    }

   /*调用对象的execute()函数时，该函数被自动调动，用来在后台拿数据*/
    @Override
    public void run() {
        /*获取TodyDish的项目表*/
        AVObject table;
        /*获取Avobject对象ID的string list*/
        List<String> dishIDs = new ArrayList<String>();
        AVQuery<AVObject> query = new AVQuery<AVObject>("YMTodayDish");
        try {
            table = query.get("56d56a4e1ea493005c1d136c");
            System.out.println("获取包含今日菜式id数组的AVObject对象成功");
            //获取今日菜式的ID
            dishIDs = table.getList("idRepo");
            System.out.println(dishIDs.size());
        } catch (AVException e1) {
            System.out.println("获取包含今日菜式id数组的AVObject对象失败");
        }
        AVQuery<AVObject> query1 = new AVQuery<AVObject>("YMDish");
        try {
            for (int i = 0; i < dishIDs.size(); i++) {
                /*根据前面的菜式对象ID，获取存有菜式信息的avobject对象*/
                allDishes.add(query1.get(dishIDs.get(i)));
            }
        } catch (AVException e2) {
            System.out.println("收藏菜式不成功");
        }

        for (int i = 0; i < allDishes.size(); i++) {
            AVObject dish = allDishes.get(i);
            String dishID = dish.getString("disheID");
            String name= dish.getString("name");
            int dishType = dish.getInt("dishType");
            List<String> listRawStuff =dish.getList("rawStuff");
            String [] rawStuff=new String[listRawStuff.size()];
            for(int j=0;j<listRawStuff.size();j++){
                rawStuff[j]=listRawStuff.get(j);
            }
            int taste = dish.getInt("taste");
            int likeNumber = dish.getInt("likeNumber");
            String dishSmallImage = dish.getString("dishSmallImage");
            String dishLargeImage = dish.getString("dishLargeImage");
            float price = (float) dish.getDouble("price");
            List<String> locations = dish.getList("locations");//获得String数组
            YMDishLocation[] ymDishLocations=new YMDishLocation[locations.size()];//分离出的YMDishLocation
            for(int k=0;k<locations.size();k++){
                String s=locations.get(k);//获得其中位置
                YMDishLocation location=new YMDishLocation(s.substring(0,1),s.substring(1,2),s.substring(2,s.length()));
                ymDishLocations[k]=location;
            }
            String description = dish.getString("dishDescription");
            int favoriteNumber = dish.getInt("favoriteNumber");
            YMDish rDishe = new YMDish(dishID, name, dishType, rawStuff, taste, likeNumber, dishSmallImage, dishLargeImage,
                    price, ymDishLocations, description, favoriteNumber);
            rDishes.add(rDishe);
        }
    }

}


