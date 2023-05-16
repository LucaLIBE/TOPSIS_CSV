import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Entrez le nom du fichier avec l'extensions : ");
        Scanner in = new Scanner(System.in);
        String userChoice = in.nextLine();
        CSV csv = new CSV(userChoice);

        csv.readCSV();
        csv.Normalize();
        csv.calcul();
        Map<String, Double> sortedMap = csv.combineSortLists();

        // Affichage du Map trié
        for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
            System.out.println("Clé: " + entry.getKey() + ", Valeur: " + entry.getValue());
        }
    }
}