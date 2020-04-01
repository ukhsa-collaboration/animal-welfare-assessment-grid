package uk.gov.phe.erdst.sc.awag.webapi.response.animal;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;

public class AnimalStudyGroupDto implements ResponseDto
{
    public Long id;
    // number due to study create/update client data-mismatch
    public String number;
}
