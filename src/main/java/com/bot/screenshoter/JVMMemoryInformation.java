package com.bot.screenshoter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JVMMemoryInformation {

    private Runtime runtime;
    private final int MB = 1024*1024;
    private final int COUNT_MINUTES = 10;

    @Scheduled(fixedRate = COUNT_MINUTES * 60 * 1000)
    public void printInformation() {
        runtime = Runtime.getRuntime();

        log.info("### JVM memory information [MB] ###");
        printUsedMemory();
        printFreeMemory();
        printTotalMemory();
        printMaxMemory();
    }

    private void printUsedMemory() {
        log.info("Used memory: {}", (runtime.totalMemory() - runtime.freeMemory()) / MB);
    }

    private void printFreeMemory() {
        log.info("Free memory: {}", runtime.freeMemory() / MB);
    }

    private void printTotalMemory() {
        log.info("Total memory: {}", runtime.totalMemory() / MB);
    }

    private void printMaxMemory() {
        log.info("Max memory: {}", runtime.maxMemory() / MB);
    }
}
