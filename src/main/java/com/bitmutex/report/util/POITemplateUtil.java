package com.bitmutex.report.util;

import com.deepoove.poi.data.Pictures;
import com.deepoove.poi.data.PictureRenderData;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import javax.imageio.ImageIO;

public class POITemplateUtil {

    public static PictureRenderData getImageFromUrl(String imageUrl, int width, int height) throws Exception {
        URL url = new URL(imageUrl);
        BufferedImage image = ImageIO.read(url.openStream());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);

        return Pictures.ofBytes(outputStream.toByteArray()).size(width, height).create();
    }
}
