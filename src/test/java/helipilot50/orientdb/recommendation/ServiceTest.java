package helipilot50.orientdb.recommendation;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

public class ServiceTest {
	/* users
	 * A2A9X58G2GTBLP
	 * A1CZX3CP8IKQIJ
	 */
	private static final String TARGET_USER_ID = "A1CZX3CP8IKQIJ";
	private OrientGraphNoTx  graph;
	OrientGraphFactory factory;
	FineFoodsService service;

	@Before
	public void setUp() throws Exception {
		factory = new OrientGraphFactory(Constants.DEFAULT_DB);
		graph = factory.getNoTx();
		service = new FineFoodsService(graph);

	}

	@After
	public void tearDown() throws Exception {
		factory.close();
	}

	@Test
	public void findUserByIdTest() {
		System.out.println("Test - findUserByIdTest");
		Vertex vUser = service.findUserById(TARGET_USER_ID);
		Assert.assertNotNull(vUser);
		System.out.println(vUser + " - " + vUser.getProperty(Constants.USER_ID));
	}

	@Test
	public void productUsersTest() {
		System.out.println("Test - productUsersTest");
		Vertex vProduct = service.findProduct("B00813GRG4");
		Assert.assertNotNull(vProduct);
		List<Vertex> users = service.usersForProduct(vProduct);
		Assert.assertNotNull(users);
		for (Vertex user : users){
			System.out.println("\t" + user + " - " + user.getProperty(Constants.USER_ID));
		}
	}

	@Test
	public void userProductsTest() {
		System.out.println("Test - userProductsTest");
		Vertex vUser = service.findUserById(TARGET_USER_ID);
		Assert.assertNotNull(vUser);
		System.out.println(vUser + " - " + vUser.getProperty(Constants.USER_ID));
		List<Vertex> products = service.productsForUser(vUser);
		Assert.assertNotNull(products);
		for (Vertex product : products){
			System.out.println("\t" + product + " - " + product.getProperty(Constants.PRODUCT_ID));
		}
	}
	@Test
	public void similarUsersTest() {
		System.out.println("Test - similarUsersTest");
		Vertex vUser = service.findUserById(TARGET_USER_ID);
		Assert.assertNotNull(vUser);
		System.out.println(vUser + " - " + vUser.getProperty(Constants.USER_ID));
		List<Vertex> similarUsers = service.similarUsers(vUser);
		Assert.assertNotNull(similarUsers);
		for (Vertex user : similarUsers){
			System.out.println("\t" + user + " - " + user.getProperty(Constants.USER_ID));
		}
	}


	@Test
	public void userProductsUsersTest() {
		System.out.println("Test - userProductsUsersTest");
		Vertex vUser = service.findUserById(TARGET_USER_ID);
		Assert.assertNotNull(vUser);
		System.out.println(vUser + " - " + vUser.getProperty(Constants.USER_ID));
		List<Vertex> products = service.productsForUser(vUser);
		Assert.assertNotNull(products);
		for (Vertex product : products){
			List<Vertex> similarUsers = service.usersForProduct(product);
			System.out.println("\t" + product + " - " + product.getProperty(Constants.PRODUCT_ID));
			for (Vertex  user : similarUsers){
				System.out.println("\t\t" + user.getProperty(Constants.USER_ID));
			}
		}
	}
	@Test
	public void userVectorTest() {
		System.out.println("Test - userVectorTest");
		Vertex vUser = service.findUserById(TARGET_USER_ID);
		Assert.assertNotNull(vUser);
		System.out.println(vUser + " - " + vUser.getProperty(Constants.USER_ID));
		List<Double> vector = service.makeVectorForUser(vUser);
		System.out.println("\tVector: " + vector);
	}
}
