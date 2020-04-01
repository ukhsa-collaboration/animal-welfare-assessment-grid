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
 * The persistent class for the import_template_parameter_factor database table.
 */
@Entity
@Table(name = "import_template_parameter_factor")
@NamedQuery(name = "ImportTemplateParameterFactor.findAll", query = "SELECT i FROM ImportTemplateParameterFactor i")
public class ImportTemplateParameterFactor implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "import_template_parameter_factor_id")
    private Long importTemplateParameterFactorId;

    @Column(name = "factor_name")
    private String factorName;

    private Long factorid;

    // bi-directional many-to-one association to ImportTemplateParameter
    @ManyToOne
    @JoinColumn(name = "import_template_parameter_id")
    private ImportTemplateParameter importTemplateParameter;

    public ImportTemplateParameterFactor()
    {
    }

    public Long getImportTemplateParameterFactorId()
    {
        return this.importTemplateParameterFactorId;
    }

    public void setImportTemplateParameterFactorId(Long importTemplateParameterFactorId)
    {
        this.importTemplateParameterFactorId = importTemplateParameterFactorId;
    }

    public String getFactorName()
    {
        return this.factorName;
    }

    public void setFactorName(String factorName)
    {
        this.factorName = factorName;
    }

    public Long getFactorid()
    {
        return this.factorid;
    }

    public void setFactorid(Long factorid)
    {
        this.factorid = factorid;
    }

    public ImportTemplateParameter getImportTemplateParameter()
    {
        return this.importTemplateParameter;
    }

    public void setImportTemplateParameter(ImportTemplateParameter importTemplateParameter)
    {
        this.importTemplateParameter = importTemplateParameter;
    }

}