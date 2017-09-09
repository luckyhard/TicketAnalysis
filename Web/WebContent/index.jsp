<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="./lib/echarts.min.js"></script>
<style type="text/css">
	#main{
		width: 800px;
	    height: 500px;
	    border-radius: 4px;
	    border:1px solid #333;
	    box-shadow:inset 0 0 3px 3px #fff;
	}
	#tableDiv{
		border-top: #ddd solid 1px;
		border-left: #ddd solid 1px;
		width:600px; 
		height:400px;
		table-layout: fixed;
	}
	#tableDiv th{
		background-color: gray;
		border-bottom: #ddd solid 1px;
		border-right: #ddd solid 1px;
		overflow: hidden;
		white-space: nowrap;
	}
	#tableDiv td{
		overflow: hidden;
		white-space: nowrap;
		border-bottom: #ddd solid 1px;
		border-right: #ddd solid 1px;
	}
</style>
</head>
<body>
	<center>
		<% String high = "high";
		   String mid = "mid";
		   String low = "low";
		   %>
		<div id="logo">
			<img alt="logo" src="image/plane.jpg">
		</div>
		<a href = "/Web/index!list?label=high">高级代理人</a>
		<a href = "/Web/index!list?label=mid">中级代理人</a>
		<a href = "/Web/index!list?label=low">低级代理人</a>
		<!-- 分页基础信息获取 -->
		<% int count = (int)session.getAttribute("countClassify");%>
		<% int size = (int)session.getAttribute("pageSize");%>
		<% int pages = count/size+1; %>
		<% int day = (int)session.getAttribute("maxDay");%>
		<% int currentPage =  (int)session.getAttribute("offset")/size + 1;%>
		<div id="title" style = "height: 45px; width: 800px;">
			<font style="position: absolute; left: 540px;">代理人总数量<%=count %></font>
		</div>
		<div id = "main">
			<div id="operateDiv">
			</div>
			<div id="tableDiv">
			<table id="myTable" style="width: 100%;" >
				<tr>
					<th>序号</th>
					<th>日期</th>
					<th>代理人编号</th>
					<th>当天售卖数量</th>
					<th>操作</th>
				</tr>
				<%int number = (int)session.getAttribute("offset")+1; %>
				<s:iterator var="person" value="persons">
					<tr>
						<td><%=number++ %></td>
						<form method ="post" action = "/Web/index!show">
						<td><select name = "day_id" id = "day_id" onchange = "getDay_id();">
							<% for(int i = day; i >= 1; i--){ %>
							<option value = "<%=i%>" <%if(i == 15){ %> selected = "selected"<%} %>
							><%=i %></option>
							<%} %>
						</select></td>
						<td><input name = "ID" value = "<s:property value="#person.ID"/>"/></td>
						<td><s:property value="#person.num"/></td>
						<script type="text/javascript">
							function getDay_id(){
								var day_id = document.getElementById("day_id").value;
								return day_id;
							}
							function see(){
								window.location.href = "/Web/index!show?ID="+
										"<s:property value="#person.ID"/>"
								+"&day_id="+getDay_id();
								alert(getDay_id());
							}
						</script>
						<td><input type = "submit" value = "查看"/></td>
						</form>
					</tr>
				</s:iterator>
			</table>
			</div>
		</div>
		<div>
		<!-- 分页代码 -->
			<%if(currentPage > 1){ %>
			<a href = "/Web/index!list?offset=0&day_id = <%=day%>">首页</a>
			<a href = "/Web/index!list?offset=<%=(currentPage - 2) * size %>&day_id = <%=day%>">上一页</a>
			<%} %>
			<%if(currentPage > 3 && currentPage < (pages - 2)) {%>
			<% for(int i = currentPage - 2; i <= currentPage + 2; i++){ %>
			<a href="/Web/index!list?offset=<%=(i-1) * size %>&day_id=<%=day%>">
			<%=i%></a><%} %>
			<%} else if(currentPage <= 3 ){ 
				if(pages >= 5){%> 
				<% for(int i = 0; i < 5; i++){%>
				<a href="/Web/index!list?offset=<%=i*size%>&day_id=
				<%=day%>"><%=i+1 %></a><% }}else if(pages < 5) {%>
				<% for(int i = 0; i < pages; i++){%>
				<a href="/Web/index!list?offset=<%=i*size%>&day_id=
				<%=day%>"><%=i+1 %></a>
				<%}} %>
			<%} else if(currentPage >= (pages - 2)){%>
			<% for(int i = pages - 5; i < pages; i++){%>
			<a href="/Web/index!list?offset=<%=i*size%>&day_id=
			    <%=day%>"><%=i+1 %></a><%} %><%} %>
			<%if(currentPage < 15){ %>
			<a href = "/Web/index!list?offset=<%=(currentPage) * size %>&day_id = <%=day%>">下一页</a>
			<a href = "/Web/index!list?offset=<%=((pages - 1) * size) %>&day_id = <%=day%>" >尾页</a>
			<%} %>
			 <font style = "position: absolute; left: 1200px;">
			 页数 <%=currentPage %> / <%=pages %></font>
		</div>
	</center>
	<script type="text/javascript">
	//senfe("表格名称","奇数行背景","偶数行背景","鼠标经过背景","点击后背景");
	senfe("myTable", "#fff", "#fff", "#cfc", "#fff");
	function senfe(o, a, b, c, d) {
		var t = document.getElementById(o).getElementsByTagName("tr");
		for (var i = 0; i < t.length; i++) {
			t[i].style.backgroundColor = (t[i].sectionRowIndex % 2 == 0) ? a : b;
			t[i].onclick = function() {
				if (this.x != "1") {
					this.x = "1";// 本来打算直接用背景色判断，FF获取到的背景是RGB值，不好判断
					this.style.backgroundColor = d;
				} else {
					this.x = "0";
					this.style.backgroundColor = (this.sectionRowIndex % 2 == 0) ? a
							: b;
				}
			}
			t[i].onmouseover = function() {
				if (this.x != "1")
					this.style.backgroundColor = c;
			}
			t[i].onmouseout = function() {
				if (this.x != "1")
					this.style.backgroundColor = (this.sectionRowIndex % 2 == 0) ? a
							: b;
			}
		}
	}
	</script>
</body>
</html>