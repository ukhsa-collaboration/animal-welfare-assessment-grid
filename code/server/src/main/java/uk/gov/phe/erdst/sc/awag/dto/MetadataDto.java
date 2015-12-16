package uk.gov.phe.erdst.sc.awag.dto;

public class MetadataDto
{
    public String self;
    public String next;
    public String previous;
    public Integer limit;
    public Integer offset;

    public MetadataDto()
    {
    }

    public MetadataDto(String self, String next, String previous, Integer limit, Integer offset)
    {
        this.self = self;
        this.next = next;
        this.previous = previous;
        this.limit = limit;
        this.offset = offset;
    }
}
