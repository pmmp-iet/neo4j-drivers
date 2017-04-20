package neo4j.driver.reactive.impl;

import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;

import neo4j.driver.transactions.SessionDependentTransaction;

public class Neo4jReactiveTransaction extends SessionDependentTransaction<org.neo4j.driver.v1.Transaction> {

	public Neo4jReactiveTransaction(Session session, Transaction internalTransaction) {
		super(session, internalTransaction);
	}

	@Override
	public void success() {
		internalTransaction.success();
	}

	@Override
	public void failure() {
		internalTransaction.failure();
	}

	@Override
	public void close() {
		internalTransaction.close();
	}

	@Override
	public boolean isOpen() {
		return internalTransaction.isOpen();
	}

}
