package com.haonan.algorithm;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.mllib.classification.SVMModel;
import org.apache.spark.mllib.classification.SVMWithSGD;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class buySVMModel {
	public static void main(String[] args) {
		SparkSession session = SparkSession.builder()
				.appName("saleSVMModel").master("local[4]").getOrCreate();
		DecimalFormat df = new DecimalFormat("###0.000");
		Map<String, String> options = new HashMap<String, String>();
		options.put("url", "jdbc:mysql://localhost:3306/soft_match");
		options.put("dbtable", "train_buyok_log");
		options.put("user", "root");
		options.put("password", "123456");
		Dataset<Row> data = session.read().format("jdbc")
				.options(options).load();
		data.registerTempTable("train_buyok_log");
		Dataset<Row> rows = data.sqlContext().sql("select * from train_buyok_log");
		JavaRDD<Row> rowRDD = rows.javaRDD();
		JavaRDD<LabeledPoint> points = rowRDD.map(new Function<Row, LabeledPoint>() {

			private static final long serialVersionUID = 1L;

			@Override
			public LabeledPoint call(Row row) throws Exception {
				double num = row.getAs("num");
				double round = row.getAs("round");
				int time = row.getAs("time");
				int label = row.getAs("label");
				double[] features = {num,round,time};
				return new LabeledPoint(label,Vectors.dense(features));
			}
		});
		SVMModel model = SVMWithSGD.train(points.rdd(), 100, 0.00001,1);
		double[] weight = model.weights().toArray();
		
		System.out.println(model);
		System.out.println(weight.length);
		System.out.println(df.format(weight[0])+" "+df.format(weight[1])+" "+df.format(weight[2]));
		model.save(session.sparkContext(), "buySVM");
		
		
//		points.foreach(new VoidFunction<LabeledPoint>() {
//			
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void call(LabeledPoint point) throws Exception {
//				System.out.println(point);
//			}
//		});
	}
}
