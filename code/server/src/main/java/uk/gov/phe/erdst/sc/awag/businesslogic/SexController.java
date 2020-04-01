package uk.gov.phe.erdst.sc.awag.businesslogic;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.inject.Inject;

import uk.gov.phe.erdst.sc.awag.dao.SexDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Sex;
import uk.gov.phe.erdst.sc.awag.webapi.response.sex.SexesDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.sex.SexesDto.SexDto;

@Stateless
public class SexController
{
    @Inject
    private SexDao mSexDao;

    public SexesDto getSexes()
    {
        Collection<Sex> sexes = mSexDao.getSexes();

        SexesDto response = new SexesDto();
        response.sexes = new ArrayList<SexDto>(sexes.size());

        for (Sex sex : sexes)
        {
            response.sexes.add(new SexDto(sex.getId(), sex.getName()));
        }

        return response;
    }

    // TODO integration test
    public Sex getSexFemale()
    {
        return mSexDao.getSexFemale();
    }

    public Sex getSexMale()
    {
        return mSexDao.getSexMale();
    }

}
