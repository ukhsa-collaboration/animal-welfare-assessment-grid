package uk.gov.phe.erdst.sc.awag.dto;

public class AnimalDto
{
    // CS:OFF: VisibilityModifier

    public Long id;
    public String animalNumber;
    public Long speciesId;
    public String speciesName;
    public Long sourceId;
    public String sourceName;
    public Long sexId;
    public Boolean isFemale;
    public Boolean isAlive;
    public Boolean isAssessed;
    public Long assessmentTemplateId;
    public String assessmentTemplateName;
    public String dateOfBirth;
    public Long fatherId;
    public String fatherName;
    public Long damId;
    public String damName;

    // CS:ON

    public AnimalDto()
    {
    }
}
