package Server;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import net.sf.json.JSONObject;
import pojo.Messages;
import pojo.UserInfo;
import pojo.UserList;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.UUID;


public class Server {
    private DatagramChannel serverChannel;
    private Selector selector;
    private int serverPort;
    //private int UDPServerPort=8089;
    public Server(int serverPort) throws Exception {
        this.serverPort=serverPort;
    }

    //register server
    public void start() throws Exception {
        serverChannel = DatagramChannel.open();
        serverChannel.configureBlocking(false);
        for(int i=0;i<5;i++) {
            try {
                serverChannel.socket().bind(new InetSocketAddress(serverPort));
                System.out.println("reg server start at port "+serverPort);
                break;
            }catch (Exception e){
                serverPort++;
            }
        }
        selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_READ);
        while(selector.select()>0){
            Iterator<SelectionKey> it=selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key=it.next();
                it.remove();
                if(key.isReadable()){
                    DatagramChannel channel=(DatagramChannel) key.channel();
                    channel.configureBlocking(false);
                    ByteBuffer buf=ByteBuffer.allocate(8*1024);
                    SocketAddress client=channel.receive(buf);
                    String ip=client.toString().replace("/","").split(":")[0];
                    System.out.println(client.toString().replace("/","").split(":")[0]+":"+client.toString().replace("/","").split(":")[1]);
                    buf.flip();/*
                    UserList userList=UserList.getUserList();
                    UserInfo user=new UserInfo("GYH",UUID.randomUUID(),"193",000);
                    userList.getList().add(user);
                    */
                    String msg=Server.byteBufferToString(buf);
                    System.out.println(msg+"  ");
                    System.out.println("测试断点1");
                    JSONObject json= JSONObject.fromObject(msg);
                    System.out.println("测试断点2");
                    if(json.get("type").equals("login")){
                        handleLogin(msg,ip);
                    }else if(json.get("type").equals("active")){
                        handleActive(msg);
                    }else if(json.get("type").equals("offline")){
                        handleOffline(msg);
                    }
                    //System.out.print(msg);
                    //msg="Hello ya!";
                    //buf= ByteBuffer.wrap(msg.getBytes());
                    // buf.flip();
                    //channel.send(buf,client);
                }
            }
        }
    }


    //用于接收udp消息
    public void listen() throws Exception {
        serverChannel = DatagramChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().setReuseAddress(true);
        serverChannel.socket().bind(new InetSocketAddress(serverPort));
        System.out.println("msg server start ad port "+serverPort);
        selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_READ);
        while(selector.select()>0){
            Iterator<SelectionKey> it=selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key=it.next();
                it.remove();
                if(key.isReadable()){
                    DatagramChannel channel=(DatagramChannel) key.channel();
                    channel.configureBlocking(false);
                    ByteBuffer buf=ByteBuffer.allocate(8*1024);
                    SocketAddress client=channel.receive(buf);
                    String ip=client.toString().replace("/","").split(":")[0];
                    String port=client.toString().replace("/","").split(":")[1];
                    System.out.println(client.toString().replace("/","").split(":")[0]+":"+client.toString().replace("/","").split(":")[1]);
                    buf.flip();
                    String msg=Server.byteBufferToString(buf);

                    System.out.println(msg);
                    JSONObject json= JSONObject.fromObject(msg);
                    if(json.get("type").equals("login")){
                        handleLogin(msg,ip);
                    }else if(json.get("type").equals("online")){
                        handleOnline(msg,ip);
                    }else if(json.get("type").equals("active")){
                        handleActive(msg);
                    }else if(json.get("type").equals("msg")){
                        handleMsg(msg);

                    }
                    //System.out.print(msg);
                    //msg="Hello ya!";
                    //buf= ByteBuffer.wrap(msg.getBytes());
                    // buf.flip();
                    //channel.send(buf,client);
                }
            }
        }
    }

    private void handleLogin(String msg,String ip) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                UserList userList = UserList.getUserList();
                JSONObject json=JSONObject.fromObject(msg);
                UserInfo user = new UserInfo(JSONObject.fromObject(msg).get("name").toString(), UUID.fromString(JSONObject.fromObject(msg).get("uuid").toString()), ip,json.getInt("port"));
                userList.getList().add(user);
                System.out.println("user++++++++++++++++++++++++++++++++++++++++");
                String response = "{\"type\":\"online\",\"uuid\":\"" + Config.uuid + "\",\"rec\":\"" + user.getUuid() + "\",\"name\":\""+Config.name+"\",\"port\":\""+Config.msgServerPort+"\"}";
                //System.out.println(response);
                DatagramSocket socket = null;
                try {

                    socket = new DatagramSocket();
                    //socket.setReuseAddress(true);
                    //socket.bind(new InetSocketAddress(Config.msgServerPort));
                    DatagramPacket packet = new DatagramPacket(response.getBytes(), response.getBytes().length, new InetSocketAddress(InetAddress.getByName(ip),json.getInt("port")));
                    socket.send(packet);
                    socket.close();
                    System.out.println("res sent to "+ip+":"+json.get("port"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(userList.getList().size());
                //javaFX operations should go here
            }
        });

    }

    //get online response
    //this message contains a exists user
    private void handleOnline(String msg,String ip){
        //TODO add this user to user list
        //System.out.print(msg);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                JSONObject json=JSONObject.fromObject(msg);
                if(json.get("rec").toString().equals(Config.uuid.toString())){
                    UserList userList=UserList.getUserList();
                    UserInfo user=new UserInfo(json.get("name").toString(),UUID.fromString(json.get("uuid").toString()),ip,json.getInt("port"));
                    userList.getList().add(user);
                    System.out.println("user++++++++++++++++++++++++++++++++++++++++");
                    System.out.println(userList.getList().size());
                }
            }
        });

    }
    private void handleActive(String msg){
        //TODO update the user's active status
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                JSONObject json=JSONObject.fromObject(msg);
                System.out.println("handle active msg");
                ObservableList<UserInfo> userList=UserList.getUserList().getList();
                for(UserInfo user : userList) {
                    if(user.getUuid().toString().equals(json.getString("uuid"))){
                        //user.setStatus(2);
                        //userList.set(userList.indexOf(user),user);
                        //userList.add(null);
                        //userList.remove(null);

                        //Messages messages=new Messages(json.getString("msg"));
                        //messages.setSelf(true);
                        //user.getMessages().add(messages);
                    }
                    System.out.println(user.getUuid());
                }
            }
        });

    }
    private void handleMsg(String msg){
        //TODO handle a message
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                JSONObject json=JSONObject.fromObject(msg);
                System.out.println("handle msg");
                ObservableList<UserInfo> userList=UserList.getUserList().getList();
                for(UserInfo user : userList) {
                    if(user.getUuid().toString().equals(json.getString("uuid"))){
                        user.setStatus(2);
                        System.out.println(UserList.getUserList().getList().size());
                        Messages messages=new Messages(json.getString("msg"));
                        //messages.setSelf(true);
                        //MainController mctrl= null;
                        //try {
                        //mctrl = ((FXMLLoader)FXMLLoader.load(getClass().getResource("../WeChat/main.fxml"))).getController();
                        //   } //catch (IOException e) {
                        //   e.printStackTrace();
                        //  }
                        //MainController mctrl=((FXMLLoader)FXMLLoader.load(getClass().getResource("main.fxml"))).getController();
                        //mctrl.userList.refresh();
                        user.getMessages().add(messages);
                    }
                    System.out.println(user.getUsername());
                }
            }
        });

    }
    private void handleOffline(String msg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                JSONObject json=JSONObject.fromObject(msg);
                System.out.println("handle msg");
                ObservableList<UserInfo> userList=UserList.getUserList().getList();
                for(UserInfo user : userList) {
                    if(user.getUuid().toString().equals(json.getString("uuid"))){
                        user.setStatus(0);
                        //System.out.println(UserList.getUserList().getList().size());
                        //Messages messages=new Messages(json.getString("msg"));
                        user.getMessages().add(null);
                    }
                    System.out.println(user.getUsername());
                }
            }
        });
    }
    public static String byteBufferToString(ByteBuffer buf)  {
        CharBuffer charBuffer = null;
        Charset charSet= Charset.forName("UTF-8");
        CharsetDecoder decode=charSet.newDecoder();
        try {
            charBuffer=decode.decode(buf);
        } catch (CharacterCodingException e) {
            e.printStackTrace();
        }
        buf.flip();
        return charBuffer.toString();

    }
}
