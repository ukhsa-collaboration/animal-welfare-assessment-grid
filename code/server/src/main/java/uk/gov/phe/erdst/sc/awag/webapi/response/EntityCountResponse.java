package uk.gov.phe.erdst.sc.awag.webapi.response;

public class EntityCountResponse implements ResponseDto
{
    public long count = 0;

    public EntityCountResponse(long count)
    {
        this.count = count;
    }
}
