package data.io.txt;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportToTXT {

    private ExportToTXT() {}

    public static void export(String path, String lineSeparator, String data, boolean preserve) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, preserve))) {
            String[] lines = data.split(lineSeparator);
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        }
    } 

    public static void export(String path, String data, boolean preserve) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, preserve))) {
            bw.write(data);
            bw.newLine(); 
        }
    } 

    public static void export(String path, List<String> data, boolean preserve) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, preserve))) {
            for (String string : data) {
                bw.write(string);
                bw.newLine(); 
            }
        }
    }
}
