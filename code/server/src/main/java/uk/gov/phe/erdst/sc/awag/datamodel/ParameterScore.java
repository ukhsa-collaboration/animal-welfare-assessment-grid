package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.gson.annotations.SerializedName;

@Entity
@Table(name = "parameter_score")
public class ParameterScore implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SerializedName("id")
    private Long mId;

    @SerializedName("parameterScored")
    private Parameter mParameterScored;

    @SerializedName("scoringFactorsScored")
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<FactorScored> mScoringFactorsScored;

    @SerializedName("comment")
    private String mComment;

    @SerializedName("averageScore")
    private double mAverageScore;

    public Long getmId()
    {
        return mId;
    }

    // CS:OFF: HiddenFieldCheck
    public void setId(Long mId)
    {
        this.mId = mId;
    }

    public Parameter getParameterScored()
    {
        return mParameterScored;
    }

    public void setParameterScored(Parameter mParameterScored)
    {
        this.mParameterScored = mParameterScored;
    }

    public Collection<FactorScored> getScoringFactorsScored()
    {
        return mScoringFactorsScored;
    }

    public void setScoringFactorsScored(Collection<FactorScored> mScoringFactorsScored)
    {
        this.mScoringFactorsScored = mScoringFactorsScored;
    }

    public String getComment()
    {
        return mComment;
    }

    public void setComment(String mComment)
    {
        this.mComment = mComment;
    }

    public double getAverageScore()
    {
        return mAverageScore;
    }

    public void setAverageScore(double mAverageScore)
    {
        this.mAverageScore = mAverageScore;
    }
    // CS:ON

}
