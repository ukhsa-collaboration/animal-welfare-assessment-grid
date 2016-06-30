package uk.gov.phe.erdst.sc.awag.service.assessment;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.AnimalHousing;
import uk.gov.phe.erdst.sc.awag.datamodel.Assessment;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentReason;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentScore;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.Parameter;
import uk.gov.phe.erdst.sc.awag.datamodel.ParameterScore;
import uk.gov.phe.erdst.sc.awag.datamodel.Sex;
import uk.gov.phe.erdst.sc.awag.datamodel.Source;
import uk.gov.phe.erdst.sc.awag.datamodel.Species;
import uk.gov.phe.erdst.sc.awag.datamodel.Study;
import uk.gov.phe.erdst.sc.awag.datamodel.User;
import uk.gov.phe.erdst.sc.awag.dto.ParameterScoredDto;
import uk.gov.phe.erdst.sc.awag.dto.assessment.AssessmentDto;
import uk.gov.phe.erdst.sc.awag.dto.assessment.AssessmentFullDto;
import uk.gov.phe.erdst.sc.awag.dto.assessment.AssessmentSearchPreviewDto;
import uk.gov.phe.erdst.sc.awag.dto.assessment.AssessmentsDto;
import uk.gov.phe.erdst.sc.awag.dto.assessment.ParametersOrdering;
import uk.gov.phe.erdst.sc.awag.service.factory.assessment.AssessmentDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.utils.AnimalTestUtils;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;

import com.google.inject.Inject;

