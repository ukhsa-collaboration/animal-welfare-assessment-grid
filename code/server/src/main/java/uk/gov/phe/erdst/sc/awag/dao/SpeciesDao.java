package uk.gov.phe.erdst.sc.awag.dao;

import java.util.Collection;
import java.util.List;

import uk.gov.phe.erdst.sc.awag.datamodel.Species;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNonUniqueException;

public interface SpeciesDao extends UniqueDao
{
    Collection<Species> getSpecies(Integer offset, Integer limit);

    Species getSpecies(String speciesName) throws AWNoSuchEntityException; // TODO integration test

    List<Species> getSpeciesLike(String like, Integer offset, Integer limit);

    Species store(Species species) throws AWNonUniqueException;

    void updateIsDeleted(Long speciesId, boolean isDeleted) throws AWNoSuchEntityException;

    void realDelete(Long id);

    Species getSpecies(Long id) throws AWNoSuchEntityException;

    Long getCountSpecies();

    Long getCountSpeciesLike(String like);

    void upload(Collection<Species> species) throws AWNonUniqueException;

}
