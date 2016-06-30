package uk.gov.phe.erdst.sc.awag.service.extractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.service.extractor.assessment.AssessmentUniqueValuesExtractor;
import uk.gov.phe.erdst.sc.awag.service.extractor.assessment.AssessmentUniqueValuesExtractor.DatesBorderValues;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;

import com.google.inject.Inject;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class AssessmentUniqueValuesExtractorTest
{

    private static final String EXPECTED_END_DATE = "2014-08-07T00:00:00.000Z";

    private static final String EXPECTED_START_DATE = "2014-08-01T00:00:00.000Z";

    @Inject
    private AssessmentUniqueValuesExtractor mExtractor;

    private List<String> mTestDates;

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
        // CS:OFF: MagicNumber
        Collection<Integer> days = new ArrayList<>(Arrays.asList(new Integer[] {1, 2, 3, 4, 5, 6, 7}));
        // CS:ON
        mTestDates = new ArrayList<>(days.size());

        for (Integer day : days)
        {
            mTestDates.add(String.format("%02d", day));
        }
    }

    @Test
    public void testDateBoderExtractionOrderedValues()
    {
        final String expectedStartDate = EXPECTED_START_DATE;
        final String expectedEndDate = EXPECTED_END_DATE;
        Collection<Assessment> assessments = createAssessmentWithDates(mTestDates);

        DatesBorderValues values = mExtractor.extractDatesBorderValues(assessments);
        Assert.assertEquals(values.dateOfFirstAssessment, expectedStartDate);
        Assert.assertEquals(values.dateOfLastAssessment, expectedEndDate);
    }

    @Test
    public void testDateBoderExtractionReverseOrderValues()
    {
        final String expectedStartDate = EXPECTED_START_DATE;
        final String expectedEndDate = EXPECTED_END_DATE;

        Collections.reverse(mTestDates);

        Collection<Assessment> assessments = createAssessmentWithDates(mTestDates);

        DatesBorderValues values = mExtractor.extractDatesBorderValues(assessments);
        Assert.assertEquals(values.dateOfFirstAssessment, expectedStartDate);
        Assert.assertEquals(values.dateOfLastAssessment, expectedEndDate);
    }

    @Test
    public void testDateBoderExtractionRandomOrderValues()
    {
        final String expectedStartDate = EXPECTED_START_DATE;
        final String expectedEndDate = EXPECTED_END_DATE;

        Collections.shuffle(mTestDates);

        Collection<Assessment> assessments = createAssessmentWithDates(mTestDates);

        DatesBorderValues values = mExtractor.extractDatesBorderValues(assessments);
        Assert.assertEquals(values.dateOfFirstAssessment, expectedStartDate);
        Assert.assertEquals(values.dateOfLastAssessment, expectedEndDate);
    }

    @Test
    public void testDateBoderExtractionIdenticalValues()
    {
        final String expectedStartDate = EXPECTED_START_DATE;
        final String expectedEndDate = expectedStartDate;

        String date = mTestDates.get(0);
        for (int i = 0; i < mTestDates.size(); i++)
        {
            mTestDates.set(i, date);
        }

        Collection<Assessment> assessments = createAssessmentWithDates(mTestDates);

        DatesBorderValues values = mExtractor.extractDatesBorderValues(assessments);
        Assert.assertEquals(values.dateOfFirstAssessment, expectedStartDate);
        Assert.assertEquals(values.dateOfLastAssessment, expectedEndDate);
    }

    @Test
    public void testDateBoderExtractionNoValues()
    {
        final String expectedStartDate = null;
        final String expectedEndDate = null;

        mTestDates.clear();

        Collection<Assessment> assessments = createAssessmentWithDates(mTestDates);

        DatesBorderValues values = mExtractor.extractDatesBorderValues(assessments);
        Assert.assertEquals(values.dateOfFirstAssessment, expectedStartDate);
        Assert.assertEquals(values.dateOfLastAssessment, expectedEndDate);
    }

    private Collection<Assessment> createAssessmentWithDates(List<String> days)
    {
        Collection<Assessment> assessments = new ArrayList<>(days.size());
        for (String day : days)
        {
            Assessment a = new Assessment();
            a.setDate("2014-08-" + day + "T00:00:00.000Z");
            assessments.add(a);
        }

        return assessments;
    }
}
