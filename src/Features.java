import java.io.*;
import java.util.*;

public class Features {
    private TreeMap<String, String> slangList;

    @SuppressWarnings("unchecked")
    public Features(Scanner sc) {
        this.slangList = new TreeMap<String, String>();
        try {
            ObjectInputStream ooi = new ObjectInputStream(new FileInputStream("indexed-slang.txt"));
            this.slangList = (TreeMap<String, String>) ooi.readObject();
            ooi.close();
        } catch (FileNotFoundException ex) {
            String path = "slang.txt";
            System.out.println(
                    "Input path for slang.txt (if left blank please press Enter and the app'll use the same path with the app): ");
            while (true) {
                String temp = sc.nextLine();
                if (temp != "")
                    path = temp;
                try {
                    FileInputStream fis = new FileInputStream(path);
                    fis.close();
                    break;
                } catch (FileNotFoundException e) {
                    System.out.print("Can't find slang.txt. Please type in the path: ");
                } catch (IOException e) {
                }
            }
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
                String line = br.readLine();
                String word;
                String meaning;
                while (line != null) {
                    word = line.substring(0, line.indexOf('`'));
                    meaning = line.substring(line.indexOf('`') + 1, line.length() - 1);
                    this.slangList.put(word, meaning);
                    line = br.readLine();
                }
                br.close();
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("indexed-slang.txt"));
                oos.writeObject(this.slangList);
                oos.close();
            } catch (FileNotFoundException e) {
                System.out.println("File not found.");
            } catch (IOException e) {
                System.out.println("Error while reading file.");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("There\'s some error while reading indexed file");
        }
    }
}
