package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the import_assessment database table.
 * 
 */
@Entity
@Table(name="import_assessment")
@NamedQuery(name="ImportAssessment.findAll", query="SELECT i FROM ImportAssessment i")
public class ImportAssessment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long importassessmentid;

	@Column(name="animal_housing_name")
	private String animalHousingName;

	@Column(name="animal_number")
	private String animalNumber;

	private Long animalhousingid;

	private Long animalnumberid;

	@Column(name="assessmentreason_name")
	private String assessmentreasonName;

	private Long assessmentreasonid;

	@Column(name="date_assessment")
	private String dateAssessment;

	private Long importassessmenttemplateid;

	private Boolean iscomplete;

	@Column(name="line_number")
	private Long lineNumber;

	@Column(name="performed_by_user")
	private String performedByUser;

	@Column(name="performed_by_user_id")
	private Long performedByUserId;

	//bi-directional many-to-one association to ImportHeader
	@ManyToOne
	@JoinColumn(name="importheaderid")
	private ImportHeader importHeader;

	//bi-directional many-to-one association to ImportAssessmentParameterFactorScore
	@OneToMany(mappedBy="importAssessment")
	private Set<ImportAssessmentParameterFactorScore> importAssessmentParameterFactorScores;

	public ImportAssessment() {
	}

	public Long getImportassessmentid() {
		return this.importassessmentid;
	}

	public void setImportassessmentid(Long importassessmentid) {
		this.importassessmentid = importassessmentid;
	}

	public String getAnimalHousingName() {
		return this.animalHousingName;
	}

	public void setAnimalHousingName(String animalHousingName) {
		this.animalHousingName = animalHousingName;
	}

	public String getAnimalNumber() {
		return this.animalNumber;
	}

	public void setAnimalNumber(String animalNumber) {
		this.animalNumber = animalNumber;
	}

	public Long getAnimalhousingid() {
		return this.animalhousingid;
	}

	public void setAnimalhousingid(Long animalhousingid) {
		this.animalhousingid = animalhousingid;
	}

	public Long getAnimalnumberid() {
		return this.animalnumberid;
	}

	public void setAnimalnumberid(Long animalnumberid) {
		this.animalnumberid = animalnumberid;
	}

	public String getAssessmentreasonName() {
		return this.assessmentreasonName;
	}

	public void setAssessmentreasonName(String assessmentreasonName) {
		this.assessmentreasonName = assessmentreasonName;
	}

	public Long getAssessmentreasonid() {
		return this.assessmentreasonid;
	}

	public void setAssessmentreasonid(Long assessmentreasonid) {
		this.assessmentreasonid = assessmentreasonid;
	}

	public String getDateAssessment() {
		return this.dateAssessment;
	}

	public void setDateAssessment(String dateAssessment) {
		this.dateAssessment = dateAssessment;
	}

	public Long getImportassessmenttemplateid() {
		return this.importassessmenttemplateid;
	}

	public void setImportassessmenttemplateid(Long importassessmenttemplateid) {
		this.importassessmenttemplateid = importassessmenttemplateid;
	}

	public Boolean getIscomplete() {
		return this.iscomplete;
	}

	public void setIscomplete(Boolean iscomplete) {
		this.iscomplete = iscomplete;
	}

	public Long getLineNumber() {
		return this.lineNumber;
	}

	public void setLineNumber(Long lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getPerformedByUser() {
		return this.performedByUser;
	}

	public void setPerformedByUser(String performedByUser) {
		this.performedByUser = performedByUser;
	}

	public Long getPerformedByUserId() {
		return this.performedByUserId;
	}

	public void setPerformedByUserId(Long performedByUserId) {
		this.performedByUserId = performedByUserId;
	}

	public ImportHeader getImportHeader() {
		return this.importHeader;
	}

	public void setImportHeader(ImportHeader importHeader) {
		this.importHeader = importHeader;
	}

	public Set<ImportAssessmentParameterFactorScore> getImportAssessmentParameterFactorScores() {
		return this.importAssessmentParameterFactorScores;
	}

	public void setImportAssessmentParameterFactorScores(Set<ImportAssessmentParameterFactorScore> importAssessmentParameterFactorScores) {
		this.importAssessmentParameterFactorScores = importAssessmentParameterFactorScores;
	}

	public ImportAssessmentParameterFactorScore addImportAssessmentParameterFactorScore(ImportAssessmentParameterFactorScore importAssessmentParameterFactorScore) {
		getImportAssessmentParameterFactorScores().add(importAssessmentParameterFactorScore);
		importAssessmentParameterFactorScore.setImportAssessment(this);

		return importAssessmentParameterFactorScore;
	}

	public ImportAssessmentParameterFactorScore removeImportAssessmentParameterFactorScore(ImportAssessmentParameterFactorScore importAssessmentParameterFactorScore) {
		getImportAssessmentParameterFactorScores().remove(importAssessmentParameterFactorScore);
		importAssessmentParameterFactorScore.setImportAssessment(null);

		return importAssessmentParameterFactorScore;
	}

}