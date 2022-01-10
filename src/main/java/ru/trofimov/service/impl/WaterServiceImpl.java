package ru.trofimov.service.impl;

import ru.trofimov.service.WaterService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WaterServiceImpl implements WaterService {
    @Override
    public String getReadings() {
        String reading;
        try {
            reading = getReadingsPrivate();
        } catch (Exception e) {
            return "";
        }
        if (reading.contains("error")){
            return reading;
        }
        String[] count = reading.split(",");
        for (int i = 0; i < count.length; i++) {
            count[i] = count[i].replaceAll("\\D+", "");
        }
        System.out.println("Cold: " + count[0] + "\nHot: " + count[1]);
        return "Cold: " + count[0] + "\nHot: " + count[1];
    }

    @Override
    public String setReadings(String readings) {
        if (!readings.contains("_"))
            return "Error format";

        int cold, hot;
        try {
            cold = Integer.parseInt(readings.split("_")[0]);
            hot = Integer.parseInt(readings.split("_")[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "You need to enter numbers" +
                    "\n" + e.getMessage();
        }

        boolean isOk;
        try {
            isOk = setReadingsPrivate(cold, hot);
        } catch (Exception e) {
            return "Connection error";
        }

        return isOk? "Readings saved" : "Something went wrong";
    }

    private String getReadingsPrivate() throws Exception {
        URL url = new URL("http://localhost:5000/api/v1/water");
        StringBuilder content = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int code;
        try {
            code = connection.getResponseCode();
        } catch (IOException e) {
            return "connection error, the GPIO rest service may not work";
        }
        if (code != 200)
            return "connection error, code = " + connection.getResponseCode();
        System.out.println(4);
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        System.out.println(5);
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        connection.disconnect();
        return content.toString();
    }

    private boolean setReadingsPrivate(int cold, int hot) throws Exception{
        URL url = new URL("http://localhost:5000/api/v1/water");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonInputString = "{\"cold\": " + cold + ", \"hot\": " + hot + "}";

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        boolean isOk;
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            isOk = response.toString().contains("true");
        }
        return isOk;
    }
}
