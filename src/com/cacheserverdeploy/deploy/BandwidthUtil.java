package com.cacheserverdeploy.deploy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BandwidthUtil {
	public static Map<String, Integer> BwMap; //各个点之间带宽
	
	public static Map<String, Integer> getBwMap() {
		return BwMap;
	}
	public static void setBwMap(Map<String, Integer> bwMap) {
		BwMap = bwMap;
	}
	
	
	/**
	 * 
	 * <p>Title: getAroundBw</p>
	 * <p>Description: 获取一个网络节点周围所有的带宽之和</p>
	 * @param @param Networkpoint
	 * @param @return   
	 * @return int  
	 * @throws
	 * @date 2017-3-28上午10:16:37
	 */
	public static int getAroundBw(int Networkpoint){
		ArrayList<Integer> aroundPoints = PointUtil.PointMap.get("NextPoint_"+Networkpoint);//Networkpoint周围所有的点
		int Bw_pointAllAroundPoints = 0; //周围点的带宽总和
		for(int i=0;i<aroundPoints.size();i++){
			int nextPoint = aroundPoints.get(i);
			int Bw_nextPoint_Networkpoint = BwMap.get("Bw"+nextPoint+"_"+Networkpoint);
			Bw_pointAllAroundPoints += Bw_nextPoint_Networkpoint;
		}
		return Bw_pointAllAroundPoints;
	}
	
	/**
	 * 
	 * <p>Title: updateP2PBw</p>
	 * <p>Description: 根据两个网络节点和这两个网络节点之间用了的带宽，更新这两个节点之间的带宽</p>
	 * @param @param NetworkPoint1
	 * @param @param NetworkPoint2
	 * @param @param usedBw   
	 * @return void  
	 * @throws
	 * @date 2017-3-24下午1:28:43
	 */
	public static void updateP2PBw(int NetworkPoint1,int NetworkPoint2,int usedBw){
		int oldBw_NetworkPoint1_NetworkPoint2 = BwMap.get("Bw"+NetworkPoint1+"_"+NetworkPoint2);
		BwMap.put("Bw"+NetworkPoint1+"_"+NetworkPoint2, oldBw_NetworkPoint1_NetworkPoint2 - usedBw);
		BwMap.put("Bw"+NetworkPoint2+"_"+NetworkPoint1, oldBw_NetworkPoint1_NetworkPoint2 - usedBw);
	}
	
	/**
	 * 
	 * <p>Title: updateLinkBw</p>
	 * <p>Description: 更新一条链路的带宽</p>
	 * @param @param resultLink   
	 * @return void  
	 * @throws
	 * @date 2017-3-28下午3:29:52
	 */
	public static void updateLinkBw(ArrayList<Integer> resultLink){
		int usedBw = resultLink.get(resultLink.size()-1);  //取最后一个数是带宽
		for(int i=0;i<resultLink.size()-3;i++){
			int NetworkPoint1 = resultLink.get(i);
			int NetworkPoint2 = resultLink.get(i+1);
			updateP2PBw(NetworkPoint1, NetworkPoint2, usedBw);
		}
		
	}
	
	/**
	 * 
	 * <p>Title: getP2PBandwidth</p>
	 * <p>Description: 计算每两个网络节点之间的带宽</p>
	 * @param @param NetworkLinkArray
	 * @param @param ConsumeLinkArray
	 * @param @return   
	 * @return Map  
	 * @throws
	 * @date 2017-3-23下午4:20:28
	 */
	public Map getP2PBandwidth(int[][] NetworkLinkArray){
		Map<String, Integer> BwMap=new HashMap<String, Integer>(); 
		String tempKey = "";
		int tempValue = 0;
		
		//把NetworkLinkArray二维数组的宽度存入
		for(int Netw_row=0;Netw_row<NetworkLinkArray.length;Netw_row++){
			tempKey = "Bw"+NetworkLinkArray[Netw_row][0]+"_"+NetworkLinkArray[Netw_row][1];
			tempValue = NetworkLinkArray[Netw_row][2];
			BwMap.put(tempKey, tempValue);
			tempKey = "Bw"+NetworkLinkArray[Netw_row][1]+"_"+NetworkLinkArray[Netw_row][0];
			BwMap.put(tempKey, tempValue);
		}
		
		//把ConsumeLinkArray二维数组的宽度存入
//		for(int ConsumeL_row=0;ConsumeL_row<ConsumeLinkArray.length;ConsumeL_row++){
//			tempKey = "Bw"+ConsumeLinkArray[ConsumeL_row][0]+"_"+ConsumeLinkArray[ConsumeL_row][1];
//			tempValue = ConsumeLinkArray[ConsumeL_row][2];
//			BwMap.put(tempKey, tempValue);
//			tempKey = "Bw"+ConsumeLinkArray[ConsumeL_row][1]+"_"+ConsumeLinkArray[ConsumeL_row][0];
//			BwMap.put(tempKey, tempValue);
//		}
		
		return BwMap;
	}
	
	/**
	 * 
	 * <p>Title: getBorderPointBwNeed</p>
	 * <p>Description: 计算与消费节点相邻点之间的带宽需求</p>
	 * @param @return   
	 * @return Map  
	 * @throws
	 * @date 2017-3-23下午4:21:13
	 */
	public Map getBorderPointBwNeed(int[][] ConsumeLinkArray){
		Map<String, Integer> BwNeedMap=new HashMap<String, Integer>(); 
		String tempKey = "";
		int tempValue = 0;
		
		//把ConsumeLinkArray二维数组的宽度需求存入
		for(int ConsumeL_row=0;ConsumeL_row<ConsumeLinkArray.length;ConsumeL_row++){
			tempKey = "BwNeed"+"_"+ConsumeLinkArray[ConsumeL_row][1];
			tempValue = ConsumeLinkArray[ConsumeL_row][2];
			BwNeedMap.put(tempKey, tempValue);
		}
		
		return BwNeedMap;
	}
}
