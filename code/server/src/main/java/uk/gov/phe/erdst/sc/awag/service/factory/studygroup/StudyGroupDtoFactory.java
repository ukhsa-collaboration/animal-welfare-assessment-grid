package uk.gov.phe.erdst.sc.awag.service.factory.studygroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;

import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.StudyGroup;
import uk.gov.phe.erdst.sc.awag.service.factory.animal.AnimalDtoFactory;
import uk.gov.phe.erdst.sc.awag.webapi.response.studygroup.StudyGroupDto;

@Stateless
public class StudyGroupDtoFactory
{
    @Inject
    private AnimalDtoFactory mAnimalDtoFactory;

    public StudyGroupDto createDto(StudyGroup studyGroup)
    {
        StudyGroupDto dto = new StudyGroupDto();
        dto.studyGroupId = studyGroup.getId();
        dto.studyGroupName = studyGroup.getStudyGroupNumber();

        for (Animal animal : studyGroup.getAnimals())
        {
            dto.studyGroupAnimals.add(mAnimalDtoFactory.createAnimalStudyGroupDto(animal));
        }

        return dto;
    }

    public Collection<StudyGroupDto> createStudyGroupDtos(Set<StudyGroup> studyGroups)
    {
        Collection<StudyGroupDto> dto = new ArrayList<>(studyGroups.size());

        for (StudyGroup studyGroup : studyGroups)
        {
            dto.add(createDto(studyGroup));
        }

        return dto;
    }
}
