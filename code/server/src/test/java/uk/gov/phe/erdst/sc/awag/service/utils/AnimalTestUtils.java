package uk.gov.phe.erdst.sc.awag.service.utils;

import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.Sex;
import uk.gov.phe.erdst.sc.awag.datamodel.Source;
import uk.gov.phe.erdst.sc.awag.datamodel.Species;

public final class AnimalTestUtils
{
    private AnimalTestUtils()
    {
    }

    public static Animal createAnimal(Long animalId)
    {
        Animal animal = new Animal();
        animal.setId(animalId);

        return animal;
    }

    private static void setNonIdProperties(Animal animal, String animalNumber, String dateOfBirth, Sex animalSex,
        Source animalSource, Species animalSpecies, AssessmentTemplate assessmentTemplate, Animal dam, Animal father,
        boolean animalIsAlive, boolean animalIsAssessed)
    {
        animal.setAnimalNumber(animalNumber);
        animal.setDateOfBirth(dateOfBirth);
        animal.setSex(animalSex);
        animal.setSpecies(animalSpecies);
        animal.setSource(animalSource);
        animal.setAssessmentTemplate(assessmentTemplate);
        animal.setDam(dam);
        animal.setFather(father);
        animal.setIsAlive(animalIsAlive);
        animal.setIsAssessed(animalIsAssessed);
    }

    public static Animal createAnimalAutoId(String animalNumber, String dateOfBirth, Sex animalSex, Source animalSource,
        Species animalSpecies, AssessmentTemplate assessmentTemplate, Animal dam, Animal father, boolean animalIsAlive,
        boolean animalIsAssessed)
    {
        Animal animal = new Animal();

        setNonIdProperties(animal, animalNumber, dateOfBirth, animalSex, animalSource, animalSpecies,
            assessmentTemplate, dam, father, animalIsAlive, animalIsAssessed);
        return animal;
    }

    public static Animal createAnimal(Long animalId, String animalNumber, String dateOfBirth, Sex animalSex,
        Source animalSource, Species animalSpecies, AssessmentTemplate assessmentTemplate, Animal dam, Animal father,
        boolean animalIsAlive, boolean animalIsAssessed)
    {
        Animal animal = new Animal();
        animal.setId(animalId);
        setNonIdProperties(animal, animalNumber, dateOfBirth, animalSex, animalSource, animalSpecies,
            assessmentTemplate, dam, father, animalIsAlive, animalIsAssessed);
        return animal;
    }
}
