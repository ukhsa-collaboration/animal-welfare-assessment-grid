package uk.gov.phe.erdst.sc.awag.service.scale;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.inject.Inject;

import uk.gov.phe.erdst.sc.awag.datamodel.Scale;
import uk.gov.phe.erdst.sc.awag.dto.ScaleDto;
import uk.gov.phe.erdst.sc.awag.service.factory.scale.ScaleDtoFactory;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class ScaleDtoFactoryTest
{

    private final Long SCALE_ID = 10000L;
    private final String SCALE_NAME = "Scale 1";
    private final Integer SCALE_MIN = 1;
    private final Integer SCALE_MAX = 10;

    @Inject
    private ScaleDtoFactory mScaleDtoFactory;

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    private void testCreateScaleDto()
    {
        Scale scale = new Scale();
        scale.setId(SCALE_ID);
        scale.setName(SCALE_NAME);
        scale.setMin(SCALE_MIN);
        scale.setMax(SCALE_MAX);

        ScaleDto scaleDto = mScaleDtoFactory.create(scale);

        Assert.assertNotNull(scaleDto);
        Assert.assertEquals(scaleDto.scaleId, SCALE_ID);
        Assert.assertEquals(scaleDto.scaleName, SCALE_NAME);
        Assert.assertEquals(scaleDto.scaleMin, SCALE_MIN);
        Assert.assertEquals(scaleDto.scaleMax, SCALE_MAX);
    }
}
