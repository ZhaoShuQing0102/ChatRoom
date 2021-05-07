package zhao.chatroomclient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ChatView {
    String userName;
    JTextField text;
    JTextArea textArea;
    ClientReadAndPrint.ChatViewListen listener;

    public ChatView(String userName) {
        this.userName = userName ;
        init();
    }

    void init() {
        JFrame jf = new JFrame("�ͻ���");
        jf.setBounds(500,200,400,330);
        jf.setResizable(false);

        JPanel jp = new JPanel();
        JLabel lable = new JLabel("�û���" + userName);
        textArea = new JTextArea("********************��¼�ɹ�����ӭ���������ң�*******************\n",12, 32);
        textArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jp.add(lable);
        jp.add(scroll);

        text = new JTextField(20);
        JButton button = new JButton("����");
        JButton openFileBtn = new JButton("�����ļ�");
        jp.add(text);
        jp.add(button);
        jp.add(openFileBtn);

        openFileBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showFileOpenDialog(jf);
            }
        });

        listener = new ClientReadAndPrint().new ChatViewListen();
        listener.setJTextField(text);
        listener.setJTextArea(textArea);
        listener.setChatViewJf(jf);
        text.addActionListener(listener);
        button.addActionListener(listener);

        jf.add(jp);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }

    void showFileOpenDialog(JFrame parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("(txt)", "txt"));
        int result = fileChooser.showOpenDialog(parent);
        if(result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String path = file.getAbsolutePath();
            ClientFileThread.outFileToServer(path);
        }
    }
}