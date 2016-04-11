package getAndParse;

import android.os.Parcel;

import com.avos.avoscloud.AVUser;

import java.util.Date;
import java.util.List;


/**
 * Created by lenovo on 2016/3/7.
 */
public class YMUser extends AVUser {
   public  static final Creator<YMUser> CREATOR=new Creator<YMUser>(){
       @Override
       public YMUser createFromParcel(Parcel source) {
           return null;
       }

       @Override
       public YMUser[] newArray(int size) {
           return new YMUser[0];
       }
   };

    public YMUser(String name,String passWord){
           this.setUsername(name);
           this.setPassword(passWord);
    }

    public void setProfileImage(String s){
        this.put("profileImage",s);
    }
    public void setYMuserGender(int gender){
        this.put("gender",gender);
    }
    /*是否重复添加在UI线程中判断，并且输出提示*/
    public void setFavoriteDishes(String dishID){
        List<String> favoriteDishesID=this.getList("favoriteDishes");
        favoriteDishesID.add(dishID);
    }
    /*是否重复添加在UI线程中判断，并且输出提示*/
    public void setLikeDishes(String dishID){
        List<String> favoriteDishesID=this.getList("likeDishes");
        favoriteDishesID.add(dishID);
    }
    public void setirthday(Date date){
        this.put("birthday", date);
    }
    public void setHometown(String hometown){
        this.put("hometown", hometown);
    }

    public void SetSentence(String sentence){
        this.put("sentence", sentence);
    }

    public void SetDisplayName(String displayName){this.put("displayName",displayName);}



}
