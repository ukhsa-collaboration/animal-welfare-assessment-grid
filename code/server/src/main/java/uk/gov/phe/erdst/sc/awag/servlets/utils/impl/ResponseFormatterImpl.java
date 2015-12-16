package uk.gov.phe.erdst.sc.awag.servlets.utils.impl;

import javax.enterprise.context.RequestScoped;

import uk.gov.phe.erdst.sc.awag.servlets.utils.ResponseFormatter;

import com.google.gson.Gson;

@RequestScoped
public class ResponseFormatterImpl implements ResponseFormatter
{
    private static Gson sGSON = new Gson();

    @Override
    public String toJson(Object src)
    {
        return sGSON.toJson(src);
    }
}
