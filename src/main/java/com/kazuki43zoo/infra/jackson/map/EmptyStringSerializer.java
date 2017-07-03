package com.kazuki43zoo.infra.jackson.map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public final class EmptyStringSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(final String value, final JsonGenerator jsonGenerator, final SerializerProvider provider) throws IOException {
        jsonGenerator.writeString("");
    }

}
