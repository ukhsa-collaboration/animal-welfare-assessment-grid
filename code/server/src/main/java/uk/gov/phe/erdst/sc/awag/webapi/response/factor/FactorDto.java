package uk.gov.phe.erdst.sc.awag.webapi.response.factor;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;

public class FactorDto implements ResponseDto
{
    public Long factorId;
    public String factorName;
    public String factorDescription;

    public FactorDto()
    {
    }

    public FactorDto(Long factorId, String factorName, String factorDescription)
    {
        this.factorId = factorId;
        this.factorName = factorName;
        this.factorDescription = factorDescription;
    }
}
