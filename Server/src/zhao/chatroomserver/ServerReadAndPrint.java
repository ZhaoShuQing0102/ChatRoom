package zhao.chatroomserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

class ServerReadAndPrint extends Thread{
    Socket nowSocket = null;
    BufferedReader in =null;
    PrintWriter out = null;

    public ServerReadAndPrint(Socket s) {
        this.nowSocket = s;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(nowSocket.getInputStream(),"UTF-8"));
            while (true) {
                String str = in.readLine();
                for(Socket socket: Server.list) {
                    out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
                    if(socket == nowSocket) {
                        out.println("(Äã)" + str);
                    }
                    else {
                        out.println(str);
                    }
                    out.flush();
                }
            }
        } catch (Exception e) {
            Server.list.remove(nowSocket);
        }
    }
}
