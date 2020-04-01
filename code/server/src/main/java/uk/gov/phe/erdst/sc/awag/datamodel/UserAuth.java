package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class UserAuth implements Serializable, EntitySelect
{
    private static final long serialVersionUID = 6543408200434414777L;

    @Id
    @Column(name = "user_name")
    private String mUsername;

    @Column(name = "password")
    private String mPassword;

    @OneToMany(mappedBy = "mUser", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<UserGroupAuth> mUserGroups;

    public UserAuth()
    {
    }

    public String getUsername()
    {
        return mUsername;
    }

    public void setUsername(String username)
    {
        this.mUsername = username;
    }

    public String getPassword()
    {
        return mPassword;
    }

    public void setPassword(String password)
    {
        this.mPassword = password;
    }

    public List<UserGroupAuth> getUserGroups()
    {
        return mUserGroups;
    }

    public void setUserGroups(List<UserGroupAuth> userGroups)
    {
        this.mUserGroups = userGroups;
    }

    public UserGroupAuth addUserGroup(UserGroupAuth userGroup)
    {
        getUserGroups().add(userGroup);
        userGroup.setUser(this);
        return userGroup;
    }

    @Override
    public String getEntitySelectId()
    {
        return getUsername();
    }

    @Override
    public String getEntitySelectName()
    {
        return getUsername();
    }
}
