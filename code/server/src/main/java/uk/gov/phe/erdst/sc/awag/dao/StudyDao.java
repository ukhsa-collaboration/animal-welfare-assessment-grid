package uk.gov.phe.erdst.sc.awag.dao;

import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.Study;
import uk.gov.phe.erdst.sc.awag.exceptions.AWMultipleResultException;

public interface StudyDao extends CommonDao<Study>
{
    /**
     * Retrieves a study an animal is assigned to.
     * @param animal
     * @return a study if the given animal is assigned to a study group which is part of the study.
     *         <br />
     *         <strong>null</strong> if the given animal is not assigned to a study
     * @throws AWMultipleResultException
     *             if the given animal is assigned to more than one study, which should not be
     *             possible
     */
    Study getStudyWithAnimal(Animal animal) throws AWMultipleResultException;
}
