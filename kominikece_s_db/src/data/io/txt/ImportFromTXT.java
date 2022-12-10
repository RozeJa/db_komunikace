package data.io.txt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImportFromTXT {

    public static final String defSeparator = "=";

    private ImportFromTXT() {}

    public static List<String> importTXT(String path) throws IOException {        
        List<String> loadedFile = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            String s;
            while ((s = br.readLine()) != null) {
                loadedFile.add(s);
            }
        }
        return loadedFile;
    }

    public static List<String> parse(String row, String separator) {
        return Arrays.asList(row.split(separator));
    }

    public static List<String> parse(String row) {
        return parse(row, defSeparator);
    }
}
