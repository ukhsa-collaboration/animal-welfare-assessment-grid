package uk.gov.phe.erdst.sc.awag.service.validation.message;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ejb.Singleton;
import javax.validation.Configuration;
import javax.validation.MessageInterpolator;
import javax.validation.Payload;
import javax.validation.Validation;
import javax.validation.metadata.ConstraintDescriptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationAnnotationUtils;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;

@Singleton
public class CustomMessageInterpolator implements MessageInterpolator
{
    private static final Logger LOGGER = LogManager.getLogger(CustomMessageInterpolator.class.getName());
    private static final Object[] PAYLOAD_CONVERSION_ARRAY = new Object[0];

    private final MessageInterpolator mDefaultInterpolator;

    public CustomMessageInterpolator()
    {
        Configuration<?> configuration = Validation.byDefaultProvider().configure();
        mDefaultInterpolator = configuration.getDefaultMessageInterpolator();
    }

    public CustomMessageInterpolator(MessageInterpolator defaultInterpolator)
    {
        mDefaultInterpolator = defaultInterpolator;
    }

    @Override
    public String interpolate(String messageTemplate, Context context)
    {
        if (isMessageSameAsDefault(messageTemplate, context))
        {
            return mDefaultInterpolator.interpolate(messageTemplate, context);
        }

        return interpolateCustom(context.getConstraintDescriptor().getPayload(), messageTemplate);
    }

    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale)
    {
        if (isMessageSameAsDefault(messageTemplate, context))
        {
            return mDefaultInterpolator.interpolate(messageTemplate, context, locale);
        }

        return interpolateCustom(context.getConstraintDescriptor().getPayload(), messageTemplate);
    }

    private static boolean isMessageSameAsDefault(String messageTemplate, Context context)
    {
        ConstraintDescriptor<?> cd = context.getConstraintDescriptor();

        @SuppressWarnings("rawtypes")
        Map defaults = null;

        try
        {
            defaults = ValidationAnnotationUtils.getAnnotationMemberDefaults(cd.getAnnotation());
        }
        catch (ReflectiveOperationException e)
        {
            LOGGER.error(e);
            return false;
        }

        String defaultMessage = (String) defaults.get("message");
        return messageTemplate.equals(defaultMessage);
    }

    private static String interpolateCustom(Set<Class<? extends Payload>> payloadSet, String messageTemplate)
    {
        if (payloadSet.isEmpty())
        {
            return messageTemplate;
        }

        Object templatePayload = payloadSet.toArray(PAYLOAD_CONVERSION_ARRAY)[0];
        String template = ValidationConstants.getAnnotationTemplate(templatePayload);

        if (template != null)
        {
            Object[] parts = messageTemplate.split("\\|");
            String message = String.format(template, parts);
            return message;
        }

        LOGGER
            .error(new IllegalStateException("No error message template for: " + templatePayload.getClass().getName()));

        return ValidationConstants.NO_ERR_TEMPLATE;
    }
}
