package perceptron;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class Training 
{
	static double LEARNING_RATE = 0.1;
	
	public static void main(String[] args) throws IOException
	{
		double[] features = new double[15];
		double[] weights = new double[16];//the last one is the bias
		double localError;
		ArrayList<Output> outputs = new ArrayList<Output>();
		int iter = 0;

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
			int featureCount = 0;
			for (Cell cell : row)
			{
				features[featureCount] = cell.getNumericCellValue();
				featureCount ++;
			}
			Output output = new Output(features, iter % 2);
			outputs.add(output);
			iter = iter + 1;
		}
		
		//update weights
		int trainingCount = 0;
		while(trainingCount < iter)
		{
			//calculate predicted class
			double currentClassification = calculateOutput(0.0, weights, outputs.get(trainingCount).features);
			// difference between predicted and actual class values
			localError = outputs.get(trainingCount).classification - currentClassification;
			double [] tempFeatures = outputs.get(trainingCount).features;
			for (int i = 0; i < weights.length-1; i++) 
			{
				weights[i] = weights[i] + 
						(LEARNING_RATE * localError * tempFeatures[i]);
			}
			weights[weights.length-1] = weights[weights.length-1] + LEARNING_RATE * localError;
			trainingCount++;
		}
		for (int i = 0; i < weights.length; i++) {
			System.out.println(weights[i]);
		}
	}
	
	static double calculateOutput(double threshold, double weights[], double features[])
	{
		double sum = 0.0;
		
		for (int i = 0; i < features.length; i++) 
		{
			sum = sum + weights[i] * features[i];
		}
		sum = sum + weights[weights.length-1];
		
		if(sum >= threshold)
		{
			return 1.0;
		}else{
			return 0.0;
		}
		
	}
	
}

class Output
{
	double [] features;
	double classification;
	
	public Output(double [] features, double classification)
	{
		this.features = features;
		this.classification = classification;
	}
	
}