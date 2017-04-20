package neo4j.driver.reactive;

import static org.neo4j.driver.v1.Values.parameters;

import org.junit.Before;
import org.junit.Test;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.Value;

import neo4j.driver.reactive.impl.Neo4jReactiveDriver;
import neo4j.driver.reactive.interfaces.ReactiveDriver;
import neo4j.driver.reactive.interfaces.ReactiveSession;
import neo4j.driver.testkit.EmbeddedTestkitDriver;

public class Neo4jReactiveDriverTest {

	protected Driver embeddedTestkitDriver;
	protected ReactiveDriver driver;
	protected ReactiveSession session;

	private void runUpdate(ReactiveSession session, String statementTemplate, Value parameters) {
		System.out.println("Running query: " + statementTemplate);
		System.out.println("With parameters: " + parameters);
		System.out.println();

		try (Transaction tx = session.beginTransaction()) {
			tx.run(statementTemplate, parameters);
			tx.success();
		}

		System.out.println();
	}

	@Before
	public void before() {
		embeddedTestkitDriver = new EmbeddedTestkitDriver();
		driver = new Neo4jReactiveDriver(embeddedTestkitDriver);
		session = driver.session();
	}

	@Test
	public void test1() throws Exception {
		final String PERSONS_QUERY = "persons";

		session.registerQuery(PERSONS_QUERY, "MATCH (a:Person) RETURN a");
		runUpdate(session, "CREATE (a:Person {name: $name, title: $title})",
				parameters("name", "Arthur", "title", "King"));
		runUpdate(session, "CREATE (a:Person {name: $name, title: $title})",
				parameters("name", "Arthur", "title", "King"));
		runUpdate(session, "MATCH (a:Person {name: $name, title: $title}) DELETE a",
				parameters("name", "Arthur", "title", "King"));
	}

	@Test
	public void test2() throws Exception {
		final String PERSONS_QUERY = "persons";

		session.registerQuery(PERSONS_QUERY, "MATCH (a:Person) RETURN a");
		runUpdate(session, "CREATE (a:Person {name: $name})", parameters("name", "Alice"));
		runUpdate(session, "CREATE (a:Person {name: $name})", parameters("name", "Bob"));
	}

	@Test
	public void test3() throws Exception {
		try (Transaction tx = session.beginTransaction()) {
			tx.run("CREATE (a:Person {name: $name})", parameters("name", "Alice"));
			tx.success();
		}

		final String PERSONS_QUERY = "persons";

		session.registerQuery(PERSONS_QUERY, "MATCH (a:Person) RETURN a");
		runUpdate(session, "CREATE (a:Person {name: $name})", parameters("name", "Bob"));
	}

}
