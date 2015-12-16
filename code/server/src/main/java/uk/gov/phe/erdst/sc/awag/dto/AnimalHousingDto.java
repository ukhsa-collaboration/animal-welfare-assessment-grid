package uk.gov.phe.erdst.sc.awag.dto;

public class AnimalHousingDto
{
    public final Long housingId;
    public final String housingName;

    public AnimalHousingDto(Long housingId, String housingName)
    {
        this.housingId = housingId;
        this.housingName = housingName;
    }

}
