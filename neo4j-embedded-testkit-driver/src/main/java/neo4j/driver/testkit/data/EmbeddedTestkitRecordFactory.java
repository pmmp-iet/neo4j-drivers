package neo4j.driver.testkit.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.neo4j.driver.internal.InternalNode;
import org.neo4j.driver.internal.InternalRecord;
import org.neo4j.driver.internal.InternalRelationship;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.Values;
import org.neo4j.graphdb.Entity;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import com.google.common.collect.Maps;

import scala.collection.JavaConversions;
import scala.collection.convert.Wrappers;

public class EmbeddedTestkitRecordFactory {

	private EmbeddedTestkitRecordFactory(){}
	public static Record create(Map<String, Object> element) {
		final List<String> keys = new ArrayList<>(element.size());
		final List<Value> values = new ArrayList<>(element.size());

		for (Entry<String, Object> entry : element.entrySet()) {
			keys.add(entry.getKey());
			final Value value = convert(entry.getValue());
			values.add(value);
		}

		return new InternalRecord(keys, values.toArray(new Value[values.size()]));
	}

	private static Value convert(Object value) {
		final Object myValue;

		if (value instanceof Entity) { // Node or Relationship
			final Entity entity = (Entity) value;

			final long id = entity.getId();
			final Map<String, Value> properties = Maps.newHashMap();
			for (final Map.Entry<String, Object> entry : entity.getAllProperties().entrySet()) {
				properties.put(entry.getKey(), convert(entry.getValue()));
			}

			if (value instanceof Node) {
				final Node node = (Node) value;
				final List<String> labels = StreamSupport.stream(node.getLabels().spliterator(), false)
						.map(label -> label.name()).collect(Collectors.toList());

				myValue = new InternalNode(id, labels, properties);
			} else if (value instanceof Relationship) {
				final Relationship relationship = (Relationship) value;
				final long start = relationship.getStartNode().getId();
				final long end = relationship.getEndNode().getId();
				final String type = relationship.getType().name();

				myValue = new InternalRelationship(id, start, end, type, properties);
			} else {
				throw new UnsupportedOperationException(
						String.format("Entity %s is neither a Node nor a Relationship.", value));
			}
		} else if (value instanceof Wrappers.SeqWrapper<?>) {
			final Wrappers.SeqWrapper<?> seqWrapper = (Wrappers.SeqWrapper<?>) value;
			final List<?> list = JavaConversions.seqAsJavaList(seqWrapper.underlying());
			myValue = list.stream().map(element -> convert(element)).collect(Collectors.toList());
		} else {
			myValue = value;
		}

		return Values.value(myValue);
	}

}
