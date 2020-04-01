package uk.gov.phe.erdst.sc.awag.service;

import java.util.LinkedHashSet;

import org.testng.Assert;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.service.export.ExportUtils;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.AssessmentFullDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.parameter.ParameterScoredDto;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class CwasCalculatorTest
{
    @Test
    public void testCalculation() throws Exception
    {
        // CS:OFF: LineLength|MagicNumber
        double[] avg1 = new double[] {1.00, 1.00, 1.00, 2.00, 1.60, 1.60, 1.60, 1.60, 1.60, 1.60, 1.80, 1.80, 1.60,
                1.60, 1.80, 1.60, 1.60, 1.60, 1.60, 1.80, 1.60, 1.60, 1.60, 1.60, 1.60, 1.60, 1.60, 1.60, 1.60, 1.80,
                1.80, 1.60, 1.60, 2.00, 2.00, 2.40, 2.20, 2.20, 2.20, 2.60, 2.80, 2.40, 2.40, 2.40, 2.60, 2.60, 2.60,
                2.40, 2.40, 2.40, 2.80, 2.80, 3.00, 2.60, 2.60, 3.00, 3.00};

        double[] avg2 = new double[] {1.80, 1.80, 1.80, 3.80, 3.40, 3.00, 3.40, 3.40, 3.40, 3.00, 3.00, 3.00, 3.00,
                2.80, 3.00, 3.00, 3.00, 3.00, 3.00, 3.00, 3.00, 3.00, 3.00, 3.00, 3.00, 3.00, 3.00, 3.00, 4.40, 3.60,
                3.60, 3.60, 3.20, 3.20, 3.20, 3.40, 3.40, 3.40, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20,
                3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20};

        double[] avg3 = new double[] {1.25, 1.25, 1.25, 4.50, 4.50, 4.50, 4.50, 4.50, 4.50, 4.50, 4.50, 4.50, 4.50,
                4.50, 4.50, 4.50, 4.50, 4.50, 4.50, 4.50, 4.50, 4.50, 4.50, 4.50, 4.50, 4.50, 4.50, 4.50, 7.25, 7.25,
                7.25, 7.25, 7.25, 7.25, 7.25, 7.25, 7.25, 7.25, 7.25, 7.25, 7.25, 7.25, 7.25, 7.75, 7.75, 7.75, 7.75,
                7.75, 7.75, 7.75, 7.75, 7.75, 5.75, 5.75, 5.75, 5.75, 5.75};

        double[] avg4 = new double[] {3.20, 2.60, 2.60, 4.40, 3.20, 2.00, 3.20, 3.20, 3.20, 2.00, 3.20, 3.20, 3.20,
                2.00, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 4.00, 3.20,
                3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20,
                3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20, 3.20};

        double[] expectedCwas = new double[] {5.63, 4.95, 4.95, 26.65, 20.13, 15.25, 20.13, 20.13, 20.13, 15.25, 19.53,
                19.53, 18.91, 14.64, 19.53, 18.91, 18.91, 18.91, 18.91, 19.53, 18.91, 18.91, 18.91, 18.91, 18.91, 18.91,
                18.91, 18.91, 37.17, 30.77, 30.77, 30.09, 28.32, 29.60, 29.60, 31.85, 31.19, 31.19, 30.24, 31.52, 32.16,
                30.88, 30.88, 32.48, 33.12, 33.12, 33.12, 32.48, 32.48, 32.48, 33.76, 33.76, 28.00, 26.72, 26.72, 28.00,
                28.00};

        int dataLenCheck = avg1.length;
        Assert.assertEquals(avg2.length, dataLenCheck);
        Assert.assertEquals(avg3.length, dataLenCheck);
        Assert.assertEquals(avg4.length, dataLenCheck);
        Assert.assertEquals(expectedCwas.length, dataLenCheck);

        for (int i = 0; i < dataLenCheck; i++)
        {
            AssessmentFullDto dto = getAssessmentFullDto(avg1[i], avg2[i], avg3[i], avg4[i]);
            double dcwas = CwasCalculator.calculateCwas(dto);
            String cwas = ExportUtils.formatNumericalValues(CwasCalculator.roundCwas(dcwas));
            String expected = ExportUtils.formatNumericalValues(expectedCwas[i]);
            Assert.assertEquals(cwas, expected);
        }

    }

    private AssessmentFullDto getAssessmentFullDto(double... avgScores)
    {
        AssessmentFullDto dto = new AssessmentFullDto();
        dto.assessmentParameters = new LinkedHashSet<>();

        for (int i = 0; i < avgScores.length; i++)
        {
            ParameterScoredDto parameterScoreDto = new ParameterScoredDto();
            parameterScoreDto.parameterId = new Long(i);
            parameterScoreDto.parameterAverage = avgScores[i];
            dto.assessmentParameters.add(parameterScoreDto);
        }

        return dto;
    }
}
