package ravensproject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by justinjackson on 9/23/15.
 */
public class SemanticNetwork {

    //i.e. Map<"a", Map<"inside", "b">>
    private Map<String, Map<String, String>> semanticMap;
    private int numberObjects;

    public SemanticNetwork() {
        semanticMap = new HashMap<>();
        numberObjects = 0;
    }

    public boolean addObject(RavensObject object) {
        Map<String, String> attributes = object.getAttributes();

        // Currently relationmap contains only one relation --> inside
        Map<String, String> relationMap = new HashMap<>();
        if(attributes.keySet().contains("inside")) {
            relationMap.put("inside", attributes.get("inside"));
        }

        semanticMap.put(object.getName(), relationMap);
        numberObjects++;

        return true;
    }

    public Map<String, Map<String, String>> getSemanticMap() {
        return semanticMap;
    }

    public int getNumberObjects() {
        return numberObjects;
    }

    /**
     * Todo - make this not weird looking
     * This method is for debugging purposes. It is used to
     * view a string representation of the semantic network.
     */
    @Override
    public String toString() {
        String semanticNetworkString = "";
        for(String obj : semanticMap.keySet()) {
            semanticNetworkString += obj + ":\n";
            for(String rel : semanticMap.get(obj).keySet()) {
                semanticNetworkString += "  " + rel + ": " + semanticMap.get(obj).get(rel) + "\n";
            }
        }

        return semanticNetworkString;
    }

}
