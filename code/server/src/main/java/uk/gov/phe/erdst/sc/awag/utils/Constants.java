package uk.gov.phe.erdst.sc.awag.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public final class Constants
{
    public static final String PERSISTENCE_CONTEXT_DEFAULT_UNIT_NAME = "aw";
    public static final String PERSISTENCE_CONTEXT_AUTH_UNIT_NAME = "awauth";
    public static final Long UNASSIGNED_ID = -1L;
    public static final Long MIN_VALID_ID = 1L;
    public static final Long ID_NOT_SET = null;
    public static final String FEMALE_CHAR = "f";
    public static final Locale AW_LOCALE = Locale.ENGLISH;
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final Charset AW_CHARSET = StandardCharsets.UTF_8;
    public static final String AW_DIGEST = "SHA-256";

    public static final String OUTPUT_ENCODING_UTF_8 = "UTF-8";
    public static final String OUTPUT_DATE_FORMAT = "yyyyMMdd'T'HHmmss'Z'";
    public static final String OUTPUT_FILES_PREFIX = "awag";

    private Constants()
    {
    }
}
