package uk.gov.phe.erdst.sc.awag.service.factory.study;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import uk.gov.phe.erdst.sc.awag.businesslogic.StudyController;
import uk.gov.phe.erdst.sc.awag.businesslogic.StudyGroupController;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportStudy;
import uk.gov.phe.erdst.sc.awag.datamodel.Study;
import uk.gov.phe.erdst.sc.awag.datamodel.StudyGroup;
import uk.gov.phe.erdst.sc.awag.webapi.request.StudyClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.StudyGroupClientData;

public class StudyFactory
{
    @Inject
    StudyController studyController;

    @Inject
    StudyGroupController studyGroupController; // TODO

    public Study create(StudyClientData clientData)
    {
        Study study = new Study();
        setNonIdProperties(study, clientData);
        return study;
    }

    public Study create(ImportStudy importStudy)
    {
        Study study = new Study();

        study.setStudyNumber(importStudy.getStudynumber());
        study.setIsOpen(importStudy.getIsstudyopen());
        study.setId(importStudy.getStudynumberid());

        Set<StudyGroup> studyGroups = new HashSet<>();
        study.setStudyGroups(studyGroups);
        /*
        for (ImportStudyStudyGroup importStudyStudyGroup : importStudy.getImportStudyStudyGroups())
        {
            if (importStudyStudyGroup.getStudystudygroupnumberid() != null)
            {
                StudyGroup studyGroup = studyController
                    .getStudyGroupNumberNonApiMethod(importStudyStudyGroup.getStudystudygroupnumber()); // TODO
                studyGroups.add(studyGroup);
            }
        }
        */

        return study;
    }

    public void update(Study study, StudyClientData clientData)
    {
        setNonIdProperties(study, clientData);
    }

    private void setNonIdProperties(Study study, StudyClientData clientData)
    {
        if (study != null)
        {
            study.setStudyNumber(clientData.studyName);
            study.setIsOpen(clientData.isStudyOpen);

            if (!clientData.studyGroups.isEmpty())
            {
                study.setStudyGroups(getStudyGroups(clientData));
            }
            else
            {
                clearStudyGroups(study);
            }
        }
    }

    private Set<StudyGroup> getStudyGroups(StudyClientData clientData)
    {
        Set<StudyGroup> studyGroups = new HashSet<StudyGroup>();
        for (StudyGroupClientData studyGroupClientData : clientData.studyGroups)
        {
            studyGroups.add(new StudyGroup(studyGroupClientData.studyGroupId));
        }
        return studyGroups;
    }

    private void clearStudyGroups(Study study)
    {
        Set<StudyGroup> currentStudyGroups = study.getStudyGroups();
        if (currentStudyGroups != null)
        {
            currentStudyGroups.clear();
        }
    }

    public StudyClientData create(Study study, Set<StudyGroupClientData> studyGroupClientData)
    {
        StudyClientData studyClientData = new StudyClientData();
        if (study != null)
        {
            studyClientData.studyId = study.getId();
            studyClientData.studyName = study.getStudyNumber();
            studyClientData.isStudyOpen = study.isOpen();
            studyClientData.studyGroups.addAll(studyGroupClientData);
        }
        return studyClientData;
    }
}
