package getAndParse;

/**
 * Created by lenovo on 2016/3/7.
 */



import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;

import java.util.ArrayList;
import java.util.List;

import dishInformation.YMDish;
import dishInformation.YMDishLocation;


public class GetHotDishes extends Thread {
    List<AVObject> allDishes;
    List<YMDish> rDishes;


    public GetHotDishes(List<YMDish>rDishes){
        this.rDishes=rDishes;
        this.allDishes=new ArrayList<AVObject>();
    }


   public void run(){
        //获取TodyDish的项目表
        AVObject table;
        List<String> dishIDs = new ArrayList<String>();
        AVQuery<AVObject> query = new AVQuery<AVObject>("YMHotDish");
        try {
            table = query.get("56d56a82df0eea0051f5f900");
            System.out.println("获取包含今日菜式id数组的AVObject对象成功");
            //获取今日菜式的ID
            dishIDs = table.getList("idRepo");
            System.out.println(dishIDs.size());
        } catch (AVException e1) {
            e1.printStackTrace();
            System.out.println("获取包含今日菜式id数组的AVObject对象失败");
        }
        AVQuery<AVObject> query1 = new AVQuery<AVObject>("YMDish");
        try {
            for (int i = 0; i < dishIDs.size(); i++) {
                //System.out.println(query1.get(dishID.get(i)).toString());
                allDishes.add(query1.get(dishIDs.get(i)));
            }
        } catch (AVException e2) {
            System.out.println("收藏菜式不成功");
        }

       for (int i = 0; i < allDishes.size(); i++) {
           AVObject dish = allDishes.get(i);
           String dishID = dish.getString("dishID");
           String name = dish.getString("name");
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


