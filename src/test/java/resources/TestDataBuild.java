package resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestDataBuild {
	public static  Map<String, Object> getFormatData(ArrayList<String> eachRow) {
		
		Map<String, Object> authRequestFormData = new HashMap<String,Object> ();
		
		authRequestFormData.put("client_id", eachRow.get(0));
		authRequestFormData.put("client_secret", eachRow.get(1));
		authRequestFormData.put("grant_type", eachRow.get(2));
		authRequestFormData.put("scope",eachRow.get(3));
	
		return authRequestFormData;
		
	}
	
public static List<List<String>>  readExcelData(String testcase, String testDataFile) throws IOException{
		
		
		List<List<String>> testDataMultiRowList= new ArrayList<List<String>>();
		List<String> testDataSingleRowList = null;
		
		FileInputStream fis = new FileInputStream(testDataFile);
		XSSFWorkbook excelWorkbook = new XSSFWorkbook(fis);
		XSSFSheet sheetTestData = excelWorkbook.getSheet("TestData");
		
		Iterator<Row>  rows = sheetTestData.iterator();
		int countOfTestDataFields =0;
		while(rows.hasNext()) {
			testDataSingleRowList= new ArrayList<String>();
			Row current_row  =rows.next();
			if (current_row.getCell(0).getStringCellValue().equalsIgnoreCase(testcase)== false) {
				continue;
			}
			Iterator<Cell> cells = current_row.iterator();
			cells.next();
			while(cells.hasNext()) {
				Cell current_cell = cells.next();
				if (current_cell.getCellType() == CellType.STRING) {
					testDataSingleRowList.add(current_cell.getStringCellValue());
				}else {
					testDataSingleRowList.add(NumberToTextConverter.toText( current_cell.getNumericCellValue()));
				}
										
			}
			countOfTestDataFields = testDataSingleRowList.size();
			testDataMultiRowList.add(testDataSingleRowList);
		}
		
		/*
		 * Object[][] objtestDataMultiRowList = new
		 * Object[testDataMultiRowList.size()][countOfTestDataFields]; int i=0;
		 * for(List<String> eachRow: testDataMultiRowList) { int j=0; for(String
		 * eachData: eachRow) {
		 * 
		 * objtestDataMultiRowList[i][j] = eachData; j++;
		 * 
		 * } i++; }
		 */
				
		fis.close();	
		return testDataMultiRowList;
		
		
	}

	public static List<HashMap<String, String>> readJsonData(String jsonDataFile, String testcaseName) throws IOException {
		//commons io and jackson databind  are the dependencies
		String jsonData = FileUtils.readFileToString(new File(jsonDataFile), StandardCharsets.UTF_8);
		ObjectMapper mapper = new ObjectMapper();
		
		HashMap<String,List<HashMap<String, String>>> parsedData = mapper.readValue(
													jsonData, 
													new TypeReference<HashMap<String,List<HashMap<String, String>>>>(){}
													);
		
		return parsedData.get(testcaseName);
	}
}
