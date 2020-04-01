package uk.gov.phe.erdst.sc.awag.dao;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;

public interface UploadCommonDao<T> extends CommonDao<T>
{
    public void upload(Collection<T> entity) throws AWNonUniqueException;
}
