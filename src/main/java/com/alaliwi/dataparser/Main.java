package com.alaliwi.dataparser;

import static com.alaliwi.dataparser.utilities.database.DatabaseHelper.writeSuccessfulRecordsToDatabase;
import static com.alaliwi.dataparser.utilities.operations.OperationsHelper.getCsvReader;
import static com.alaliwi.dataparser.utilities.operations.OperationsHelper.isBadRecord;
import static com.alaliwi.dataparser.utilities.operations.OperationsHelper.writeStatisticalReport;

import com.alaliwi.dataparser.utilities.database.DatabaseHelper;
import com.opencsv.CSVReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Main tool that runs the program */
public class Main {

  public static void main(String args[]) {

    if (args.length != 1) {
      System.err.println("Invalid number of arguments. Expected 1: csv input file name.");
      System.exit(0);
    }

    int badRecords = 0;
    List<String[]> goodRecordsList = new ArrayList<>();
    String fileNameWithoutExtension = args[0].substring(0, args[0].length() - 4);

    try (FileWriter fileWriter = new FileWriter(fileNameWithoutExtension + "-bad.csv")) {
      CSVReader csvReader = getCsvReader(args[0]);

      String[] record;
      csvReader.readNext();
      while ((record = csvReader.readNext()) != null) {
        // Skip empty lines
        if (record.length == 1 && record[0].isEmpty()) {
          continue;
        }
        if (isBadRecord(record)) {
          badRecords++;
          fileWriter.write(String.join(",", record) + "\n");
        } else {
          goodRecordsList.add(record);
        }
      }
      try (Connection connection = DatabaseHelper.getDatabaseConnection(fileNameWithoutExtension)) {
        writeSuccessfulRecordsToDatabase(connection, goodRecordsList);
      }
    } catch (SQLException e) {
      System.err.println("An error occured while closing the connection to the database");
      System.exit(0);
    } catch (IOException e) {
      System.out.println("An error occured while reading the records");
      System.exit(0);
    }

    writeStatisticalReport(fileNameWithoutExtension, goodRecordsList.size(), badRecords);

    System.exit(0);
  }
}
