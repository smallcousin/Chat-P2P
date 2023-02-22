package Server;

import javafx.application.Application;

import java.util.UUID;

public class Config {
   // public static int UDPServerPort=8088;//register server
    public static final int msgServerPort= (int)(Math.random()*2000)+3000;//msg server
    //public int UDPPort=8088;
    public  static int serverPort = 3000;
    public static String name;
    //public int TCPServerPort=9099;
    public static final UUID uuid=UUID.randomUUID();
    public static Application wechat;
    //public static DatagramSocket socket;
}
