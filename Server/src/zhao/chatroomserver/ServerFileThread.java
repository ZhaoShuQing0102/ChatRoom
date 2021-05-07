package zhao.chatroomserver;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ServerFileThread extends Thread{
    ServerSocket server = null;
    Socket socket = null;
    static List<Socket> list = new ArrayList<Socket>();

    public void run() {
        try {
            server = new ServerSocket(8090);
            while(true) {
                socket = server.accept();
                list.add(socket);
                FileReadAndWrite fileReadAndWrite = new FileReadAndWrite(socket);
                fileReadAndWrite.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

