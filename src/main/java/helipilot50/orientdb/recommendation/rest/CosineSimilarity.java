package helipilot50.orientdb.recommendation.rest;

import java.util.List;

public class CosineSimilarity {

	/**
	 * Cosing similarity
	 * @param vec1
	 * @param vec2
	 * @return
	 */
	public static double cosineSimilarity(List<Double> vec1, List<Double> vec2) { 
		double dp = dotProduct(vec1, vec2); 
		double magnitudeA = magnitude(vec1); 
		double magnitudeB = magnitude(vec2); 
		return dp / magnitudeA * magnitudeB; 
	} 
	/**
	 * Magnitude
	 * @param vec
	 * @return
	 */
	public static double magnitude(List<Double> vec) { 
		double sum_mag = 0; 
		for(Double value : vec) { 
			sum_mag += value * value; 
		} 
		return Math.sqrt(sum_mag); 
	} 
	/**
	 * Dot product
	 * @param vec1
	 * @param vec2
	 * @return
	 */
	public static double dotProduct(List<Double> vec1, List<Double> vec2) { 
		double sum = 0; 
		if (vec1.size() > vec2.size()) {
			int diff = vec1.size() - vec2.size();
			for (int i = 0; i < diff; i++)
					vec2.add(0.0d);
			
		} else if (vec1.size() < vec2.size()) {
			int diff = vec2.size() - vec1.size();
			for (int i = 0; i < diff; i++)
					vec1.add(0.0d);
		}
		for(int i = 0; i<vec1.size(); i++) { 
			sum += vec1.get(i) * vec2.get(i); 
		} 
		return sum; 
	} 

}
