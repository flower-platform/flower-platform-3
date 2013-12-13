package db;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

/**
 * DataStax Academy Sample Application
 *
 * Copyright 2013 DataStax
 *
 * This is a Singleton class that holds 1 Cassandra session that all requests will share.
 * It has 1 public method to return that session.
 *
 *
 *
 *
 */

public class CassandraData {

  //
  // A static variable that holds the session.  Only one of these will exist for the whole application
  //

  private static Session cassandraSession = null;

  /**
   * Required constructor, but it doesn't need to do anything.
   */

  CassandraData () {
    // Do nothing
  }

  /**
   *
   *  Return the Cassandra session.
   *  When the application starts up, the session
   *  is set to null.  When this function is called, it checks to see if the session is null.
   *  If so, it creates a new session, and sets the static session.
   *
   *  All of the DAO classes are subclasses of this
   *
   * @return - a valid cassandra session
   */

  public static Session getSession() {

    if (cassandraSession == null) {
      cassandraSession = createSession();
    }

    return cassandraSession;
  }

  /**
   *
   * Create a new cassandra Cluster() and Session().  Returns the Session.
   *
   * @return A new Cassandra session
   */

  protected static Session createSession() {
    Cluster cluster = Cluster.builder().addContactPoint("localhost").build();
    
    Session session = cluster.connect("test");

    session.execute("TRUNCATE cse");
    session.execute("TRUNCATE relation");
    session.execute("TRUNCATE node");
    session.execute("TRUNCATE repository");
    session.execute("TRUNCATE resource");
    
    return session;
  }
}
