package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
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

@Entity
@Table(name = "study_group")
@NamedQueries(value = {
        @NamedQuery(name = StudyGroup.Q_FIND_ALL_STUDY_GROUPS_LIKE,
            query = "SELECT s FROM StudyGroup s WHERE LOWER(s.mStudyGroupNumber) LIKE :like "
                + "ORDER BY LENGTH(s.mStudyGroupNumber) ASC, s.mStudyGroupNumber ASC"),
        @NamedQuery(name = StudyGroup.Q_COUNT_ALL_STUDY_GROUPS_LIKE,
            query = "SELECT COUNT(s) FROM StudyGroup s WHERE LOWER(s.mStudyGroupNumber) LIKE :like"),
        @NamedQuery(name = StudyGroup.Q_FIND_ALL, query = "SELECT s FROM StudyGroup s"),
        @NamedQuery(name = StudyGroup.Q_FIND_COUNT_ALL, query = "SELECT COUNT(s) FROM StudyGroup s")})
public class StudyGroup implements Serializable, EntitySelect
{
    public static final String Q_FIND_ALL_STUDY_GROUPS_LIKE = "findAllStudyGroupsLike";
    public static final String Q_COUNT_ALL_STUDY_GROUPS_LIKE = "findCountAllStudyGroupsLike";
    public static final String Q_FIND_ALL = "findAllStudyGroups";
    public static final String Q_FIND_COUNT_ALL = "findCountAllStudyGroups";
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mId;

    @Column(nullable = false, unique = true)
    private String mStudyGroupNumber;

    @OneToMany
    @JoinTable(name = "study_group_animal", joinColumns = {@JoinColumn(name = "studygroup_mid")},
        inverseJoinColumns = {@JoinColumn(name = "manimals_mid")})
    private Set<Animal> mAnimals;

    public StudyGroup()
    {
    }

    public StudyGroup(Long id)
    {
        mId = id;
    }

    public Long getId()
    {
        return mId;
    }

    public String getStudyGroupNumber()
    {
        return this.mStudyGroupNumber;
    }

    public void setStudyGroupNumber(String studyGroupNumber)
    {
        this.mStudyGroupNumber = studyGroupNumber;
    }

    public Set<Animal> getAnimals()
    {
        return this.mAnimals;
    }

    public void setAnimals(Set<Animal> animals)
    {
        this.mAnimals = animals;
    }

    @Override
    public Long getEntitySelectId()
    {
        return mId;
    }

    @Override
    public String getEntitySelectName()
    {
        return mStudyGroupNumber;
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
        StudyGroup other = (StudyGroup) obj;
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
