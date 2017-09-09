<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>代理人信息</title>
<script src="./lib/echarts.min.js"></script>
<style type="text/css">
	#main{
		height: 1200px;
		width: 1700px;
		margin: 0 auto;
	}
	#history{
		width: 450px;
		height: 400px;
		display: none;
	}
	.chart{
		width: 800px;
		height: 500px;
	}
	#realSale{
		margin: 50px;
		float: left;
	}
	#predictSale{
		float: right;
	}
	#realBuy{
		float: left;
		clear: left;
	}
	#predictBuy{
		float: right;
		background-color: pink;
	}
</style>
</head>
<!-- 从session中获取数据（横轴和纵轴的值），以及计算交易人数和销售额 -->
<script type="text/javascript">
/*出售信息  */
var categories = [];
	<% String[] strs = (String[])session.getAttribute("salerXaxis");
		int numSalePerson = 0;
		for(int i = 0; i<strs.length; i++){%>
		categories[<%=i%>] = '<%=strs[i]%>';
		<% numSalePerson++;}%>
	
	var data = [];
	<% int[] data = (int[])session.getAttribute("salerSeries");
		int sumSale = 0;
	for(int i = 0; i<data.length; i++){%>
	data[<%=i%>] = <%=data[i]%>;
	<%sumSale = sumSale + data[i];}%>
	var offset = 0;
	var offsize = 15;
	/*买入信息  */
	var buyRealCategories = [];
 	<% String[] buyStrs = (String[])session.getAttribute("buyerXaxis");
 		int numBuyPerson = 0;
 		for(int i = 0; i<buyStrs.length; i++){%>
 		buyRealCategories[<%=i%>] = '<%=buyStrs[i]%>';
 		<%numBuyPerson++;}%>
 	
 	var buyRealData = [];
 	<% int[] buyData = (int[])session.getAttribute("buyerSeries");
 		int sumBuy = 0;
		for(int i = 0; i<buyData.length; i++){%>
		buyRealData[<%=i%>] = <%=buyData[i]%>;
		<%sumBuy = sumBuy + buyData[i];}%>
	var achi = [];
	<% int[] achi = (int[])session.getAttribute("achi");
	   for(int i = 0; i<achi.length; i++){%>
	   achi[<%=i%>] = <%=achi[i]%>;<%}%>
	var days = [];
	<% int[] days = (int[])session.getAttribute("days");
	   for(int i = 0; i<days.length; i++){%>
	   days[<%=i%>] = <%=days[i]%>;<%}%>
	function show(){
		var history = document.getElementById("history");
		if(history.style.display == "none"){
			history.style.display = "block";
		}else{
			history.style.display = "none";
		}
	}
</script>
<body>
	<center>
		
		
		<div id = "main">
			<div id = "achievement">
				<font>代理人 <s:property value = "ID"/>：</font>今天人均销售额 
				<s:property value = "person.achievement"/><br />
				<button onclick = "show();">历史信息</button>
				<div id = "history"></div>
			</div>
			<div id = "realSale" class = "chart" >
				<font>人数<%=numSalePerson %></font>
				<font>数量<%=sumSale %></font>
			</div>
			<div id = "predictSale" class = "chart">
				预测售明天票数目<s:property value = "predictNum"/></br>
				预测售明天票额<s:property value = "predictRound"/>
			</div> 
			<div id = "realBuy" class = "chart">
				<font>人数<%=numBuyPerson %></font>
				<font>数量<%=sumBuy %></font>
			</div>
			<div id = "predictBuy" class = "chart">
				预测明天购买票数<s:property value = "predictBuyNum"/>	</br>			
				预测明天买票额<s:property value = "predictBuyRound"/>
			</div>
		</div>
		<div>
			<input type="button" value="返回" onclick="javascript:history.back(-1);">
		</div>
	</center>
	<script type="text/javascript">
	var historyChart = echarts.init(document.getElementById("history"));
	var historyOption = {
			title: {
				text: '代理人历史指标信息',
				subtext: ' 天数: '+<%=achi.length%>
			},
			tooltip: {
				show: true,
				trigger: 'axis'
			},
			legend: {
				data: ['代理人售卖']
			},
			xAxis: [{
				axisLabel:{
					interval: 0,
				},
				type: 'category',
				data: days
			}],
			yAxis: [{
				type: 'value',
			}],
			dataZoom:{
				show: true,
				realtime: true,
				startValue: offset,
				endValue: offsize,
				backgroundColor: '#DAEBEE',
                borderColor: '#D7D5D5',
                handleColor: '#4169E1'
			},
			series:[{
				name: '人均销售额',
				type: 'line',
				data: achi,
				itemStyle:{
					normal:{
						label:{
							show: true
						}
					},
				}
			}]
	}
	historyChart.setOption(historyOption);
	var realChart = echarts.init(document.getElementById("realSale"));
	var realOption = {
			title: {
				text: '代理人出售信息',
				subtext: '销售人数: ' + <%=numSalePerson%>+'  总销售票数: '+<%=sumSale%>
			},
			tooltip: {
				show: true,
				trigger: 'axis'
			},
			legend: {
				data: ['代理人售卖']
			},
			xAxis: [{
				axisLabel:{
					interval: 0,
				},
				type: 'category',
				data: categories
			}],
			yAxis: [{
				type: 'value',
			}],
			dataZoom:{
				show: true,
				realtime: true,
				startValue: offset,
				endValue: offsize,
				backgroundColor: '#DAEBEE',
                borderColor: '#D7D5D5',
                handleColor: '#4169E1'
			},
			series:[{
				name: '售卖',
				type: 'line',
				data: data,
				itemStyle:{
					normal:{
						label:{
							show: true
						}
					},
				}
			}]
	}
	realChart.setOption(realOption);
	var buyRealChart = echarts.init(document.getElementById("realBuy"));
	
	var buyRealOption = {
			title: {
				text: '代理人买入信息',
				subtext: '买入人数: ' + <%=numBuyPerson%>+'  买入票数: '+<%=sumBuy%>
			},
			tooltip: {
				show: true,
				trigger: 'axis'
			},
			legend: {
				data: ['代理人买入']
			},
			xAxis: [{
				axisLabel:{
					interval: 0,
				},
				type: 'category',
				data: buyRealCategories
			}],
			yAxis: [{
				type: 'value',
			}],
			dataZoom:{
				show: true,
				realtime: true,
				startValue: offset,
				endValue: offsize,
				backgroundColor: '#DAEBEE',
                borderColor: '#D7D5D5',
                handleColor: '#4169E1'
			},
			series:[{
				name: '买入',
				type: 'line',
				data: buyRealData,
				itemStyle:{
					normal:{
						label:{
							show: true
						}
					},
				}
			}]
	}
	buyRealChart.setOption(buyRealOption);
	/*var pieChart = echarts.init(document.getElementById("pieChart"));
	pieOption={
			title:{
				text:'代理人信息饼状图',
				x:'center',
			},
			tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    legend: {
		        orient : 'vertical',
		        x : 'left',
		    },
		    calculable : true,
		    series : [
		        {
		            name:'访问来源',
		            type:'pie',
		            radius : '55%',
		            center: ['50%', '60%'],
		            data:(function(){  
                        var res = [];  
                        var len = data.length;  
                        while (len--) {  
                            res.push({  
                            name: categories[len],  
                            value: data[len]  
                        });  
                        }  
                        return res;  
                        })()
		        }
		    ]
	};
	pieChart.setOption(pieOption)*/
	
	</script>
</body>
</html>