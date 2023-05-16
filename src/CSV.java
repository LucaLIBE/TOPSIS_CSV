import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class CSV {
    protected  String name;
    protected String csvDelimiter = ",";
    protected List<String> Criteria = new ArrayList<>();
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
            File file = new File(this.name);
            Scanner scanner = new Scanner(file);
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(csvDelimiter);
                List<Double> temp = new ArrayList<>();
                    Criteria.add(data[0]);
                    Weight.add(Double.parseDouble(data[1]));
                    Negative.add(Integer.parseInt(data[2]));
                for(int i = 0; i< data.length-3;i++){
                    temp.add(Double.parseDouble(data[i+3]));
                }
                Options.add(temp);
            }
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

    public List<List<Double>> Normalize(){
        List<List<Double>> ret = Options;
        int n = Options.get(0).size();
        int m = Options.size();
        double W = 0.0;
        for(Double wei : Weight){W += wei;}
        double R;
        for(int j=0; j<n; j++){
            R = 0.0;
            for(int i=0; i<m; i++){
                R = R + Math.pow(Options.get(i).get(j),2);
            }
            R = Math.sqrt(R);
            for(int i=0; i<m; i++){
                ret.get(i).set(j,(Options.get(i).get(j)/R)*(Weight.get(j)/W));
            }
        }
        return ret;
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
}

