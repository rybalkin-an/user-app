package com.github.rybalkin_an.app.user.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StringHelper {

    public static String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
