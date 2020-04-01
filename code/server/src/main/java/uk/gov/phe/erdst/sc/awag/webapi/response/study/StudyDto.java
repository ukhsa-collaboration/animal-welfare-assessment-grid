package uk.gov.phe.erdst.sc.awag.webapi.response.study;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.studygroup.StudyGroupDto;

public class StudyDto implements ResponseDto
{
    public Long studyId;
    public String studyName;
    public boolean isStudyOpen;
    public Collection<StudyGroupDto> studyGroups;
}
