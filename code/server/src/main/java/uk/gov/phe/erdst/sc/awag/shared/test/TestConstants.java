package uk.gov.phe.erdst.sc.awag.shared.test;

import java.io.File;

public final class TestConstants
{
    // TestNG group names correspond to Maven build properties
    public static final String TESTNG_CONTAINER_TESTS_GROUP = "container-based";
    public static final String TESTNG_UNIT_TESTS_GROUP = "unit";
    public static final String TESTNG_CONTAINER_RETRIEVE_TEST_GROUP = "retrieve";
    public static final String TESTNG_CONTAINER_STORE_TEST_GROUP = "store";
    public static final String TESTNG_CONTAINER_UPDATE_TEST_GROUP = "update";
    public static final String TESTNG_CONTAINER_REMOVE_TEST_GROUP = "remove";

    public static final String TESTNG_SOURCE_TESTS_GROUP = "container-source-based"; // TODO review names
    public static final String TESTNG_CONTAINER_UPLOAD_TESTS_GROUP = "container-upload-based";
    public static final String TESTNG_CONTAINER_ANIMAL_IMPORT_TESTS_GROUP = "container-animal-import-based";
    public static final String TESTNG_CONTAINER_ASSESSMENT_IMPORT_TESTS_GROUP = "container-assessment-import-based";

    public static final int RETRIEVE_PRIORITY = 0;
    public static final int STORE_PRIORITY = 1;
    public static final int UPDATE_PRIORITY = 2;
    public static final int REMOVE_PRIORITY = 3;

    public static final Integer TEST_NO_OFFSET = null;
    public static final Integer TEST_NO_LIMIT = null;
    public static final Integer TEST_OFFSET = 1;
    public static final Integer TEST_LIMIT = 3;

    public static final String TEST_STUDY_NAME = "Test study";

    public static final Long TEST_ASSESSMENT_TEMPLATE_ID = 10000L;
    public static final Long TEST_ASSESSMENT_TEMPLATE_3_ID = 10002L;

    public static final String INITIAL_USER_1_NAME = "User 1";
    public static final String INITIAL_USER_2_NAME = "User 2";
    public static final Long INITIAL_IMPORT_ANIMAL_HEADER_ID = 2L;

    // CS:OFF: LineLength
    public static final String DUMMY_ASSESSMENT_RAW_DATA = "{'id':-1,'animalId':10000,'reason':'Reason 1','date':'2014-08-14T00:00:00.000Z','animalHousing':'Housing 1','performedBy':'User 1','score':{'10000':{'10000':{'score':6,'isIgnored':false},'10001':{'score':6,'isIgnored':false},'10002':{'score':6,'isIgnored':false},'10003':{'score':6,'isIgnored':false}}},'averageScores':{'10000':6},'parameterComments':{'10000':'Physical parameter comments'}}";
    public static final String DUMMY_ASSESSMENT_RAW_EMPTY_INVALID_DATA = "{'id':-1,'animalId':-1,'reason':'','date':'','animalHousing':'','performedBy':'','score':{},'averageScores':{},'parameterComments':{}}";
    public static final String DUMMY_ASSESSMENT_RAW_INVALID_ZERO_SCORES = "{'id':-1,'animalId':10000,'reason':'Reason 1','animalHousing':'Housing 1','performedBy':'User 1','date':'2015-05-14T00:00:00.000Z','score':{'1':{'1':{'score':0,'isIgnored':false},'2':{'score':0,'isIgnored':false},'3':{'score':0,'isIgnored':false},'4':{'score':0,'isIgnored':false}},'2':{'1':{'score':0,'isIgnored':false},'2':{'score':0,'isIgnored':false},'3':{'score':0,'isIgnored':false}},'3':{'1':{'score':0,'isIgnored':false},'2':{'score':0,'isIgnored':false}}},'averageScores':{'1':0.0,'2':0.0,'3':0.0},'parameterComments':{}}";
    public static final String DUMMY_ASSESSMENT_RAW_VALID_IGNORED_SCORES = "{'id':-1,'animalId':10000,'reason':'Reason 1','animalHousing':'Housing 1','performedBy':'User 1','date':'2015-05-14T00:00:00.000Z','score':{'1':{'1':{'score':0,'isIgnored':true},'2':{'score':0,'isIgnored':true},'3':{'score':0,'isIgnored':true},'4':{'score':0,'isIgnored':true}},'2':{'1':{'score':0,'isIgnored':true},'2':{'score':0,'isIgnored':true},'3':{'score':0,'isIgnored':true}},'3':{'1':{'score':0,'isIgnored':true},'2':{'score':0,'isIgnored':true}}},'averageScores':{'1':0.0,'2':0.0,'3':0.0},'parameterComments':{}}";
    public static final String DUMMY_ASSESSMENT_RAW_VALID_SCORE_TEMPLATE = "{'id':-1,'animalId':10003,'reason':'Reason 1','date':'2015-12-02T00:00:00.000Z','animalHousing':'Housing 1','performedBy':'User 1','score':{'10000':{'10000':{'score':0,'isIgnored':false},'10001':{'score':0,'isIgnored':false},'10002':{'score':0,'isIgnored':false},'10003':{'score':0,'isIgnored':false}},'10001':{'10000':{'score':0,'isIgnored':false},'10001':{'score':0,'isIgnored':false},'10002':{'score':0,'isIgnored':false},'10003':{'score':0,'isIgnored':false}}},'averageScores':{'10000':0,'10001':0},'parameterComments':{}}";
    public static final String DUMMY_NEW_ANIMAL_RAW_DATA = "{'id':-1,'number':'Animal 444','dob':'2011-05-01T00:00:00Z','sex':10001,'species':10000,'source':10000,'assessmentTemplate':10000,'dam':1,'father':2,'isAlive':true,'isAssessed':true}";
    public static final String DUMMY_UPDATE_ANIMAL_RAW_DATA = "{'id':10002,'number':'Animal 3','dob':'2015-01-02T00:00:00.000Z','sex':10000,'species':10000,'source':10000,'assessmentTemplate':10000,'dam':10000,'father':10001,'isAlive':false,'isAssessed':true}";
    public static final String DUMMY_NEW_SPECIES_RAW_DATA = "{'speciesId': -1, 'speciesName':'Species 2'}";
    public static final String DUMMY_UPDATE_SPECIES_RAW_DATA = "{'speciesId': 10000, 'speciesName':'Jonny 5'}";
    public static final String DUMMY_NEW_SOURCE_RAW_DATA = "{'sourceId': 10000, 'sourceName':'Source 1'}";
    public static final String DUMMY_UPDATE_SOURCE_RAW_DATA = "{'sourceId': 10000, 'sourceName':'Source 1 new'}";
    public static final String DUMMY_NEW_USER_RAW_DATA = "{'userId': -1, 'userName':'Technician 2'}";

