package uk.gov.phe.erdst.sc.awag.webapi;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import uk.gov.phe.erdst.sc.awag.webapi.exceptions.InputValidationExceptionMapper;
import uk.gov.phe.erdst.sc.awag.webapi.exceptions.NoSuchEntityExceptionMapper;
import uk.gov.phe.erdst.sc.awag.webapi.exceptions.NonUniqueExceptionMapper;
import uk.gov.phe.erdst.sc.awag.webapi.exceptions.SeriousExceptionMapper;
import uk.gov.phe.erdst.sc.awag.webapi.resource.ActivityLogResource;
import uk.gov.phe.erdst.sc.awag.webapi.resource.AnimalHousingResource;
import uk.gov.phe.erdst.sc.awag.webapi.resource.AnimalResource;
import uk.gov.phe.erdst.sc.awag.webapi.resource.AssessmentResource;
import uk.gov.phe.erdst.sc.awag.webapi.resource.AssessmentTemplateResource;
import uk.gov.phe.erdst.sc.awag.webapi.resource.AuthenticationResource;
import uk.gov.phe.erdst.sc.awag.webapi.resource.FactorResource;
import uk.gov.phe.erdst.sc.awag.webapi.resource.MyJacksonObjectMapper;
import uk.gov.phe.erdst.sc.awag.webapi.resource.ParameterResource;
import uk.gov.phe.erdst.sc.awag.webapi.resource.ReasonResource;
import uk.gov.phe.erdst.sc.awag.webapi.resource.ScaleResource;
import uk.gov.phe.erdst.sc.awag.webapi.resource.SexResource;
import uk.gov.phe.erdst.sc.awag.webapi.resource.SourceResource;
import uk.gov.phe.erdst.sc.awag.webapi.resource.SpeciesResource;
import uk.gov.phe.erdst.sc.awag.webapi.resource.StudyGroupResource;
import uk.gov.phe.erdst.sc.awag.webapi.resource.StudyResource;
import uk.gov.phe.erdst.sc.awag.webapi.resource.UserAuthResource;
import uk.gov.phe.erdst.sc.awag.webapi.resource.UserResource;

@ApplicationPath("webapi")
public class MyApplication extends ResourceConfig
{
    public MyApplication()
    {
        // Resource classes
        register(AnimalResource.class);
        register(AssessmentResource.class);
        register(ActivityLogResource.class);
        register(FactorResource.class);
        register(AnimalHousingResource.class);
        register(ReasonResource.class);
        register(ParameterResource.class);
        register(ScaleResource.class);
        register(SourceResource.class);
        register(SpeciesResource.class);
        register(StudyGroupResource.class);
        register(StudyResource.class);
        register(AssessmentTemplateResource.class);
        register(SexResource.class);
        register(UserResource.class);
        register(UserAuthResource.class);
        register(AuthenticationResource.class);

        // JSON provider marshaller
        register(MyJacksonObjectMapper.class);

        // Exception mappers
        register(InputValidationExceptionMapper.class);
        register(NoSuchEntityExceptionMapper.class);
        register(NonUniqueExceptionMapper.class);
        register(SeriousExceptionMapper.class);

        // JSON provider
        register(JacksonFeature.class);
        register(MultiPartFeature.class);

    }
}
