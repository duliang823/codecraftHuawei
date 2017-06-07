package com.cacheserverdeploy.deploy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CostUtil {
	public static Map<String, Integer> CostMap;
	
	
	public static Map<String, Integer> getCostMap() {
		return CostMap;
	}
	public static void setCostMap(Map<String, Integer> costMap) {
		CostMap = costMap;
	}


	public Map getP2PCost(int[][] NetworkLinkArray){
		Map<String, Integer> CostMap=new HashMap<String, Integer>(); 
		String tempKey = "";
		int tempValue = 0;
		//把NetworkLinkArray二维数组的宽度存入
		for(int Netw_row=0;Netw_row<NetworkLinkArray.length;Netw_row++){
			tempKey = "Cost"+NetworkLinkArray[Netw_row][0]+"_"+NetworkLinkArray[Netw_row][1];
			tempValue = NetworkLinkArray[Netw_row][3];
			CostMap.put(tempKey, tempValue);
			tempKey = "Cost"+NetworkLinkArray[Netw_row][1]+"_"+NetworkLinkArray[Netw_row][0];
			CostMap.put(tempKey, tempValue);
		}
		
		return CostMap;
	}
	
	/**
	 * 
	 * <p>Title: calculateAllCost</p>
	 * <p>Description: 计算输出链路的总费用</p>
	 * @param @param Links
	 * @param @param ServerCost
	 * @param @return   
	 * @return int  
	 * @throws
	 * @date 2017-3-30上午11:28:43
	 */
	public static int calculateAllCost(ArrayList<ArrayList<Integer>> Links,int ServerCost){
		ArrayList<Integer> serverPoints = new ArrayList<Integer>();  //存放链路的服务器
		int CostLink = 0;  //链路的费用，没有包括服务器的
		for(int i=0;i<Links.size();i++){
			ArrayList<Integer> nowLink = Links.get(i);  //当前的一条链路
			
			//添加服务器到serverPoints数组中：
			int tempServerPoint =  nowLink.get(0);  //第0个是服务器
			if(serverPoints.contains(tempServerPoint) == false){
				serverPoints.add(tempServerPoint);
			}
			
			//计算链路上的费用：
			for(int j=0;j<nowLink.size()-3;j++){
				int NetworkPoint1 = nowLink.get(j);
				int NetworkPoint2 = nowLink.get(j+1);
				CostLink += CostMap.get("Cost"+NetworkPoint1+"_"+NetworkPoint2) * nowLink.get(nowLink.size()-1);
			}
		}
		int allCost = (serverPoints.size() * ServerCost) + CostLink;
		return allCost;
	}
	
	
}
