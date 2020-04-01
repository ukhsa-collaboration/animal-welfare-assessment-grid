package uk.gov.phe.erdst.sc.awag.service.studygroup;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.StudyGroup;
import uk.gov.phe.erdst.sc.awag.deprecated.RequestConverter;
import uk.gov.phe.erdst.sc.awag.service.factory.studygroup.StudyGroupFactory;
import uk.gov.phe.erdst.sc.awag.service.utils.StudyGroupTestUtils;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;
import uk.gov.phe.erdst.sc.awag.webapi.request.AnimalClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.StudyGroupClientData;

import com.google.inject.Inject;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class StudyGroupFactoryTest
{
    @Inject
    private RequestConverter mRequestConverter;

    @Inject
    private StudyGroupFactory mStudyGroupFactory;

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    private StudyGroupClientData getStudyGroupClientData(String rawClientData)
    {
        StudyGroupClientData clientData = (StudyGroupClientData) mRequestConverter.convert(rawClientData,
            StudyGroupClientData.class);
        return clientData;
    }

    @Test
    private void testUpdateAddAnimalsInStudyGroup()
    {
        StudyGroupClientData clientData = getStudyGroupClientData(TestConstants.DUMMY_UPDATE_SG_ADD_ANIMALS_RAW_DATA);
        StudyGroup studyGroupToUpdate = StudyGroupTestUtils.getStudyGroupWithoutAnimals();
        mStudyGroupFactory.update(studyGroupToUpdate, clientData);
        assertNonIdProperties(studyGroupToUpdate, clientData);
        Assert.assertNotNull(studyGroupToUpdate.getAnimals());
        Assert.assertEquals(studyGroupToUpdate.getAnimals().size(), 2);
        for (AnimalClientData animalClientData : clientData.studyGroupAnimals)
        {
            Assert.assertTrue(studyGroupToUpdate.getAnimals().contains(new Animal(animalClientData.id)));
        }
    }

    @Test
    private void testUpdateRemoveAnimalsInStudyGroup()
    {
        // CS:OFF: LineLength
        StudyGroupClientData clientData = getStudyGroupClientData(
            TestConstants.DUMMY_UPDATE_SG_REMOVE_ANIMALS_RAW_DATA);
        // CS:ON
        StudyGroup studyGroupToUpdate = StudyGroupTestUtils.getStudyGroupWithAnimals();
        mStudyGroupFactory.update(studyGroupToUpdate, clientData);
        assertNonIdProperties(studyGroupToUpdate, clientData);
        Assert.assertEquals(studyGroupToUpdate.getAnimals().size(), 0);
        Assert.assertEquals(studyGroupToUpdate.getAnimals().size(), clientData.studyGroupAnimals.size());
    }

    @Test
    private void testUpdateStudyGroupWithoutAnimals()
    {
        StudyGroupClientData clientData = getStudyGroupClientData(TestConstants.DUMMY_UPDATE_SG_RAW_DATA);
        StudyGroup studyGroupToUpdate = StudyGroupTestUtils.getStudyGroupWithoutAnimals();
        mStudyGroupFactory.update(studyGroupToUpdate, clientData);
        assertNonIdProperties(studyGroupToUpdate, clientData);
        Assert.assertNull(studyGroupToUpdate.getAnimals());
    }

    @Test
    private void testCreateStudyGroupWithoutAnimals()
    {
        StudyGroupClientData clientData = getStudyGroupClientData(TestConstants.DUMMY_NEW_SG_RAW_DATA);
        StudyGroup studyGroup = mStudyGroupFactory.create(clientData);
        assertNonIdProperties(studyGroup, clientData);
        Assert.assertNull(studyGroup.getAnimals());
    }

    @Test
    private void testCreateStudyGroupWithAnimals()
    {
        StudyGroupClientData clientData = getStudyGroupClientData(TestConstants.DUMMY_NEW_SG_WITH_ANIMALS_RAW_DATA);
        StudyGroup studyGroup = mStudyGroupFactory.create(clientData);
        assertNonIdProperties(studyGroup, clientData);
        Assert.assertNotNull(studyGroup.getAnimals());
        Assert.assertEquals(studyGroup.getAnimals().isEmpty(), false);
        Assert.assertEquals(studyGroup.getAnimals().size(), clientData.studyGroupAnimals.size());
        for (AnimalClientData animalClientData : clientData.studyGroupAnimals)
        {
            Assert.assertTrue(studyGroup.getAnimals().contains(new Animal(animalClientData.id)));
        }
    }

    private void assertNonIdProperties(StudyGroup studyGroup, StudyGroupClientData clientData)
    {
        Assert.assertEquals(studyGroup.getStudyGroupNumber(), clientData.studyGroupName);
    }
}
