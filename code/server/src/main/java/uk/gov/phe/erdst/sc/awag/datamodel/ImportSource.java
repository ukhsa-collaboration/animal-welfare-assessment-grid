package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the import_source database table.
 * 
 */
@Entity
@Table(name="import_source")
@NamedQuery(name="ImportSource.findAll", query="SELECT i FROM ImportSource i")
public class ImportSource implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long importsourceid;

	@Column(name="line_number")
	private Long lineNumber;

	@Column(name="source_name")
	private String sourceName;

	private Long sourceid;

	//bi-directional many-to-one association to ImportHeader
	@ManyToOne
	@JoinColumn(name="importheaderid")
	private ImportHeader importHeader;

	public ImportSource() {
	}

	public Long getImportsourceid() {
		return this.importsourceid;
	}

	public void setImportsourceid(Long importsourceid) {
		this.importsourceid = importsourceid;
	}

	public Long getLineNumber() {
		return this.lineNumber;
	}

	public void setLineNumber(Long lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getSourceName() {
		return this.sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public Long getSourceid() {
		return this.sourceid;
	}

	public void setSourceid(Long sourceid) {
		this.sourceid = sourceid;
	}

	public ImportHeader getImportHeader() {
		return this.importHeader;
	}

	public void setImportHeader(ImportHeader importHeader) {
		this.importHeader = importHeader;
	}

}