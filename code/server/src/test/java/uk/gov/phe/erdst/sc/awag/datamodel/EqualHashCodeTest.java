package uk.gov.phe.erdst.sc.awag.datamodel;

import java.util.HashSet;

import org.testng.Assert;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.webapi.request.AnimalClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.StudyGroupClientData;
import uk.gov.phe.erdst.sc.awag.webapi.response.assessment.AssessmentSimpleDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.factor.FactorScoredDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.parameter.ParameterScoredDto;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class EqualHashCodeTest
{
    @Test
    private void testAnimals()
    {
        HashSet<Animal> animals = new HashSet<Animal>();
        Animal uniqueAnimal = new Animal(1L);
        Animal nonUniqueAnimal = new Animal(1L);
        animals.add(uniqueAnimal);
        animals.add(nonUniqueAnimal);
        int expectedNoAnimals = 1;
        Assert.assertEquals(animals.size(), expectedNoAnimals);
    }

    @Test
    private void testAnimalClientData()
    {
        HashSet<AnimalClientData> animals = new HashSet<AnimalClientData>();
        AnimalClientData uniqueAnimal = new AnimalClientData(1L);
        AnimalClientData nonUniqueAnimal = new AnimalClientData(1L);
        animals.add(uniqueAnimal);
        animals.add(nonUniqueAnimal);
        int expectedNoAnimals = 1;
        Assert.assertEquals(animals.size(), expectedNoAnimals);
    }

    @Test
    private void testStudyGroups()
    {
        HashSet<StudyGroup> studyGroup = new HashSet<StudyGroup>();
        StudyGroup uniqueStudyGroup = new StudyGroup(1L);
        StudyGroup nonUniqueStudyGroup = new StudyGroup(1L);
        studyGroup.add(uniqueStudyGroup);
        studyGroup.add(nonUniqueStudyGroup);
        int expectedNoStudyGroups = 1;
        Assert.assertEquals(studyGroup.size(), expectedNoStudyGroups);
    }

    @Test
    private void testStudyGroupClientData()
    {
        HashSet<StudyGroupClientData> studyGroupClientData = new HashSet<StudyGroupClientData>();
        StudyGroupClientData uniqueStudyGroupClientData = new StudyGroupClientData(1L);
        StudyGroupClientData nonUniqueStudyGroupClientData = new StudyGroupClientData(1L);
        studyGroupClientData.add(uniqueStudyGroupClientData);
        studyGroupClientData.add(nonUniqueStudyGroupClientData);
        int expectedNoStudyGroups = 1;
        Assert.assertEquals(studyGroupClientData.size(), expectedNoStudyGroups);
    }

    @Test
    private void testFactorDto()
    {
        HashSet<FactorScoredDto> factorDtos = new HashSet<FactorScoredDto>(2);
        FactorScoredDto uniqueFactorDto = new FactorScoredDto(1L);
        FactorScoredDto nonUniqueFactorDto = new FactorScoredDto(1L);
        factorDtos.add(uniqueFactorDto);
        factorDtos.add(nonUniqueFactorDto);
        int expectedNoFactorDtos = 1;
        Assert.assertEquals(factorDtos.size(), expectedNoFactorDtos);
    }

    @Test
    private void testParameterDto()
    {
        HashSet<ParameterScoredDto> parameterDtos = new HashSet<ParameterScoredDto>();
        ParameterScoredDto uniqueParameterDto = new ParameterScoredDto(1L);
        ParameterScoredDto nonUniqueParameterDto = new ParameterScoredDto(1L);
        parameterDtos.add(uniqueParameterDto);
        parameterDtos.add(nonUniqueParameterDto);
        int expectedNoparameterDtos = 1;
        Assert.assertEquals(parameterDtos.size(), expectedNoparameterDtos);
    }

    @Test
    private void testAssessmentDto()
    {
        HashSet<AssessmentSimpleDto> assessmentDtos = new HashSet<AssessmentSimpleDto>();
        AssessmentSimpleDto uniqueAssessmentDto = new AssessmentSimpleDto(1L);
        AssessmentSimpleDto nonUniqueAssessmentDto = new AssessmentSimpleDto(1L);
        assessmentDtos.add(uniqueAssessmentDto);
        assessmentDtos.add(nonUniqueAssessmentDto);
        int expectedNoAssessmentDtos = 1;
        Assert.assertEquals(assessmentDtos.size(), expectedNoAssessmentDtos);
    }
}
