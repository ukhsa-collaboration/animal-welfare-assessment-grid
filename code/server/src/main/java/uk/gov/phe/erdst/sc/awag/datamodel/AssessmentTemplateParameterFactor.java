package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "assessment_template_parameter_factor")
@NamedQueries({
        @NamedQuery(name = AssessmentTemplateParameterFactor.Q_FIND_ALL,
            query = "SELECT a FROM AssessmentTemplateParameterFactor a"),
        @NamedQuery(name = AssessmentTemplateParameterFactor.Q_DELETE_TEMPLATE_PARAMETER_FACTOR,
            query = "DELETE FROM AssessmentTemplateParameterFactor a WHERE a.mId.mAssessmentTemplateId=:templateId AND a.mId.mParameterId=:parameterId"),
        @NamedQuery(name = AssessmentTemplateParameterFactor.Q_FIND_ALL_TEMPLATE_PARAMETER_FACTORS,
            query = "SELECT a FROM AssessmentTemplateParameterFactor a WHERE a.mId.mAssessmentTemplateId=:templateId AND a.mId.mParameterId=:parameterId")})
public class AssessmentTemplateParameterFactor implements Serializable
{
    public static final String Q_FIND_ALL = "findAllAssessmentTemplateParameterFactors";
    public static final String Q_DELETE_TEMPLATE_PARAMETER_FACTOR = "deleteAssessmentTemplateParameterAndFactors";
    public static final String Q_FIND_ALL_TEMPLATE_PARAMETER_FACTORS = "findAllTemplateParameterFactors";

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private AssessmentTemplateParameterFactorPK mId;

    @ManyToOne()
    @JoinColumn(name = "assessment_template_id")
    private AssessmentTemplate mAssessmentTemplate;

    @ManyToOne
    @JoinColumn(name = "factor_id")
    private Factor mFactor;

    @ManyToOne
    @JoinColumn(name = "parameter_id")
    private Parameter mParameter;

    public AssessmentTemplateParameterFactor()
    {
    }

    public AssessmentTemplateParameterFactorPK getId()
    {
        return this.mId;
    }

    public void setId(AssessmentTemplateParameterFactorPK id)
    {
        this.mId = id;
    }

    public AssessmentTemplate getAssessmentTemplate()
    {
        return this.mAssessmentTemplate;
    }

    public void setAssessmentTemplate(AssessmentTemplate assessmentTemplate)
    {
        this.mAssessmentTemplate = assessmentTemplate;
    }

    public Factor getFactor()
    {
        return this.mFactor;
    }

    public void setFactor(Factor factor)
    {
        this.mFactor = factor;
    }

    public Parameter getParameter()
    {
        return this.mParameter;
    }

    public void setParameter(Parameter parameter)
    {
        this.mParameter = parameter;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mAssessmentTemplate == null) ? 0 : mAssessmentTemplate.hashCode());
        result = prime * result + ((mFactor == null) ? 0 : mFactor.hashCode());
        result = prime * result + ((mId == null) ? 0 : mId.hashCode());
        result = prime * result + ((mParameter == null) ? 0 : mParameter.hashCode());
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
        AssessmentTemplateParameterFactor other = (AssessmentTemplateParameterFactor) obj;
        if (mAssessmentTemplate == null)
        {
            if (other.mAssessmentTemplate != null)
            {
                return false;
            }
        }
        else if (!mAssessmentTemplate.equals(other.mAssessmentTemplate))
        {
            return false;
        }
        if (mFactor == null)
        {
            if (other.mFactor != null)
            {
                return false;
            }
        }
        else if (!mFactor.equals(other.mFactor))
        {
            return false;
        }
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
        if (mParameter == null)
        {
            if (other.mParameter != null)
            {
                return false;
            }
        }
        else if (!mParameter.equals(other.mParameter))
        {
            return false;
        }
        return true;
    }
}
