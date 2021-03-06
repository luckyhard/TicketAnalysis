# TicketAnalysis

## 民航机票分析系统
  
     （1）代理人分层模型：
    高级：15天以内销售票数达到10000以上
    中级：15天以内销售票数达到1000以上
    低级：15天以内销售票数达到1000以下
     （2）代理人销售信息
         可指定代理人指定天数查看代理人在当天的销售情况以及购买情况（折线图表示），包括销售数量，销售人。
     （3）销售预测
        ①预测代理人下一天的销售总额以及销售总票数
        ②预测代理人下一天可能交易的买方。
     （4）历史信息查看
        指定代理人查看其15天每天销售的总数量以及总金额
     3.2算法解析
    （1）预测代理人下一天的销售总额以及销售总票数
       所用模型：回归模型 
       算法描述：统计每个代理人每天的销售额，使用spark建
       立线性回归，预测下一天的销售额，其中features包含前5天平均销售额、代理人等级，label为明天销售额。
      
    （2）预测代理人下一天可能交易的买方。
      所用算法：SVM算法
      算法描述：根据原始表sales_log新建表predict_sale包
	含信息有：saler、buyer、times（交易次数）、num、round。
	处理方式将sales_log中14天内每个（代理人）saler和
	  buyer的交易次数、交易票数、交易额统计出来使用SVM
	算法进行训练，训练出来的模型使用第15天的数据进
	行测试。

	所有代理人分层显示
<img src="https://github.com/luckyhard/TicketAnalysis/blob/master/images/1.png"/>

    指定代理人查看其某天的销售信息

<img src="https://github.com/luckyhard/TicketAnalysis/blob/master/images/2.png"/>

    对指定代理人预测其明天的销售总票数，总金额:

<img src="https://github.com/luckyhard/TicketAnalysis/blob/master/images/3.png"/>

    查看代理人15天内每天的平均销售资金:
<img src="https://github.com/luckyhard/TicketAnalysis/blob/master/images/4.png"/>

    心得：通过这次民航系统的开发，简单了解了大数据，并独立搭建了大数据分析的平台，学会简单使用Spark进行数据分析。

    



