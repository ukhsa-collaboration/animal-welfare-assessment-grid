package uk.gov.phe.erdst.sc.awag.service.factory.assessment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;

import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentReason;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentScore;
import uk.gov.phe.erdst.sc.awag.datamodel.ParameterScore;
import uk.gov.phe.erdst.sc.awag.datamodel.utils.ParametersOrdering;
import uk.gov.phe.erdst.sc.awag.service.factory.animal.AnimalDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.housing.AnimalHousingDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.parameter.ParameterScoredDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.study.StudyDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.factory.user.UserDtoFactory;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.webapi.response.animal.AnimalDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.AssessmentFullDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.AssessmentMinimalDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.AssessmentSearchResultDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.AssessmentSearchResultWrapperDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.AssessmentSimpleDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.AssessmentSimpleWrapperDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.PreviousAssessmentDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.parameter.ParameterScoredDto;

// CS:OFF: ClassDataAbstractionCoupling
@Stateless
public class AssessmentDtoFactory
// CS:ON
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

    public Collection<AssessmentMinimalDto> createMinimalAssessmentsDto(Collection<Assessment> assessments)
    {
        Collection<AssessmentMinimalDto> dtos = new ArrayList<>(assessments.size());

        for (Assessment assessment : assessments)
        {
            AssessmentMinimalDto dto = new AssessmentMinimalDto();
            dto.id = assessment.getId();
            dto.entityName = assessment.getAnimal().getAnimalNumber() + "|" + assessment.getDate();
            dtos.add(dto);
        }

        return dtos;
    }

    public AssessmentSimpleWrapperDto createSimpleAssessmentsDto(List<Assessment> assessments,
        ParametersOrdering ordering)
    {

        Set<AssessmentSimpleDto> assessmentDtos = new LinkedHashSet<>(assessments.size());

        for (Assessment assessment : assessments)
        {
            AssessmentSimpleDto assessmentDto = createSimpleAssessmentDto(assessment, ordering);
            assessmentDtos.add(assessmentDto);
        }

        return new AssessmentSimpleWrapperDto(assessmentDtos);
    }

    public AssessmentSimpleDto createSimpleAssessmentDto(Assessment assessment, ParametersOrdering ordering)
    {
        AssessmentSimpleDto assessmentDto = new AssessmentSimpleDto();

        assessmentDto.assessmentId = assessment.getId();
        assessmentDto.assessmentDate = assessment.getDate();

        setAssessmentReason(assessmentDto, assessment);
        setAssessmentScore(assessmentDto, assessment, ordering);
        setAnimal(assessmentDto, assessment);

        return assessmentDto;
    }

    public AssessmentFullDto createAssessmentFullDto(Assessment assessment, ParametersOrdering ordering)
    {
        AssessmentSimpleDto simpleDto = createSimpleAssessmentDto(assessment, ordering);
        AssessmentFullDto dto = new AssessmentFullDto();

        dto.assessmentId = simpleDto.assessmentId;
        dto.animal = mAnimalDtoFactory.createAnimalDto(assessment.getAnimal());
        dto.assessmentDate = simpleDto.assessmentDate;
        dto.assessmentParameters = simpleDto.assessmentParameters;
        dto.isComplete = assessment.isComplete();

        dto.housing = assessment.getAnimalHousing() != null
            ? mAnimalHousingDtoFactory.createAnimalHousingDto(assessment.getAnimalHousing())
            : null;

        dto.performedBy = assessment.getPerformedBy() != null
            ? mUserDtoFactory.createUserDto(assessment.getPerformedBy())
            : null;

        dto.study = assessment.getStudy() != null ? mStudyDtoFactory.createStudySimpleDto(assessment.getStudy()) : null;

        dto.reason = assessment.getReason() != null
            ? mAssessmentReasonDtoFactory.createAssessmentReasonDto(assessment.getReason())
            : null;

        return dto;
    }

    public AssessmentSearchResultDto createAssessmentSearchResultDto(Assessment assessment)
    {
        AssessmentSearchResultDto dto = new AssessmentSearchResultDto();

        dto.assessmentId = assessment.getId();
        dto.assessmentDate = assessment.getDate();
        dto.animal = mAnimalDtoFactory.createAnimalBasicDto(assessment.getAnimal());
        dto.isComplete = assessment.isComplete();

        dto.assessmentReason = assessment.getReason() != null ? assessment.getReason().getName() : null;
        dto.performedBy = assessment.getPerformedBy() != null ? assessment.getPerformedBy().getName() : null;

        return dto;
    }

    public AssessmentSearchResultWrapperDto createAssessmentSearchResultWrapperDto(Collection<Assessment> assessments)
    {
        Set<AssessmentSearchResultDto> assessmentDtos = new LinkedHashSet<>(assessments.size());

        for (Assessment assessment : assessments)
        {
            AssessmentSearchResultDto assessmentDto = createAssessmentSearchResultDto(assessment);
            assessmentDtos.add(assessmentDto);
        }

        return new AssessmentSearchResultWrapperDto(assessmentDtos);
    }

    public PreviousAssessmentDto createPreviousAssessmentDto(Assessment prevAssessment,
        ParametersOrdering parametersOrdering)
    {
        PreviousAssessmentDto dto = new PreviousAssessmentDto();

        HashMap<Long, PreviousAssessmentDto.Score> prevAssessmentScoreDtoMap = new LinkedHashMap<>();
        for (Long parameterId : parametersOrdering.getParameterIdList())
        {
            prevAssessmentScoreDtoMap.put(parameterId, null);
        }

        dto.date = prevAssessment.getDate();
        dto.isComplete = prevAssessment.isComplete();

        String paramName;
        String avgScore;
        for (ParameterScore paramScore : prevAssessment.getScore().getParametersScored())
        {
            paramName = paramScore.getParameterScored().getName();
            avgScore = String.valueOf(paramScore.getAverageScore());
            PreviousAssessmentDto.Score score = new PreviousAssessmentDto.Score(paramName, avgScore);
            prevAssessmentScoreDtoMap.put(paramScore.getParameterScored().getEntitySelectId(), score);
        }

        dto.scores.addAll(new ArrayList<>(prevAssessmentScoreDtoMap.values()));

        return dto;
    }

    private void setAssessmentReason(AssessmentSimpleDto assessmentDto, Assessment assessment)
    {
        AssessmentReason reason = assessment.getReason();
        if (reason != null)
        {
            assessmentDto.assessmentReason = reason.getName();
        }
        else
        {
            assessmentDto.assessmentReason = Constants.EMPTY_STRING;
        }
    }

    private void setAnimal(AssessmentSimpleDto assessmentDto, Assessment assessment)
    {
        AnimalDto animalDto = mAnimalDtoFactory.createAnimalBasicDto(assessment.getAnimal());
        assessmentDto.animal = animalDto;
    }

    private void setAssessmentScore(AssessmentSimpleDto assessmentDto, Assessment assessment,
        ParametersOrdering parametersOrdering)
    {
        AssessmentScore assessmentScore = assessment.getScore();

        Set<ParameterScoredDto> parameterDtos;

        if (assessmentScore != null)
        {
            Collection<ParameterScore> parametersScored = assessmentScore.getParametersScored();

            // TODO simplify as a template
            HashMap<Long, ParameterScoredDto> parameterDtoMap = new LinkedHashMap<>();
            for (Long parameterId : parametersOrdering.getParameterIdList())
            {
                parameterDtoMap.put(parameterId, null);
            }

            for (ParameterScore parameterScore : parametersScored)
            {
                ParameterScoredDto parameterDto = mParameterDtoFactory.createParameterDto(parameterScore);
                parameterDtoMap.put(parameterDto.parameterId, parameterDto);
            }

            parameterDtos = new LinkedHashSet<>(parameterDtoMap.values());
        }
        else
        {
            parameterDtos = new HashSet<ParameterScoredDto>();
        }

        assessmentDto.assessmentParameters = parameterDtos;
    }
}