    public static final String DUMMY_ANIMAL_VALID_RAW_DATA = "{'id':-1,'number':'Test Animal','dob':'2013-02-01T00:00:00.000Z','sex':10000,'species':10000,'source':10000,'dam':10000,'father':10001,'isAlive':false,'assessmentTemplate':10000}";
    public static final String DUMMY_ANIMAL_NULL_ANIMAL_NUMBER_RAW_DATA = "{'id':-1,'dob':'2013-02-01T00:00:00.000Z','sex':10000,'species':10000,'source':10000,'dam':10000,'father':10001,'isAlive':false,'assessmentTemplate':10000}";
    public static final String DUMMY_ANIMAL_MIN_ANIMAL_NUMBER_LENGTH_RAW_DATA = "{'id':-1,'number':'Te','dob':'2013-02-01T00:00:00.000Z','sex':10000,'species':10000,'source':10000,'dam':10000,'father':10001,'isAlive':false,'assessmentTemplate':10000}";
    public static final String DUMMY_ANIMAL_MAX_ANIMAL_NUMBER_LENGTH_RAW_DATA = "{'id':-1,'number':'Tessssssssssssssssssssssssssssst Animal','dob':'2013-02-01T00:00:00.000Z','sex':10000,'species':10000,'source':10000,'dam':10000,'father':10001,'isAlive':false,'assessmentTemplate':10000}";
    public static final String DUMMY_ANIMAL_DOB_INVALID_RAW_DATA = "{'id':-1,'number':'Test Animal','dob':'2013-02-01T00:00:00aa.000Z','sex':10000,'species':10000,'source':10000,'dam':10000,'father':10001,'isAlive':false,'assessmentTemplate':10000}";
    public static final String DUMMY_ANIMAL_PARENT_SAME_RAW_DATA = "{'id':-1,'number':'Test Animal','dob':'2013-02-01T00:00:00.000Z','sex':10000,'species':10000,'source':10000,'dam':10000,'father':10000,'isAlive':false,'assessmentTemplate':10000}";
    public static final String DUMMY_ANIMAL_PARENT_ANIMAL_SAME_RAW_DATA = "";
    public static final String DUMMY_ANIMAL_ANIMAL_DAM_NOT_FEMALE_RAW_DATA = "{'id':-1,'number':'Test Animal','dob':'2013-02-01T00:00:00.000Z','sex':10000,'species':10000,'source':10000,'dam':10001,'isAlive':false,'assessmentTemplate':10000}";
    public static final String DUMMY_ANIMAL_ANIMAL_FATHER_NOT_MALE_RAW_DATA = "{'id':-1,'number':'Test Animal','dob':'2013-02-01T00:00:00.000Z','sex':10000,'species':10000,'source':10000,'father':10000,'isAlive':false,'assessmentTemplate':10000}";
    public static final String DUMMY_ANIMAL_PARENT_DAM_DOB_INVALID_RAW_DATA = "";
    public static final String DUMMY_ANIMAL_PARENT_FATHER_DOB_INVALID_RAW_DATA = "";

