package aufgabe_2;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.MultiColorScatter;
import org.jzy3d.plot3d.primitives.Scatter;

public class Drawing {
	
	private DataObject data;
	private LabelObject label;
	private boolean safeImages;

	public Drawing (DataObject data, LabelObject label, boolean safeImages) {
	
		this.data = data;
		this.label = label;
		this.safeImages = safeImages;
	}
	
	public void draw() {
		
		int x;
		int y;
		double z;
		Coord3d[] pointsValue = new Coord3d[data.size];

		int coordCounter = 0;
		
		// Create data scatter points
		for(int i=0; i<data.rows; i++){
			for(int j = 0; j < data.cols; j++) {
				x = i;
			    y = j;
			    z = data.values[i][j];
			    pointsValue[coordCounter] = new Coord3d(x, y, z);
			    coordCounter++;
			}
		    
		}
		
		// Create a drawable scatter with a colormap
		ColorMapRainbow colorMapRainbow = new ColorMapRainbow();
		ColorMapper colorMapper = new ColorMapper(colorMapRainbow , (int)data.valueMin, (int)data.valueMax);
		MultiColorScatter scatterValue = new MultiColorScatter( pointsValue, colorMapper);
		
		
		// Label drawing
		
		Coord3d[] pointsLabel = new Coord3d[label.points.size()];
		
		// Create label scatter points
		for(int i = 0; i < pointsLabel.length; i++) {
			x = label.points.get(i).x;
			y = label.points.get(i).y;
			z = data.values[label.points.get(i).x][label.points.get(i).y];
			pointsLabel[i] = new Coord3d(x, y, z);
		}
		
		Scatter scatterLabel = new Scatter(pointsLabel, Color.WHITE);
		scatterLabel.setWidth(5);
		// Create a chart and add scatters
		Chart chart = new Chart();
		chart.getAxeLayout().setMainColor(Color.WHITE);
		chart.getView().setBackgroundColor(Color.BLACK);
		chart.getScene().add(scatterValue);
		chart.getScene().add(scatterLabel);
		chart.getView().rotate(new Coord2d(0.0, -0.5), true);
		ChartLauncher.openChart(chart);
		
		// Safe Images
		if(safeImages) {
			for(int i = 0; i < 63; i++) {
				Coord2d rotateCoord= new Coord2d(0.1, 0.0);
				chart.getView().rotate(rotateCoord, true);
				
				File outputfile = new File("Images\\Data0\\image" + i + ".jpg");
				try {
					ImageIO.write(chart.screenshot(), "jpg", outputfile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
