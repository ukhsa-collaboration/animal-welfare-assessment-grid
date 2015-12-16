package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "assessment_template")
@NamedQueries({@NamedQuery(name = AssessmentTemplate.Q_FIND_ALL, query = "SELECT at FROM AssessmentTemplate at"),
        @NamedQuery(name = AssessmentTemplate.Q_FIND_COUNT_ALL, query = "SELECT COUNT(at) FROM AssessmentTemplate at"),
        @NamedQuery(name = AssessmentTemplate.Q_FIND_WITH_IDS,
            query = "SELECT at FROM AssessmentTemplate at WHERE at.mId IN :ids"),
        @NamedQuery(name = AssessmentTemplate.Q_FIND_ALL_BY_NAME,
            query = "SELECT at FROM AssessmentTemplate at WHERE at.mName = :name"),
        @NamedQuery(name = AssessmentTemplate.Q_FIND_ALL_ASSESSMENT_TEMPLATES_LIKE,
            query = "SELECT at FROM AssessmentTemplate at WHERE LOWER(at.mName) LIKE :like ORDER BY at.mName"),
        @NamedQuery(name = AssessmentTemplate.Q_FIND_COUNT_ALL_ASSESSMENT_TEMPLATES_LIKE,
            query = "SELECT COUNT(at) FROM AssessmentTemplate at WHERE LOWER(at.mName) LIKE :like")})
public class AssessmentTemplate implements Serializable, EntitySelect
{
    public static final String Q_FIND_ALL = "findAllAssessmentTemplates";
    public static final String Q_FIND_COUNT_ALL = "findCountAllAssessmentTemplates";
    public static final String Q_FIND_ALL_ASSESSMENT_TEMPLATES_LIKE = "findAllAssessmentTemplatesLike";
    public static final String Q_FIND_COUNT_ALL_ASSESSMENT_TEMPLATES_LIKE = "findCountAllAssessmentTemplatesLike";
    public static final String Q_FIND_WITH_IDS = "findAssessmentTemplatesAllByIds";
    public static final String Q_FIND_ALL_BY_NAME = "findAllAssessmentTemplatesByName";

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mId;

    private String mName;

    @OneToMany(mappedBy = "mAssessmentTemplate", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<AssessmentTemplateParameterFactor> mAssessmentTemplateParameterFactors;

    @ManyToOne
    @JoinColumn(name = "mscale_mid")
    private Scale mScale;

    public AssessmentTemplate()
    {
    }

    public AssessmentTemplate(Long assessmentTemplateId)
    {
        this.mId = assessmentTemplateId;
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
        assessmentTemplateParameterFactor.setAssessmentTemplate(this);

        return assessmentTemplateParameterFactor;
    }

    public AssessmentTemplateParameterFactor
        removeAssessmentTemplateParameterFactor(AssessmentTemplateParameterFactor assessmentTemplateParameterFactor)
    {
        getAssessmentTemplateParameterFactors().remove(assessmentTemplateParameterFactor);
        assessmentTemplateParameterFactor.setAssessmentTemplate(null);

        return assessmentTemplateParameterFactor;
    }

    public Scale getScale()
    {
        return this.mScale;
    }

    public void setScale(Scale scale)
    {
        this.mScale = scale;
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
        AssessmentTemplate other = (AssessmentTemplate) obj;
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
