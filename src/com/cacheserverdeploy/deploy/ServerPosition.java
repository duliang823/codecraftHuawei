package com.cacheserverdeploy.deploy;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServerPosition{
	
	/**
	 * 
	 * <p>Title: getServerPositionArray</p>
	 * <p>Description: 计算出服务器处的位置数组</p>
	 * @param @param NetworkLinkArray_2d
	 * @param @param serverNum
	 * @param @return   
	 * @return int[]  
	 * @throws
	 * @date 2017-3-21下午3:59:59
	 */
	public int[] getServerPositionArray(int[][] NetworkLinkArray_2d,Integer serverNum){
		int[] ServerPositionArray = new int[serverNum];   //存放服务器节点的数组
		
		int[] pointArr = new int[NetworkLinkArray_2d.length*2];   //所有节点的数据（包括重复的)
    	int pointArr_j = 0;
    	for(int i = 0;i < NetworkLinkArray_2d.length;i++){
    		pointArr[pointArr_j++] = NetworkLinkArray_2d[i][0];
    		pointArr[pointArr_j++] = NetworkLinkArray_2d[i][1];
    	}
    	Arrays.sort(pointArr); //给数组排序
    	int count=0;
        int tmp=pointArr[0];
        Map map=new HashMap();
        for(int i=0; i < pointArr.length; i++) {
            if(tmp != pointArr[i]) {
                tmp=pointArr[i];
                count=1;
            } else {
                count++;
            }
            map.put(pointArr[i], count);
        }
        map=sortByValue(map);
        
        
        int serverPosition_j = 0;
        Set<Integer> key = map.keySet();
        for (Iterator it = key.iterator(); it.hasNext();) {
            if(serverPosition_j < serverNum){
            	ServerPositionArray[serverPosition_j++] = (Integer) it.next();
            }else{
            	break;
            }
        }
		
		return ServerPositionArray;
	}
	
	public static Map sortByValue(Map map) {
        List list=new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            // 将链表按照值得从小到大进行排序
            public int compare(Object o1, Object o2) {
                return ((Comparable)((Map.Entry)(o2)).getValue()).compareTo(((Map.Entry)(o1)).getValue());
            }
        });
        Map result=new LinkedHashMap();
        for(Iterator it=list.iterator(); it.hasNext();) {
            Map.Entry entry=(Map.Entry)it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
