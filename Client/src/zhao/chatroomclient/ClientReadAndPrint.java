package zhao.chatroomclient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.*;


class ClientReadAndPrint extends Thread{
    static Socket mySocket = null;
    static JTextField textInput;
    static JTextArea textShow;
    static JFrame chatViewJFrame;
    static BufferedReader in = null;
    static PrintWriter out = null;
    static String userName;

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(mySocket.getInputStream(),"UTF-8"));
            while (true) {
                String str = in.readLine();
                textShow.append(str + '\n');
                textShow.setCaretPosition(textShow.getDocument().getLength());
            }
        } catch (Exception e) {}
    }

    class LoginListen implements ActionListener {
        JTextField textField;
        JPasswordField pwdField;
        JFrame loginJFrame;  // 登录窗口本身

        ChatView chatView = null;

        public void setJTextField(JTextField textField) {
            this.textField = textField;
        }
        public void setJPasswordField(JPasswordField pwdField) {
            this.pwdField = pwdField;
        }
        public void setJFrame(JFrame jFrame) {
            this.loginJFrame = jFrame;
        }

        public Boolean check(String userName, String userPwd) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Connection conn = null;
            Statement st = null;
            ResultSet resultSet = null;
            try {
                conn = DriverManager.getConnection("jdbc:mysql://49.232.159.181:3306/chat?characterEncoding=utf8&useSSL=false", "chatuser", "12345678");
                String sql = "select `pwd` from `login` where `name` = '" + userName + "'";
                st = conn.createStatement();
                resultSet = st.executeQuery(sql);
                if (resultSet.next()) {
                    String rightPwd = resultSet.getString(1);
                    resultSet.close();
                    st.close();
                    conn.close();
                    return rightPwd.equals(userPwd);
                } else {
                    resultSet.close();
                    st.close();
                    conn.close();
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        public void actionPerformed(ActionEvent event) {
            userName = textField.getText();
            String userPwd = String.valueOf(pwdField.getPassword());
            if(check(userName, userPwd)) {
                chatView = new ChatView(userName);
                try {
                    InetAddress addr = InetAddress.getByName("49.232.159.181");
                    mySocket = new Socket(addr,8081);
                    loginJFrame.setVisible(false);
                    out = new PrintWriter(new OutputStreamWriter(mySocket.getOutputStream(), "UTF-8"));
                    out.println("用户【" + userName + "】进入聊天室！");
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ClientReadAndPrint readAndPrint = new ClientReadAndPrint();
                readAndPrint.start();
                ClientFileThread fileThread = new ClientFileThread(userName, chatViewJFrame, out);
                fileThread.start();
            }
            else {
                JOptionPane.showMessageDialog(loginJFrame, "账号或密码错误，请重新输入！", "提示", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    class RegisterListen implements ActionListener {
        JTextField textField;
        JPasswordField pwdField;
        JFrame loginJFrame;

        public void setJTextField(JTextField textField) {
            this.textField = textField;
        }
        public void setJPasswordField(JPasswordField pwdField) {
            this.pwdField = pwdField;
        }
        public void setJFrame(JFrame jFrame) {
            this.loginJFrame = jFrame;
        }

        public Boolean register(String userName, String userPwd) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Connection conn = null;
            Statement st = null;
            try {
                conn = DriverManager.getConnection("jdbc:mysql://49.232.159.181:3306/chat?characterEncoding=utf8&useSSL=false", "chatuser", "12345678");
                String sql = "insert into `login` values('" + userName + "','" + userPwd + "')";
                st = conn.createStatement();
                st.executeUpdate(sql);
                st.close();
                conn.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        public void actionPerformed(ActionEvent event) {
            userName = textField.getText();
            String userPwd = String.valueOf(pwdField.getPassword());  // getPassword方法获得char数组
            if(register(userName, userPwd)) {
                JOptionPane.showMessageDialog(loginJFrame, "注册成功", "提示", JOptionPane.WARNING_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(loginJFrame, "该用户已被注册", "提示", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    class ChatViewListen implements ActionListener{
        public void setJTextField(JTextField text) { textInput = text; }
        public void setJTextArea(JTextArea textArea) {
            textShow = textArea;
        }
        public void setChatViewJf(JFrame jFrame) {
            chatViewJFrame = jFrame;
            chatViewJFrame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    out.println("用户【" + userName + "】离开聊天室！");
                    out.flush();
                    System.exit(0);
                }
            });
        }
        public void actionPerformed(ActionEvent event) {
            try {
                String str = textInput.getText();
                if("".equals(str)) {
                    textInput.grabFocus();
                    JOptionPane.showMessageDialog(chatViewJFrame, "输入为空，请重新输入！", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                out.println(userName + "说：" + str);
                out.flush();
                textInput.setText("");
                textInput.grabFocus();
            } catch (Exception e) {}
        }
    }
}
