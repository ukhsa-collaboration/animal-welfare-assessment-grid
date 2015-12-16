package uk.gov.phe.erdst.sc.awag.utils;

public final class ExceptionUtils
{
    private ExceptionUtils()
    {
    }

    public static String getUserFriendlyExceptionString(Exception ex)
    {
        return ex.getMessage().split(":")[1];
    }
}
