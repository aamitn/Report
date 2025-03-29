package com.bitmutex.report.service;

import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.Pictures;
import com.deepoove.poi.data.PictureType;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;

@Service
public class ImageStyleService {

    /**
     * Creates PictureRenderData from the given pictureData.
     * Supports:
     * - Option 1: Local image ("path")
     * - Option 2: URL image ("url")
     * - Option 3: Image stream ("stream")
     * - Option 4: Buffered image provided as a Base64 string ("buffered")
     */
    public PictureRenderData createPicture(Map<String, Object> pictureData) throws Exception {
        if (pictureData == null) {
            return null;
        }
        int width = pictureData.get("width") != null ? (int) pictureData.get("width") : 100;
        int height = pictureData.get("height") != null ? (int) pictureData.get("height") : 100;

        // Option 1: Local image
        if (pictureData.containsKey("path")) {
            String path = (String) pictureData.get("path");
            return Pictures.ofLocal(path).size(width, height).create();
        }
        // Option 2: URL image
        else if (pictureData.containsKey("url")) {
            String url = (String) pictureData.get("url");
            return Pictures.ofUrl(url).size(width, height).create();
        }
        // Option 3: Image stream
        else if (pictureData.containsKey("stream")) {
            String filePath = (String) pictureData.get("stream");
            InputStream is = new FileInputStream(filePath);
            return Pictures.ofStream(is, PictureType.JPEG).size(width, height).create();
        }
        // Option 4: Buffered image provided as Base64 encoded string
        else if (pictureData.containsKey("buffered")) {
            String base64Image = (String) pictureData.get("buffered");
            BufferedImage bufferedImage = decodeToBufferedImage(base64Image);
            return Pictures.ofBufferedImage(bufferedImage, PictureType.PNG).size(width, height).create();
        }
        return null;
    }

    /**
     * Decodes a Base64-encoded image string to a BufferedImage.
     * It supports data URI schemes, e.g., "data:image/png;base64,..."
     */
    private BufferedImage decodeToBufferedImage(String base64Image) throws Exception {
        // Remove data URI prefix if present
        if (base64Image.startsWith("data:image")) {
            base64Image = base64Image.substring(base64Image.indexOf(",") + 1);
        }
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        BufferedImage bufferedImage = ImageIO.read(bis);
        bis.close();
        return bufferedImage;
    }
}
