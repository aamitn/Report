package com.bitmutex.report.service;

import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.data.Texts;
import com.deepoove.poi.data.style.Style;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TextStyleService {

    public TextRenderData createStyledText(Map<String, Object> textData) {
        if (textData == null || !textData.containsKey("text")) {
            return null;
        }

        String text = (String) textData.get("text");
        @SuppressWarnings("unchecked")
        Map<String, Object> style = (Map<String, Object>) textData.get("style");

        // Start building the text content
        Texts.TextBuilder textBuilder = Texts.of(text);

        if (style != null) {
            // Build a composite style using the builder
            Style.StyleBuilder builder = Style.builder();

            if (Boolean.TRUE.equals(style.get("strike"))) {
                builder = builder.buildStrike();  // Parameterless: adds strike-through
            }
            if (Boolean.TRUE.equals(style.get("bold"))) {
                builder = builder.buildBold();    // Parameterless: adds bold styling
            }
            if (Boolean.TRUE.equals(style.get("italic"))) {
                builder = builder.buildItalic();  // Parameterless: adds italic styling
            }
            if (Boolean.TRUE.equals(style.get("underline"))) {
                builder = builder.buildUnderlinePatterns(UnderlinePatterns.SINGLE); // Apply underline styling
            }
            if (style.get("color") != null) {
                builder = builder.buildColor((String) style.get("color"));
            }
            if (style.get("fontFamily") != null) {
                builder = builder.buildFontFamily((String) style.get("fontFamily"));
            }
            if (style.get("fontSize") != null) {
                builder = builder.buildFontSize((int) style.get("fontSize"));
            }

            // Build the composite style and apply it
            Style compositeStyle = builder.build();
            textBuilder.style(compositeStyle);

            // If a hyperlink is provided, apply it separately.
            if (style.get("link") != null) {
                textBuilder.link((String) style.get("link"));
            }
        }

        return textBuilder.create();
    }
}
