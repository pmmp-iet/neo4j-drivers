package neo4j.driver.util;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.neo4j.driver.internal.InternalNode;
import org.neo4j.driver.internal.InternalRelationship;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.Values;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Relationship;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import neo4j.driver.util.PrettyPrinter;

public class PrettyPrintingNodeTest {

	@Test
	public void testName() throws Exception {
		List<String> labels = ImmutableList.of("Person");
		Map<String, Value> nodeProperties = ImmutableMap.of("name", Values.value("John Doe"));
		Node node = new InternalNode(1, labels, nodeProperties);
		System.out.println(PrettyPrinter.toString(node));

		Map<String, Value> relationshipProperties = ImmutableMap.of("weight", Values.value(2));
		Relationship rel = new InternalRelationship(5, 1, 2, "REL", relationshipProperties);
		System.out.println(PrettyPrinter.toString(rel));
	}

}
