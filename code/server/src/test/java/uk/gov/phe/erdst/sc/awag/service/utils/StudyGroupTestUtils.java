package uk.gov.phe.erdst.sc.awag.service.utils;

import java.util.HashSet;
import java.util.Set;

import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.StudyGroup;

public final class StudyGroupTestUtils
{
    private static final String TEST_STUDY_GROUP_NUMBER = "Study Group 1";
    private static final Long TEST_ANIMAL_IN_STUDY_GROUP = 10000L;

    private StudyGroupTestUtils()
    {
    }

    public static StudyGroup getStudyGroupWithoutAnimals()
    {
        StudyGroup studyGroup = new StudyGroup();
        setProperties(studyGroup);
        return studyGroup;
    }

    public static StudyGroup getStudyGroupWithAnimals()
    {
        StudyGroup studyGroup = new StudyGroup();
        setProperties(studyGroup);
        Animal animal = new Animal(TEST_ANIMAL_IN_STUDY_GROUP);
        Set<Animal> animals = new HashSet<Animal>(1);
        animals.add(animal);
        studyGroup.setAnimals(animals);
        return studyGroup;
    }

    private static void setProperties(StudyGroup studyGroup)
    {
        studyGroup.setStudyGroupNumber(TEST_STUDY_GROUP_NUMBER);
    }
}
