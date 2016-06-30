package uk.gov.phe.erdst.sc.awag.dto;

import java.util.ArrayList;
import java.util.Collection;

public class EntitySelectDtoCollection
{
    public final Collection<EntitySelectDto> values;

    public EntitySelectDtoCollection(int size)
    {
        values = new ArrayList<>(size);
    }

    public void add(EntitySelectDto dto)
    {
        values.add(dto);
    }
}
