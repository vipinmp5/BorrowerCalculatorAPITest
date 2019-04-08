package com.lendico.utils;

import java.io.File;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.log4j.Logger;

public class TestDataReader {
	private static final String EXCEL_DATA_SHEET_FOLDER="\\Resources\\testdata";
    private static final Logger log = Logger.getLogger(TestDataReader.class);
    static String environment="";
    
    
    /**
     * To read data for test from an excel sheet
     * @param tableName
     * @return
     */
    public static String[][] getDataFromExcelFile(String testClassName,String tableName)
    {
        String[][] tabArray = null;
        try
        {
        	String userDirectory=System.getProperty("user.dir");
        	String filepath=userDirectory+EXCEL_DATA_SHEET_FOLDER+"\\"+testClassName+".xls";
        	Workbook workbook = Workbook.getWorkbook(new File(filepath).getAbsoluteFile());
            Sheet sheet = workbook.getSheet("TestData");
            int startRow, startCol, endRow, endCol, ci, cj;
            jxl.Cell tableStart = null;
            if (sheet != null)
            {
                tableStart = sheet.findCell(tableName);
                if (tableStart != null)
                {
                    startRow = tableStart.getRow();
                    startCol = tableStart.getColumn();
                    endCol = startCol + 1;
                    endRow = startRow + 1;
                    for (int i = startCol + 1;; i++)
                    {
                        String content = getData(sheet, i, startRow);
                        if (content.isEmpty())
                        {
                            endCol = i;
                            for (int j = startRow + 1;; j++)
                            {
                                String contentAgain = getData(sheet,
                                        endCol - 1, j);
                                if (contentAgain.isEmpty())
                                {
                                    endRow = j;
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    tabArray = new String[endRow - startRow - 1][endCol
                            - startCol - 1];
                    ci = 0;
                    for (int i = startRow + 1; i < endRow; i++, ci++)
                    {
                        cj = 0;
                        for (int j = startCol + 1; j < endCol; j++, cj++)
                        {
                            tabArray[ci][cj] = getData(sheet, j, i);
                        }
                    }
                }
                else
                {
                    log.info(tableName
                            + " - table Name not found in the Excel");
                }
            }
        }
        catch (Exception e)
        {
        	log.info("Error while reading data for tests : "+e);
            e.printStackTrace();
        }
        return tabArray;
    }
    /**
     * To get data from a sheet of excel document
     * @param sheet
     * @param row
     * @param col
     * @return
     */
    public static String getData(Sheet sheet, int row, int col)
    {
        try
        {
            return sheet.getCell(row, col).getContents().trim();
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            return "";
        }
    }



}
