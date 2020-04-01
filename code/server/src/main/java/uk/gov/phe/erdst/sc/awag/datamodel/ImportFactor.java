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
 * The persistent class for the import_factor database table.
 */
@Entity
@Table(name = "import_factor")
@NamedQuery(name = "ImportFactor.findAll", query = "SELECT i FROM ImportFactor i")
public class ImportFactor implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long importfactorid;

    @Column(name = "factor_name")
    private String factorName;

    private Long factorid;

    @Column(name = "line_number")
    private Long lineNumber;

    @Column(name = "factor_description")
    private String factorDescription;

    // bi-directional many-to-one association to ImportHeader
    @ManyToOne
    @JoinColumn(name = "importheaderid")
    private ImportHeader importHeader;

    public ImportFactor()
    {
    }

    public Long getImportfactorid()
    {
        return this.importfactorid;
    }

    public void setImportfactorid(Long importfactorid)
    {
        this.importfactorid = importfactorid;
    }

    public String getFactorName()
    {
        return this.factorName;
    }

    public void setFactorName(String factorName)
    {
        this.factorName = factorName;
    }

    public String getFactorDescription()
    {
        return factorDescription;
    }

    public void setFactorDescription(String factorDescription)
    {
        this.factorDescription = factorDescription;
    }

    public Long getFactorid()
    {
        return this.factorid;
    }

    public void setFactorid(Long factorid)
    {
        this.factorid = factorid;
    }

    public Long getLineNumber()
    {
        return this.lineNumber;
    }

    public void setLineNumber(Long lineNumber)
    {
        this.lineNumber = lineNumber;
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