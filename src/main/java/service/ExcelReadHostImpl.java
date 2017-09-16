package service;

import bean.Host;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cellargalaxy on 17-9-16.
 */
public class ExcelReadHostImpl implements ExcelReadHost{
	public List<Host> readExcelHost(File excelFile) {
		Workbook workbook = null;
		try {
			workbook = Workbook.getWorkbook(excelFile);
			Sheet sheet = workbook.getSheet(0);
			List<Host> hosts = new LinkedList<Host>();
			for (int i = 1; i < sheet.getRows(); i++) {
				Cell[] cells = sheet.getRow(i);
				Host host;
				if (cells.length == 5) {
					//					address					building				floor					model					name
					host = new Host(cells[0].getContents().trim(), cells[1].getContents().trim(), cells[2].getContents().trim(), cells[3].getContents().trim(), cells[4].getContents().trim());
					hosts.add(host);
				} else if(cells.length == 4){
					//					address					building				floor					model					name
					host = new Host(cells[0].getContents().trim(), cells[1].getContents().trim(), cells[2].getContents().trim(), cells[3].getContents().trim(), null);
					hosts.add(host);
				}
			}
			return hosts;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (workbook != null) {
				workbook.close();
			}
		}
	}
}
