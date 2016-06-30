package uk.gov.phe.erdst.sc.awag.datamodel.client;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidId;
import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidStudy;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

@ValidStudy
public class StudyClientData
{
    @ValidId(message = ValidationConstants.STUDY_ENTITY_NAME, payload = ValidationConstants.ValidIdRange.class)
    @NotNull(message = ValidationConstants.STUDY_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    public Long studyId;

    @NotNull(message = ValidationConstants.STUDY_NAME_PROPERTY,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    @Pattern(regexp = ValidationConstants.ENTITY_NAME_REGEX, message = ValidationConstants.STUDY_NAME_PROPERTY,
        payload = ValidationConstants.NameRegex.class)
    @Size(min = ValidationConstants.STUDY_NAME_SIZE_MIN,
        max = ValidationConstants.STUDY_NAME_SIZE_MAX, message = ValidationConstants.STUDY_NAME_PROPERTY + "|"
            + ValidationConstants.STUDY_NAME_SIZE_MIN + "|" + ValidationConstants.STUDY_NAME_SIZE_MAX,
        payload = ValidationConstants.TextSizeLimits.class)
    public String studyName;

    public boolean isStudyOpen;

    public Set<StudyGroupClientData> studyGroups = new HashSet<StudyGroupClientData>();

    public StudyClientData()
    {
    }

    // CS:OFF: HiddenFieldCheck
    public StudyClientData(Long studyId, String studyName)
    {
        this.studyId = studyId;
        this.studyName = studyName;
    }
    // CS:ON
}
