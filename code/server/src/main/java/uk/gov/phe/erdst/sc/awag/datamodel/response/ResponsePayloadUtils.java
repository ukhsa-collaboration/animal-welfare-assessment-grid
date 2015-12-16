package uk.gov.phe.erdst.sc.awag.datamodel.response;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class ResponsePayloadUtils
{
    private static final String ERR_ENCODE_STRING = "Could not encode url";

    private ResponsePayloadUtils()
    {
    }

    public static String getEncodedUrl(String unEncodedUrl)
    {
        try
        {
            return URLEncoder.encode(unEncodedUrl, StandardCharsets.UTF_8.name());
        }
        catch (UnsupportedEncodingException ex)
        {
            return ERR_ENCODE_STRING;
        }
    }
}
