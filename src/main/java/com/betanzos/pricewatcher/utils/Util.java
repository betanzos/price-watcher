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
package com.betanzos.pricewatcher.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Eduardo Betanzos
 * @since 1.0
 */
public final class Util {

    private Util() {}

    /**
     * Permite obtener una imagen escalada a partir de {@code imageData} cuyas nuevas dimensiones
     * serán {@code newWith} y {@code newHeight}. Esta imagen es creada manteniendo su "aspect ratio"
     * original, por lo que no se deformará.
     *
     * @param imageData La imagen original
     * @param newWidth Nuevo ancho
     * @param newHeight Nueva altura
     *
     * @return Imagen escalada
     */
    public static byte[] scaleImagePreservingAspectRatio(byte[] imageData, int newWidth, int newHeight) throws IOException {
        var is = new ByteArrayInputStream(imageData);
        var bufferedImage = ImageIO.read(is);

        var scaledImage = bufferedImage.getScaledInstance(-1, newHeight, Image.SCALE_FAST);
        if ((new ImageIcon(scaledImage)).getIconWidth() > newWidth) {
            scaledImage = scaledImage.getScaledInstance(newWidth, -1, Image.SCALE_FAST);
        }

        var scaledImageIcon = new ImageIcon(scaledImage);
        var width = scaledImageIcon.getIconWidth();
        var height = scaledImageIcon.getIconHeight();
        var newBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        var g2d = newBufferedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        var baos = new ByteArrayOutputStream();
        ImageIO.write(newBufferedImage, "png", baos);

        return baos.toByteArray();
    }
}
