import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class CSV {
    protected  String name;
    protected String csvDelimiter = ",";
    protected List<String> Names = new ArrayList<>();
    protected List<Double> Weight = new ArrayList<>();
    protected List<Integer> Negative = new ArrayList<>();
    protected List<List<Double>> Options = new ArrayList<List<Double>>();
    protected List<Double> Aw = new ArrayList<>();
    protected List<Double> Ab = new ArrayList<>();
    protected List<Double> Dw = new ArrayList<>();
    protected List<Double> Db = new ArrayList<>();
    protected List<Double> Sw = new ArrayList<>();


    public CSV(String name){
        this.name = name;
    }
    public void readCSV() {
        try {
            //Lecture du fichier .csv
            File file = new File(this.name);
            Scanner scanner = new Scanner(file);
            int epo = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(csvDelimiter);
                //Récupération des noms des différentes options
                if(epo==0){
                    for(int i = 3; i<data.length;i++){Names.add(data[i]);epo=1;}
                }
                else {
                    //Récupération des poids, des impacts (négatifs, positifs) et des valeurs associés à chaque critères
                    List<Double> temp = new ArrayList<>();
                    Weight.add(Double.parseDouble(data[1]));
                    Negative.add(Integer.parseInt(data[2]));
                    for (int i = 0; i < data.length - 3; i++) {
                        temp.add(Double.parseDouble(data[i + 3]));
                    }
                    Options.add(temp);
                }
            }
            //Transposition de la matrice des valeurs pour un traitement plus facile par la suite
            Options = transpose(Options);
            scanner.close();
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static <T> List<List<T>> transpose(List<List<T>> table) {
        List<List<T>> ret = new ArrayList<List<T>>();
        int N = table.get(0).size();
        for (int i = 0; i < N; i++) {
            List<T> col = new ArrayList<T>();
            for (List<T> row : table) {
                col.add(row.get(i));
            }
            ret.add(col);
        }
        return ret;
    }

    public void Normalize(){
        int n = Options.get(0).size();
        int m = Options.size();
        double W = 0.0;
        for(Double wei : Weight){W += wei;}
        double r;
        for(int j=0; j<n; j++){
            r = 0.0;
            for(int i=0; i<m; i++){
                r = r + Math.pow(Options.get(i).get(j),2);
            }
            r = Math.sqrt(r);
            for(int i=0; i<m; i++){
                Options.get(i).set(j,(Options.get(i).get(j)/r)*(Weight.get(j)/W));
            }
        }
    }

    public void calcul(){
        List<List<Double>> opt = transpose(Options);
        int i = 0;
        for(List<Double> value : opt){
            if(Negative.get(i)==1) {
                Aw.add(Collections.max(value));
                Ab.add(Collections.min(value));
            }
            else{
                Aw.add(Collections.min(value));
                Ab.add(Collections.max(value));}
            i++;
        }

        for(List<Double> val : Options){
            Double diw = 0.0;
            Double dib = 0.0;
            for(int j=0; j<val.size();j++){
                diw = diw + Math.pow((val.get(j)-Aw.get(j)),2);
                dib = dib + Math.pow((val.get(j)-Ab.get(j)),2);
            }
            diw = Math.sqrt(diw);
            dib = Math.sqrt(dib);
            Sw.add(diw/(diw+dib));
            Dw.add(diw);
            Db.add(dib);
        }
    }
    public Map<String, Double>  combineSortLists() {
        TreeMap<String, Double> stringToValueMap = new TreeMap<>();

        for(int i = 0; i<Names.size();i++){
            stringToValueMap.put(Names.get(i),Sw.get(i));
        }

        // Conversion du Map en List<Map.Entry> pour trier les paires clé-valeur
        List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(stringToValueMap.entrySet());
        Collections.sort(sortedEntries, Collections.reverseOrder(Map.Entry.comparingByValue()));

        // Création d'un nouveau LinkedHashMap pour stocker les paires clé-valeur triées
        Map<String, Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : sortedEntries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
}

