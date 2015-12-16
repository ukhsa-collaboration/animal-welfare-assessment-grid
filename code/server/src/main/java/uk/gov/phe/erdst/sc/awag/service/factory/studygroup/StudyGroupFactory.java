package uk.gov.phe.erdst.sc.awag.service.factory.studygroup;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.StudyGroup;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AnimalClientData;
import uk.gov.phe.erdst.sc.awag.datamodel.client.StudyGroupClientData;

public class StudyGroupFactory
{
    public StudyGroup create(StudyGroupClientData clientData)
    {
        StudyGroup studyGroup = new StudyGroup();
        setNonIdProperties(studyGroup, clientData);
        return studyGroup;
    }

    public void update(StudyGroup studyGroup, StudyGroupClientData clientData)
    {
        setNonIdProperties(studyGroup, clientData);
    }

    public StudyGroupClientData create(StudyGroup studyGroup)
    {
        StudyGroupClientData studyGroupClientData = new StudyGroupClientData();
        studyGroupClientData.studyGroupId = studyGroup.getId();
        studyGroupClientData.studyGroupName = studyGroup.getStudyGroupNumber();
        for (Animal animal : studyGroup.getAnimals())
        {
            AnimalClientData animalClientData = new AnimalClientData();
            animalClientData.id = animal.getId();
            animalClientData.number = animal.getAnimalNumber();
            studyGroupClientData.studyGroupAnimals.add(animalClientData);
        }
        return studyGroupClientData;
    }

    private void setNonIdProperties(StudyGroup studyGroup, StudyGroupClientData clientData)
    {
        studyGroup.setStudyGroupNumber(clientData.studyGroupName);

        if (!clientData.studyGroupAnimals.isEmpty())
        {
            studyGroup.setAnimals(getStudyGroupAnimals(clientData));
        }
        else
        {
            clearStudyGroupAnimals(studyGroup);
        }
    }

    private Set<Animal> getStudyGroupAnimals(StudyGroupClientData clientData)
    {
        Set<Animal> animals = new HashSet<Animal>();
        for (AnimalClientData animalClientData : clientData.studyGroupAnimals)
        {
            Animal animal = new Animal(animalClientData.id);
            animals.add(animal);
        }
        return animals;
    }

    private void clearStudyGroupAnimals(StudyGroup studyGroup)
    {
        if (studyGroup.getAnimals() != null)
        {
            Set<Animal> currentStudyGroupAnimals = studyGroup.getAnimals();
            currentStudyGroupAnimals.clear();
        }
    }

    public Set<StudyGroupClientData> create(Set<StudyGroup> studyGroups)
    {
        Set<StudyGroupClientData> studyGroupsClientData = new LinkedHashSet<StudyGroupClientData>(studyGroups.size());
        for (StudyGroup studyGroup : studyGroups)
        {
            studyGroupsClientData.add(create(studyGroup));
        }
        return studyGroupsClientData;
    }
}
