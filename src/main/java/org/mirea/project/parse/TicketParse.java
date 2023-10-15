package org.mirea.project.parse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;

public class TicketParse {
    // класс билета
    public static class Ticket{
        String origin; // откуда
        String destination; // куда
        String origin_airport; // IATA-код аэропорта отправления
        String destination_airport; //  IATA-код аэропорта прибытия
        long price; // стоимость
        String airline; //  IATA-код компании
        String flight_number; // номер рейса
        String departure_at; // дата отправления
        String return_at; // дата возвращения
        long transfers; // кол-во пересадок туда
        long return_transfers; // кол-во пересадок обратно
        long duration; // общая продолжительность перелёта туда-обратно в минутах.
        long duration_to; // продолжительность перелёта до места назначения в минутах.
        long duration_back; // продолжительность перелёта обратно в минутах.
        String currency; // валюта
        String link; // ссылка на билет

        public Ticket(String origin, String destination, String origin_airport, String destination_airport, long price, String airline, String flight_number, String departure_at, String return_at, long transfers, long return_transfers, long duration, long duration_to, long duration_back, String currency, String link) {
            this.origin = origin;
            this.destination = destination;
            this.origin_airport = origin_airport;
            this.destination_airport = destination_airport;
            this.price = price;
            this.airline = airline;
            this.flight_number = flight_number;
            this.departure_at = departure_at;
            this.return_at = return_at;
            this.transfers = transfers;
            this.return_transfers = return_transfers;
            this.duration = duration;
            this.duration_to = duration_to;
            this.duration_back = duration_back;
            this.currency = currency;
            this.link = link;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public String getOrigin_airport() {
            return origin_airport;
        }

        public void setOrigin_airport(String origin_airport) {
            this.origin_airport = origin_airport;
        }

        public String getDestination_airport() {
            return destination_airport;
        }

        public void setDestination_airport(String destination_airport) {
            this.destination_airport = destination_airport;
        }

        public long getPrice() {
            return price;
        }

        public void setPrice(long price) {
            this.price = price;
        }

        public String getAirline() {
            if(airline.equals("DP")) return "Pobeda";
            if(airline.equals("SU")) return "Aeroflot";
            if(airline.equals("FV")) return "Rossiya";
            return airline;
        }

        public void setAirline(String airline) {
            this.airline = airline;
        }

        public String getFlight_number() {
            return flight_number;
        }

        public void setFlight_number(String flight_number) {
            this.flight_number = flight_number;
        }

        public String getDeparture_at() {
            return departure_at;
        }

        public void setDeparture_at(String departure_at) {
            this.departure_at = departure_at;
        }

        public String getReturn_at() {
            return return_at;
        }

        public void setReturn_at(String return_at) {
            this.return_at = return_at;
        }

        public long getTransfers() {
            return transfers;
        }

        public void setTransfers(long transfers) {
            this.transfers = transfers;
        }

        public long getReturn_transfers() {
            return return_transfers;
        }

        public void setReturn_transfers(long return_transfers) {
            this.return_transfers = return_transfers;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public long getDuration_to() {
            return duration_to;
        }

        public void setDuration_to(long duration_to) {
            this.duration_to = duration_to;
        }

        public long getDuration_back() {
            return duration_back;
        }

        public void setDuration_back(long duration_back) {
            this.duration_back = duration_back;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }

    //
    public Ticket[] allTickets(String from, String to, String start, String end, String token) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Ticket[] ticketArray;
        JSONArray jArray;

        File f = new File("tickets_from_"+from+"_to_"+to+"_start_"+start+"_end_"+end+".atsf");
        if(f.exists()){
            jArray = (JSONArray) parser.parse(new FileReader(f));
        } else {
            URL parsingUrl = new URL("https://api.travelpayouts.com/aviasales/v3/prices_for_dates?origin="+from+"&"+
                    "&destination="+to+"&"+
                    "&departure_at="+start+"&"+
                    "&return_at="+end+"&"+
                    "&token="+token);

            JSONObject obj = (JSONObject) parser.parse(new InputStreamReader(parsingUrl.openStream()));
            jArray = (JSONArray) obj.get("data");

            PrintWriter out = new PrintWriter("tickets_from_"+from+"_to_"+to+"_start_"+start+"_end_"+end+".atsf");
            out.println(jArray);
            out.close();
        }
        ticketArray = new Ticket[jArray.size()];

        for (int i = 0; i < jArray.size(); i++) {
            JSONObject object = (JSONObject) jArray.get(i);
            Ticket ticket = new Ticket((String) object.get("origin"),(String) object.get("destination"),
                    (String) object.get("origin_airport"),(String) object.get("destination_airport"),
                    (long) object.get("price"),(String) object.get("airline"),(String) object.get("flight_number"),
                    (String) object.get("departure_at"),(String) object.get("return_at"),(long) object.get("transfers"),
                    (long) object.get("return_transfers"),(long) object.get("duration"),(long) object.get("duration_to"),
                    (long) object.get("duration_back"),(String) object.get("currency"),(String) object.get("link"));
            ticketArray[i] = ticket;
        }

        return ticketArray;
    }

    public void Gay(Ticket[] ok){
        ok[0].origin = "";
    }
}
