package uk.gov.phe.erdst.sc.awag.dao;

public interface DaoErrorMessageProvider
{

    String getNonUniqueEntityErrorMessage(Object entity);

    String getNoSuchEntityMessage(Long id);

    String getNoSuchEntityMessage(String nameValue);
}
