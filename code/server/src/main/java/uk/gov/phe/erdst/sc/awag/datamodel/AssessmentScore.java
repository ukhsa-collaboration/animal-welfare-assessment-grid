package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.gson.annotations.SerializedName;

@Entity
@Table(name = "assessment_score")
@NamedQueries({@NamedQuery(name = AssessmentScore.Q_FIND_ALL, query = "SELECT a FROM AssessmentScore a")})
public class AssessmentScore implements Serializable
{
    public static final String Q_FIND_ALL = "findAllAssessmentScores";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SerializedName("id")
    private Long mId;

    @SerializedName("parametersScored")
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<ParameterScore> mParametersScored;

    public Long getmId()
    {
        return mId;
    }

    public void setmId(Long id)
    {
        mId = id;
    }

    public Collection<ParameterScore> getParametersScored()
    {
        return mParametersScored;
    }

    public void setParametersScored(Collection<ParameterScore> parametersScored)
    {
        mParametersScored = parametersScored;
    }

}
