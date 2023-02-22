package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Client extends Config {
    private DatagramSocket socket;

    public Client() throws Exception {
        //socket.setReuseAddress(true);
        socket = new DatagramSocket();
        //socket.setReuseAddress(true);
        //socket.bind(new InetSocketAddress(Config.msgServerPort));

    }

    //{"type":"online","uuid":UUID}
    public void getOnline(String name) throws Exception {
        //broadcast=null;
        String onlineMsg = "{\"type\":\"login\",\"uuid\":\"" + Config.uuid.toString() + "\",\"name\":\""+name+"\",\"port\":\""+Config.msgServerPort+"\"}";
        for(int i=0;i<5;i++) {
            DatagramPacket packet = new DatagramPacket(onlineMsg.getBytes(), onlineMsg.getBytes().length, InetAddress.getByName("255.255.255.255"), Config.serverPort + i);
            socket.send(packet);
        }

        //broadcast.close();
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
        Runnable active = () -> {
            try {
                active();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        exec.scheduleAtFixedRate(active, 5000, 5000, TimeUnit.MILLISECONDS);
    }

    public void active() throws Exception {
        String activeMsg = "{\"type\":\"active\",\"msg\":\"I'm \",\"uuid\":\""+Config.uuid+"\"}";
        for(int i=0;i<5;i++) {
            DatagramPacket packet = new DatagramPacket(activeMsg.getBytes(), activeMsg.getBytes().length, InetAddress.getByName("255.255.255.255"), serverPort+i);
            socket.send(packet);
        }
    }
    public void sendMsg(String msg, InetSocketAddress address) throws IOException {
        DatagramPacket msgPacket=new DatagramPacket(msg.getBytes(),msg.getBytes().length,address);
        socket.send(msgPacket);
        socket.close();
    }
    public void offline() throws IOException {
        String msg="{\"type\":\"offline\",\"uuid\":\""+Config.uuid.toString()+"\"}";
        for(int i=0;i<5;i++) {
            DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, InetAddress.getByName("255.255.255.255"), serverPort + i);
            socket.send(msgPacket);
        }
        socket.close();
    }
    //public static void main(String[] args) throws Exception {
    //    UDPClient client=new UDPClient();
    //   client.getOnline("GYH");
    // }
}
