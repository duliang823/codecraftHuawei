package com.cacheserverdeploy.deploy;

public class ConsumePoint {
	
	/**
	 * 
	 * <p>Title: getConsumePointPositionArray</p>
	 * <p>Description: 计算出消费节点处的位置数组</p>
	 * @param @param ConsumeLinkArray
	 * @param @return   
	 * @return int[]  
	 * @throws
	 * @date 2017-3-21下午3:59:48
	 */
	public int[] getConsumePointPositionArray(int[][] ConsumeLinkArray){
		int[] ConsumePointPositionArray = new int[ConsumeLinkArray.length];
		for(int i=0;i<ConsumeLinkArray.length;i++){
			ConsumePointPositionArray[i] = ConsumeLinkArray[i][0];
		}
		return ConsumePointPositionArray;
	}
	
	/**
	 * 
	 * <p>Title: getConsumeBorderPointPositionArray</p>
	 * <p>Description: 计算出消费节点相邻节点的位置数组</p>
	 * @param @param ConsumeLinkArray
	 * @param @return   
	 * @return int[]  
	 * @throws
	 * @date 2017-3-21下午4:29:21
	 */
	public int[] getConsumeBorderPointPositionArray(int[][] ConsumeLinkArray){
		int[] ConsumeBorderPointPositionArray = new int[ConsumeLinkArray.length];
		for(int i=0;i<ConsumeLinkArray.length;i++){
			ConsumeBorderPointPositionArray[i] = ConsumeLinkArray[i][1];
		}
		return ConsumeBorderPointPositionArray;
	}
	
}
