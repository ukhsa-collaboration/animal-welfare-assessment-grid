package uk.gov.phe.erdst.sc.awag.dto;

public final class EntitySelectDto
{
    // CS:OFF: HiddenField|VisibilityModifier
    public final long id;
    public final String entityName;

    public EntitySelectDto(long id, String entityName)
    {
        this.id = id;
        this.entityName = entityName;
    }
    // CS:ON
}
