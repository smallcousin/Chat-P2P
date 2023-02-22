package Server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPserver {
    //    private static TCPserver tcPserver = null;
//    public TCPserver getTcPserver() throws IOException {
//        if (tcPserver == null){
//            tcPserver = new TCPserver();
//        }
//        return tcPserver;
//    }
    public ServerSocket serverSocket;
    public TCPserver() throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(65536));

    }
    public void start(){
        while(true) {
            try  {
                Socket socket = serverSocket.accept();
                new Thread(new UpLoad(socket)).start();
            }catch (Exception e){
                System.out.println(e);
                System.out.println("TCPserver的start这里出问题了");
            }
        }
    }
}
class UpLoad implements Runnable{

    private Socket socket;

    public UpLoad(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        FileOutputStream out = null;
        try {
            // 创建文件输入流，接收客户端的socket中的文件流
            InputStream in = socket.getInputStream();
            out  = new FileOutputStream("D://test1.txt");
            byte[] buf = new byte[1024];
            int len = 0;
            while((len = in.read(buf)) != -1){
                out.write(buf,0,buf.length);
            }
            System.out.println("文件传输成功");

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 关闭资源
            // 关闭输出流
            try {
                if(out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                out = null;
            }
            // 关闭socket
            try {
                if(socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                socket = null;
            }
        }
    }
}
