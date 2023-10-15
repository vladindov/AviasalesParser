package org.mirea.project.main;

import org.json.simple.parser.ParseException;
import org.mirea.project.parse.TicketParse;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
        String token = "3b41f5952d682d8b4f1fce64a97a2edb", from="MOW", to="VOG", start="2023-10-20", end="2023-10-25"; // default settings
        TicketParse tp = new TicketParse(); // create new instanse
        TicketParse.Ticket[] tickets = tp.allTickets(from, to, start, end, token); // get tickets

        createWindow(tickets);
    }

    public static void createWindow(TicketParse.Ticket[] tickets){
        JList<String> list = new JList<>();
        DefaultListModel<String> model = new DefaultListModel<>();

        list.setModel(model);

        for (TicketParse.Ticket ticket : tickets) model.addElement("Price: " + ticket.getPrice() + "\n Company: " + ticket.getAirline());

        MyFrame frame = new MyFrame();
        frame.add(new JScrollPane(list));
        frame.setVisible(true);
    }
}