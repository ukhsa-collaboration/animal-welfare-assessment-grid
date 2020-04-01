package uk.gov.phe.erdst.sc.awag.dao;

import uk.gov.phe.erdst.sc.awag.datamodel.ImportHeader;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;

public interface ImportHeaderDao extends UniqueDao
{
    ImportHeader store(ImportHeader importHeader) throws AWNonUniqueException;

    void realDelete(Long importHeaderId);
}
