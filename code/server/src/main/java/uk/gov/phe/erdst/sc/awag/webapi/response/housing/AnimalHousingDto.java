package uk.gov.phe.erdst.sc.awag.webapi.response.housing;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;

public class AnimalHousingDto implements ResponseDto
{
    public final Long housingId;
    public final String housingName;

    public AnimalHousingDto(Long housingId, String housingName)
    {
        this.housingId = housingId;
        this.housingName = housingName;
    }

}
