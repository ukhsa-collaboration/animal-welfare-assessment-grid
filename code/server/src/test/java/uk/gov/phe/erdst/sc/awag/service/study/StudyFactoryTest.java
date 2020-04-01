package uk.gov.phe.erdst.sc.awag.service.study;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.Study;
import uk.gov.phe.erdst.sc.awag.datamodel.StudyGroup;
import uk.gov.phe.erdst.sc.awag.deprecated.RequestConverter;
import uk.gov.phe.erdst.sc.awag.service.factory.study.StudyFactory;
import uk.gov.phe.erdst.sc.awag.service.utils.StudyTestUtils;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;
import uk.gov.phe.erdst.sc.awag.webapi.request.StudyClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.StudyGroupClientData;

import com.google.inject.Inject;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class StudyFactoryTest
{
    @Inject
    private RequestConverter mRequestConverter;

    @Inject
    private StudyFactory mStudyFactory;

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    private void testUpdateAddStudyGroupsInStudy()
    {
        StudyClientData clientData = getStudyClientData(TestConstants.DUMMY_UPDATE_STUDY_ADD_SGS_RAW_DATA);
        Study studyToUpdate = StudyTestUtils.getStudyWithoutStudyGroups();
        mStudyFactory.update(studyToUpdate, clientData);
        assertNonIdProperties(studyToUpdate, clientData);
        Assert.assertNotNull(studyToUpdate.getStudyGroups());
        Assert.assertEquals(studyToUpdate.getStudyGroups().size(), 1);
        for (StudyGroupClientData studyGroupClientData : clientData.studyGroups)
        {
            Assert
                .assertTrue(studyToUpdate.getStudyGroups().contains(new StudyGroup(studyGroupClientData.studyGroupId)));
        }
    }

    @Test
    private void testUpdateRemoveStudyGroupsInStudy()
    {
        StudyClientData clientData = getStudyClientData(TestConstants.DUMMY_UPDATE_STUDY_REMOVE_SGS_RAW_DATA);
        Study studyToUpdate = StudyTestUtils.getStudyWithStudyGroups();
        mStudyFactory.update(studyToUpdate, clientData);
        assertNonIdProperties(studyToUpdate, clientData);
        Assert.assertEquals(studyToUpdate.getStudyGroups().size(), 0);
        Assert.assertEquals(studyToUpdate.getStudyGroups().size(), clientData.studyGroups.size());
    }

    @Test
    private void testUpdateStudyWithoutStudyGroups()
    {
        StudyClientData clientData = getStudyClientData(TestConstants.DUMMY_UPDATE_STUDY_RAW_DATA);
        Study studyToUpdate = StudyTestUtils.getStudyWithoutStudyGroups();
        mStudyFactory.update(studyToUpdate, clientData);
        assertNonIdProperties(studyToUpdate, clientData);
    }

    @Test
    private void testCreateStudyWithoutStudyGroups()
    {
        StudyClientData clientData = getStudyClientData(TestConstants.DUMMY_NEW_STUDY_RAW_DATA);
        Study study = mStudyFactory.create(clientData);
        assertNonIdProperties(study, clientData);
        Assert.assertNull(study.getStudyGroups());
    }

    @Test
    private void testCreateStudyWithStudyGroups()
    {
        StudyClientData clientData = getStudyClientData(TestConstants.DUMMY_NEW_STUDY_WITH_SGS_RAW_DATA);
        Study study = mStudyFactory.create(clientData);
        assertNonIdProperties(study, clientData);
        Assert.assertNotNull(study.getStudyGroups());
        Assert.assertEquals(study.getStudyGroups().isEmpty(), false);
        Assert.assertEquals(study.getStudyGroups().size(), clientData.studyGroups.size());

        for (StudyGroupClientData studyGroupClientData : clientData.studyGroups)
        {
            Assert.assertTrue(study.getStudyGroups().contains(new StudyGroup(studyGroupClientData.studyGroupId)));
        }
    }

    private void assertNonIdProperties(Study study, StudyClientData clientData)
    {
        Assert.assertEquals(study.getStudyNumber(), clientData.studyName);
        Assert.assertEquals(study.isOpen(), clientData.isStudyOpen);
    }

    private StudyClientData getStudyClientData(String rawClientData)
    {
        StudyClientData clientData = (StudyClientData) mRequestConverter.convert(rawClientData, StudyClientData.class);
        return clientData;
    }
}
