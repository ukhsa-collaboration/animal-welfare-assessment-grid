package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;
import javax.persistence.*;

@Embeddable
public class AssessmentTemplateParameterFactorPK implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Column(name = "assessment_template_id", insertable = false, updatable = false)
    private Long mAssessmentTemplateId;

    @Column(name = "parameter_id", insertable = false, updatable = false)
    private Long mParameterId;

    @Column(name = "factor_id", insertable = false, updatable = false)
    private Long mFactorId;

    public AssessmentTemplateParameterFactorPK()
    {
    }

    public Long getAssessmentTemplateId()
    {
        return this.mAssessmentTemplateId;
    }

    public void setAssessmentTemplateId(Long assessmentTemplateId)
    {
        this.mAssessmentTemplateId = assessmentTemplateId;
    }

    public Long getParameterId()
    {
        return this.mParameterId;
    }

    public void setParameterId(Long parameterId)
    {
        this.mParameterId = parameterId;
    }

    public Long getFactorId()
    {
        return this.mFactorId;
    }

    public void setFactorId(Long factorId)
    {
        this.mFactorId = factorId;
    }

    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }
        if (!(other instanceof AssessmentTemplateParameterFactorPK))
        {
            return false;
        }
        AssessmentTemplateParameterFactorPK castOther = (AssessmentTemplateParameterFactorPK) other;
        return this.mAssessmentTemplateId.equals(castOther.mAssessmentTemplateId)
            && this.mParameterId.equals(castOther.mParameterId) && this.mFactorId.equals(castOther.mFactorId);
    }

    public int hashCode()
    {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.mAssessmentTemplateId.hashCode();
        hash = hash * prime + this.mParameterId.hashCode();
        hash = hash * prime + this.mFactorId.hashCode();

        return hash;
    }
}