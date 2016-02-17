package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "users_groups")
public class UserGroupAuth implements Serializable
{
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private UserGroupAuthPK mId;

    @ManyToOne()
    @JoinColumn(name = "user_name")
    private UserAuth mUser;

    @ManyToOne()
    @JoinColumn(name = "group_name")
    private GroupAuth mGroup;

    public UserGroupAuth()
    {
    }

    public void setId(UserGroupAuthPK id)
    {
        this.mId = id;
    }

    public UserGroupAuthPK getId()
    {
        return mId;
    }

    public UserAuth getUser()
    {
        return mUser;
    }

    public void setUser(UserAuth mUser)
    {
        this.mUser = mUser;
    }

    public GroupAuth getGroup()
    {
        return mGroup;
    }

    public void setGroup(GroupAuth mGroup)
    {
        this.mGroup = mGroup;
    }
}
