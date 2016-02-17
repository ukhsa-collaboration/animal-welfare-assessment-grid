package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "groups")
public class GroupAuth implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "group_name")
    private String mGroupName;

    public GroupAuth()
    {
    }

    public GroupAuth(String groupName)
    {
        this.mGroupName = groupName;
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
        GroupAuth other = (GroupAuth) obj;
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
        return true;
    }
}
