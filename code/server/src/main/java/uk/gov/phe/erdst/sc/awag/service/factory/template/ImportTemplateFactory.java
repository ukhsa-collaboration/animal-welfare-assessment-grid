package uk.gov.phe.erdst.sc.awag.service.factory.template;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import uk.gov.phe.erdst.sc.awag.dao.AssessmentTemplateDao;
import uk.gov.phe.erdst.sc.awag.dao.ScaleDao;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportTemplate;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportTemplateParameter;
import uk.gov.phe.erdst.sc.awag.datamodel.ImportTemplateParameterFactor;

@Stateless
public class ImportTemplateFactory
{
    @Inject
    private ScaleDao scaleDao;

    @Inject
    private AssessmentTemplateDao templateDao;

    private static int CSV_COLUMN_LINE_NUMBER = 0;
    private static int CSV_COLUMN_TEMPLATE_NAME = 1;
    private static int CSV_COLUMN_SCALE_NAME = 2;
    private static int CSV_COLUMN_PARAMETER_COUNT = 3;
    private static int CSV_COLUMN_START_PARAMETER_DETAIL = 4;

    private static String CSV_FACTOR_SEPARATOR = "\\|";
    private static int CSV_PARAMETER_FACTOR_COLUMN_COUNT = 2;

    public ImportTemplate create(String[] importAssessmentTemplateCSVLineData)
    {
        final Long lineNumber = Long.parseLong(importAssessmentTemplateCSVLineData[CSV_COLUMN_LINE_NUMBER]);
        final String templateName = String.valueOf(importAssessmentTemplateCSVLineData[CSV_COLUMN_TEMPLATE_NAME]);
        final String scaleName = String.valueOf(importAssessmentTemplateCSVLineData[CSV_COLUMN_SCALE_NAME].trim());
        final Long parameterCount = Long
            .parseLong(importAssessmentTemplateCSVLineData[CSV_COLUMN_PARAMETER_COUNT].trim());

        ImportTemplate importTemplate = new ImportTemplate();
        importTemplate.setLineNumber(lineNumber);
        importTemplate.setAssessmentTemplateName(templateName);
        importTemplate.setScaleName(scaleName);
        importTemplate.setParameterCount(parameterCount);
        importTemplate.setImportTemplateParameters(new LinkedHashSet<>());

        // TODO cannot add empty parameter name
        // TODO cannot add empty factor names
        // TODO parameter count in the line must match, all data must be non-null and some data must exist
        long endColumnIndex = CSV_COLUMN_START_PARAMETER_DETAIL + (parameterCount * CSV_PARAMETER_FACTOR_COLUMN_COUNT);
        for (int columnIndex = CSV_COLUMN_START_PARAMETER_DETAIL; columnIndex <= endColumnIndex; ++columnIndex)
        {
            String parameterName = String.valueOf(importAssessmentTemplateCSVLineData[columnIndex]).trim();
            String parameterFactorStrings = String.valueOf(importAssessmentTemplateCSVLineData[++columnIndex]).trim();

            ImportTemplateParameter importTemplateParameter = createTemplateParameter(parameterName,
                parameterFactorStrings);
            importTemplate.addImportTemplateParameter(importTemplateParameter);

            importTemplateParameter.setImportTemplateParameterFactors(new LinkedHashSet<>());
            final List<String> parameterFactors = Arrays.asList(parameterFactorStrings.split(CSV_FACTOR_SEPARATOR));
            for (String parameterFactorName : parameterFactors)
            {
                ImportTemplateParameterFactor importTemplateParameterFactor = createTemplateParameterFactor(
                    parameterFactorName);
                importTemplateParameter.addImportTemplateParameterFactor(importTemplateParameterFactor);
            }

        }

        return importTemplate;
    }

    private ImportTemplateParameter createTemplateParameter(String parameterName, String parameterFactorStrings)
    {
        ImportTemplateParameter importTemplateParameter = new ImportTemplateParameter();
        importTemplateParameter.setParameterName(parameterName);
        importTemplateParameter.setFactors(parameterFactorStrings);
        return importTemplateParameter;
    }

    private ImportTemplateParameterFactor createTemplateParameterFactor(String parameterFactorName)
    {
        ImportTemplateParameterFactor importTemplateParameterFactor = new ImportTemplateParameterFactor();
        importTemplateParameterFactor.setFactorName(parameterFactorName);
        return importTemplateParameterFactor;
    }

}
