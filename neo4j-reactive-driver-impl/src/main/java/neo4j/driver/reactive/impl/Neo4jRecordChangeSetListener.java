package neo4j.driver.reactive.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import neo4j.driver.reactive.data.RecordChangeSet;
import neo4j.driver.reactive.interfaces.RecordChangeSetListener;

public class Neo4jRecordChangeSetListener implements RecordChangeSetListener {
	private static final Logger logger = Logger.getLogger(Neo4jRecordChangeSetListener.class.getName());
	protected String queryName;

	public Neo4jRecordChangeSetListener(String queryName) {
		this.queryName = queryName;
	}

	@Override
	public void notify(RecordChangeSet rcs) {
		logger.log(Level.ALL, "A new changeSet appeared for listener '%s':",queryName);
		String  recordCangeSetString = rcs.toString();
		logger.log(Level.ALL, recordCangeSetString);
	}

}
