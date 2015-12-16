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
@Table(name = "users")
@NamedQueries({@NamedQuery(name = User.Q_FIND_ALL, query = "SELECT u FROM User u"),
        @NamedQuery(name = User.Q_FIND_COUNT_ALL, query = "SELECT COUNT(u) FROM User u"),
        @NamedQuery(name = User.Q_FIND_BY_NAME, query = "SELECT u FROM User u WHERE u.mName = :name"),
        @NamedQuery(name = User.Q_FIND_ALL_LIKE_ORDERED,
            query = "SELECT u FROM User u WHERE LOWER(u.mName) LIKE :like ORDER BY LENGTH(u.mName), u.mName ASC"),
        @NamedQuery(name = User.Q_FIND_COUNT_ALL_LIKE_ORDERED,
            query = "SELECT COUNT(u) FROM User u WHERE LOWER(u.mName)" + " LIKE :like")})
public class User implements Serializable, EntitySelect
{
    public static final String Q_FIND_ALL = "findAllUsers";
    public static final String Q_FIND_COUNT_ALL = "findCountAllUsers";
    public static final String Q_FIND_BY_NAME = "findUserByName";
    public static final String Q_FIND_ALL_LIKE_ORDERED = "findAllUsersLikeOrdered";
    public static final String Q_FIND_COUNT_ALL_LIKE_ORDERED = "findCountAllUsersLikeOrdered";

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SerializedName("id")
    private Long mId;

    @Column(nullable = false, unique = true)
    @SerializedName("name")
    private String mName;

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
        result = prime * result + ((mName == null) ? 0 : mName.hashCode());
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

        User other = (User) obj;
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

        if (mName == null)
        {
            if (other.mName != null)
            {
                return false;
            }
        }
        else if (!mName.equals(other.mName))
        {
            return false;
        }

        return true;
    }

}
