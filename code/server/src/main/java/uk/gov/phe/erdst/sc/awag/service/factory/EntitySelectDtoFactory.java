package uk.gov.phe.erdst.sc.awag.service.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.gov.phe.erdst.sc.awag.datamodel.EntitySelect;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;

public class EntitySelectDtoFactory
{

    public List<EntitySelectDto> createEntitySelectDtos(Collection<? extends EntitySelect> entities)
    {
        List<EntitySelectDto> dtos = new ArrayList<EntitySelectDto>();
        for (EntitySelect entity : entities)
        {
            EntitySelectDto dto = createEntitySelectDto(entity);
            dtos.add(dto);
        }
        return dtos;
    }

    public EntitySelectDto createEntitySelectDto(EntitySelect entity)
    {
        return new EntitySelectDto(entity.getEntitySelectId(), entity.getEntitySelectName());
    }

    public EntitySelectDto createEntitySelectDto(EntitySelect entity, String newName)
    {
        return new EntitySelectDto(entity.getEntitySelectId(), newName);
    }
}
