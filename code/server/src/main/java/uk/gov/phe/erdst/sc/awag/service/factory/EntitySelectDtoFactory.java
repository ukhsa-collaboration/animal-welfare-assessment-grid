package uk.gov.phe.erdst.sc.awag.service.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import uk.gov.phe.erdst.sc.awag.datamodel.EntitySelect;
import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDto;

public class EntitySelectDtoFactory
{
    public List<EntitySelectDto> createEntitySelectDtos(List<? extends EntitySelect> entities)
    {
        List<EntitySelectDto> dtos = new ArrayList<EntitySelectDto>();
        for (EntitySelect entity : entities)
        {
            EntitySelectDto dto = createEntitySelectDto(entity);
            dtos.add(dto);
        }
        return dtos;
    }

    public List<EntitySelectDto> createEntitySelectDtos(Set<? extends EntitySelect> entities)
    {
        List<EntitySelectDto> dtos = new ArrayList<EntitySelectDto>();
        for (EntitySelect entity : entities)
        {
            EntitySelectDto dto = createEntitySelectDto(entity);
            dtos.add(dto);
        }
        return dtos;
    }

    private EntitySelectDto createEntitySelectDto(EntitySelect entity)
    {
        return new EntitySelectDto(entity.getEntitySelectId(), entity.getEntitySelectName());
    }
}
