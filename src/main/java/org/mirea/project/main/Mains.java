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

public class Mains {
    public static void main(String[] args) throws IOException, ParseException {
        String token = "3b41f5952d682d8b4f1fce64a97a2edb"; // api token
        String[] airports = {"MOW", "ZRH", "HKG", "VOG", "MRV", "MCX"}; // набор аэропортов (можно изменить)
        String[] airlines = {"Pobeda", "Aeroflot", "Rossiya", "Smartavia", "Nord Wind", "Ural", "S7", "Arabia", "Qatar", "Emirates", "Red Wings", "Pegasus Hava Tasimaciligi"};

        createWindow(token, airports, airlines);
    }

    public static void createWindow(String token, String[] airports, String[] airlines){
        JPanel contentsVerticalMain = new JPanel(); // содержит все элементы
        JPanel contentsHorizontalCities = new JPanel(); // содержат выбор аэропорта
        JPanel contentsHorizontalDates = new JPanel(); // содержат выбор дат
        JPanel contentsHorizontalButtons = new JPanel(); // содержат выбор
        JPanel contentsHorizontalAirlines = new JPanel(); // содержат выбор
        MyFrame frame = new MyFrame(); // основное окно
        // текста для окна
        JLabel labelFrom = new JLabel("Откуда: ");
        JLabel labelTo = new JLabel("Куда: ");
        JLabel labelStart = new JLabel("Вылет: ");
        JLabel labelEnd = new JLabel("Возвращение: ");
        // кнопка обновления
        JButton buttonUpdate = new JButton();
        JButton buttonLoad = new JButton();
        JButton buttonChange = new JButton();
        // выбор дат
        JDateChooser dateChooserStart = new JDateChooser();
        JDateChooser dateChooserEnd = new JDateChooser();
        // список всех предложений
        JList<String> ticketsList = new JList<>();
        JList<String> airlineOkList = new JList<>();
        JList<String> airlineNOkList = new JList<>();
        // выбор аэропортов
        JComboBox<String> comboBox1 = new JComboBox<>();
        JComboBox<String> comboBox2 = new JComboBox<>();
        DefaultListModel<String> ticketsModel = new DefaultListModel<>(); // обработчик списка
        DefaultListModel<String> OkModel = new DefaultListModel<>(); // обработчик списка
        DefaultListModel<String> NOkModel = new DefaultListModel<>(); // обработчик списка
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
        airlineOkList.setModel(OkModel);
        airlineNOkList.setModel(NOkModel);
        ticketsList.setModel(ticketsModel);
        // что делаем по нажатии кнопки
        buttonUpdate.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                String from = cbModel1.getSelectedItem().toString();
                String to = cbModel2.getSelectedItem().toString();
                String start = sdf.format(dateChooserStart.getDate());
                String end = sdf.format(dateChooserEnd.getDate());String[] sort = {};
                try{ for(int i = 0; i< airlineNOkList.getModel().getSize();i++) sort[i] = airlineNOkList.getModel().getElementAt(i);}
                catch (Exception ignored){}

                if(!from.equals(to)) {
                    File f = new File("tickets_from_" + from + "_to_" + to + "_start_" + start + "_end_" + end + ".atsf");

                    GetTickets(f, true, from, to, start, end, token, ticketsModel, ticketsList, sort);

                    contentsVerticalMain.updateUI();
                    frame.setVisible(true); // обновление данных на экране
                } else {
                    ticketsModel.clear(); // очистка списка
                    ticketsModel.addElement("Нельзя так полететь");
                }
            }
        });
        buttonLoad.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                String from = cbModel1.getSelectedItem().toString();
                String to = cbModel2.getSelectedItem().toString();
                String start = sdf.format(dateChooserStart.getDate());
                String end = sdf.format(dateChooserEnd.getDate());
                String[] sort = {"","","","","","","","","","","",""};
                for(int i = 0; i< NOkModel.getSize(); i++) sort[i] = NOkModel.getElementAt(i);

                if(!from.equals(to)) {
                    File f = new File("tickets_from_" + from + "_to_" + to + "_start_" + start + "_end_" + end + ".atsf");

                    System.out.println(sort[0]);

                    GetTickets(f, false, from, to, start, end, token, ticketsModel, ticketsList, sort);

                    contentsVerticalMain.updateUI();
                    frame.setVisible(true); // обновление данных на экране
                } else {
                    ticketsModel.clear(); // очистка списка
                    ticketsModel.addElement("Нельзя так полететь");
                }
            }
        });
        buttonChange.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    NOkModel.addElement(airlineOkList.getSelectedValue());
                    OkModel.remove(airlineOkList.getSelectedIndex());
                } catch (Exception ignored){}

                try {
                    OkModel.addElement(airlineNOkList.getSelectedValue());
                    NOkModel.remove(airlineNOkList.getSelectedIndex());
                } catch (Exception ignored){}
            }
        });
        buttonUpdate.setText("Обновить данные"); // устанавливаем текст на кнопке
        buttonLoad.setText("Загрузить данные"); // устанавливаем текст на кнопке
        buttonChange.setText("Сменить сортировку"); // устанавливаем текст на кнопке
        contentsVerticalMain.setLayout(new BoxLayout(contentsVerticalMain, BoxLayout.Y_AXIS)); // установка ориентации расположения всех элементов

        for (String el : airlines) OkModel.addElement(el); // заполнение списка аэропортов
        for (String el : airports){ cbModel1.addElement(el); cbModel2.addElement(el); } // заполнение списка аэропортов

        // заполнение экрана
        contentsVerticalMain.add(buttonUpdate);
        contentsHorizontalButtons.add(buttonUpdate);
        contentsHorizontalButtons.add(buttonLoad);
        contentsHorizontalCities.add(labelFrom);
        contentsHorizontalCities.add(comboBox1);
        contentsHorizontalCities.add(labelTo);
        contentsHorizontalCities.add(comboBox2);
        contentsHorizontalDates.add(labelStart);
        contentsHorizontalDates.add(dateChooserStart);
        contentsHorizontalDates.add(labelEnd);
        contentsHorizontalDates.add(dateChooserEnd);
        contentsHorizontalAirlines.add(airlineOkList);
        contentsHorizontalAirlines.add(airlineNOkList);
        contentsVerticalMain.add(contentsHorizontalButtons);
        contentsVerticalMain.add(contentsHorizontalCities);
        contentsVerticalMain.add(contentsHorizontalAirlines);
        contentsVerticalMain.add(buttonChange);
        contentsVerticalMain.add(contentsHorizontalDates);
        contentsVerticalMain.add(new JScrollPane(ticketsList));
        frame.add(contentsVerticalMain);
        // включение отображения всего
        frame.setVisible(true);
    }

    private static void GetTickets(File f, boolean del, String from, String to, String start, String end, String token,
                                   DefaultListModel<String> model, JList<String> list, String[] sort){
        if(del) {
            boolean delete = f.delete(); // поиск и удаление файла
            if (delete) System.out.println("Yes");
        }

        TicketParse tp = new TicketParse(); // create new instanse
        model.clear();

        try {
            TicketParse.Ticket[] tickets = tp.allTickets(from, to, start, end, token);
            for (TicketParse.Ticket ticket : tickets) {
                boolean sInSort = false;
                for(String s : sort) if(s.equals(ticket.getAirline())) sInSort = true;
                if(!sInSort){
                    model.addElement(ticket.toString()); // заполнение списка предложений
                }
            }
        } catch (IOException | ParseException ex) {
            throw new RuntimeException(ex); // обработка возможных ошибок
        }
        if (model.getSize() < 1){
            model.addElement("Не существует таких билетов"); // проверка на наличие предложений
        }
        list.setModel(model);
    }
}