package com.example.production.service;


import com.example.production.model.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
public class ExportPdf {
    private static PdfPCell date(Date startDate, Date endDate) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");

        // Создание строки с датой с какого по какое
        String dateRange = "Report Period: " + dateFormatter.format(startDate) + " - " + dateFormatter.format(endDate);
        PdfPCell dateRangeCell = new PdfPCell(new Phrase(dateRange));
        dateRangeCell.setBorder(Rectangle.NO_BORDER);
        dateRangeCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        return dateRangeCell;
    }

    private static PdfPTable fullNameTable(String[] detailsArray) {
        String fullName = detailsArray[0].trim();
        String position = detailsArray[1].trim();

        PdfPCell fullNameCell = new PdfPCell(new Phrase("\nName: " + fullName + "                                     Signature: _________________"));
        fullNameCell.setBorder(Rectangle.NO_BORDER);
        fullNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell positionCell = new PdfPCell(new Phrase("\nPosition: " + position));
        positionCell.setBorder(Rectangle.NO_BORDER);
        positionCell.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPTable footerTable = new PdfPTable(1);
        footerTable.setWidthPercentage(100);
        footerTable.addCell(fullNameCell);
        footerTable.addCell(positionCell);

        return footerTable;
    }

    private static void newCell(PdfPTable dataTable, String phrase) {
        PdfPCell cell = new PdfPCell(new Phrase(phrase));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        dataTable.addCell(cell);
    }

    public static ByteArrayInputStream productProductionReport(List<ProductProduction> productions, String[] details, Date startDate, Date endDate, float total) {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(100);
            PdfPCell dateRangeCell = date(startDate, endDate);
            headerTable.addCell(dateRangeCell);

            // Добавление строки с датой в документ
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(headerTable);

            // Добавление таблицы с данными
            PdfPTable dataTable = new PdfPTable(4);
            dataTable.setWidthPercentage(100);
            dataTable.setWidths(new int[]{5, 3, 4, 5});

            // Заголовки столбцов
            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            String[] columnNames = {"Product", "Quantity", "Date", "Employee"};
            for (String columnName : columnNames) {
                PdfPCell hcell = new PdfPCell(new Phrase(columnName, headFont));
                hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                dataTable.addCell(hcell);
            }

            // Добавление данных о продукции в таблицу
            for (ProductProduction productionDto : productions) {

                newCell(dataTable, productionDto.getProduct().getName());
                newCell(dataTable, String.valueOf(productionDto.getQuantity()));
                newCell(dataTable, String.valueOf(productionDto.getDate()));
                newCell(dataTable, productionDto.getEmployee().getFullName());
            }

            newCell(dataTable, "Total:");
            newCell(dataTable, String.valueOf(total));

            for (int i = 0; i < 3; i++) {
                PdfPCell emptyCell = new PdfPCell();
                dataTable.addCell(emptyCell);
            }
            document.add(dataTable);

            PdfPTable footerTable = fullNameTable(details);
            document.add(footerTable);

            document.close();

        } catch (DocumentException ex) {
            ex.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    public static ByteArrayInputStream productSaleReport(List<ProductSale> sales, String[] details, Date startDate, Date endDate, float totalQuantity, float totalAmount) {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(100);
            PdfPCell dateRangeCell = date(startDate, endDate);
            headerTable.addCell(dateRangeCell);

            // Добавление строки с датой в документ
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(headerTable);

            // Добавление таблицы с данными
            PdfPTable dataTable = new PdfPTable(5);
            dataTable.setWidthPercentage(100);
            dataTable.setWidths(new int[]{5, 3, 4, 4, 5});

            // Заголовки столбцов
            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            String[] columnNames = {"Product", "Quantity", "Amount", "Date", "Employee"};
            for (String columnName : columnNames) {
                PdfPCell hcell = new PdfPCell(new Phrase(columnName, headFont));
                hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                dataTable.addCell(hcell);
            }

            // Добавление данных о продукции в таблицу
            for (ProductSale productSale : sales) {

                newCell(dataTable, productSale.getProduct().getName());
                newCell(dataTable, String.valueOf(productSale.getQuantity()));
                newCell(dataTable, String.valueOf(productSale.getAmount()));
                newCell(dataTable, String.valueOf(productSale.getDate()));
                newCell(dataTable, productSale.getEmployee().getFullName());
            }
            newCell(dataTable,"Total:");
            newCell(dataTable, String.valueOf(totalQuantity));
            newCell(dataTable, String.valueOf(totalAmount));

            for (int i = 0; i < 2; i++) {
                PdfPCell emptyCell = new PdfPCell();
                dataTable.addCell(emptyCell);
            }
            document.add(dataTable);

            PdfPTable footerTable = fullNameTable(details);
            document.add(footerTable);


            document.close();

        } catch (DocumentException ex) {
            ex.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    public static ByteArrayInputStream materialPurchasesReport(List<RawMaterialPurchase> purchases, String[] details, Date startDate, Date endDate, float totalQuantity, float totalAmount) {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(100);
            PdfPCell dateRangeCell = date(startDate, endDate);
            headerTable.addCell(dateRangeCell);

            // Добавление строки с датой в документ
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(headerTable);

            // Добавление таблицы с данными
            PdfPTable dataTable = new PdfPTable(5);
            dataTable.setWidthPercentage(100);
            dataTable.setWidths(new int[]{5, 3, 4, 4, 5});

            // Заголовки столбцов
            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            String[] columnNames = {"Raw material", "Quantity", "Amount", "Date", "Employee"};
            for (String columnName : columnNames) {
                PdfPCell hcell = new PdfPCell(new Phrase(columnName, headFont));
                hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                dataTable.addCell(hcell);
            }

            // Добавление данных о продукции в таблицу
            for (RawMaterialPurchase materialPurchase : purchases) {

                newCell(dataTable, materialPurchase.getRawMaterial().getName());
                newCell(dataTable, String.valueOf(materialPurchase.getQuantity()));
                newCell(dataTable, String.valueOf(materialPurchase.getAmount()));
                newCell(dataTable, String.valueOf(materialPurchase.getDate()));
                newCell(dataTable, materialPurchase.getEmployee().getFullName());
            }

            newCell(dataTable,"Total:");
            newCell(dataTable, String.valueOf(totalQuantity));
            newCell(dataTable, String.valueOf(totalAmount));

            for (int i = 0; i < 2; i++) {
                PdfPCell emptyCell = new PdfPCell();
                dataTable.addCell(emptyCell);
            }

            document.add(dataTable);

            PdfPTable footerTable = fullNameTable(details);
            document.add(footerTable);

            document.close();

        } catch (DocumentException ex) {
            ex.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    public static ByteArrayInputStream banksReport(List<Bank> banks, String[] details, Date startDate, Date endDate, float total) {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(100);
            PdfPCell dateRangeCell = date(startDate, endDate);
            headerTable.addCell(dateRangeCell);

            // Добавление строки с датой в документ
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(headerTable);

            // Добавление таблицы с данными
            PdfPTable dataTable = new PdfPTable(6);
            dataTable.setWidthPercentage(100);
            dataTable.setWidths(new int[]{3, 3, 3, 3, 4, 4});

            // Заголовки столбцов
            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            String[] columnNames = {"Receipt date", "Sum", "Percent", "Fine", "Month", "Is Paid"};
            for (String columnName : columnNames) {
                PdfPCell hcell = new PdfPCell(new Phrase(columnName, headFont));
                hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                dataTable.addCell(hcell);
            }

            // Добавление данных о продукции в таблицу
            for (Bank bank : banks) {
                newCell(dataTable, String.valueOf(bank.getReceiptDate()));
                newCell(dataTable, String.valueOf(bank.getSum()));
                newCell(dataTable, String.valueOf(bank.getPercent()));
                newCell(dataTable, String.valueOf(bank.getFine()));
                newCell(dataTable, String.valueOf(bank.getMonth()));
                newCell(dataTable, String.valueOf(bank.isPaid()));
            }
            newCell(dataTable,"Total:");
            newCell(dataTable, String.valueOf(total));

            for (int i = 0; i < 4; i++) {
                PdfPCell emptyCell = new PdfPCell();
                dataTable.addCell(emptyCell);
            }

            document.add(dataTable);

            PdfPTable footerTable = fullNameTable(details);
            document.add(footerTable);

            document.close();

        } catch (DocumentException ex) {
            ex.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    public static ByteArrayInputStream salariesReport(List<Salary> salaries, String[] details, Date startDate, Date endDate, float total) {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(100);
            PdfPCell dateRangeCell = date(startDate, endDate);
            headerTable.addCell(dateRangeCell);

            // Добавление строки с датой в документ
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(headerTable);

            // Добавление таблицы с данными
            PdfPTable dataTable = new PdfPTable(7);
            dataTable.setWidthPercentage(100);
            dataTable.setWidths(new int[]{4, 4, 3, 4, 4, 3, 3});

            // Заголовки столбцов
            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            String[] columnNames = {"Employee", "Date", "common", "salary", "bonus", "general", "issued"};
            for (String columnName : columnNames) {
                PdfPCell hcell = new PdfPCell(new Phrase(columnName, headFont));
                hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                dataTable.addCell(hcell);
            }

            // Добавление данных о продукции в таблицу
            for (Salary salary : salaries) {
                newCell(dataTable, String.valueOf(salary.getEmployee().getFullName()));
                newCell(dataTable,salary.getYear() + "-" + salary.getMonth());
                newCell(dataTable, String.valueOf(salary.getCommon()));
                newCell(dataTable, String.valueOf(salary.getSalary()));
                newCell(dataTable, String.valueOf(salary.getBonus()));
                newCell(dataTable, String.valueOf(salary.getGeneral()));
                newCell(dataTable, String.valueOf(salary.isIssued()));
            }
            newCell(dataTable, "Total:");

            for (int i = 0; i < 4; i++) {
                PdfPCell emptyCell = new PdfPCell();
                dataTable.addCell(emptyCell);
            }

            newCell(dataTable, String.valueOf(total));

            PdfPCell emptyCell = new PdfPCell();
            dataTable.addCell(emptyCell);

            document.add(dataTable);

            PdfPTable footerTable = fullNameTable(details);
            document.add(footerTable);

            document.close();

        } catch (DocumentException ex) {
            ex.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}