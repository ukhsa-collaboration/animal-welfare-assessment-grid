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
@Table(name = "animal")
@NamedQueries({@NamedQuery(name = Animal.Q_FIND_ALL, query = "SELECT a FROM Animal a"),
        @NamedQuery(name = Animal.Q_FIND_NON_DELETED_BY_ID,
            query = "SELECT a FROM Animal a WHERE a.mIsDeleted = false AND a.mId = :id"),
        @NamedQuery(name = Animal.Q_FIND_ALL_DELETED_OR_NOT,
            query = "SELECT a FROM Animal a WHERE a.mIsDeleted = :isDeleted"),
        @NamedQuery(name = Animal.Q_FIND_COUNT_ALL_DELETED_OR_NOT,
            query = "SELECT COUNT(a) FROM Animal a WHERE a.mIsDeleted = :isDeleted"),
        @NamedQuery(name = Animal.Q_FIND_IN_IDS, query = "SELECT a FROM Animal a WHERE a.mId IN :idList"),
        @NamedQuery(name = Animal.Q_FIND_ANIMAL_ASSESSMENT_TEMPLATE,
            query = "SELECT a.mAssessmentTemplate FROM Animal a WHERE a.mId = :id"),
        @NamedQuery(name = Animal.Q_DELETE_ANIMAL_BY_ID, query = "DELETE FROM Animal a WHERE a.mId = :id"),
        @NamedQuery(name = Animal.Q_FIND_NON_DELETED_LIKE_ORDERED,
            query = "SELECT a FROM Animal a " + "WHERE a.mIsDeleted = :isDeleted AND LOWER(a.mAnimalNumber)"
                + " LIKE :like ORDER BY LENGTH(a.mAnimalNumber) ASC, a.mAnimalNumber ASC"),
        @NamedQuery(name = Animal.Q_FIND_COUNT_NON_DELETED_LIKE_ORDERED,
            query = "SELECT COUNT(a) FROM Animal a "
                + "WHERE a.mIsDeleted = :isDeleted AND LOWER(a.mAnimalNumber) LIKE :like"),
        @NamedQuery(name = Animal.Q_FIND_NON_DELETED_LIKE_SEX_ORDERED,
            query = "SELECT a FROM Animal a " + "WHERE a.mIsDeleted = :isDeleted AND LOWER(a.mAnimalNumber)"
                + " LIKE :like AND a.mSex.mName= :sexName ORDER BY LENGTH(a.mAnimalNumber) ASC,"
                + " a.mAnimalNumber ASC"),
        @NamedQuery(name = Animal.Q_FIND_COUNT_NON_DELETED_LIKE_SEX_ORDERED, query = "SELECT COUNT(a) FROM Animal a "
            + "WHERE a.mIsDeleted = :isDeleted AND LOWER(a.mAnimalNumber)" + " LIKE :like AND a.mSex.mName= :sexName")})
public class Animal implements Serializable, EntitySelect
{
    public static final String Q_FIND_ALL = "findAllAnimals";
    public static final String Q_FIND_ALL_DELETED_OR_NOT = "findAllDeletedOrNotAnimals";
    public static final String Q_FIND_COUNT_ALL_DELETED_OR_NOT = "findCountAllDeletedOrNotAnimals";
    public static final String Q_FIND_IN_IDS = "findAnimalsInIds";
    public static final String Q_FIND_ANIMAL_ASSESSMENT_TEMPLATE = "findAnimalAssessmentTemplate";
    public static final String Q_DELETE_ANIMAL_BY_ID = "deleteAnimalById";
    public static final String Q_FIND_NON_DELETED_LIKE_ORDERED = "findNonDeletedAnimalsLikeOrdered";
    public static final String Q_FIND_NON_DELETED_BY_ID = "findNonDeletedAnimalById";
    public static final String Q_FIND_COUNT_NON_DELETED_LIKE_ORDERED = "findCountNonDeletedAnimalsLikeOrdered";
    public static final String Q_FIND_COUNT_NON_DELETED_LIKE_SEX_ORDERED = "findCountNonDeletedLikeSexOrdered";
    public static final String Q_FIND_NON_DELETED_LIKE_SEX_ORDERED = "findNonDeletedAnimalsSexLikeOrdered";
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SerializedName("id")
    private Long mId;

    @Column(nullable = false, unique = true)
    @SerializedName("number")
    private String mAnimalNumber;

    @SerializedName("species")
    private Species mSpecies;

    @SerializedName("sex")
    private Sex mSex;

    @SerializedName("source")
    private Source mSource;

    @SerializedName("dateOfBirth")
    private String mDateOfBirth;

    @SerializedName("dam")
    private Animal mDam;

    @SerializedName("father")
    private Animal mFather;

    @SerializedName("isAlive")
    private boolean mIsAlive;

    @SerializedName("assessmentTemplate")
    private AssessmentTemplate mAssessmentTemplate;

    @SerializedName("isAssessed")
    private Boolean mIsAssessed;

    @SerializedName("isDeleted")
    private boolean mIsDeleted;

    public Animal()
    {
    }

    public Animal(long id)
    {
        mId = id;
    }

    public Long getId()
    {
        return mId;
    }

    // CS:OFF: HiddenFieldCheck
    public void setId(Long mId)
    {
        this.mId = mId;
    }

    public String getAnimalNumber()
    {
        return mAnimalNumber;
    }

    public void setAnimalNumber(String mAnimalNumber)
    {
        this.mAnimalNumber = mAnimalNumber;
    }

    public Species getSpecies()
    {
        return mSpecies;
    }

    public void setSpecies(Species mSpecies)
    {
        this.mSpecies = mSpecies;
    }

    public Sex getSex()
    {
        return mSex;
    }

    public void setSex(Sex mSex)
    {
        this.mSex = mSex;
    }

    public Source getSource()
    {
        return mSource;
    }

    public void setSource(Source mSource)
    {
        this.mSource = mSource;
    }

    public Animal getDam()
    {
        return mDam;
    }

    public void setDam(Animal mDam)
    {
        this.mDam = mDam;
    }

    public Animal getFather()
    {
        return mFather;
    }

    public void setFather(Animal mFather)
    {
        this.mFather = mFather;
    }

    public boolean isAlive()
    {
        return mIsAlive;
    }

    public void setIsAlive(boolean mIsAlive)
    {
        this.mIsAlive = mIsAlive;
    }

    public Boolean isAssessed()
    {
        return mIsAssessed;
    }

    public void setIsAssessed(Boolean mIsAssessed)
    {
        this.mIsAssessed = mIsAssessed;
    }

    public String getDateOfBirth()
    {
        return mDateOfBirth;
    }

    public void setDateOfBirth(String mDateOfBirth)
    {
        this.mDateOfBirth = mDateOfBirth;
    }

    public AssessmentTemplate getAssessmentTemplate()
    {
        return mAssessmentTemplate;
    }

    public void setAssessmentTemplate(AssessmentTemplate mAssessmentTemplate)
    {
        this.mAssessmentTemplate = mAssessmentTemplate;
    }

    public boolean isDeleted()
    {
        return mIsDeleted;
    }

    public void setIsDeleted(boolean mIsDeleted)
    {
        this.mIsDeleted = mIsDeleted;
    }

    @Override
    public Long getEntitySelectId()
    {
        return mId;
    }

    @Override
    public String getEntitySelectName()
    {
        return mAnimalNumber;
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
        Animal other = (Animal) obj;
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
    // CS:ON
}
