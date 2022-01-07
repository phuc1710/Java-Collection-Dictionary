import java.io.*;
import java.util.*;
import java.time.*;
import java.time.temporal.ChronoField;

public class Features {
    private TreeMap<String, String> slangList;
    private ArrayList<String> history;
    private int historyCount = 20;

    // !Referenced code
    @SuppressWarnings("unchecked")

    public Features(Scanner sc) {
        // load history
        try {
            ObjectInputStream ooi = new ObjectInputStream(new FileInputStream("history.txt"));
            this.history = (ArrayList<String>) ooi.readObject();
            ooi.close();
        } catch (Exception e) {
            this.history = new ArrayList<String>();
        }
        // load index
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
                this.slangList = new TreeMap<String, String>();
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
                int i = 1;
                int startList = avg;
                while (wordArray.get(avg).startsWith(input)) {
                    System.out.println(i + ". " + wordArray.get(avg));
                    ++avg;
                    ++i;
                }
                System.out.print("------------------------------------------------------"
                        + "\nInput a number from above list to show its definition: ");
                input = sc.nextLine();
                while (this.slangList.get(wordArray.get(startList + Integer.parseInt(input))) == null) {
                    System.out.print("Wrong input.\nPlease type in again: ");
                    input = sc.nextLine();
                }
                System.out.println("Slang: " + wordArray.get(startList + Integer.parseInt(input) - 1)
                        + "\nDefinition: "
                        + this.slangList.get(wordArray.get(startList + Integer.parseInt(input) - 1)));
                this.history.add(wordArray.get(startList + Integer.parseInt(input) - 1));
                // limit the history to most recent searches
                if (this.history.size() > this.historyCount)
                    this.history.remove(0);
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
            ArrayList<String> found = new ArrayList<String>();
            Set<String> wordSet = this.slangList.keySet();
            ArrayList<String> wordArray = new ArrayList<String>(wordSet);
            for (int i = 0; i < this.slangList.size(); ++i)
                if (this.slangList.get(wordArray.get(i)).toLowerCase().contains(input))
                    found.add(wordArray.get(i));
            if (found.size() == 0)
                System.out.println("------------------"
                        + "\nDefinition not found");
            else {
                System.out.println("--------------------------------------------------"
                        + "\nFound slangs which has above keyword in definition:");
                for (int i = 0; i < found.size(); ++i)
                    System.out.println((i + 1) + ". " + found.get(i));
                System.out.print("--------------------------------------------------"
                        + "\nInput a number from above list to show its definition: ");
                input = sc.nextLine();
                while (this.slangList.get(found.get(Integer.parseInt(input) - 1)) == null) {
                    System.out.print("Wrong input.\nPlease type in again: ");
                    input = sc.nextLine();
                }
                System.out.println("Slang: " + found.get(Integer.parseInt(input) - 1)
                        + "\nDefinition: " + this.slangList.get(found.get(Integer.parseInt(input) - 1)));
                this.history.add(found.get(Integer.parseInt(input) - 1));
                // limit the history to most recent searches
                if (this.history.size() > this.historyCount)
                    this.history.remove(0);
            }
            System.out.print("-----------------------------------"
                    + "\nDo you want to search more? (Y/N): ");
            choice = sc.nextLine().toUpperCase();
        }
    }

    public void showSearchHistory(Scanner sc) {
        String choice = "Y";
        while (choice.equals("Y")) {
            if (this.history.size() == 0)
                System.out.println("------------------"
                        + "\nNo search history");
            else {
                System.out.println("--------------------------------------------------"
                        + "\n" + historyCount + " most recent slang searches:");
                for (int i = 0; i < this.history.size(); ++i)
                    System.out.println((i + 1) + ". " + this.history.get(i));
                System.out.print("--------------------------------------------------"
                        + "\nInput a number from above list to show its definition: ");
                String input = sc.nextLine();
                while (this.slangList.get(this.history.get(Integer.parseInt(input) - 1)) == null) {
                    System.out.print("Wrong input.\nPlease type in again: ");
                    input = sc.nextLine();
                }
                System.out.println("Slang: " + this.history.get(Integer.parseInt(input) - 1)
                        + "\nDefinition: " + this.slangList.get(this.history.get(Integer.parseInt(input) - 1)));
            }
            System.out.print("-----------------------------------"
                    + "\nDo you want to show definition again? (Y/N): ");
            choice = sc.nextLine().toUpperCase();
        }
    }

    public void addSlang(Scanner sc) {
        String choice = "Y";
        String definition;
        while (choice.equals("Y")) {
            System.out.print("-------------------------"
                    + "\nInput a slang you want to add: ");
            String input = sc.nextLine().toUpperCase();
            if (this.slangList.get(input) != null) {
                System.out.println("----------------------------------------"
                        + "\nSlang found in list. Here's your option:"
                        + "\n1. Overwrite existing slang"
                        + "\n2. Duplicate to a new slang with new definition"
                        + "\n----------------------------------------");
                System.out.print("Choose your option by type in the number: ");
                String option = sc.nextLine();
                System.out.print("Input a definition for your slang: ");
                definition = sc.nextLine();
                try {
                    Integer optionInteger = Integer.parseInt(option);
                    switch (optionInteger) {
                        case 1:
                            this.slangList.put(input, definition);
                            System.out.println("Overwriten successfully!");
                            break;
                        case 2:
                            String dupInput = input;
                            while (this.slangList.get(dupInput) != null)
                                dupInput += " ";
                            this.slangList.put(dupInput, definition);
                            System.out.println("Duplicated successfully!");
                            break;
                        default:
                            System.out.print("Input must be from 1 to 2.\nPlease add again!");
                            continue;
                    }
                } catch (Exception e) {
                    System.out.print("Wrong input.\nPlease type in again: ");
                }
            } else {
                System.out.print("Input a definition for your slang: ");
                definition = sc.nextLine();
                this.slangList.put(input, definition);
                System.out.println("Added successfully!");
            }
            System.out.print("-----------------------------------"
                    + "\nDo you want to add more? (Y/N): ");
            choice = sc.nextLine().toUpperCase();
        }
    }

    public void editSlang(Scanner sc) {
        String choice = "Y";
        while (choice.equals("Y")) {
            System.out.print("-------------------------"
                    + "\nInput a slang to edit: ");
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
                int i = 1;
                int startList = avg;
                while (wordArray.get(avg).startsWith(input)) {
                    System.out.println(i + ". " + wordArray.get(avg));
                    ++avg;
                    ++i;
                }
                System.out.print("------------------------------------------------------"
                        + "\nInput a number from above list to show its definition and edit: ");
                input = sc.nextLine();
                while (this.slangList.get(wordArray.get(startList + Integer.parseInt(input))) == null) {
                    System.out.print("Wrong input.\nPlease type in again: ");
                    input = sc.nextLine();
                }
                System.out.println("Slang: " + wordArray.get(startList + Integer.parseInt(input) - 1)
                        + "\nDefinition: "
                        + this.slangList.get(wordArray.get(startList + Integer.parseInt(input) - 1)));
                System.out.println("------------------------------"
                        + "\nHere's your option:"
                        + "\n1. Edit slang"
                        + "\n2. Edit slang's definition"
                        + "\n------------------------------");
                System.out.print("Choose your option by type in the number: ");
                String option = sc.nextLine();
                try {
                    Integer optionInteger = Integer.parseInt(option);
                    switch (optionInteger) {
                        case 1:
                            System.out.print("Input new slang for the selected slang: ");
                            String newSlang = sc.nextLine();
                            String oldDef = this.slangList.get(wordArray.get(startList + Integer.parseInt(input) - 1));
                            this.slangList.remove(wordArray.get(startList + Integer.parseInt(input) - 1));
                            this.slangList.put(newSlang, oldDef);
                            System.out.println("Edited successfully!");
                            break;
                        case 2:
                            System.out.print("Input new definition for the selected slang: ");
                            String newDef = sc.nextLine();
                            this.slangList.replace(wordArray.get(startList + Integer.parseInt(input) - 1), newDef);
                            System.out.println("Edited successfully!");
                            break;
                        default:
                            System.out.print("Input must be from 1 to 2.\nPlease add again!");
                            continue;
                    }
                } catch (Exception e) {
                    System.out.print("Wrong input.\nPlease type in again: ");
                }
            }
            System.out.print("-----------------------------------"
                    + "\nDo you want to edit more? (Y/N): ");
            choice = sc.nextLine().toUpperCase();
        }
    }

    public void deleteSlang(Scanner sc) {
        String choice = "Y";
        while (choice.equals("Y")) {
            System.out.print("-------------------------"
                    + "\nInput a slang to delete: ");
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
                int i = 1;
                int startList = avg;
                while (wordArray.get(avg).startsWith(input)) {
                    System.out.println(i + ". " + wordArray.get(avg));
                    ++avg;
                    ++i;
                }
                System.out.print("------------------------------------------------------"
                        + "\nInput a number from above list to show its definition and delete: ");
                input = sc.nextLine();
                while (this.slangList.get(wordArray.get(startList + Integer.parseInt(input))) == null) {
                    System.out.print("Wrong input.\nPlease type in again: ");
                    input = sc.nextLine();
                }
                System.out.println("Slang: " + wordArray.get(startList + Integer.parseInt(input) - 1)
                        + "\nDefinition: "
                        + this.slangList.get(wordArray.get(startList + Integer.parseInt(input) - 1)));
                System.out.println("--------------------------------------------------"
                        + "\nDo you really sure you want to DELETE this slang? (Y/N)"
                        + "\n--------------------------------------------------");
                System.out.print("Choose your option by type in Y or N: ");
                String option = sc.nextLine().toUpperCase();
                if (option.equals("Y"))
                    this.slangList.remove(wordArray.get(startList + Integer.parseInt(input) - 1));
            }
            System.out.print("-----------------------------------"
                    + "\nDo you want to delete more? (Y/N): ");
            choice = sc.nextLine().toUpperCase();
        }
    }

    public void resetSlangList(Scanner sc) {
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
            this.slangList = new TreeMap<String, String>();
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
            System.out.println("Slang list resetted successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (IOException e) {
            System.out.println("Error while reading file.");
        }
    }

    public void randomSlangWordByDay() {
        // !Referenced code
        Clock clock = Clock.systemDefaultZone();
        LocalDate localDate = LocalDate.now(clock);

        Random generator = new Random(localDate.getLong(ChronoField.EPOCH_DAY));
        int slangIndex = generator.nextInt(this.slangList.size() - 1);
        Set<String> wordSet = this.slangList.keySet();
        ArrayList<String> wordArray = new ArrayList<String>(wordSet);
        System.out.println("------------------------------"
                + "\nSlang of the day: " + wordArray.get(slangIndex)
                + "\nDefinition: "
                + this.slangList.get(wordArray.get(slangIndex))
                + "\n------------------------------");
    }

    public void quizRandomSlang(Scanner sc) {
        String choice = "Y";
        while (choice.equals("Y")) {
            Random generator = new Random();
            int slangIndex = generator.nextInt(this.slangList.size() - 1);
            Set<String> wordSet = this.slangList.keySet();
            ArrayList<String> wordArray = new ArrayList<String>(wordSet);
            ArrayList<String> answer = new ArrayList<String>();
            answer.add(this.slangList.get(wordArray.get(slangIndex)));
            answer.add(this.slangList.get(wordArray.get(generator.nextInt(this.slangList.size() - 1))));
            answer.add(this.slangList.get(wordArray.get(generator.nextInt(this.slangList.size() - 1))));
            answer.add(this.slangList.get(wordArray.get(generator.nextInt(this.slangList.size() - 1))));
            // !Referenced code
            Collections.shuffle(answer);

            System.out.println("-------------------------------"
                    + "\n|         SLANG QUIZ          |"
                    + "\n-------------------------------"
                    + "\nChoose the definition for this slang: " + wordArray.get(slangIndex)
                    + "\n------------------------------"
                    + "\n1. " + answer.get(0)
                    + "\n2. " + answer.get(1)
                    + "\n3. " + answer.get(2)
                    + "\n4. " + answer.get(3)
                    + "\n------------------------------");
            System.out.print("Choose your answer by type in the number: ");
            String option;
            while (true) {
                option = sc.nextLine();
                try {
                    if (answer.get(Integer.parseInt(option) - 1)
                            .equals(this.slangList.get(wordArray.get(slangIndex)))) {
                        System.out.println("Correct answer! Congratulations!");
                        break;
                    } else {
                        System.out.println(
                                "Wrong answer! The correct answer is: "
                                        + this.slangList.get(wordArray.get(slangIndex)));
                        break;
                    }
                } catch (Exception e) {
                    System.out.print("Wrong input.\nPlease type in again: ");
                }
            }
            System.out.print("-----------------------------------"
                    + "\nDo you want to play more? (Y/N): ");
            choice = sc.nextLine().toUpperCase();
        }
    }

    public void writeSearchHistory() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("history.txt"));
            oos.writeObject(this.history);
            oos.close();
        } catch (Exception e) {
            System.out.println("There\'s some error when writting history");
        }
    }

    public void writeSlangList() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("indexed-slang.txt"));
            oos.writeObject(this.slangList);
            oos.close();
        } catch (Exception e) {
            System.out.println("There\'s some error when writting index");
        }
    }
}
