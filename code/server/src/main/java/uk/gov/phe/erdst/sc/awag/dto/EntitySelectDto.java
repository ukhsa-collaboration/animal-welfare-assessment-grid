package uk.gov.phe.erdst.sc.awag.dto;

public final class EntitySelectDto
{
    // CS:OFF: HiddenField|VisibilityModifier
    public final Object id;
    public final String entityName;

    public EntitySelectDto(Object object, String entityName)
    {
        this.id = object;
        this.entityName = entityName;
    }
    // CS:ON
}
