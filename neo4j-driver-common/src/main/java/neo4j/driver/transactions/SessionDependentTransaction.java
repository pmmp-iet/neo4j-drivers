package neo4j.driver.transactions;

import java.util.Collections;
import java.util.Map;

import org.neo4j.driver.internal.types.InternalTypeSystem;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Statement;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.types.TypeSystem;

public abstract class SessionDependentTransaction<T extends AutoCloseable> implements org.neo4j.driver.v1.Transaction {

	protected final Session session;
	protected final T internalTransaction;

	public SessionDependentTransaction(Session session, T internalTransaction) {
		this.session = session;
		this.internalTransaction = internalTransaction;
	}

	@Override
	public StatementResult run(String statementTemplate, Map<String, Object> statementParameters) {
		return session.run(statementTemplate, statementParameters);
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
		return InternalTypeSystem.TYPE_SYSTEM;
	}

}
