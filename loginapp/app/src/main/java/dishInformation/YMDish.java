package dishInformation;

import java.util.Arrays;

/**
 * Created by lenovo on 2016/3/3.
 */
public class YMDish {
    String dishID;
    String name;
    int dishType;/*java中不提供无符号整数*/
    String [] rawStuff;
    int taste;
    int likeNumber;
    String dishSmallImage;
    String dishLargeImage;
    float price;
    YMDishLocation [] ymDishLocations;
    String description;
    int favoriteNumber;


    /*构造函数*/
    public YMDish(String dishID,String name, int dishType,String [] rawStuff, int taste,
                   int likeNumber,String dishSmallImage,String dishLargeImage,float price,
                  YMDishLocation [] ymDishLocations, String description,int favoriteNumber){
        this.dishID=dishID;
        this.name=name;
        this.dishType=dishType;
        this.rawStuff=rawStuff;
        this.taste=taste;
        this.likeNumber=likeNumber;
        this.dishSmallImage=dishSmallImage;
        this.dishLargeImage=dishLargeImage;
        this.price=price;
        this.ymDishLocations=ymDishLocations;
        this.description=description;
        this.favoriteNumber=favoriteNumber;
    }

    @Override
    public String toString() {
        return "YMDish{" +
                "description='" + description + '\'' +
                ", dishID='" + dishID + '\'' +
                ", name='" + name + '\'' +
                ", dishType=" + dishType +
                ", rawStuff=" + Arrays.toString(rawStuff) +
                ", taste=" + taste +
                ", likeNumber=" + likeNumber +
                ", dishSmallImage='" + dishSmallImage + '\'' +
                ", dishLargeImage='" + dishLargeImage + '\'' +
                ", price=" + price +
                ", ymDishLocations=" + Arrays.toString(ymDishLocations) +
                ", favoriteNumber=" + favoriteNumber +
                '}';
    }

    //setter方法
    public void setDishID(String dishID) {
        this.dishID = dishID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDishType(int dishType) {
        this.dishType = dishType;
    }

    public void setRawStuff(String[] rawStuff) {
        this.rawStuff = rawStuff;
    }

    public void setTaste(int taste) {
        this.taste = taste;
    }

    public void setLikeNumber(int likeNumber) {
        this.likeNumber = likeNumber;
    }

    public void setDishSmallImage(String dishSmallImage) {
        this.dishSmallImage = dishSmallImage;
    }

    public void setDishLargeImage(String dishLargeImage) {
        this.dishLargeImage = dishLargeImage;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setYmDishLocations(YMDishLocation[] ymDishLocations) {
        this.ymDishLocations = ymDishLocations;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFavoriteNumber(int favoriteNumber) {
        this.favoriteNumber = favoriteNumber;
    }

    //getter方法
    public String getDishID() {
        return dishID;
    }

    public String getName() {
        return name;
    }

    public int getTaste() {
        return taste;
    }

    public int getDishType() {
        return dishType;
    }

    public String[] getRawStuff() {
        return rawStuff;
    }

    public int getLikeNumber() {
        return likeNumber;
    }

    public String getDishSmallImage() {
        return dishSmallImage;
    }

    public String getDishLargeImage() {
        return dishLargeImage;
    }

    public float getPrice() {
        return price;
    }

    public YMDishLocation[] getYmDishLocations() {
        return ymDishLocations;
    }

    public String getDescription() {
        return description;
    }

    public int getFavoriteNumber() {
        return favoriteNumber;
    }
}
