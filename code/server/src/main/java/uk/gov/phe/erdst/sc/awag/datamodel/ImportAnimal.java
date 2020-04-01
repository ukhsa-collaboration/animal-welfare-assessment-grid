package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the import_animal database table.
 */
@Entity
@Table(name = "import_animal")
@NamedQuery(name = ImportAnimal.Q_FIND_IMPORT_ANIMAL_BY_NUMBER,
    query = "SELECT a FROM ImportAnimal a WHERE a.animalNumber = :animalNumber AND a.importHeader.importheaderid = :importHeaderId")
public class ImportAnimal implements Serializable
{

    public static final String Q_FIND_IMPORT_ANIMAL_BY_NUMBER = "findImportAnimalByNumber";
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long importanimalid;

    @Column(name = "animal_number")
    private String animalNumber;

    private Long animalid;

    @Column(name = "assessment_template_name")
    private String assessmentTemplateName;

    private Long assessmenttemplateid;

    @Column(name = "dam_animal_name")
    private String damAnimalName;

    @Column(name = "dam_importanimalid")
    private Long damImportanimalid;

    private Long damanimalid;

    @Column(name = "date_birth")
    private String dateBirth;

    @Column(name = "father_animal_name")
    private String fatherAnimalName;

    @Column(name = "father_importanimalid")
    private Long fatherImportanimalid;

    private Long fatheranimalid;

    private Boolean isalive;

    @Column(name = "line_number")
    private Long lineNumber;

    private String sex;

    private Long sexid;

    @Column(name = "source_name")
    private String sourceName;

    private Long sourceid;

    @Column(name = "species_name")
    private String speciesName;

    private Long speciesid;

    // bi-directional many-to-one association to ImportHeader
    @ManyToOne
    @JoinColumn(name = "importheaderid")
    private ImportHeader importHeader;

    public ImportAnimal()
    {
    }

    public Long getImportanimalid()
    {
        return this.importanimalid;
    }

    public void setImportanimalid(Long importanimalid)
    {
        this.importanimalid = importanimalid;
    }

    public String getAnimalNumber()
    {
        return this.animalNumber;
    }

    public void setAnimalNumber(String animalNumber)
    {
        this.animalNumber = animalNumber;
    }

    public Long getAnimalid()
    {
        return this.animalid;
    }

    public void setAnimalid(Long animalid)
    {
        this.animalid = animalid;
    }

    public String getAssessmentTemplateName()
    {
        return this.assessmentTemplateName;
    }

    public void setAssessmentTemplateName(String assessmentTemplateName)
    {
        this.assessmentTemplateName = assessmentTemplateName;
    }

    public Long getAssessmenttemplateid()
    {
        return this.assessmenttemplateid;
    }

    public void setAssessmenttemplateid(Long assessmenttemplateid)
    {
        this.assessmenttemplateid = assessmenttemplateid;
    }

    public String getDamAnimalName()
    {
        return this.damAnimalName;
    }

    public void setDamAnimalName(String damAnimalName)
    {
        this.damAnimalName = damAnimalName;
    }

    public Long getDamImportanimalid()
    {
        return this.damImportanimalid;
    }

    public void setDamImportanimalid(Long damImportanimalid)
    {
        this.damImportanimalid = damImportanimalid;
    }

    public Long getDamanimalid()
    {
        return this.damanimalid;
    }

    public void setDamanimalid(Long damanimalid)
    {
        this.damanimalid = damanimalid;
    }

    public String getDateBirth()
    {
        return this.dateBirth;
    }

    public void setDateBirth(String dateBirth)
    {
        this.dateBirth = dateBirth;
    }

    public String getFatherAnimalName()
    {
        return this.fatherAnimalName;
    }

    public void setFatherAnimalName(String fatherAnimalName)
    {
        this.fatherAnimalName = fatherAnimalName;
    }

    public Long getFatherImportanimalid()
    {
        return this.fatherImportanimalid;
    }

    public void setFatherImportanimalid(Long fatherImportanimalid)
    {
        this.fatherImportanimalid = fatherImportanimalid;
    }

    public Long getFatheranimalid()
    {
        return this.fatheranimalid;
    }

    public void setFatheranimalid(Long fatheranimalid)
    {
        this.fatheranimalid = fatheranimalid;
    }

    public Boolean getIsalive()
    {
        return this.isalive;
    }

    public void setIsalive(Boolean isalive)
    {
        this.isalive = isalive;
    }

    public Long getLineNumber()
    {
        return this.lineNumber;
    }

    public void setLineNumber(Long lineNumber)
    {
        this.lineNumber = lineNumber;
    }

    public String getSex()
    {
        return this.sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public Long getSexid()
    {
        return this.sexid;
    }

    public void setSexid(Long sexid)
    {
        this.sexid = sexid;
    }

    public String getSourceName()
    {
        return this.sourceName;
    }

    public void setSourceName(String sourceName)
    {
        this.sourceName = sourceName;
    }

    public Long getSourceid()
    {
        return this.sourceid;
    }

    public void setSourceid(Long sourceid)
    {
        this.sourceid = sourceid;
    }

    public String getSpeciesName()
    {
        return this.speciesName;
    }

    public void setSpeciesName(String speciesName)
    {
        this.speciesName = speciesName;
    }

    public Long getSpeciesid()
    {
        return this.speciesid;
    }

    public void setSpeciesid(Long speciesid)
    {
        this.speciesid = speciesid;
    }

    public ImportHeader getImportHeader()
    {
        return this.importHeader;
    }

    public void setImportHeader(ImportHeader importHeader)
    {
        this.importHeader = importHeader;
    }

}