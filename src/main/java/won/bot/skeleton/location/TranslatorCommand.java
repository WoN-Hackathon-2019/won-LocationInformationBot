package won.bot.skeleton.location;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TranslatorCommand {

    private static Map<URI, City> connections = Collections.synchronizedMap(new HashMap<>());

    //location of London
    private static Double sourceLon = -0.118092;
    private static Double sourceLat = 51.509865;

    private static String message = "Hello ; Good bye ; Thank you ; Good Morning ; Where is the toilet ; Hungry ";
    private static String rdfId = "reqID";

    public static String createMessageForSending(float targetLatitude, float targetLongitude, URI uri) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("targetLat", targetLatitude);
        jsonObject.addProperty("targetLon", targetLongitude);
        jsonObject.addProperty("text", message);
        jsonObject.addProperty("sourceLat", sourceLat);
        jsonObject.addProperty("sourceLon", sourceLon);
        jsonObject.addProperty(rdfId, uri.toString());
        System.out.println(jsonObject.toString());
        return jsonObject.toString();
    }

    public static String getParsedMessageFromResponse(String jsonString) throws URISyntaxException {
        System.out.println("jsonString parse form response" + jsonString);
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = (JsonObject) parser.parse(jsonString);
        URI uri = new URI(jsonObject.get(rdfId).toString().replace("\"", ""));
        if (connections.containsKey(uri)) {
            return connections.get(uri) + "\n" + prettyMessage(jsonObject.get("message").toString());
        } else return null;
    }

    public static void main(String[] args) {
        String test="\"Ciao.\n Arrivederci\n Grazie\n Buongiorno\n Dove si trova il prossimo bagno\n affamato\n Arrabbiato\"";
        System.out.println("out:"+prettyMessage(test));
    }

    private static String prettyMessage(String translation) {
        System.out.println(translation);
        String[] translationSplit = translation.replace("\"","").split(";");
        String[] messageSplit = message.split(";");
        System.out.println("OUTPUT:"+ Arrays.stream(translationSplit).collect(Collectors.joining())+"\n"+ Arrays.stream(messageSplit).collect(Collectors.joining()));
        String returnMessage = "_____________________\n";
        for (int i = 0; i < translationSplit.length; i++) {
            //System.out.println("i="+i);
            try {
                returnMessage += messageSplit[i] + ": " + translationSplit[i] + "\n";
            }catch(Exception e){}
            //System.out.println(messageSplit[i]+" "+translationSplit[i]);
            //returnMessage.append(messageSplit[i]).append(": ").append(translationSplit[i]).append("\n");
        }
        return returnMessage;
    }

    public static String getUri(String message) {
        System.out.println("message to parse to json: " + message);
        JsonObject jsonObject = new JsonParser().parse(message).getAsJsonObject();
        System.out.println("jsonObject: " + jsonObject.toString());
        String string = jsonObject.get(rdfId).toString();
        System.out.println("rdfId: " + string);
        return string.replace("\"", "");
    }

    public static void putConnection(URI uri, City city) {
        connections.put(uri, city);
    }

    public static void removeConnection(URI uri) {
        connections.remove(uri);
    }
}
