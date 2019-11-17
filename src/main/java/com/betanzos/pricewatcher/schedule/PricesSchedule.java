/**
 * Copyright 2019 Eduardo E. Betanzos Morales
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.betanzos.pricewatcher.schedule;

import com.betanzos.pricewatcher.domain.PriceWatcherService;
import com.betanzos.pricewatcher.processors.AmazonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author Eduardo Betanzos
 * @since 1.0
 */
@Component
@EnableAsync
public class PricesSchedule {

    @Value("${spring.mail.username}")
    private String mailFrom;
    @Value("${app.notification.mail.to}")
    private String mainTo;

    private static final String MAIL_SUBJECT = "Price Watcher - Amazon price discount notification";
    private static final String MAIL_BODY_TEMPLATE = "It's time to buy, " +
            "the following product has been discounted according to your expectations%n%n" +
            "Product: %s%n" +
            "Expected price: %s%n" +
            "Current price: %s%n%n" +
            "Go to => %s";

    private static NumberFormat formatter = new DecimalFormat("#,000.00");

    @Autowired
    private PriceWatcherService priceWatcherService;
    @Autowired
    private JavaMailSender mailSender;

    @Async
    @Scheduled(fixedRate = 43_200_000, initialDelay = 600_000)//12 hours, 10 minutes
    public void updateWatchedPrices() {
        long initTime = System.currentTimeMillis();
        System.out.println("Staring price update process...");
        System.out.println(LocalDateTime.now());
        System.out.println("---------------------------------------------------------------------------------");
        priceWatcherService.getAll()
                .stream()
                .forEach(p -> {
                    System.out.printf("Updating price...%n    URL: %s%n", p.getUrl());

                    try {
                        var currentPriceInfoDto = AmazonProcessor.process(p.getUrl())
                                .orElseThrow(RuntimeException::new);

                        System.out.printf("    New price: %s%n", currentPriceInfoDto.getCurrentPrice());

                        priceWatcherService.updateWatcherCurrentPrice(p.getId(), currentPriceInfoDto.getCurrentPrice());
                        System.out.printf("    Price updated!!!%n");

                        // Send mail if watched price was reached
                        if (currentPriceInfoDto.getCurrentPrice().compareTo(p.getPriceToWatch()) <= 0) {
                            System.out.println("    Sending mail...");
                            sendMailWatchedPriceReached(
                                    p.getTitle(),
                                    p.getPriceToWatch(),
                                    currentPriceInfoDto.getCurrentPrice(),
                                    p.getUrl()
                            );
                        }
                    } catch (Exception e) {
                        System.out.printf("    Error: %s%n", e.getMessage());
                    }

                    System.out.println();
                });
        long endTime = System.currentTimeMillis();
        System.out.println("---------------------------------------------------------------------------------");
        System.out.println(LocalDateTime.now());
        System.out.println("Finish price update process!!!");
        System.out.printf("Duration: %s%n%n", getDurationStr(initTime, endTime));
    }

    private String getDurationStr(long initMillis, long endMillis) {
        var durationStr = Duration.ofMillis(endMillis - initMillis).toString()
                .replaceAll("PT", "");

        if (durationStr.endsWith(":")) {
            durationStr += "00";
        }

        return durationStr;
    }

    private void sendMailWatchedPriceReached(String product, BigDecimal watchedPrice, BigDecimal currentPrice, String url) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(mainTo);
        message.setSubject(MAIL_SUBJECT);
        message.setText(String.format(
                MAIL_BODY_TEMPLATE,
                product,
                "$ " + formatter.format(watchedPrice.doubleValue()),
                "$ " + formatter.format(currentPrice.doubleValue()),
                url
        ));

        try {
            mailSender.send(message);
            System.out.println("    Sent!!");
        } catch (Exception e) {
            System.out.println("    Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
