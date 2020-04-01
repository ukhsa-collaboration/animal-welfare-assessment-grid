package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the import_header database table.
 */
@Entity
@Table(name = "import_header")
@NamedQuery(name = "ImportHeader.findAll", query = "SELECT i FROM ImportHeader i")
public class ImportHeader implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long importheaderid;

    @Column(name = "activity_log_action")
    private String activityLogAction;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_import")
    private Date dateImport;

    @Column(name = "user_name")
    private String userName;

    // bi-directional many-to-one association to ImportAnimal
    @OneToMany(mappedBy = "importHeader")
    private Set<ImportAnimal> importAnimals;

    // bi-directional many-to-one association to ImportAnimalHousing
    @OneToMany(mappedBy = "importHeader")
    private Set<ImportAnimalHousing> importAnimalHousings;

    // bi-directional many-to-one association to ImportAssessment
    @OneToMany(mappedBy = "importHeader")
    private Set<ImportAssessment> importAssessments;

    // bi-directional many-to-one association to ImportAssessmentReason
    @OneToMany(mappedBy = "importHeader")
    private Set<ImportAssessmentReason> importAssessmentReasons;

    // bi-directional many-to-one association to ImportFactor
    @OneToMany(mappedBy = "importHeader")
    private Set<ImportFactor> importFactors;

    // bi-directional many-to-one association to ImportParameter
    @OneToMany(mappedBy = "importHeader")
    private Set<ImportParameter> importParameters;

    // bi-directional many-to-one association to ImportScale
    @OneToMany(mappedBy = "importHeader")
    private Set<ImportScale> importScales;

    // bi-directional many-to-one association to ImportSource
    @OneToMany(mappedBy = "importHeader")
    private Set<ImportSource> importSources;

    // bi-directional many-to-one association to ImportSpecy
    @OneToMany(mappedBy = "importHeader")
    private Set<ImportSpecy> importSpecies;

    // bi-directional many-to-one association to ImportStudy
    @OneToMany(mappedBy = "importHeader")
    private Set<ImportStudy> importStudies;

    // bi-directional many-to-one association to ImportStudyGroup
    @OneToMany(mappedBy = "importHeader")
    private Set<ImportStudyGroup> importStudyGroups;

    // bi-directional many-to-one association to ImportTemplate
    @OneToMany(mappedBy = "importHeader")
    private Set<ImportTemplate> importTemplates;

    public ImportHeader()
    {
    }

    public Long getImportheaderid()
    {
        return this.importheaderid;
    }

    public void setImportheaderid(Long importheaderid)
    {
        this.importheaderid = importheaderid;
    }

    public String getActivityLogAction()
    {
        return this.activityLogAction;
    }

    public void setActivityLogAction(String activityLogAction)
    {
        this.activityLogAction = activityLogAction;
    }

    public Date getDateImport()
    {
        return this.dateImport;
    }

    public void setDateImport(Date dateImport)
    {
        this.dateImport = dateImport;
    }

    public String getUserName()
    {
        return this.userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public Set<ImportAnimal> getImportAnimals()
    {
        return this.importAnimals;
    }

    public void setImportAnimals(Set<ImportAnimal> importAnimals)
    {
        this.importAnimals = importAnimals;
    }

    public ImportAnimal addImportAnimal(ImportAnimal importAnimal)
    {
        getImportAnimals().add(importAnimal);
        importAnimal.setImportHeader(this);

        return importAnimal;
    }

    public ImportAnimal removeImportAnimal(ImportAnimal importAnimal)
    {
        getImportAnimals().remove(importAnimal);
        importAnimal.setImportHeader(null);

        return importAnimal;
    }

    public Set<ImportAnimalHousing> getImportAnimalHousings()
    {
        return this.importAnimalHousings;
    }

    public void setImportAnimalHousings(Set<ImportAnimalHousing> importAnimalHousings)
    {
        this.importAnimalHousings = importAnimalHousings;
    }

    public ImportAnimalHousing addImportAnimalHousing(ImportAnimalHousing importAnimalHousing)
    {
        getImportAnimalHousings().add(importAnimalHousing);
        importAnimalHousing.setImportHeader(this);

        return importAnimalHousing;
    }

    public ImportAnimalHousing removeImportAnimalHousing(ImportAnimalHousing importAnimalHousing)
    {
        getImportAnimalHousings().remove(importAnimalHousing);
        importAnimalHousing.setImportHeader(null);

        return importAnimalHousing;
    }

    public Set<ImportAssessment> getImportAssessments()
    {
        return this.importAssessments;
    }

    public void setImportAssessments(Set<ImportAssessment> importAssessments)
    {
        this.importAssessments = importAssessments;
    }

    public ImportAssessment addImportAssessment(ImportAssessment importAssessment)
    {
        getImportAssessments().add(importAssessment);
        importAssessment.setImportHeader(this);

        return importAssessment;
    }

    public ImportAssessment removeImportAssessment(ImportAssessment importAssessment)
    {
        getImportAssessments().remove(importAssessment);
        importAssessment.setImportHeader(null);

        return importAssessment;
    }

    public Set<ImportAssessmentReason> getImportAssessmentReasons()
    {
        return this.importAssessmentReasons;
    }

    public void setImportAssessmentReasons(Set<ImportAssessmentReason> importAssessmentReasons)
    {
        this.importAssessmentReasons = importAssessmentReasons;
    }

    public ImportAssessmentReason addImportAssessmentReason(ImportAssessmentReason importAssessmentReason)
    {
        getImportAssessmentReasons().add(importAssessmentReason);
        importAssessmentReason.setImportHeader(this);

        return importAssessmentReason;
    }

    public ImportAssessmentReason removeImportAssessmentReason(ImportAssessmentReason importAssessmentReason)
    {
        getImportAssessmentReasons().remove(importAssessmentReason);
        importAssessmentReason.setImportHeader(null);

        return importAssessmentReason;
    }

    public Set<ImportFactor> getImportFactors()
    {
        return this.importFactors;
    }

    public void setImportFactors(Set<ImportFactor> importFactors)
    {
        this.importFactors = importFactors;
    }

    public ImportFactor addImportFactor(ImportFactor importFactor)
    {
        getImportFactors().add(importFactor);
        importFactor.setImportHeader(this);

        return importFactor;
    }

    public ImportFactor removeImportFactor(ImportFactor importFactor)
    {
        getImportFactors().remove(importFactor);
        importFactor.setImportHeader(null);

        return importFactor;
    }

    public Set<ImportParameter> getImportParameters()
    {
        return this.importParameters;
    }

    public void setImportParameters(Set<ImportParameter> importParameters)
    {
        this.importParameters = importParameters;
    }

    public ImportParameter addImportParameter(ImportParameter importParameter)
    {
        getImportParameters().add(importParameter);
        importParameter.setImportHeader(this);

        return importParameter;
    }

    public ImportParameter removeImportParameter(ImportParameter importParameter)
    {
        getImportParameters().remove(importParameter);
        importParameter.setImportHeader(null);

        return importParameter;
    }

    public Set<ImportScale> getImportScales()
    {
        return this.importScales;
    }

    public void setImportScales(Set<ImportScale> importScales)
    {
        this.importScales = importScales;
    }

    public ImportScale addImportScale(ImportScale importScale)
    {
        getImportScales().add(importScale);
        importScale.setImportHeader(this);

        return importScale;
    }

    public ImportScale removeImportScale(ImportScale importScale)
    {
        getImportScales().remove(importScale);
        importScale.setImportHeader(null);

        return importScale;
    }

    public Set<ImportSource> getImportSources()
    {
        return this.importSources;
    }

    public void setImportSources(Set<ImportSource> importSources)
    {
        this.importSources = importSources;
    }

    public ImportSource addImportSource(ImportSource importSource)
    {
        getImportSources().add(importSource);
        importSource.setImportHeader(this);

        return importSource;
    }

    public ImportSource removeImportSource(ImportSource importSource)
    {
        getImportSources().remove(importSource);
        importSource.setImportHeader(null);

        return importSource;
    }

    public Set<ImportSpecy> getImportSpecies()
    {
        return this.importSpecies;
    }

    public void setImportSpecies(Set<ImportSpecy> importSpecies)
    {
        this.importSpecies = importSpecies;
    }

    public ImportSpecy addImportSpecy(ImportSpecy importSpecy)
    {
        getImportSpecies().add(importSpecy);
        importSpecy.setImportHeader(this);

        return importSpecy;
    }

    public ImportSpecy removeImportSpecy(ImportSpecy importSpecy)
    {
        getImportSpecies().remove(importSpecy);
        importSpecy.setImportHeader(null);

        return importSpecy;
    }

    public Set<ImportStudy> getImportStudies()
    {
        return this.importStudies;
    }

    public void setImportStudies(Set<ImportStudy> importStudies)
    {
        this.importStudies = importStudies;
    }

    public ImportStudy addImportStudy(ImportStudy importStudy)
    {
        getImportStudies().add(importStudy);
        importStudy.setImportHeader(this);

        return importStudy;
    }

    public ImportStudy removeImportStudy(ImportStudy importStudy)
    {
        getImportStudies().remove(importStudy);
        importStudy.setImportHeader(null);

        return importStudy;
    }

    public Set<ImportStudyGroup> getImportStudyGroups()
    {
        return this.importStudyGroups;
    }

    public void setImportStudyGroups(Set<ImportStudyGroup> importStudyGroups)
    {
        this.importStudyGroups = importStudyGroups;
    }

    public ImportStudyGroup addImportStudyGroup(ImportStudyGroup importStudyGroup)
    {
        getImportStudyGroups().add(importStudyGroup);
        importStudyGroup.setImportHeader(this);

        return importStudyGroup;
    }

    public ImportStudyGroup removeImportStudyGroup(ImportStudyGroup importStudyGroup)
    {
        getImportStudyGroups().remove(importStudyGroup);
        importStudyGroup.setImportHeader(null);

        return importStudyGroup;
    }

    public Set<ImportTemplate> getImportTemplates()
    {
        return this.importTemplates;
    }

    public void setImportTemplates(Set<ImportTemplate> importTemplates)
    {
        this.importTemplates = importTemplates;
    }

    public ImportTemplate addImportTemplate(ImportTemplate importTemplate)
    {
        getImportTemplates().add(importTemplate);
        importTemplate.setImportHeader(this);

        return importTemplate;
    }

    public ImportTemplate removeImportTemplate(ImportTemplate importTemplate)
    {
        getImportTemplates().remove(importTemplate);
        importTemplate.setImportHeader(null);

        return importTemplate;
    }

}