package uk.gov.phe.erdst.sc.awag.service.factory.animal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.Sex;
import uk.gov.phe.erdst.sc.awag.dto.AnimalDto;

@Stateless
public class AnimalDtoFactory
{
    public List<AnimalDto> createAnimalDtos(Collection<Animal> animals)
    {
        List<AnimalDto> dtos = new ArrayList<AnimalDto>(animals.size());
        for (Animal animal : animals)
        {
            AnimalDto dto = createAnimalDto(animal);
            dtos.add(dto);
        }

        return dtos;
    }

    public AnimalDto createAnimalBasicDto(Animal animal)
    {
        AnimalDto dto = new AnimalDto();
        dto.id = animal.getId();
        dto.animalNumber = animal.getAnimalNumber();
        return dto;
    }

    public AnimalDto createAnimalDto(Animal animal)
    {
        AnimalDto dto = new AnimalDto();
        dto.id = animal.getId();
        dto.animalNumber = animal.getAnimalNumber();
        dto.dateOfBirth = animal.getDateOfBirth();

        if (animal.getDam() != null)
        {
            dto.damId = animal.getDam().getId();
            dto.damName = animal.getDam().getAnimalNumber();
        }
        if (animal.getFather() != null)
        {
            dto.fatherId = animal.getFather().getId();
            dto.fatherName = animal.getFather().getAnimalNumber();
        }

        if (animal.getAssessmentTemplate() != null)
        {
            dto.assessmentTemplateId = animal.getAssessmentTemplate().getId();
            dto.assessmentTemplateName = animal.getAssessmentTemplate().getName();
        }

        if (animal.getSpecies() != null)
        {
            dto.speciesId = animal.getSpecies().getId();
            dto.speciesName = animal.getSpecies().getName();
        }

        if (animal.getSource() != null)
        {
            dto.sourceId = animal.getSource().getId();
            dto.sourceName = animal.getSource().getName();
        }

        if (animal.getSex() != null)
        {
            dto.sexId = animal.getSex().getId();
            dto.isFemale = Sex.FEMALE.equals(animal.getSex().getName());
        }

        dto.isAlive = animal.isAlive();
        dto.isAssessed = animal.isAssessed();

        return dto;
    }
}
