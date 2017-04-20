package neo4j.driver.reactive.impl;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Statement;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.types.TypeSystem;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;

import neo4j.driver.reactive.data.RecordChangeSet;
import neo4j.driver.reactive.interfaces.ReactiveSession;
import neo4j.driver.reactive.interfaces.RecordChangeSetListener;

public class Neo4jReactiveSession implements ReactiveSession {

	final Session session;

	final Map<String, String> querySpecifications = Maps.newHashMap();
	final Map<String, Multiset<Record>> queryResults = Maps.newHashMap();
	final Map<String, RecordChangeSetListener> listeners = Maps.newHashMap();

	public Neo4jReactiveSession(Session session) {
		super();
		this.session = session;
	}

	@Override
	public RecordChangeSetListener registerQuery(String queryName, String querySpecification) {
		if (querySpecifications.containsKey(queryName)) {
			throw new IllegalStateException("Query " + queryName + " is already registered.");
		}

		querySpecifications.put(queryName, querySpecification);
		queryResults.put(queryName, HashMultiset.create());
		final Neo4jRecordChangeSetListener listener = new Neo4jRecordChangeSetListener(queryName);
		listeners.put(queryName, listener);

		return listener;
	}

	@Override
	public RecordChangeSet getDeltas(String queryName) {
		final String querySpecification = querySpecifications.get(queryName);

		final Multiset<Record> currentResults = queryResults.get(queryName);
		final Multiset<Record> newResults = HashMultiset.create();

		final StatementResult statementResult = session.run(querySpecification);
		while (statementResult.hasNext()) {
			final Record record = statementResult.next();
			newResults.add(record);
		}

		final Multiset<Record> positiveChanges = Multisets.difference(newResults, currentResults);
		final Multiset<Record> negativeChanges = Multisets.difference(currentResults, newResults);

		queryResults.put(queryName, newResults);

		return new RecordChangeSet(positiveChanges, negativeChanges);
	}

	@Override
	public boolean isOpen() {
		return session.isOpen();
	}

	@Override
	public StatementResult run(String statementTemplate, Map<String, Object> statementParameters) {
		final StatementResult result = session.run(statementTemplate, statementParameters);

		for (final Entry<String, RecordChangeSetListener> entry : listeners.entrySet()) {
			final String queryName = entry.getKey();
			final RecordChangeSetListener listener = entry.getValue();

			final RecordChangeSet rcs = getDeltas(queryName);
			listener.notify(rcs);
		}

		return result;
	}

	@Override
	public StatementResult run(String statementTemplate, Value parameters) {
		return run(statementTemplate, parameters.asMap());
	}

	@Override
	public StatementResult run(String statementTemplate, Record statementParameters) {
		return run(statementTemplate, statementParameters.asMap());
	}

	@Override
	public StatementResult run(String statementTemplate) {
		return run(statementTemplate, Collections.emptyMap());
	}

	@Override
	public StatementResult run(Statement statement) {
		return run(statement.text());
	}

	@Override
	public TypeSystem typeSystem() {
		return session.typeSystem();
	}

	@Override
	public Transaction beginTransaction() {
		final Transaction transaction = session.beginTransaction();
		return new Neo4jReactiveTransaction(this, transaction);
	}
	/**
	 * @deprecated
	 */
	@Deprecated @Override
	public Transaction beginTransaction(String bookmark) {
		final Transaction transaction = session.beginTransaction(bookmark);
		return new Neo4jReactiveTransaction(this, transaction);
	}

	@Override
	public String lastBookmark() {
		return session.lastBookmark();
	}
	/**
	 * @deprecated
	 */
	@Deprecated @Override
	public void reset() {
		session.reset();
	}

	@Override
	public void close() {
		session.close();
	}

	@Override
	public <T> T readTransaction(TransactionWork<T> work) {
		throw new UnsupportedOperationException("readTransaction method not supported.");
	}

	@Override
	public <T> T writeTransaction(TransactionWork<T> work) {
		throw new UnsupportedOperationException("writeTransaction method not supported.");
	}

}
