package dishInformation;

/**
 * Created by lenovo on 2016/3/3.
 */
public class YMDishLocation {
   private String canteen;
   private String floor;
   private String window;

    public String getCanteen() {

        if(canteen.equals("1"))
            return "一";
        else if(canteen.equals("2"))
            return "二";

        return null;
    }

    public void setCanteen(String canteen) {
        this.canteen = canteen;
    }

    public String getFloor() {

        if(floor.equals("1"))
            return "一";
        else if(floor.equals("2"))
            return "二";

        return null;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getWindow() {
        return window;
    }

    public void setWindow(String window) {
        this.window = window;
    }

    public YMDishLocation(String canteen,String floor,String window) {
        this.canteen = canteen;
        this.floor=floor;
        this.window=window;
    }

    @Override
    public String toString() {
        return canteen + '饭' +
                floor + '楼' +
                 window + "号窗口";
    }
}
