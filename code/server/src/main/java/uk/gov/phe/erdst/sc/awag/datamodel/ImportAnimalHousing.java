package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the import_animal_housing database table.
 * 
 */
@Entity
@Table(name="import_animal_housing")
@NamedQuery(name="ImportAnimalHousing.findAll", query="SELECT i FROM ImportAnimalHousing i")
public class ImportAnimalHousing implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long importanimalhousingid;

	@Column(name="animal_housing_name")
	private String animalHousingName;

	private Long animalhousingid;

	@Column(name="line_number")
	private Long lineNumber;

	//bi-directional many-to-one association to ImportHeader
	@ManyToOne
	@JoinColumn(name="importheaderid")
	private ImportHeader importHeader;

	public ImportAnimalHousing() {
	}

	public Long getImportanimalhousingid() {
		return this.importanimalhousingid;
	}

	public void setImportanimalhousingid(Long importanimalhousingid) {
		this.importanimalhousingid = importanimalhousingid;
	}

	public String getAnimalHousingName() {
		return this.animalHousingName;
	}

	public void setAnimalHousingName(String animalHousingName) {
		this.animalHousingName = animalHousingName;
	}

	public Long getAnimalhousingid() {
		return this.animalhousingid;
	}

	public void setAnimalhousingid(Long animalhousingid) {
		this.animalhousingid = animalhousingid;
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