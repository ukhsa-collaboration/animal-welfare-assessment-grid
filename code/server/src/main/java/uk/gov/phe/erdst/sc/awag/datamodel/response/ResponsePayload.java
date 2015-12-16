package uk.gov.phe.erdst.sc.awag.datamodel.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.gson.annotations.SerializedName;

public class ResponsePayload
{
    @SerializedName("data")
    private Object mData = new Object();

    @SerializedName("errors")
    private List<String> mErrors = new ArrayList<String>();

    @SerializedName("metadata")
    private Map<String, Object> mMetadata = new HashMap<String, Object>();

    public ResponsePayload()
    {
    }

    public Map<String, Object> getMetadata()
    {
        return mMetadata;
    }

    public void setData(Object data)
    {
        this.mData = data;
    }

    public Object getData()
    {
        return mData;
    }

    public boolean hasErrors()
    {
        return (!mErrors.isEmpty());
    }

    public List<String> getErrors()
    {
        return mErrors;
    }

    public void addError(String error)
    {
        mErrors.add(error);
    }

    public <T> void addValidationErrors(Set<ConstraintViolation<T>> errors)
    {
        Iterator<ConstraintViolation<T>> it = errors.iterator();
        while (it.hasNext())
        {
            mErrors.add(it.next().getMessage());
        }
    }
}
