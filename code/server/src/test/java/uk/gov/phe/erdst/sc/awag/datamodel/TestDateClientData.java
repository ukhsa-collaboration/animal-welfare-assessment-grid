package uk.gov.phe.erdst.sc.awag.datamodel;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidDate;

public class TestDateClientData
{
    @ValidDate
    public String date;

    public TestDateClientData(String date)
    {
        this.date = date;
    }
}
