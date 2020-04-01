package uk.gov.phe.erdst.sc.awag.service.parameter;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.inject.Inject;

import uk.gov.phe.erdst.sc.awag.datamodel.Parameter;
import uk.gov.phe.erdst.sc.awag.datamodel.ParameterScore;
import uk.gov.phe.erdst.sc.awag.service.factory.parameter.ParameterScoredDtoFactory;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;
import uk.gov.phe.erdst.sc.awag.webapi.response.parameter.ParameterScoredDto;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class ParameterDtoFactoryTest
{
    @Inject
    private ParameterScoredDtoFactory mParameterDtoFactory;

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    public void testCreateValidParameterDto()
    {
        ParameterScore parameterScore = new ParameterScore();
        parameterScore.setId(1L);
        parameterScore.setComment("Test comment");
        parameterScore.setAverageScore(1);
        Parameter parameter = new Parameter();
        parameter.setId(1L);
        parameter.setName("Physical");
        parameterScore.setParameterScored(parameter);

        ParameterScoredDto parameterDto = mParameterDtoFactory.createParameterDto(parameterScore);

        Assert.assertEquals(parameterDto.parameterId, parameterScore.getParameterScored().getId());
        Assert.assertEquals(parameterDto.parameterName, parameterScore.getParameterScored().getName());
        Assert.assertEquals(parameterDto.parameterAverage, parameterScore.getAverageScore());
        Assert.assertEquals(parameterDto.parameterComment, parameterScore.getComment());
    }

    @Test
    public void testCreateNullParameter()
    {
        ParameterScore parameterScore = new ParameterScore();
        parameterScore.setId(1L);
        parameterScore.setScoringFactorsScored(null);
        parameterScore.setParameterScored(null);

        ParameterScoredDto parameterDto = mParameterDtoFactory.createParameterDto(parameterScore);

        Assert.assertEquals(parameterDto.parameterId, null);
        Assert.assertEquals(parameterDto.parameterName, null);
        Assert.assertEquals(parameterDto.parameterAverage, 0.0);
        Assert.assertEquals(parameterDto.parameterComment, null);
    }
}