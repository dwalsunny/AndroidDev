package ca.uwaterloo.lab4_202_11;

import java.util.ArrayList;
import java.util.List;

import mapper.InterceptPoint;
import mapper.MapView;
import mapper.NavigationalMap;
import android.graphics.PointF;

public class PathFinder {
	public final float EPSILON = 1;
	
	List<Node> openList, closedList;
	NavigationalMap navMap;
	Node destination;
	Node current;
	Node origin;
	MapView mapView;
	
	
	public PathFinder(MapView mv, NavigationalMap navMap) {
		openList = new ArrayList<Node>();
		closedList = new ArrayList<Node>();
		this.navMap = new NavigationalMap();
		mapView = mv;
	}
	
	
	public void calculateRoute(Node originNode){
		Node n = originNode;

		
		openList.clear();
		closedList.clear();
		
		setCurrent(n);
		addToClosedList(n);
		
		checkAdjacentNodes();
		
		boolean isDestination = false;
		while(!isDestination) {
				
			checkAdjacentNodes();
		
			// Search for lowest F_score on openList
			int score = 500;
			Node c = new Node();
			for(Node lowScore : openList) {
				if(lowScore.getF_score() < score) {
					score = lowScore.getF_score();
					c = lowScore;	// Potential for ERROR
				}
			}
			
			setCurrent(c);
			removeFromOpenList(c);
			addToClosedList(c);
			if(c.equals(destination.x, destination.y)) {
				isDestination = true;
				setParent(c, destination);
				
			}
		}
		List<Node> route = generateRoute();
		displayRoute(route);
		
	}
	
	public List<Node> generateRoute() {
		List<Node> route = new ArrayList<Node>();
		route.add(destination);
		Node dest = destination;
		boolean atOrigin = false;
		while(!atOrigin) {
			route.add(dest.getParent());
			Node parent = dest.getParent();
			if(parent.equals(origin.x, origin.y)) { 	// Potential for ERROR
				atOrigin = true;
			}
		}
		
		return route;
	}
	
	public void checkAdjacentNodes() {
		// Adding adjacent squares to open list
		for(float i=-0.5f;i<=0.5f;i=i+0.5f) {
			for(float j=-0.5f;j<=0.5f;j=i+0.5f) {
				// Ignore diagonal squares
				if(Math.abs(i)!=Math.abs(j)) {
					float x = current.getX()+i;
					float y = current.getY()+j;
					Node adjacentNode = new Node(x,y);
					List<InterceptPoint> intersect = navMap.calculateIntersections(current, adjacentNode);
					// Check if adjacent node is already in the closed list
					boolean inClosedList = false;
					
					for(Node h : closedList) {
						if(adjacentNode.equals(h.x, h.y)) {
							inClosedList = true;
						}
					}
					if(intersect.isEmpty() && !inClosedList) {
						addToOpenList(adjacentNode);
						calculateScores(adjacentNode);
						setParent(current,adjacentNode);
					}
				}
			}
		}
	}
	
	
	public void displayRoute(List<Node> route) {
		List<PointF> routePoints = new ArrayList<PointF>();
		routePoints.addAll(route);
		mapView.setUserPath(routePoints);
	}
	
	public void calculateScores(Node n) {
		calculate_G(n);
		calculate_H(n);
		calculate_F(n);
	}
	
	public void setParent(Node cur, Node adj) {
		adj.setParent(cur);
	}
	
	public void addToOpenList(Node n) {
		openList.add(n);
	}
	public void removeFromOpenList(Node n) {
		openList.remove(n);
	}
	
	public void addToClosedList(Node n) {
		closedList.add(n);
	}
	
	public void setCurrent (Node n) {
		current = n;
	}
	private void calculate_G(Node n) {
		float x = (int)Math.abs(origin.x-n.x);
		float y = (int)Math.abs(origin.y-n.y);
		int score = Math.round((x*10)+(y*10));	// Potential for rounding errors in scores
		n.setG_score(score);
	}
	
	private void calculate_H(Node n) {
		int x = (int) Math.abs(n.x-destination.x);
		int y = (int) Math.abs(n.y-destination.y);
		int score = Math.round((x*10)+(y*10));	// Potential for rounding errors in scores
		n.setH_score(score);
	}
	
	private void calculate_F(Node n) {
		n.calculateF_score();
	}
	
	public void setOrigin(Node originNode) {
		origin = originNode;
		if(destination != null) {
			calculateRoute(origin);	
		}
	}
	
	public void setDestination(Node destinationNode) {
		destination = destinationNode;
		if(origin != null) {
			calculateRoute(origin);
		}
	}
	

}
