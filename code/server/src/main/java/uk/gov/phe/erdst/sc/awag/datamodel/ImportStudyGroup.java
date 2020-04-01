package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the import_study_group database table.
 * 
 */
@Entity
@Table(name="import_study_group")
@NamedQuery(name="ImportStudyGroup.findAll", query="SELECT i FROM ImportStudyGroup i")
public class ImportStudyGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long importstudygroupid;

	@Column(name="line_number")
	private Long lineNumber;

	private String studygroupanimalnumbers;

	private String studygroupnumber;

	private Long studygroupnumberid;

	//bi-directional many-to-one association to ImportHeader
	@ManyToOne
	@JoinColumn(name="importheaderid")
	private ImportHeader importHeader;

	//bi-directional many-to-one association to ImportStudyGroupAnimal
	@OneToMany(mappedBy="importStudyGroup")
	private Set<ImportStudyGroupAnimal> importStudyGroupAnimals;

	public ImportStudyGroup() {
	}

	public Long getImportstudygroupid() {
		return this.importstudygroupid;
	}

	public void setImportstudygroupid(Long importstudygroupid) {
		this.importstudygroupid = importstudygroupid;
	}

	public Long getLineNumber() {
		return this.lineNumber;
	}

	public void setLineNumber(Long lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getStudygroupanimalnumbers() {
		return this.studygroupanimalnumbers;
	}

	public void setStudygroupanimalnumbers(String studygroupanimalnumbers) {
		this.studygroupanimalnumbers = studygroupanimalnumbers;
	}

	public String getStudygroupnumber() {
		return this.studygroupnumber;
	}

	public void setStudygroupnumber(String studygroupnumber) {
		this.studygroupnumber = studygroupnumber;
	}

	public Long getStudygroupnumberid() {
		return this.studygroupnumberid;
	}

	public void setStudygroupnumberid(Long studygroupnumberid) {
		this.studygroupnumberid = studygroupnumberid;
	}

	public ImportHeader getImportHeader() {
		return this.importHeader;
	}

	public void setImportHeader(ImportHeader importHeader) {
		this.importHeader = importHeader;
	}

	public Set<ImportStudyGroupAnimal> getImportStudyGroupAnimals() {
		return this.importStudyGroupAnimals;
	}

	public void setImportStudyGroupAnimals(Set<ImportStudyGroupAnimal> importStudyGroupAnimals) {
		this.importStudyGroupAnimals = importStudyGroupAnimals;
	}

	public ImportStudyGroupAnimal addImportStudyGroupAnimal(ImportStudyGroupAnimal importStudyGroupAnimal) {
		getImportStudyGroupAnimals().add(importStudyGroupAnimal);
		importStudyGroupAnimal.setImportStudyGroup(this);

		return importStudyGroupAnimal;
	}

	public ImportStudyGroupAnimal removeImportStudyGroupAnimal(ImportStudyGroupAnimal importStudyGroupAnimal) {
		getImportStudyGroupAnimals().remove(importStudyGroupAnimal);
		importStudyGroupAnimal.setImportStudyGroup(null);

		return importStudyGroupAnimal;
	}

}