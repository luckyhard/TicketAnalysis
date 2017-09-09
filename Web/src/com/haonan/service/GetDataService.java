package com.haonan.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hive.ql.parse.HiveParser_IdentifiersParser.precedenceAmpersandExpression_return;

import com.haonan.model.Achievement;
import com.haonan.model.Person;
import com.haonan.util.db;

public class GetDataService {
	//该等级所有代理人的列表
	public List<Person> list(String label, int offset, int pageSize){
		Connection conn = db.createConnection();
		List<Person> persons = new ArrayList<Person>();
		int max = maxDay();
		int num = pageSize;	//每次查询的数量
		String sql = "select * from train_log where " +label 
				+" = 1 and day_id = " + max +" order by num desc "
						+ "limit " + offset+", "+num;
		PreparedStatement ps = db.prepare(conn, sql);
		try {
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Person person = new Person();
				person.setDay_id(rs.getInt("day_id"));
				person.setID(rs.getString("saler"));
				person.setNum(rs.getInt("num"));
				
				persons.add(person);
				}
			db.close(rs);
			db.close(ps);
		} catch (Exception e) {
			System.out.println("数据库语句执行错误！");
		}
		db.close(conn);
		return persons;
	}
	//查询一个代理人历史评价
	public List<Achievement> achievements(String saler,int day_id){
		List<Achievement> history = new ArrayList<Achievement>();
		Connection conn = db.createConnection();
		String sql1 = "select * from train_log where saler = '"+saler+"'"+" and day_id <= "+day_id +" and day_id >=1";
		PreparedStatement ps1 = db.prepare(conn, sql1);
		
		try{
			ResultSet rs1 = ps1.executeQuery();
			
			while(rs1.next()){
				Achievement person = new Achievement();
				person.setDay(rs1.getInt("day_id"));
				person.setID(rs1.getString("saler"));
				person.setNum(rs1.getDouble("num"));
				person.setRound(rs1.getDouble("round"));
				
				String sql2 = "select count(*) from sales_log where saler = '"+saler+"'"+" and day_id = "+person.getDay();
				PreparedStatement ps2 = db.prepare(conn, sql2);
				ResultSet rs2 = ps2.executeQuery();
				while(rs2.next()){
					person.setNumPerson(rs2.getInt("count(*)"));
				}
				history.add(person);
				db.close(rs2);
				db.close(ps2);
			}
			db.close(rs1);
			db.close(ps1);
			
		}catch(Exception e){
			System.out.println("代理人历史指标查询出错");
		}
		db.close(conn);
		return history;
	}
	//查询一个代理人评价指标
	public Achievement achievement(String saler, int day_id){
		Achievement person = new Achievement();
		Connection conn = db.createConnection();
		String sql1 = "select * from train_log where saler = '"+saler+"'"+" and day_id = "+day_id;
		String sql2 = "select count(*) from sales_log where saler = '"+saler+"'"+" and day_id = "+day_id;
		PreparedStatement ps1 = db.prepare(conn, sql1);
		PreparedStatement ps2 = db.prepare(conn, sql2);
		try{
			ResultSet rs1 = ps1.executeQuery();
			ResultSet rs2 = ps2.executeQuery();
			while(rs1.next()){
				person.setDay(rs1.getInt("day_id"));
				person.setID(rs1.getString("saler"));
				person.setNum(rs1.getDouble("num"));
				person.setRound(rs1.getDouble("round"));
			}
			while(rs2.next()){
				person.setNumPerson(rs2.getInt("count(*)"));
			}
		}catch(Exception e){
			System.out.println("指标查询失败");
		}
		return person;
	}
	//查询
	public List<Person> search(String ID, int day_id){
		Connection conn = db.createConnection();
		List<Person> persons = new ArrayList<Person>();
		String sql;
		if(ID == null){
			sql = "select * from sales_log where day_id = " + day_id;
		}
		else{
			sql = "select * from sales_log where saler like '"+ID
					+"%' and day_id = "+day_id;
		}
		System.out.println(sql);
		PreparedStatement ps = db.prepare(conn, sql);
		try {
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Person person = new Person();
				person.setDay_id(rs.getInt("day_id"));
				person.setID(rs.getString("buyer"));
				person.setNum(rs.getInt("num"));
				
				persons.add(person);
			}
			db.close(rs);
			db.close(ps);
		} catch (SQLException e) {
			System.out.println("单个代理人售卖信息查询失败!");
		}
		db.close(conn);
		return persons;
	}
	//查询某个代理人出售信息，用于绘制折线图
	public List<Person> showSaler(String ID,int day_id){
		Connection conn = db.createConnection();
		List<Person> persons = new ArrayList<Person>();
		int max = maxDay();
		String sql = "select * from sales_log where saler = '"+ID
				+"' and day_id = "+day_id;
		PreparedStatement ps = db.prepare(conn, sql);
		
		try {
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Person person = new Person();
				person.setDay_id(rs.getInt("day_id"));
				person.setID(rs.getString("buyer"));
				person.setNum(rs.getInt("num"));
				
				persons.add(person);
			}
			db.close(rs);
			db.close(ps);
		} catch (SQLException e) {
			System.out.println("单个代理人出售信息查询失败!");
		}
		db.close(conn);
		return persons;
	}
	//查询某个代理人买入信息
	public List<Person> showBuyer(String ID,int day_id){
		Connection conn = db.createConnection();
		List<Person> persons = new ArrayList<Person>();
		int max = maxDay();
		String sql = "select * from sales_log where buyer = '"+ID
				+"' and day_id = "+day_id;
		PreparedStatement ps = db.prepare(conn, sql);
		
		try {
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Person person = new Person();
				person.setDay_id(rs.getInt("day_id"));
				person.setID(rs.getString("saler"));
				person.setNum(rs.getInt("num"));
				
				persons.add(person);
			}
			db.close(rs);
			db.close(ps);
		} catch (SQLException e) {
			System.out.println("单个代理人买入信息查询失败!");
		}
		db.close(conn);
		return persons;
	}
	//折线图横轴坐标
	public String[] xAxis(List<Person> persons){
		String[] xaxis = new String[persons.size()];
		for(int i = 0;i<persons.size();i++){
			String temp = persons.get(i).getID();
			xaxis[i] = temp;
		}
		return xaxis;
	}
	//折线图的中折线
	public int[] series(List<Person> persons){
		int[] series = new int[persons.size()];
		for(int i=0;i<persons.size();i++){
			int temp = persons.get(i).getNum();
			series[i] = temp;
		}
		return series;
	}
	//每级代理人的数量
	public int countClassify(String label,int day_id){
		int count = 0;
		Connection conn = db.createConnection();
		String sql = "select count(*) from train_log where "
		    +label + " = 1" + " and day_id=" + day_id;
		PreparedStatement ps = db.prepare(conn, sql);
		
		try {
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				count = rs.getInt("count(*)");
			}
			db.close(rs);
			db.close(ps);
		} catch (SQLException e) {
			System.out.println("每层代理人人数查询失败！");
		}
		db.close(conn);
		return count;
	}
	//最大天数
	public int maxDay(){
		int maxDayID = 0;
		Connection conn = db.createConnection();
		String sql = "select max(day_id) from train_log";
		PreparedStatement ps = db.prepare(conn, sql);
		try {
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				maxDayID = rs.getInt("max(day_id)");
			}
			db.close(rs);
			db.close(ps);
		} catch (SQLException e) {
			System.out.println("最大天数查询失败!");
		}
		db.close(conn);
		return maxDayID;
	}
}
