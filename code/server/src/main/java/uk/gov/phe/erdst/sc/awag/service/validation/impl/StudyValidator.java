package uk.gov.phe.erdst.sc.awag.service.validation.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import uk.gov.phe.erdst.sc.awag.service.validation.annotations.ValidStudy;
import uk.gov.phe.erdst.sc.awag.service.validation.utils.ValidationConstants;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.webapi.request.AnimalClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.StudyClientData;
import uk.gov.phe.erdst.sc.awag.webapi.request.StudyGroupClientData;

@Stateless
public class StudyValidator implements ConstraintValidator<ValidStudy, StudyClientData>
{
    @Override
    public void initialize(ValidStudy annotation)
    {
    }

    @Override
    public boolean isValid(StudyClientData studyClientData, ConstraintValidatorContext context)
    {
        return isValidStudyGroups(studyClientData, context);
    }

    private boolean isValidStudyGroups(StudyClientData studyClientData, ConstraintValidatorContext context)
    {
        Set<AnimalClientData> uniqueAnimals = new HashSet<AnimalClientData>();
        List<String> duplicateAnimals = new ArrayList<String>();

        context.disableDefaultConstraintViolation();

        if (studyClientData.studyGroups.size() == 0)
        {
            return true;
        }

        for (StudyGroupClientData studyGroupClientData : studyClientData.studyGroups)
        {
            for (AnimalClientData animalClientData : studyGroupClientData.studyGroupAnimals)
            {
                if (uniqueAnimals.contains(animalClientData))
                {
                    duplicateAnimals.add(animalClientData.number);
                }
                else
                {
                    uniqueAnimals.add(animalClientData);
                }
            }
        }

        if (duplicateAnimals.size() > 0)
        {
            String errorMessage = getDuplicateAnimalsErrorMessage(duplicateAnimals);
            context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
            return false;
        }

        return true;
    }

    private String getDuplicateAnimalsErrorMessage(List<String> duplicateAnimals)
    {
        return String.format(ValidationConstants.ERR_STUDY_DUP_ANIMALS, Arrays.toString(duplicateAnimals.toArray())
            .replace(Constants.ARR_START, "").replace(Constants.ARR_END, ""));
    }
}