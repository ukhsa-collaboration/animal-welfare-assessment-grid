package uk.gov.phe.erdst.sc.awag.service.validation.utils;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Payload;

import uk.gov.phe.erdst.sc.awag.servlets.utils.ServletConstants;

public final class ValidationConstants
{
    public static final String VALID_ID_ANNOT_DEFAULT_MESSAGE = "Invalid Id";
    public static final String VALID_ANIMAL_ANNOT_DEFAULT_MSG = "Invalid animal";
    public static final String VALID_SCALE_ANNOT_DEFAULT_MSG = "Invalid scale";
    public static final String VALID_DATE_ANNOT_DEFAULT_MSG = "Invalid date";
    public static final String VALID_STUDY_ANNOT_DEFAUL_MSG = "Invalid study";
    public static final String VALID_ASSESSMENT_SCORE_ANNOT_DEFAULT_MSG = "Invalid score";
    public static final String SIMPLE_TEXT_INPUT_ANNOT_DEFAULT_MSG = "Invalid text input";
    public static final String VALID_ASSESSMENTS_GET_REQUEST_ANNOT_DEFAULT_MSG = "Invalid assessments get request";
    public static final String VALID_ACTIVITY_LOG_GET_REQUEST_ANNOT_DEFAULT_MSG = "Invalid activity log get request";
    public static final String VALID_USER_AUTH_ANNOT_DEFAULT_MSG = "Invalid authentication details";
    public static final String VALID_IDS_ARRAY_ANNOT_DEFAULT_MESSAGE = "Invalid Id in array";

    public static final String NO_ERR_TEMPLATE = "No error template set";

    public static final String SIMPLE_TEXT_INPUT_REGEX = "^[a-zA-Z0-9\\-\\_\\.\\,\\/\\(\\) ]*$";
    public static final String COMMENTS_TEXT_INPUT_REGEX = "^[a-zA-Z0-9\\-\\_\\.\\,\\/\\(\\)\\: ]*$";
    public static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$;";
    public static final String PASSWORD_REGEX = "^[a-zA-Z0-9\\-\\_\\.\\,\\/\\(\\) ]*$";
    public static final int SIMPLE_TEXT_INPUT_SIZE_MAX = 255;
    public static final int SIMPLE_TEXT_INPUT_SIZE_MIN = 2;

    public static final int EXTENDED_SIMPLE_TEXT_INPUT_SIZE_MAX = 1024;

    public static final long ENTITY_NEG_ID = -1L;
    public static final long ENTITY_MIN_ID = 1L;
    public static final String ENTITY_NAME_REGEX = SIMPLE_TEXT_INPUT_REGEX;

    public static final String ERR_FROM_TO_DATE_PARAMS = "Date from must be before date to.";

    // Animal
    public static final int ANIMAL_NO_SIZE_MIN = 2;
    public static final int ANIMAL_NO_SIZE_MAX = SIMPLE_TEXT_INPUT_SIZE_MAX;
    public static final String ANIMAL_ENTITY_NAME = "Animal";
    public static final String ANIMAL_NUMBER_PROPERTY = "Animal number";
    public static final String ANIMAL_IS_ALIVE_NULL_ERR = "You must provide a status of whether"
        + " the animal is alive or not.";
    // Parents
    public static final String DAM = "Dam";
    public static final String FATHER = "Father";
    public static final String ERR_DAM_DOB = "Incorrect date %s, dam must be older than date of birth %s.";
    public static final String ERR_DAM_NOT_FEMALE = "Dam must be set to a female animal.";
    public static final String ERR_FATHER_DOB = "Incorrect date %s, father must be older than date of birth %s.";
    public static final String ERR_FATHER_NOT_MALE = "Father must be set to a male animal.";
    public static final String ERR_PARENT_ANIMAL_SAME = "Animal cannot be the same as a parent.";
    public static final String ERR_PARENT_SAME = "Dam and father cannot be the same.";
    public static final String ERR_PARENT_NO_SUCH_DAM = "Dam does not exist.";
    public static final String ERR_PARENT_NO_SUCH_FATHER = "Father does not exist.";

