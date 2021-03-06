package com.haonan.algorithm;

import java.text.DecimalFormat;

import org.apache.spark.mllib.classification.SVMModel;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.sql.SparkSession;

public class TestSaleSVM {
	public static void main(String[] args) {
		SparkSession session = SparkSession.builder()
				.appName("TestBUyModel").master("local").getOrCreate();
		DecimalFormat df = new DecimalFormat("###0.000000");
		SVMModel model = SVMModel.load(session.sparkContext(), "saleSVM");
		double[] features = {75440/118, 13};
		double[] weights = model.weights().toArray();
		System.out.println(df.format(weights[0])+" "+df.format(weights[1]));
		System.out.println(model.predict(Vectors.dense(features)));
	}
}
