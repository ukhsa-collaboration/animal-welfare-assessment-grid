package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.google.gson.annotations.SerializedName;

@Entity
@Table(name = "assessment_reason")
@NamedQueries({@NamedQuery(name = AssessmentReason.Q_FIND_ALL, query = "SELECT r FROM AssessmentReason r"),
        @NamedQuery(name = AssessmentReason.Q_FIND_COUNT_ALL, query = "SELECT COUNT(r) FROM AssessmentReason r"),
        @NamedQuery(name = AssessmentReason.Q_FIND_BY_NAME,
            query = "SELECT r FROM AssessmentReason r WHERE r.mName = :name"),
        @NamedQuery(name = AssessmentReason.Q_FIND_LIKE_ORDERED,
            query = "SELECT r FROM AssessmentReason r WHERE LOWER(r.mName)"
                + " LIKE :like ORDER BY LENGTH(r.mName) ASC, r.mName ASC"),
        @NamedQuery(name = AssessmentReason.Q_FIND_COUNT_LIKE_ORDERED,
            query = "SELECT COUNT(r) FROM AssessmentReason r WHERE LOWER(r.mName)" + " LIKE :like")})
public class AssessmentReason implements Serializable, EntitySelect
{
    public static final String Q_FIND_ALL = "findAllAssessmentReasons";
    public static final String Q_FIND_BY_NAME = "findAssessmentReasonByName";
    public static final String Q_FIND_COUNT_ALL = "findCountAllAssessmentReasons";
    public static final String Q_FIND_LIKE_ORDERED = "findAllAssessmentReasonsLikeOrdered";
    public static final String Q_FIND_COUNT_LIKE_ORDERED = "findCountAllAssessmentReasonsLikeOrdered";

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SerializedName("reasonId")
    private Long mId;

    @Column(nullable = false, unique = true)
    @SerializedName("reasonName")
    private String mName;

    public AssessmentReason()
    {
    }

    public Long getId()
    {
        return mId;
    }

    public void setId(Long id)
    {
        mId = id;
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String name)
    {
        mName = name;
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
        AssessmentReason other = (AssessmentReason) obj;
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
