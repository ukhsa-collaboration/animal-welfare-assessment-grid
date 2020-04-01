package uk.gov.phe.erdst.sc.awag.webapi.request;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidAssessmentScore;
import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidAssessmentScoreTemplate;
import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidDate;
import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidId;
import uk.gov.phe.erdst.sc.awag.service.validation.groups.SavedAssessment;
import uk.gov.phe.erdst.sc.awag.service.validation.groups.SubmittedAssessment;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

@ValidAssessmentScore(groups = SubmittedAssessment.class)
@ValidAssessmentScoreTemplate(groups = {SubmittedAssessment.class, SavedAssessment.class})
public class AssessmentClientData
{
    @NotNull(message = ValidationConstants.ASSESSMENT_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class,
        groups = {SubmittedAssessment.class, SavedAssessment.class})
    @ValidId(message = ValidationConstants.ASSESSMENT_ENTITY_NAME, payload = ValidationConstants.ValidIdRange.class,
        groups = {SubmittedAssessment.class, SavedAssessment.class})
    public Long id;

    @NotNull(message = ValidationConstants.ANIMAL_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class,
        groups = {SubmittedAssessment.class, SavedAssessment.class})
    @Min(value = ValidationConstants.ENTITY_MIN_ID, message = ValidationConstants.ANIMAL_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class,
        groups = {SubmittedAssessment.class, SavedAssessment.class})
    public Long animalId;

    @NotNull(message = ValidationConstants.REASON_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class, groups = {SubmittedAssessment.class})
    @Size(min = ValidationConstants.SIMPLE_TEXT_INPUT_SIZE_MIN, max = ValidationConstants.SIMPLE_TEXT_INPUT_SIZE_MAX,
        message = ValidationConstants.REASON_ENTITY_NAME + "|" + ValidationConstants.SIMPLE_TEXT_INPUT_SIZE_MIN + "|"
            + ValidationConstants.SIMPLE_TEXT_INPUT_SIZE_MAX,
        payload = ValidationConstants.TextSizeLimits.class, groups = {SubmittedAssessment.class})
    @Pattern(regexp = ValidationConstants.SIMPLE_TEXT_INPUT_REGEX, message = ValidationConstants.REASON_ENTITY_NAME,
        payload = ValidationConstants.NameRegex.class, groups = {SubmittedAssessment.class})
    public String reason;

    @NotNull(message = ValidationConstants.ANIMAL_HOUSING_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class, groups = {SubmittedAssessment.class})
    @Size(min = ValidationConstants.SIMPLE_TEXT_INPUT_SIZE_MIN, max = ValidationConstants.SIMPLE_TEXT_INPUT_SIZE_MAX,
        message = ValidationConstants.ANIMAL_HOUSING_ENTITY_NAME + "|" + ValidationConstants.SIMPLE_TEXT_INPUT_SIZE_MIN
            + "|" + ValidationConstants.SIMPLE_TEXT_INPUT_SIZE_MAX,
        payload = ValidationConstants.TextSizeLimits.class, groups = {SubmittedAssessment.class})
    @Pattern(regexp = ValidationConstants.SIMPLE_TEXT_INPUT_REGEX,
        message = ValidationConstants.ANIMAL_HOUSING_ENTITY_NAME, payload = ValidationConstants.NameRegex.class,
        groups = {SubmittedAssessment.class})
    public String animalHousing;

    @NotNull(message = ValidationConstants.USER_ENTITY_NAME, payload = ValidationConstants.PropertyMustBeProvided.class,
        groups = {SubmittedAssessment.class})
    @Size(min = ValidationConstants.SIMPLE_TEXT_INPUT_SIZE_MIN, max = ValidationConstants.SIMPLE_TEXT_INPUT_SIZE_MAX,
        message = ValidationConstants.USER_ENTITY_NAME + "|" + ValidationConstants.SIMPLE_TEXT_INPUT_SIZE_MIN + "|"
            + ValidationConstants.SIMPLE_TEXT_INPUT_SIZE_MAX,
        payload = ValidationConstants.TextSizeLimits.class, groups = {SubmittedAssessment.class})
    @Pattern(regexp = ValidationConstants.SIMPLE_TEXT_INPUT_REGEX, message = ValidationConstants.USER_ENTITY_NAME,
        payload = ValidationConstants.NameRegex.class, groups = {SubmittedAssessment.class})
    public String performedBy;

    @ValidDate(message = ValidationConstants.ASSESSMENT_DATE, payload = ValidationConstants.SimpleDateFormatTmpl.class,
        groups = {SubmittedAssessment.class, SavedAssessment.class})
    public String date;

    // Validated as part of the class on submit
    @NotNull(groups = {SavedAssessment.class})
    public Map<String, Map<String, AssessmentClientFactor>> score;

    // Validated as part of the class on submit
    @NotNull(groups = {SavedAssessment.class})
    public Map<String, Double> averageScores;

    // Validated as part of the class on submit
    @NotNull(groups = {SavedAssessment.class})
    public Map<String, String> parameterComments;

    public boolean isAllowZeroScores;

    public AssessmentClientData()
    {
    }

    public AssessmentClientData(AssessmentClientData data)
    {
        id = data.id;
        animalId = data.animalId;
        reason = data.reason;
        animalHousing = data.animalHousing;
        performedBy = data.performedBy;
        date = data.date;
        score = copyAssessmentScore(data);
        averageScores = new HashMap<String, Double>(data.averageScores);
        parameterComments = new HashMap<String, String>(data.parameterComments);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Map<String, Map<String, AssessmentClientFactor>> copyAssessmentScore(AssessmentClientData data)
    {
        Map newMap = new HashMap();

        for (Entry entry : data.score.entrySet())
        {
            newMap.put(entry.getKey(), new HashMap((Map) entry.getValue()));
        }

        return newMap;
    }

}
