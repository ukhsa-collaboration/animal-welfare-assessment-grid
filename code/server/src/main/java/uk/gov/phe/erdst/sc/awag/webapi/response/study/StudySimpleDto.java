package uk.gov.phe.erdst.sc.awag.webapi.response.study;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;

public final class StudySimpleDto implements ResponseDto
{
    public final String studyNumber;

    public StudySimpleDto(String studyNumber)
    {
        this.studyNumber = studyNumber;
    }
}
