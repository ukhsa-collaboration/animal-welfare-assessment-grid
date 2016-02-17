package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserGroupAuthPK implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Column(name = "user_name", insertable = false, updatable = false)
    private String mUsername;

    @Column(name = "group_name", insertable = false, updatable = false)
    private String mGroupName;

    public UserGroupAuthPK()
    {
    }

    public void setUsername(String username)
    {
        this.mUsername = username;
    }

    public String getUsername()
    {
        return mUsername;
    }

    public void setGroupName(String groupName)
    {
        this.mGroupName = groupName;
    }

    public String getGroupName()
    {
        return mGroupName;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mGroupName == null) ? 0 : mGroupName.hashCode());
        result = prime * result + ((mUsername == null) ? 0 : mUsername.hashCode());
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
        UserGroupAuthPK other = (UserGroupAuthPK) obj;
        if (mGroupName == null)
        {
            if (other.mGroupName != null)
            {
                return false;
            }
        }
        else if (!mGroupName.equals(other.mGroupName))
        {
            return false;
        }
        if (mUsername == null)
        {
            if (other.mUsername != null)
            {
                return false;
            }
        }
        else if (!mUsername.equals(other.mUsername))
        {
            return false;
        }
        return true;
    }
}
