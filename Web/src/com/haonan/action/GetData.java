package com.haonan.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.haonan.model.Achievement;
import com.haonan.model.Person;
import com.haonan.service.GetDataService;
import com.haonan.service.PredictService;
import com.opensymphony.xwork2.ActionContext;

public class GetData {
	private List<Person> persons;	
	private Achievement person;
	private List<Achievement> history;
	private int achi[];
	private int days[];
	private List<Person> salerPersons;//代理人出售给那些人
	private List<Person> buyerPersons;//代理人从那些人买入
	private GetDataService service = new GetDataService();
	private PredictService pService = new PredictService();
	private int predictNum;			//Num预测值
	private int predictRound;		//Round的预测值
	private double Nfeatures[] = new double[4];		//预测Num的特征向量
	private double Rfeatures[] = new double[4];		//预测Round的特征向量
	private int predictBuyNum;		//buyNum预测值
	private int predictBuyRound;	//buyRound预测值
	private double BNfeature;		//buyNum特征值
	private double BRfeature;		//buyRound特征值
	private Map session;
	private String label;	//代理人登记标签
	private String ID;				//获取前台传递的saler的ID
	private int day_id;				//获取前台传递的天数		
	private String[] salerXaxis;	//出售折线图横轴显示
	private int[] salerSeries;		//出售折线图value值
	private String[] buyerXaxis;	//买入折线图横轴显示
	private int[] buyerSeries;		//买入折线图value值
	private int offset;				//数据库位移起始变量
	private int countClassify;		//每天每层代理人总数量
	private int pageSize;			//每次查询的数量
	private int day;				//售卖的天数，默认为最近天数
	public GetData(){
		label = "high";
		session = ActionContext.getContext().getSession();
		day_id = 0;
		offset = 0;
		countClassify = 0;
		pageSize = 15;
		day = service.maxDay();
		session.put("pageSize", pageSize);
		session.put("maxDay", day);
		session.put("offset", offset);
	}
	public String list(){
		if(countClassify == 0){
			if(day_id == 0){
				day_id = day;
			}
			countClassify = service.countClassify(label,day_id);
		}
		session.put("countClassify", countClassify);
		session.put("offset", offset);
		
		this.persons = service.list(label, offset, pageSize);
		return "list";
	}
	public String search(){
		if(countClassify == 0){
			if(day_id == 0){
				day_id = day;
			}
			countClassify = service.countClassify(label,day_id);
		}
		session.put("countClassify", countClassify);
		session.put("offset", offset);
//		this.persons = service.search(ID, day_id);
		return "search";
	}
	public String show(){
		if(day_id == 0){
			day_id = day;
		}
		person = service.achievement(ID,day_id);
		person.setAchievement((int)(person.getRound() / person.getNumPerson()));
		history = service.achievements(ID, day_id);
		achi = new int[history.size()];
		days = new int[history.size()];
		for(int i = 0; i < history.size(); i++){
			days[i] = history.get(i).getDay();
			achi[i] = (int) history.get(i).getRound()/history.get(i).getNumPerson();
		}
		//获取persons
		System.out.println(day_id +"  "+ID);
		salerPersons = service.showSaler(ID,day_id);
		buyerPersons = service.showBuyer(ID, day_id);
		//将persons转化为折线图中坐标数据
		salerXaxis = service.xAxis(salerPersons);
		salerSeries = service.series(salerPersons);
		buyerXaxis = service.xAxis(buyerPersons);
		buyerSeries = service.series(buyerPersons);
		//将数据放到session，在jsp获得session即可
		session.put("salerXaxis", salerXaxis);
		session.put("salerSeries", salerSeries);
		session.put("buyerXaxis", buyerXaxis);
		session.put("buyerSeries", buyerSeries);
		session.put("achi", achi);
		session.put("days", days);
		//对于numSale和roundSale进行预测
		Nfeatures = pService.getAVG(ID,day_id);
		predictNum = (int) pService.predictSaleNum(Nfeatures);
		Rfeatures[0] = predictNum;
		Rfeatures[1] = Nfeatures[1];
		Rfeatures[2] = Nfeatures[2];
		Rfeatures[3] = Nfeatures[3];
		predictRound = (int) pService.predictSaleRound(Rfeatures);
		//对于numBuy和roundBuy进行预测
		BNfeature = pService.getBuyAvg(ID, day_id);
		predictBuyNum = (int) pService.predictBuyNum(BNfeature);
		BRfeature = predictBuyNum;
		predictBuyRound = (int) pService.predictBuyRound(BRfeature);
		return "show";
	}
	
	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}
	
	public List<Person> getSalerPersons() {
		return salerPersons;
	}
	public void setSalerPersons(List<Person> salerPersons) {
		this.salerPersons = salerPersons;
	}
	public List<Person> getBuyerPersons() {
		return buyerPersons;
	}
	public void setBuyerPersons(List<Person> buyerPersons) {
		this.buyerPersons = buyerPersons;
	}
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public int getDay_id() {
		return day_id;
	}

	public void setDay_id(int day_id) {
		this.day_id = day_id;
	}

	public String[] getSalerXaxis() {
		return salerXaxis;
	}
	public void setSalerXaxis(String[] salerXaxis) {
		this.salerXaxis = salerXaxis;
	}
	public int[] getSalerSeries() {
		return salerSeries;
	}
	public void setSalerSeries(int[] salerSeries) {
		this.salerSeries = salerSeries;
	}
	
	public String[] getBuyerXaxis() {
		return buyerXaxis;
	}
	public void setBuyerXaxis(String[] buyerXaxis) {
		this.buyerXaxis = buyerXaxis;
	}
	public int[] getBuyerSeries() {
		return buyerSeries;
	}
	public void setBuyerSeries(int[] buyerSeries) {
		this.buyerSeries = buyerSeries;
	}
	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getCountClassify() {
		return countClassify;
	}

	public void setCountClassify(int countClassify) {
		this.countClassify = countClassify;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getPredictNum() {
		return predictNum;
	}
	public void setPredictNum(int predictNum) {
		this.predictNum = predictNum;
	}
	public int getPredictRound() {
		return predictRound;
	}
	public void setPredictRound(int predictRound) {
		this.predictRound = predictRound;
	}
	public double[] getNfeatures() {
		return Nfeatures;
	}
	public void setNfeatures(double[] nfeatures) {
		Nfeatures = nfeatures;
	}
	public void setBNfeature(double bNfeature) {
		BNfeature = bNfeature;
	}
	public double getBRfeature() {
		return BRfeature;
	}
	public void setBRfeature(double bRfeature) {
		BRfeature = bRfeature;
	}
	public int getPredictBuyRound() {
		return predictBuyRound;
	}
	public void setPredictBuyRound(int predictBuyRound) {
		this.predictBuyRound = predictBuyRound;
	}
	public int getPredictBuyNum() {
		return predictBuyNum;
	}
	public void setPredictBuyNum(int predictBuyNum) {
		this.predictBuyNum = predictBuyNum;
	}
	public Achievement getPerson() {
		return person;
	}
	public void setPerson(Achievement person) {
		this.person = person;
	}
	public List<Achievement> getHistory() {
		return history;
	}
	public void setHistory(List<Achievement> history) {
		this.history = history;
	}
	public int[] getAchi() {
		return achi;
	}
	public void setAchi(int[] achi) {
		this.achi = achi;
	}
	public int[] getDays() {
		return days;
	}
	public void setDays(int[] days) {
		this.days = days;
	}
	
	
}
