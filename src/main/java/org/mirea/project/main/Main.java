package org.mirea.project.main;

import com.toedter.calendar.JDateChooser;
import org.json.simple.parser.ParseException;
import org.mirea.project.parse.TicketParse;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        String token = "3b41f5952d682d8b4f1fce64a97a2edb"; // api token
        String[] airports = {"MOW", "ZRH", "HKG", "VOG", "MRV", "MCX"}; // набор аэропортов (можно изменить)

        createWindow(token, airports);
    }

    public static void createWindow(String token, String[] airports){
        JPanel contentsVerticalMain = new JPanel(); // содержит все элементы
        JPanel contentsHorizontalCities = new JPanel(); // содержат выбор аэропорта
        JPanel contentsHorizontalDates = new JPanel(); // содержат выбор дат
        MyFrame frame = new MyFrame(); // основное окно
        // текста для окна
        JLabel labelFrom = new JLabel("Откуда: ");
        JLabel labelTo = new JLabel("Куда: ");
        JLabel labelStart = new JLabel("Вылет: ");
        JLabel labelEnd = new JLabel("Возвращение: ");
        // кнопка обновления
        JButton button = new JButton();
        // выбор дат
        JDateChooser dateChooserStart = new JDateChooser();
        JDateChooser dateChooserEnd = new JDateChooser();
        // список всех предложений
        JList<String> list = new JList<>();
        // выбор аэропортов
        JComboBox<String> comboBox1 = new JComboBox<>();
        JComboBox<String> comboBox2 = new JComboBox<>();
        DefaultListModel<String> model = new DefaultListModel<>(); // обработчик списка
        DefaultComboBoxModel<String> cbModel1 = new DefaultComboBoxModel<>(); // обработчик 1 списка аэропортов
        DefaultComboBoxModel<String> cbModel2 = new DefaultComboBoxModel<>(); // обработчик 2 списка аэропортов
        Calendar calendar = Calendar.getInstance(); // календарь для отслеживания даты

        // настройка даты вылета
        dateChooserStart.setDateFormatString("yyyy-MM-dd");
        dateChooserStart.setDate(calendar.getTime());
        dateChooserStart.setMinSelectableDate(new Date());
        dateChooserStart.getCalendarButton();
        dateChooserStart.getDateEditor().addPropertyChangeListener(
                e -> {
                    if ("date".equals(e.getPropertyName())) {
                        dateChooserEnd.setMinSelectableDate((Date) e.getNewValue());
                        dateChooserEnd.setDate((Date) e.getNewValue());
                    }
                });
        // настройка даты прилёта
        dateChooserEnd.setDateFormatString("yyyy-MM-dd");
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        dateChooserEnd.setDate(calendar.getTime());
        dateChooserEnd.setMinSelectableDate(calendar.getTime());
        //установка обработчиков в свои элементы
        comboBox1.setModel(cbModel1);
        comboBox2.setModel(cbModel2);
        list.setModel(model);
        // что делаем по нажатии кнопки
        button.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                String from = cbModel1.getSelectedItem().toString();
                String to = cbModel2.getSelectedItem().toString();
                String start = sdf.format(dateChooserStart.getDate());
                String end = sdf.format(dateChooserEnd.getDate());

                if(!from.equals(to)) {
                    File f = new File("tickets_from_" + from + "_to_" + to + "_start_" + start + "_end_" + end + ".atsf");
                    boolean del = f.delete(); // поиск и удаление файла
                    if (del) System.out.println("Вышло");
                    model.clear();
                    TicketParse tp = new TicketParse(); // create new instanse
                    try {
                        TicketParse.Ticket[] tickets = tp.allTickets(from, to, start, end, token);
                        for (TicketParse.Ticket ticket : tickets)
                            model.addElement(ticket.toString()); // заполнение списка предложений
                    } catch (IOException | ParseException ex) {
                        throw new RuntimeException(ex); // обработка возможных ошибок
                    }
                    if (model.getSize() < 1){
                        model.addElement("Не существует таких билетов"); // проверка на наличие предложений
                    }
                    contentsVerticalMain.updateUI();
                    frame.setVisible(true); // обновление данных на экране
                } else {
                    model.clear(); // очистка списка
                    model.addElement("Нельзя так полететь");
                }
            }
        });
        button.setText("Обновить данные"); // устанавливаем текст на кнопке
        contentsVerticalMain.setLayout(new BoxLayout(contentsVerticalMain, BoxLayout.Y_AXIS)); // установка ориентации расположения всех элементов

        for (String el : airports){ cbModel1.addElement(el); cbModel2.addElement(el); } // заполнение списка аэропортов

        // заполнение экрана
        contentsVerticalMain.add(button);
        contentsHorizontalCities.add(labelFrom);
        contentsHorizontalCities.add(comboBox1);
        contentsHorizontalCities.add(labelTo);
        contentsHorizontalCities.add(comboBox2);
        contentsHorizontalDates.add(labelStart);
        contentsHorizontalDates.add(dateChooserStart);
        contentsHorizontalDates.add(labelEnd);
        contentsHorizontalDates.add(dateChooserEnd);
        contentsVerticalMain.add(contentsHorizontalCities);
        contentsVerticalMain.add(contentsHorizontalDates);
        contentsVerticalMain.add(new JScrollPane(list));
        frame.add(contentsVerticalMain);
        // включение отображения всего
        frame.setVisible(true);
    }
}