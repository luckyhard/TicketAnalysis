package com.haonan.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LinearRegressionModel;
import org.apache.spark.sql.SparkSession;

import com.haonan.util.db;

public class PredictService {
	//根据销售数量预测销售额
	public double predictSaleRound(double[] features){
		double predictRound = 0;
		//利用计算线性回归算法计算出的参数，预测
		predictRound = (features[0] * 7.758 + features[1] * 0.002 
				+ features[2] * 0.001) * 100;
		return predictRound;
	}
	//根据前五天的平均销售数量预测销售数量
	public double predictSaleNum(double[] features){
		double predictNum = 0;
		//利用计算线性回归算法计算出的参数，预测
		predictNum = features[0] * 1.026 / 5;
		return predictNum;
	}
	//获得之前五天的平均值，作为预测数据
	public double[] getAVG(String saler,int day_id){
		Connection conn = db.createConnection();
		String sql = "select saler,sum(num),high,mid,low from train_log "
				+ "where day_id <= "+(day_id)+" and day_id >= " + (day_id - 4)
				+ " and saler = '" + saler + "' group by saler,high,mid,low";
		PreparedStatement ps = db.prepare(conn, sql);
		double[] features = new double[5];
		try{
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				features[0] = rs.getDouble("sum(num)");
				features[1] = rs.getDouble("high");
				features[2] = rs.getDouble("mid");
				features[3] = rs.getDouble("low");
			}
			db.close(rs);
			db.close(ps);
		}catch(Exception e){
			System.out.println("预测数据五天平均值查询失败");
		}
		db.close(conn);
		return features;
	}
	
	public double predictBuyNum(double feature){
		double predictNum = 0;
		predictNum = feature * 0.993;
		return predictNum;
	}
	
	public double predictBuyRound(double feature){
		double predictRound = 0;
		predictRound = feature * 774;
		return predictRound;
	}
	
	public double getBuyAvg(String buyer, int day_id){
		Connection conn = db.createConnection();
		String sql = "select buyer,sum(num) from buy_train_log "
				+ "where day_id >= "+(day_id - 4)+" and day_id <= "+(day_id )
				+ " and buyer = '"+buyer+"' group by buyer";
		PreparedStatement ps = db.prepare(conn, sql);
		double avg = 0;
		try{
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				avg = rs.getDouble("sum(num)") / 5;
			}
			db.close(rs);
			db.close(ps);
		}catch(Exception e){
			System.out.println("查询buyer前五天数据失败");
		}
		db.close(conn);
		return avg;
	}
}
