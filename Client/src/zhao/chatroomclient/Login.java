package zhao.chatroomclient;

import java.awt.*;
import javax.swing.*;

public class Login {
    JTextField textField = null;
    JPasswordField pwdField = null;
    ClientReadAndPrint.LoginListen listener=null;
    ClientReadAndPrint.RegisterListen registerlistener=null;

    public Login() {
        init();
    }

    void init() {
        JFrame jf = new JFrame("登录");
        jf.setBounds(500, 250, 310, 210);
        jf.setResizable(false);  // 设置是否缩放

        JPanel jp1 = new JPanel();
        JLabel headJLabel = new JLabel("登录界面");
        headJLabel.setFont(new Font(null, 0, 35));  // 设置文本的字体类型、样式 和 大小
        jp1.add(headJLabel);

        JPanel jp2 = new JPanel();
        JLabel nameJLabel = new JLabel("用户名：");
        textField = new JTextField(20);
        JLabel pwdJLabel = new JLabel("密码：    ");
        pwdField = new JPasswordField(20);
        JButton loginButton = new JButton("登录");
        JButton registerButton = new JButton("注册");
        jp2.add(nameJLabel);
        jp2.add(textField);
        jp2.add(pwdJLabel);
        jp2.add(pwdField);
        jp2.add(loginButton);
        jp2.add(registerButton);

        JPanel jp = new JPanel(new BorderLayout());
        jp.add(jp1, BorderLayout.NORTH);
        jp.add(jp2, BorderLayout.CENTER);

        ClientReadAndPrint clientReadAndPrint = new ClientReadAndPrint();
        listener = clientReadAndPrint.new LoginListen();
        listener.setJTextField(textField);
        listener.setJPasswordField(pwdField);
        listener.setJFrame(jf);
        pwdField.addActionListener(listener);
        loginButton.addActionListener(listener);

        registerlistener = clientReadAndPrint.new RegisterListen();
        registerlistener.setJTextField(textField);
        registerlistener.setJPasswordField(pwdField);
        registerlistener.setJFrame(jf);
        pwdField.addActionListener(registerlistener);
        registerButton.addActionListener(registerlistener);


        jf.add(jp);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }
}
