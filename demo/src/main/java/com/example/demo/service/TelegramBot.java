package com.example.demo.service;

import com.example.demo.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScope;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig bot;
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
                        break;
                case "/help":
                     sendMessage(chatId,HELP_TEXT);
                        break;
                default:
                        sendMessage(chatId,messageText);

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
        try {
            execute(message);
        }catch (TelegramApiException e){
                e.printStackTrace();
        }

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
