package uk.gov.phe.erdst.sc.awag.dto.assessment;

import java.util.HashMap;
import java.util.Map;

public final class ParametersOrdering
{
    private Map<Long, Integer> mOrder = new HashMap<>();

    public int getIndex(Long parameterId)
    {
        return mOrder.get(parameterId);
    }

    public void add(Long id, int i)
    {
        mOrder.put(id, i);
    }
}
