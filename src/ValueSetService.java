import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ValueSetService {
    public String findCategory(String code){
        return "";
    }

    BufferedReader br = null;

    public List<String> loadValueSet(){
        List<String> valueSet = new ArrayList<>();

        try{
            File file = new File("/home/michael/Documents/SpringIdea/ConceptCodeMapping/src/dataset/valueset.csv");

            br = new BufferedReader(new FileReader(file));
            String line;
            while((line = br.readLine()) != null){
                valueSet.add(line);
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if(br != null){
                try{
                    br.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return valueSet;
    }
}
