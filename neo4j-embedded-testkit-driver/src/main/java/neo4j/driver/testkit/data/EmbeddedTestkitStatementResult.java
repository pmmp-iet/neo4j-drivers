package neo4j.driver.testkit.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.exceptions.NoSuchRecordException;
import org.neo4j.driver.v1.summary.ResultSummary;
import org.neo4j.driver.v1.util.Function;
import org.neo4j.graphdb.Result;

public class EmbeddedTestkitStatementResult implements StatementResult {

	final Result result;

	public EmbeddedTestkitStatementResult(final Result result) {
		this.result = result;
	}

	@Override
	public List<String> keys() {
		return result.columns();
	}

	@Override
	public boolean hasNext() {
		return result.hasNext();
	}

	@Override
	public Record next() {
		final Map<String, Object> element = result.next();
		return EmbeddedTestkitRecordFactory.create(element);
	}

	@Override
	public Record single(){
		if (result.hasNext()) {
			return next();
		} else {
			throw new NoSuchRecordException("Result is empty");
		}
	}

	@Override
	public Record peek() {
		throw new UnsupportedOperationException("Peek not supported");
	}

	@Override
	public List<Record> list() {
		List<Record> recordList = new ArrayList<>();
		while(this.hasNext()){
			recordList.add(this.next());
		}
		return recordList;
	}

	@Override
	public <T> List<T> list(Function<Record, T> mapFunction) {
		return Collections.emptyList();
	}

	@Override
	public ResultSummary consume() {
		return null;
	}

	@Override
	public ResultSummary summary() {
		return null;
	}

}
