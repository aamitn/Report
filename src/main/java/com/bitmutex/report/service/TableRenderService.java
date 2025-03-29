package com.bitmutex.report.service;

import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.TableRenderData;
import com.deepoove.poi.data.Tables;
import com.deepoove.poi.data.Rows;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TableRenderService {

    public TableRenderData createTable(Object tableData) {
        if (!(tableData instanceof List)) {
            throw new IllegalArgumentException("Invalid table data format");
        }

        @SuppressWarnings("unchecked")
        List<List<String>> tableRows = (List<List<String>>) tableData;

        if (tableRows.isEmpty()) {
            return Tables.create(); // Return an empty table
        }

        // Convert each list of strings into RowRenderData
        List<RowRenderData> rowDataList = new ArrayList<>();
        for (List<String> row : tableRows) {
            rowDataList.add(Rows.of(row.toArray(new String[0])).create());
        }

        // Convert List to an array and pass as varargs
        return Tables.create(rowDataList.toArray(new RowRenderData[0]));
    }
}
