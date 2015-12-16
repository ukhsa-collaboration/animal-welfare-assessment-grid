package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.google.gson.annotations.SerializedName;

@Entity
@NamedQueries({@NamedQuery(name = Parameter.Q_FIND_ALL, query = "SELECT p FROM Parameter p"),
        @NamedQuery(name = Parameter.Q_FIND_BY_NAME, query = "SELECT p FROM Parameter p WHERE p.mName = :name"),
        @NamedQuery(name = Parameter.Q_FIND_WITH_IDS, query = "SELECT p FROM Parameter p WHERE p.mId IN :ids"),
        @NamedQuery(name = Parameter.Q_FIND_COUNT_PARAMETERS_LIKE,
            query = "SELECT COUNT(p) FROM Parameter p WHERE LOWER(p.mName) LIKE :like"),
        @NamedQuery(name = Parameter.Q_FIND_COUNT_ALL, query = "SELECT COUNT(p) FROM Parameter p"),
        @NamedQuery(name = Parameter.Q_FIND_ALL_PARAMETERS_LIKE,
            query = "SELECT p FROM Parameter p WHERE LOWER(p.mName) LIKE :like ORDER BY LENGTH(p.mName) ASC,"
                + " p.mName ASC")})
public class Parameter implements Serializable, EntitySelect
{
    public static final String Q_FIND_ALL = "findAllParameters";
    public static final String Q_FIND_WITH_IDS = "findParametersWithIds";
    public static final String Q_FIND_COUNT_PARAMETERS_LIKE = "findCountAllParametersLike";
    public static final String Q_FIND_COUNT_ALL = "findCountAllParameters";
    public static final String Q_FIND_ALL_PARAMETERS_LIKE = "findAllParametersLike";
    public static final String Q_FIND_BY_NAME = "findParametersByName";

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SerializedName(value = "parameterId")
    private Long mId;

    @SerializedName(value = "parameterName")
    private String mName;

    @OneToMany(mappedBy = "mParameter", fetch = FetchType.EAGER)
    @SerializedName(value = "parameterFactors")
    private List<AssessmentTemplateParameterFactor> mAssessmentTemplateParameterFactors;

    public Parameter()
    {
    }

    public Long getId()
    {
        return mId;
    }

    public void setId(Long mId)
    {
        this.mId = mId;
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String mName)
    {
        this.mName = mName;
    }

    public List<AssessmentTemplateParameterFactor> getAssessmentTemplateParameterFactors()
    {
        return this.mAssessmentTemplateParameterFactors;
    }

    public void setAssessmentTemplateParameterFactors(
        List<AssessmentTemplateParameterFactor> assessmentTemplateParameterFactors)
    {
        this.mAssessmentTemplateParameterFactors = assessmentTemplateParameterFactors;
    }

    public AssessmentTemplateParameterFactor
        addAssessmentTemplateParameterFactor(AssessmentTemplateParameterFactor assessmentTemplateParameterFactor)
    {
        getAssessmentTemplateParameterFactors().add(assessmentTemplateParameterFactor);
        assessmentTemplateParameterFactor.setParameter(this);

        return assessmentTemplateParameterFactor;
    }

    public AssessmentTemplateParameterFactor
        removeAssessmentTemplateParameterFactor(AssessmentTemplateParameterFactor assessmentTemplateParameterFactor)
    {
        getAssessmentTemplateParameterFactors().remove(assessmentTemplateParameterFactor);
        assessmentTemplateParameterFactor.setParameter(null);

        return assessmentTemplateParameterFactor;
    }

    @Override
    public Long getEntitySelectId()
    {
        return mId;
    }

    @Override
    public String getEntitySelectName()
    {
        return mName;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mId == null) ? 0 : mId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        Parameter other = (Parameter) obj;
        if (mId == null)
        {
            if (other.mId != null)
            {
                return false;
            }
        }
        else if (!mId.equals(other.mId))
        {
            return false;
        }
        return true;
    }

}
