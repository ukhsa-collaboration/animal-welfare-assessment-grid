package uk.gov.phe.erdst.sc.awag.service.factory.species;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.ImportSpecy;
import uk.gov.phe.erdst.sc.awag.datamodel.Species;
import uk.gov.phe.erdst.sc.awag.webapi.request.SpeciesClientData;

@Stateless
public class SpeciesFactory
{

    public SpeciesFactory()
    {
    }

    public Species create(SpeciesClientData clientData)
    {
        Species species = new Species();

        setNonIdSpeciesProperties(species, clientData);

        return species;
    }

    public Species create(ImportSpecy importSpecies)
    {
        Species species = new Species();
        species.setName(importSpecies.getSpeciesName());
        return species;
    }

    public void update(Species species, SpeciesClientData clientData)
    {
        setNonIdSpeciesProperties(species, clientData);
    }

    private void setNonIdSpeciesProperties(Species species, SpeciesClientData clientData)
    {
        species.setName(clientData.speciesName);
    }
}
