package uk.gov.phe.erdst.sc.awag.utils;

public final class StringHelper
{
    private StringHelper()
    {
    }

    public static String getStringOfLength(int length)
    {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
        {
            sb.append("a");
        }

        return sb.toString();
    }
}
