package uk.gov.phe.erdst.sc.awag.service.extractor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.datamodel.Study;
import uk.gov.phe.erdst.sc.awag.datamodel.StudyGroup;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDtoCollection;
import uk.gov.phe.erdst.sc.awag.service.utils.StudyGroupTestUtils;
import uk.gov.phe.erdst.sc.awag.service.utils.StudyTestUtils;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class AssessmentUniqueStudyGroupsExtractorTest extends AssessmentUniqueEntitiesExtractorTestTemplate
{
    private static final String TEST_STUDY_2_NUMBER = "Study2";
    private static final String TEST_STUDY_GROUP_3_NUMBER = "StudyGroup3";
    private static final String TEST_STUDY_GROUP_2_NUMBER = "StudyGroup2";
    private static final String TEST_STUDY_GROUP_1_NUMBER = "StudyGroup1";
    private static final String TEST_STUDY_1_NUMBER = "Study1";

    @Override
    @BeforeMethod
    public void setUp() throws Exception
    {
        super.setUp();
    }

    @Override
    protected Class<?> getTestEntityClass()
    {
        return StudyGroup.class;
    }

    @Test
    public void testSingleStudySingleGroup()
    {
        Collection<Assessment> data = new ArrayList<>();
        Collection<TestDataRecord> testRecords = new ArrayList<>();

        Map<Long, String> idsNumbers = new HashMap<>();

        final int numberOfAssessments = 10;

        String studyNumber = TEST_STUDY_1_NUMBER;
        testRecords.add(new TestDataRecord(TEST_STUDY_GROUP_1_NUMBER, 0L));

        for (int i = 0; i < numberOfAssessments; i++)
        {
            data.add(createTestData(studyNumber, testRecords, idsNumbers));
        }

        EntitySelectDtoCollection extractedValues = mExtractor.extract(data, StudyGroup.class, mEntitySelectDtoFactory);

        final int expectedGroups = 1;
        Assert.assertEquals(extractedValues.values.size(), expectedGroups);

        for (EntitySelectDto dto : extractedValues.values)
        {
            Assert.assertTrue(idsNumbers.containsKey(dto.id));
        }
    }

    @Test
    public void testSingleStudyMultipleGroups()
    {
        Collection<Assessment> data = new ArrayList<>();
        Collection<TestDataRecord> testRecords = new ArrayList<>();

        final int numberOfAssessments = 10;

        Map<Long, String> idsNumbers = new HashMap<>();

        String studyNumber = TEST_STUDY_1_NUMBER;
        testRecords.add(new TestDataRecord(TEST_STUDY_GROUP_1_NUMBER, 0L));
        testRecords.add(new TestDataRecord(TEST_STUDY_GROUP_2_NUMBER, 1L));
        testRecords.add(new TestDataRecord(TEST_STUDY_GROUP_3_NUMBER, 2L));

        for (int i = 0; i < numberOfAssessments; i++)
        {
            data.add(createTestData(studyNumber, testRecords, idsNumbers));
        }

        EntitySelectDtoCollection extractedValues = mExtractor.extract(data, StudyGroup.class, mEntitySelectDtoFactory);

        final int expectedGroups = 3;
        Assert.assertEquals(extractedValues.values.size(), expectedGroups);

        for (EntitySelectDto dto : extractedValues.values)
        {
            Assert.assertTrue(idsNumbers.containsKey(dto.id));
        }
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void testAssessmentsFromDifferentStudies()
    {
        Collection<Assessment> data = new ArrayList<>();
        Collection<TestDataRecord> testRecords = new ArrayList<>();

        final int numberOfAssessments = 1;

        Map<Long, String> idsNumbers = new HashMap<>();

        String studyNumber = TEST_STUDY_1_NUMBER;
        testRecords.add(new TestDataRecord(TEST_STUDY_GROUP_1_NUMBER, 0L));

        for (int i = 0; i < numberOfAssessments; i++)
        {
            data.add(createTestData(0L, studyNumber, testRecords, idsNumbers));
        }

        studyNumber = TEST_STUDY_2_NUMBER;
        testRecords.add(new TestDataRecord(TEST_STUDY_GROUP_2_NUMBER, 1L));

        for (int i = 0; i < numberOfAssessments; i++)
        {
            data.add(createTestData(1L, studyNumber, testRecords, idsNumbers));
        }

        mExtractor.extract(data, StudyGroup.class, mEntitySelectDtoFactory);
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void testNullStudy()
    {
        Collection<Assessment> data = new ArrayList<>();
        data.add(new Assessment());

        mExtractor.extract(data, StudyGroup.class, mEntitySelectDtoFactory);
    }

    private Assessment createTestData(String studyNumber, Collection<TestDataRecord> testData,
        Map<Long, String> idsNumbers)
    {
        return createTestData(0L, studyNumber, testData, idsNumbers);
    }

    private Assessment createTestData(Long studyId, String studyNumber, Collection<TestDataRecord> testData,
        Map<Long, String> idsNumbers)
    {
        Set<StudyGroup> groups = new HashSet<>();

        for (TestDataRecord record : testData)
        {
            StudyGroup group = StudyGroupTestUtils.getStudyGroupWithoutAnimals(record.studyGroupId,
                record.studyGroupNumber);
            groups.add(group);

            idsNumbers.put(record.studyGroupId, record.studyGroupNumber);
        }

        Study study = StudyTestUtils.getStudyWithoutStudyGroups();
        study.setId(studyId);
        study.setStudyNumber(studyNumber);
        study.setStudyGroups(groups);

        Assessment assessment = new Assessment();
        assessment.setStudy(study);

        return assessment;
    }

    @Override
    protected Assessment createTestAssessment(Long entityId, String entityNumber)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void nullifyTestEntity(Assessment assessment)
    {
        throw new UnsupportedOperationException();
    }

    // CS:OFF: MemberName|HiddenField
    private static class TestDataRecord
    {
        private final String studyGroupNumber;
        private final Long studyGroupId;

        public TestDataRecord(String studyGroupNumber, Long studyGroupId)
        {
            this.studyGroupNumber = studyGroupNumber;
            this.studyGroupId = studyGroupId;
        }
    }
    // CS:ON

}
