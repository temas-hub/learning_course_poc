package tutorial;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class Bot extends TelegramLongPollingBot {
    private boolean screaming = false;

    private InlineKeyboardMarkup keyboardM1;
    private InlineKeyboardMarkup keyboardM2;



    @Override
    public String getBotUsername() {
        return "TutorialBot";
    }

    @Override
    public String getBotToken() {
        return "7370284276:AAEfIfkd3ItnhEZURRwK-k1A27Jii7LpR1s";
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update);
        var callbackQuery = update.getCallbackQuery();
        if (callbackQuery != null && callbackQuery.getData() != null) {
            var message = callbackQuery.getMessage();
            var user = callbackQuery.getFrom();
            var id = user.getId();
            try {
                buttonTap(id, callbackQuery.getId(), callbackQuery.getData(), message.getMessageId());
                return;
            } catch (TelegramApiException e) {
                System.out.println(e);
                throw new RuntimeException(e);
            }
        }
        var message = update.getMessage();
        var user = message.getFrom();
        var id = user.getId();
        var txt = message.getText();
        var next = InlineKeyboardButton.builder()
                .text("К уроку").callbackData("next")
                .build();
        var back = InlineKeyboardButton.builder()
                .text("Назад").callbackData("back")
                .build();
        var url = InlineKeyboardButton.builder()
                .text("На ютьюбчик")
                .url("https://www.youtube.com/")
                .build();
        keyboardM1 = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(next)).build();
        //Buttons are wrapped in lists since each keyboard is a set of button rows
        keyboardM2 = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(back))
                .keyboardRow(List.of(url))
                .build();

        if(message.isCommand()) {
            if (txt.equals("/scream"))
                screaming = true;
            else if (txt.equals("/whisper"))
                screaming = false;
            else if (txt.equals("/menu"))
                sendMenu(id, "<b>Урок 1</b>", keyboardM1);
            return;
        }
        if(screaming)                            //If we are screaming
            scream(id, update.getMessage());     //Call a custom method
        else
            copyMessage(id, message.getMessageId()); //Else proceed normally
    }


    private void scream(Long id, Message msg) {
        if(msg.hasText())
            sendText(id, msg.getText().toUpperCase());
        else
            copyMessage(id, msg.getMessageId());  //We can't really scream a sticker
    }

    public void sendMenu(Long who, String txt, InlineKeyboardMarkup kb){
        SendMessage sm = SendMessage.builder().chatId(who.toString())
                .parseMode("HTML").text(txt)
                .replyMarkup(kb).build();

        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }

    public void copyMessage(Long who, Integer msgId){
        CopyMessage cm = CopyMessage.builder()
                .fromChatId(who.toString())  //We copy from the user
                .chatId(who.toString())      //And send it back to him
                .messageId(msgId)            //Specifying what message
                .build();
        try {
            execute(cm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void buttonTap(Long id, String queryId, String data, int msgId) throws TelegramApiException {
        System.out.println(data);
        SendMessage newTxt = SendMessage.builder()
                .chatId(id.toString()) //Who are we sending a message to
                .text("").build();    //Message content
//        EditMessageText newTxt = EditMessageText.builder()
//                .chatId(id.toString())
//                .messageId(msgId).text("").build();

//        EditMessageReplyMarkup newKb = EditMessageReplyMarkup.builder()
//                .chatId(id.toString()).messageId(msgId).build();

        if(data.equals("next")) {
            newTxt.setText("Урок 2");
            newTxt.setReplyMarkup(keyboardM2);
        } else if(data.equals("back")) {
            newTxt.setText("Урок 1");
            newTxt.setReplyMarkup(keyboardM1);
        }

        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(queryId).build();

        execute(close);
        execute(newTxt);
//        execute(newKb);
    }
}
