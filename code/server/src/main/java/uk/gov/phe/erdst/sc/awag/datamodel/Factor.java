package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@NamedQueries({@NamedQuery(name = Factor.Q_FIND_ALL, query = "SELECT f FROM Factor f"),
        @NamedQuery(name = Factor.Q_FIND_COUNT_ALL, query = "SELECT COUNT(f) FROM Factor f"),
        @NamedQuery(name = Factor.Q_FIND_ALL_FACTORS_LIKE,
            query = "SELECT f FROM Factor f WHERE LOWER(f.mName) LIKE :like ORDER BY LENGTH(f.mName) ASC,"
                + " f.mName ASC"),
        @NamedQuery(name = Factor.Q_FIND_COUNT_ALL_FACTORS_LIKE,
            query = "SELECT COUNT(f) FROM Factor f WHERE LOWER(f.mName) LIKE :like")})
public class Factor implements Serializable, EntitySelect
{
    public static final String Q_FIND_ALL = "findAllFactors";
    public static final String Q_FIND_COUNT_ALL = "findCountAllFactors";
    public static final String Q_FIND_ALL_FACTORS_LIKE = "findAllFactorsLike";
    public static final String Q_FIND_COUNT_ALL_FACTORS_LIKE = "findCountAllFactorsLike";

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mId;

    private String mName;

    @OneToMany(mappedBy = "mFactor", fetch = FetchType.EAGER)
    private List<AssessmentTemplateParameterFactor> mAssessmentTemplateParameterFactors;

    public Factor()
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
        assessmentTemplateParameterFactor.setFactor(this);

        return assessmentTemplateParameterFactor;
    }

    public AssessmentTemplateParameterFactor
        removeAssessmentTemplateParameterFactor(AssessmentTemplateParameterFactor assessmentTemplateParameterFactor)
    {
        getAssessmentTemplateParameterFactors().remove(assessmentTemplateParameterFactor);
        assessmentTemplateParameterFactor.setFactor(null);

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
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Factor other = (Factor) obj;
        if (mId == null)
        {
            if (other.mId != null)
                return false;
        }
        else if (!mId.equals(other.mId))
            return false;
        return true;
    }
}
