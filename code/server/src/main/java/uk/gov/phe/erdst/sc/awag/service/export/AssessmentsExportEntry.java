package uk.gov.phe.erdst.sc.awag.service.export;

import java.util.Set;

import uk.gov.phe.erdst.sc.awag.datamodel.StudyGroup;
import uk.gov.phe.erdst.sc.awag.dto.ParameterScoredDto;
import uk.gov.phe.erdst.sc.awag.dto.assessment.AssessmentFullDto;

public class AssessmentsExportEntry
{
    // CS:OFF: VisibilityModifier
    public StudyGroup studyGroup;
    public AssessmentFullDto dto;
    public double cwas;

    // CS:ON

    public String getAnimalNumber()
    {
        return dto.animal.animalNumber;
    }

    public String getAnimalDob()
    {
        return dto.animal.dateOfBirth;
    }

    public String getAnimalSex(String male, String female)
    {
        return dto.animal.isFemale ? female : male;
    }

    public String getAnimalSpecies()
    {
        return dto.animal.speciesName;
    }

    public String getAnimalSource()
    {
        return dto.animal.sourceName;
    }

    public String getAnimalDam()
    {
        return dto.animal.damName;
    }

    public String getAnimalFather()
    {
        return dto.animal.fatherName;
    }

    public String getStudy()
    {
        return dto.study != null ? dto.study.studyNumber : null;
    }

    public String getStudyGroup()
    {
        return studyGroup != null ? studyGroup.getStudyGroupNumber() : null;
    }

    public String getTemplate()
    {
        return dto.animal.assessmentTemplateName;
    }

    public String getAssessmentDate()
    {
        return dto.assessmentDate;
    }

    public String getReason()
    {
        return dto.reason.reasonName;
    }

    public String getPerformedBy()
    {
        return dto.performedBy.userName;
    }

    public String getHousing()
    {
        return dto.housing.housingName;
    }

    public Set<ParameterScoredDto> getScores()
    {
        return dto.assessmentParameters;
    }

}
