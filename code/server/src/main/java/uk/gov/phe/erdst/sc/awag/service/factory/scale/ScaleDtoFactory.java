package uk.gov.phe.erdst.sc.awag.service.factory.scale;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.Scale;
import uk.gov.phe.erdst.sc.awag.dto.ScaleDto;

@Stateless
public class ScaleDtoFactory
{

    public ScaleDto create(Scale scale)
    {
        ScaleDto scaleDto = new ScaleDto();
        if (scale != null)
        {
            scaleDto.scaleId = scale.getId();
            scaleDto.scaleName = scale.getName();
            scaleDto.scaleMin = scale.getMin();
            scaleDto.scaleMax = scale.getMax();
        }
        return scaleDto;
    }
}
