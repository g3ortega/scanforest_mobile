package challenge.scanforest.utils;

/**
 * Created by gerardo on 4/12/15.
 */
public class Session {
    private String token="";
    private static Session instance;
    private Session(){

    }

    public  static Session getInstance(){
        if(instance==null){
            instance=new Session();
        }
        return instance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
