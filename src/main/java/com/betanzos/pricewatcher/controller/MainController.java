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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Eduardo Betanzos
 * @since 1.0
 */
@Controller
public class MainController {

    @Autowired
    private PriceWatcherService priceWatcherService;

    @GetMapping("/")
    private String watchingList(Model model) {
        var pricesList = priceWatcherService.getAll();
        model.addAttribute("pricesList", pricesList);

        return "watcher_list";
    }

    @GetMapping("/add-watcher")
    private String addWatcherPage() {
        return "add_watcher";
    }
}
