package uk.gov.phe.erdst.sc.awag.dto;

public class FactorDto
{
    public Long factorId;
    public String factorName;

    public FactorDto()
    {
    }

    public FactorDto(Long factorId, String factorName)
    {
        this.factorId = factorId;
        this.factorName = factorName;
    }

    public FactorDto(Long factorId, String factorName, Long factorScore)
    {
        this.factorId = factorId;
        this.factorName = factorName;
    }
}
