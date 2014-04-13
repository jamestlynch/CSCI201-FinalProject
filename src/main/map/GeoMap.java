package main.map;

import java.awt.Dimension;

import javax.swing.JPanel;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;

public class GeoMap extends JPanel {
	private GeoMapView view;
	private GeoMapModel model;
	
	public GeoMap(GeoMapView view, GeoMapModel model) {
		this.view = view;
		this.model = model;
		
//		this.view.drawPath(this.model.);
	}
	
	public GeoMapView getViewInstance() {
		return view;
	}
	
	public GeoMapModel getModelInstance() {
		return model;
	}
}
