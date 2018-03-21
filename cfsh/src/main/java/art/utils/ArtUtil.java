package art.utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import art.items.Item;
import art.items.User;
import art.sort.ComparatorItem;
import art.sort.ComparatorTran;

public class ArtUtil {
	/**
	 * 用来统计每个item总共出现的次数
	 */
	public static Map<String, Integer> map_count = new HashMap<String, Integer>();
	/**
	 * 用来统计所以的transaction中item——item的次数
	 */
	public static Map<String, Integer> map_conf_count = new HashMap<String, Integer>();

	public static ComparatorTran sort_tran=new ComparatorTran();
	public static ComparatorItem sort_item=new ComparatorItem();
	private static String path="C:\\Users\\fleture\\workspace\\art\\data\\";
	
	public static String train_path=path+"offline_train.txt";
	public static String test_path=path+"offline_test.txt";
	public static String res_left_path=path+"v_left";
	public static String res_right_path=path+"v_right";
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
