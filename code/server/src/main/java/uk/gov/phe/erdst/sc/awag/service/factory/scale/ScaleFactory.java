package uk.gov.phe.erdst.sc.awag.service.factory.scale;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.Scale;
import uk.gov.phe.erdst.sc.awag.datamodel.client.ScaleClientData;
import uk.gov.phe.erdst.sc.awag.utils.Constants;

@Stateless
public class ScaleFactory
{
    public Scale create(ScaleClientData clientData)
    {
        Scale scale = new Scale();
        if (clientData.scaleId != Constants.UNASSIGNED_ID)
        {
            scale.setId(clientData.scaleId);
        }
        scale.setName(clientData.scaleName);
        scale.setMin(clientData.scaleMin);
        scale.setMax(clientData.scaleMax);
        return scale;
    }

    public void update(Scale scale, ScaleClientData clientData)
    {
        if (scale != null)
        {
            scale.setId(clientData.scaleId);
            scale.setName(clientData.scaleName);
            scale.setMin(clientData.scaleMin);
            scale.setMax(clientData.scaleMax);
        }
    }
}
