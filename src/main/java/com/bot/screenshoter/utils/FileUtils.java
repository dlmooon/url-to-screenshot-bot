package com.bot.screenshoter.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Slf4j
@Component
public class FileUtils {

    public File getFileFromBufferedImage(BufferedImage image, String name) {
        File file = new File(name + ".png");

        log.debug("Attempt to write a screenshot to a file");
        try {
            ImageIO.write(image, "PNG", file);
        } catch (IOException e) {
            log.warn("Failed to write file", e);
        }

        return file;
    }

    public void deleteFile(String name) {
        File file = new File(name);
        if (file.delete()) {
            log.info("File named {} deleted", name);
        }
    }
}
