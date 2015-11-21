package perceptron;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class Training 
{
	static double LEARNING_RATE = 0.1;
	double[] weights = new double[16];//the last one is the bias
	double localError;
	ArrayList<Output> outputs = new ArrayList<Output>();
	int iter = 0;
	
	public Training () 
	{
		//initialize weights arbitrarily
		Random random = new Random();
		for (int i = 0; i < weights.length; i++) {
			weights[i] = random.nextDouble();
		}

		InputStream inp = null;
		try {
			inp = new FileInputStream("/Users/tiagomachado/Documents/workspace/GVG-AI/src/SuplementaryFiles/Big_File_GVGA1.xls");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(new POIFSFileSystem(inp));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HSSFSheet sheet1 = wb.getSheetAt(0);
				
		//initialize features
		Iterator<Row> rowIterator = sheet1.rowIterator();
		while (rowIterator.hasNext()) {
			Row row = (Row) rowIterator.next();
			double[] features = new double[15];
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
		
		for (double weight : this.weights) 
		{
			System.out.println(weight);
		}
		
	}
	
	public double [] updateWeights()
	{
		//update weights
		int trainingCount = 0;
		while(trainingCount < iter)
		{
			double currentClassification = stepFunction(0.0, weights, outputs.get(trainingCount).features);

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
		return weights;
	}
	
	public double stepFunction(double threshold, double weights[], double features[])
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
	
	public static void main (String [] args) throws IOException
	{
		Training t = new Training();
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

class Weights
{
	double [] weights;
	
	public Weights(double [] weights)
	{
		this.weights = weights; 
	}
	
}