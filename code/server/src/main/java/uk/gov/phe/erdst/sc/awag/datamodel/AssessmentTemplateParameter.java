package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the assessment_template_parameter database table.
 */
@Entity
@Table(name = "assessment_template_parameter")
@NamedQuery(name = "AssessmentTemplateParameter.findAll", query = "SELECT a FROM AssessmentTemplateParameter a")
public class AssessmentTemplateParameter implements Serializable
{
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private AssessmentTemplateParameterPK id;

    @Column(name = "clockwise_display_order_number")
    private Long clockwiseDisplayOrderNumber;

    // bi-directional many-to-one association to AssessmentTemplate
    @ManyToOne
    @JoinColumn(name = "assessment_template_id")
    private AssessmentTemplate mAssessmentTemplate;

    // bi-directional many-to-one association to Parameter
    @ManyToOne
    @JoinColumn(name = "parameter_id")
    private Parameter parameter;

    public AssessmentTemplateParameter()
    {
    }

    public AssessmentTemplateParameterPK getId()
    {
        return this.id;
    }

    public void setId(AssessmentTemplateParameterPK id)
    {
        this.id = id;
    }

    public Long getClockwiseDisplayOrderNumber()
    {
        return this.clockwiseDisplayOrderNumber;
    }

    public void setClockwiseDisplayOrderNumber(Long clockwiseDisplayOrderNumber)
    {
        this.clockwiseDisplayOrderNumber = clockwiseDisplayOrderNumber;
    }

    public AssessmentTemplate getmAssessmentTemplate()
    {
        return this.mAssessmentTemplate;
    }

    public void setmAssessmentTemplate(AssessmentTemplate assessmentTemplate)
    {
        this.mAssessmentTemplate = assessmentTemplate;
    }

    public Parameter getParameter()
    {
        return this.parameter;
    }

    public void setParameter(Parameter parameter)
    {
        this.parameter = parameter;
    }

}