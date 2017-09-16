package service;

import bean.Host;

import java.io.File;
import java.util.List;

/**
 * Created by cellargalaxy on 17-9-16.
 */
public interface ExcelReadHost {
	List<Host> readExcelHost(File excelFile);
}
