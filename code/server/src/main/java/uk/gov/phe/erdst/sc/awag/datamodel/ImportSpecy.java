package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the import_species database table.
 * 
 */
@Entity
@Table(name="import_species")
@NamedQuery(name="ImportSpecy.findAll", query="SELECT i FROM ImportSpecy i")
public class ImportSpecy implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long importspeciesid;

	@Column(name="line_number")
	private Long lineNumber;

	@Column(name="species_name")
	private String speciesName;

	private Long speciesid;

	//bi-directional many-to-one association to ImportHeader
	@ManyToOne
	@JoinColumn(name="importheaderid")
	private ImportHeader importHeader;

	public ImportSpecy() {
	}

	public Long getImportspeciesid() {
		return this.importspeciesid;
	}

	public void setImportspeciesid(Long importspeciesid) {
		this.importspeciesid = importspeciesid;
	}

	public Long getLineNumber() {
		return this.lineNumber;
	}

	public void setLineNumber(Long lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getSpeciesName() {
		return this.speciesName;
	}

	public void setSpeciesName(String speciesName) {
		this.speciesName = speciesName;
	}

	public Long getSpeciesid() {
		return this.speciesid;
	}

	public void setSpeciesid(Long speciesid) {
		this.speciesid = speciesid;
	}

	public ImportHeader getImportHeader() {
		return this.importHeader;
	}

	public void setImportHeader(ImportHeader importHeader) {
		this.importHeader = importHeader;
	}

}