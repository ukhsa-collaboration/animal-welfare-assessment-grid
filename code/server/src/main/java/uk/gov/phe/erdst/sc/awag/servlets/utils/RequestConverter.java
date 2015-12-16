package uk.gov.phe.erdst.sc.awag.servlets.utils;

public interface RequestConverter
{

    <T> Object convert(String data, Class<T> clazz);
}
