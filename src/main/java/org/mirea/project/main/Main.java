package org.mirea.project.main;

import org.json.simple.parser.ParseException;
import org.mirea.project.parse.TicketParse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class Main {
    public static String token = "3b41f5952d682d8b4f1fce64a97a2edb", from="MOW", to="VOG", start="2023-10-20", end="2023-10-25"; // default settings
    public static String[] airports = {"MOW", "DVE", "SVO", "VOG", "MRV", "MCX"};
    public static void main(String[] args) throws IOException, ParseException {
        TicketParse tp = new TicketParse(); // create new instanse
        TicketParse.Ticket[] tickets = tp.allTickets(from, to, start, end, token); // get tickets

        createWindow(tickets);
    }

    public static void createWindow(TicketParse.Ticket[] tickets){
        JButton button = new JButton();
        JPanel contentsVertical = new JPanel();
        JPanel contentsHorizontal = new JPanel();
        MyFrame frame = new MyFrame();
        JList<String> list = new JList<>();
        JComboBox<String> comboBox1 = new JComboBox<>();
        JComboBox<String> comboBox2 = new JComboBox<>();
        DefaultListModel<String> model = new DefaultListModel<>();
        DefaultComboBoxModel<String> cbModel1 = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> cbModel2 = new DefaultComboBoxModel<>();

        comboBox1.setModel(cbModel1);
        comboBox2.setModel(cbModel2);
        list.setModel(model);
        button.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                from = cbModel1.getSelectedItem().toString();
                to = cbModel2.getSelectedItem().toString();
                File f = new File("tickets_from_"+from+"_to_"+to+"_start_"+start+"_end_"+end+".atsf");
                boolean del = f.delete();
                if(del) System.out.println("Вышло");
                model.clear();
                TicketParse tp = new TicketParse(); // create new instanse
                try {
                    TicketParse.Ticket[] tickets = tp.allTickets(from, to, start, end, token);
                    for (TicketParse.Ticket ticket : tickets) model.addElement("<html>Price: " + ticket.getPrice() + "<br> Company: " + ticket.getAirline() + "<html>");
                } catch (IOException | ParseException ex) {
                    throw new RuntimeException(ex);
                }
                try{
                    model.getElementAt(0);
                } catch (Exception exept){
                    model.addElement("Tickets doesn't exist");
                }
                contentsVertical.updateUI();
                frame.setVisible(true);
            }
        });
        button.setText("Reload list");
        contentsVertical.setLayout(new BoxLayout(contentsVertical, BoxLayout.Y_AXIS));

        for (TicketParse.Ticket ticket : tickets) model.addElement("<html>Price: " + ticket.getPrice() + "<br> Company: " + ticket.getAirline() + "<html>");
        for (String el : airports) cbModel1.addElement(el);
        for (String el : airports) cbModel2.addElement(el);

        contentsVertical.add(button);
        contentsHorizontal.add(comboBox1);
        contentsHorizontal.add(comboBox2);
        contentsVertical.add(contentsHorizontal);
        contentsVertical.add(new JScrollPane(list));
        frame.add(contentsVertical);
        frame.setVisible(true);
    }
}