package uk.gov.phe.erdst.sc.awag.dao;

import uk.gov.phe.erdst.sc.awag.datamodel.ImportAnimal;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;

public interface ImportAnimalDao extends UniqueDao
{
    ImportAnimal getImportAnimal(String animalNumber, Long importHeaderId) throws AWNoSuchEntityException;
}
