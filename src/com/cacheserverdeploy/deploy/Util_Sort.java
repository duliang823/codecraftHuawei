package com.cacheserverdeploy.deploy;

import java.util.ArrayList;

public class Util_Sort {
	
	/**
	 * 
	 * <p>Title: ArraySort</p>
	 * <p>Description: 将数组排序，服务器的数组放前面</p>
	 * @param @param pointAndBw
	 * @param @param serverPoints
	 * @param @return   
	 * @return int[]  
	 * @throws
	 * @date 2017-3-28下午1:17:35
	 */
	public static int[] ArraySort(int[] pointAndBw,ArrayList<Integer> serverPoints){
		ArrayList<Integer> ServerPoints = new ArrayList<Integer>();
		ArrayList<Integer> NormalPoints = new ArrayList<Integer>();
		for(int x_x=0;x_x<pointAndBw.length;x_x=x_x+3){
			if(serverPoints.contains(pointAndBw[x_x])){ 
				ServerPoints.add(pointAndBw[x_x]);
				ServerPoints.add(pointAndBw[x_x+1]);
				ServerPoints.add(pointAndBw[x_x+2]);
			}else{
				NormalPoints.add(pointAndBw[x_x]);
				NormalPoints.add(pointAndBw[x_x+1]);
				NormalPoints.add(pointAndBw[x_x+2]);
			}
		}
		
		
		int[] ServerPoints_ints = Util_ArrayToList.toArrays(ServerPoints);
		int[] NormalPoints_ints = Util_ArrayToList.toArrays(NormalPoints);
		
		for(int i_i=1;i_i<ServerPoints_ints.length;i_i=i_i+3){
			for(int j_j=i_i+3;j_j<ServerPoints_ints.length;j_j=j_j+3){
				if(ServerPoints_ints[i_i] < ServerPoints_ints[j_j]){
					int temp1 = ServerPoints_ints[i_i];
					int temp2 = ServerPoints_ints[i_i-1];
					int temp3 = ServerPoints_ints[i_i+1];
					ServerPoints_ints[i_i] = ServerPoints_ints[j_j];
					ServerPoints_ints[i_i-1] = ServerPoints_ints[j_j-1];
					ServerPoints_ints[i_i+1] = ServerPoints_ints[j_j+1];
					ServerPoints_ints[j_j] = temp1;
					ServerPoints_ints[j_j-1] = temp2;
					ServerPoints_ints[j_j+1] = temp3;
				}else if(ServerPoints_ints[i_i] == ServerPoints_ints[j_j]){
					if(ServerPoints_ints[i_i+1] > ServerPoints_ints[j_j+1]){
						int temp1 = ServerPoints_ints[i_i];
						int temp2 = ServerPoints_ints[i_i-1];
						int temp3 = ServerPoints_ints[i_i+1];
						ServerPoints_ints[i_i] = ServerPoints_ints[j_j];
						ServerPoints_ints[i_i-1] = ServerPoints_ints[j_j-1];
						ServerPoints_ints[i_i+1] = ServerPoints_ints[j_j+1];
						ServerPoints_ints[j_j] = temp1;
						ServerPoints_ints[j_j-1] = temp2;
						ServerPoints_ints[j_j+1] = temp3;
					}
				}
			}
		}
		
		for(int i_i=1;i_i<NormalPoints_ints.length;i_i=i_i+3){
			for(int j_j=i_i+3;j_j<NormalPoints_ints.length;j_j=j_j+3){
				if(NormalPoints_ints[i_i] < NormalPoints_ints[j_j]){
					int temp1 = NormalPoints_ints[i_i];
					int temp2 = NormalPoints_ints[i_i-1];
					int temp3 = NormalPoints_ints[i_i+1];
					NormalPoints_ints[i_i] = NormalPoints_ints[j_j];
					NormalPoints_ints[i_i-1] = NormalPoints_ints[j_j-1];
					NormalPoints_ints[i_i+1] = NormalPoints_ints[j_j+1];
					NormalPoints_ints[j_j] = temp1;
					NormalPoints_ints[j_j-1] = temp2;
					NormalPoints_ints[j_j+1] = temp3;
				}else if(NormalPoints_ints[i_i] == NormalPoints_ints[j_j]){
					if(NormalPoints_ints[i_i+1] > NormalPoints_ints[j_j+1]){
						int temp1 = NormalPoints_ints[i_i];
						int temp2 = NormalPoints_ints[i_i-1];
						int temp3 = NormalPoints_ints[i_i+1];
						NormalPoints_ints[i_i] = NormalPoints_ints[j_j];
						NormalPoints_ints[i_i-1] = NormalPoints_ints[j_j-1];
						NormalPoints_ints[i_i+1] = NormalPoints_ints[j_j+1];
						NormalPoints_ints[j_j] = temp1;
						NormalPoints_ints[j_j-1] = temp2;
						NormalPoints_ints[j_j+1] = temp3;
					}
				}
			}
		}
		
		int[] resultInts = new int[ServerPoints_ints.length + NormalPoints_ints.length];
		int resultInts_i = 0;
		
		for(int i=0;i<ServerPoints_ints.length;i++){
			resultInts[resultInts_i++] = ServerPoints_ints[i];
		}
		for(int i=0;i<NormalPoints_ints.length;i++){
			resultInts[resultInts_i++] = NormalPoints_ints[i];
		}
		
		return resultInts;
	}
	
}
