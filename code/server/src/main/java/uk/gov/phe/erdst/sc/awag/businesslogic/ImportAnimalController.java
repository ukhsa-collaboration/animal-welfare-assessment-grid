package uk.gov.phe.erdst.sc.awag.businesslogic;

import javax.ejb.Stateless;
import javax.inject.Inject;

import uk.gov.phe.erdst.sc.awag.dao.ImportAnimalDao;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportAnimal;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;

@Stateless
public class ImportAnimalController
{
    @Inject
    private ImportAnimalDao importAnimalDao;

    public ImportAnimal getAnimalByNameNonApiMethod(final String animalNumber, final Long importHeaderId)
        throws AWNoSuchEntityException
    {
        return importAnimalDao.getImportAnimal(animalNumber, importHeaderId);
    }

}
