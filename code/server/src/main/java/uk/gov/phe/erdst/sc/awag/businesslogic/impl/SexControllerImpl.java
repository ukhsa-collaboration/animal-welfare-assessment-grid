package uk.gov.phe.erdst.sc.awag.businesslogic.impl;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.inject.Inject;

import uk.gov.phe.erdst.sc.awag.businesslogic.SexController;
import uk.gov.phe.erdst.sc.awag.dao.SexDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Sex;

@Stateless
public class SexControllerImpl implements SexController
{
    @Inject
    private SexDao mSexDao;

    @Override
    public Collection<Sex> getSexes()
    {
        return mSexDao.getSexes();
    }
}
