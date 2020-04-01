package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the import_parameter database table.
 * 
 */
@Entity
@Table(name="import_parameter")
@NamedQuery(name="ImportParameter.findAll", query="SELECT i FROM ImportParameter i")
public class ImportParameter implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long importparameterid;

	@Column(name="line_number")
	private Long lineNumber;

	@Column(name="parameter_name")
	private String parameterName;

	private Long parameterid;

	//bi-directional many-to-one association to ImportHeader
	@ManyToOne
	@JoinColumn(name="importheaderid")
	private ImportHeader importHeader;

	public ImportParameter() {
	}

	public Long getImportparameterid() {
		return this.importparameterid;
	}

	public void setImportparameterid(Long importparameterid) {
		this.importparameterid = importparameterid;
	}

	public Long getLineNumber() {
		return this.lineNumber;
	}

	public void setLineNumber(Long lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getParameterName() {
		return this.parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public Long getParameterid() {
		return this.parameterid;
	}

	public void setParameterid(Long parameterid) {
		this.parameterid = parameterid;
	}

	public ImportHeader getImportHeader() {
		return this.importHeader;
	}

	public void setImportHeader(ImportHeader importHeader) {
		this.importHeader = importHeader;
	}

}