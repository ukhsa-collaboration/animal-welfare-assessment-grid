package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.annotations.SerializedName;

@Entity
@Table(name = "activity_log")
public class ActivityLog implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SerializedName("id")
    private Long mId;

    @Column(nullable = false)
    @SerializedName("dateTime")
    private String mDateTime;

    @Column(nullable = false)
    @SerializedName("action")
    private String mAction;

    @Column(nullable = false)
    @SerializedName("username")
    private String mUsername;

    public Long getId()
    {
        return mId;
    }

    public void setId(Long id)
    {
        mId = id;
    }

    public String getDateTime()
    {
        return mDateTime;
    }

    public void setDateTime(String dateTime)
    {
        mDateTime = dateTime;
    }

    public String getAction()
    {
        return mAction;
    }

    public void setAction(String action)
    {
        mAction = action;
    }

    public String getUsername()
    {
        return mUsername;
    }

    public void setUsername(String username)
    {
        mUsername = username;
    }
}