// CS:OFF: ClassDataAbstractionCoupling
@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class AssessmentDtoFactoryTest
// CS:ON
{
    private static final String TEST_REASON = "Test reason";
    private static final String TEST_ANIMAL_NUMBER = "Test animal number";
    private static final String TEST_DATE_TIME = "2015-09-01T00:00:00.000Z";

    @Inject
    private AssessmentDtoFactory mAssessmentDtoFactory;

    private Assessment mAssessment;

    private ParametersOrdering mParametersOrdering = new ParametersOrdering();

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
        mAssessment = new Assessment();
        mAssessment.setId(ValidationConstants.ENTITY_MIN_ID);
        mAssessment.setDate(TEST_DATE_TIME);
    }

    @Test
    public void testCreateValidAssessment()
    {
        AssessmentDto assessmentDto = mAssessmentDtoFactory.createAssessmentDto(mAssessment, mParametersOrdering);

        Assert.assertEquals(assessmentDto.assessmentId, mAssessment.getId());
        Assert.assertEquals(assessmentDto.assessmentDate, mAssessment.getDate());
    }

    @Test
    public void testCreateAssessmentNullAnimal()
    {
        mAssessment.setAnimal(null);

        AssessmentDto assessmentDto = mAssessmentDtoFactory.createAssessmentDto(mAssessment, mParametersOrdering);

        Assert.assertEquals(assessmentDto.assessmentId, mAssessment.getId());
        Assert.assertEquals(assessmentDto.assessmentDate, mAssessment.getDate());
        Assert.assertNotNull(assessmentDto.animal);
    }

    @Test
    public void testCreateAssessmentNullScore()
    {
        mAssessment.setScore(null);

        AssessmentDto assessmentDto = mAssessmentDtoFactory.createAssessmentDto(mAssessment, mParametersOrdering);

        Assert.assertEquals(assessmentDto.assessmentId, mAssessment.getId());
        Assert.assertEquals(assessmentDto.assessmentDate, mAssessment.getDate());
        Assert.assertNotNull(assessmentDto.assessmentParameters);
    }

    @Test
    public void testCreateAssessmentNullReason()
    {
        mAssessment.setReason(null);

        AssessmentDto assessmentDto = mAssessmentDtoFactory.createAssessmentDto(mAssessment, mParametersOrdering);

        Assert.assertEquals(assessmentDto.assessmentReason, "");
    }

    @Test
    public void testCreateAssessmentSearchPreviewDto()
    {
        AssessmentReason reason = new AssessmentReason();
        reason.setId(ValidationConstants.ENTITY_MIN_ID);
        reason.setName(TEST_REASON);

        mAssessment.setReason(reason);

        Animal animal = new Animal();
        animal.setId(ValidationConstants.ENTITY_MIN_ID);
        animal.setAnimalNumber(TEST_ANIMAL_NUMBER);

        mAssessment.setAnimal(animal);

        AssessmentSearchPreviewDto dto = mAssessmentDtoFactory.createAssessmentSearchPreviewDto(mAssessment);

        Assert.assertEquals(dto.assessmentId, mAssessment.getId());
        Assert.assertEquals(dto.assessmentDate, mAssessment.getDate());
        Assert.assertEquals(dto.assessmentReason, mAssessment.getReason().getName());
        Assert.assertEquals(dto.animal.id, mAssessment.getAnimal().getId());
        Assert.assertEquals(dto.animal.animalNumber, mAssessment.getAnimal().getAnimalNumber());
    }

    @Test
    public void testCreateAssessmentFullDto()
    {
        AssessmentReason reason = new AssessmentReason();
        reason.setId(ValidationConstants.ENTITY_MIN_ID);
        reason.setName(TEST_REASON);

        mAssessment.setReason(reason);

        Animal animal = createAnimal();

        mAssessment.setAnimal(animal);

        AnimalHousing housing = new AnimalHousing();
        housing.setId(ValidationConstants.ENTITY_MIN_ID);
        housing.setName("Test animal housing");

        mAssessment.setAnimalHousing(housing);

        User user = new User();
        user.setId(ValidationConstants.ENTITY_MIN_ID);
        user.setName("Test user name");

        mAssessment.setPerformedBy(user);

        Study study = new Study();
        study.setId(ValidationConstants.ENTITY_MIN_ID);
        study.setStudyNumber("Test study number");

        mAssessment.setStudy(study);

        mAssessment.setIsComplete(true);

        AssessmentFullDto dto = mAssessmentDtoFactory.createAssessmentFullDto(mAssessment, mParametersOrdering);

        Assert.assertEquals(dto.assessmentId, mAssessment.getId());
        Assert.assertEquals(dto.assessmentDate, mAssessment.getDate());

        Assert.assertTrue(dto.isComplete);

        // Animal, housing, reason, study and score dtos are tested in their respective factory
        // tests

        mAssessment.setAnimalHousing(null);
        mAssessment.setPerformedBy(null);
        mAssessment.setReason(null);
        mAssessment.setStudy(null);
        mAssessment.setIsComplete(false);

        dto = mAssessmentDtoFactory.createAssessmentFullDto(mAssessment, mParametersOrdering);

        Assert.assertNull(dto.reason);
        Assert.assertNull(dto.housing);
        Assert.assertNull(dto.performedBy);
        Assert.assertNull(dto.study);
        Assert.assertFalse(dto.isComplete);
    }

    private static Animal createAnimal()
    {
        AssessmentTemplate template = new AssessmentTemplate();
        template.setId(ValidationConstants.ENTITY_MIN_ID);

        Sex sex = new Sex();
        sex.setName(Sex.MALE);

        Species species = new Species();
        species.setId(ValidationConstants.ENTITY_MIN_ID);
        species.setName("Test species name");

        Source source = new Source();
        source.setId(ValidationConstants.ENTITY_MIN_ID);
        source.setName("Test source name");

        return AnimalTestUtils.createAnimal(ValidationConstants.ENTITY_MIN_ID, TEST_ANIMAL_NUMBER, TEST_DATE_TIME, sex,
            source, species, template, null, null, true, true);
    }

    @Test
    public void testCreateAssessmentsDto()
    {
        Assessment assessment1 = new Assessment();
        assessment1.setId(1L);
        Assessment assessment2 = new Assessment();
        assessment2.setId(2L);

        List<Assessment> assessments = new ArrayList<Assessment>();
        assessments.add(assessment1);
        assessments.add(assessment2);

        AssessmentsDto assessmentsDataDto = mAssessmentDtoFactory
            .createAssessmentsDto(assessments, mParametersOrdering);

        Assert.assertEquals(assessmentsDataDto.assessments.size(), assessments.size());
    }

    @Test
    public void testCreateAssessmentsDtoWithParametersOrdering()
    {
        AssessmentScore score = new AssessmentScore();
        List<ParameterScore> parameterScores = new ArrayList<>();

        ParametersOrdering ordering = new ParametersOrdering();
        final int limit = 7;
        for (int i = 0, j = limit - 1; i < limit; i++, j--)
        {
            Long id = Long.valueOf(i);

            Parameter parameter = new Parameter();
            parameter.setId(id);

            ParameterScore parameterScore = new ParameterScore();
            parameterScore.setParameterScored(parameter);

            parameterScores.add(parameterScore);
            ordering.add(id, j);
        }

        score.setParametersScored(parameterScores);

        Assessment assessment = new Assessment();
        assessment.setScore(score);

        setSecondaryProperties(assessment);

        AssessmentFullDto fullDto = mAssessmentDtoFactory.createAssessmentFullDto(assessment, ordering);

        List<ParameterScoredDto> scoreDtos = new ArrayList<>(fullDto.assessmentParameters);

        for (int i = 0; i < scoreDtos.size(); i++)
        {
            ParameterScoredDto scoreDto = scoreDtos.get(i);
            int orderingIdx = ordering.getIndex(scoreDto.parameterId);
            Assert.assertEquals(i, orderingIdx);
        }
    }

    private void setSecondaryProperties(Assessment assessment)
    {
        Animal animal = createAnimal();
        assessment.setAnimal(animal);
    }
}
