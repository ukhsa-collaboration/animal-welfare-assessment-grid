package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the import_template database table.
 */
@Entity
@Table(name = "import_template")
@NamedQuery(name = "ImportTemplate.findAll", query = "SELECT i FROM ImportTemplate i")
public class ImportTemplate implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long importtemplateid;

    @Column(name = "assessment_template_name")
    private String assessmentTemplateName;

    @Column(name = "assessment_templateid")
    private Long assessmentTemplateid;

    @Column(name = "line_number")
    private Long lineNumber;

    @Column(name = "parameter_count")
    private Long parameterCount;

    @Column(name = "scale_name")
    private String scaleName;

    private Long scaleid;

    // bi-directional many-to-one association to ImportHeader
    @ManyToOne
    @JoinColumn(name = "importheaderid")
    private ImportHeader importHeader;

    // bi-directional many-to-one association to ImportTemplateParameter
    @OneToMany(mappedBy = "importTemplate")
    private Set<ImportTemplateParameter> importTemplateParameters;

    public ImportTemplate()
    {
    }

    public Long getImporttemplateid()
    {
        return this.importtemplateid;
    }

    public void setImporttemplateid(Long importtemplateid)
    {
        this.importtemplateid = importtemplateid;
    }

    public String getAssessmentTemplateName()
    {
        return this.assessmentTemplateName;
    }

    public void setAssessmentTemplateName(String assessmentTemplateName)
    {
        this.assessmentTemplateName = assessmentTemplateName;
    }

    public Long getAssessmentTemplateid()
    {
        return this.assessmentTemplateid;
    }

    public void setAssessmentTemplateid(Long assessmentTemplateid)
    {
        this.assessmentTemplateid = assessmentTemplateid;
    }

    public Long getLineNumber()
    {
        return this.lineNumber;
    }

    public void setLineNumber(Long lineNumber)
    {
        this.lineNumber = lineNumber;
    }

    public Long getParameterCount()
    {
        return this.parameterCount;
    }

    public void setParameterCount(Long parameterCount)
    {
        this.parameterCount = parameterCount;
    }

    public String getScaleName()
    {
        return this.scaleName;
    }

    public void setScaleName(String scaleName)
    {
        this.scaleName = scaleName;
    }

    public Long getScaleid()
    {
        return this.scaleid;
    }

    public void setScaleid(Long scaleid)
    {
        this.scaleid = scaleid;
    }

    public ImportHeader getImportHeader()
    {
        return this.importHeader;
    }

    public void setImportHeader(ImportHeader importHeader)
    {
        this.importHeader = importHeader;
    }

    public Set<ImportTemplateParameter> getImportTemplateParameters()
    {
        return this.importTemplateParameters;
    }

    public void setImportTemplateParameters(Set<ImportTemplateParameter> importTemplateParameters)
    {
        this.importTemplateParameters = importTemplateParameters;
    }

    public ImportTemplateParameter addImportTemplateParameter(ImportTemplateParameter importTemplateParameter)
    {
        getImportTemplateParameters().add(importTemplateParameter);
        importTemplateParameter.setImportTemplate(this);

        return importTemplateParameter;
    }

    public ImportTemplateParameter removeImportTemplateParameter(ImportTemplateParameter importTemplateParameter)
    {
        getImportTemplateParameters().remove(importTemplateParameter);
        importTemplateParameter.setImportTemplate(null);

        return importTemplateParameter;
    }

}