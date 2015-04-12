package challenge.scanforest.utils;

/**
 * Created by gerardo on 4/12/15.
 */
public class TypeConverter {
    public static int toInt(String s,int def){
        try{
            return Integer.parseInt(s);
        }catch (Exception ex){
            return def;
        }
    }

    public static Float toFloat(String s,float def){
        try{
            return Float.parseFloat(s);
        }catch (Exception ex){
            return def;
        }
    }
}
