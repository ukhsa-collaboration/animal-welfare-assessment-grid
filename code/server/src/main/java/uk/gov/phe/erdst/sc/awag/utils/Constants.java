package uk.gov.phe.erdst.sc.awag.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import uk.gov.phe.erdst.sc.awag.datamodel.Sex;

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
    public static final String OUTPUT_NUMERICAL_VALUES_FORMAT = "#.##";
    public static final CharSequence ARR_START = "[";
    public static final CharSequence ARR_END = "]";
    public static final String EMPTY_STRING = StringUtils.EMPTY;
    public static final Long DISPLAY_ORDER_NUMBER_NOT_SET = 0L;

    private Constants()
    {

    }

    public static final class Security
    {
        public static final String SECURITY_DISABLED_USER = "SECURITY IS DISABLED";
    }

    public static final class WebApi
    {
        /**
         * This is used so that the JAX-RS Analyzer tool doesn't mark optional parameters as
         * required.
         */
        public static final String OPTIONAL_PARAM_VALUE = "";

        public static final String DOWNLOAD_STATUS_COOKIE_PATH = "/";
        public static final int DEFAULT_DOWNLOAD_STATUS_COOKIE_EXPIRATION_TIME_SEC = 10;
        public static final String DEFAULT_DOWNLOAD_STATUS_COOKIE_VALUE = "finished";

        public static final String RESOURCE_PATH_API = "webapi";
        public static final String RESOURCE_ENTITY_API = "entity";
        public static final String RESOURCE_SYSTEM_API = "system";

        public static final String RESOURCE_ALL_PATH = "/all";
        public static final String RESOURCE_LIKE_PATH = "/like";
        public static final String RESOURCE_COUNT_PATH = "/count";
        public static final String RESOURCE_EXPORT_PATH = "/export";
        public static final String RESOURCE_UPLOAD_PATH = "/upload";
        public static final String RESOURCE_DELETE_UPLOAD_PATH = "/delete-upload"; // TODO + param
        public static final String RESOURCE_REVIEWED_UPLOAD_PATH = "/reviewed-upload"; // TODO add to the code? renamed?

        public static final String RESOURCE_SOURCE_PATH = "/source";
        public static final String RESOURCE_HOUSING_PATH = "/housing";
        public static final String RESOURCE_REASON_PATH = "/reason";
        public static final String RESOURCE_SPECIES_PATH = "/species";
        public static final String RESOURCE_ANIMAL_PATH = "/animal";
        public static final String RESOURCE_STUDYGROUP_PATH = "/studygroup";
        public static final String RESOURCE_STUDY_PATH = "/study";
        public static final String RESOURCE_ASSESSMENT_PATH = "/assessment";

        public static final String RESOURCE_ANIMAL_API = RESOURCE_ENTITY_API + "/animal";
        public static final String RESOURCE_ANIMAL_FEMALE_LIKE_PATH = "/female" + RESOURCE_LIKE_PATH;
        public static final String RESOURCE_ANIMAL_MALE_LIKE_PATH = "/male" + RESOURCE_LIKE_PATH;
        public static final String RESOURCE_ANIMAL_DELETED_PATH = "/deleted";

        public static final String RESOURCE_ASSESSMENT_API = RESOURCE_ENTITY_API + "/assessment";
        public static final String RESOURCE_ASSESSMENT_COUNT_ALL = RESOURCE_COUNT_PATH + "/all";
        public static final String RESOURCE_ASSESSMENT_COUNT_BY_ANIMAL_PATH = RESOURCE_COUNT_PATH + "/by-animal";
        public static final String RESOURCE_ASSESSMENT_COUNT_BY_TEMPLATE_PATH = RESOURCE_COUNT_PATH + "/by-template";
        public static final String RESOURCE_ASSESSMENT_COUNT_BY_COMPLETED_PATH = RESOURCE_COUNT_PATH + "/completed";
        public static final String RESOURCE_ASSESSMENT_COUNT_BY_INCOMPLETE_PATH = RESOURCE_COUNT_PATH + "/incomplete";
        public static final String RESOURCE_ASSESSMENT_SEARCH_GET_UNIQUE_FILTERS_PATH = "/search-and-get-unique-filters";
        public static final String RESOURCE_ASSESSMENT_SEARCH_COMPLETE_BETWEEN_DATES_PATH = "/search-complete-between-dates";
        public static final String RESOURCE_ASSESSMENT_SEARCH_PATH = "/search";
        public static final String RESOURCE_ASSESSMENT_PREVIOUS_FOR_ANIMAL_PATH = "/previous/animal";
        public static final String RESOURCE_ASSESSMENT_PREVIOUS_PREVIEW_BY_DATE = "/previous/preview-by-date";
        public static final String RESOURCE_ASSESSMENT_COMPARE_SCORES_WITH_PREVIOUS = "/compare-with-previous";

        public static final String RESOURCE_ASSESSMENT_QUERY_PARAM_STUDY_ID = "studyId";
        public static final String RESOURCE_ASSESSMENT_QUERY_PARAM_STUDY_GROUP_ID = "studyGroupId";
        public static final String RESOURCE_ASSESSMENT_QUERY_PARAM_ANIMAL_ID = "animalId";
        public static final String RESOURCE_ASSESSMENT_QUERY_PARAM_USER_ID = "userId";
        public static final String RESOURCE_ASSESSMENT_QUERY_PARAM_REASON_ID = "reasonId";

        public static final String RESOURCE_ASSESSMENT_QUERY_PARAM_IS_COMPLETE = "isComplete";
        public static final String RESOURCE_ASSESSMENT_QUERY_PARAM_ASSESSMENT_DATE = "assessmentDate";
        public static final String RESOURCE_ASSESSMENT_QUERY_PARAM_ASSESSMENT_ID = "assessmentId";

        public static final String RESOURCE_ASSESSMENT_EXPORT_FORM_PARAM_ASSESSMENT_IDS = "assessmentIds";

        public static final String RESOURCE_ACTIVITY_LOG_API = RESOURCE_ENTITY_API + "/activitylog";

        public static final String RESOURCE_FACTOR_API = RESOURCE_ENTITY_API + "/factor";

        public static final String RESOURCE_HOUSING_API = RESOURCE_ENTITY_API + "/housing";

        public static final String RESOURCE_REASON_API = RESOURCE_ENTITY_API + "/reason";

        public static final String RESOURCE_PARAMETER_API = RESOURCE_ENTITY_API + "/parameter";

        public static final String RESOURCE_SCALE_API = RESOURCE_ENTITY_API + "/scale";

        public static final String RESOURCE_SOURCE_API = RESOURCE_ENTITY_API + "/source";

        public static final String RESOURCE_SPECIES_API = RESOURCE_ENTITY_API + "/species";

        public static final String RESOURCE_STUDY_GROUP_API = RESOURCE_ENTITY_API + "/studygroup";

        public static final String RESOURCE_STUDY_API = RESOURCE_ENTITY_API + "/study";
        public static final String RESOURCE_STUDY_WITH_ANIMAL = "/with-animal";

        public static final String RESOURCE_ASSESSMENT_TEMPLATE_API = RESOURCE_ENTITY_API + "/assessment-template";
        public static final String RESOURCE_ASSESSMENT_TEMPLATE_PARAMETER_PATH = "/parameter";
        public static final String RESOURCE_ASSESSMENT_TEMPLATE_FOR_ANIMAL = "/for-animal";
        public static final String RESOURCE_ASSESSMENT_TEMPLATE_PARAMETER_ID_PATH_PARAM = "parameterId";
        public static final String RESOURCE_ASSESSMENT_TEMPLATE_PARAMETER_PATH_ID_PARAM = "{"
            + RESOURCE_ASSESSMENT_TEMPLATE_PARAMETER_ID_PATH_PARAM + "}";
        public static final String RESOURCE_ASSESSMENT_TEMPLATE_COUNT_ALL = RESOURCE_COUNT_PATH + "/all";

        public static final String RESOURCE_SEX_API = RESOURCE_ENTITY_API + "/sex";

        public static final String RESOURCE_USER_API = RESOURCE_ENTITY_API + "/user";

        public static final String RESOURCE_USER_ACCOUNT_API = RESOURCE_ENTITY_API + "/user-auth";

        public static final String RESOURCE_AUTHENTICATION_API = RESOURCE_SYSTEM_API + "/auth";
        public static final String RESOURCE_AUTHENTICATION_CHECK_PATH = "/auth-check";
        public static final String RESOURCE_AUTHENTICATION_LOGON_DETAILS_PATH = "/logondetails";
        public static final String RESOURCE_AUTHENTICATION_LOGOUT_PATH = "/logout";

        public static final String ID_PATH_PARAM = "id";
        public static final String PATH_ID_PARAM = "{" + ID_PATH_PARAM + "}";

        public static final String QUERY_PARAM_LIKE_FILTER = "likeFilter";

        public static final String PARAM_DATE_FROM = "dateFrom";
        public static final String PARAM_DATE_TO = "dateTo";

        public static final String PAGING_REQUEST_OFFSET_QUERY_PARAM = "offset";
        public static final String PAGING_REQUEST_LIMIT_QUERY_PARAM = "limit";

        public static final String PAGING_RESPONSE_METADATA_OBJECTS_TOTAL_COUNT = "total";
        public static final String PAGING_RESPONSE_METADATA_PAGE = "page";
        public static final String PAGING_RESPONSE_METADATA_TOTAL_PAGES_COUNT = "totalPages";
        public static final int PAGING_RESPONSE_INVALID_PAGE_NUMBER = -1;

        public static final String RESOURCE_SOURCE_UPLOAD_API = RESOURCE_ENTITY_API + RESOURCE_SOURCE_PATH
            + RESOURCE_UPLOAD_PATH;
        public static final String RESOURCE_HOUSING_UPLOAD_API = RESOURCE_ENTITY_API + RESOURCE_HOUSING_PATH
            + RESOURCE_UPLOAD_PATH;
        public static final String RESOURCE_REASON_UPLOAD_API = RESOURCE_ENTITY_API + RESOURCE_REASON_PATH
            + RESOURCE_UPLOAD_PATH;
        public static final String RESOURCE_SPECIES_UPLOAD_API = RESOURCE_ENTITY_API + RESOURCE_SPECIES_PATH
            + RESOURCE_UPLOAD_PATH;
        public static final String RESOURCE_ANIMAL_UPLOAD_API = RESOURCE_ENTITY_API + RESOURCE_ANIMAL_PATH
            + RESOURCE_UPLOAD_PATH;
        public static final String RESOURCE_STUDYGROUP_UPLOAD_API = RESOURCE_ENTITY_API + RESOURCE_STUDYGROUP_PATH
            + RESOURCE_UPLOAD_PATH;
        public static final String RESOURCE_STUDY_UPLOAD_API = RESOURCE_ENTITY_API + RESOURCE_STUDY_PATH
            + RESOURCE_UPLOAD_PATH;
        public static final String RESOURCE_ASSESSMENT_UPLOAD_API = RESOURCE_ENTITY_API + RESOURCE_ASSESSMENT_PATH
            + RESOURCE_UPLOAD_PATH;
    }

    public static final class Upload
    {
        public static final int MIN_TEMPLATE_PARAMETER_FACTOR_COLUMNS = 4; // TODO make flexible - TEST

        public static final String ERR_IMPORT_CONSTRAINT_VIOLATION = "Duplicates found. Line %d, Column '%s' already exists in database.";
        public static final String ERR_IMPORT_INVALID_FORMAT_ABORT = "The file format is invalid, import aborted.";
        public static final String ERR_IMPORT_INVALID_NUMBER_OF_COLUMNS = "The file format is invalid, expects %d column(s) at line %d, import aborted.";
        public static final String ERR_IMPORT_EMPTY_DATA_FILE = "The data file is empty.";

        public static final String CSV_SEPARATOR = ",";
        public static final long UPLOAD_HEADER_COLUMN_LINE_NUMBER = 1L;

        public static final String SEX_FEMALE_CASE_INSENSITIVE = Sex.FEMALE.toLowerCase(); // TODO remove
        public static final String SEX_MALE_CASE_INSENSITIVE = Sex.MALE.toLowerCase(); // TODO remove

        public static final String[] UPLOAD_HEADER_FACTOR_COLUMNS = new String[] {"factor-name", "factor-description"};
        public static final String[] UPLOAD_HEADER_PARAMETER_COLUMNS = new String[] {"parameter-name"};
        public static final String[] UPLOAD_HEADER_PREFIX_TEMPLATE_PARAMETER_COLUMNS = new String[] {"template-name",
                "scale-name", "parameter-count", "is-allow-zero-scores"};

        public static final String[] UPLOAD_HEADER_SOURCE_COLUMNS = new String[] {"source-name"};
        public static final String[] UPLOAD_HEADER_SCALE_COLUMNS = new String[] {"scale-name", "scale-minimum",
                "scale-maximum"};
        public static final String[] UPLOAD_HEADER_SPECIES_COLUMNS = new String[] {"species-name"};
        public static final String[] UPLOAD_HEADER_ASSESSMENT_REASON_COLUMNS = new String[] {"assessment-reason-name"};
        public static final String[] UPLOAD_HEADER_ASSESSMENT_ANIMAL_HOUSING_COLUMNS = new String[] {
                "animal-housing-name"};
        public static final String[] UPLOAD_HEADER_ANIMAL_COLUMNS = new String[] {"animal-number", "date-of-birth",
                "sex", "species", "source-name", "dam-animal-name", "father-animal-name", "assessment-template-name",
                "is-alive"};
        public static final String[] UPLOAD_HEADER_PREFIX_ASSESSMENT_COLUMNS = new String[] {"date", "is-complete",
                "animal-number", "animal-housing-name", "assessment-reason-name", "performed-by"};
        public static final String[] UPLOAD_HEADER_STUDY_COLUMNS = new String[] {"study-number", "is-study-open",
                "study-study-groups"};
        public static final String[] UPLOAD_HEADER_STUDY_GROUPS_COLUMNS = new String[] {"study-number", "is-study-open",
                "study-study-groups"};

    }

}