    public static final Long NON_PERSISTED_ID = 0L;
    public static final String DUMMY_NEW_SG_RAW_DATA = "{'studyGroupId':-1,'studyGroupName':'Study Group 2','studyGroupAnimals': []}";
    public static final String DUMMY_NEW_SG_WITH_ANIMALS_RAW_DATA = "{'studyGroupId':-1,'studyGroupName':'Study Group 2','studyGroupAnimals': [{'id':10002,number:'Animal 2'},{'id':16801,number:'Test Animal'}]}";
    public static final String DUMMY_UPDATE_SG_RAW_DATA = "{'studyGroupId':10000,'studyGroupName':'New Study Group 2','studyGroupAnimals': []}";
    public static final String DUMMY_UPDATE_SG_ADD_ANIMALS_RAW_DATA = "{'studyGroupId':10000,'studyGroupName':'New Study Group 2','studyGroupAnimals': [{'id':10002,number:'Animal 2'},{'id':16801,number:'Test Animal'}]}";
    public static final String DUMMY_UPDATE_SG_REMOVE_ANIMALS_RAW_DATA = "{'studyGroupId':10000,'studyGroupName':'New Study Group 2','studyGroupAnimals': []}";
    public static final String DUMMY_UPDATE_STUDY_RAW_DATA = "{'studyId':10000,'studyName':'New Study 2','studyOpen':true,'studyGroups': []}";
    public static final String DUMMY_UPDATE_STUDY_ADD_SGS_RAW_DATA = "{'studyId':18251,'studyName':'Study 2','studyOpen':true,'studyGroups': [{'studyGroupId':10000,'studyGroupName':'New Study Group 2','studyGroupAnimals': []}]}";
    public static final String DUMMY_UPDATE_STUDY_REMOVE_SGS_RAW_DATA = "{'studyId':10000,'studyName':'New Study 2','studyOpen':true,'studyGroups': []}";
    public static final String DUMMY_NEW_STUDY_RAW_DATA = "{'studyId':-1,'studyName':'Study 2','studyOpen':false,'studyGroups': []}";
    public static final String DUMMY_NEW_STUDY_WITH_SGS_RAW_DATA = "{'studyId':-1,'studyName':'Study 3','studyOpen':false,'studyGroups': [{'studyGroupId':10000,'studyGroupName':'New Study Group 2','studyGroupAnimals': []}]}";

    public static final String DUMMY_NEW_HOUSING_RAW_DATA = "{'housingId':10000, 'housingName':'Housing 1'}";

    public static final String DUMMY_NEW_REASON_RAW_DATA = "{'reasonId':10000, 'reasonName':'Reason 1'}";

    public static final String DUMMY_NEW_SCALE_RAW_DATA = "{'scaleId':10000, 'scaleName':'1 to 10', 'scaleMin': 1, 'scaleMax' : 10}";
    public static final String DUMMY_NEW_FACTOR_RAW_DATA = "{'factorId': -1,'factorName':'Factor 1'}";
    public static final String DUMMY_UPDATE_FACTOR_RAW_DATA = "{'factorId' : 10001, 'factorName' : 'Factor 1 new'}";
    public static final String DUMMY_NEW_PARAMETER_WITH_FACTORS_RAW_DATA = "{'parameterId':10000, 'parameterName':'Parameter 1', 'parameterFactors':[{'factorId': 10000,'factorName':'Factor 1'}]}";
    public static final String DUMMY_NEW_PARAMETER_RAW_DATA = "{'parameterId':10000, 'parameterName':'Parameter 1', 'parameterFactors':[]}";

    public static final String UPLOAD_TEST_DATA_FOLDER = System.getProperty("user.dir") + File.separator + "test-data";

    // CS:ON
    private TestConstants()
    {
    }
}
