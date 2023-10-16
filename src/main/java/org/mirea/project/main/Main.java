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
        String[] airports = {"MOW", "ZRH", "HKG", "VOG", "MRV", "MCX"}; // ����� ���������� (����� ��������)

        createWindow(token, airports);
    }

    public static void createWindow(String token, String[] airports){
        JPanel contentsVerticalMain = new JPanel(); // �������� ��� ��������
        JPanel contentsHorizontalCities = new JPanel(); // �������� ����� ���������
        JPanel contentsHorizontalDates = new JPanel(); // �������� ����� ���
        MyFrame frame = new MyFrame(); // �������� ����
        // ������ ��� ����
        JLabel labelFrom = new JLabel("������: ");
        JLabel labelTo = new JLabel("����: ");
        JLabel labelStart = new JLabel("�����: ");
        JLabel labelEnd = new JLabel("�����������: ");
        // ������ ����������
        JButton button = new JButton();
        // ����� ���
        JDateChooser dateChooserStart = new JDateChooser();
        JDateChooser dateChooserEnd = new JDateChooser();
        // ������ ���� �����������
        JList<String> list = new JList<>();
        // ����� ����������
        JComboBox<String> comboBox1 = new JComboBox<>();
        JComboBox<String> comboBox2 = new JComboBox<>();
        DefaultListModel<String> model = new DefaultListModel<>(); // ���������� ������
        DefaultComboBoxModel<String> cbModel1 = new DefaultComboBoxModel<>(); // ���������� 1 ������ ����������
        DefaultComboBoxModel<String> cbModel2 = new DefaultComboBoxModel<>(); // ���������� 2 ������ ����������
        Calendar calendar = Calendar.getInstance(); // ��������� ��� ������������ ����

        // ��������� ���� ������
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
        // ��������� ���� ������
        dateChooserEnd.setDateFormatString("yyyy-MM-dd");
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        dateChooserEnd.setDate(calendar.getTime());
        dateChooserEnd.setMinSelectableDate(calendar.getTime());
        //��������� ������������ � ���� ��������
        comboBox1.setModel(cbModel1);
        comboBox2.setModel(cbModel2);
        list.setModel(model);
        // ��� ������ �� ������� ������
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
                    boolean del = f.delete(); // ����� � �������� �����
                    if (del) System.out.println("�����");
                    model.clear();
                    TicketParse tp = new TicketParse(); // create new instanse
                    try {
                        TicketParse.Ticket[] tickets = tp.allTickets(from, to, start, end, token);
                        for (TicketParse.Ticket ticket : tickets)
                            model.addElement(ticket.toString()); // ���������� ������ �����������
                    } catch (IOException | ParseException ex) {
                        throw new RuntimeException(ex); // ��������� ��������� ������
                    }
                    if (model.getSize() < 1){
                        model.addElement("�� ���������� ����� �������"); // �������� �� ������� �����������
                    }
                    contentsVerticalMain.updateUI();
                    frame.setVisible(true); // ���������� ������ �� ������
                } else {
                    model.clear(); // ������� ������
                    model.addElement("������ ��� ��������");
                }
            }
        });
        button.setText("�������� ������"); // ������������� ����� �� ������
        contentsVerticalMain.setLayout(new BoxLayout(contentsVerticalMain, BoxLayout.Y_AXIS)); // ��������� ���������� ������������ ���� ���������

        for (String el : airports){ cbModel1.addElement(el); cbModel2.addElement(el); } // ���������� ������ ����������

        // ���������� ������
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
        // ��������� ����������� �����
        frame.setVisible(true);
    }
}