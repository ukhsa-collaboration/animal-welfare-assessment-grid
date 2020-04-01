package uk.gov.phe.erdst.sc.awag.service.factory.factor;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.Factor;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportFactor;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.webapi.request.FactorClientData;

@Stateless
public class FactorFactory
{
    public Factor create(FactorClientData clientData)
    {
        Factor scoringFactor = new Factor();
        if (clientData.factorId != Constants.UNASSIGNED_ID)
        {
            scoringFactor.setId(clientData.factorId);
        }
        scoringFactor.setName(clientData.factorName);
        scoringFactor.setFactorDescription(clientData.factorDescription);
        return scoringFactor;
    }

    public Factor create(ImportFactor importFactor)
    {
        Factor scoringFactor = new Factor();
        scoringFactor.setName(importFactor.getFactorName());
        scoringFactor.setFactorDescription(importFactor.getFactorDescription());
        return scoringFactor;
    }

    public void update(Factor scoringFactor, FactorClientData clientData)
    {
        if (scoringFactor != null)
        {
            scoringFactor.setId(clientData.factorId);
            scoringFactor.setName(clientData.factorName);
            scoringFactor.setFactorDescription(clientData.factorDescription);
        }
    }
}
