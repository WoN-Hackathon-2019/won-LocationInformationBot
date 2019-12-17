package won.bot.skeleton.location;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TranslatorCommand {

    private static Map<URI, City> connections = Collections.synchronizedMap(new HashMap<>());

    //location of London
    private static Double sourceLon = -0.118092;
    private static Double sourceLat = 51.509865;

    private static String message = "Hello \n Good bye \n Thank you \n Good Morning \n Where is the next toilet \n Hungry \n Angry";
    private static String rdfId = "LocationInformationBot";

    private static String rdfid = "regID";

    public static String createMessageForSending(float targetLatitude, float targetLongitude, URI uri) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("targetLat", targetLatitude);
        jsonObject.addProperty("targetLon", targetLongitude);
        jsonObject.addProperty("text", message);
        jsonObject.addProperty("sourceLat", sourceLat);
        jsonObject.addProperty("sourceLon", sourceLon);
        jsonObject.addProperty(rdfid, uri.toString());
        System.out.println(jsonObject.toString());
        return jsonObject.toString();
    }

    public static String getParsedMessageFromResponse(String jsonString) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = (JsonObject) parser.parse(jsonString);
        return prettyMessage(jsonObject.get("message").toString());
    }

    private static String prettyMessage(String translation) {
        String[] translationSplit = translation.split("\n");
        String[] messageSplit = message.split("\n");
        StringBuilder returnMessage = new StringBuilder();
        for (int i = 0; i < translationSplit.length; i++) {
            returnMessage.append(messageSplit[i]).append(": ").append(translationSplit[i]).append("\n");
        }
        return returnMessage.toString();
    }

    public static String getUri(String message) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = (JsonObject) parser.parse(message);
        return jsonObject.get(rdfId).toString();
    }

    public static void putConnection(URI uri, City city) {
        connections.put(uri, city);
    }

    public static void removeConnection(URI uri) {
        connections.remove(uri);
    }
}
