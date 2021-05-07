package zhao.chatroomserver;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server{
    static ServerSocket server = null;
    static Socket socket = null;
    static List<Socket> list = new ArrayList<Socket>();

    public static void main(String[] args) {
        try {
            ServerFileThread serverFileThread = new ServerFileThread();
            serverFileThread.start();
            server = new ServerSocket(8081);
            while (true) {
                socket = server.accept();
                list.add(socket);
                ServerReadAndPrint readAndPrint = new ServerReadAndPrint(socket);
                readAndPrint.start();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}

