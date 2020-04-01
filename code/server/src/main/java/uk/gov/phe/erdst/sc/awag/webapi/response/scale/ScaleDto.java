package uk.gov.phe.erdst.sc.awag.webapi.response.scale;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;

public class ScaleDto implements ResponseDto
{
    public Long scaleId;
    public String scaleName;
    public Integer scaleMin;
    public Integer scaleMax;

    public ScaleDto()
    {
    }

    public ScaleDto(Long scaleId, String scaleName, Integer scaleMin, Integer scaleMax)
    {
        this.scaleId = scaleId;
        this.scaleName = scaleName;
        this.scaleMin = scaleMin;
        this.scaleMax = scaleMax;
    }
}
