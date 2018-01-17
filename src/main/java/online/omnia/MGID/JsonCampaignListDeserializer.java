package online.omnia.MGID;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lollipop on 28.11.2017.
 */
public class JsonCampaignListDeserializer implements JsonDeserializer<List<JsonCampaignEntity>>{
    @Override
    public List<JsonCampaignEntity> deserialize(JsonElement jsonElement, Type type,
                                                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        String data = object.toString().replaceAll("\"\\d+\":", "").replaceAll(",\"widgets\":\\{(\"\\[.*\\]\",?)*\\}", "");
        int length = data.length();
        data = "[" + data.substring(1, length - 1) + "]";
        System.out.println(data);
        JsonParser parser = new JsonParser();
        jsonElement = parser.parse(data);
        JsonArray array = jsonElement.getAsJsonArray();
        List<JsonCampaignEntity> jsonCampaignEntities = new ArrayList<>();
        JsonCampaignEntity jsonCampaignEntity;
        for (JsonElement element : array) {
            jsonCampaignEntity = new JsonCampaignEntity();
            jsonCampaignEntity.setId(element.getAsJsonObject().get("id").getAsInt());
            jsonCampaignEntity.setName(element.getAsJsonObject().get("name").toString());
            jsonCampaignEntities.add(jsonCampaignEntity);
        }
        return jsonCampaignEntities;
    }
}
