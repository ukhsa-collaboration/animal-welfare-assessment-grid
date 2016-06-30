package uk.gov.phe.erdst.sc.awag.datamodel;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidIdsArray;

public class TestIdsArrayClientData
{
    @ValidIdsArray
    public Long[] ids;

    public TestIdsArrayClientData(Long... ids)
    {
        this.ids = ids;
    }
}
