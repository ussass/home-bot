package ru.trofimov;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.trofimov.service.ReadPropertiesService;
import ru.trofimov.service.impl.ReadPropertiesServiceImpl;


public class Bot extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;

    public Bot(String botUsername, String botToken) {
        this.botUsername = botUsername;
        this.botToken = botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("update.getMessage().getText() = " + update.getMessage().getText());
    }

    public static void main(String[] args) {

        ReadPropertiesService readProp = new ReadPropertiesServiceImpl();
        Bot bot = new Bot(readProp.getBotUsername(), readProp.getBotToken());
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
