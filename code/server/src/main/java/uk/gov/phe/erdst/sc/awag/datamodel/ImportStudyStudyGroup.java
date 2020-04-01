package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the import_study_study_group database table.
 * 
 */
@Entity
@Table(name="import_study_study_group")
@NamedQuery(name="ImportStudyStudyGroup.findAll", query="SELECT i FROM ImportStudyStudyGroup i")
public class ImportStudyStudyGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long importstudystudygroupid;

	private String studystudygroupnumber;

	private Long studystudygroupnumberid;

	//bi-directional many-to-one association to ImportStudy
	@ManyToOne
	@JoinColumn(name="importstudyid")
	private ImportStudy importStudy;

	public ImportStudyStudyGroup() {
	}

	public Long getImportstudystudygroupid() {
		return this.importstudystudygroupid;
	}

	public void setImportstudystudygroupid(Long importstudystudygroupid) {
		this.importstudystudygroupid = importstudystudygroupid;
	}

	public String getStudystudygroupnumber() {
		return this.studystudygroupnumber;
	}

	public void setStudystudygroupnumber(String studystudygroupnumber) {
		this.studystudygroupnumber = studystudygroupnumber;
	}

	public Long getStudystudygroupnumberid() {
		return this.studystudygroupnumberid;
	}

	public void setStudystudygroupnumberid(Long studystudygroupnumberid) {
		this.studystudygroupnumberid = studystudygroupnumberid;
	}

	public ImportStudy getImportStudy() {
		return this.importStudy;
	}

	public void setImportStudy(ImportStudy importStudy) {
		this.importStudy = importStudy;
	}

}