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
@Table(name = "animal_housing")
@NamedQueries({@NamedQuery(name = AnimalHousing.Q_FIND_ALL, query = "SELECT h FROM AnimalHousing h"),
        @NamedQuery(name = AnimalHousing.Q_FIND_COUNT_ALL, query = "SELECT COUNT(h) FROM AnimalHousing h"),
        @NamedQuery(name = AnimalHousing.Q_FIND_BY_NAME, query = "SELECT h FROM AnimalHousing h WHERE h.mName = :name"),
        @NamedQuery(name = AnimalHousing.Q_FIND_ALL_LIKE,
            // CS:OFF: LineLength
            query = "SELECT h FROM AnimalHousing h WHERE LOWER(h.mName) LIKE :like ORDER BY LENGTH(h.mName), h.mName ASC"),
        // CS:ON
        @NamedQuery(name = AnimalHousing.Q_FIND_COUNT_ALL_LIKE,
            query = "SELECT COUNT(h) FROM AnimalHousing h WHERE LOWER(h.mName) LIKE :like")})
public class AnimalHousing implements Serializable, EntitySelect
{
    public static final String Q_FIND_ALL = "findAllAnimalHousing";
    public static final String Q_FIND_COUNT_ALL = "findCountAllAnimalHousing";
    public static final String Q_FIND_BY_NAME = "findHousingByName";
    public static final String Q_FIND_ALL_LIKE = "findAllHousingLike";
    public static final String Q_FIND_COUNT_ALL_LIKE = "findCountAllHousingLike";

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SerializedName("id")
    private Long mId;

    @Column(nullable = false, unique = true)
    @SerializedName("name")
    private String mName;

    public AnimalHousing()
    {

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
        mName = name;
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
        AnimalHousing other = (AnimalHousing) obj;
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
