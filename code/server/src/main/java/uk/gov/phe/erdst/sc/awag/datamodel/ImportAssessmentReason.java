package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the import_assessment_reason database table.
 * 
 */
@Entity
@Table(name="import_assessment_reason")
@NamedQuery(name="ImportAssessmentReason.findAll", query="SELECT i FROM ImportAssessmentReason i")
public class ImportAssessmentReason implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="importassessment_reasonid")
	private Long importassessmentReasonid;

	@Column(name="assessment_reason_name")
	private String assessmentReasonName;

	private Long assessmentreasonid;

	@Column(name="line_number")
	private Long lineNumber;

	//bi-directional many-to-one association to ImportHeader
	@ManyToOne
	@JoinColumn(name="importheaderid")
	private ImportHeader importHeader;

	public ImportAssessmentReason() {
	}

	public Long getImportassessmentReasonid() {
		return this.importassessmentReasonid;
	}

	public void setImportassessmentReasonid(Long importassessmentReasonid) {
		this.importassessmentReasonid = importassessmentReasonid;
	}

	public String getAssessmentReasonName() {
		return this.assessmentReasonName;
	}

	public void setAssessmentReasonName(String assessmentReasonName) {
		this.assessmentReasonName = assessmentReasonName;
	}

	public Long getAssessmentreasonid() {
		return this.assessmentreasonid;
	}

	public void setAssessmentreasonid(Long assessmentreasonid) {
		this.assessmentreasonid = assessmentreasonid;
	}

	public Long getLineNumber() {
		return this.lineNumber;
	}

	public void setLineNumber(Long lineNumber) {
		this.lineNumber = lineNumber;
	}

	public ImportHeader getImportHeader() {
		return this.importHeader;
	}

	public void setImportHeader(ImportHeader importHeader) {
		this.importHeader = importHeader;
	}

}