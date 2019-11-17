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

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Eduardo Betanzos
 * @since 1.0
 */
public interface PriceWatcherService {
    Long addPriceWatcher(String url, String title, BigDecimal currentPrice, BigDecimal priceToWatch, byte[] image);
    List<PriceWatchedDto> getAll();
    void removeWatcher(Long id);
    void updateWatcherCurrentPrice(Long id, BigDecimal currentPrice);
}
