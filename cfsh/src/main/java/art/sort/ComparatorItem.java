package art.sort;


import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import art.items.Item;
import art.items.Tran;


public class ComparatorItem implements Comparator {

	public int compare(Object o1, Object o2) {
		Item n1 = (Item) o1;
		Item n2 = (Item) o2;
	
		if(n1.score<n2.score)
			return 1;
		return -1;
	}


}