    // Source
    public static final int SOURCE_NAME_SIZE_MIN = 2;
    public static final int SOURCE_NAME_SIZE_MAX = SIMPLE_TEXT_INPUT_SIZE_MAX;
    public static final String SOURCE_ENTITY_NAME = "Source";
    public static final String SOURCE_NAME_PROPERTY = "Source name";

    // Species
    public static final int SPECIES_NAME_SIZE_MIN = 3;
    public static final int SPECIES_NAME_SIZE_MAX = SIMPLE_TEXT_INPUT_SIZE_MAX;
    public static final String SPECIES_ENTITY_NAME = "Species";
    public static final String SPECIES_NAME_PROPERTY = "Species name";

    // Reason
    public static final String REASON_ENTITY_NAME = "Reason";
    public static final int REASON_NAME_SIZE_MAX = SIMPLE_TEXT_INPUT_SIZE_MAX;
    public static final String REASON_NAME_PROPERTY = "Reason name";
    public static final int REASON_NAME_SIZE_MIN = 3;

    // Animal Housing
    public static final int ANIMAL_HOUSING_NAME_SIZE_MIN = 3;
    public static final String ANIMAL_HOUSING_ENTITY_NAME = "Animal Housing";
    public static final int ANIMAL_HOUSING_NAME_SIZE_MAX = SIMPLE_TEXT_INPUT_SIZE_MAX;
    public static final String ANIMAL_HOUSING_NAME_PROPERTY = "Animal Housing name";

    // User
    public static final int USER_NAME_SIZE_MIN = SIMPLE_TEXT_INPUT_SIZE_MIN;
    public static final int USER_NAME_SIZE_MAX = SIMPLE_TEXT_INPUT_SIZE_MAX;
    public static final String USER_ENTITY_NAME = "User";
    public static final String USER_NAME_PROPERTY = "User name";

    // Parameter
    public static final int PARAMETER_NAME_SIZE_MIN = 3;
    public static final String PARAMETER_ENTITY_NAME = "Parameter";
    public static final int PARAMETER_NAME_SIZE_MAX = SIMPLE_TEXT_INPUT_SIZE_MAX;
    public static final String PARAMETER_NAME_PROPERTY = "Parameter name";

    // Sex
    public static final String SEX_ENTITY_NAME = "Sex";

    // AssessmentTemplate
    public static final String ASSESSMENT_TEMPLATE_ENTITY_NAME = "Assessment template";

    // Date
    public static final String DOB = "DOB";

    // Study group
    public static final int STUDY_GROUP_NAME_SIZE_MIN = SIMPLE_TEXT_INPUT_SIZE_MIN;
    public static final int STUDY_GROUP_NAME_SIZE_MAX = SIMPLE_TEXT_INPUT_SIZE_MAX;
    public static final String STUDY_GROUP_ENTITY_NAME = "Study group";
    public static final String STUDY_GROUP_NAME_PROPERTY = "Study group name";

    // Study
    public static final int STUDY_NAME_SIZE_MIN = SIMPLE_TEXT_INPUT_SIZE_MIN;
    public static final int STUDY_NAME_SIZE_MAX = SIMPLE_TEXT_INPUT_SIZE_MAX;
    public static final String STUDY_ENTITY_NAME = "Study";
    public static final String STUDY_NAME_PROPERTY = "Study name";

    // Scale
    public static final int SCALE_NAME_SIZE_MIN = SIMPLE_TEXT_INPUT_SIZE_MIN;
    public static final int SCALE_NAME_SIZE_MAX = SIMPLE_TEXT_INPUT_SIZE_MAX;
    public static final String SCALE_ENTITY_NAME = "Scale";
    public static final String SCALE_NAME_PROPERTY = "Scale name";
    public static final String SCALE_MIN_PROPERTY = "Scale min";
    public static final String SCALE_MAX_PROPERTY = "Scale max";

    // Factor
    public static final int FACTOR_NAME_SIZE_MIN = SIMPLE_TEXT_INPUT_SIZE_MIN;
    public static final int FACTOR_NAME_SIZE_MAX = SIMPLE_TEXT_INPUT_SIZE_MAX;
    public static final String FACTOR_ENTITY_NAME = "Factor";
    public static final String FACTOR_NAME_PROPERTY = "Factor name";

