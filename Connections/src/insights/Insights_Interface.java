package insights;

import java.io.BufferedReader;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.FastVector;
import weka.core.Instances;

public interface Insights_Interface {

	BufferedReader readDataFile(String filename);
	Evaluation classify(Classifier model,Instances trainingSet, Instances testingSet);
	double calculateAccuracy(FastVector predictions);
	Instances[][] crossValidationSplit(Instances data, int numberOfFolds);
}
