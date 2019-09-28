package com.alaliwi.dataparser.utilities.operations;

import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/** Helper class for utility operations */
public class OperationsHelper {

  /**
   * Returns a {@link CSVReader} object that containts the records of the csv file to read from
   *
   * @param csvFile name of csv file to read
   * @return {@link CSVReader} object with the contents of the csv file
   */
  public static CSVReader getCsvReader(String csvFile) {
    System.out.println("Reading " + csvFile);
    try {
      return new CSVReader(new FileReader(csvFile));
    } catch (FileNotFoundException e) {
      System.err.println(String.format("Specified csv file [%s] does not exist", csvFile));
      System.exit(0);
    }
    return null;
  }

  /**
   * Returns true if the record has an empty email value in it or if the column count is not 10.
   * Returns false otherwise.
   *
   * @param record record to check
   * @return true if the record has an empty column in it. Returns false otherwise.
   */
  public static boolean isBadRecord(String[] record) {
    if (record.length != 10 || record[2].isEmpty()) {
      return true;
    }
    return false;
  }

  /**
   * Writes a statistical report to a file consisting of total number of records recieved, number of
   * records successful and number of records that failed
   *
   * @param fileNameWithoutExtension file name to write to
   * @param goodRecordsCount number of good records
   * @param badRecordsCount number of bad records
   */
  public static void writeStatisticalReport(
      String fileNameWithoutExtension, int goodRecordsCount, int badRecordsCount) {
    String statisticalReportFileName = fileNameWithoutExtension + ".log";
    try (FileWriter fileWriter = new FileWriter(statisticalReportFileName)) {
      fileWriter.write("Number of records recieved: " + (goodRecordsCount + badRecordsCount));
      fileWriter.write("\nNumber of records successful: " + goodRecordsCount);
      fileWriter.write("\nNumber of records failed: " + badRecordsCount);
    } catch (IOException e) {
      System.err.println(
          "An error occured while writing statistical report to " + statisticalReportFileName);
      System.exit(0);
    }
    System.out.println("Statistical report generated successfully");
  }
}
