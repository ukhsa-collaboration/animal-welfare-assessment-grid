package uk.gov.phe.erdst.sc.awag.deprecated;

import javax.ejb.Stateless;

import com.google.gson.Gson;

/**
 * Leftover class from version where JSON conversion was done manually. Can be removed once integration tests are
 * updated to use Jersey JAX-RS client.
 */
@Stateless
@Deprecated
public class RequestConverter
{
    private static Gson sGSON = new Gson();

    public <T> Object convert(String data, Class<T> clazz)
    {
        return sGSON.fromJson(data, clazz);
    }

}
