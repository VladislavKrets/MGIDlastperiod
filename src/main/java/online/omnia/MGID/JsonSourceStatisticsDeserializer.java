package online.omnia.MGID;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by lollipop on 28.11.2017.
 */
public class JsonSourceStatisticsDeserializer implements JsonDeserializer<List<JsonSourceEntity>>{
    @Override
    public List<JsonSourceEntity> deserialize(JsonElement jsonElement, Type type,
                                              JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        
        return null;
    }
}
