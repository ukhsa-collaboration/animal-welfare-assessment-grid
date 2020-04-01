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
@Table(name = "source")
@NamedQueries({@NamedQuery(name = Source.Q_FIND_ALL, query = "SELECT s FROM Source s"),
        @NamedQuery(name = Source.Q_FIND_COUNT_ALL, query = "SELECT COUNT(s) FROM Source s"),
        @NamedQuery(name = Source.Q_FIND_ALL_SOURCES_LIKE,
            query = "SELECT s FROM Source s WHERE LOWER(s.mName) LIKE :like ORDER BY LENGTH(s.mName), s.mName ASC"),
        @NamedQuery(name = Source.Q_FIND_SOURCE_BY_NAME, query = "SELECT s FROM Source s WHERE s.mName = :sourceName"),
        @NamedQuery(name = Source.Q_FIND_COUNT_ALL_SOURCES_LIKE,
            query = "SELECT COUNT(s) FROM Source s WHERE LOWER(s.mName) LIKE :like"),
        @NamedQuery(name = Source.Q_DELETE_SOURCE_BY_ID, query = "DELETE FROM Source s WHERE s.mId = :id")})
public class Source implements Serializable, EntitySelect
{
    public static final String Q_FIND_ALL = "findAllSources";
    public static final String Q_FIND_COUNT_ALL = "findCountAllSources";
    public static final String Q_FIND_ALL_SOURCES_LIKE = "findAllSourcesLike";
    public static final String Q_FIND_COUNT_ALL_SOURCES_LIKE = "findCountAllSourcesLike";
    public static final String Q_FIND_SOURCE_BY_NAME = "findSourceByName";
    public static final String Q_DELETE_SOURCE_BY_ID = "deleteSourceById";
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SerializedName("id")
    private Long mId;

    @Column(nullable = false, unique = true)
    @SerializedName("name")
    private String mName;

    public Source()
    {

    }

    public Source(Long id)
    {
        mId = id;
    }

    public Source(Long id, String name)
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
        Source other = (Source) obj;
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
