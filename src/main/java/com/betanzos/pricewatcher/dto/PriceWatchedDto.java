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
package com.betanzos.pricewatcher.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Eduardo Betanzos
 * @since 1.0
 */
public class PriceWatchedDto {

    private Long id;
    private String url;
    private String title;
    private BigDecimal currentPrice;
    private LocalDateTime currentPriceTime;
    private BigDecimal priceToWatch;
    private LocalDateTime addedTime;
    private String imageBase64;

    public PriceWatchedDto() {}

    public Long getId() {
        return id;
    }

    public PriceWatchedDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public PriceWatchedDto setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PriceWatchedDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public PriceWatchedDto setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
        return this;
    }

    public LocalDateTime getCurrentPriceTime() {
        return currentPriceTime;
    }

    public PriceWatchedDto setCurrentPriceTime(LocalDateTime currentPriceTime) {
        this.currentPriceTime = currentPriceTime;
        return this;
    }

    public BigDecimal getPriceToWatch() {
        return priceToWatch;
    }

    public PriceWatchedDto setPriceToWatch(BigDecimal priceToWatch) {
        this.priceToWatch = priceToWatch;
        return this;
    }

    public LocalDateTime getAddedTime() {
        return addedTime;
    }

    public PriceWatchedDto setAddedTime(LocalDateTime addedTime) {
        this.addedTime = addedTime;
        return this;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public PriceWatchedDto setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
        return this;
    }
}
