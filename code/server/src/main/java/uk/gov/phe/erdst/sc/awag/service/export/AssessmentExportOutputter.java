package uk.gov.phe.erdst.sc.awag.service.export;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletResponse;

import com.opencsv.CSVWriter;

import uk.gov.phe.erdst.sc.awag.datamodel.utils.ParametersOrdering;
import uk.gov.phe.erdst.sc.awag.service.CwasCalculator;
import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.utils.WebApiUtils;
import uk.gov.phe.erdst.sc.awag.webapi.response.factor.FactorScoredDto;
import uk.gov.phe.erdst.sc.awag.webapi.response.parameter.ParameterScoredDto;

@Stateless
public class AssessmentExportOutputter
{
    private static final SimpleDateFormat FILENAME_DATE_FORMATTER = new SimpleDateFormat(Constants.OUTPUT_DATE_FORMAT);

    // CS:OFF: MultipleStringLiteral
    private static final String NO_VALUE_STRING = "-";
    private static final String IGNORED_FACTOR_VALUE_STRING = "0";

    // CS:ON

    public void outputExport(HttpServletResponse response, AssessmentsExport export) throws IOException
    {
        String fileName = getCsvFileName();

        WebApiUtils.setContentTypeToCsv(response);
        WebApiUtils.setContentDispositionHeader(response, fileName);

        writeCsv(export, fileName, response.getOutputStream());
    }

    private String getCsvFileName()
    {
        String date = FILENAME_DATE_FORMATTER.format(new Date(System.currentTimeMillis()));
        return String.format("%s-%s-%s.csv", Constants.OUTPUT_FILES_PREFIX, "assessments-export", date);
    }

    private void writeCsv(AssessmentsExport export, String fileName, OutputStream output) throws IOException
    {
        BufferedWriter buffWriter = new BufferedWriter(new OutputStreamWriter(output, Constants.OUTPUT_ENCODING_UTF_8));
        CSVWriter csvWriter = new CSVWriter(buffWriter);

        if (export.entries.isEmpty())
        {
            exportEmptyFile(csvWriter);
            return;
        }

        List<String> headerValues = getHeaderValues(export);
        csvWriter.writeNext(headerValues.toArray(new String[] {}));

        Row dataRow;
        final int dataSize = headerValues.size();
        for (AssessmentsExportEntry entry : export.entries)
        {
            dataRow = new Row();
            populateRow(dataRow, entry, export.parametersOrdering);
            writeDataRow(dataRow, dataSize, csvWriter);
        }

        csvWriter.close();
    }

    private void populateRow(Row row, AssessmentsExportEntry exportEntry, ParametersOrdering parametersOrdering)
    {
        row.animalNumber = exportEntry.getAnimalNumber();
        row.animalDob = ExportUtils.formatIso8601ToSimpleDate(exportEntry.getAnimalDob());
        row.animalSex = exportEntry.getAnimalSex("Male", "Female");
        row.animalSpecies = exportEntry.getAnimalSpecies();
        row.animalSource = exportEntry.getAnimalSource();
        row.animalDam = exportEntry.getAnimalDam();
        row.animalFather = exportEntry.getAnimalFather();
        row.study = exportEntry.getStudy();
        row.studyGroup = exportEntry.getStudyGroup();
        row.template = exportEntry.getTemplate();
        row.date = ExportUtils.formatIso8601ToSimpleDate(exportEntry.getAssessmentDate());
        row.reason = exportEntry.getReason();
        row.performedBy = exportEntry.getPerformedBy();
        row.housing = exportEntry.getHousing();

        row.cwas = ExportUtils.formatNumericalValues(CwasCalculator.roundCwas(exportEntry.cwas));

        Set<ParameterScoredDto> scoresDto = exportEntry.getScores();
        List<ParameterScoredDto> scores = new ArrayList<>(scoresDto.size());

        for (ParameterScoredDto scoreDto : scoresDto)
        {
            int paramIdx = parametersOrdering.getIndex(scoreDto.parameterId);
            scores.add(paramIdx, scoreDto);
        }

        row.scores = scores;
    }

    private void writeDataRow(Row dataRow, final int dataSize, CSVWriter csvWriter)
    {
        List<String> data = new ArrayList<>(dataSize);

        data.add(dataRow.animalNumber);
        data.add(dataRow.animalDob);
        data.add(dataRow.animalSex);
        data.add(dataRow.animalSpecies);
        data.add(dataRow.animalSource);
        data.add(dataRow.animalDam != null ? dataRow.animalDam : NO_VALUE_STRING);
        data.add(dataRow.animalFather != null ? dataRow.animalFather : NO_VALUE_STRING);
        data.add(dataRow.study != null ? dataRow.study : NO_VALUE_STRING);
        data.add(dataRow.studyGroup != null ? dataRow.studyGroup : NO_VALUE_STRING);
        data.add(dataRow.template);
        data.add(dataRow.date);
        data.add(dataRow.reason);
        data.add(dataRow.performedBy);
        data.add(dataRow.housing);
        data.add(dataRow.cwas);

        for (ParameterScoredDto paramDto : dataRow.scores)
        {
            data.add(ExportUtils.formatNumericalValues(paramDto.parameterAverage));

            for (FactorScoredDto factorDto : paramDto.parameterFactors)
            {
                if (factorDto.isIgnored)
                {
                    data.add(IGNORED_FACTOR_VALUE_STRING);
                }
                else
                {
                    data.add(String.valueOf(factorDto.factorScore));
                }
            }

            data.add(paramDto.parameterComment != null ? paramDto.parameterComment : NO_VALUE_STRING);
        }

        csvWriter.writeNext(data.toArray(new String[] {}));
    }

    private void exportEmptyFile(CSVWriter csvWriter) throws IOException
    {
        csvWriter.writeNext(new String[] {"No assessments found to be exported"});
        csvWriter.close();
    }

    private List<String> getHeaderValues(AssessmentsExport export)
    {
        Row headerRow = new Row();

        for (AssessmentsExportEntry entry : export.entries)
        {
            populateRow(headerRow, entry, export.parametersOrdering);
            break;
        }

        List<String> headers = new ArrayList<>();

        headers.add("Animal number");
        headers.add("Date of Birth"); // TODO changed
        headers.add("Sex");
        headers.add("Species");
        headers.add("Source");
        headers.add("Dam");
        headers.add("Father");
        headers.add("Study name"); // TODO changed
        headers.add("Study group name");
        headers.add("Assessment Template used");
        headers.add("Assessment date");
        headers.add("Assessment reason");
        headers.add("Assessment performed by");
        headers.add("Animal housing");
        headers.add("CWAS");

        for (ParameterScoredDto paramDto : headerRow.scores)
        {
            headers.add(paramDto.parameterName + "-average");

            for (FactorScoredDto factorDto : paramDto.parameterFactors)
            {
                headers.add(paramDto.parameterName + "-" + factorDto.factorName);
            }

            headers.add(paramDto.parameterName + "-comments");
        }

        return headers;
    }

    // CS:OFF: VisibilityModifier
    private static class Row
    {
        public String animalNumber;
        public String animalDob;
        public String animalSex;
        public String animalSpecies;
        public String animalSource;
        public String animalDam;
        public String animalFather;
        public String study;
        public String studyGroup;
        public String template;
        public String date;
        public String reason;
        public String performedBy;
        public String housing;
        public String cwas;
        public List<ParameterScoredDto> scores;
    }
    // CS:ON

}
