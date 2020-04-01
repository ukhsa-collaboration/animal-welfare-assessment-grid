package uk.gov.phe.erdst.sc.awag.service.factory.parameter;

import uk.gov.phe.erdst.sc.awag.datamodel.ImportParameter;
import uk.gov.phe.erdst.sc.awag.datamodel.Parameter;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.webapi.request.ParameterClientData;

public class ParameterFactory
{
    public void update(Parameter parameter, ParameterClientData clientData)
    {
        parameter.setId(clientData.parameterId);
        setNonIdProperties(parameter, clientData);
    }

    public Parameter create(ParameterClientData clientData)
    {
        Parameter parameter = new Parameter();
        if (clientData.parameterId != Constants.UNASSIGNED_ID)
        {
            parameter.setId(clientData.parameterId);
        }
        setNonIdProperties(parameter, clientData);
        return parameter;
    }

    // TODO unit test
    public Parameter create(ImportParameter importParameter)
    {
        Parameter parameter = new Parameter();
        parameter.setName(importParameter.getParameterName());
        return parameter;
    }

    private void setNonIdProperties(Parameter parameter, ParameterClientData clientData)
    {
        parameter.setName(clientData.parameterName);
    }
}
