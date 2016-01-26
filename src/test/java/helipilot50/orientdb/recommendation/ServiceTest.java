package helipilot50.orientdb.recommendation;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

public class ServiceTest {
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

	//@Test
	public void findUserByIdTest() {
		Vertex vUser = service.findUserById("A3SGXH7AUHU8GW");
		Assert.assertNotNull(vUser);
	}

	@Test
	public void createUserTest() {
		Vertex vUser = service.createUser("A3SGXH7AUHU8GW", "delmartian");
		Assert.assertNotNull(vUser);
		vUser = service.createUser("A3SGXH7AUHU8GW", "delmartian");
		Assert.assertNotNull(vUser);
	}

}
