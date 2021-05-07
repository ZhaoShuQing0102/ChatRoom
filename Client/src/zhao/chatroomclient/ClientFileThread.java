package zhao.chatroomclient;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class ClientFileThread extends Thread{
    private Socket socket = null;
    private JFrame chatViewJFrame = null;
    static String userName = null;
    static PrintWriter out = null;
    static DataInputStream fileIn = null;
    static DataOutputStream fileOut = null;
    static DataInputStream fileReader = null;
    static DataOutputStream fileWriter = null;

    public ClientFileThread(String userName, JFrame chatViewJFrame, PrintWriter out) {
        ClientFileThread.userName = userName;
        this.chatViewJFrame = chatViewJFrame;
        ClientFileThread.out = out;
    }

    public void run() {
        try {
            InetAddress addr = InetAddress.getByName("49.232.159.181");
            socket = new Socket(addr, 8090);
            fileIn = new DataInputStream(socket.getInputStream());
            fileOut = new DataOutputStream(socket.getOutputStream());
            while(true) {
                String textName = fileIn.readUTF();
                long totleLength = fileIn.readLong();
                int result = JOptionPane.showConfirmDialog(chatViewJFrame, "是否接受？", "提示",
                        JOptionPane.YES_NO_OPTION);
                int length = -1;
                byte[] buff = new byte[1024];
                long curLength = 0;
                if(result == 0){
                    File userFile = new File("C:\\SocketFile\\" + userName);
                    if(!userFile.exists()) {
                        userFile.mkdirs();
                    }
                    File file = new File("C:\\SocketFile\\" + userName + "\\"+ textName);
                    fileWriter = new DataOutputStream(new FileOutputStream(file));
                    while((length = fileIn.read(buff)) > 0) {
                        fileWriter.write(buff, 0, length);
                        fileWriter.flush();
                        curLength += length;
                        if(curLength == totleLength) {
                            break;
                        }
                    }
                    out.println("【" + userName + "接收了文件！】");
                    out.flush();
                    JOptionPane.showMessageDialog(chatViewJFrame, "文件存放地址：\n" +
                            "C:\\SocketFile\\" +
                            userName + "\\" + textName, "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    while((length = fileIn.read(buff)) > 0) {
                        curLength += length;
                        if(curLength == totleLength) {
                            break;
                        }
                    }
                }
                fileWriter.close();
            }
        } catch (Exception e) {}
    }

    static void outFileToServer(String path) {
        try {
            File file = new File(path);
            fileReader = new DataInputStream(new FileInputStream(file));
            fileOut.writeUTF(file.getName());  // 发送文件名字
            fileOut.flush();
            fileOut.writeLong(file.length());  // 发送文件长度
            fileOut.flush();
            int length = -1;
            byte[] buff = new byte[1024];
            while ((length = fileReader.read(buff)) > 0) {  // 发送内容

                fileOut.write(buff, 0, length);
                fileOut.flush();
            }
            out.println("【" + userName + "已成功发送文件！】");
            out.flush();
        } catch (Exception e) {}
    }
}
