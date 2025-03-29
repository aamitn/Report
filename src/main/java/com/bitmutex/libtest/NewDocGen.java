package com.bitmutex.libtest;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class NewDocGen {

    public static void main(String[] args) throws Exception {

        String templatePath =  "E:\\ReportRepository\\Templates\\template.docx"; // (For Windows Use)
       // String templatePath =  "/mnt/sda1/ReportRepository/Templates/template.docx" ; // (For Unix Use)

        String outputPath =  "E:\\ReportRepository\\Reports\\output.docx"; // (For Windows Use)
      // String outputPath =  "E:\\ReportRepository\\Reports\\output.docx";  // (For Unix Use)

        String imagePath = "E:\\ReportRepository\\Images\\myImage.png"; // (For Windows Use)
        //String imagePath = "/mnt/sda1/ReportRepository/Images/myImage.png"; // (For Unix Use)

        String imageUrlPath = "https://files.worldwildlife.org/wwfcmsprod/images/African_elephant_YE2021_Karine_Aigner_5kzx389mvt/magazine_small/1s803ne5x2_elephantv2.jpg";

        // Define the map for data
        Map<String, Object> data = new HashMap<>();

        //Dynamic text
        data.put("title", "MY TITLE");

        // Local image
        data.put("image", imagePath);

        // URL image
        data.put("imageUrl", imageUrlPath);

        // Local image with custom size
        data.put("image1", Pictures.ofLocal(imagePath).size(200, 150).create()); // Adjust size as needed

        // URL image with custom size
        data.put("image1Url", Pictures.ofUrl(imageUrlPath).size(100, 100).create());

        //Dynamic Buffered Image
        BufferedImage bufferedImage = generateDynamicImage();

        // Insert the dynamic BufferedImage into the data map
        data.put("imageBuffered", Pictures.ofBufferedImage (bufferedImage, PictureType.PNG).size(100, 100).create() );


        //Table
        RowRenderData row0 = Rows.of("Event", "Date").textColor("FFFFFF").bgColor("4472C4").center().create();
        RowRenderData row1 = Rows.create("Creation", "02101996");
        data.put("myTable", Tables.create(row0, row1));

        // Compile and render the template
        XWPFTemplate template = XWPFTemplate.compile(templatePath).render(data);

        // Write and close the output
        template.writeAndClose(new FileOutputStream(outputPath));
    }


    private static BufferedImage generateDynamicImage() {
        // Create a BufferedImage with specified width and height
        BufferedImage bufferedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.getGraphics();

        // Set background color
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 200, 200);

        // Draw a simple rectangle (you can customize this to draw whatever you need)
        g.setColor(Color.BLUE);
        g.fillRect(20, 20, 160, 160);

        // Dispose of graphics context to release resources
        g.dispose();

        return bufferedImage;
    }
}