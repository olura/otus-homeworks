package ru.otus.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;

@ConfigurationProperties(prefix = "app")
public record AppProps(int rightAnswers, Locale locale, String fileName)
        implements FileSourceProvider, LocaleProvider, AnswerSettingProvider {
    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public int getRightAnswer() {
        return rightAnswers;
    }
}
