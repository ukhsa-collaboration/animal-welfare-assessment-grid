package uk.gov.phe.erdst.sc.awag.dao.impl;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import uk.gov.phe.erdst.sc.awag.dao.SexDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Sex;
import uk.gov.phe.erdst.sc.awag.utils.Constants;

@Stateless
public class SexDaoImpl implements SexDao
{
    @PersistenceContext(unitName = Constants.PERSISTENCE_CONTEXT_DEFAULT_UNIT_NAME)
    private EntityManager mEntityManager;

    @Override
    public Collection<Sex> getSexes()
    {
        return mEntityManager.createNamedQuery(Sex.Q_FIND_ALL, Sex.class).getResultList();
    }

    @Override
    public Sex getSexFemale()
    {

        return mEntityManager.createNamedQuery(Sex.Q_FIND_SEX_BY_NAME, Sex.class).setParameter("name", Sex.FEMALE)
            .getSingleResult();
    }

    @Override
    public Sex getSexMale()
    {

        return mEntityManager.createNamedQuery(Sex.Q_FIND_SEX_BY_NAME, Sex.class).setParameter("name", Sex.MALE)
            .getSingleResult();
    }

}
