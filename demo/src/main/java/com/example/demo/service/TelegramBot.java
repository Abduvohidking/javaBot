package com.example.demo.service;

import com.example.demo.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScope;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig bot;
    List <String> answers = new ArrayList<>();
    final String HELP_TEXT = "Botni ishga tushirish uchun /start \n"+" Yordam uchun /help";

    public TelegramBot(BotConfig bot) throws TelegramApiException {
        this.bot = bot;
        List<BotCommand> commandList = new ArrayList<>();
        commandList.add(new BotCommand("/start","Botni ishga tushiradi"));
        commandList.add(new BotCommand("/help","yordam kerakmi ?"));
        this.execute(new SetMyCommands(commandList, new BotCommandScopeDefault(),null));
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case "/start":
                        startCommandReceive(chatId,update.getMessage().getChat().getFirstName());
                    try {
                        test_start(chatId);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "/help":
                     sendMessage(chatId,HELP_TEXT);
                        break;
                default:
                        sendMessage(chatId,messageText);

            }
        } else if (update.hasCallbackQuery()) {
            String callBackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            answers.add(callBackData);
            try {
                execute(new DeleteMessage(String.valueOf(chatId),(int)messageId));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            try {
                test_start(chatId);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

        }


    }

    private void startCommandReceive(Long chatId,String name)  {
        String answer = "Salom "+name +" hush kelibsiz !!!";
        sendMessage(chatId,answer);

    }
    private void sendMessage(Long chatId, String sendText)  {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(sendText);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Toshkent");
        row.add("Buxoro");
        keyboardRows.add(row);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(replyKeyboardMarkup);
        try {
            execute(message);
        }catch (TelegramApiException e){
                e.printStackTrace();
        }

    }
    void test_start(Long chatId) throws TelegramApiException {


        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("So’rovnoma");
        execute(message);
        message.setText("So’rovnomaning maqsadi ko’rish bo’yicha nogironligi bo’lgan shaxslarning  ishsizlik, qulay mehnat sharoitlari, oylik daramodlari ehtiyojlarni qondirishga yetarlilik darajasini aniqlash, xalqaro tashkilotlar e’tirof etgan huquq va imtiyozlarni ro’yobga chiqarish uchun hukumatga taklif va tavsiyalar bilan ilmiy ish orqali murojat qilish");
        execute(message);
        message.setText("Yoshingiz?");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline4 = new ArrayList<>();
        var button = new InlineKeyboardButton();
        button.setText("18 dan kichkina");
        button.setCallbackData("YOUNG");
        rowInline.add(button);
        var button1 = new InlineKeyboardButton();
        button1.setText("18 dan - 29 gacha");
        button1.setCallbackData("YOUNG+");
        rowInline1.add(button1);
        var button2 = new InlineKeyboardButton();
        button2.setText("30 dan - 44 gacha");
        button2.setCallbackData("YOUNG++");
        rowInline2.add(button2);
        var button3 = new InlineKeyboardButton();
        button3.setText("45 dan - 59 gacha");
        button3.setCallbackData("YOUNG+++");
        rowInline3.add(button3);
        var button4 = new InlineKeyboardButton();
        button4.setText("60 dan yuqori");
        button4.setCallbackData("YOUNG++++");
        rowInline4.add(button4);

        rowsInline.add(rowInline);
        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);
        rowsInline.add(rowInline3);
        rowsInline.add(rowInline4);

        markupInline.setKeyboard(rowsInline);

        message.setReplyMarkup(markupInline);

        execute(message);


    }

    @Override
    public String getBotToken() {
        return bot.getBotToken();
    }

    @Override
    public String getBotUsername() {
        return bot.getBotName();
    }

}
