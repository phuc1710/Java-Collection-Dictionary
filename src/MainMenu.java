import java.util.Scanner;

public class MainMenu {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        Features ft = new Features(sc);
        while (true) {
            System.out.println("Slang Dictionary"
                    + "\n----------------"
                    + "\n1. Search by slang word"
                    + "\n2. Search by definition"
                    + "\n3. Show slang search history"
                    + "\n4. Add a slang word"
                    + "\n5. Edit a slang word"
                    + "\n6. Delete a slang word"
                    + "\n7. Reset default slang words list"
                    + "\n8. On this day slang word (random)"
                    + "\n9. Quiz (random slang)"
                    + "\n10. Quiz (random definition)"
                    + "\n11. Quit"
                    + "\n----------------");
            System.out.print("Choose your option by type in the number: ");
            String option = sc.nextLine();
            try {
                Integer optionInteger = Integer.parseInt(option);
                switch (optionInteger) {
                    case 1:
                        ft.searchBySlang(sc);
                        break;
                    case 2:
                        // TODO: implement search by definition
                        break;
                    case 3:
                        // TODO: implement show slang search by history
                        break;
                    case 4:
                        // TODO: implement add a slang word
                        break;
                    case 5:
                        // TODO: implement edit a slang word
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
                        System.out.println("See you next time!");
                        return;
                    default:
                        System.out.print("Input must be from 1 to 10.\nPlease type in again: ");
                        continue;
                }
            } catch (Exception e) {
                System.out.print(e);
                System.out.print("Wrong input.\nPlease type in again: ");
            }
        }
    }
}
