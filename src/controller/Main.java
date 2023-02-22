package controller;

import Server.Config;
import Server.Client;
import Server.Server;
import Server.TCPserver;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
    Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        stage=primaryStage;
        //Config.wechat=this;
        FXMLLoader fxloader=new FXMLLoader(getClass().getResource("login.fxml"));
        Parent login= fxloader.load();
        LoginController ctrl=fxloader.getController();
        ctrl.setApp(this);
        Scene loginView=new Scene(login,300,300);
        primaryStage.setScene(loginView);


        primaryStage.show();
    }
    public void login(String userName) throws Exception{
        Config.name=userName;
        //TODO Start a server thread
        //TODO Discover other client
        //TODO Init main page

        Thread regServer=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new Server(Config.serverPort).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.print("server is ready.");
            }
        });
        regServer.start();
        Thread msgServer=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    new Server(Config.msgServerPort).listen();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        msgServer.start();

        Thread fileServer = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    new TCPserver().start();
                }catch(Exception e){
                    System.out.println(e);
                    System.out.println("main里面的TCP线程出错");
                }
            }
        });
        fileServer.start();


        new Client().getOnline(userName);
        System.out.println(Config.uuid);
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        //MainController mctrl=((FXMLLoader)FXMLLoader.load(getClass().getResource("main.fxml"))).getController();
        //mctrl.setApp(this);
        Scene main=new Scene(root);

        stage.close();
        stage.setTitle("Hello World");
        stage.setScene(main);
        //main.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    new Client().offline();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}
