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
@Table(name = "species")
@NamedQueries({@NamedQuery(name = Species.Q_FIND_ALL, query = "SELECT s FROM Species s"),
        // CS:OFF: LineLength
        @NamedQuery(name = Species.Q_FIND_ALL_NON_DELETED,
            query = "SELECT s FROM Species s WHERE s.mIsDeleted = false"),
        // CS:ON
        @NamedQuery(name = Species.Q_FIND_COUNT_ALL_NON_DELETED,
            query = "SELECT COUNT(s) FROM Species s WHERE s.mIsDeleted = false"),
        @NamedQuery(name = Species.Q_FIND_ALL_NON_DELETED_LIKE_ORDERED,
            query = "SELECT s FROM Species s "
                + "WHERE s.mIsDeleted = false AND LOWER(s.mName) LIKE :like ORDER BY LENGTH(s.mName) ASC, s.mName ASC"),
        @NamedQuery(name = Species.Q_FIND_COUNT_ALL_NON_DELETED_LIKE_ORDERED,
            query = "SELECT COUNT(s) FROM Species s " + "WHERE s.mIsDeleted = false AND LOWER(s.mName) LIKE :like"),
        @NamedQuery(name = Species.Q_FIND_SPECIES_BY_NAME,
            query = "SELECT s FROM Species s WHERE s.mName = :speciesName"),
        @NamedQuery(name = Species.Q_DELETE_SPECIES_BY_ID, query = "DELETE FROM Species s WHERE s.mId = :id")})
public class Species implements Serializable, EntitySelect
{
    public static final String Q_FIND_ALL = "findAllSpecies";
    public static final String Q_FIND_ALL_NON_DELETED = "findAllSpeciesNonDeleted";
    public static final String Q_FIND_COUNT_ALL_NON_DELETED = "findCountAllSpeciesNonDeleted";
    public static final String Q_FIND_ALL_NON_DELETED_LIKE_ORDERED = "findAllNonDeletedSpeciesLikeOrdered";
    public static final String Q_FIND_COUNT_ALL_NON_DELETED_LIKE_ORDERED = "findCountAllNonDeletedSpeciesLikeOrdered";
    public static final String Q_FIND_SPECIES_BY_NAME = "findSpeciesByName";
    public static final String Q_DELETE_SPECIES_BY_ID = "deleteSpeciesById";
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SerializedName("id")
    private Long mId;

    @Column(nullable = false, unique = true)
    @SerializedName("name")
    private String mName;

    private boolean mIsDeleted;

    public Species()
    {
    }

    public Species(Long id)
    {
        mId = id;
    }

    public Species(Long id, String name)
    {
        mId = id;
        mName = name;
    }

    public Long getId()
    {
        return mId;
    }

    public void setId(Long id)
    {
        mId = id;
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String name)
    {
        this.mName = name;
    }

    public boolean isDeleted()
    {
        return mIsDeleted;
    }

    public void setIsDeleted(boolean isDeleted)
    {
        mIsDeleted = isDeleted;
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
        Species other = (Species) obj;
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
