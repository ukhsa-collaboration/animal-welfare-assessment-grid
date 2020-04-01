package uk.gov.phe.erdst.sc.awag.service.factory.study;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.inject.Inject;

import uk.gov.phe.erdst.sc.awag.datamodel.Study;
import uk.gov.phe.erdst.sc.awag.service.factory.studygroup.StudyGroupDtoFactory;
import uk.gov.phe.erdst.sc.awag.webapi.response.study.StudyDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.study.StudySimpleDto;

@Stateless
public class StudyDtoFactory
{
    @Inject
    private StudyGroupDtoFactory mStudyGroupDtoFactory;

    public StudySimpleDto createStudySimpleDto(Study study)
    {
        String studyNumber;

        // Animals don't have to be part of a study to be assessed
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

    public StudyDto createStudyDto(Study study)
    {
        StudyDto dto = new StudyDto();
        dto.studyId = study.getId();
        dto.studyName = study.getStudyNumber();
        dto.isStudyOpen = study.isOpen();
        dto.studyGroups = mStudyGroupDtoFactory.createStudyGroupDtos(study.getStudyGroups());
        return dto;
    }

    public Collection<StudyDto> createStudyDtos(Collection<Study> studies)
    {
        Collection<StudyDto> dto = new ArrayList<>(studies.size());

        for (Study study : studies)
        {
            dto.add(createStudyDto(study));
        }

        return dto;
    }
}
