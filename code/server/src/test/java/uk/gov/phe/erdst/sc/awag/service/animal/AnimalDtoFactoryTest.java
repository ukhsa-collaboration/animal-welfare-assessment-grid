package uk.gov.phe.erdst.sc.awag.service.animal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.inject.Inject;

import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.service.factory.animal.AnimalDtoFactory;
import uk.gov.phe.erdst.sc.awag.service.utils.AnimalTestUtils;
import uk.gov.phe.erdst.sc.awag.shared.test.TestConstants;
import uk.gov.phe.erdst.sc.awag.utils.GuiceHelper;
import uk.gov.phe.erdst.sc.awag.webapi.response.animal.AnimalDto;

@Test(groups = {TestConstants.TESTNG_UNIT_TESTS_GROUP})
public class AnimalDtoFactoryTest
{
    @Inject
    private AnimalDtoFactory mAnimalDtoFactory;

    @BeforeMethod
    public void setUp() throws Exception
    {
        GuiceHelper.injectTestDependencies(this);
    }

    @Test
    public void testCreateBasicAnimalDto()
    {
        Animal animal = AnimalTestUtils.createAnimalWithIdOnly(1L);
        animal.setAnimalNumber("Animal 1");

        AnimalDto animalDto = mAnimalDtoFactory.createAnimalDto(animal);

        Assert.assertEquals(animalDto.id, animal.getId());
        Assert.assertEquals(animalDto.animalNumber, animal.getAnimalNumber());
    }

    @Test
    public void testCreateAnimalDtos()
    {
        List<Animal> animals = new ArrayList<Animal>();
        animals.add(AnimalTestUtils.createAnimalWithIdOnly(1L));
        animals.add(AnimalTestUtils.createAnimalWithIdOnly(2L));

        List<AnimalDto> animalDtos = mAnimalDtoFactory.createAnimalDtos(animals);

        Assert.assertEquals(animalDtos.size(), animals.size());

        for (AnimalDto dto : animalDtos)
        {
            boolean foundMatch = false;
            for (Animal animal : animals)
            {
                if (animal.getId() == dto.id)
                {
                    foundMatch = true;
                }
            }

            Assert.assertEquals(foundMatch, true);
        }
    }

    @Test
    public void testCreateAnimalDtosNoSourceData()
    {
        List<Animal> animals = Collections.emptyList();
        List<AnimalDto> animalDtos = mAnimalDtoFactory.createAnimalDtos(animals);
        Assert.assertNotNull(animalDtos);
        Assert.assertTrue(animalDtos.isEmpty());
    }

    @Test
    public void testCreateAnimalDto()
    {
        Animal animalObj = AnimalTestUtils.createAnimalWithIdOnly(1L);
        List<Animal> animals = new ArrayList<Animal>();
        animals.add(animalObj);

        List<AnimalDto> dtos = mAnimalDtoFactory.createAnimalDtos(animals);

        Assert.assertFalse(dtos.isEmpty());

        AnimalDto aDto = dtos.get(0);

        Assert.assertEquals(aDto.id, animalObj.getId());
        Assert.assertEquals(aDto.animalNumber, animalObj.getAnimalNumber());
    }

    @Test
    public void testCreateAnimalDtoNoSourceData()
    {
        List<Animal> animals = Collections.emptyList();
        List<AnimalDto> dtos = mAnimalDtoFactory.createAnimalDtos(animals);

        Assert.assertNotNull(dtos);
        Assert.assertTrue(dtos.isEmpty());
    }
}
