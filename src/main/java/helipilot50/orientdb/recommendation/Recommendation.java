package helipilot50.orientdb.recommendation;

import java.util.HashSet;
import java.util.Set;

public class Recommendation {
	String userId;
	Set<String> reviewedProducts;
	Set<String> recommendedProducts;
	public Set<String> getReviewedProducts() {
		return reviewedProducts;
	}
	public void setReviewedProducts(Set<String> reviewedProducts) {
		this.reviewedProducts = reviewedProducts;
	}
	public Set<String> getRecommendedProducts() {
		return recommendedProducts;
	}
	public void setRecommendedProducts(Set<String> recommendedProducts) {
		this.recommendedProducts = recommendedProducts;
	}
	
	public String getUserId() {
		return userId;
	}
	public Recommendation(String userId) {
		super();
		this.userId = userId;
		this.reviewedProducts = new HashSet<String>();
		this.recommendedProducts = new HashSet<String>();
	}
	public Recommendation(String userId, Set<String> reviewedProducts, Set<String> recommendedProducts) {
		super();
		this.userId = userId;
		this.reviewedProducts = reviewedProducts;
		this.recommendedProducts = recommendedProducts;
	}
	public Recommendation(String userId, Set<String> reviewedProducts) {
		super();
		this.userId = userId;
		this.reviewedProducts = reviewedProducts;
	}
	
}
