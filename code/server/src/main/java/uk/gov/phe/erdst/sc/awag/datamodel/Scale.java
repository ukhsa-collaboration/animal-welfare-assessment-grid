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

/**
 * The persistent class for the scale database table.
 */
@Entity
@NamedQueries({@NamedQuery(name = Scale.Q_FIND_ALL, query = "SELECT s FROM Scale s"),
        @NamedQuery(name = Scale.Q_FIND_COUNT_ALL, query = "SELECT COUNT(s) FROM Scale s"),
        @NamedQuery(name = Scale.Q_FIND_ALL_LIKE,
            query = "SELECT s FROM Scale s WHERE LOWER(s.mName) LIKE :like ORDER BY LENGTH(s.mName), s.mName ASC"),
        @NamedQuery(name = Scale.Q_FIND_COUNT_ALL_LIKE,
            query = "SELECT COUNT(s) FROM Scale s WHERE LOWER(s.mName) LIKE :like")})
public class Scale implements Serializable, EntitySelect
{
    public static final String Q_FIND_ALL = "findAllScales";
    public static final String Q_FIND_ALL_LIKE = "findAllScaleLike";
    public static final String Q_FIND_COUNT_ALL_LIKE = "findCountAllScaleLike";
    public static final String Q_FIND_COUNT_ALL = "findCountAllScales";

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mId;

    private int mMax;

    private int mMin;

    private String mName;

    // bi-directional many-to-one association to AssessmentTemplate
    @OneToMany(mappedBy = "mScale", fetch = FetchType.EAGER)
    private List<AssessmentTemplate> mAssessmentTemplates;

    public Scale()
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

    public int getMax()
    {
        return mMax;
    }

    public void setMax(int mMax)
    {
        this.mMax = mMax;
    }

    public int getMin()
    {
        return mMin;
    }

    public void setMin(int mMin)
    {
        this.mMin = mMin;
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String mName)
    {
        this.mName = mName;
    }

    public List<AssessmentTemplate> getAssessmentTemplates()
    {
        return this.mAssessmentTemplates;
    }

    public void setAssessmentTemplates(List<AssessmentTemplate> assessmentTemplates)
    {
        this.mAssessmentTemplates = assessmentTemplates;
    }

    public AssessmentTemplate addAssessmentTemplate(AssessmentTemplate assessmentTemplate)
    {
        getAssessmentTemplates().add(assessmentTemplate);
        assessmentTemplate.setScale(this);

        return assessmentTemplate;
    }

    public AssessmentTemplate removeAssessmentTemplate(AssessmentTemplate assessmentTemplate)
    {
        getAssessmentTemplates().remove(assessmentTemplate);
        assessmentTemplate.setScale(null);

        return assessmentTemplate;
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
        Scale other = (Scale) obj;
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
