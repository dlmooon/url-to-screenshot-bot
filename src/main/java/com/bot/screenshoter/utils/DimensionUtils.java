package com.bot.screenshoter.utils;

import org.openqa.selenium.Dimension;
import org.springframework.stereotype.Component;

@Component
public class DimensionUtils {

    public Dimension getDimension(String text) {
        String[] mas = text.split("[xXхХ]");
        int width = Integer.parseInt(mas[0].trim());
        int height = Integer.parseInt(mas[1].trim());
        return new Dimension(width, height);
    }

    public boolean isCorrectFormatOfDimension(String text) {
        return text.matches("\\b\\d+\\h[xXхХ]\\h\\d+\\b");
    }

    public boolean isCorrectDimension(Dimension dimension) {
        int min = 1;
        int max = 6000;
        return dimension.getWidth() >= min &&
                dimension.getHeight() >= min &&
                dimension.getWidth() <= max &&
                dimension.getHeight() <= max;
    }
}
