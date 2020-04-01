package uk.gov.phe.erdst.sc.awag.webapi.response.studygroup;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.paged.PagingInfo;

public class StudyGroupsDto implements ResponseDto
{
    public Collection<StudyGroupDto> studyGroups;
    public PagingInfo pagingInfo;
}
