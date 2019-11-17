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
package com.betanzos.pricewatcher.controller;

import com.betanzos.pricewatcher.domain.PriceWatcherService;
import com.betanzos.pricewatcher.dto.UrlPriceDto;
import com.betanzos.pricewatcher.processors.AmazonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Eduardo Betanzos
 * @since 1.0
 */
@RestController
@RequestMapping("/api")
public class APiController {

    @Autowired
    private PriceWatcherService priceWatcherService;

    @PostMapping("/watchers")
    private void saveWatcher(@RequestBody UrlPriceDto data) {
        var currentPriceInfoDto = AmazonProcessor.process(data.getUrl())
                .orElseThrow(RuntimeException::new);

        priceWatcherService.addPriceWatcher(
                data.getUrl(),
                currentPriceInfoDto.getTitle(),
                currentPriceInfoDto.getCurrentPrice(),
                data.getPrice(),
                currentPriceInfoDto.getImage()
        );
    }

    @DeleteMapping("/watchers/{id}")
    private void removeWatcher(@PathVariable Long id) {
        priceWatcherService.removeWatcher(id);
    }
}
