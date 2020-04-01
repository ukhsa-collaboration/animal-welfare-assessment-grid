package uk.gov.phe.erdst.sc.awag.webapi.response.sex;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;

public class SexesDto implements ResponseDto
{
    public Collection<SexDto> sexes;

    public static class SexDto
    {
        public Long id;
        public String name;

        public SexDto(Long id, String name)
        {
            this.id = id;
            this.name = name;
        }
    }
}
