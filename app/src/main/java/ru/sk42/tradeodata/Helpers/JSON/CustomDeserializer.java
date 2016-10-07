package ru.sk42.tradeodata.Helpers.JSON;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

import ru.sk42.tradeodata.Model.Catalogs.Charact;

public class CustomDeserializer extends JsonDeserializer<Charact> {

    /**
     * {"id":1,"itemNr":"theItem","owner":2}
     */
    @Override
    public Charact deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
        final JsonNode node = jp.getCodec().readTree(jp);
//        final int id = (Integer) ((IntNode) node.get("id")).numberValue();
//        final String itemName = node.get("itemName").asText();
//        final int userId = (Integer) ((IntNode) node.get("createdBy")).numberValue();

        return new Charact();
    }

}