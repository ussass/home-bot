package ru.trofimov.service.impl;

import ru.trofimov.service.ReadPropertiesService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadPropertiesServiceImpl implements ReadPropertiesService {

    @Override
    public String getBotUsername() {
        return read("bot.username");
    }

    @Override
    public String getBotToken() {
        return read("bot.token");
    }

    private String read(String key){

        try (InputStream input = new FileInputStream("src/main/resources/botConfig.properties")) {

            Properties prop = new Properties();
            prop.load(input);

            return prop.getProperty(key);
        } catch (IOException io) {
            io.printStackTrace();
        }

        return null;
    }
}
