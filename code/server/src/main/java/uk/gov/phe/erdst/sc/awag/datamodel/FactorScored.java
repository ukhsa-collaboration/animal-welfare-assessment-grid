package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.annotations.SerializedName;

@Entity
@Table(name = "factor_scored")
public class FactorScored implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SerializedName("id")
    private Long mId;

    @SerializedName("scoringFactor")
    private Factor mScoringFactor;

    @SerializedName("score")
    private int mScore;

    @SerializedName("isIgnored")
    private boolean mIsIgnored;

    public Long getId()
    {
        return mId;
    }

    // CS:OFF: HiddenFieldCheck
    public void setId(Long mId)
    {
        this.mId = mId;
    }

    public Factor getScoringFactor()
    {
        return mScoringFactor;
    }

    public void setScoringFactor(Factor mScoringFactor)
    {
        this.mScoringFactor = mScoringFactor;
    }

    public int getScore()
    {
        return mScore;
    }

    public void setScore(int mScore)
    {
        this.mScore = mScore;
    }

    public boolean isIgnored()
    {
        return mIsIgnored;
    }

    public void setIsIgnored(boolean mIsIgnored)
    {
        this.mIsIgnored = mIsIgnored;
    }

    // CS:ON
}
