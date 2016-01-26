package helipilot50.orientdb.recommendation.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

import helipilot50.orientdb.recommendation.Constants;
import helipilot50.orientdb.recommendation.FineFoodsService;
import helipilot50.orientdb.recommendation.Recommendation;


@Controller
public class RESTController {
	private static Logger log = LoggerFactory.getLogger(RESTController.class);

	@Autowired
	OrientGraphFactory  graphFactory;



	/**
	 * get a recommendation for a specific user
	 * @param The user ID for a User
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/finefoods/recommendation/{userId}", method=RequestMethod.GET)
	public @ResponseBody Recommendation getAerospikeRecommendationFor(@PathVariable("userId") String userId) throws Exception {
		log.debug("Finding recomendations for " + userId);
		OrientGraphNoTx graph = graphFactory.getNoTx();
		FineFoodsService service = new FineFoodsService(graph);
		/*
		 * find user
		 */
		Vertex vUser = service.findUserById(userId);
		/*
		 * build a vector list of Reviews for User
		 */
		List<Double> thisUserReviewVector = service.makeVectorForUser(vUser);

		Recommendation rec = new Recommendation(userId, productListAsString(service.productsForUser(vUser)));

		Vertex bestMatchedUser = null;
		List<Vertex> bestMatchedList = null;
		double bestScore = 0;
		/*
		 * for each Review from this User, iterate
		 * through the other User that also reviewed
		 * the food 
		 */
		List<Vertex> otherUsers = service.similarUsers(vUser);
		for (Vertex similarUser : otherUsers){
			if (!similarUser.equals(userId)) {
				// find user with the highest similarity

				List<Double> similarUserVector = service.makeVectorForUser(similarUser);

				double score = easySimilarity(thisUserReviewVector, similarUserVector);

				if (score > bestScore){

					bestScore = score;
					bestMatchedUser = similarUser;
					bestMatchedList = service.productsForUser(similarUser);
				}
			}
		}
		log.debug("Best customer: " + bestMatchedUser);
		log.debug("Best score: " + bestScore);
		// return the best matched user's purchases as the recommendation
		Set<String> recommendedProducts = productListAsString(bestMatchedList);


		log.debug("Found these recomendations: " + recommendedProducts);
		
		rec.setRecommendedProducts(recommendedProducts);
		return rec;
	}


	/**
	 * This is a very rudimentary algorithm using Cosine similarity
	 * @param thisUserVector
	 * @param similarUserVector
	 * @return
	 */
	public double easySimilarity(List<Double> thisUserVector, List<Double> similarUserVector){
		/*
		 * this is the place where you can create clever
		 * similarity score.
		 * 
		 * This algorithm simple returns how many reviews these users have in common.
		 * 
		 * You could use any similarity algorithm you wish
		 */

		return CosineSimilarity.cosineSimilarity(thisUserVector, similarUserVector);
	}

	private Set<String> productListAsString(List<Vertex> productList){
		Set<String> theList = new HashSet<String>();
		for (Vertex prod : productList){
			theList.add((String) prod.getProperty(Constants.PRODUCT_ID));
		}
		return theList;
	}
}
