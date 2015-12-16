package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.gson.annotations.SerializedName;

@Entity
@Table(name = "study")
@NamedQueries(value = {@NamedQuery(name = Study.Q_FIND_ALL, query = "SELECT s FROM Study s"),
        @NamedQuery(name = Study.Q_FIND_COUNT_ALL, query = "SELECT COUNT(s) FROM Study s"),
        // CS:OFF: LineLength
        @NamedQuery(name = Study.Q_FIND_ALL_STUDIES_LIKE,
            query = "SELECT s FROM Study s WHERE LOWER(s.mStudyNumber) LIKE :like ORDER BY LENGTH(s.mStudyNumber) ASC,"
                + " s.mStudyNumber ASC"),
        @NamedQuery(name = Study.Q_FIND_COUNT_ALL_STUDIES_LIKE,
            query = "SELECT COUNT(s) FROM Study s WHERE LOWER(s.mStudyNumber) LIKE :like"),
        @NamedQuery(name = Study.Q_FIND_STUDY_WITH_ANIMAL,
            query = "SELECT s FROM Study s JOIN s.mStudyGroups groups JOIN groups.mAnimals animals WHERE s.mIsOpen = true AND animals = :animal")})
// CS:ON
public class Study implements Serializable, EntitySelect
{
    public static final String Q_FIND_ALL = "findAll";
    public static final String Q_FIND_COUNT_ALL = "findCountAll";
    public static final String Q_FIND_ALL_STUDIES_LIKE = "findAllStudiesLike";
    public static final String Q_FIND_COUNT_ALL_STUDIES_LIKE = "findCountAllStudiesLike";
    public static final String Q_FIND_STUDY_WITH_ANIMAL = "findStudyWithAnimal";
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SerializedName(value = "studyId")
    private Long mId;

    @SerializedName(value = "studyIsOpen")
    private boolean mIsOpen;

    @SerializedName(value = "studyName")
    private String mStudyNumber;

    @OneToMany
    @JoinTable(name = "study_study_group", joinColumns = {@JoinColumn(name = "study_mid")},
        inverseJoinColumns = {@JoinColumn(name = "mgroups_mid")})
    @SerializedName(value = "studyGroups")
    private Set<StudyGroup> mStudyGroups;

    public Long getId()
    {
        return mId;
    }

    public void setId(Long id)
    {
        mId = id;
    }

    public boolean isOpen()
    {
        return mIsOpen;
    }

    public void setIsOpen(boolean isOpen)
    {
        mIsOpen = isOpen;
    }

    public String getStudyNumber()
    {
        return mStudyNumber;
    }

    public void setStudyNumber(String studyNumber)
    {
        mStudyNumber = studyNumber;
    }

    public Set<StudyGroup> getStudyGroups()
    {
        return mStudyGroups;
    }

    public void setStudyGroups(Set<StudyGroup> studyGroups)
    {
        mStudyGroups = studyGroups;
    }

    @Override
    public Long getEntitySelectId()
    {
        return mId;
    }

    @Override
    public String getEntitySelectName()
    {
        return mStudyNumber;
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
        Study other = (Study) obj;
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
