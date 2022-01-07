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
                    System.out.print("Can't find slang.txt. Please input the path again: ");
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

    public void searchBySlang(Scanner sc) {
        String choice = "Y";
        while (choice.equals("Y")) {
            System.out.print("Input a slang to search: ");
            String input = sc.nextLine().toUpperCase();
            Set<String> wordSet = this.slangList.keySet();
            ArrayList<String> wordArray = new ArrayList<String>(wordSet);
            // implement binary search
            int min = 0, max = wordArray.size() - 1, avg = 0;
            while (max - min > 1) {
                avg = Math.floorDiv(max + min, 2);
                if (wordArray.get(avg).startsWith(input))
                    break;
                else if (wordArray.get(avg).compareTo(input) > 0)
                    max = avg;
                else if (wordArray.get(avg).compareTo(input) < 0)
                    min = avg;
            }
            if (max - min == 1)
                System.out.println("Slang not found.");
            else {
                // loop to the beginning word that start with input
                while (wordArray.get(avg).startsWith(input))
                    --avg;
                ++avg;
                System.out.println("Found slangs:");
                while (wordArray.get(avg).startsWith(input)) {
                    System.out.println(wordArray.get(avg));
                    ++avg;
                }
                System.out.print("Input a slang from above list to show its definition: ");
                input = sc.nextLine();
                while (slangList.get(input.toUpperCase()) == null) {
                    System.out.print("Wrong input.\nPlease type in again: ");
                    input = sc.nextLine();
                }
                System.out.println("Definition: " + slangList.get(input.toUpperCase()));
            }
            System.out.print("Do you want to search more? (Y/N): ");
            choice = sc.nextLine().toUpperCase();
        }
    }
}
