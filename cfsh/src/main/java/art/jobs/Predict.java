package art.jobs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import art.items.Item;
import art.items.Tran;
import art.items.User;
import art.utils.ArtUtil;

public class Predict {

	static double[][] vl;
	static double[][] vr;
	static int row = 0;
	static int column = 0;
	static int dim = 25;
	static double lamada=0;
	static int p_length=1;
	public static int step=70000;
	public static void init() throws NumberFormatException, IOException {
		Integer count = AR.getItemCount(ArtUtil.train_path);
		vl = new double[count][dim];
		vr = new double[count][dim];
		row = count;
		column = dim;
		init_data(vl,ArtUtil.res_left_path);
		init_data(vr,ArtUtil.res_right_path);
	}

	private static void init_data(double[][]matrix,String path) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		FileInputStream fis = new FileInputStream(path+step
				);
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader reader = new BufferedReader(isr);
		String line = null;
		int idx = 0;
		while ((line = reader.readLine()) != null) {
			String[] ids = line.split(" ");
			int step = 0;
			for (int i = 0; i < ids.length; i++) {
				if (ids[i].length() > 0) {
					matrix[idx][step] = Double.parseDouble(ids[i]);
					step++;
				}
			}
			idx++;
		}
		reader.close();
	}

	public static Map<String, List<Item>> buildItemMap() throws IOException {
		Map<Integer, String> idToStr = AR
				.getIndexItemIdToStr(ArtUtil.train_path);
		Map<String, List<Item>> result = new HashMap<String, List<Item>>();
		for (int i = 0; i < row; i++) {
			String key = idToStr.get(i);
			List<Item> list = new ArrayList<Item>();
			for (int j = 0; j < row; j++) {
				String item_j = idToStr.get(j);
				double score = getScore(vl[i], vr[j]);
				Item item = new Item();
				item.name = item_j;
				item.score = score;
				list.add(item);
			}
			// Collections.sort(list,ArtUtil.sort_item);
			result.put(key, list);
		}
		return result;
	}

	private static double getScore(double[] v1, double[] v2) {
		int dim = v1.length;
		double score = 0;
		for (int i = 0; i < dim; i++)
			score = score + v1[i] * v2[i];
		return score;
	}

	public static Map<String, List<Item>> getPredictItems() throws IOException {
		List<User> list = AR.getUserList(ArtUtil.train_path);
		System.out.println("user size"+list.size());
		Map<String, List<Item>> map = buildItemMap();
		Map<String, List<Item>> result=new HashMap<String, List<Item>>();
		for (User user : list) {
		//	Tran tran = user.tlist.get(user.tlist.size() - 1);
			List<Item> ulist = generateList(row);
			for(Tran tran:user.tlist){
			for (String item_name : tran.itemList) {
				List<Item> tmp = map.get(item_name);
				for (int idx = 0; idx < tmp.size(); idx++) {
					Item to = ulist.get(idx);
					Item from = tmp.get(idx);
					to.name = from.name;
					to.score = to.score + from.score;
				}
			}
			}
			Collections.sort(ulist,ArtUtil.sort_item);
			List<Item>tmp=new ArrayList<Item>();
			for(int i=0;i<p_length;i++){
				Item item=new Item();
				item.name=ulist.get(i).name;
				item.score=ulist.get(i).score;
				tmp.add(item);
			}
		
			result.put(user.uid, tmp);
		}
		return result;
	}

	private static List<Item> generateList(int row2) {
		List<Item> list = new ArrayList<Item>();
		for (int i = 0; i < row2; i++) {
			Item item = new Item();
			list.add(item);
		}

		return list;
	}

	public static void preidct() throws IOException {
		// TODO Auto-generated method stub
		Map<String,Integer>item_map=AR.getIndexItemStrToId(ArtUtil.train_path);
		Map<String, List<Item>>map_pre= getPredictItems() ;
		FileInputStream fis = new FileInputStream(ArtUtil.test_path);
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader reader = new BufferedReader(isr);
		String line = null;
		int idx = 0;
		double hit_count=0;
		int pre_count=0;
		double recall_count=0;
		while ((line = reader.readLine()) != null) {
			String[] strs = line.split("\t");
			String uid=strs[strs.length-2];
			List<Item>tmp=map_pre.get(uid);
			for (int i = 1; i < strs.length - 2; i++) {
				
				String item_name=strs[i];
				if(item_name.length()==0)
					continue;
				if(!item_map.containsKey(item_name))
					continue;
				recall_count++;
				for(int j=0;j<p_length;j++){
					String item_pre=tmp.get(j).name;
					if(item_name.equals(item_pre)){
						hit_count++;
					}
				}
			}
		}
		reader.close();

		double recall=hit_count/recall_count;
		double precision=hit_count/p_length/18315;
		System.out.println(hit_count+" "+recall_count);
		System.out.println(recall+","+precision);
		double f1=(2*recall*precision)/(recall+precision);
		System.out.println(f1);
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		init();
		preidct();
		System.out.println("done");
	}

}
