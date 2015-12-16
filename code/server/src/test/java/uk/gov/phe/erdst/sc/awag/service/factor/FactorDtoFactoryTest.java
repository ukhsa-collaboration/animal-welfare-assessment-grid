package uk.gov.phe.erdst.sc.awag.service.factor;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.Factor;
import uk.gov.phe.erdst.sc.awag.datamodel.FactorScored;
import uk.gov.phe.erdst.sc.awag.dto.FactorScoredDto;
import uk.gov.phe.erdst.sc.awag.service.factory.factor.FactorScoredDtoFactory;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;

import com.google.inject.Inject;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class FactorDtoFactoryTest
{
    @Inject
    private FactorScoredDtoFactory mFactorDtoFactory;

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    public void testCreateNullScoringFactor()
    {
        FactorScored factorScored = new FactorScored();
        factorScored.setScore(1);
        factorScored.setScoringFactor(null);

        FactorScoredDto factorDto = mFactorDtoFactory.createFactorDto(factorScored);

        Assert.assertEquals(factorDto.factorId, null);
        Assert.assertEquals(factorDto.factorName, null);
        Assert.assertEquals(factorDto.factorScore, null);
    }

    @Test
    public void testCreateValidFactorDtoTest()
    {
        FactorScored factorScored = new FactorScored();
        Factor factor = new Factor();
        factor.setId(1L);
        factor.setName("Water deprivation");
        factorScored.setScoringFactor(factor);
        factorScored.setScore(1);
        factorScored.setIsIgnored(true);

        FactorScoredDto factorDto = mFactorDtoFactory.createFactorDto(factorScored);

        Assert.assertEquals(factorDto.factorId, factorScored.getScoringFactor().getId());
        Assert.assertEquals(factorDto.factorName, factorScored.getScoringFactor().getName());
        Assert.assertEquals(factorDto.factorScore, Long.valueOf(factorScored.getScore()));
        Assert.assertTrue(factorDto.isIgnored);
    }
}
