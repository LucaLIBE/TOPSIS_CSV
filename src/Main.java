import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<List<Double>> R = new ArrayList<List<Double>>();
        CSV csv = new CSV("options.csv");
        csv.readCSV();
        System.out.println((csv.Options));
        R = csv.Normalize();
        System.out.println(R);

        csv.calcul();
        System.out.println((csv.Negative));
        System.out.println((csv.Ab));
        System.out.println((csv.Aw));
        System.out.println((csv.Db));
        System.out.println((csv.Dw));
        System.out.println((csv.Sw));

    }
}