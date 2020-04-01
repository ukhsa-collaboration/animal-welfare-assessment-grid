package uk.gov.phe.erdst.sc.awag.service.validation.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import uk.gov.phe.erdst.sc.awag.dao.AnimalDao;
import uk.gov.phe.erdst.sc.awag.datamodel.Animal;
import uk.gov.phe.erdst.sc.awag.datamodel.Sex;
import uk.gov.phe.erdst.sc.awag.exceptions.AWNoSuchEntityException;
import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidAnimal;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidatorUtils;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.webapi.request.AnimalClientData;

@Stateless
public class AnimalValidator implements ConstraintValidator<ValidAnimal, AnimalClientData>
{
    @Inject
    private AnimalDao mAnimalDao;

    private Animal mDam;
    private Animal mFather;

    public AnimalValidator()
    {
    }

    @Override
    public void initialize(ValidAnimal animal)
    {
    }

    @Override
    public boolean isValid(AnimalClientData clientData, ConstraintValidatorContext context)
    {
        context.disableDefaultConstraintViolation();

        try
        {
            setParents(clientData, context);
        }
        catch (AWNoSuchEntityException ex)
        {
            return false;
        }

        return isParentsValid(clientData, context);
    }

    private boolean isParentsValid(AnimalClientData clientData, ConstraintValidatorContext context)
    {
        return isDamDobValid(clientData, context) && isFatherDobValid(clientData, context)
            && isAnimalNotParent(clientData, context) && isDamAndFatherDifferent(clientData, context)
            && isParentsSexValid(clientData, context);
    }

    private void setParents(AnimalClientData clientData, ConstraintValidatorContext context)
        throws AWNoSuchEntityException
    {
        AWNoSuchEntityException finalEx = null;

        if (clientData.father != Constants.ID_NOT_SET)
        {
            try
            {
                mFather = mAnimalDao.getAnimal(clientData.father);
            }
            catch (AWNoSuchEntityException ex)
            {
                mFather = null;
                context.buildConstraintViolationWithTemplate(ValidationConstants.ERR_PARENT_NO_SUCH_FATHER)
                    .addConstraintViolation();
                finalEx = ex;
            }
        }

        if (clientData.dam != Constants.ID_NOT_SET)
        {
            try
            {
                mDam = mAnimalDao.getAnimal(clientData.dam);
            }
            catch (AWNoSuchEntityException ex)
            {
                mDam = null;
                context.buildConstraintViolationWithTemplate(ValidationConstants.ERR_PARENT_NO_SUCH_DAM)
                    .addConstraintViolation();
                finalEx = ex;
            }
        }

        if (finalEx != null)
        {
            throw finalEx;
        }
    }

    private boolean isDamDobValid(AnimalClientData clientData, ConstraintValidatorContext context)
    {
        if (clientData.dam != Constants.ID_NOT_SET)
        {
            return isParentDobValid(clientData.dam, mDam.getDateOfBirth(), context, clientData.dob,
                ValidationConstants.ERR_DAM_DOB);
        }
        return true;
    }

    private boolean isFatherDobValid(AnimalClientData clientData, ConstraintValidatorContext context)
    {
        if (clientData.father != Constants.ID_NOT_SET)
        {
            return isParentDobValid(clientData.father, mFather.getDateOfBirth(), context, clientData.dob,
                ValidationConstants.ERR_FATHER_DOB);
        }
        return true;
    }

    private boolean isParentDobValid(Long parentId, String parentDob, ConstraintValidatorContext context,
        String animalDob, String errorMsgFormat)
    {

        if (animalDob == null)
        {
            return false;
        }

        if (parentId != Constants.ID_NOT_SET)
        {
            if (!ValidatorUtils.isFirstDateAfterSecondDate(animalDob, parentDob))
            {
                context.buildConstraintViolationWithTemplate(String.format(errorMsgFormat, animalDob, parentDob))
                    .addConstraintViolation();
                return false;
            }
        }
        return true;
    }

    private static boolean isAnimalNotParent(AnimalClientData clientData, ConstraintValidatorContext context)
    {
        if (!clientData.id.equals(Constants.UNASSIGNED_ID)
            && ((clientData.id.equals(clientData.dam)) || clientData.id.equals(clientData.father)))
        {
            context.buildConstraintViolationWithTemplate(ValidationConstants.ERR_PARENT_ANIMAL_SAME)
                .addConstraintViolation();
            return false;
        }
        return true;
    }

    private static boolean isDamAndFatherDifferent(AnimalClientData clientData, ConstraintValidatorContext context)
    {
        if (clientData.dam != Constants.ID_NOT_SET && clientData.father != Constants.ID_NOT_SET)
        {
            if (clientData.dam.equals(clientData.father))
            {
                context.buildConstraintViolationWithTemplate(ValidationConstants.ERR_PARENT_SAME)
                    .addConstraintViolation();
                return false;
            }
        }
        return true;
    }

    private boolean isParentsSexValid(AnimalClientData clientData, ConstraintValidatorContext context)
    {
        int numErrors = 0;

        if (clientData.father != Constants.ID_NOT_SET)
        {
            if (!mFather.getSex().getName().equals(Sex.MALE))
            {
                context.buildConstraintViolationWithTemplate(ValidationConstants.ERR_FATHER_NOT_MALE)
                    .addConstraintViolation();
                numErrors++;
            }
        }

        if (clientData.dam != Constants.ID_NOT_SET)
        {
            if (!mDam.getSex().getName().equals(Sex.FEMALE))
            {
                context.buildConstraintViolationWithTemplate(ValidationConstants.ERR_DAM_NOT_FEMALE)
                    .addConstraintViolation();
                numErrors++;
            }
        }

        return (numErrors == 0);
    }
}
