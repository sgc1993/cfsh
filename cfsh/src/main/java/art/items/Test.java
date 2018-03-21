package art.items;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class Test {

	public static String train_from_path="F:\\test\\offline_train.txt";
	public static String train_to_path="F:\\test\\train.txt";
	public static String test_from_path="F:\\test\\offline_test.txt";
	public static String test_to_path="F:\\test\\test.txt";
	public static void format(String from_path,String to_path) throws IOException{
		FileInputStream fis = new FileInputStream(from_path);
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader reader = new BufferedReader(isr);
		String line = null;	
		StringBuilder sb=new StringBuilder();
		while ((line = reader.readLine()) != null) {
			String[] tmp = line.split("\t");
			sb.append(tmp[0]).append("\t").append(tmp[tmp.length-1]).append("\t").append(tmp[tmp.length-2]).append("\t");
			for(int i=1;i<tmp.length-2;i++)
				sb.append(tmp[i]+",");
			sb.append("\n");
		}
		reader.close();
		FileOutputStream fos = new FileOutputStream(to_path);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		BufferedWriter bw = new BufferedWriter(osw);
		bw.write(sb.toString());
		bw.close();
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		format(test_from_path,test_to_path);
	}

}
