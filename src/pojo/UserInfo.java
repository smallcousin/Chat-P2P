package pojo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.UUID;

public class UserInfo {
    public UserInfo(){

    }
    public UserInfo(String username, UUID uuid, String ip, int port) {
        this.username = username;
        this.ip=ip;
        this.uuid=uuid;
        this.port=port;
        this.status=1;
    }

    //用户名
    private String username;
    //用户头像
    private String userpic;
    //用户IP
    private String  ip;
    //UUID
    private UUID uuid;
    //Port
    private int port;
    //Messages
    private ObservableList<Messages> messages= FXCollections.observableArrayList();
    //Status
    //1:online  2:messaged  0:offline
    private int status;

    public int getStatus(){return status;}
    public void setStatus(int status){this.status=status;}
    public void addMsg(Messages msg){
        messages.add(msg);
    }

    public ObservableList<Messages> getMessages() {
        return messages;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getIp(){
        return ip;
    }
    public int getPort(){
        return port;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUserpic() {
        return userpic;
    }
    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }
}
