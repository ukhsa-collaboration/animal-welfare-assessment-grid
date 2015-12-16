package uk.gov.phe.erdst.sc.awag.datamodel.client;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidId;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

public class StudyGroupClientData
{
    @ValidId(message = ValidationConstants.STUDY_GROUP_ENTITY_NAME, payload = ValidationConstants.ValidIdRange.class)
    @NotNull(message = ValidationConstants.STUDY_GROUP_ENTITY_NAME,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    public Long studyGroupId;

    @NotNull(message = ValidationConstants.STUDY_GROUP_NAME_PROPERTY,
        payload = ValidationConstants.PropertyMustBeProvided.class)
    @Pattern(regexp = ValidationConstants.ENTITY_NAME_REGEX, message = ValidationConstants.STUDY_GROUP_NAME_PROPERTY,
        payload = ValidationConstants.NameRegex.class)
    @Size(min = ValidationConstants.STUDY_GROUP_NAME_SIZE_MIN,
        max = ValidationConstants.STUDY_GROUP_NAME_SIZE_MAX, message = ValidationConstants.STUDY_GROUP_NAME_PROPERTY
            + "|" + ValidationConstants.STUDY_GROUP_NAME_SIZE_MIN + "|" + ValidationConstants.STUDY_GROUP_NAME_SIZE_MAX,
        payload = ValidationConstants.TextSizeLimits.class)
    public String studyGroupName;

    public Set<AnimalClientData> studyGroupAnimals = new HashSet<AnimalClientData>();

    public StudyGroupClientData()
    {
    }

    // CS:OFF: HiddenFieldCheck
    public StudyGroupClientData(Long studyGroupId, String studyGroupName)
    {
        this.studyGroupId = studyGroupId;
        this.studyGroupName = studyGroupName;
    }

    // CS:ON

    public StudyGroupClientData(Long studyGroupId)
    {
        this.studyGroupId = studyGroupId;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((studyGroupId == null) ? 0 : studyGroupId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        StudyGroupClientData other = (StudyGroupClientData) obj;
        if (studyGroupId == null)
        {
            if (other.studyGroupId != null)
            {
                return false;
            }
        }
        else if (!studyGroupId.equals(other.studyGroupId))
        {
            return false;
        }
        return true;
    }
}
