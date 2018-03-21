package art.items;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class User {

	public String uid;
	public List<Tran> tlist = new ArrayList<Tran>();

	public Set<String>itemSet=new HashSet<String>();
}