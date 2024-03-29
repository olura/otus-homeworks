package ru.otus.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.otus.configuration.LocaleProvider;

import java.util.Locale;

@Component
public class LocalizationService {
    private final MessageSource messageSource;

    private final Locale locale;

    public LocalizationService(MessageSource messageSource, LocaleProvider localeProvider) {
        this.messageSource = messageSource;
        this.locale = localeProvider.getLocale();
    }

    public String getMessage(String message, String ...args) {
        return messageSource.getMessage(message, args, locale);
    }

}
