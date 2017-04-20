package neo4j.driver.testkit;

import org.neo4j.driver.v1.Session;
import org.neo4j.graphdb.Transaction;

import neo4j.driver.transactions.SessionDependentTransaction;

public class EmbeddedTestkitTransaction extends SessionDependentTransaction<org.neo4j.graphdb.Transaction> {

	public EmbeddedTestkitTransaction(Session session, Transaction internalTransaction) {
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
		return true;
	}

}
