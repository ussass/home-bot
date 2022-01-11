package ru.trofimov;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public class CallbackQueryHandle {

    private final CallbackQuery callbackQuery;

    public CallbackQueryHandle(CallbackQuery callbackQuery) {
        this.callbackQuery = callbackQuery;
    }

    public SendMessage getSendMessage(){
        SendMessage sendMessage = null;
        String action = callbackQuery.getData();
        switch (action){
            case "set_readings":
                sendMessage = SendMessage.builder()
                        .text("Please enter cold and hot water readings.\n" +
                                "Use this format:\n" +
                                "\n" +
                                "cold_hot")
                        .chatId(callbackQuery.getMessage().getChatId().toString())
                        .build();
                Bot.setMode("setReadings");
                break;
            default:
                String text = findButtonText(callbackQuery.toString(), callbackQuery.getData());

                sendMessage = SendMessage.builder()
                        .text("The \"" + text + "\" button is not working yet")
                        .chatId(callbackQuery.getMessage().getChatId().toString())
                        .build();
        }


        return sendMessage;
    }

    private String findButtonText(String callbackQuery, String data){

        int n = callbackQuery.indexOf(data);
        char[] chars = callbackQuery.toCharArray();
        int start = 0;
        int end = 0;
        for (int i = n; i > 0; i--) {
            if (chars[i] == '(') {
                start = i;
                break;
            }
        }
        for (int i = n; i < chars.length; i++) {
            if (chars[i] == ')') {
                end = i;
                break;
            }
        }
        String substring = callbackQuery.substring(start + 1, end);
        String[] split = substring.split(",");
        String result = "";
        for (String s : split) {
            if (s.contains("text=")) {
                result = s.split("=")[1];
                break;
            }
        }

        return result;
    }
}
