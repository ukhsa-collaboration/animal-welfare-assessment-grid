package uk.gov.phe.erdst.sc.awag.service.factory.assessment;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;

import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentReason;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentScore;
import uk.gov.phe.erdst.sc.awag.datamodel.ParameterScore;
import uk.gov.phe.erdst.sc.awag.dto.AnimalDto;
import uk.gov.phe.erdst.sc.awag.dto.AssessmentDto;
import uk.gov.phe.erdst.sc.awag.dto.AssessmentFullDto;
import uk.gov.phe.erdst.sc.awag.dto.AssessmentSearchPreviewDto;
import uk.gov.phe.erdst.sc.awag.dto.AssessmentsDto;
import uk.gov.phe.erdst.sc.awag.dto.ParameterScoredDto;
import uk.gov.phe.erdst.sc.awag.dto.PreviousAssessmentDto;
import uk.gov.phe.erdst.sc.awag.service.factory.animal.AnimalDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.housing.AnimalHousingDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.parameter.ParameterScoredDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.study.StudyDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.user.UserDtoFactory;

@Stateless
public class AssessmentDtoFactory
{
    @Inject
    private ParameterScoredDtoFactory mParameterDtoFactory;

    @Inject
    private AnimalDtoFactory mAnimalDtoFactory;

    @Inject
    private AnimalHousingDtoFactory mAnimalHousingDtoFactory;

    @Inject
    private AssessmentReasonDtoFactory mAssessmentReasonDtoFactory;

    @Inject
    private UserDtoFactory mUserDtoFactory;

    @Inject
    private StudyDtoFactory mStudyDtoFactory;

    public AssessmentsDto createAssessmentsDto(List<Assessment> assessments)
    {

        Set<AssessmentDto> assessmentDtos = new LinkedHashSet<AssessmentDto>(assessments.size());

        for (Assessment assessment : assessments)
        {
            AssessmentDto assessmentDto = createAssessmentDto(assessment);
            assessmentDtos.add(assessmentDto);
        }

        return new AssessmentsDto(assessmentDtos);
    }

    public AssessmentDto createAssessmentDto(Assessment assessment)
    {
        AssessmentDto assessmentDto = new AssessmentDto();

        // FIXME: why the null check?
        if (assessment != null)
        {
            assessmentDto.assessmentId = assessment.getId();
            assessmentDto.assessmentDate = assessment.getDate();

            setAssessmentReason(assessmentDto, assessment);
            setAssessmentScore(assessmentDto, assessment);
            setAnimal(assessmentDto, assessment);

        }

        return assessmentDto;
    }

    public AssessmentSearchPreviewDto createAssessmentSearchPreviewDto(Assessment assessment)
    {
        AssessmentSearchPreviewDto dto = new AssessmentSearchPreviewDto();

        dto.assessmentId = assessment.getId();
        dto.assessmentDate = assessment.getDate();
        dto.animal = mAnimalDtoFactory.createAnimalBasicDto(assessment.getAnimal());
        dto.isComplete = assessment.isComplete();

        dto.assessmentReason = assessment.getReason() != null ? assessment.getReason().getName() : null;
        dto.performedBy = assessment.getPerformedBy() != null ? assessment.getPerformedBy().getName() : null;

        return dto;
    }

    public AssessmentFullDto createAssessmentFullDto(Assessment assessment)
    {
        AssessmentDto simpleDto = createAssessmentDto(assessment);
        AssessmentFullDto dto = new AssessmentFullDto();

        dto.assessmentId = simpleDto.assessmentId;
        dto.animal = mAnimalDtoFactory.createAnimalDto(assessment.getAnimal());
        dto.assessmentDate = simpleDto.assessmentDate;
        dto.assessmentParameters = simpleDto.assessmentParameters;
        dto.isComplete = assessment.isComplete();

        dto.housing = assessment.getAnimalHousing() != null ? mAnimalHousingDtoFactory
            .createAnimalHousingDto(assessment.getAnimalHousing()) : null;

        dto.performedBy = assessment.getPerformedBy() != null ? mUserDtoFactory.createUserDto(assessment
            .getPerformedBy()) : null;

        dto.study = assessment.getStudy() != null ? mStudyDtoFactory.createStudySimpleDto(assessment.getStudy()) : null;

        dto.reason = assessment.getReason() != null ? mAssessmentReasonDtoFactory.createAssessmentReasonDto(assessment
            .getReason()) : null;

        return dto;
    };

    public PreviousAssessmentDto createPreviousAssessmentDto(Assessment prevAssessment)
    {
        PreviousAssessmentDto dto = new PreviousAssessmentDto();

        dto.date = prevAssessment.getDate();
        dto.isComplete = prevAssessment.isComplete();

        String paramName;
        String avgScore;
        for (ParameterScore paramScore : prevAssessment.getScore().getParametersScored())
        {
            paramName = paramScore.getParameterScored().getName();
            avgScore = String.valueOf(paramScore.getAverageScore());
            dto.scores.add(new PreviousAssessmentDto.Score(paramName, avgScore));
        }

        return dto;
    }

    private void setAssessmentReason(AssessmentDto assessmentDto, Assessment assessment)
    {
        AssessmentReason reason = assessment.getReason();
        if (reason != null)
        {
            assessmentDto.assessmentReason = reason.getName();
        }
        else
        {
            assessmentDto.assessmentReason = "";
        }
    }

    private void setAnimal(AssessmentDto assessmentDto, Assessment assessment)
    {
        // FIXME: why is there an animal null check if uk.gov.phe.erdst.sc.awag.validation prevents that?
        Animal animal = assessment.getAnimal();
        AnimalDto animalDto = new AnimalDto();
        if (animal != null)
        {
            animalDto = mAnimalDtoFactory.createAnimalBasicDto(animal);
        }

        assessmentDto.animal = animalDto;
    }

    private void setAssessmentScore(AssessmentDto assessmentDto, Assessment assessment)
    {
        AssessmentScore assessmentScore = assessment.getScore();

        Set<ParameterScoredDto> parameterDtos;

        if (assessmentScore != null)
        {
            Collection<ParameterScore> parametersScored = assessmentScore.getParametersScored();
            parameterDtos = new HashSet<ParameterScoredDto>(parametersScored.size());

            for (ParameterScore parameterScore : parametersScored)
            {
                ParameterScoredDto parameterDto = mParameterDtoFactory.createParameterDto(parameterScore);
                parameterDtos.add(parameterDto);
            }
        }
        else
        {
            parameterDtos = new HashSet<ParameterScoredDto>();
        }

        assessmentDto.assessmentParameters = parameterDtos;
    }

}
