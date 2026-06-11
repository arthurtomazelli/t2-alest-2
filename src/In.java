import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class In{
    public In() {}

    public List<String[]> readCSV(String filePath, String splitChar) {
        List<String[]> data = new ArrayList<>();
        String line = "";
        String csvSplitBy = splitChar;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                String[] row = line.split(csvSplitBy);
                data.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}