package zhao.chatroomserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


class FileReadAndWrite extends Thread {
    private Socket nowSocket = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;

    public FileReadAndWrite(Socket socket) {
        this.nowSocket = socket;
    }
    public void run() {
        try {
            input = new DataInputStream(nowSocket.getInputStream());
            while (true) {
                String textName = input.readUTF();
                long textLength = input.readLong();
                for(Socket socket: ServerFileThread.list) {
                    output = new DataOutputStream(socket.getOutputStream());
                    if(socket != nowSocket) {
                        output.writeUTF(textName);
                        output.flush();
                        output.writeLong(textLength);
                        output.flush();
                    }
                }
                int length = -1;
                long curLength = 0;
                byte[] buff = new byte[1024];
                while ((length = input.read(buff)) > 0) {
                    curLength += length;
                    for(Socket socket: ServerFileThread.list) {
                        output = new DataOutputStream(socket.getOutputStream());
                        if(socket != nowSocket) {
                            output.write(buff, 0, length);
                            output.flush();
                        }
                    }
                    if(curLength == textLength) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            ServerFileThread.list.remove(nowSocket);
        }
    }
}
