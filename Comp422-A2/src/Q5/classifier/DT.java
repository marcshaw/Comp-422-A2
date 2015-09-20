package Q5.classifier;

import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class DT {


	private J48 classifier = new J48();
	private Evaluation eval;

	public DT(){

		Instances instances = null;
		try {
			DataSource source = new DataSource("/am/state-opera/home1/shawmarc/git/Comp-422-A2/Comp-422-A2/Comp422-A2/src/Q5/balance.arff");
			//DataSource source = new DataSource("/am/state-opera/home1/shawmarc/git/Comp-422-A2/Comp-422-A2/Comp422-A2/src/Q5/wine.arff");
			instances = source.getDataSet();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error(e);
		}

		// Make the last attribute be the class
		instances.setClassIndex(instances.numAttributes() - 1);

		instances.randomize(new java.util.Random(0));

		int trainSize = (int) Math.round(instances.numInstances() * .80);
		int testSize = instances.numInstances() - trainSize;
		Instances train = new Instances(instances, 0, trainSize);
		Instances test = new Instances(instances, trainSize, testSize);

		trainClassifier(train);

		testClassifier(test);

	}

	public static void main(String args[]){
		new DT();
	}

	public void trainClassifier(Instances trainingData){

		try {
			classifier.buildClassifier(trainingData);
		} catch (Exception e) {
			throw new Error(e);
		}

	}

	public void testClassifier(Instances test) {

		try {
			//Evaluate the model with the test
			eval = new Evaluation(test);
			eval.crossValidateModel(classifier, test, 10, new Random());

		} catch (Exception e) {
			throw new Error(e);
		}
		String graph = null;
		try {
			graph = classifier.graph();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Print all the information about the evaluation
		String strSummary = eval.toSummaryString();
		System.out.println(strSummary);

		try {
			System.out.println(eval.toClassDetailsString());
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		double[][] matrix = eval.confusionMatrix();

		for(int i = 0 ; i < matrix.length ; i ++){
			for(int j = 0 ; j < matrix[i].length ; j ++){
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}

	}

}