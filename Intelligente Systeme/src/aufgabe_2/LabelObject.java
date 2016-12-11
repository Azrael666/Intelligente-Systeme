package aufgabe_2;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Alexandra Scheben, Dirk Teschner
 *
 * label object
 * contains preset labels
 *
 */
public class LabelObject {

	//list of labels
	private List<Point> points;
	//list size
	private int size;
	
	/**
	 * constructor
	 * @param inputFileName filename of the label sheet
	 */
	public LabelObject (String inputFileName) {
		
		LinkedList<Point> points = new LinkedList<Point>();
		
		// adding all points from the sheets to the list
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
		
		//set list
		setPoints(points);
		//set size
		setSize(points.size());
	}

	/**
	 * gets the list of points
	 * @return list of points
	 */
	public List<Point> getPoints() {
		return points;
	}

	/**
	 * gets the size of the list of points
	 * @return size of the list of points
	 */
	public int getSize() {
		return size;
	}

	/**
	 * sets the list of points
	 * @param points list of points
	 */
	public void setPoints(List<Point> points) {
		this.points = points;
	}

	/**
	 * sets the size of the list
	 * @param size size of the list
	 */
	public void setSize(int size) {
		this.size = size;
	}
}
