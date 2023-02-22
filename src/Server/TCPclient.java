package Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPclient {
    InputStream in;
    Socket socket;
    File file;
    Boolean flag;
    public TCPclient(File file) throws Exception{
        if (file.exists() && file.isFile()){
            in = new FileInputStream(file.getAbsolutePath());
            socket = new Socket();
            this.file = file;
            flag = true;
        }else{
            System.out.println("文件出错！！！！！！");
            flag = false;
        }
    }
    public void snedfile(){
        if(flag){
            try {
                socket.connect(new InetSocketAddress("127.0.0.1",65536));
                OutputStream out = socket.getOutputStream();
//                out.write((file.getName() + "\r\n").getBytes());
                System.out.println("发送文件的绝对路径为：" + file.getAbsolutePath() );

                System.out.println("发送的文件名字为：" + file.getName());
                // 向服务器发送[文件字节长度\r\n]
//                out.write((file.length() + "\r\n").getBytes());
//                System.out.println("发送的文件字节数为：" + file.length());
                // 向服务器发送[文件字节内容]
                byte[] data = new byte[1024];
                int i = 0;
                while((i = in.read(data)) != -1) {
                    out.write(data, 0, i);
                }
            }catch(Exception e){
                System.out.println(e);
                System.out.println("客户端传文件这里出错了");
            }finally {
                /**
                 * 关闭Scanner，文件输入流，套接字
                 * 套接字装饰了输出流，所以不用关闭输出流
                 */
//                try {
//                    if(in != null) {
//                        in.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }finally {
//                    // 强制将输入流置为空
//                    in = null;
//                }
//                try {
//                    if(socket != null) {
//                        socket.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }finally {
//                    // 强制释放socket
//                    socket = null;
//                }
            }
            System.out.println("文件传输完成");

        }
    }
}
