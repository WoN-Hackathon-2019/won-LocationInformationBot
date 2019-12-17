package won.bot.skeleton.location;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TranslatorCommand {

    private static Map<URI, City> connections = Collections.synchronizedMap(new HashMap<>());

    //location of London
    private static Double sourceLon = -0.118092;
    private static Double sourceLat = 51.509865;

    private static String message = "Hello \n Good bye \n Thank you \n Good Morning \n Where is the next toilet \n Hungry \n Angry";
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
