package art.jobs;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

import art.utils.ArtUtil;

public class Algor {
	static double[][] vl;
	static double[][]vr;
	static int row = 0;
	static int column = 0;
	static int dim = 25;
	static double alpha=0.06;
	static double beta=0;
	static int step=1;
	
	public static void init() throws NumberFormatException, IOException {
		Integer count = AR.getItemCount(ArtUtil.train_path);
		vl = new double[count][dim];
		vr = new double[count][dim];
		for (int i = 0; i < count; i++)
			for (int j = 0; j < dim; j++){
				vl[i][j] = Math.random();
				vr[i][j]=Math.random();
			}
		row=count;
		column=dim;
	}
	static boolean isValidSupp(int i, int j,Map<String, Integer> pair_map, Map<Integer, String> idToStr
			) {
		String str_i = idToStr.get(i);
		String str_j = idToStr.get(j);
		String key = str_i + "_" + str_j;
		if (!pair_map.containsKey(key))
			return false;
		return true;
	}

	public static double getSuppCost(int i, int j, Map<String, Integer> pair_map,
			Map<Integer, String> idToStr) {
		double cost = 0;
		for (int idx = 0; idx < dim; idx++) {
			cost = vl[i][idx] * vr[j][idx]+cost;
		}
		String str_i = idToStr.get(i);
		String str_j = idToStr.get(j);
		String key = str_i + "_" + str_j;
		Integer supp_i_j = pair_map.get(key);
		return cost - supp_i_j;
	}

	public static double getSuppCosts(Map<String, Integer> pair_map,Map<Integer, String> idToStr){
		double cost=0;
		int N=pair_map.size();
		for(int i=0;i<row;i++){
			for(int j=0;j<column;j++){
				if(isValidSupp(i,j,pair_map,idToStr)){
					cost=cost+Math.pow(getSuppCost(i,j,pair_map,idToStr),2);			
				}
			}
		}
		cost=cost+getRegular()*beta;
		cost=cost/N;
		System.out.println(cost);
		return cost;
	}

	private static double getRegular() {
		double result=0;
		for(int i=0;i<row;i++)
			for(int j=0;j<column;j++){
				result=result+vl[i][j]*vl[i][j]+vr[i][j]*vr[i][j];
			}
		return result;
	}
	public void assign(double[]a,double[]b){
		
	}
	public static void iterator(Map<String, Integer> pair_map,Map<Integer, String> idToStr){
		getSuppCosts(pair_map,idToStr);
		double[][] v_tmp_l=new double[row][column];
		double[][]v_tmp_r=new double[row][column];
		int N=pair_map.size();
		//v的迭代
		for(int i=0;i<row;i++){
			//对于每一个向量进行梯度下降
			for(int j=0;j<row;j++){
				//对有过supp的每一个item进行叠加，计算梯度
				if(isValidSupp(i,j,pair_map,idToStr)){
					double par=getSuppCost(i,j,pair_map,idToStr);
					for(int k=0;k<column;k++){
						v_tmp_l[i][k]=v_tmp_l[i][k]+vr[j][k]*par/N;
						v_tmp_r[j][k]=v_tmp_r[j][k]+vl[i][k]*par/N;
					}
				}
				
//				if(isValidSupp(j,i,pair_map,idToStr)){
//					double par=getSuppCost(j,i,pair_map,idToStr);
//					for(int k=0;k<column;k++){
//						v_tmp_l[i][k]=v_tmp_l[i][k]=vr[j][k]*par+vr[i][k]*beta/N;
//					}
//				}
			}
		}
//		//右v的迭代
//		for(int i=0;i<row;i++){
//			for(int j=0;j<row;j++){
//				
//			}
//		}
		for(int i=0;i<row;i++)
			for(int j=0;j<column;j++){
				vl[i][j]=vl[i][j] -alpha*v_tmp_l[i][j];
				vr[i][j]=vr[i][j] -alpha*v_tmp_r[i][j];
			}
	}
	
	public static void build(int iter) throws IOException{
		init();
		Map<String, Integer> count_map = AR.getCountMap(ArtUtil.train_path);
		Map<String, Integer> pair_map = AR.getPairCountMap(ArtUtil.train_path);
		Map<Integer, String> idToStr = AR.getIndexItemIdToStr(ArtUtil.train_path);
		for(int i=0;i<iter;i++){
			System.out.println("iterator:"+i);
			step++;
			if(step%10000==0){
				save(vr,"right");
				save(vl,"left");
			}
			iterator( pair_map, idToStr);
		}
	}
	public static void save(double[][]v,String name) throws IOException{
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<row;i++){
			for(int j=0;j<column;j++)
				sb.append(v[i][j]).append(" ");
			sb.append("\n");
		}
		FileOutputStream fos = new FileOutputStream(ArtUtil.res_left_path+"_"+name+step);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		BufferedWriter bw = new BufferedWriter(osw);
		bw.write(sb.toString());
		bw.close();
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		build(200000);
		
	}

}
