package uk.gov.phe.erdst.sc.awag.service.factory.scale;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.Scale;
import uk.gov.phe.erdst.sc.awag.webapi.response.scale.ScaleDto;

@Stateless
public class ScaleDtoFactory
{

    public ScaleDto create(Scale scale)
    {
        ScaleDto scaleDto = new ScaleDto();
        scaleDto.scaleId = scale.getId();
        scaleDto.scaleName = scale.getName();
        scaleDto.scaleMin = scale.getMin();
        scaleDto.scaleMax = scale.getMax();
        return scaleDto;
    }

    public Collection<ScaleDto> createScaleDtos(Collection<Scale> scales)
    {
        Collection<ScaleDto> dto = new ArrayList<>(scales.size());

        for (Scale scale : scales)
        {
            dto.add(create(scale));
        }

        return dto;
    }
}
