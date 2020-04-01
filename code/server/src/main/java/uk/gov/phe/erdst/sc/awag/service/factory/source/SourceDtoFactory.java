package uk.gov.phe.erdst.sc.awag.service.factory.source;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.Source;
import uk.gov.phe.erdst.sc.awag.webapi.response.source.SourceDto;

@Stateless
public class SourceDtoFactory
{
    public SourceDto create(Source source)
    {
        SourceDto sourceDto = new SourceDto();
        sourceDto.sourceId = source.getId();
        sourceDto.sourceName = source.getName();
        return sourceDto;
    }

    public Collection<SourceDto> createSourceDtos(Collection<Source> sources)
    {
        Collection<SourceDto> dto = new ArrayList<>(sources.size());

        for (Source source : sources)
        {
            dto.add(create(source));
        }

        return dto;
    }
}
