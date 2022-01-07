import java.util.Scanner;

public class MainMenu {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        Features ft = new Features(sc);
        while (true) {
            System.out.println("\n/------------------------------------\\"
                    + "\n|          Slang Dictionary          |"
                    + "\n\\------------------------------------/"
                    + "\n| 1. Search by slang word            |"
                    + "\n| 2. Search by definition            |"
                    + "\n| 3. Show slang search history       |"
                    + "\n| 4. Add a slang word                |"
                    + "\n| 5. Edit a slang word               |"
                    + "\n| 6. Delete a slang word             |"
                    + "\n| 7. Reset default slang words list  |"
                    + "\n| 8. On this day slang word (random) |"
                    + "\n| 9. Quiz (random slang)             |"
                    + "\n| 10. Quiz (random definition)       |"      
                    + "\n| 11. Quit                           |"
                    + "\n/------------------------------------\\");
            System.out.print("Choose your option by type in the number: ");
            String option = sc.nextLine();
            try {
                Integer optionInteger = Integer.parseInt(option);
                switch (optionInteger) {
                    case 1:
                        ft.searchBySlang(sc);
                        break;
                    case 2:
                        ft.searchByDefinition(sc);
                        break;
                    case 3:
                        ft.showSearchHistory(sc);
                        break;
                    case 4:
                        ft.addSlang(sc);
                        break;
                    case 5:
                        ft.editSlang(sc);
                        break;
                    case 6:
                        // TODO: implement delete a slang word
                        break;
                    case 7:
                        // TODO: implement reset default slang words list
                        break;
                    case 8:
                        // TODO: implement on this day slang word (random)
                        break;
                    case 9:
                        // TODO: implement quiz (random slang)
                        break;
                    case 10:
                        // TODO: implement quiz (random definition)
                        break;
                    case 11:
                        sc.close();
                        ft.writeSearchHistory();
                        ft.writeSlangList();
                        System.out.println("See you next time!");
                        return;
                    default:
                        System.out.print("Input must be from 1 to 10.\nPlease type in again: ");
                        continue;
                }
            } catch (Exception e) {
                System.out.print("Wrong input.\nPlease type in again: ");
            }
        }
    }
}
