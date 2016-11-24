package aufgabe_2;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class LabelObject {

	List<Point> points;
	int size;
	
	LabelObject (String inputFileName) {
		
		points = new LinkedList<Point>();
		
		try{
			
		BufferedReader input = new BufferedReader(new FileReader(inputFileName));

		String line;
		
		while((line = input.readLine()) != null) {
			String elements[] = line.split(",");
			elements[0] = elements[0].substring(0, elements[0].length()-4);
			elements[1] = elements[1].substring(0, elements[1].length()-4);
			int x = Integer.parseInt(elements[1]);
			int y = Integer.parseInt(elements[0]);
			points.add(new Point(x,y));
		}
		
		input.close();
		
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		size = points.size();
	}
}
