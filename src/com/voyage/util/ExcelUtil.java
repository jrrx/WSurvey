package com.voyage.util;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * The utility class for excel operation.
 * 
 * @author Houyangyang
 */
public class ExcelUtil {

	public static ExcelUtil it() {
		return new ExcelUtil();
	}

	public Workbook getResultExcelDefault() {
		Workbook wb = null;
		try {
			File file = FileFinder
					.findFile("resources/excel/BasicTemplate.xlsx");
			wb = new XSSFWorkbook(new FileInputStream(file));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wb;
	}

	public Row getRow(Sheet st, int rowNum) {
		Row row = st.getRow(rowNum);
		return row != null ? row : st.createRow(rowNum);
	}

	public Cell getCell(Row row, int cellNum) {
		Cell cell = row.getCell(cellNum);
		return cell != null ? cell : row.createCell(cellNum);
	}

}
