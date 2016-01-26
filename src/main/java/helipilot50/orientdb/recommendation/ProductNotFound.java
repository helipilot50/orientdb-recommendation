package helipilot50.orientdb.recommendation;

public class ProductNotFound extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2689850285266341615L;
	private String productId;

	public ProductNotFound() {
		super();
	}


	public ProductNotFound(String productId, Throwable cause) {
		super("Product not found: " + productId, cause);
		this.productId = productId;
	}


	public ProductNotFound(String productId) {
		super("Product not found: " + productId);
		this.productId = productId;
	}


	public String getProductId() {
		return productId;
	}

	
}
