package art.jobs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import art.items.Tran;
import art.items.User;
import art.utils.ArtUtil;

/**
 * 
 * @author fleture
 *计算每一个item的出现次数，以及item后面跟着的item的次数
 *
 */
public class AR {

	public static Map<String, Integer> getIndexItemStrToId(String path)
			throws IOException {
		Map<String, Integer> idx_item = new HashMap<String, Integer>();
		FileInputStream fis = new FileInputStream(path);
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader reader = new BufferedReader(isr);
		String line = null;
		int idx=0;
		while ((line = reader.readLine()) != null) {
			String[] tmp = line.split("\t");
			for (int i = 1; i < tmp.length - 2; i++) {
				if(tmp[i].length()==0)
					continue;
				if(!idx_item.containsKey(tmp[i])){
					idx_item.put(tmp[i], idx);
					idx++;
				}
			}
		}
		reader.close();
		System.out.println("idx item count:"+idx_item.size());
		return idx_item;
	}
	
	public static Map<Integer, String> getIndexItemIdToStr(String path)
			throws IOException {
		 Map<Integer, String> idx_item = new HashMap<Integer, String>();
		FileInputStream fis = new FileInputStream(path);
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader reader = new BufferedReader(isr);
		String line = null;
		Set<String>set=new HashSet<String>();
		int idx=0;
		while ((line = reader.readLine()) != null) {
			String[] tmp = line.split("\t");
			for (int i = 1; i < tmp.length - 2; i++) {
				if(tmp[i].length()==0)
					continue;
				if(!set.contains(tmp[i])){
					set.add(tmp[i]);
					idx_item.put( idx,tmp[i]);
					idx++;
				}
			}
		}
		reader.close();
		System.out.println("idx item id to str count:"+idx_item.size());
		return idx_item;
	}
	
	public static int getItemCount(String path)
			throws NumberFormatException, IOException {
		//
		// BufferedReader reader = new BufferedReader(new FileReader(
		// path.trainPath));
		FileInputStream fis = new FileInputStream(path);
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader reader = new BufferedReader(isr);

		String line = null;
		Set<String> set = new HashSet<String>();
		while ((line = reader.readLine()) != null) {
			String[] tmp = line.split("\t");
			for (int i = 1; i < tmp.length - 2; i++) {

				if (tmp[i].length() > 0) {
					set.add(tmp[i]);
				}
			}
		}
		reader.close();
		System.out.println("item size:" + set.size());
		return set.size();
	}
	/**
	 * 返回每个item出现的总次数
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static Map<String,Integer>getCountMap(String path) throws IOException{
		Map<String,Integer>map=new HashMap<String,Integer>();
		FileInputStream fis = new FileInputStream(path);
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader reader = new BufferedReader(isr);
		String line = null;
		// user-tran-brand
		while ((line = reader.readLine()) != null) {
			String[]tmp=line.split("\t");
			for(int i=1;i<tmp.length-2;i++){
				String item=tmp[i];
				Integer count=map.containsKey(item)?map.get(item)+1:1;
				map.put(item, count);
			}
		}
		reader.close();
		return map;
	}
	/**
	 * 获取每个用户的交易数据，并按照时间顺序存储
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static List<User>getUserList(String path) throws IOException{
		Map<String,User>map=new HashMap<String,User>();
		List<User>result=new ArrayList<User>();
		FileInputStream fis = new FileInputStream(path);
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader reader = new BufferedReader(isr);
		String line = null;
		// user-tran-brand
		while ((line = reader.readLine()) != null) {
			String[]tmp=line.split("\t");
			String tId=tmp[0];
			String uid=tmp[tmp.length-2];
			String time=tmp[tmp.length-1];
			Tran tran=new Tran();
			tran.time=time;
			tran.tranId=tId;
			for(int i=1;i<tmp.length-2;i++){
				String item=tmp[i];
				if(item.length()==0)
					continue;
				tran.itemList.add(item);
			}
			User user=map.containsKey(uid)?map.get(uid):new User();
			user.uid=uid;
			user.tlist.add(tran);
			map.put(user.uid, user);
		}
		reader.close();
		//sort
		Iterator<String>iter_user=map.keySet().iterator();
		while(iter_user.hasNext()){
			String key=iter_user.next();
			User user=map.get(key);
			Collections.sort(user.tlist,ArtUtil.sort_tran);
			result.add(user);
		}
		return result;
	}
	
	public static Map<String,Integer>getPairCountMap(String path) throws IOException{
		List<User>list=getUserList(path);
		Map<String,Integer>map=new HashMap<String,Integer>();
		for(int i=0;i<list.size();i++){
			User user=list.get(i);
			for(int j=0;j<user.tlist.size();j++){
				for(int k=j+1;k<user.tlist.size();k++){
					Tran from=user.tlist.get(j);
					Tran to=user.tlist.get(k);
					for(String item_f:from.itemList){
						for(String item_t:to.itemList){
							String key=item_f+"_"+item_t;
							Integer count=map.containsKey(key)?map.get(key)+1:1;
							map.put(key, count);
						}
					}
				}
			}
		}
		return map;
	}


	public static void main(String[]args) throws IOException{
		Map<String,Integer>test=getPairCountMap(ArtUtil.test_path);
		Iterator<String>iter=test.keySet().iterator();
		while(iter.hasNext()){
			String key=iter.next();
			System.out.println(key+"\t"+test.get(key));
		}
		
	}
}
