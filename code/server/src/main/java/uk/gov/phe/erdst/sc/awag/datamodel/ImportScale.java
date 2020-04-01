package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the import_scale database table.
 * 
 */
@Entity
@Table(name="import_scale")
@NamedQuery(name="ImportScale.findAll", query="SELECT i FROM ImportScale i")
public class ImportScale implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long importscaleid;

	@Column(name="line_number")
	private Long lineNumber;

	private Integer max;

	private Integer min;

	@Column(name="scale_name")
	private String scaleName;

	private Long scaleid;

	//bi-directional many-to-one association to ImportHeader
	@ManyToOne
	@JoinColumn(name="importheaderid")
	private ImportHeader importHeader;

	public ImportScale() {
	}

	public Long getImportscaleid() {
		return this.importscaleid;
	}

	public void setImportscaleid(Long importscaleid) {
		this.importscaleid = importscaleid;
	}

	public Long getLineNumber() {
		return this.lineNumber;
	}

	public void setLineNumber(Long lineNumber) {
		this.lineNumber = lineNumber;
	}

	public Integer getMax() {
		return this.max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Integer getMin() {
		return this.min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public String getScaleName() {
		return this.scaleName;
	}

	public void setScaleName(String scaleName) {
		this.scaleName = scaleName;
	}

	public Long getScaleid() {
		return this.scaleid;
	}

	public void setScaleid(Long scaleid) {
		this.scaleid = scaleid;
	}

	public ImportHeader getImportHeader() {
		return this.importHeader;
	}

	public void setImportHeader(ImportHeader importHeader) {
		this.importHeader = importHeader;
	}

}