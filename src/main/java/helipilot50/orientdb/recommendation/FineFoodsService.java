package helipilot50.orientdb.recommendation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

public class FineFoodsService {
	private OrientGraphNoTx  graph;
	private static final Logger log = LoggerFactory.getLogger(FineFoodsService.class);
	public FineFoodsService(OrientGraphNoTx graph) {
		super();
		this.graph = graph;
	}
	public Vertex findProduct(String productId){
		Vertex vProduct = null;
		Iterable<Vertex> products = graph.getVertices(Constants.PRODUCT_ID, productId);
		if (products.iterator().hasNext()) {
			vProduct = products.iterator().next();
		} else {
			throw new ProductNotFound(productId);
		}
		return vProduct;

	}
	public Vertex findUserByProfileName(String profileName){
		Vertex vUser = null;
		Iterable<Vertex> users = graph.getVertices(Constants.PROFILE_NAME, profileName);
		if (users.iterator().hasNext()) {
			vUser = users.iterator().next();
		} else {
			throw new UserNotFound(profileName);
		}
		return vUser;
	}
	public Vertex findUserById(String userId){
		Vertex vUser = null;
		Iterable<Vertex> users = graph.getVertices(Constants.USER_ID, userId);
		if (users.iterator().hasNext()) {
			vUser = users.iterator().next();
		} else {
			throw new UserNotFound(userId);
		}

		return vUser;
	}

	public Vertex createUser(String userId, String profileName){
		Vertex vUser = null;
		try {
			vUser = findUserById(userId);
		} catch (UserNotFound e) {
			/*
			 * create a new user 
			 */
			vUser = graph.addVertex("class:User");
			vUser.setProperty(Constants.USER_ID, userId);
			vUser.setProperty(Constants.PROFILE_NAME, profileName);
		}
		return vUser;

	}

	public List<Double> makeVectorForUser(Vertex vUser){
		List<Double> reviewVector = new ArrayList<Double>();
		Iterable<Edge> reviewEdges = vUser.getEdges(Direction.OUT, Constants.EDGE_REVIEWED);
		for (Edge review : reviewEdges){
			Vertex vProduct = review.getVertex(Direction.OUT);
			String productVertexPostfix = vProduct.getId().toString();
			productVertexPostfix = productVertexPostfix.substring(productVertexPostfix.lastIndexOf(':')+1);
			reviewVector.add(Double.parseDouble(productVertexPostfix));
			Double score = review.getProperty("score");
			reviewVector.add(score);
		}
		return reviewVector;
	}

	public boolean vectorContains(Vertex vert, List<Double> vector){
		String vertexPostfix = vert.getId().toString();
		vertexPostfix = vertexPostfix.substring(vertexPostfix.lastIndexOf(':')+1);
		Double postfix = Double.parseDouble(vertexPostfix);
		return vector.contains(postfix);
	}

	public Vertex createProduct(String productId){
		Vertex vProduct = null;
		try {
			vProduct = findProduct(productId);
		} catch (ProductNotFound e){
			vProduct = graph.addVertex("class:Product");
			vProduct.setProperty(Constants.PRODUCT_ID, productId);
		}
		return vProduct;
	}

	public Edge createReview(Vertex vUser, Vertex vProduct, Map<String, Object> review){
		Edge toProduct = graph.addEdge(null, vUser, vProduct, Constants.EDGE_REVIEWED);
		for (Map.Entry<String, Object> entry : review.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			toProduct.setProperty(key, value);
		}
		return toProduct;
	}

	public List<Vertex> productsForUser(Vertex vUser){
		List<Vertex> products = new ArrayList<Vertex>();
		Iterable<Edge> reviewEdges = vUser.getEdges(Direction.OUT, Constants.EDGE_REVIEWED);
		for (Edge review : reviewEdges){
			Vertex vProduct = review.getVertex(Direction.IN);
			products.add(vProduct);
		}
		return products;
	}
	public List<Vertex> usersForProduct(Vertex vProduct){
		List<Vertex> users = new ArrayList<Vertex>();
		Iterable<Edge> reviewEdges = vProduct.getEdges(Direction.IN, Constants.EDGE_REVIEWED);
		for (Edge review : reviewEdges){
			Vertex vUser = review.getVertex(Direction.OUT);
			users.add(vUser);
		}
		return users;
	}

	public List<Vertex> similarUsers(Vertex vUser){
		
		List<Vertex> users = new ArrayList<Vertex>();
		List<Vertex> products = productsForUser(vUser);
		for (Vertex product : products){
			List<Vertex> similarUsers = usersForProduct(product);
			for (Vertex  user : similarUsers){
				users.add(user);
			}
		}
		return users;
	}
	
}
