package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.google.gson.annotations.SerializedName;

@Entity
@Table(name = "assessment")
@NamedQueries({@NamedQuery(name = Assessment.Q_FIND_ALL, query = "SELECT a FROM Assessment a"),
        @NamedQuery(name = Assessment.Q_COUNT_FIND_ALL, query = "SELECT Count(a) FROM Assessment a"),
        @NamedQuery(name = Assessment.Q_FIND_PREV_ANIMAL_ASSESSMENT,
            query = "SELECT a FROM Assessment a WHERE a.mAnimal.mId = :animalId" + " ORDER BY a.mId DESC"),
        @NamedQuery(name = Assessment.Q_ANIMAL_ASSESSMENT_BETWEEN,
            query = "SELECT a FROM Assessment a WHERE a.mAnimal.mId = :animalId AND a.mDate "
                + "BETWEEN :fromDate AND :toDate ORDER BY a.mDate ASC"),
        @NamedQuery(name = Assessment.Q_COUNT_ANIMAL_ASSESSMENT_BETWEEN,
            query = "SELECT COUNT(a) FROM Assessment a WHERE a.mAnimal.mId = :animalId AND a.mDate "
                + "BETWEEN :fromDate AND :toDate"),
        @NamedQuery(name = Assessment.Q_COUNT_ANIMAL_ASSESSMENTS,
            query = "SELECT COUNT(a) FROM Assessment a WHERE a.mAnimal.mId = :animalId"),
        @NamedQuery(name = Assessment.Q_COUNT_TEMPLATE_ASSESSMENTS,
            query = "SELECT COUNT(a) FROM Assessment a WHERE a.mAnimal.mAssessmentTemplate.mId = :templateId")})
public class Assessment implements Serializable
{
    public static final String Q_FIND_ALL = "findAllAssessments";
    public static final String Q_FIND_PREV_ANIMAL_ASSESSMENT = "findPrevAnimalAssessment";
    public static final String Q_ANIMAL_ASSESSMENT_BETWEEN = "findAnimalAssessmentBetween";
    public static final String Q_COUNT_FIND_ALL = "countFindAllAssessments";
    public static final String Q_COUNT_ANIMAL_ASSESSMENT_BETWEEN = "findCountAnimalAssessmentBetween";
    public static final String Q_COUNT_ANIMAL_ASSESSMENTS = "findCountAnimalAssessments";

    private static final long serialVersionUID = 1L;
    public static final String Q_COUNT_TEMPLATE_ASSESSMENTS = "findCountTemplateAssessments";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SerializedName("id")
    private Long mId;

    @SerializedName("animal")
    private Animal mAnimal;

    @SerializedName("study")
    private Study mStudy;

    @SerializedName("reason")
    private AssessmentReason mReason;

    @SerializedName("date")
    private String mDate;

    @SerializedName("animalHousing")
    private AnimalHousing mAnimalHousing;

    @SerializedName("performedBy")
    private User mPerformedBy;

    @SerializedName("score")
    @OneToOne(cascade = CascadeType.ALL)
    private AssessmentScore mScore;

    @SerializedName("isComplete")
    private boolean mIsComplete;

    public Long getId()
    {
        return mId;
    }

    // CS:OFF: HiddenFieldCheck
    public void setId(Long mId)
    {
        this.mId = mId;
    }

    public Animal getAnimal()
    {
        return mAnimal;
    }

    public void setAnimal(Animal mAnimal)
    {
        this.mAnimal = mAnimal;
    }

    public Study getStudy()
    {
        return mStudy;
    }

    public void setStudy(Study study)
    {
        this.mStudy = study;
    }

    public AssessmentReason getReason()
    {
        return mReason;
    }

    public void setReason(AssessmentReason mReason)
    {
        this.mReason = mReason;
    }

    public String getDate()
    {
        return mDate;
    }

    public void setDate(String mDate)
    {
        this.mDate = mDate;
    }

    public AnimalHousing getAnimalHousing()
    {
        return mAnimalHousing;
    }

    public void setAnimalHousing(AnimalHousing mAnimalHousing)
    {
        this.mAnimalHousing = mAnimalHousing;
    }

    public User getPerformedBy()
    {
        return mPerformedBy;
    }

    public void setPerformedBy(User mPerformedBy)
    {
        this.mPerformedBy = mPerformedBy;
    }

    public AssessmentScore getScore()
    {
        return mScore;
    }

    public void setScore(AssessmentScore mScore)
    {
        this.mScore = mScore;
    }

    public boolean isComplete()
    {
        return mIsComplete;
    }

    public void setIsComplete(boolean isComplete)
    {
        this.mIsComplete = isComplete;
    }

    // CS:ON
}
