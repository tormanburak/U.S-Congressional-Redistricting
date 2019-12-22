//package webapp.model;
//
//import jdk.nashorn.internal.parser.JSONParser;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//
//public class Data {
//
//    public void getData(){
//        JSONParser parser = new JSONParser();
//
//        Object obj = null;
//        try {
//            FileReader f = new FileReader("webapp/data/LA Precincts Test 2.json");
//            obj = parser.parse(f);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        JSONObject jsonObject = (JSONObject) obj;
//        JSONArray features = (JSONArray) jsonObject.get("features");
//        //   JSONArray features = (JSONArray) jsonObject.get("features")[0];
//
//    }
//}
