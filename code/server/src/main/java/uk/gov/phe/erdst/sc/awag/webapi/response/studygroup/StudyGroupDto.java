package uk.gov.phe.erdst.sc.awag.webapi.response.studygroup;

import java.util.HashSet;
import java.util.Set;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.animal.AnimalStudyGroupDto;

public class StudyGroupDto implements ResponseDto
{
    public Long studyGroupId;
    public String studyGroupName;
    public Set<AnimalStudyGroupDto> studyGroupAnimals = new HashSet<AnimalStudyGroupDto>();

    public StudyGroupDto()
    {
    }
}
