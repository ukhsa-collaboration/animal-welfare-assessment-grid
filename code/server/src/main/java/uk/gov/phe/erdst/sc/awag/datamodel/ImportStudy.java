package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the import_study database table.
 * 
 */
@Entity
@Table(name="import_study")
@NamedQuery(name="ImportStudy.findAll", query="SELECT i FROM ImportStudy i")
public class ImportStudy implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long importstudyid;

	private Boolean isstudyopen;

	@Column(name="line_number")
	private Long lineNumber;

	private String studynumber;

	private Long studynumberid;

	private String studystudygroupnumbers;

	//bi-directional many-to-one association to ImportHeader
	@ManyToOne
	@JoinColumn(name="importheaderid")
	private ImportHeader importHeader;

	//bi-directional many-to-one association to ImportStudyStudyGroup
	@OneToMany(mappedBy="importStudy")
	private Set<ImportStudyStudyGroup> importStudyStudyGroups;

	public ImportStudy() {
	}

	public Long getImportstudyid() {
		return this.importstudyid;
	}

	public void setImportstudyid(Long importstudyid) {
		this.importstudyid = importstudyid;
	}

	public Boolean getIsstudyopen() {
		return this.isstudyopen;
	}

	public void setIsstudyopen(Boolean isstudyopen) {
		this.isstudyopen = isstudyopen;
	}

	public Long getLineNumber() {
		return this.lineNumber;
	}

	public void setLineNumber(Long lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getStudynumber() {
		return this.studynumber;
	}

	public void setStudynumber(String studynumber) {
		this.studynumber = studynumber;
	}

	public Long getStudynumberid() {
		return this.studynumberid;
	}

	public void setStudynumberid(Long studynumberid) {
		this.studynumberid = studynumberid;
	}

	public String getStudystudygroupnumbers() {
		return this.studystudygroupnumbers;
	}

	public void setStudystudygroupnumbers(String studystudygroupnumbers) {
		this.studystudygroupnumbers = studystudygroupnumbers;
	}

	public ImportHeader getImportHeader() {
		return this.importHeader;
	}

	public void setImportHeader(ImportHeader importHeader) {
		this.importHeader = importHeader;
	}

	public Set<ImportStudyStudyGroup> getImportStudyStudyGroups() {
		return this.importStudyStudyGroups;
	}

	public void setImportStudyStudyGroups(Set<ImportStudyStudyGroup> importStudyStudyGroups) {
		this.importStudyStudyGroups = importStudyStudyGroups;
	}

	public ImportStudyStudyGroup addImportStudyStudyGroup(ImportStudyStudyGroup importStudyStudyGroup) {
		getImportStudyStudyGroups().add(importStudyStudyGroup);
		importStudyStudyGroup.setImportStudy(this);

		return importStudyStudyGroup;
	}

	public ImportStudyStudyGroup removeImportStudyStudyGroup(ImportStudyStudyGroup importStudyStudyGroup) {
		getImportStudyStudyGroups().remove(importStudyStudyGroup);
		importStudyStudyGroup.setImportStudy(null);

		return importStudyStudyGroup;
	}

}