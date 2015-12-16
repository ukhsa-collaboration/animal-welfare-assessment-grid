package uk.gov.phe.erdst.sc.awag.dao;

import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.datamodel.Sex;

public interface SexDao
{
    Collection<Sex> getSexes();
}