    // Assessment Template
    public static final int TEMPLATE_NAME_SIZE_MIN = SIMPLE_TEXT_INPUT_SIZE_MIN;
    public static final int TEMPLATE_NAME_SIZE_MAX = SIMPLE_TEXT_INPUT_SIZE_MAX;
    public static final String TEMPLATE_NAME_PROPERTY = "Template name";
    public static final String TEMPLATE_ENTITY_NAME = "Template";

    // User auth
    public static final String AUTH_USER_ENTITY_NAME = "User";
    public static final int AUTH_USER_NAME_SIZE_MIN = SIMPLE_TEXT_INPUT_SIZE_MIN;
    public static final int AUTH_USER_NAME_SIZE_MAX = SIMPLE_TEXT_INPUT_SIZE_MAX;
    public static final String AUTH_USER_NAME_PROPERTY = "Username";

    public static final int AUTH_USER_EMAIL_SIZE_MIN = 5;
    public static final int AUTH_USER_EMAIL_SIZE_MAX = SIMPLE_TEXT_INPUT_SIZE_MAX;
    public static final String AUTH_USER_EMAIL_PROPERTY = "Email";

    public static final int AUTH_PASSWORD_SIZE_MIN = 8;
    public static final int AUTH_PASSWORD_SIZE_MAX = SIMPLE_TEXT_INPUT_SIZE_MAX;
    public static final String AUTH_PASSWORD_PROPERTY = "Password";
    public static final String AUTH_RETYPED_PASSWORD_PROPERTY = "Retyped password";

    public static final String AUTH_GROUP_PROPERTY = "Group name";
    public static final int AUTH_GROUP_SIZE_MIN = SIMPLE_TEXT_INPUT_SIZE_MIN;
    public static final int AUTH_GROUP_SIZE_MAX = SIMPLE_TEXT_INPUT_SIZE_MAX;

    // Assessment
    public static final String ASSESSMENT_ENTITY_NAME = "Assessment";
    public static final String ASSESSMENT_DATE = "Assessment date";
    public static final String ERR_ASSESSMENT_PARTS_NOT_NULL = "Scores, average scores and"
        + " parameters comments must not be null.";
    public static final String ERR_ASSESSMENT_SCORE_AVERAGE_KEYS_DIFF = "Parameters ids do not"
        + " match in score and average scores parts.";

    public static final String ASSESSMENT_AVERAGE_SCORES = "Average scores";
    public static final String ASSESSMENT_PARAMETER_SCORES = "Parameters scores";
    public static final String ASSESSMENT_PARAMETER_COMMENTS = "Parameters comments";

    public static final String ERR_ASSESSMENT_SCORES_AVERAGES_INCORRECT = "Parameters scores do"
        + " not match averages after recalculation.";
    public static final String ERR_ASSESSMENT_PARAMETER_COMMENTS_NOT_NULL = "Parameters comments"
        + " must not be null.";
    public static final String ERR_ASSESSMENT_ZERO_NONIGNORED_SCORE = "Parameter score cannot be 0 "
        + "unless it is ignored.";
    public static final String ERR_ASSESSMENT_UPDATE_COMPLETED_ATTEMPT = "Assessment cannot be updated since it has "
        + "been finalised already.";
    public static final String ERR_ASSESSMENT_SCORE_OUTSIDE_SCALE_FORMAT = "Score for factor is outside "
        + "scale (min: %s, max: %s)";

    public static final String ERR_ASSESSMENT_TEMPLATE_MISMATCH_FORMAT = "%s %s do not match template used";
    public static final String ERR_ASSESSMENT_TEMPLATE_FACTORS_MISMATCH = String.format(
        ERR_ASSESSMENT_TEMPLATE_MISMATCH_FORMAT, "Sent", "factors");
    public static final String ERR_ASSESSMENT_TEMPLATE_PARAMETERS_MISMATCH = String.format(
        ERR_ASSESSMENT_TEMPLATE_MISMATCH_FORMAT, "Sent", "parameters");
    public static final String ERR_ASSESSMENT_TEMPLATE_SUBMITTED_FACTORS_MISMATCH = String.format(
        ERR_ASSESSMENT_TEMPLATE_MISMATCH_FORMAT, "Submitted", "factors");
    public static final String ERR_ASSESSMENT_TEMPLATE_SUBMITTED_PARAMETERS_MISMATCH = String.format(
        ERR_ASSESSMENT_TEMPLATE_MISMATCH_FORMAT, "Submitted", "parameters");

