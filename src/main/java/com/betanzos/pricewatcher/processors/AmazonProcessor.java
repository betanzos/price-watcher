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
package com.betanzos.pricewatcher.processors;

import com.betanzos.pricewatcher.dto.CurrentPriceInfoDto;
import com.betanzos.pricewatcher.exception.AppException;
import com.betanzos.pricewatcher.utils.Util;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Optional;

/**
 * @author Eduardo Betanzos
 * @since 1.0
 */
public final class AmazonProcessor {

    private AmazonProcessor() {}

    public static Optional<CurrentPriceInfoDto> process(String url) {
        try {
            if (!new URI(url).getHost().startsWith("www.amazon.")) {
                throw new AppException("200", "Unsupported site URL");
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("'url' wrong format", e);
        } catch (AppException e) {
            throw e;
        }

        return downloadUrlContent(URI.create(url))
                .map(AmazonProcessor::parsePageContent)
                .orElseGet(Optional::empty);
    }

    public static Optional<BigDecimal> getCurrentPrice(String url) {
        return downloadUrlContent(URI.create(url))
                .map(AmazonProcessor::parsePageContentNoImages)
                .orElseGet(() -> Optional.of(new CurrentPriceInfoDto()))
                .map(CurrentPriceInfoDto::getCurrentPrice);
    }

    private static Optional<String> downloadUrlContent(URI uri) {
        var httpClient = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(uri)
                .GET()
                .headers(
                        "User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:68.0) Gecko/20100101 Firefox/68.0",
                        "Accept", "text/html"
                )
                .build();

        try {
            var body = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
            return Optional.ofNullable(body);
        } catch (IOException | InterruptedException e) {
            return Optional.empty();
        }
    }

    private static Optional<CurrentPriceInfoDto> parsePageContent(String page) {
        return parsePageContent(page, true);
    }

    private static Optional<CurrentPriceInfoDto> parsePageContentNoImages(String page) {
        return parsePageContent(page, false);
    }

    private static Optional<CurrentPriceInfoDto> parsePageContent(String page, boolean downloadImage) {
        try {
            var bodyElement = Jsoup.parse(page).body();

            String title = bodyElement.getElementById("productTitle").text();

            var priceElement = bodyElement.getElementById("priceblock_ourprice");
            if (priceElement == null) {
                priceElement = bodyElement.getElementById("priceblock_saleprice");

                if (priceElement == null) {
                    priceElement = bodyElement.getElementById("priceblock_dealprice");

                    if (priceElement == null) {
                        priceElement = bodyElement.getElementsByClass("a-size-medium a-color-price offer-price a-text-normal")
                                .tagName("span")
                                .first();
                    }
                }
            }
            var priceStr = priceElement.text()
                    .replaceAll("\\$", "")
                    .replaceAll(",", "");
            var price = new BigDecimal(priceStr);

            byte[] imageData = null;
            if (downloadImage) {
                var imageSrc = "";
                var imageElement = bodyElement.getElementsByClass("imgTagWrapper").first();

                if (imageElement != null) {
                    imageSrc = imageElement.children().tagName("img").attr("data-old-hires");
                } else {
                    imageElement = bodyElement.getElementById("imgBlkFront");
                    imageSrc = imageElement != null ? imageElement.attr("src") : "";
                }

                if (imageSrc != null) {
                    imageSrc = imageSrc.strip();
                }

                if (imageSrc.startsWith("data:image")) {
                    imageData = Base64.getDecoder()
                            .decode(extractContentFromHtmlBase64Image(imageSrc));
                } else {
                    imageData = downloadImage(imageSrc).orElseGet(() -> null);
                }
            }

            if (imageData.length > 500_000) {
                imageData = Util.scaleImagePreservingAspectRatio(imageData, 500, 500);
            }

            var dto = new CurrentPriceInfoDto()
                    .setTitle(title)
                    .setCurrentPrice(price)
                    .setImage(imageData);

            return Optional.of(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private static String extractContentFromHtmlBase64Image(String imageSrc) {
        var base64Index = imageSrc.indexOf("base64");
        return base64Index != -1 ? imageSrc.substring(base64Index + 7) : imageSrc;
    }

    private static Optional<byte[]> downloadImage(String url) {
        var request = HttpRequest.newBuilder(URI.create(url)).GET().build();
        var httpClient = HttpClient.newHttpClient();
        try {
            byte[] imageData = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray()).body();
            return Optional.ofNullable(imageData);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
