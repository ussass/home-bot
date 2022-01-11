package ru.trofimov;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.trofimov.service.WaterService;
import ru.trofimov.service.impl.WaterServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
                    if (message.getChatId() == 265956961){
                        sendMessage = SendMessage.builder()
                                .text(waterServiceGet.getReadings())
                                .chatId(message.getChatId().toString())
                                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(getListsButtons()).build())
                                .build();
                    } else {
                        sendMessage = SendMessage.builder()
                                .text(waterServiceGet.getReadings())
                                .chatId(message.getChatId().toString())
                                .build();
                    }
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

    public SendMessage getSendMessageByText(){
        SendMessage sendMessage = null;

        if(Bot.getMode().equals("setReadings")){
            WaterService waterService = new WaterServiceImpl();
            sendMessage = SendMessage.builder().chatId(message.getChatId().toString())
                    .text(waterService.setReadings(message.getText()))
                    .build();
            Bot.setMode("");

        }
        return sendMessage;
    }

    private List<List<InlineKeyboardButton>> getListsButtons() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(Arrays.asList(
                InlineKeyboardButton.builder()
                        .text("Set readings")
                        .callbackData("set_readings")
                        .build(),
                InlineKeyboardButton.builder()
                        .text("Test Button")
                        .callbackData("Test data")
                        .build()));
        return buttons;
    }
}
