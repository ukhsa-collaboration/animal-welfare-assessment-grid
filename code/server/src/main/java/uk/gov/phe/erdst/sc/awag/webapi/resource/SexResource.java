package uk.gov.phe.erdst.sc.awag.webapi.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import uk.gov.phe.erdst.sc.awag.businesslogic.SexController;
import uk.gov.phe.erdst.sc.awag.utils.Constants.WebApi;

@Path(WebApi.RESOURCE_SEX_API)
public class SexResource extends GlobalResource
{
    @Inject
    private SexController mSexController;

    /**
     * Get all sex entities.
     * @return sexes dto
     */
    @GET
    public Response getAll()
    {
        return onRetrieveSuccess(mSexController.getSexes());
    }

}
