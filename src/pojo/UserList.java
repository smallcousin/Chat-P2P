package pojo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserList {
    private static UserList userList=null;
    //LinkedList<UserInfo> list=new LinkedList<>();
    ObservableList<UserInfo> list= FXCollections.observableArrayList();
    private UserList(){
    }
    public static UserList getUserList() {
        if(userList==null){
            userList=new UserList();
        }
        return userList;
    }

    public ObservableList<UserInfo> getList() {
        return list;
    }
    public void add(UserInfo user){
        list.add(user);
    }
}
