package uk.gov.phe.erdst.sc.awag.dao;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.datamodel.Sex;

public interface SexDao extends UniqueDao
{
    Collection<Sex> getSexes();

    Sex getSexFemale();

    Sex getSexMale();
}
