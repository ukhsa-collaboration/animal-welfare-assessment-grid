package uk.gov.phe.erdst.sc.awag.service.factory.study;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.Study;
import uk.gov.phe.erdst.sc.awag.dto.StudySimpleDto;

@Stateless
public class StudyDtoFactory
{
    public StudySimpleDto createStudySimpleDto(Study study)
    {
        String studyNumber;

        if (study == null)
        {
            studyNumber = "";
        }
        else
        {
            studyNumber = study.getStudyNumber();
        }

        return new StudySimpleDto(studyNumber);
    }
}
