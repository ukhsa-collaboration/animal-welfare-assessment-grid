package uk.gov.phe.erdst.sc.awag.webapi.response.userauth;

import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;

public class LogonDetailsDto implements ResponseDto
{
    public String username;
    public String authType;
    public String groupName;
}
