package pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Messages {
    private String message;
    private String time;
    private boolean self;

    public Messages(String message){
        this.message=message;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date data=new Date();
        time=sdf.format(data);
        this.self=false;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public boolean isSelf() {
        return self;
    }

    public void setSelf(boolean self){
        this.self=self;
    }
}
