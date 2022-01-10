package ru.trofimov;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import ru.trofimov.service.WaterService;
import ru.trofimov.service.impl.WaterServiceImpl;

import java.util.Optional;

public class TextHandle {

    private final Message message;

    public TextHandle(Message message) {
        this.message = message;
    }

    public SendMessage getSendMessage(){
        Optional<MessageEntity> commandEntity =
                message.getEntities().stream().filter(messageEntity -> "bot_command".equals(messageEntity.getType())).findFirst();
        SendMessage sendMessage = null;
        if (commandEntity.isPresent()) {
            String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
            switch (command) {
                case "/water_readings":
                    WaterService waterServiceGet = new WaterServiceImpl();
                    sendMessage = SendMessage.builder()
                            .text(waterServiceGet.getReadings())
                            .chatId(message.getChatId().toString())
                            .build();
                    break;
                default:
                    sendMessage = SendMessage.builder()
                            .text("'" + command + "' command is not processed")
                            .chatId(message.getChatId().toString())
                            .build();
            }
        }

        return sendMessage;
    }
}
