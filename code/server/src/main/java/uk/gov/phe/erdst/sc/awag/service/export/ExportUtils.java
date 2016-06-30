package uk.gov.phe.erdst.sc.awag.service.export;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import uk.gov.phe.erdst.sc.awag.utils.Constants;

public final class ExportUtils
{
    private static final String DATE_SEPARATOR = "/";
    private static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat(Constants.OUTPUT_NUMERICAL_VALUES_FORMAT);

    static
    {
        DECIMAL_FORMATTER.setRoundingMode(RoundingMode.HALF_UP);
    }

    private ExportUtils()
    {
    }

    public static String formatIso8601ToSimpleDate(String iso8601)
    {
        String date = iso8601.split("T")[0];
        String[] parts = date.split("-");
        StringBuilder sb = new StringBuilder();
        sb.append(parts[2]).append(DATE_SEPARATOR).append(parts[1]).append(DATE_SEPARATOR).append(parts[0]);
        return sb.toString();
    }

    public static String formatNumericalValues(double value)
    {
        return DECIMAL_FORMATTER.format(value);
    }
}
