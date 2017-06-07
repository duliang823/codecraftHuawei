package com.cacheserverdeploy.deploy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Deploy
{
    
    public static String[] deployServer(String[] graphContent)
    {
    	
    	String[] arr = graphContent[0].split(" ");   //获取第一行数据  
    	int ServerCost = Integer.parseInt(graphContent[2].split(" ")[0]); //服务器费用  eg:100
//    	System.out.println(ServerCost);
    	
    	int NetworkPoint_num = Integer.parseInt(arr[0]);  //网络节点的数量
    	int NetworkLink_num = Integer.parseInt(arr[1]);  //网络链路的数量
    	int ConsumePoint_num = Integer.parseInt(arr[2]);  //消费节点的数量
    	int  rowNum_3_4 = graphContent.length-ConsumePoint_num-1;   //第3段和第4段之间空行的行号,eg：62-12-1=49
    	
    	
    	//将网络节点，网络链路，消费节点注入到NetworkPoint类中
    	NetworkPoint networkPoint = new NetworkPoint();
    	networkPoint.setGraphContent(graphContent);
    	networkPoint.setConsumePoint_num(ConsumePoint_num);
    	networkPoint.setNetworkLink_num(NetworkLink_num);
    	networkPoint.setNetworkPoint_num(NetworkPoint_num);
    	networkPoint.setRowNum_3_4(rowNum_3_4);
    	
    	/****************获取起始ID，终止ID，总带宽，租费的二维数组****************/
    	int[][] NetworkLinkArray = networkPoint.getNetworkLinkArray();
    	/****************获取消费节点ID，相连节点ID，带宽需求二维数组****************/
    	int[][] ConsumeLinkArray = networkPoint.getConsumeLinkArray();
    	
    	/****************计算出服务器处的位置数组****************/
//    	int serverNum = (int) (NetworkPoint_num*0.1+0.5);  //定义服务器数量  //网络节点数*0.1然后四舍五入
    	int serverNum = NetworkPoint_num; //把长度定为网络节点数
    	ServerPosition serverPosition = new ServerPosition();
    	int[] ServerPositionArray = serverPosition.getServerPositionArray(NetworkLinkArray, serverNum);
    	ArrayList<Integer> ServerPositionArray_ArrayList = Util_ArrayToList.toArrayList(ServerPositionArray);
    	
        /****************计算出消费节点处的位置数组****************/
    	ConsumePoint consumePoint = new ConsumePoint();
    	int[] ConsumePointPositionArray = consumePoint.getConsumePointPositionArray(ConsumeLinkArray);
    	Arrays.sort(ConsumePointPositionArray); //给消费节点数组从小到大排序
    	
    	/****************计算出网络节点处的位置数组****************/
    	PointUtil pointUtil = new PointUtil();
    	int[]  NetworkPointPositionArray = pointUtil.getNetworkPoint(NetworkLinkArray);
    	pointUtil.setServerPositionArray(ServerPositionArray);  //将服务器处的位置数组注入到PointUtil中//
    	
    	/****************计算出消费节点相邻节点的位置数组(起始点)****************/ 
    	int[] ConsumeBorderPointPositionArray = consumePoint.getConsumeBorderPointPositionArray(ConsumeLinkArray);
    	pointUtil.setConsumeBorderPointPositionArray(ConsumeBorderPointPositionArray);  //将消费节点相邻节点的位置数组注入到PointUtil中//
    	ArrayList<Integer> ConsumeBorderPointPositionArray_ArrayList = Util_ArrayToList.toArrayList(ConsumeBorderPointPositionArray);
    	
    	/****************计算出各个点之间带宽****************/ //取法：BwMap.get("Bw0_16")
    	Map<String, Integer> BwMap=new HashMap<String, Integer>(); 
    	BandwidthUtil bandwidthUtil = new BandwidthUtil();
    	BwMap = bandwidthUtil.getP2PBandwidth(NetworkLinkArray);
    	bandwidthUtil.setBwMap(BwMap); //注入各个点之间带宽到BandwidthUtil类中//
    	
    	
    	/****************计算与消费节点相邻点之间的带宽需求(起始点需求的带宽)****************/ //取法：BwNeedMap.get("BwNeed_8")
    	Map<String, Integer> BwNeedMap=new HashMap<String, Integer>(); 
    	BwNeedMap = bandwidthUtil.getBorderPointBwNeed(ConsumeLinkArray);
    	
    	/****************计算出各个点的费用****************/  //取法：CostMap.get("Cost0_16")
    	Map<String, Integer> CostMap=new HashMap<String, Integer>(); 
    	CostUtil costUtil = new CostUtil();
    	CostMap = costUtil.getP2PCost(NetworkLinkArray);
    	costUtil.setCostMap(CostMap);
    	
    	
    	/****************计算出各个网络节点相邻有哪些节点****************/  //取法：PointMap.get("NextPoint_0")
    	Map<String, ArrayList<Integer>> PointMap=new HashMap<String, ArrayList<Integer>>(); 
    	PointMap = pointUtil.getNextPoint(NetworkLinkArray);
    	pointUtil.setPointMap(PointMap); //将网络节点相邻有哪些节点注入到PointUtil中//
    	
    	  
    	/****************获取消费节点挨着的是什么网络节点****************/  //取法：ConsumePointMap.get("NextPoint_0")
    	Map<String, Integer> ConsumePointMap=new HashMap<String, Integer>(); 
    	ConsumePointMap = pointUtil.getConsumePointNextPoint(ConsumeLinkArray);
    	
    	/****************获取挨着消费节点的网络节点处是哪个消费节点****************/  //取法：BorderPointMap.get("NextPoint_8")
    	Map<String, Integer> BorderPointMap=new HashMap<String, Integer>(); 
    	BorderPointMap = pointUtil.getBorderPointNextPoint(ConsumeLinkArray);
    	pointUtil.setBorderPointMap(BorderPointMap);
    	
    	
    	
    	/********************************找服务器_开始********************************/
    	ArrayList<Integer> Used_ServerPoints = new ArrayList<Integer>();  //已经找出服务器的点的ArrayList数组
    	ArrayList<Integer> Used_BorderPoints = new ArrayList<Integer>();  //已经找出BorderPoint点的ArrayList数组
    	
    	/*******1.第一次找V型的服务器_开始*******/
    	for(int i=0;i<ConsumeBorderPointPositionArray_ArrayList.size();i++){ //遍历起始点
    		int nowBorderPoint = ConsumeBorderPointPositionArray_ArrayList.get(i);  //当前的起始点
//    		int nowBorderPoint = 16; //测试起始点
    		if(Used_BorderPoints.contains(nowBorderPoint) == false){ //这个起始点没有用过
    			ArrayList<Integer> AroundPoints_nowBorderPoint = PointMap.get("NextPoint_"+nowBorderPoint);  //起始点周围的网络节点
//        		System.out.println(nowBorderPoint+"点周围的所有网络节点："+AroundPoints_nowBorderPoint);
        		int LinkMaxnum = 0; //起始点的节点2连接有效起始点最大的数量
        		int CostMinLink = 2147483641; //最小费用
        		ArrayList<Integer> BetterBorderPoint = new ArrayList<Integer>();  //存放更优的节点2周围的起始点
        		int BetterServerPoint = 2147483640;  //线路最多的服务器
        		for(int j=0;j<AroundPoints_nowBorderPoint.size();j++){  //0,  1
        			int NextPoint = AroundPoints_nowBorderPoint.get(j); //16以内的一个点，节点2
        			if((Used_BorderPoints.contains(NextPoint) == false) && (Used_ServerPoints.contains(NextPoint) == false) && ((BwMap.get("Bw"+NextPoint+"_"+nowBorderPoint)) >= (BwNeedMap.get("BwNeed_"+nowBorderPoint)))){ //(节点2（是起始点的情况）没被用过)  && (并且节点2不是服务器，) && (并且节点2到目前的起始点nowBorderPoint的带宽 >= 该起始点的带宽)
        				ArrayList<Integer> AroundPoints_NextPoint = PointUtil.NetworkPoint_Around_ConsumeBorderPoint(NextPoint);//节点2周围所有的起始点
//            			System.out.println(NextPoint+"节点2周围的起始点："+AroundPoints_NextPoint);
            			ArrayList<Integer> tempBorderPoint_X_s = new ArrayList<Integer>(); //存放临时可用的起始点
            			int Cost_nowAroundPoint = 0;   //当前节点2到 节点2周围满足的起始点的费用总和
            			//遍历节点2周围所有的起始点，找满足要求的起始点：
            			for(int x=0;x<AroundPoints_NextPoint.size();x++){  // 16 8 7 3
            				int tempBorderPoint_X = AroundPoints_NextPoint.get(x);  //节点2周围的一个起始点X
            				if(Used_BorderPoints.contains(tempBorderPoint_X) == false){ //tempBorderPoint_X这个点不在用过的起始点内
            					int Bw_NextPoint_tempBorderPoint_X = BwMap.get("Bw"+NextPoint+"_"+tempBorderPoint_X); //节点2和 当前的起始点X之间的带宽
                    			int BwNeed_tempBorderPoint_X = BwNeedMap.get("BwNeed_"+tempBorderPoint_X);//当前的节点X需求的带宽
                    			if(Bw_NextPoint_tempBorderPoint_X >= BwNeed_tempBorderPoint_X){ //带宽满足要求eg : 1-16的带宽 >= 16的带宽
                    				//把这个起始点X放入临时存起始点tempBorderPoint_X_s的数组中
                    				if(tempBorderPoint_X_s.contains(tempBorderPoint_X) == false){
                    					tempBorderPoint_X_s.add(tempBorderPoint_X);
                    				}
                    				//并把这个起始点到节点2的费用加上
                    				Cost_nowAroundPoint += CostMap.get("Cost"+NextPoint+"_"+tempBorderPoint_X) * BwNeed_tempBorderPoint_X; //NextPoint_tempBorderPoint_X之间的费用
                    			}
            				}
            			}
            			//如果节点2找到周围的起始点至少有2个起始点满足要求的：
            			if(tempBorderPoint_X_s.size() > 1){
            				if(tempBorderPoint_X_s.size() > LinkMaxnum){ //如果找到的点更多，取最多的
            					LinkMaxnum = tempBorderPoint_X_s.size();
            					CostMinLink = Cost_nowAroundPoint;
            					BetterServerPoint = NextPoint;
            					BetterBorderPoint = tempBorderPoint_X_s;
            				}else if(tempBorderPoint_X_s.size() == LinkMaxnum){ //如果找到的点相同，取费用更优的
            					 if(CostMinLink > Cost_nowAroundPoint){ //费用小于以前找到的费用
            						 LinkMaxnum = tempBorderPoint_X_s.size();
            						 CostMinLink = Cost_nowAroundPoint;
            						 BetterServerPoint = NextPoint;
                 					 BetterBorderPoint = tempBorderPoint_X_s;
            					 }
            				}
            			}
            			
        			}
        		}
        		//把更好的服务器加入到Used_ServerPoints中，把用过的起始点加入到Used_BorderPoints中：
        		if(BetterServerPoint != 2147483640){ //找到可用的服务器
        			//把更好的服务器加入到Used_ServerPoints中：
        			if(Used_ServerPoints.contains(BetterServerPoint) == false){
        				Used_ServerPoints.add(BetterServerPoint);
        			}
        			if(ConsumeBorderPointPositionArray_ArrayList.contains(BetterServerPoint)){ //如果这个服务器点是起始点，需要把这个点也算作使用了的起始点：
        				if(Used_BorderPoints.contains(BetterServerPoint) == false){
        					Used_BorderPoints.add(BetterServerPoint);
        				}
        			}
        			//把用过的起始点加入到Used_BorderPoints中：
        			Used_BorderPoints.addAll(BetterBorderPoint);
        		}
    		}
    	}
//    	System.out.println("第1.次优化后服务器位置："+Used_ServerPoints);
//    	System.out.println("第1.次优化后被使用的起始点："+Used_BorderPoints);
    	/*******1.第一次找V型的服务器_结束*******/
    	
    	
    	/*******2.第二次找起始点相邻的服务器_开始*******/
    	for(int i=0;i<ConsumeBorderPointPositionArray_ArrayList.size();i++){ //遍历起始点
    		int nowBorderPoint = ConsumeBorderPointPositionArray_ArrayList.get(i);  //当前的起始点
//    		int nowBorderPoint = 24; //测试起始点
    		if(Used_BorderPoints.contains(nowBorderPoint) == false){ //这个起始点没被使用过
    			int BwNeed_nowBorderPoint = BwNeedMap.get("BwNeed_"+nowBorderPoint); //起始点需求的带宽
    			ArrayList<Integer> AroundPoints_nowBorderPoint = PointUtil.NetworkPoint_Around_ConsumeBorderPoint(nowBorderPoint); //起始点周围起始点
//    			System.out.println(nowBorderPoint+"起始点周围的起始点："+AroundPoints_nowBorderPoint);
    			for(int j=0;j<AroundPoints_nowBorderPoint.size();j++){  //去遍历起始点周围的起始点数组
    				int tempNextBorderPoint = AroundPoints_nowBorderPoint.get(j); //起始点下的另一个起始点2
    				if(Used_BorderPoints.contains(tempNextBorderPoint) == false){ //这个起始点2没有使用过
    					int BwNeed_tempNextBorderPoint = BwNeedMap.get("BwNeed_"+tempNextBorderPoint);//第二个的起始点需要的带宽
    					int Bw_tempNextBorderPoint_nowBorderPoint = BwMap.get("Bw"+tempNextBorderPoint+"_"+nowBorderPoint);//起始点2到起始点之间的带宽 eg：22到 24之间的带宽
    					int tempServerPoint = (BwNeed_nowBorderPoint > BwNeed_tempNextBorderPoint) ? nowBorderPoint : tempNextBorderPoint; //取起始点需求带宽大的点作为服务器
    					int newNextBorderPoint = (BwNeed_nowBorderPoint > BwNeed_tempNextBorderPoint) ? tempNextBorderPoint : nowBorderPoint;   //带宽需求小的起始点作为起始点2
    					int BwNeed_newNextBorderPoint = BwNeedMap.get("BwNeed_"+newNextBorderPoint); //新的起始点2 需求的带宽
    					if(Bw_tempNextBorderPoint_nowBorderPoint >= BwNeed_newNextBorderPoint){ //如果带宽满足需求
    						//把这两个点都加到被使用了的起始点数组Used_BorderPoints中：
    						if(Used_BorderPoints.contains(tempServerPoint) ==false){
    							Used_BorderPoints.add(tempServerPoint);
    						}
    						if(Used_BorderPoints.contains(newNextBorderPoint) ==false){
    							Used_BorderPoints.add(newNextBorderPoint);
    						}
    						//把tempServerPoint添加到服务器数组Used_ServerPoints中：
    						if(Used_ServerPoints.contains(tempServerPoint) == false){
    							Used_ServerPoints.add(tempServerPoint);
    						}
    					}
    				}
    			}
    		}
    	}
//    	System.out.println("第2.次优化后服务器位置："+Used_ServerPoints);
//    	System.out.println("第2.次优化后被使用的起始点："+Used_BorderPoints);
    	/*******2.第二次找起始点相邻的服务器_结束*******/
    	
    	
    	/*******3.第三次找起始点绕行的服务器_开始*******/
    	//获取没被使用的起始点X和起始点X对应需求的带宽数组：
    	ArrayList<Integer> PointsAndBw_NoUse_BorderPoints = new ArrayList<Integer>(); //没被使用过的起始点加带宽信息数组
    	for(int i=0;i<ConsumeBorderPointPositionArray_ArrayList.size();i++){ //遍历起始点
    		int nowBorderPoint = ConsumeBorderPointPositionArray_ArrayList.get(i); //当前的起始点
    		if(Used_BorderPoints.contains(nowBorderPoint) == false){ //如果这个起始点没有被使用过
    			int BwNeed_nowBorderPoint = BwNeedMap.get("BwNeed_"+nowBorderPoint);//该起始点需求的带宽
    			PointsAndBw_NoUse_BorderPoints.add(nowBorderPoint);  //把没被使用的起始点X放入PointsAndBw_NoUse_BorderPoints中
    			PointsAndBw_NoUse_BorderPoints.add(BwNeed_nowBorderPoint); //把没被使用的起始点X的带宽放入PointsAndBw_NoUse_BorderPoints中
    		}
    	}
    	//将PointsAndBw_NoUse_BorderPoints按照起始点需求的带宽由大到小排序：
    	int[] PointsAndBw_NoUse_BorderPoints_Arrays = Util_ArrayToList.toArrays(PointsAndBw_NoUse_BorderPoints);
    	for(int i=1;i<PointsAndBw_NoUse_BorderPoints_Arrays.length;i=i+2){
    		for(int j=i+2;j<PointsAndBw_NoUse_BorderPoints_Arrays.length;j=j+2){
    			if(PointsAndBw_NoUse_BorderPoints_Arrays[i] < PointsAndBw_NoUse_BorderPoints_Arrays[j]){
    				int temp1 = PointsAndBw_NoUse_BorderPoints_Arrays[i];
					int temp2 = PointsAndBw_NoUse_BorderPoints_Arrays[i-1];
					PointsAndBw_NoUse_BorderPoints_Arrays[i] = PointsAndBw_NoUse_BorderPoints_Arrays[j];
					PointsAndBw_NoUse_BorderPoints_Arrays[i-1] = PointsAndBw_NoUse_BorderPoints_Arrays[j-1];
					PointsAndBw_NoUse_BorderPoints_Arrays[j] = temp1;
					PointsAndBw_NoUse_BorderPoints_Arrays[j-1] = temp2;
    			}
    		}
    	}
    	//把没有过的起始点按照起始点需求的带宽由大到小将起始点放入NoUse_BorderPoints中：
    	ArrayList<Integer> NoUse_BorderPoints = new ArrayList<Integer>(); //没被使用过的起始点数组
    	for(int i=0;i<PointsAndBw_NoUse_BorderPoints_Arrays.length;i=i+2){
    		NoUse_BorderPoints.add(PointsAndBw_NoUse_BorderPoints_Arrays[i]);
    	}
    	
//    	System.out.println("第2.次优化后没被使用过的起始点："+NoUse_BorderPoints);
    	for(int i=0;i<NoUse_BorderPoints.size();i++){//遍历没被使用过的起始点
    		int nowBorderPoint = NoUse_BorderPoints.get(i);
//    		int nowBorderPoint = 22;  //测试的起始点
    		if(Used_BorderPoints.contains(nowBorderPoint) == false){ //这个起始点没被使用过
    			ArrayList<Integer> AroundPoints_nowBorderPoint = PointMap.get("NextPoint_"+nowBorderPoint); //起始点周围的网络节点数组
//    			System.out.println(nowBorderPoint+"起始点周围所有的网络节点："+AroundPoints_nowBorderPoint);
    			for(int j=0;j<AroundPoints_nowBorderPoint.size();j++){  
    				int NextPoint = AroundPoints_nowBorderPoint.get(j);  //起始点相邻的节点  节点2
    				if((Used_ServerPoints.contains(NextPoint) == false) && (Used_BorderPoints.contains(NextPoint) == false)){ //节点2不是服务器点，不是使用过的起始点
    					int Bw_nowBorderPoint_NextPoint = BwMap.get("Bw"+nowBorderPoint+"_"+NextPoint);//起始点和节点2之间的带宽
    					ArrayList<Integer> AroundPoints_NextPoint = PointUtil.NetworkPoint_Around_ConsumeBorderPoint(NextPoint);  //节点2周围所有的起始点
//    					System.out.println(NextPoint+"节点2周围所有的起始点："+AroundPoints_NextPoint);
    					for(int v=0;v<AroundPoints_NextPoint.size();v++){
    						int NextPoint_3 = AroundPoints_NextPoint.get(v); //节点2周围的起始点  节点2
    						if((NextPoint_3 != nowBorderPoint) && (Used_ServerPoints.contains(NextPoint_3) == false) && (Used_BorderPoints.contains(NextPoint_3) == false)){ //这个点不是nowBorderPoint，不是服务器点，不是使用过的起始点
    							int BwNeed_NextPoint_3 = BwNeedMap.get("BwNeed_"+NextPoint_3);//节点3（起始点）需求的带宽
    							int Bw_NextPoint_NextPoint_3 =  BwMap.get("Bw"+NextPoint+"_"+NextPoint_3);//节点2和节点3之间的带宽
    							if((Bw_NextPoint_NextPoint_3 >= BwNeed_NextPoint_3) && (Bw_nowBorderPoint_NextPoint >= BwNeed_NextPoint_3)){
    								//把nowBorderPoint当做服务器加入Used_ServerPoints中：
    								if(Used_ServerPoints.contains(nowBorderPoint) == false){
    									Used_ServerPoints.add(nowBorderPoint);
    								}
    								//把nowBorderPoint和NextPoint_3加入Used_BorderPoints中：
    								if(Used_BorderPoints.contains(nowBorderPoint) == false){
    									Used_BorderPoints.add(nowBorderPoint);
    								}
    								if(Used_BorderPoints.contains(NextPoint_3) == false){
    									Used_BorderPoints.add(NextPoint_3);
    								}
    								break;
    							}
    						}
    					}
    				}
    			}
    		}
    	}
//		System.out.println("第3.次优化后服务器位置："+Used_ServerPoints);
//    	System.out.println("第3.次优化后被使用的起始点："+Used_BorderPoints);	
    	/*******3.第三次找起始点绕行的服务器_结束*******/
    	/********************************找服务器_结束********************************/
    	
    	
    	
    	/////////////////////////////////////////////////////////////////////////////////////
    	ArrayList<Integer> List_BorderPoints = Used_BorderPoints; 
    	for(int i=0;i<ConsumeBorderPointPositionArray_ArrayList.size();i++){
    		int nowBorderPoint = ConsumeBorderPointPositionArray_ArrayList.get(i);
    		if(List_BorderPoints.contains(nowBorderPoint) == false){
    			List_BorderPoints.add(nowBorderPoint);
    		}
    	}
    	ConsumeBorderPointPositionArray_ArrayList = List_BorderPoints;
    	
    	
    	//把以上找出的服务器点放在serverPoints中：
    	ArrayList<Integer> serverPoints = new ArrayList<Integer>();
//    	serverPoints.add(1);
//    	serverPoints.add(4);
//    	serverPoints.add(22);
    	serverPoints = Used_ServerPoints;  ////****起始点找出的服务器
    	
    	/********************************找路径_开始********************************/
    	
    	
    	
    	
    	/*******1.第一次根据之前找出的服务器找路径_开始*******/
    	Map<String, Integer> BwMap_New=new HashMap<String, Integer>(); 
    	for(Iterator it = BwMap.keySet().iterator();it.hasNext();){
    		String key = it.next().toString();
    		BwMap_New.put(key, BwMap.get(key));
    	}
    	
    	ArrayList<ArrayList<Integer>> AllLinks = new ArrayList<ArrayList<Integer>>(); //总的链路
    	for(int i=0;i<ConsumeBorderPointPositionArray_ArrayList.size();i++){
    		int startPoint = ConsumeBorderPointPositionArray_ArrayList.get(i);
    		int nextPoint = startPoint;  //下一个点,第一次下一个点等于起始点
    		ArrayList<Integer> used_NetWorkPoints = new ArrayList<Integer>();//路径中已经使用过的网络点
    		used_NetWorkPoints.add(startPoint);  //第一次使用过的网络点有起始点
    		int BwNeed_startPoint = BwNeedMap.get("BwNeed_"+startPoint);
    		ArrayList<Integer> resultLink = findPath(startPoint,BwNeed_startPoint,nextPoint,used_NetWorkPoints,serverPoints,ServerCost,0,BwMap_New);
    		AllLinks.add(resultLink);
    		//更新带宽:
    		int usedBw = resultLink.get(resultLink.size()-1);  //取最后一个数是带宽
    		for(int g=0;g<resultLink.size()-3;g++){
    			int NetworkPoint1 = resultLink.get(g);
    			int NetworkPoint2 = resultLink.get(g+1);
    			int oldBw_NetworkPoint1_NetworkPoint2 = BwMap_New.get("Bw"+NetworkPoint1+"_"+NetworkPoint2);
    			BwMap_New.put("Bw"+NetworkPoint1+"_"+NetworkPoint2, oldBw_NetworkPoint1_NetworkPoint2 - usedBw);
    			BwMap_New.put("Bw"+NetworkPoint2+"_"+NetworkPoint1, oldBw_NetworkPoint1_NetworkPoint2 - usedBw);
    		}
    	}
    	/*******1.第一次根据之前找出的服务器找路径_结束*******/
    	
    	
    	/*******2.第二次根据所有网络节点找路径_开始*******/
    	ArrayList<Integer> Better_ServerPoints = PointUtil.getValidServerPoints(AllLinks, Used_ServerPoints); //更好的服务器数组  //第一次更好的服务器数组等于之前找到的服务器数组
    	ArrayList<ArrayList<Integer>> Better_AllLinks = PointUtil.handleAllLinks(AllLinks, Used_ServerPoints);  //更好的链路
    	int BetterAllCost = CostUtil.calculateAllCost(Better_AllLinks, ServerCost); //更好的费用（选小的）  //第一次就是上面的费用
    	
    	for(int a=0;a<ServerPositionArray_ArrayList.size();a++){
    		int nowNetworkPoint = ServerPositionArray_ArrayList.get(a);
//    		int nowNetworkPoint = 0; //测试
    		if(Better_ServerPoints.contains(nowNetworkPoint) == false){ //这个网络节点不在Better_ServerPoints中：
    			//构建新的BwMap_New：
    			Map<String, Integer> BwMap_New2=new HashMap<String, Integer>(); 
    	    	for(Iterator it = BwMap.keySet().iterator();it.hasNext();){
    	    		String key = it.next().toString();
    	    		BwMap_New2.put(key, BwMap.get(key));
    	    	}
    	    	//添加一个网络节点到服务器中 :
    	    	if(Better_ServerPoints.contains(nowNetworkPoint) == false){ 
    	    		Better_ServerPoints.add(nowNetworkPoint); 
    	    	}
    	    	
    	    	ArrayList<ArrayList<Integer>> NowAllLinks = new ArrayList<ArrayList<Integer>>(); //当前服务器点的总的链路
    	    	for(int i=0;i<ConsumeBorderPointPositionArray_ArrayList.size();i++){  //遍历所有起始点
    	    		int startPoint = ConsumeBorderPointPositionArray_ArrayList.get(i);
    	    		int nextPoint = startPoint;  //下一个点,第一次下一个点等于起始点
    	    		ArrayList<Integer> used_NetWorkPoints = new ArrayList<Integer>();//路径中已经使用过的网络点
    	    		used_NetWorkPoints.add(startPoint);  //第一次使用过的网络点有起始点
    	    		int BwNeed_startPoint = BwNeedMap.get("BwNeed_"+startPoint);
    	    		
    	    		
    	    		ArrayList<Integer> resultLink = findPath(startPoint,BwNeed_startPoint,nextPoint,used_NetWorkPoints,Better_ServerPoints,ServerCost,0,BwMap_New2);
    	    		NowAllLinks.add(resultLink);
    	    		//更新带宽:
    	    		int usedBw = resultLink.get(resultLink.size()-1);  //取最后一个数是带宽
    	    		for(int g=0;g<resultLink.size()-3;g++){
    	    			int NetworkPoint1 = resultLink.get(g);
    	    			int NetworkPoint2 = resultLink.get(g+1);
    	    			int oldBw_NetworkPoint1_NetworkPoint2 = BwMap_New2.get("Bw"+NetworkPoint1+"_"+NetworkPoint2);
    	    			BwMap_New2.put("Bw"+NetworkPoint1+"_"+NetworkPoint2, oldBw_NetworkPoint1_NetworkPoint2 - usedBw);
    	    			BwMap_New2.put("Bw"+NetworkPoint2+"_"+NetworkPoint1, oldBw_NetworkPoint1_NetworkPoint2 - usedBw);
    	    		}
    	    	}
    	    	if(BetterAllCost > CostUtil.calculateAllCost(NowAllLinks, ServerCost)){ //费用比之前的小
    	    		BetterAllCost = CostUtil.calculateAllCost(NowAllLinks, ServerCost);
    	    		Better_AllLinks = NowAllLinks;
    	    	}else{ //费用不比之前的小,则需要把加入服务器的网络节点删除
    	    		Better_ServerPoints.remove(Better_ServerPoints.get(Better_ServerPoints.size()-1));
    	    	}
    		}
    	}
    	
    	Better_AllLinks = PointUtil.handleAllLinks(Better_AllLinks, Better_ServerPoints);
    	/*******2.第二次根据所有网络节点找路径_结束*******/
    	/********************************找路径_结束********************************/
    	
    	
    	//输出当前的Cost:
//    	System.out.println("Cost = " + CostUtil.calculateAllCost(Better_AllLinks, ServerCost));  
    	/**********************把路径添加到输出restlutStr[]中_开始**********************/
    	int restlutStr_i = 1;
    	String[] restlutStr = new String[Better_AllLinks.size()+1];
    	restlutStr[0] = Better_AllLinks.size() + " \n";
    	
    	for(int i=0;i<Better_AllLinks.size();i++){
    		restlutStr[restlutStr_i++] = Util_ArrayToList.toString(Better_AllLinks.get(i));
    	}
    	/**********************把路径添加到输出restlutStr[]中_结束**********************/
    	
    	
        return restlutStr;
    }
    
    
    
    /**
     * 
     * <p>Title: findPath</p>
     * <p>Description: 找单条路线满足带宽的路径</p>
     * @param @param startPoint
     * @param @param BwNeed_startPoint
     * @param @param nextPoint
     * @param @param used_NetWorkPoints
     * @param @param serverPoints
     * @param @param ServerCost
     * @param @param LinkCost
     * @param @return   
     * @return ArrayList<Integer>  
     * @throws
     * @date 2017-3-29下午5:11:40
     */
    public static ArrayList<Integer> findPath(int startPoint,int BwNeed_startPoint,int nextPoint,ArrayList<Integer> used_NetWorkPoints,ArrayList<Integer> serverPoints,int ServerCost,int LinkCost,Map<String, Integer> BwMap_New){
    	
    	if(serverPoints.contains(nextPoint)){ //当前点是服务器点
    		ArrayList<Integer> resultLink = new ArrayList<Integer>();
    		for(int i=used_NetWorkPoints.size()-1;i>=0;i--){
    			resultLink.add(used_NetWorkPoints.get(i));
    		}
    		resultLink.add(PointUtil.BorderPointMap.get("NextPoint_"+startPoint));
    		resultLink.add(BwNeed_startPoint);
    		return resultLink;  //
    	}
    	
    	
    	int nowusableNextPoint = 2147483640;
    	if(serverPoints.contains(nextPoint) == false){ //当前点不是服务器点
    		int nowPoint = nextPoint;   //把下一个点当做当前点
    		ArrayList<Integer> newNextPoints = PointUtil.PointMap.get("NextPoint_"+nowPoint);//当前点的下个点数组  //12 14 15
			ArrayList<Integer> pointAndBw = new ArrayList<Integer>();  //一个数组，前面存点名称，后面存带宽 eg:a = {12,15,14,11,15,27};
			for(int v=0;v<newNextPoints.size();v++){
    			int newNextPoint = newNextPoints.get(v);  //12 15 14 中的其中一个
    			if(used_NetWorkPoints.contains(newNextPoint) == false){ //这个点12 15 14 不在已经走过的路径中的点
    				int Bw_nextPoint_nowPoint = BwMap_New.get("Bw"+nowPoint+"_"+newNextPoint);//12 15 14 中的其中一个点到13点之间的带宽
    				int Cost_nextPoint_nowPoint = CostUtil.CostMap.get("Cost"+nowPoint+"_"+newNextPoint);//12 15 14 中的其中一个点到13点之间的费用
        			pointAndBw.add(newNextPoint);
        			pointAndBw.add(Bw_nextPoint_nowPoint);
        			pointAndBw.add(Cost_nextPoint_nowPoint);
    			}
			}
			//采用冒泡排序：
			int[] pointAndBw_new = Util_Sort.ArraySort(Util_ArrayToList.toArrays(pointAndBw), serverPoints);
			int flag_nolink = 1;  //没有链路的标识，1表示没有链路
			for(int m=0;m<pointAndBw_new.length;m=m+3){
				nowusableNextPoint = pointAndBw_new[m];
				int Bw_nowusableNextPoint_nowPoint = pointAndBw_new[m+1];
				if(Bw_nowusableNextPoint_nowPoint >= BwNeed_startPoint){
//					System.out.println("找到的下个点:"+nowusableNextPoint);
					if(used_NetWorkPoints.contains(nowusableNextPoint) == false){
						used_NetWorkPoints.add(nowusableNextPoint);
					}
					LinkCost += CostUtil.CostMap.get("Cost"+nowPoint+"_"+nowusableNextPoint) * BwNeed_startPoint;
//					System.out.println(LinkCost);
					flag_nolink = 0;
					break;
				}else{
					flag_nolink = 1;
				}
			}
			
			if((flag_nolink == 1) || (LinkCost >= ServerCost)){ //没有链路  //或者线路费用大于等于服务器费用
				ArrayList<Integer> resultLink = new ArrayList<Integer>();
				resultLink.add(startPoint);
				resultLink.add(PointUtil.BorderPointMap.get("NextPoint_"+startPoint));
	    		resultLink.add(BwNeed_startPoint);
				return resultLink;
			}
    		
    	}
    	
    	return findPath(startPoint,BwNeed_startPoint,nowusableNextPoint,used_NetWorkPoints,serverPoints,ServerCost,LinkCost,BwMap_New);
    }
    
}

