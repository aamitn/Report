package com.bitmutex.report.service;

import com.bitmutex.report.entity.Template;
import com.bitmutex.report.repository.TemplateRepository;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class ReportService {

    private final TextStyleService textStyleService;
    private final ImageStyleService imageStyleService;
    private final TableRenderService tableRenderService;
    private final TemplateRepository templateRepository;

    public ReportService(
            TextStyleService textStyleService,
            ImageStyleService imageStyleService,
            TableRenderService tableRenderService,
            TemplateRepository templateRepository) {
        this.textStyleService = textStyleService;
        this.imageStyleService = imageStyleService;
        this.tableRenderService = tableRenderService;
        this.templateRepository = templateRepository;
    }

    @Transactional
    public ByteArrayOutputStream generateReport(Map<String, Object> requestData) throws IOException {
        String templateFilename = requestData.containsKey("template") ?
                (String) requestData.get("template") : "default.docx";
        requestData.remove("template");

        Optional<Template> templateOptional = templateRepository.findByFilename(templateFilename);
        if (templateOptional.isEmpty()) {
            throw new RuntimeException("Template not found in database: " + templateFilename);
        }

        Template templateEntity = templateOptional.get();
        byte[] templateBytes = templateEntity.getFileData();
        if (templateBytes == null || templateBytes.length == 0) {
            throw new RuntimeException("Template file is empty!");
        }

        XWPFTemplate template = XWPFTemplate.compile(new ByteArrayInputStream(templateBytes))
                .render(processData(requestData));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        template.write(outputStream);
        template.close();

        return outputStream;
    }

    private Map<String, Object> processData(Map<String, Object> requestData) {
        Map<String, Object> processedData = new HashMap<>();

        for (Map.Entry<String, Object> entry : requestData.entrySet()) {
            Object value = entry.getValue();

            if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> dataMap = (Map<String, Object>) value;

                if (dataMap.containsKey("text")) {
                    TextRenderData styledText = textStyleService.createStyledText(dataMap);
                    processedData.put(entry.getKey(), styledText);
                } else if (dataMap.containsKey("path") || dataMap.containsKey("url")
                        || dataMap.containsKey("stream") || dataMap.containsKey("buffered")) {
                    try {
                        PictureRenderData pictureData = imageStyleService.createPicture(dataMap);
                        processedData.put(entry.getKey(), pictureData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (dataMap.containsKey("table")) {
                    try {
                        TableRenderData tableData = tableRenderService.createTable(dataMap.get("table"));
                        processedData.put(entry.getKey(), tableData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    processedData.put(entry.getKey(), value);
                }
            } else {
                processedData.put(entry.getKey(), value);
            }
        }

        return processedData;
    }
}
