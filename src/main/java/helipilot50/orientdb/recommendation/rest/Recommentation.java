package helipilot50.orientdb.recommendation.rest;

public class Recommentation {
	String productId;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Recommentation(String productId) {
		super();
		this.productId = productId;
	}
	
}
