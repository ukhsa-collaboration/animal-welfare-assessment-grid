package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the import_assessment_parameter_factor_scores database table.
 * 
 */
@Entity
@Table(name="import_assessment_parameter_factor_scores")
@NamedQuery(name="ImportAssessmentParameterFactorScore.findAll", query="SELECT i FROM ImportAssessmentParameterFactorScore i")
public class ImportAssessmentParameterFactorScore implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long importassessmentparameterfactorscoresid;

	@Column(name="factor_scores")
	private String factorScores;

	@Column(name="parameter_comments")
	private String parameterComments;

	@Column(name="parameter_number")
	private Long parameterNumber;

	//bi-directional many-to-one association to ImportAssessment
	@ManyToOne
	@JoinColumn(name="importassessmentid")
	private ImportAssessment importAssessment;

	public ImportAssessmentParameterFactorScore() {
	}

	public Long getImportassessmentparameterfactorscoresid() {
		return this.importassessmentparameterfactorscoresid;
	}

	public void setImportassessmentparameterfactorscoresid(Long importassessmentparameterfactorscoresid) {
		this.importassessmentparameterfactorscoresid = importassessmentparameterfactorscoresid;
	}

	public String getFactorScores() {
		return this.factorScores;
	}

	public void setFactorScores(String factorScores) {
		this.factorScores = factorScores;
	}

	public String getParameterComments() {
		return this.parameterComments;
	}

	public void setParameterComments(String parameterComments) {
		this.parameterComments = parameterComments;
	}

	public Long getParameterNumber() {
		return this.parameterNumber;
	}

	public void setParameterNumber(Long parameterNumber) {
		this.parameterNumber = parameterNumber;
	}

	public ImportAssessment getImportAssessment() {
		return this.importAssessment;
	}

	public void setImportAssessment(ImportAssessment importAssessment) {
		this.importAssessment = importAssessment;
	}

}