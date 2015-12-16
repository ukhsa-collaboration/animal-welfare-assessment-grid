package uk.gov.phe.erdst.sc.awag.dto;

public class ScaleDto
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
