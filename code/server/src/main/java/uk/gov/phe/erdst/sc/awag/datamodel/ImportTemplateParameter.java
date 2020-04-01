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
 * The persistent class for the import_template_parameter database table.
 */
@Entity
@Table(name = "import_template_parameter")
@NamedQuery(name = "ImportTemplateParameter.findAll", query = "SELECT i FROM ImportTemplateParameter i")
public class ImportTemplateParameter implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "import_template_parameter_id")
    private Long importTemplateParameterId;

    private String factors;

    @Column(name = "parameter_name")
    private String parameterName;

    private Long parameterid;

    // bi-directional many-to-one association to ImportTemplate
    @ManyToOne
    @JoinColumn(name = "importtemplateid")
    private ImportTemplate importTemplate;

    // bi-directional many-to-one association to ImportTemplateParameterFactor
    @OneToMany(mappedBy = "importTemplateParameter")
    private Set<ImportTemplateParameterFactor> importTemplateParameterFactors;

    public ImportTemplateParameter()
    {
    }

    public Long getImportTemplateParameterId()
    {
        return this.importTemplateParameterId;
    }

    public void setImportTemplateParameterId(Long importTemplateParameterId)
    {
        this.importTemplateParameterId = importTemplateParameterId;
    }

    public String getFactors()
    {
        return this.factors;
    }

    public void setFactors(String factors)
    {
        this.factors = factors;
    }

    public String getParameterName()
    {
        return this.parameterName;
    }

    public void setParameterName(String parameterName)
    {
        this.parameterName = parameterName;
    }

    public Long getParameterid()
    {
        return this.parameterid;
    }

    public void setParameterid(Long parameterid)
    {
        this.parameterid = parameterid;
    }

    public ImportTemplate getImportTemplate()
    {
        return this.importTemplate;
    }

    public void setImportTemplate(ImportTemplate importTemplate)
    {
        this.importTemplate = importTemplate;
    }

    public Set<ImportTemplateParameterFactor> getImportTemplateParameterFactors()
    {
        return this.importTemplateParameterFactors;
    }

    public void setImportTemplateParameterFactors(Set<ImportTemplateParameterFactor> importTemplateParameterFactors)
    {
        this.importTemplateParameterFactors = importTemplateParameterFactors;
    }

    public ImportTemplateParameterFactor
        addImportTemplateParameterFactor(ImportTemplateParameterFactor importTemplateParameterFactor)
    {
        getImportTemplateParameterFactors().add(importTemplateParameterFactor);
        importTemplateParameterFactor.setImportTemplateParameter(this);

        return importTemplateParameterFactor;
    }

    public ImportTemplateParameterFactor
        removeImportTemplateParameterFactor(ImportTemplateParameterFactor importTemplateParameterFactor)
    {
        getImportTemplateParameterFactors().remove(importTemplateParameterFactor);
        importTemplateParameterFactor.setImportTemplateParameter(null);

        return importTemplateParameterFactor;
    }

}