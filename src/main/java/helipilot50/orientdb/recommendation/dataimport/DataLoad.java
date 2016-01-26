package helipilot50.orientdb.recommendation.dataimport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

import helipilot50.orientdb.recommendation.Constants;
import helipilot50.orientdb.recommendation.FineFoodsService;


public class DataLoad{
	
	private static final Logger log = LoggerFactory.getLogger(DataLoad.class);
	private OrientGraphNoTx  graph;
	OrientGraphFactory factory;
	FineFoodsService service;
	//	private OGraphDatabase odb;
	

	public DataLoad() {
		factory = new OrientGraphFactory(Constants.DEFAULT_DB);
		graph = factory.getNoTx();
		service = new FineFoodsService(graph);
	}
	public static void main(String[] args) throws FileNotFoundException, IOException {

		DataLoad dl = new DataLoad();
		dl.processFile(new File("data/finefoodsaa"));


	}
	public void processFile(File inputFile) throws FileNotFoundException, IOException{
		int lineNumber = 0;
		int recordCount = 0;
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		String line;
		Map<String,Object> review = new HashMap<String, Object>();
		while((line = br.readLine()) != null) {
			lineNumber++;

			if (line.trim().isEmpty()) { 
				
				/*
				 * Find or create a user
				 */
				String userId = (String) review.get(Constants.USER_ID);
				String profileName = (String) review.get(Constants.PROFILE_NAME);
				Vertex vUser = service.createUser(userId, profileName);
				/*
				 * Find or create a product
				 */
				String productId = (String) review.get(Constants.PRODUCT_ID);
				Vertex vProduct = service.createProduct(productId);

				/*
				 * Create Edge from User to Product
				 */
				
				Edge toProduct = service.createReview(vUser, vProduct, review);

				graph.commit();

				recordCount++;
				
//				if (log.isDebugEnabled() && recordCount % 10000 == 0){
//					log.info(String.format("User: %s", vUser.getId().toString()));
//					log.info(String.format("\tProfile name: %s", vUser.getProperty(Constants.PROFILE_NAME)));
//					Iterable<Edge> reviewEdges = vUser.getEdges(Direction.OUT, Constants.EDGE_REVIEWED);
//					log.info("\tReviews");
//					for (Edge edge : reviewEdges){
//						log.info(String.format("\t\tEdge Id: %s", edge.getId().toString()));
//						vProduct = edge.getVertex(Direction.OUT);
//						log.info(String.format("\t\tProduct vId: %s", vProduct.getId().toString()));
//						log.info(String.format("\t\tproductId: %s", vProduct.getProperty(Constants.PRODUCT_ID)));
//						log.info(String.format("\t\thelpfulness: %s", edge.getProperty("helpfulness")));
//						log.info(String.format("\t\tscore: %f", edge.getProperty("score")));
//						log.info(String.format("\t\ttime: %d", edge.getProperty("time")));
//						log.info(String.format("\t\tsummary: %s", edge.getProperty("summary")));
//						log.info(String.format("\t\ttext: %s", edge.getProperty("text")));
//					}
//				}
				if (recordCount == 50000)
					break;
				/*
				 * start a new review
				 */
				review = new HashMap<String, Object>();
			} else {

				String[] elements = line.split(": ");
				String key = elements[0].substring(elements[0].lastIndexOf('/')+1);
				if (key.equalsIgnoreCase("score"))
					review.put(key, Double.parseDouble(elements[1]));
				else if (key.equalsIgnoreCase("time"))
					review.put(key, Long.parseLong(elements[1]));
				else
					review.put(key, elements[1]);
			}

			if (lineNumber % 10000 == 0){
				log.info(String.format("Processed %d records from %d lines from file: %s", recordCount, lineNumber, inputFile.getName()));
			}
		}
		br.close();
		log.info(String.format("Processed %d records from %d lines from file: %s", recordCount, lineNumber, inputFile.getName()));

	}

	protected void finalize() throws Throwable {
		if (this.factory != null)
			factory.close();
	}


	private boolean checkFileExists(File file){
		if (!file.exists()) {
			return false;
		}
		return true;

	}

}
