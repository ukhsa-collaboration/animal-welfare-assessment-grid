package uk.gov.phe.erdst.sc.awag.service.housing;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.AnimalHousing;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AnimalHousingClientData;
import uk.gov.phe.erdst.sc.awag.service.factory.housing.AnimalHousingFactory;
import uk.gov.phe.erdst.sc.awag.servlets.utils.RequestConverter;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;

import com.google.inject.Inject;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class HousingFactoryTest
{
    private static final Long HOUSING_ONE_ID = 10000L;
    private static final String HOUSING_ON_NAME = "Housing 1";
    @Inject
    private RequestConverter mRequestConverter;
    @Inject
    private AnimalHousingFactory mAnimalHousingFactory;

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    public void testParseAnimalHousingClientData()
    {
        AnimalHousingClientData clientData = (AnimalHousingClientData) mRequestConverter
            .convert(TestConstants.DUMMY_NEW_HOUSING_RAW_DATA, AnimalHousingClientData.class);
        Assert.assertEquals(clientData.housingId, HOUSING_ONE_ID);
        Assert.assertEquals(clientData.housingName, HOUSING_ON_NAME);
    }

    @Test
    public void testCreateAnimalHousing()
    {
        AnimalHousingClientData clientData = new AnimalHousingClientData(HOUSING_ONE_ID, HOUSING_ON_NAME);
        AnimalHousing animalHousing = mAnimalHousingFactory.create(clientData);

        Assert.assertEquals(animalHousing.getName(), clientData.housingName);
    }

    @Test
    public void testUpdateAnimalHousing()
    {
        AnimalHousingClientData clientData = new AnimalHousingClientData(HOUSING_ONE_ID, HOUSING_ON_NAME);
        AnimalHousing animalHousing = new AnimalHousing();
        mAnimalHousingFactory.update(animalHousing, clientData);
        Assert.assertEquals(animalHousing.getId(), clientData.housingId);
        Assert.assertEquals(animalHousing.getName(), clientData.housingName);
    }
}
