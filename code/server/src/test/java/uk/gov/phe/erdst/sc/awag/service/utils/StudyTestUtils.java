package uk.gov.phe.erdst.sc.awag.service.utils;

import java.util.HashSet;
import java.util.Set;

import uk.gov.phe.erdst.sc.awag.datamodel.Study;
import uk.gov.phe.erdst.sc.awag.datamodel.StudyGroup;

public final class StudyTestUtils
{
    private static final String TEST_STUDY_NUMBER = "Study 1";
    private static final Long TEST_STUDY_GROUP_IN_STUDY = 10000L;

    private StudyTestUtils()
    {
    }

    public static Study getStudyWithoutStudyGroups()
    {
        Study study = new Study();
        setProperties(study);
        return study;
    }

    public static Study getStudyWithStudyGroups()
    {
        Study study = new Study();
        setProperties(study);
        StudyGroup studyGroup = new StudyGroup(TEST_STUDY_GROUP_IN_STUDY);
        Set<StudyGroup> studyGroups = new HashSet<StudyGroup>(1);
        studyGroups.add(studyGroup);
        study.setStudyGroups(studyGroups);
        return study;
    }

    private static void setProperties(Study study)
    {
        study.setStudyNumber(TEST_STUDY_NUMBER);
        study.setIsOpen(false);
    }
}
