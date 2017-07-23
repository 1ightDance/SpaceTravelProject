/**
 * Created by LightDance on 2017/7/4.
 * 用于求变化率
 */
public class Derivative {
    public static double numDervative(double numLastTime,double numNow,double littleTime){
        return (numNow-numLastTime)/littleTime;
    }
}
