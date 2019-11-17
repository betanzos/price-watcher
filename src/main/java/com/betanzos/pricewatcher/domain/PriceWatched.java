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
package com.betanzos.pricewatcher.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Eduardo Betanzos
 * @since 1.0
 */
@Entity
public class PriceWatched {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 2000)
    private String url;
    @Column(nullable = false, length = 1000)
    private String title;
    @Column(nullable = false)
    private BigDecimal currentPrice;
    @Column(nullable = false)
    private LocalDateTime currentPriceTime;
    @Column(nullable = false)
    private BigDecimal priceToWatch;
    @Column(nullable = false)
    private LocalDateTime addedTime;
    @Column(nullable = false, length = 500_000)
    private byte[] image;

    public Long getId() {
        return id;
    }

    public PriceWatched setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public PriceWatched setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PriceWatched setTitle(String title) {
        this.title = title;
        return this;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public PriceWatched setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
        return this;
    }

    public LocalDateTime getCurrentPriceTime() {
        return currentPriceTime;
    }

    public PriceWatched setCurrentPriceTime(LocalDateTime currentPriceTime) {
        this.currentPriceTime = currentPriceTime;
        return this;
    }

    public BigDecimal getPriceToWatch() {
        return priceToWatch;
    }

    public PriceWatched setPriceToWatch(BigDecimal priceToWatch) {
        this.priceToWatch = priceToWatch;
        return this;
    }

    public LocalDateTime getAddedTime() {
        return addedTime;
    }

    public PriceWatched setAddedTime(LocalDateTime addedTime) {
        this.addedTime = addedTime;
        return this;
    }

    public byte[] getImage() {
        return image;
    }

    public PriceWatched setImage(byte[] image) {
        this.image = image;
        return this;
    }
}
