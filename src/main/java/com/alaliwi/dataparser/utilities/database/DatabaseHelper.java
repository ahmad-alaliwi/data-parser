package com.alaliwi.dataparser.utilities.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/** Helper class for database operations
 *
 * @author Ahmad Alaliwi
 */
public class DatabaseHelper {

  /**
   * Returns the database connection for a specified database name. Creates it if it does not exist.
   *
   * @param databaseName name used in creation of database
   * @return database connection
   */
  public static Connection getDatabaseConnection(String databaseName) throws IOException {
    Files.deleteIfExists(Paths.get(databaseName + ".db"));

    Connection connection = null;
    try {
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:" + databaseName + ".db");
    } catch (ClassNotFoundException e) {
      System.err.println("org.sqlite.JDBC class not found");
      System.exit(0);
    } catch (SQLException e) {
      System.err.println("An error occured while getting the database connection");
      System.exit(0);
    }
    System.out.println("Database connection created successfully");
    return connection;
  }

  /**
   * Creates a table named RECORDS
   *
   * @param connection sql connection object
   */
  private static void createTable(Connection connection) {
    System.out.println("Creating table RECORDS");
    try {
      Statement stmt = connection.createStatement();
      // Using the email as the primary key to ensure uniqueness
      String sql =
          "CREATE TABLE RECORDS "
              + "(C TEXT PRIMARY KEY NOT NULL,"
              + " A TEXT, "
              + " B TEXT, "
              + " D TEXT,"
              + " E TEXT,"
              + " F TEXT,"
              + " G TEXT,"
              + " H TEXT,"
              + " I TEXT,"
              + " J TEXT)";
      stmt.executeUpdate(sql);
      stmt.close();
    } catch (SQLException e) {
      System.err.println("An error has occured while creating the table");
      System.exit(0);
    }
    System.out.println("Database table RECORDS created successfully");
  }

  /**
   * Inserts records to the RECORDS table in the database
   *
   * @param connection sql connection object
   * @param records records to insert
   */
  public static void writeSuccessfulRecordsToDatabase(
      Connection connection, List<String[]> records) {
    if (records.size() == 0) {
      return;
    }
    createTable(connection);
    System.out.println("Preparing to write records to database");
    // Sending one large INSERT statement because it is considerably faster than using separate
    // single-row INSERT statements
    String sql = "INSERT INTO RECORDS(A,B,C,D,E,F,G,H,I,J) VALUES";
    for (String[] record : records) {
      sql += "(\"" + String.join("\",\"", record) + "\"),";
    }
    String sqlToExecute = sql.substring(0, sql.length() - 1) + ";";
    try {
      Statement stmt = connection.createStatement();
      System.out.println("Writing successful records to database");
      stmt.executeUpdate(sqlToExecute);
      stmt.close();
    } catch (SQLException e) {
      System.err.println("An error occured while writing records to database");
      System.exit(0);
    }
    System.out.println("Records written to database successfully");
  }
}
