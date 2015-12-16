package uk.gov.phe.erdst.sc.awag.service.validation.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

public final class ValidationAnnotationUtils
{
    private ValidationAnnotationUtils()
    {
    }

    @SuppressWarnings("rawtypes")
    public static Map getAnnotationMemberDefaults(Annotation annotation) throws ReflectiveOperationException
    {
        Field[] fields = annotation.getClass().getSuperclass().getDeclaredFields();
        for (Field field : fields)
        {
            field.setAccessible(true);
            Object instance = field.get(annotation);
            if (isAnnotationInvocationHandler(instance.getClass()))
            {
                Field typeField = instance.getClass().getDeclaredField("type");
                typeField.setAccessible(true);
                Object typeInstance = typeField.get(instance);

                Field annotationTypeField = typeInstance.getClass().getDeclaredField("annotationType");
                annotationTypeField.setAccessible(true);
                Object annotationTypeInstance = annotationTypeField.get(typeInstance);

                Field memberDefaultsField = annotationTypeInstance.getClass().getDeclaredField("memberDefaults");
                memberDefaultsField.setAccessible(true);
                Map memberDefaults = (Map) memberDefaultsField.get(annotationTypeInstance);

                return memberDefaults;
            }
        }

        return Collections.emptyMap();
    }

    private static boolean isAnnotationInvocationHandler(Class<?> clazz)
    {
        try
        {
            clazz.getDeclaredField("memberMethods");
            clazz.getDeclaredField("memberValues");
            clazz.getDeclaredField("type");
        }
        catch (NoSuchFieldException e)
        {
            return false;
        }

        return true;
    }
}
