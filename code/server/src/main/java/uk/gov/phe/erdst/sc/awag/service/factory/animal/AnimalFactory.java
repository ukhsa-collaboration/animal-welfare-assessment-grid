package uk.gov.phe.erdst.sc.awag.service.factory.animal;

import javax.ejb.Stateless;
import javax.inject.Inject;

import uk.gov.phe.erdst.sc.awag.dao.AnimalDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportAnimal;
import uk.gov.phe.erdst.sc.awag.datamodel.Sex;
import uk.gov.phe.erdst.sc.awag.datamodel.Source;
import uk.gov.phe.erdst.sc.awag.datamodel.Species;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.webapi.request.AnimalClientData;

@Stateless
public class AnimalFactory
{
    @Inject
    private AnimalDao mAnimalDao;

    public AnimalFactory()
    {
    }

    private void setNonIdAnimalProperties(Animal animal, AnimalClientData clientData)
    {
        animal.setAnimalNumber(clientData.number);
        animal.setDateOfBirth(clientData.dob);
        animal.setSex(new Sex(clientData.sex));
        animal.setSource(new Source(clientData.source));
        animal.setSpecies(new Species(clientData.species));

        if (clientData.father == null)
        {
            animal.setFather(null);
        }
        else
        {
            animal.setFather(new Animal(clientData.father));
        }

        if (clientData.dam == null)
        {
            animal.setDam(null);
        }
        else
        {
            animal.setDam(new Animal(clientData.dam));
        }

        animal.setIsAlive(clientData.isAlive);
        animal.setIsAssessed(clientData.isAssessed);
        animal.setAssessmentTemplate(new AssessmentTemplate(clientData.assessmentTemplate));
    }

    public Animal create(AnimalClientData clientData)
    {
        Animal animal = new Animal();

        setNonIdAnimalProperties(animal, clientData);

        return animal;
    }

    public Animal create(ImportAnimal importAnimal)
    {
        Animal animal = new Animal();
        animal.setAnimalNumber(importAnimal.getAnimalNumber());
        animal.setDateOfBirth(importAnimal.getDateBirth());
        animal.setSex(new Sex(importAnimal.getSexid()));
        animal.setSource(new Source(importAnimal.getSourceid()));
        animal.setSpecies(new Species(importAnimal.getSpeciesid()));

        createAnimalSetDam(animal, importAnimal);
        createAnimalSetFather(animal, importAnimal);

        animal.setIsAlive(importAnimal.getIsalive());
        animal.setIsAssessed(false);
        animal.setAssessmentTemplate(new AssessmentTemplate(importAnimal.getAssessmenttemplateid()));
        return animal;
    }

    public void update(Animal animal, AnimalClientData clientData)
    {
        setNonIdAnimalProperties(animal, clientData);
    }

    private void createAnimalSetDam(Animal animal, ImportAnimal importAnimal)
    {
        if (importAnimal.getDamanimalid() != null)
        {
            animal.setDam(new Animal(importAnimal.getDamanimalid()));
        }
        else if (importAnimal.getDamImportanimalid() != null)
        {
            try
            {
                Animal animalDam = mAnimalDao.getAnimal(importAnimal.getDamAnimalName());
                animal.setDam(animalDam);
            }
            catch (AWNoSuchEntityException ex)
            {
                animal.setDam(null);
            }
        }
        else
        {
            animal.setDam(null);
        }
    }

    private void createAnimalSetFather(Animal animal, ImportAnimal importAnimal)
    {
        if (importAnimal.getFatheranimalid() != null)
        {
            animal.setFather(new Animal(importAnimal.getFatheranimalid()));
        }
        else if (importAnimal.getFatherImportanimalid() != null)
        {
            try
            {
                Animal animalFather = mAnimalDao.getAnimal(importAnimal.getFatherAnimalName());
                animal.setFather(animalFather);
            }
            catch (AWNoSuchEntityException ex)
            {
                animal.setDam(null);
            }
        }
        else
        {
            animal.setFather(null);
        }
    }

}
