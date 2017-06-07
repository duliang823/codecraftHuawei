package com.cacheserverdeploy.deploy;

public class NetworkPoint {
	
	private String[] graphContent;
	private Integer NetworkPoint_num; //网络节点的数量
	private Integer NetworkLink_num;  //网络链路的数量
	private Integer ConsumePoint_num; //消费节点的数量
	
	private Integer rowNum_3_4; //第3段和第4段之间空行的行号,eg：62-12-1=49
	
	public Integer getRowNum_3_4() {
		return rowNum_3_4;
	}
	public void setRowNum_3_4(Integer rowNum_3_4) {
		this.rowNum_3_4 = rowNum_3_4;
	}
	
	public String[] getGraphContent() {
		return graphContent;
	}
	public void setGraphContent(String[] graphContent) {
		this.graphContent = graphContent;
	}

	public Integer getNetworkPoint_num() {
		return NetworkPoint_num;
	}
	public void setNetworkPoint_num(Integer networkPoint_num) {
		NetworkPoint_num = networkPoint_num;
	}

	public Integer getNetworkLink_num() {
		return NetworkLink_num;
	}

	public void setNetworkLink_num(Integer networkLink_num) {
		NetworkLink_num = networkLink_num;
	}

	public Integer getConsumePoint_num() {
		return ConsumePoint_num;
	}

	public void setConsumePoint_num(Integer consumePoint_num) {
		ConsumePoint_num = consumePoint_num;
	}
	
	
	/**
	 * 
	 * <p>Title: getNetworkLinkArray</p>
	 * <p>Description: 获取起始ID，终止ID，总带宽，租费的二维数组</p>
	 * @param @param rowNum_3_4   
	 * @param @param graphContent
	 * @param @return   
	 * @return int[][]  
	 * @throws
	 * @date 2017-3-21下午3:08:59
	 */
	public int[][] getNetworkLinkArray(){
    	int[][] NetworkLinkArray_2d = new int[rowNum_3_4-4][4];
    	int j = 0;
    	for(int i=4;i<rowNum_3_4;i++ ){
    		String[] tempStr = graphContent[i].split(" ");
    		NetworkLinkArray_2d[j][0] = Integer.parseInt(tempStr[0]);
    		NetworkLinkArray_2d[j][1] = Integer.parseInt(tempStr[1]);
    		NetworkLinkArray_2d[j][2] = Integer.parseInt(tempStr[2]);
    		NetworkLinkArray_2d[j][3] = Integer.parseInt(tempStr[3]);
    		j = j + 1;
    	}
    	return NetworkLinkArray_2d;
    }
	
	/**
	 * 
	 * <p>Title: getConsumeLinkArray</p>
	 * <p>Description: 获取消费节点ID，相连节点ID，带宽需求二维数组</p>
	 * @param @return   
	 * @return int[][]  
	 * @throws
	 * @date 2017-3-21下午3:15:20
	 */
	public int[][] getConsumeLinkArray(){
		int[][] ConsumeLinkArray_2d = new int[ConsumePoint_num][3];
		int j = 0;
    	for(int i=rowNum_3_4+1;i<graphContent.length;i++ ){
    		String[] tempStr = graphContent[i].split(" ");
    		ConsumeLinkArray_2d[j][0] = Integer.parseInt(tempStr[0]);
    		ConsumeLinkArray_2d[j][1] = Integer.parseInt(tempStr[1]);
    		ConsumeLinkArray_2d[j][2] = Integer.parseInt(tempStr[2]);
    		j = j + 1;
    	}
		return ConsumeLinkArray_2d;
	}
	
}
