package uk.gov.phe.erdst.sc.awag.dao;

import java.util.Collection;
import java.util.List;

import uk.gov.phe.erdst.sc.awag.datamodel.Source;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;

public interface SourceDao
{
    Collection<Source> getSources(Integer offset, Integer limit);

    List<Source> getSourcesLike(String like, Integer offset, Integer limit);

    Source store(Source source) throws AWNonUniqueException;

    Source getSource(Long sourceId) throws AWNoSuchEntityException;

    Source getSource(String sourceName) throws AWNoSuchEntityException; // TODO integration test

    void realDelete(Long id);

    Long getCountSourcesLike(String like);

    Long getCountSources();

    void upload(Collection<Source> sources) throws AWNonUniqueException;
}
