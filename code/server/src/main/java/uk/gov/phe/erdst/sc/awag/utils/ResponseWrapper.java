package uk.gov.phe.erdst.sc.awag.utils;

public final class ResponseWrapper
{
    private ResponseWrapper()
    {
    }

    public static String wrap(String callback, String data)
    {
        if (callback != null && data != null)
        {
            return callback + "(" + data + ")";
        }

        throw new IllegalArgumentException();
    }
}
