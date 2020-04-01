package uk.gov.phe.erdst.sc.awag.webapi.resource;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Provider
public class MyJacksonObjectMapper implements ContextResolver<ObjectMapper>
{
    final ObjectMapper defaultObjectMapper;

    public MyJacksonObjectMapper()
    {
        defaultObjectMapper = createDefaultMapper();
    }

    @Override
    public ObjectMapper getContext(final Class<?> type)
    {
        return defaultObjectMapper;
    }

    private static ObjectMapper createDefaultMapper()
    {
        final ObjectMapper result = new ObjectMapper();
        result.disable(SerializationFeature.INDENT_OUTPUT);
        result.setSerializationInclusion(Include.NON_NULL);
        result.setSerializationInclusion(Include.NON_EMPTY);

        return result;
    }
}
