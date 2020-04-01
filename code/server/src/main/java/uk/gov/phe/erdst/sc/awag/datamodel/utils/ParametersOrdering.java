package uk.gov.phe.erdst.sc.awag.datamodel.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class ParametersOrdering
{
    private Map<Long, Integer> mOrder = new LinkedHashMap<>();

    public int getIndex(Long parameterId)
    {
        return mOrder.get(parameterId);
    }

    public void add(Long id, int displayOrderIndex)
    {
        mOrder.put(id, displayOrderIndex);
    }

    public Set<Long> getParameterIdList()
    {
        // TODO unit test
        return mOrder.keySet();
    }
}
