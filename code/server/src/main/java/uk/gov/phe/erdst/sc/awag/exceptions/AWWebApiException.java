package uk.gov.phe.erdst.sc.awag.exceptions;

import java.util.List;

public interface AWWebApiException
{
    List<String> getErrors();
}
