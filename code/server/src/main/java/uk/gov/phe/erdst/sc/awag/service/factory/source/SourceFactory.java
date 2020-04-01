package uk.gov.phe.erdst.sc.awag.service.factory.source;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.ImportSource;
import uk.gov.phe.erdst.sc.awag.datamodel.Source;
import uk.gov.phe.erdst.sc.awag.webapi.request.SourceClientData;

@Stateless
public class SourceFactory
{

    public Source create(SourceClientData clientRequestData)
    {
        Source source = new Source();
        source.setName(clientRequestData.sourceName);
        return source;
    }

    public Source create(ImportSource importSource)
    {
        Source source = new Source();
        source.setName(importSource.getSourceName());
        return source;
    }

    public void update(Source source, SourceClientData clientData)
    {
        setNonIdSourceProperties(source, clientData);
    }

    private void setNonIdSourceProperties(Source source, SourceClientData clientData)
    {
        source.setName(clientData.sourceName);
    }
}
