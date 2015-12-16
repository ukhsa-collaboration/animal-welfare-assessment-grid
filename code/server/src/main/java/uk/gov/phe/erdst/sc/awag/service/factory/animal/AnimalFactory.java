package uk.gov.phe.erdst.sc.awag.service.factory.animal;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.AssessmentTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.Sex;
import uk.gov.phe.erdst.sc.awag.datamodel.Source;
import uk.gov.phe.erdst.sc.awag.datamodel.Species;
import uk.gov.phe.erdst.sc.awag.datamodel.client.AnimalClientData;

@Stateless
public class AnimalFactory
{
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

    public void update(Animal animal, AnimalClientData clientData)
    {
        setNonIdAnimalProperties(animal, clientData);
    }
}
