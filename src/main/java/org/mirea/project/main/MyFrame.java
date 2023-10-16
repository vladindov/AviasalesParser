package org.mirea.project.main;

import javax.swing.*;

public class MyFrame extends JFrame {
    MyFrame(){
        this.setTitle("Aviasales парсер"); // Ќазвание
        this.setResizable(false); // отключаем изменение размера окна
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // or 3 // при нажатии креста выключать приложение
        this.setSize(525, 800); // установка размера окна
    }
}
