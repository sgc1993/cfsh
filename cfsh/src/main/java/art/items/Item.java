package art.items;

public class Item {

	public String name;
	public int id;
	public double[] ls;
	public int count;
	public double score;
	public Item(int count) {
		ls = new double[count];
	}
	public Item() {
		// TODO Auto-generated constructor stub
	}
}
