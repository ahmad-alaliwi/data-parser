# CSV Data Parser

The purpose of this project is to parse a csv file and write its valid records to a SQLite database and its invalid records to a csv file in addition to a statistical report of all records.

## Getting started

To get this app installed, you must have JDK 1.8 installed and at least maven 3.5.4

Once you have JDK and maven installed, navigate to the project direcotry and run: 
```bash
mvn clean install
```

The bundled jar is then generated under the target folder in the project directory.

Once you have this jar, run 
```bash
java -jar <bundled_jar_name>.jar <input_filename>.csv
```

Example: 
```bash
java -jar target/data-parser-bundled-1.0-SNAPSHOT.jar ms3interview.csv
```

## Approach

This application uses an open source library called OpenCSV to read the input csv file.
It then iterates through each of the rows in the CSV file skipping empty rows and checking for successful and unsuccessful records.<br>
Successful records are those that have exactly 10 columns plus a non-empty email field and are added to an ArrayList object upon encounter.
Other records are considered to be unsuccessful which are then written to a csv file with the name <input_file>-bad.csv upon encounter.<br>
After iterating through all the records in the csv file, a SQLite database is created with the name <input_file>.db and a table called RECORDS is then created in the database. <br>
If the successful records ArrayList is not empty, all items in that list are inserted into the databse using one insert statement instead of multiple ones upon encountering a successful record because sending one large INSERT statement is considerably faster than using separate  single-row INSERT statements.
An assumption is that the email is unique and hence it is not allowed to be empty is because it is supposed to be used as a primary key in the database table.
At the end of the processing, a statistical report is generated and written to a file with the name <input_file>.log which indicates total number of records recieved, number of successful records and number of unsuccessful ones.