    public static final String PAGING_OFFSET_NAME = "Offset";
    public static final String PAGING_LIMIT_NAME = "Limit";
    public static final int MIN_PAGING_OFFSET_VALUE = 0;
    public static final int MIN_PAGING_LIMIT_VALUE = 1;

    // Date
    public static final String ERR_DATE_FORMAT = "%s must be in the format dd-mm-yyyy.";
    public static final String DATE_FROM = "Date from";
    public static final String DATE_TO = "Date to";

    // Exception messages
    public static final String ERR_NO_SUCH_ENTITY = "Could not find entity %s with id %s.";
    public static final String ERR_NO_SUCH_ENTITY_WITH_NAME = "Could not find entity %s with name %s.";

    public static final String ERR_SEX_PARAM = String.format("Sex parameter %s should be set to %s or %s.",
        ServletConstants.REQ_PARAM_SEX, ServletConstants.REQ_PARAM_SEX_F, ServletConstants.REQ_PARAM_SEX_M);
    public static final String ERR_CALLBACK_PARAM = String.format(
        "The callback parameter %s must be provided for GET requests.", ServletConstants.REQ_PARAM_CALLBACK);
    public static final String ERR_ACTION_PARAM = String.format("The action parameter %s must be provided.",
        ServletConstants.REQ_PARAM_ACTION);
    public static final String ERR_LIKE_PARAM = String.format(
        "The like parameter %s must also be provided if %s is provided.", ServletConstants.REQ_PARAM_LIKE,
        ServletConstants.REQ_PARAM_SELECT_ACTION_LIKE);
    public static final String ERR_RESOURCE_ID_NOT_NUMBER = "The id provided is not a number.";

    public static final String ERR_ID_PARAM = String.format(
        "The id parameter %s must also be provided if %s is provided.", ServletConstants.REQ_PARAM_ID,
        ServletConstants.REQ_PARAM_SELECT_ACTION_SEL_ID);

    public static final String ERR_USER_PARAM = String.format("The user parameter %s must be provided.",
        ServletConstants.REQ_PARAM_USER_ID);
    public static final String ERR_REASON_PARAM = String.format("The reason parameter %s must be provided.",
        ServletConstants.REQ_PARAM_REASON_ID);
    public static final String ERR_ANIMAL_ID_PARAM = String.format("The animal id parameter %s must be provided.",
        ServletConstants.REQ_PARAM_ANIMAL_ID);

    public static final String ERR_HOUSING_PARAM = String.format("The housing parameter %s must be provided.",
        ServletConstants.REQ_PARAM_HOUSING);
    public static final String ERR_ALL_PARAM = String.format("The parameter %s if provided must be set to %s.",
        ServletConstants.REQ_PARAM_ALL, ServletConstants.REQ_PARAM_ALL);
    public static final String ERR_STUDY_GROUP_PARAM = String.format("The study group parameter %s must be provided.",
        ServletConstants.REQ_PARAM_STUDY_GROUP);
    public static final String ERR_TEMPLATE_ID_PARAM = String.format("The template id parameter %s must be provided.",
        ServletConstants.REQ_PARAM_TEMPLATE_ID);
    public static final String ERR_STUDY_DUP_ANIMALS = "Study groups in this study contain the following duplicate animals: %s.";

    public static final String ERR_ASSESSMENTS_GET_DATE_PARAMS = ERR_FROM_TO_DATE_PARAMS;

    public static final String ERR_SCALE_MIN_MAX = "Scale max must be more than scale min.";

    public static final String ERR_TEMPLATE_IN_USE_UPD_SCALE = "Cannot update the scale for this template because there are assessments using it.";
    public static final String ERR_TEMPLATE_IN_USE_UPD_PARAMS = "Cannot update the parameters and factors for this template because there are assessments using it.";
    public static final String ERR_TEMPLATE_IN_USE_DEL = "Cannot delete template parameter because there are assessments using it.";

    public static final String ERR_PASSWORD_RETYPED_PASSWORD = "Password does not match retyped password";

