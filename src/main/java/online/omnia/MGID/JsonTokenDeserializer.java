package online.omnia.MGID;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by lollipop on 28.10.2017.
 */
public class JsonTokenDeserializer implements JsonDeserializer<JsonTokenEntity>{
    @Override
    public JsonTokenEntity deserialize(JsonElement jsonElement, Type type,
                                       JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        JsonTokenEntity tokenEntity = new JsonTokenEntity();
        tokenEntity.setToken(object.get("token").getAsString());
        tokenEntity.setRefreshToken(object.get("refreshToken").getAsString());
        tokenEntity.setIdAuth(object.get("accountId").getAsString());
        return tokenEntity;
    }
}
