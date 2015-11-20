package perceptron;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

public class Training 
{
	public static void main(String[] args) throws IOException
	{
		double[] features = new double[15];
		double[] weights = new double[16];

		//initialize weights arbitrarily
		Random random = new Random();
		for (int i = 0; i < weights.length; i++) {
			weights[i] = random.nextDouble();
		}

		InputStream inp = new FileInputStream("/Users/tiagomachado/Documents/workspace/GVG-AI/src/SuplementaryFiles/Big_File_GVGA1.xls");
		HSSFWorkbook wb = new HSSFWorkbook(new POIFSFileSystem(inp));
		HSSFSheet sheet1 = wb.getSheetAt(0);
		
		//initialize features
		for (Row row : sheet1) 
		{
			int iter = 0;
			for (Cell cell : row)
			{
				features[iter] = cell.getNumericCellValue();
			}
			iter++;
		}
				
	}
	
	static double calculateOutput(double weights[], double features[])
	{
		double sum = 0.0;
		for (double f : features) {
			for (double w : weights) {
				sum = sum + w * f;
			}
		}
		return sum;
	}
	
	static int calculateThreshold(double threshold, double weights[], double features[])
	{
		double sum = calculateOutput(weights, features);
		return (sum >= threshold) ? 1 : 0;
	}
}