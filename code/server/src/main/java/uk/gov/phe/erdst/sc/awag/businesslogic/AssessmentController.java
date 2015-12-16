package uk.gov.phe.erdst.sc.awag.businesslogic;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.response.ResponsePayload;
import uk.gov.phe.erdst.sc.awag.dto.AssessmentFullDto;
import uk.gov.phe.erdst.sc.awag.dto.AssessmentSearchPreviewDto;
import uk.gov.phe.erdst.sc.awag.dto.AssessmentsDto;
import uk.gov.phe.erdst.sc.awag.dto.PreviousAssessmentDto;

public interface AssessmentController
{
    void store(AssessmentClientData clientData, boolean isSubmit, ResponsePayload responsePayload);

    PreviousAssessmentDto getPreviousAssessment(Long animalId);

    AssessmentsDto getAnimalAssessmentsBetween(String dateFrom, String dateTo, Long animalId, Integer offset,
        Integer limit, ResponsePayload responsePayload, boolean includeMetadata);

    Collection<Assessment> getAssessments(Integer offset, Integer limit);

    Collection<Assessment> getAssessments(Long animalId, String dateFrom, String dateTo, Long userId, Long reasonId,
        Long studyId, Boolean isComplete, Integer offset, Integer limit);

    Collection<AssessmentSearchPreviewDto> getAssessmentsPreviewDtos(Long animalId, String dateFrom, String dateTo,
        Long userId, Long reasonId, Long studyId, Boolean isComplete, Integer offset, Integer limit,
        ResponsePayload responsePayload, boolean includeMetadata);

    PreviousAssessmentDto getPreviousAssessmentByDate(Long animalId, String date, Long currentAssessmentId);

    void update(Long assessmentId, AssessmentClientData clientData, boolean isSubmit, ResponsePayload responsePayload);

    AssessmentFullDto getAssessmentFullDto(Long assessmentId, ResponsePayload responsePayload);

    Long getAssessmentsCount();

    Long getAssessmentsCountByAnimalId(Long animalId);

    Long getAssessmentsCountByTemplateId(Long templateId);
}
