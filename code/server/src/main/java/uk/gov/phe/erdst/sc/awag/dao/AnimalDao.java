package uk.gov.phe.erdst.sc.awag.dao;

import java.util.Collection;
import java.util.List;

import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;

public interface AnimalDao extends UniqueDao
{
    List<Animal> getNonDeletedAnimals(Integer offset, Integer limit);

    Animal getAnimal(Long animalId) throws AWNoSuchEntityException;

    Animal getAnimal(String animalNumber) throws AWNoSuchEntityException; // TODO integration test

    Animal store(Animal animal) throws AWNonUniqueException;

    Collection<Animal> getAnimals();

    Collection<Animal> getAnimals(List<Long> animalIds);

    void realDelete(Long animalId) throws AWNoSuchEntityException;

    Animal updateIsDeleted(Long animalId, boolean isDeleted) throws AWNoSuchEntityException;

    void updateIsAlive(Long animalId, boolean isAlive) throws AWNoSuchEntityException;

    void updateIsAssessed(Long animalId, boolean isAssessed) throws AWNoSuchEntityException;

    List<Animal> getNonDeletedAnimalsLike(String like, Integer offset, Integer limit);

    List<Animal> getNonDeletedAnimalsLikeSex(String like, String sex, Integer offset, Integer limit);

    Animal getNonDeletedAnimalById(Long animalId) throws AWNoSuchEntityException;

    Long getCountNonDeletedAnimalsLike(String like);

    Long getCountNonDeleteAnimalsLikeSex(String like, String sex);

    Long getCountNonDeletedAnimals();

    Long getCountDeletedAnimals();

    List<Animal> getDeletedAnimals(Integer offset, Integer limit);

    void upload(Collection<Animal> animals) throws AWNonUniqueException;

}
