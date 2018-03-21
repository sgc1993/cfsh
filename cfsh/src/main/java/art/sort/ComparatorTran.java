package art.sort;


import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import art.items.Tran;


public class ComparatorTran implements Comparator {

	public int compare(Object o1, Object o2) {
		Tran n1 = (Tran) o1;
		Tran n2 = (Tran) o2;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date time1 = null;
		Date time2 = null;
		try {
			time1 = formatter.parse(n1.time);
			time2 = formatter.parse(n2.time);
		} catch (Exception ex) {

		}
		if (time1.after(time2))
			return 1;
		else
			return -1;
	}


}
