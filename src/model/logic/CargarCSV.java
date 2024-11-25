package model.logic;

import java.io.FileReader;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;


public class CargarCSV {

    public Iterable<CSVRecord> cargarCSV(String filePath) throws Exception {
        Reader in = new FileReader(filePath);
		return CSVFormat.RFC4180.withHeader().parse(in);
    }

}
