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
                    meaning = line.substring(line.indexOf('`') + 1, line.length());
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
            System.out.print("-------------------------"
                + "\nInput a slang to search: ");
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
                System.out.println("----------------"
                    + "\nSlang not found.");
            else {
                // loop to the beginning word that start with input
                while (wordArray.get(avg).startsWith(input))
                    --avg;
                ++avg;
                System.out.println("---------------"
                    + "\nFound slangs:");
                while (wordArray.get(avg).startsWith(input)) {
                    System.out.println(wordArray.get(avg));
                    ++avg;
                }
                System.out.print("------------------------------------------------------"
                    + "\nInput a slang from above list to show its definition: ");
                input = sc.nextLine();
                while (slangList.get(input.toUpperCase()) == null) {
                    System.out.print("Wrong input.\nPlease type in again: ");
                    input = sc.nextLine();
                }
                System.out.println("Definition: " + this.slangList.get(input.toUpperCase()));
            }
            System.out.print("-----------------------------------"
                    + "\nDo you want to search more? (Y/N): ");
            choice = sc.nextLine().toUpperCase();
        }
    }

    public void searchByDefinition(Scanner sc) {
        String choice = "Y";
        while (choice.equals("Y")) {
            System.out.print("-------------------------"
                + "\nInput a definition to search: ");
            String input = sc.nextLine().toLowerCase();
            TreeMap<String, String> found = new TreeMap<String, String>();
            Set<String> wordSet = this.slangList.keySet();
            ArrayList<String> wordArray = new ArrayList<String>(wordSet);
            for (int i = 0; i < this.slangList.size(); ++i)
                if (this.slangList.get(wordArray.get(i)).toLowerCase().startsWith(input))
                    found.put(this.slangList.get(wordArray.get(i)), wordArray.get(i));
            if (found.size() == 0)
                System.out.println("------------------"
                    + "\nDefinition not found");
            else {
                System.out.println("---------------"
                    + "\nFound definition:");
                Set<String> defSet = found.keySet();
                ArrayList<String> defArray = new ArrayList<String>(defSet);
                for (int i = 0; i < found.size(); ++i)
                    System.out.println((i + 1) + ". " + defArray.get(i));
                System.out.print("--------------------------------------------------"
                    + "\nInput a number from above list to show its slang: ");
                input = sc.nextLine();
                while (defArray.get(Integer.parseInt(input) - 1) == null) {
                    System.out.print("Wrong input.\nPlease type in again: ");
                    input = sc.nextLine();
                }
                System.out.println("Slang: " + found.get(defArray.get(Integer.parseInt(input) - 1)));
            }
            System.out.print("-----------------------------------"
                    + "\nDo you want to search more? (Y/N): ");
            choice = sc.nextLine().toUpperCase();
        }
    }
}
