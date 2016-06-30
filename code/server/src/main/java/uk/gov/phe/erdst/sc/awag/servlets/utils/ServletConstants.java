package uk.gov.phe.erdst.sc.awag.servlets.utils;

public final class ServletConstants
{
    public static final String REQ_PARAM_SELECT_ACTION_ALL = "all";
    public static final String REQ_PARAM_ANIMAL_ID = "animalId";
    public static final String REQ_PARAM_TEMPLATE_ID = "templateId";
    public static final String REQ_PARAM_SELECT_ACTION_LIKE = "sellike";
    public static final String REQ_PARAM_CALLBACK = "callback";
    public static final String REQ_PARAM_ACTION = "selaction";
    public static final String RES_NO_RESPONSE = "";
    public static final String REQ_PARAM_ALL = "all";
    public static final String REQ_PARAM_SELECT_ACTION_SEL_ID = "selid";
    public static final String REQ_PARAM_ID = "id";
    public static final String REQ_PARAM_SEX_M = "m";
    public static final String REQ_PARAM_SEX_F = "f";
    public static final String REQ_PARAM_LIKE = "like";
    public static final String REQ_PARAM_SEX = "sex";
    public static final String REQ_PARAM_USER_ID = "userId";
    public static final String REQ_PARAM_REASON_ID = "reasonId";
    public static final String REQ_PARAM_STUDY_ID = "studyId";
    public static final String REQ_PARAM_HOUSING = "housing";
    public static final String REQ_PARAM_STUDY_GROUP = "study-group";
    public static final String REQ_PARAM_STUDY_GROUP_ID = "studyGroupId";
    public static final String REQ_PARAM_ASSESS_IS_COMPLETE = "isComplete";
    public static final String REQ_PARAM_STUDY_WITH_ANIMAL = "study-with-animal";
    public static final String REQ_PARAM_ASSESS_BETWEEN = "assessments-between";
    public static final String REQ_PARAM_ASSESS_SEARCH = "assessments-search";
    public static final String REQ_PARAM_ASSESS_DYNAMIC_SEARCH = "assessment-dynamic-search";
    public static final String REQ_PARAM_COUNT_BY_COMPLETENESS = "assessments-count-by-completeness";
    public static final String REQ_PARAM_ASSESS_IS_SUBMIT = "isSubmit";
    public static final String REQ_PARAM_ANIMAL_TEMPLATE = "animal-template";
    public static final String REQ_PARAM_DATE_TO = "date-to";
    public static final String REQ_PARAM_DATE_FROM = "date-from";
    public static final String REQ_PARAM_DATE = "date";
    public static final String REQ_PARAM_INCLUDE = "include";
    public static final String REQ_PARAM_OFFSET = "offset";
    public static final String REQ_PARAM_LIMIT = "limit";
    public static final String REQ_PARAM_COUNT = "count";
    public static final String REQ_PARAM_COUNT_BY_ANIMAL_ID = "count-by-animal-id";
    public static final String REQ_PARAM_COUNT_BY_TEMPLATE_ID = "count-by-template-id";

    public static final String REQ_PARAM_EXPORT_TYPE = "export-type";
    public static final String EXPORT_TYPE_ACTIVITY_LOGS = "activitylogs";
    public static final String EXPORT_TYPE_ASSESSMENTS = "assessments";
    public static final String REQ_PARAM_EXPORT_DATA = "exportData";

    public static final int DEFAULT_DOWNLOAD_STATUS_COOKIE_EXPIRATION_TIME_SEC = 10;
    public static final String DEFAULT_DOWNLOAD_STATUS_COOKIE_VALUE = "finished";

    private ServletConstants()
    {
    }
}
