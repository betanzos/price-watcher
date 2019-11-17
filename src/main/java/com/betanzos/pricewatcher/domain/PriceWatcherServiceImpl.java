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

import com.betanzos.pricewatcher.dto.PriceWatchedDto;
import com.betanzos.pricewatcher.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Eduardo Betanzos
 * @since 1.0
 */
@Service
@Transactional
public class PriceWatcherServiceImpl implements PriceWatcherService {

    private PriceWatchedRepository priceWatchedRepository;

    @Autowired
    public PriceWatcherServiceImpl(PriceWatchedRepository priceWatchedRepository) {
        this.priceWatchedRepository = priceWatchedRepository;
    }

    @Override
    public Long addPriceWatcher(String url, String title, BigDecimal currentPrice, BigDecimal priceToWatch, byte[] image) {
        Objects.requireNonNull(url, "'url' can not be null");
        Objects.requireNonNull(title, "'title' can not be null");
        Objects.requireNonNull(priceToWatch, "'priceToWatch' can not be null");
        Objects.requireNonNull(image, "'image' can not be null");

        // Validar URL
        try {new URL(url);}
        catch (MalformedURLException e) {
            throw new IllegalArgumentException("'url' format ir wrong");
        }

        if (!priceWatchedRepository.findAllByUrl(url).isEmpty()) {
            throw new AppException("100", "Product already exist");
        }

        if (priceToWatch.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("'priceToWatch' must be greater than 0");
        }

        if (currentPrice == null || currentPrice.compareTo(BigDecimal.ZERO) <= 0) {
            currentPrice = new BigDecimal("-1");
        }

        var now = LocalDateTime.now();

        var priceWatched = new PriceWatched()
                .setUrl(url)
                .setTitle(title)
                .setCurrentPrice(currentPrice)
                .setCurrentPriceTime(now)
                .setPriceToWatch(priceToWatch)
                .setAddedTime(now)
                .setImage(image);

        return priceWatchedRepository.save(priceWatched).getId();
    }

    @Override
    public List<PriceWatchedDto> getAll() {
        return priceWatchedRepository.findAllByOrderByAddedTimeDesc()
                .stream()
                .map(PriceWatcherServiceImpl::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void removeWatcher(Long id) {
        Objects.requireNonNull(id, "'id' can not be null");

        var priceWatched = priceWatchedRepository.findById(id)
                .orElseThrow(() -> new AppException("101", "Price watcher not found"));

        priceWatchedRepository.delete(priceWatched);
    }

    private static PriceWatchedDto entityToDto(PriceWatched entity) {
        return new PriceWatchedDto()
                .setId(entity.getId())
                .setUrl(entity.getUrl())
                .setTitle(entity.getTitle())
                .setCurrentPrice(entity.getCurrentPrice())
                .setCurrentPriceTime(entity.getCurrentPriceTime())
                .setPriceToWatch(entity.getPriceToWatch())
                .setAddedTime(entity.getAddedTime())
                .setImageBase64(Base64.getEncoder().encodeToString(entity.getImage()));
    }

    @Override
    public void updateWatcherCurrentPrice(Long id, BigDecimal currentPrice) {
        Objects.requireNonNull(id, "'id' can not be null");

        var priceWatched = priceWatchedRepository.findById(id)
                .orElseThrow(() -> new AppException("101", "Price watcher not found"));

        if (currentPrice == null || currentPrice.compareTo(BigDecimal.ZERO) <= 0) {
            currentPrice = new BigDecimal("-1");
        }

        priceWatched.setCurrentPrice(currentPrice);
        priceWatched.setCurrentPriceTime(LocalDateTime.now());
    }
}
