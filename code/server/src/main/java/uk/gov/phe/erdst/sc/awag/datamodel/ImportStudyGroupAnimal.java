package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the import_study_group_animal database table.
 * 
 */
@Entity
@Table(name="import_study_group_animal")
@NamedQuery(name="ImportStudyGroupAnimal.findAll", query="SELECT i FROM ImportStudyGroupAnimal i")
public class ImportStudyGroupAnimal implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long importstudygroupanimalid;

	private String studygroupanimalnumber;

	private Long studygroupanimalnumberid;

	//bi-directional many-to-one association to ImportStudyGroup
	@ManyToOne
	@JoinColumn(name="importstudygroupid")
	private ImportStudyGroup importStudyGroup;

	public ImportStudyGroupAnimal() {
	}

	public Long getImportstudygroupanimalid() {
		return this.importstudygroupanimalid;
	}

	public void setImportstudygroupanimalid(Long importstudygroupanimalid) {
		this.importstudygroupanimalid = importstudygroupanimalid;
	}

	public String getStudygroupanimalnumber() {
		return this.studygroupanimalnumber;
	}

	public void setStudygroupanimalnumber(String studygroupanimalnumber) {
		this.studygroupanimalnumber = studygroupanimalnumber;
	}

	public Long getStudygroupanimalnumberid() {
		return this.studygroupanimalnumberid;
	}

	public void setStudygroupanimalnumberid(Long studygroupanimalnumberid) {
		this.studygroupanimalnumberid = studygroupanimalnumberid;
	}

	public ImportStudyGroup getImportStudyGroup() {
		return this.importStudyGroup;
	}

	public void setImportStudyGroup(ImportStudyGroup importStudyGroup) {
		this.importStudyGroup = importStudyGroup;
	}

}