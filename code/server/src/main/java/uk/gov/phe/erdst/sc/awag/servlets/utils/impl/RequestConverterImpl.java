package uk.gov.phe.erdst.sc.awag.servlets.utils.impl;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.servlets.utils.RequestConverter;

import com.google.gson.Gson;

@Stateless
public class RequestConverterImpl implements RequestConverter
{
    private static Gson sGSON = new Gson();

    @Override
    public <T> Object convert(String data, Class<T> clazz)
    {
        return sGSON.fromJson(data, clazz);
    }

}
