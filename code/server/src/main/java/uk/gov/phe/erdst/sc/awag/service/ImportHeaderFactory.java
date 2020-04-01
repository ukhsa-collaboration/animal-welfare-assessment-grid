package uk.gov.phe.erdst.sc.awag.service;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;

import uk.gov.phe.erdst.sc.awag.datamodel.ImportAnimal;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportAnimalHousing;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportAssessment;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportAssessmentReason;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportFactor;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportHeader;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportParameter;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportScale;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportSource;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportSpecy;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportStudy;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportStudyGroup;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportTemplate;
import uk.gov.phe.erdst.sc.awag.service.activitylogging.LoggedUser;

@Stateless
public class ImportHeaderFactory
{
    public ImportHeaderFactory()
    {
    }

    // TODO will nullifying the list make any difference, can this be shortened?

    private ImportHeader create(LoggedUser loggedUser)
    {
        ImportHeader importHeader = new ImportHeader();
        importHeader.setUserName(loggedUser.username);
        importHeader.setDateImport(new java.util.Date());
        return importHeader;
    }

    public ImportHeader createWithImportStudies(LoggedUser loggedUser)
    {
        ImportHeader importHeader = create(loggedUser);
        Set<ImportStudy> importStudies = new HashSet<>();
        importHeader.setImportStudies(importStudies);
        return importHeader;
    }

    public ImportHeader createWithImportStudyGroups(LoggedUser loggedUser)
    {
        ImportHeader importHeader = create(loggedUser);
        Set<ImportStudyGroup> importStudyGroups = new HashSet<>();
        importHeader.setImportStudyGroups(importStudyGroups);
        return importHeader;
    }

    public ImportHeader createWithImportTemplate(LoggedUser loggedUser)
    {
        ImportHeader importHeader = create(loggedUser);
        Set<ImportTemplate> importAssessmentTemplates = new HashSet<>();
        importHeader.setImportTemplates(importAssessmentTemplates);
        return importHeader;
    }

    public ImportHeader createWithImportParameter(LoggedUser loggedUser)
    {
        ImportHeader importHeader = create(loggedUser);
        Set<ImportParameter> importParameters = new HashSet<>();
        importHeader.setImportParameters(importParameters);
        return importHeader;
    }

    public ImportHeader createWithImportFactor(LoggedUser loggedUser)
    {
        ImportHeader importHeader = create(loggedUser);
        Set<ImportFactor> importFactors = new HashSet<>();
        importHeader.setImportFactors(importFactors);
        return importHeader;
    }

    public ImportHeader createWithImportScales(LoggedUser loggedUser)
    {
        ImportHeader importHeader = create(loggedUser);
        Set<ImportScale> importScales = new HashSet<>();
        importHeader.setImportScales(importScales);
        return importHeader;
    }

    public ImportHeader createWithImportSources(LoggedUser loggedUser)
    {
        ImportHeader importHeader = create(loggedUser);
        Set<ImportSource> importSources = new HashSet<>();
        importHeader.setImportSources(importSources);
        return importHeader;
    }

    public ImportHeader createWithImportSpecies(LoggedUser loggedUser)
    {
        ImportHeader importHeader = create(loggedUser);
        Set<ImportSpecy> importSpecies = new HashSet<>();
        importHeader.setImportSpecies(importSpecies);
        return importHeader;
    }

    public ImportHeader createWithImportAssessmentReason(LoggedUser loggedUser)
    {
        ImportHeader importHeader = create(loggedUser);
        Set<ImportAssessmentReason> importAssessmentReasons = new HashSet<>();
        importHeader.setImportAssessmentReasons(importAssessmentReasons);
        return importHeader;
    }

    public ImportHeader createWithImportAnimalHousing(LoggedUser loggedUser)
    {
        ImportHeader importHeader = create(loggedUser);
        Set<ImportAnimalHousing> importAnimalHousing = new HashSet<>();
        importHeader.setImportAnimalHousings(importAnimalHousing);
        return importHeader;
    }

    public ImportHeader createWithImportAnimal(LoggedUser loggedUser)
    {
        ImportHeader importHeader = create(loggedUser);
        Set<ImportAnimal> importAnimals = new HashSet<>();
        importHeader.setImportAnimals(importAnimals);
        return importHeader;
    }

    public ImportHeader createWithImportAssessment(LoggedUser loggedUser)
    {
        ImportHeader importHeader = create(loggedUser);
        Set<ImportAssessment> importAssessment = new HashSet<>();
        importHeader.setImportAssessments(importAssessment);
        return importHeader;
    }

}
