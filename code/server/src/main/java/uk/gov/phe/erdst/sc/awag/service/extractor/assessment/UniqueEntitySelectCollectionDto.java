package uk.gov.phe.erdst.sc.awag.service.extractor.assessment;

import uk.gov.phe.erdst.sc.awag.dto.EntitySelectDtoCollection;

// CS:OFF: VisibilityModifier|HiddenField
public class UniqueEntitySelectCollectionDto
{
    public final EntitySelectDtoCollection studies;
    public final EntitySelectDtoCollection studyGroups;
    public final EntitySelectDtoCollection animals;
    public final EntitySelectDtoCollection reasons;
    public final EntitySelectDtoCollection users;

    public UniqueEntitySelectCollectionDto(EntitySelectDtoCollection studies, EntitySelectDtoCollection studyGroups,
        EntitySelectDtoCollection animals, EntitySelectDtoCollection reasons, EntitySelectDtoCollection users)
    {
        this.studies = studies;
        this.studyGroups = studyGroups;
        this.animals = animals;
        this.reasons = reasons;
        this.users = users;
    }
}
// CS:ON
