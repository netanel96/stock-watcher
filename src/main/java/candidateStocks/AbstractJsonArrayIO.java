package candidateStocks;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public abstract class  AbstractJsonArrayIO<T> {
    String path;
    JSONParser jsonParser = new JSONParser();
    protected List<T> jsonObjectList =new ArrayList<>();
    String UpdateJsonFileLog ="";

    public AbstractJsonArrayIO(String path) {
        this.path=path;
        JSONArray jsonArray = initiateJsonArray();
        setJsonObjectList(jsonArray);
    }

    private void setJsonObjectList(JSONArray jsonArray) {
        jsonObjectList=new ArrayList<>();
        jsonArray.forEach(jsonObject-> addToList((JSONObject)jsonObject));
    }

    public void UpdateObject(T updateObject) {
        JSONArray array = initiateJsonArray();
        updateJsonArray(array, updateObject);
        writeJsonArrayToFile(array);
        logUpdate(getObjectFromList(updateObject), updateObject);
        setJsonObjectList(array);
        }

    private void logUpdate(T oldObject, T updatedObject){
         UpdateJsonFileLog +="\n"+"old object:"+oldObject+"\n"+"updated object:"+updatedObject+"\n";
     }

    private JSONArray initiateJsonArray() {
        JSONArray array = null;
        try {
            array = (JSONArray) jsonParser.parse(new FileReader(path));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return array;
    }

    private void writeJsonArrayToFile(JSONArray array) {
        try (FileWriter file = new FileWriter(path)) {
            file.write(array.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void addToList(JSONObject jsonObject);

    protected abstract T getObjectFromList(T objectIdentifier);

    protected abstract void updateJsonArray(JSONArray array,T object);
}
