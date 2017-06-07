package com.cacheserverdeploy.deploy;

import java.util.ArrayList;

public class Util_ArrayToList {
	
	
	/**
	 * 
	 * <p>Title: toString</p>
	 * <p>Description: 将ArrayList<Integer>转换为String，并且中间数与数用英文空格隔开</p>
	 * @param @param ArrayLists
	 * @param @return   
	 * @return String  
	 * @throws
	 * @date 2017-3-28下午3:45:50
	 */
	public static String toString(ArrayList<Integer> ArrayLists){
		String resultStr = "";
		for(int i=0;i<ArrayLists.size();i++){
			if(i != ArrayLists.size()-1){
				resultStr += ArrayLists.get(i)+" ";
			}else{
				resultStr += ArrayLists.get(i);
			}
		}
		return resultStr;
	}
	
	
	/**
	 * 
	 * <p>Title: toArrayList</p>
	 * <p>Description: 将int[]数组转换为ArrayList<Integer></p>
	 * @param @param Arrays
	 * @param @return   
	 * @return ArrayList<Integer>  
	 * @throws
	 * @date 2017-3-24上午8:59:45
	 */
	public static ArrayList<Integer> toArrayList(int[] Arrays){
		ArrayList<Integer> resultArrayList = new ArrayList<Integer>();
		for(int i=0;i<Arrays.length;i++){
			resultArrayList.add(Arrays[i]);
		}
		return resultArrayList;
	}
	
	/**
	 * 
	 * <p>Title: toArrays</p>
	 * <p>Description: 将ArrayList<Integer>转换为int[]数组</p>
	 * @param @param ArrayLists
	 * @param @return   
	 * @return int[]  
	 * @throws
	 * @date 2017-3-24上午9:07:49
	 */
	public static int[] toArrays(ArrayList<Integer> ArrayLists){
		int ArrayLists_length = ArrayLists.size();
		int[] resultArrays = new int[ArrayLists_length];
		for(int i=0;i<ArrayLists_length;i++){
			resultArrays[i] = ArrayLists.get(i);
		}
		return resultArrays;
	}
}
