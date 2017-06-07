package com.cacheserverdeploy.deploy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PointUtil {
	
	private static int[] ServerPositionArray;  //出服务器处的位置数组
	private static int[] ConsumeBorderPointPositionArray;  //消费节点相邻节点的位置数组
	public static Map<String, ArrayList<Integer>> PointMap;  //网络节点相邻有哪些节点  //取法：PointMap.get("NextPoint_0")
	public static Map<String, Integer> BorderPointMap; //起始点周围的消费节点
	
	public static Map<String, Integer> getBorderPointMap() {
		return BorderPointMap;
	}
	public static void setBorderPointMap(Map<String, Integer> borderPointMap) {
		BorderPointMap = borderPointMap;
	}
	public static Map<String, ArrayList<Integer>> getPointMap() {
		return PointMap;
	}
	public static void setPointMap(Map<String, ArrayList<Integer>> pointMap) {
		PointMap = pointMap;
	}
	public static int[] getConsumeBorderPointPositionArray() {
		return ConsumeBorderPointPositionArray;
	}
	public static void setConsumeBorderPointPositionArray(
			int[] consumeBorderPointPositionArray) {
		ConsumeBorderPointPositionArray = consumeBorderPointPositionArray;
	}
	public int[] getServerPositionArray() {
		return ServerPositionArray;
	}
	public void setServerPositionArray(int[] serverPositionArray) {
		ServerPositionArray = serverPositionArray;
	}
	
	
	/**
	 * 
	 * <p>Title: getCommonArrays</p>
	 * <p>Description: 找两个ArrayList<Integer>数组的公共点</p>
	 * @param @param ArrayList1
	 * @param @param ArrayList2
	 * @param @return   
	 * @return ArrayList<Integer>  
	 * @throws
	 * @date 2017-3-24下午6:27:11
	 */
	public static ArrayList<Integer> getCommonArrays(ArrayList<Integer> ArrayList1,ArrayList<Integer> ArrayList2){
		ArrayList<Integer> CommonArrays = new ArrayList<Integer>();
		for(int i=0; i<ArrayList1.size();i++){
			if(ArrayList2.contains(ArrayList1.get(i)) && (CommonArrays.contains(ArrayList1.get(i)) == false)){
				CommonArrays.add(ArrayList1.get(i));
			}
		}
		return CommonArrays;
	}
	
	
	/**
	 * 
	 * <p>Title: NetworkPoint_Around_ServerPoint</p>
	 * <p>Description: 取出网络节点周围的服务器点的数组</p>
	 * @param @param NetworkPoint
	 * @param @return   
	 * @return ArrayList<Integer>  
	 * @throws
	 * @date 2017-3-24上午9:48:45
	 */
	public static ArrayList<Integer> NetworkPoint_Around_ServerPoint(int NetworkPoint){
		ArrayList<Integer> resultServerPoint = new ArrayList<Integer>();
		ArrayList<Integer> AroundPoints = PointMap.get("NextPoint_"+NetworkPoint);
		for(int i=0;i<AroundPoints.size();i++){
			if(isServerPoint(AroundPoints.get(i))){
				resultServerPoint.add(AroundPoints.get(i));
			}
		}
		return resultServerPoint;
	}
	
	/**
	 * 
	 * <p>Title: NetworkPoint_Around_ConsumeBorderPoint</p>
	 * <p>Description: 取网络节点周围与消费节点相连的网络节点的数组</p>
	 * @param @param NetworkPoint
	 * @param @return   
	 * @return ArrayList<Integer>  
	 * @throws
	 * @date 2017-3-24上午9:35:27
	 */
	public static ArrayList<Integer> NetworkPoint_Around_ConsumeBorderPoint(int NetworkPoint){
		ArrayList<Integer> resultConsumeBorderPoint = new ArrayList<Integer>();
		ArrayList<Integer> AroundPoints = PointMap.get("NextPoint_"+NetworkPoint);
		for(int i=0;i<AroundPoints.size();i++){
			if(isConsumeBorderPoint(AroundPoints.get(i))){
				resultConsumeBorderPoint.add(AroundPoints.get(i));
			}
		}
		return resultConsumeBorderPoint;
	}
	
	
	/**
	 * 
	 * <p>Title: isConsumeBorderPoint</p>
	 * <p>Description:  判断一个网络节点是否是消费节点相连的点，是则返回true，不是则返回false</p>
	 * @param @param NetworkPoint
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @date 2017-3-24上午9:24:01
	 */
	public static boolean isConsumeBorderPoint(int NetworkPoint){
		for(int i=0;i<ConsumeBorderPointPositionArray.length;i++){
			if(NetworkPoint == ConsumeBorderPointPositionArray[i]){
				return true;
			}
		}
		return false;
	}
	
	
	
	/**
	 * 
	 * <p>Title: isServerPoint</p>
	 * <p>Description: 判断一个网络节点是否是服务器点，是则返回true，不是则返回false</p>
	 * @param @param NetworkPoint
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @date 2017-3-24上午9:16:57
	 */
	public static boolean isServerPoint(int NetworkPoint){
		for(int i=0;i<ServerPositionArray.length;i++){
			if(NetworkPoint == ServerPositionArray[i]){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 
	 * <p>Title: getNetworkPoint</p>
	 * <p>Description: 获取网络节点数组</p>
	 * @param @param NetworkLinkArray
	 * @param @return   
	 * @return int[]  
	 * @throws
	 * @date 2017-3-23上午11:13:22
	 */
	public int[] getNetworkPoint(int[][] NetworkLinkArray){
		ArrayList<Integer> AllNetworkPoint = new ArrayList<Integer>();
		for(int i=0;i<NetworkLinkArray.length;i++){
			for(int j=0;j<2;j++){  //只有前两个是网络节点
				if( (AllNetworkPoint.contains(NetworkLinkArray[i][j]) == false)){
					AllNetworkPoint.add(NetworkLinkArray[i][j]);
				}
				
			}
		}
		int[]  NetworkPointPositionArray = new int[AllNetworkPoint.size()];
		int NetworkPointPositionArray_i = 0;
		for(int tmp:AllNetworkPoint){
			NetworkPointPositionArray[NetworkPointPositionArray_i++] = tmp;
        }
		Arrays.sort(NetworkPointPositionArray);  //从小到大排序
		return NetworkPointPositionArray;
	}
	
	/**
	 * 
	 * <p>Title: getNextPoint</p>
	 * <p>Description: 获取网络节点周围的节点</p>
	 * @param @param NetworkLinkArray
	 * @param @return   
	 * @return Map  
	 * @throws
	 * @date 2017-3-23下午5:03:56
	 */
	public Map getNextPoint(int[][] NetworkLinkArray){
		Map<String, ArrayList<Integer>> PointMap=new HashMap<String, ArrayList<Integer>>(); 
		int[] NetworkPointPositionArray = getNetworkPoint(NetworkLinkArray);
		
		String tempKey = "";
		//每个网络节点周围的节点
		for(int i=0;i<NetworkPointPositionArray.length;i++){
			ArrayList<Integer> tempList = new ArrayList<Integer>();
			tempKey = "NextPoint_"+NetworkPointPositionArray[i];   
			
			for(int Netw_row=0;Netw_row<NetworkLinkArray.length;Netw_row++){
				for(int Netw_column=0;Netw_column<2;Netw_column++){
					if(NetworkPointPositionArray[i] == NetworkLinkArray[Netw_row][Netw_column]){
						if(Netw_column == 0){
							tempList.add(NetworkLinkArray[Netw_row][1]);
						}else if(Netw_column == 1){
							tempList.add(NetworkLinkArray[Netw_row][0]);
						}
						
					}
				}
			}
			PointMap.put(tempKey, tempList);
		}
		
		return PointMap;
	}
	
	/**
	 * 
	 * <p>Title: getConsumePointNextPoint</p>
	 * <p>Description: 获取消费节点挨着的是什么网络节点</p>
	 * @param @param NetworkLinkArray
	 * @param @return   
	 * @return Map  
	 * @throws
	 * @date 2017-3-23下午5:05:37
	 */
	public Map getConsumePointNextPoint(int[][] ConsumeLinkArray){
		Map<String, Integer> ConsumePointMap=new HashMap<String, Integer>(); 
		String tempKey = "";
		int tempValue = 0;
		
		for(int ConsumeL_row=0;ConsumeL_row<ConsumeLinkArray.length;ConsumeL_row++){
			tempKey = "NextPoint_"+ConsumeLinkArray[ConsumeL_row][0];
			tempValue = ConsumeLinkArray[ConsumeL_row][1];
			ConsumePointMap.put(tempKey, tempValue);
		}
		
		return ConsumePointMap;
	}
	
	/**
	 * 
	 * <p>Title: getBorderPointNextPoint</p>
	 * <p>Description: 获取挨着消费节点的网络节点处是哪个消费节点</p>
	 * @param @param ConsumeLinkArray
	 * @param @return   
	 * @return Map  
	 * @throws
	 * @date 2017-3-23下午5:19:52
	 */
	public Map getBorderPointNextPoint(int[][] ConsumeLinkArray){
		Map<String, Integer> BorderPointMap=new HashMap<String, Integer>(); 
		String tempKey = "";
		int tempValue = 0;
		
		for(int ConsumeL_row=0;ConsumeL_row<ConsumeLinkArray.length;ConsumeL_row++){
			tempKey = "NextPoint_"+ConsumeLinkArray[ConsumeL_row][1];
			tempValue = ConsumeLinkArray[ConsumeL_row][0];
			BorderPointMap.put(tempKey, tempValue);
		}
		
		return BorderPointMap;
	}
	
	/**
	 * 
	 * <p>Title: getInvalidServerPoints</p>
	 * <p>Description: 获取服务器点构成的路径中只构成一条路径的服务器点</p>
	 * @param @param AllLinks
	 * @param @param ServerPoints
	 * @param @return   
	 * @return ArrayList<Integer>  
	 * @throws
	 * @date 2017-3-30下午5:20:34
	 */
	public static ArrayList<Integer> getInvalidServerPoints(ArrayList<ArrayList<Integer>> AllLinks,ArrayList<Integer> ServerPoints){
		//把由服务器构成的链路放在LinksInServerPoints中：
		ArrayList<ArrayList<Integer>> LinksInServerPoints =  new ArrayList<ArrayList<Integer>>(); //由服务器点构成的链路
		ArrayList<Integer> AllServerPoints = new ArrayList<Integer>(); //链路由服务器构成的，把开头的第0个服务器放入AllServerPoints中，包含重复的
		for(int i=0;i<AllLinks.size();i++){
			ArrayList<Integer> nowLink = AllLinks.get(i);
			if(ServerPoints.contains(nowLink.get(0))){  //如果链路的第0个数是在ServerPoints数组中，说明这条链路是由服务器构成的
				AllServerPoints.add(nowLink.get(0));
				LinksInServerPoints.add(nowLink);
			}
		}
		
		//找出由服务器点构成的路径中只构成一条路径的服务器点，并放入single_ServerPoints中：
		ArrayList<Integer> single_ServerPoints = new ArrayList<Integer>();
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for(int i=0;i<AllServerPoints.size();i++){
			if(map.containsKey(AllServerPoints.get(i))){
				map.put(AllServerPoints.get(i), map.get(AllServerPoints.get(i))+1);
			}else{
				map.put(AllServerPoints.get(i), 1);
			}
		}
		for(Integer key : map.keySet()){
			if(map.get(key) == 1){
				single_ServerPoints.add(key);
			}
		}
		
		return single_ServerPoints;
	}
	
	
	/**
	 * 
	 * <p>Title: getValidServerPoints</p>
	 * <p>Description: 获取不含服务器点只构建一条路径的服务器数组</p>
	 * @param @param AllLinks
	 * @param @param ServerPoints
	 * @param @return   
	 * @return ArrayList<Integer>  
	 * @throws
	 * @date 2017-3-31下午3:48:24
	 */
	public static ArrayList<Integer> getValidServerPoints(ArrayList<ArrayList<Integer>> AllLinks,ArrayList<Integer> ServerPoints){
		ArrayList<Integer> single_ServerPoints = getInvalidServerPoints(AllLinks,ServerPoints);
		ArrayList<Integer> ValidServerPoints = new ArrayList<Integer>();
		for(int i=0;i<ServerPoints.size();i++){
			if(single_ServerPoints.contains(ServerPoints.get(i)) == false){
				if(ValidServerPoints.contains(ServerPoints.get(i)) == false){
					ValidServerPoints.add(ServerPoints.get(i));
				}
			}
		}
		return ValidServerPoints;
	}
	
	
	/**
	 * 
	 * <p>Title: handleAllLinks</p>
	 * <p>Description: 处理服务器点只构建了一条路径的链路</p>
	 * @param @param AllLinks
	 * @param @param ServerPoints
	 * @param @return   
	 * @return ArrayList<ArrayList<Integer>>  
	 * @throws
	 * @date 2017-3-31下午3:30:57
	 */
	public static ArrayList<ArrayList<Integer>> handleAllLinks(ArrayList<ArrayList<Integer>> AllLinks,ArrayList<Integer> ServerPoints){
		ArrayList<Integer> single_ServerPoints = getInvalidServerPoints(AllLinks,ServerPoints);
		
		//处理服务器点只构建了一条路径的链路：
		ArrayList<ArrayList<Integer>> Result_AllLinks = new ArrayList<ArrayList<Integer>>();
		for(int i=0;i<AllLinks.size();i++){
			ArrayList<Integer> nowLink = AllLinks.get(i);
			if(single_ServerPoints.contains(nowLink.get(0))){
				int temp1 = nowLink.get(nowLink.size()-3);
				int temp2 = nowLink.get(nowLink.size()-2);
				int temp3 = nowLink.get(nowLink.size()-1);
				ArrayList<Integer> nowLink_new = new ArrayList<Integer>();
				nowLink_new.add(temp1);
				nowLink_new.add(temp2);
				nowLink_new.add(temp3);
				Result_AllLinks.add(nowLink_new);
			}else{
				Result_AllLinks.add(nowLink);
			}
		}
		
		return Result_AllLinks;
	}
	
}