    public static final String ERR_DELETE_ADMIN_NOT_ALLOWED = "Deletion of the main admin user is not allowed";
    public static final String ERR_ADMIN_ROLE_CHANGE = "You cannot change the group that the main admin user belongs to";
    public static final String ERR_GROUP_NAME_NULL = "You must set a group name for the user";

    // Other common
    public static final String ERR_NOT_NEGATIVE_VALUES = "%s values must not be negative.";

    // Annotation templates
    // IMPORTANT: ONLY USE %s IN TEMPLATES, even for numbers.
    public static final String TEXT_SIZE_CHECK_TEMPLATE = "%s length has to be between %s and %s characters long.";

    public static final String NAME_REGEX_TEMPLATE = "%s can only contain alpha-numeric, dash, dot, forward slash, comma, bracket or underscore characters.";
    public static final String ERR_DATE_FROM_PARAM = String.format("The date from parameter %s must be provided.",
        ServletConstants.REQ_PARAM_DATE_FROM);
    public static final String ERR_DATE_TO_PARAM = String.format("The date to parameter %s must be provided.",
        ServletConstants.REQ_PARAM_DATE_TO);

    private static final String NOT_NULL_TEMPLATE = "%s must be provided.";

    private static final String VALID_ID_RANGE_TEMPLATE = "%s id must be " + ENTITY_NEG_ID + " or at least "
        + ENTITY_MIN_ID + ".";

    private static final String ENTITY_MIN_ID_TEMPLATE = "%s id must be more than or equal to " + ENTITY_MIN_ID + ".";

    private static final String SIMPLE_DATE_FORMAT_TEMPLATE = "%s must be in the format dd-mm-yyyy";

    private static final Map<Object, String> ANNOTATION_TEMPLATES = new HashMap<Object, String>();
    private static final String NON_NEGATIVE_INTEGER_TEMPLATE = "%s must be more than or equal to 0.";
    private static final String NON_ZERO_POSITIVE_INTEGER_TEMPLATE = "%s must be more than 0.";

    static
    {
        ANNOTATION_TEMPLATES.put(PropertyMustBeProvided.class, NOT_NULL_TEMPLATE);
        ANNOTATION_TEMPLATES.put(ValidIdRange.class, VALID_ID_RANGE_TEMPLATE);
        ANNOTATION_TEMPLATES.put(TextSizeLimits.class, TEXT_SIZE_CHECK_TEMPLATE);
        ANNOTATION_TEMPLATES.put(NameRegex.class, NAME_REGEX_TEMPLATE);
        ANNOTATION_TEMPLATES.put(EntityMinId.class, ENTITY_MIN_ID_TEMPLATE);
        ANNOTATION_TEMPLATES.put(SimpleDateFormatTmpl.class, SIMPLE_DATE_FORMAT_TEMPLATE);
        ANNOTATION_TEMPLATES.put(NonNegativeInteger.class, NON_NEGATIVE_INTEGER_TEMPLATE);
        ANNOTATION_TEMPLATES.put(NonZeroPositiveInteger.class, NON_ZERO_POSITIVE_INTEGER_TEMPLATE);
        // Used in unit tests
        ANNOTATION_TEMPLATES.put(TestMultiParamMessage.class, "Field value has to be %s, %s or %s");
    }

    private ValidationConstants()
    {
    }

    public static String getAnnotationTemplate(Object templateKey)
    {
        return ANNOTATION_TEMPLATES.get(templateKey);
    }

    public static class ValidScaleMinMax implements Payload
    {

    }

    public static class ValidIdRange implements Payload
    {

    }

    public static class PropertyMustBeProvided implements Payload
    {

    }

    public static class TextSizeLimits implements Payload
    {

    }

    public static class NameRegex implements Payload
    {

    }

    public static class EntityMinId implements Payload
    {

    }

    public static class SimpleDateFormatTmpl implements Payload
    {

    }

    public static class NonNegativeInteger implements Payload
    {

    }

    public static class NonZeroPositiveInteger implements Payload
    {

    }

    // Used in unit tests
    public static class TestMultiParamMessage implements Payload
    {

    }

    // Used in unit tests
    public static class TestUnconfiguredTemplate implements Payload
    {

    }
}
