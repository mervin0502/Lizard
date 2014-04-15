package me.mervin.project.usr.mervin;

import java.awt.*;
import java.awt.event.*;
import java.awt.Frame;

public class Test2 {
 public static void main(String[] args) {
  Frame f = new Frame("用户登录");
  f.setLayout(new GridLayout(4, 2));
  final TextField id = new TextField("输入用户名", 10);
  final TextField pw = new TextField(10);
  pw.setEchoChar('*');
  f.add(new Label("用户名:", Label.CENTER));
  f.add(id);
  f.add(new Label("密码:", Label.CENTER));
  f.add(pw);
  Button b1 = new Button("登陆");
  String str1 = id.getText();
  String str2 = pw.getText();
  final TextField printid = new TextField(str1, 10);
  final TextField printpw = new TextField(str2, 10);
  // //----------
  b1.addActionListener(new ActionListener() {

   @Override
   public void actionPerformed(ActionEvent e) {
    printid.setText(id.getText());
    printpw.setText(pw.getText());
   }

  });
  // //----------
  Button b2 = new Button("取消");
  f.add(b1);
  f.add(b2);

  f.add(printid);
  f.add(printpw);
  f.pack();

  printid.setBackground(new Color(220, 0, 0));
  printpw.setBackground(new Color(220, 0, 0));
  f.setSize(250, 120);
  f.setVisible(true);
  f.addWindowListener(new WindowAdapter() {
   public void windowClosing(WindowEvent args) {
    System.exit(0);
   }
  });
 }
}