package uk.gov.phe.erdst.sc.awag.datamodel;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidId;

public class TestIdClientData
{
    @ValidId
    public Long id;

    public TestIdClientData(Long id)
    {
        this.id = id;
    }
}
