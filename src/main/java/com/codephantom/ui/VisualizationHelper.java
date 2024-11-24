package com.codephantom.ui;

import org.knowm.xchart.*;

import java.util.List;

public class VisualizationHelper {
    public static void generateBarChart(String title, List<String> categories, List<Integer> values) {
        CategoryChart chart = new CategoryChartBuilder()
                .title(title)
                .xAxisTitle("Category")
                .yAxisTitle("Value")
                .build();

        chart.addSeries("Data", categories, values);

        try {
            BitmapEncoder.saveBitmap(chart, "BarChart", BitmapEncoder.BitmapFormat.PNG);
            System.out.println("Bar chart saved as BarChart.png");
        } catch (Exception e) {
            System.err.println("Error generating bar chart: " + e.getMessage());
        }
    }
}
