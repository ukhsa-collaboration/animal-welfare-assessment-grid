package uk.gov.phe.erdst.sc.awag.service.species;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.inject.Inject;

import uk.gov.phe.erdst.sc.awag.dao.species.SpeciesDaoImplTest;
import uk.gov.phe.erdst.sc.awag.datamodel.Species;
import uk.gov.phe.erdst.sc.awag.datamodel.client.SpeciesClientData;
import uk.gov.phe.erdst.sc.awag.service.factory.species.SpeciesFactory;
import uk.gov.phe.erdst.sc.awag.servlets.utils.RequestConverter;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class SpeciesFactoryTest
{
    @Inject
    private RequestConverter mRequestConverter;

    @Inject
    private SpeciesFactory mSpeciesFactory;

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    public void testCreate()
    {
        SpeciesClientData clientData = (SpeciesClientData) mRequestConverter
            .convert(TestConstants.DUMMY_NEW_SPECIES_RAW_DATA, SpeciesClientData.class);
        Species species = mSpeciesFactory.create(clientData);

        assertNonIdProperties(species, clientData);
    }

    @Test
    public void testUpdate()
    {
        Species speciesToUpdate = new Species(SpeciesDaoImplTest.SPECIES_ONE_ID);

        SpeciesClientData speciesClientData = (SpeciesClientData) mRequestConverter
            .convert(TestConstants.DUMMY_UPDATE_ANIMAL_RAW_DATA, SpeciesClientData.class);

        mSpeciesFactory.update(speciesToUpdate, speciesClientData);

        assertNonIdProperties(speciesToUpdate, speciesClientData);
    }

    private void assertNonIdProperties(Species species, SpeciesClientData clientData)
    {
        Assert.assertEquals(species.getName(), clientData.speciesName);
    }

}
