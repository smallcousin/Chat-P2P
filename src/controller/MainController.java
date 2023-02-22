package controller;

import Server.Config;
import Server.Client;
import Server.TCPclient;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pojo.Messages;
import pojo.UserInfo;
import pojo.UserList;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    public ListView<UserInfo> userList;// = new ListView<UserInfo>();
    @FXML
    private ListView<Messages > messages;//=new ListView<>();
    @FXML
    private TextArea msgSend;//=new TextArea();
    @FXML
    private Button btnSend;
    private Main app;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // add self to the user list
        UserInfo self=new UserInfo(Config.name,Config.uuid,"127.0.0.1",Config.msgServerPort);
        UserList.getUserList().getList().add(self);
        userList.setItems(UserList.getUserList().getList());
        userList.setCellFactory((ListView<UserInfo> l) -> new User());
        userList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserInfo>() {
            @Override
            public void changed(ObservableValue<? extends UserInfo> observable, UserInfo oldValue, UserInfo newValue) {
                messages.setItems(newValue.getMessages());
                messages.setCellFactory((ListView<Messages> l)->new msgView());
                newValue.setStatus(1);
                //UserList.getUserList().getList().set(UserList.getUserList().getList().indexOf(newValue),newValue);
               //oldValue.setStatus(1);
               UserList.getUserList().getList().add(null);
               UserList.getUserList().getList().remove(null);
               // UserList.getUserList().getList().set(UserList.getUserList().getList().indexOf(oldValue),oldValue);
            }
        });
    }
    public void setApp(Main app){
        this.app=app;
    }
    @FXML
    public void send(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String msg="{\"type\":\"msg\",\"uuid\":\""+Config.uuid.toString()+"\",\"msg\":\""+msgSend.getText()+"\"}";
                try {
                    UserInfo user= userList.getItems().get(userList.getSelectionModel().getSelectedIndex());
                    new Client().sendMsg(msg,new InetSocketAddress(user.getIp(),user.getPort()));
                    Messages msgs=new Messages(msgSend.getText());
                    msgs.setSelf(true);
                    user.getMessages().add(msgs);
                    msgSend.setText(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    File file;
    @FXML
    public void selectFile(){
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run(){
//
//
//            }
//        });
        Stage stage = new Stage();
        System.out.println("这个按键确实是响应了！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
        FileChooser fil_chooser = new FileChooser();
//        Label label = new Label("no files selected");
        file = null;
        file = fil_chooser.showOpenDialog(stage);
        if (file != null){
            msgSend.setText(file.getAbsolutePath());
            System.out.println("文件名读取成功：" + file.getAbsolutePath());
        }

//        if (file != null) {
//
//            label.setText(file.getAbsolutePath()
//                    + "  selected");
    }

    @FXML
    public void sendFile(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String msg="{\"type\":\"msg\",\"uuid\":\""+Config.uuid.toString()+"\",\"msg\":\""+msgSend.getText()+"\"}";
                try {
                    UserInfo user= userList.getItems().get(userList.getSelectionModel().getSelectedIndex());
                    new Client().sendMsg(msg,new InetSocketAddress(user.getIp(),user.getPort()));
                    Messages msgs=new Messages(msgSend.getText());
                    msgs.setSelf(true);
                    user.getMessages().add(msgs);
                    msgSend.setText(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            new TCPclient(file).snedfile();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("mainconttoller这里出错");
        }
    }
    class msgView extends ListCell<Messages>{
        protected void updateItem(Messages item, boolean empty) {
            super.updateItem(item,empty);
            setGraphic(null);
            setText(null);
            this.setStyle("-fx-background-color: #effffd");
            if(item!=null){
                HBox hBox=new HBox();
                ImageView pic=new ImageView();
                Image image=new Image("/images/yupi.jpg", 50, 50, true, true);
                pic.setImage(image);
                VBox vBox=new VBox();
                Text msg=new Text(item.getMessage());
                msg.maxWidth(300);
                msg.minWidth(180);
                userList.getItems().get(userList.getSelectionModel().getSelectedIndex()).setStatus(1);
                userList.refresh();
                System.out.println("selected"+userList.getSelectionModel().getSelectedIndex());
                //msg.setWrappingWidth(300);
                msg.wrappingWidthProperty();
                Text time=new Text("      ----"+item.getTime());
                time.setWrappingWidth(180);
                vBox.getChildren().addAll(msg,new Text(),time);
                if(item.isSelf()){
                    hBox.getChildren().addAll(vBox,pic);
                    vBox.setStyle("-fx-background-color: #54cd82");
                    hBox.setAlignment(Pos.CENTER_RIGHT);
                }else {
                    hBox.getChildren().addAll(pic,vBox);
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    vBox.setStyle("-fx-background-color: white");
                }
                setGraphic(hBox);
            }
        }
    }
    class User extends ListCell<UserInfo> {
        @Override
        protected void updateItem(UserInfo item, boolean empty) {
            super.updateItem(item, empty);
            setGraphic(null);
            setText(null);

            //build user info item
            if (item != null) {
                item.getMessages().addListener(new ListChangeListener<Messages>() {
                    @Override
                    public void onChanged(Change<? extends Messages> c) {
                        userList.refresh();
                    }
                });

                HBox hBox = new HBox();
                hBox.setSpacing(5);
                Text name = new Text(item.getUsername());
                //name.setWrappingWidth(30);
                ImageView status = new ImageView();
                Image image=null;
                if(item.getStatus()==1) {
                    image = new Image("/images/online.png", 16, 16, true, true);
                }else if(item.getStatus()==2){
                    image = new Image("/images/newmsg.png", 16, 16, true, true);
                }else if(item.getStatus()==0){
                    image = new Image("/images/offline.png", 16, 16, true, true);
                }
                status.setImage(image);
                ImageView picView = new ImageView();
                Image pic = new Image("/images/yupi.jpg", 50, 50, true, true);
                picView.setImage(pic);
                hBox.getChildren().addAll(status, picView, name);
                hBox.setAlignment(Pos.CENTER_LEFT);
                setGraphic(hBox);
            }
        }
    }
}