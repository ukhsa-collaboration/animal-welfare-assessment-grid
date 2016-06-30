package uk.gov.phe.erdst.sc.awag.service.export;

import java.util.ArrayList;
import java.util.List;

import uk.gov.phe.erdst.sc.awag.dto.assessment.ParametersOrdering;

public class AssessmentsExport
{
    // CS:OFF: VisibilityModifier
    public ParametersOrdering parametersOrdering;
    public List<AssessmentsExportEntry> entries;

    // CS:ON

    public AssessmentsExport(final int size)
    {
        entries = new ArrayList<>(size);
    }

    public void add(AssessmentsExportEntry entry)
    {
        entries.add(entry);
    }

}
