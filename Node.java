package ca.uwaterloo.lab4_202_11;

import android.graphics.PointF;

public class Node extends PointF {
	int G_score, H_score, F_score;
	Node parent;
	
	
	
	public Node() {
		super();
	}
	
	public Node(float x, float y) {
		super(x,y);	
	}

	public void calculateF_score() {
		F_score = G_score + H_score;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	
	public int getG_score() {
		return G_score;
	}

	public void setG_score(int g_score) {
		G_score = g_score;
	}

	public int getH_score() {
		return H_score;
	}

	public void setH_score(int h_score) {
		H_score = h_score;
	}

	public int getF_score() {
		return F_score;
	}

	public void setF_score(int f_score) {
		F_score = f_score;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	
	
	
	
	

}
