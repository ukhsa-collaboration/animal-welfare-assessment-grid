package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the assessment_template_parameter database table.
 */
@Embeddable
public class AssessmentTemplateParameterPK implements Serializable
{
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "assessment_template_id", insertable = false, updatable = false)
    private Long assessmentTemplateId;

    @Column(name = "parameter_id", insertable = false, updatable = false)
    private Long parameterId;

    public AssessmentTemplateParameterPK()
    {
    }

    public Long getAssessmentTemplateId()
    {
        return this.assessmentTemplateId;
    }

    public void setAssessmentTemplateId(Long assessmentTemplateId)
    {
        this.assessmentTemplateId = assessmentTemplateId;
    }

    public Long getParameterId()
    {
        return this.parameterId;
    }

    public void setParameterId(Long parameterId)
    {
        this.parameterId = parameterId;
    }

    @Override
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }
        if (!(other instanceof AssessmentTemplateParameterPK))
        {
            return false;
        }
        AssessmentTemplateParameterPK castOther = (AssessmentTemplateParameterPK) other;
        return this.assessmentTemplateId.equals(castOther.assessmentTemplateId)
            && this.parameterId.equals(castOther.parameterId);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.assessmentTemplateId.hashCode();
        hash = hash * prime + this.parameterId.hashCode();

        return hash;
    }
}