package uk.gov.phe.erdst.sc.awag.validation.assessment;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.deprecated.RequestConverter;
import uk.gov.phe.erdst.sc.awag.service.validation.impl.AssessmentScoreCalculator;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.AssessmentClientFactor;

import com.google.inject.Inject;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class AssessmentScoreCalculatorTest
{
    private static final String PARAMETER_1_ID = "10000";
    private static final double ACCEPTABLE_DELTA = 0.01d;

    @Inject
    private RequestConverter mRequestConverter;

    private AssessmentScoreCalculator mCalculator;

    @BeforeMethod
    private void setUp()
    {
        GuiceHelper.injectTestDependencies(this);
        mCalculator = new AssessmentScoreCalculator();
    }

    @Test
    public void testCorrectAverageDecimal()
    {
        // CS:OFF: MagicNumber
        int[] scores = new int[] {3, 1, 2, 3};
        boolean[] ignored = new boolean[] {false, false, false, false};

        AssessmentClientData data = setScores(getScores(scores, ignored));
        Double average = mCalculator.calculateParametersAverages(data).get(PARAMETER_1_ID);

        Assert.assertEquals(average, 2.25d, ACCEPTABLE_DELTA);
        // CS:ON
    }

    @Test
    public void testCorrectAverageDecimalRecursion()
    {
        // CS:OFF: MagicNumber
        int[] scores = new int[] {4, 4, 2};
        boolean[] ignored = new boolean[] {false, false, false};

        AssessmentClientData data = setScores(getScores(scores, ignored));
        Double average = mCalculator.calculateParametersAverages(data).get(PARAMETER_1_ID);

        Assert.assertEquals(average, 3.33d, ACCEPTABLE_DELTA);
        // CS:ON
    }

    @Test
    public void testCorrectAverageInteger()
    {
        // CS:OFF: MagicNumber
        int[] scores = new int[] {6, 6, 6};
        boolean[] ignored = new boolean[] {false, false, false};

        AssessmentClientData data = setScores(getScores(scores, ignored));
        Double average = mCalculator.calculateParametersAverages(data).get(PARAMETER_1_ID);

        Assert.assertEquals(average, 6d, 0);
        // CS:ON
    }

    @Test
    public void testAllIgnored()
    {
        // CS:OFF: MagicNumber
        int[] scores = new int[] {6, 6, 6};
        boolean[] ignored = new boolean[] {true, true, true};

        AssessmentClientData data = setScores(getScores(scores, ignored));
        Double average = mCalculator.calculateParametersAverages(data).get(PARAMETER_1_ID);

        Assert.assertEquals(average, 0d, 0);
        // CS:ON
    }

    @Test
    public void testSingleIgnored()
    {
        // CS:OFF: MagicNumber
        int[] scores = new int[] {6, 6, 4};
        boolean[] ignored = new boolean[] {false, true, false};

        AssessmentClientData data = setScores(getScores(scores, ignored));
        Double average = mCalculator.calculateParametersAverages(data).get(PARAMETER_1_ID);

        Assert.assertEquals(average, 5d, 0);
        // CS:ON
    }

    private AssessmentClientData setScores(List<AssessmentClientFactor> scores)
    {
        AssessmentClientData data = getValidAssessmentClientData();

        data.score.get(PARAMETER_1_ID).clear();
        for (int i = 0; i < scores.size(); i++)
        {
            data.score.get(PARAMETER_1_ID).put(String.valueOf(i), scores.get(i));
        }

        return data;
    }

    private List<AssessmentClientFactor> getScores(int[] scores, boolean[] ignored)
    {
        List<AssessmentClientFactor> factorsScores = new ArrayList<AssessmentClientFactor>();

        for (int i = 0; i < scores.length; i++)
        {
            AssessmentClientFactor factor = new AssessmentClientFactor();
            factor.score = scores[i];
            factor.isIgnored = ignored[i];
            factorsScores.add(factor);
        }

        return factorsScores;
    }

    private AssessmentClientData getValidAssessmentClientData()
    {
        return (AssessmentClientData) mRequestConverter.convert(TestConstants.DUMMY_ASSESSMENT_RAW_DATA,
            AssessmentClientData.class);
    }

}
