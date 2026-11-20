package mydrinkshop.tests;

import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class CsvLoginReader {

    public static List<LoginData> readLoginData(String resourcePath) {
        try {
            InputStream inputStream = CsvLoginReader.class.getClassLoader().getResourceAsStream(resourcePath);
            if (inputStream == null) {
                throw new RuntimeException("Nu s-a gasit fisierul: " + resourcePath);
            }

            try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
                List<String[]> rows = reader.readAll();

                return rows.stream()
                        .skip(1)
                        .map(row -> new LoginData(
                                row.length > 0 ? row[0] : "",
                                row.length > 1 ? row[1] : ""
                        ))
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new RuntimeException("Eroare la citirea CSV: " + resourcePath, e);
        }
    }
}
