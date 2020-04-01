package uk.gov.phe.erdst.sc.awag.dao;

import java.util.Collection;
import java.util.Set;

import uk.gov.phe.erdst.sc.awag.datamodel.StudyGroup;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;

public interface StudyGroupDao extends UniqueDao
{
    StudyGroup store(StudyGroup studyGroup) throws AWNonUniqueException;

    void deleteStudyGroupById(Long id) throws AWNoSuchEntityException;

    StudyGroup getStudyGroup(Long id) throws AWNoSuchEntityException;

    StudyGroup getStudyGroup(String studyGroupNumber) throws AWNoSuchEntityException; // TODO integration test

    Set<StudyGroup> getStudyGroupsLike(String like, Integer offset, Integer limit);

    Set<StudyGroup> getStudyGroups(Integer offset, Integer limit);

    Long getCountStudyGroupsLike(String like);

    Long getCountStudyGroups();

    void upload(Collection<StudyGroup> studyGroups) throws AWNonUniqueException;

}
