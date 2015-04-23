package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels.example;

public class Point {
	
	private String id;
	private String comment;
	private Double xValue;
	private Double yValue;
	
	
	public Point(String id, String comment, Double xValue, Double yValue) {
		this.id = id;
		this.comment = comment;
		this.xValue = xValue;
		this.yValue = yValue;
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Double getxValue() {
		return xValue;
	}
	public void setxValue(Double xValue) {
		this.xValue = xValue;
	}
	public Double getyValue() {
		return yValue;
	}
	public void setyValue(Double yValue) {
		this.yValue = yValue;
	}
	
		

}
