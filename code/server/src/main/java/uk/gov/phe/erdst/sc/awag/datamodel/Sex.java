package uk.gov.phe.erdst.sc.awag.datamodel;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.google.gson.annotations.SerializedName;

@Entity
@Table(name = "sex")
@NamedQueries({@NamedQuery(name = Sex.Q_FIND_ALL, query = "SELECT s FROM Sex s"),
        @NamedQuery(name = Sex.Q_FIND_SEX_BY_NAME, query = "SELECT s FROM Sex s WHERE s.mName = :name")})
public class Sex implements Serializable
{
    public static final String Q_FIND_ALL = "findAllSexes";
    public static final String Q_FIND_SEX_BY_NAME = "findSexByName"; // TODO getEntityByNameField

    public static final String FEMALE = "Female";
    public static final String MALE = "Male";

    private static final long serialVersionUID = 1L;

    @Id
    @SerializedName("id")
    private Long mId;

    @SerializedName("name")
    private String mName;

    public Sex()
    {
    }

    public Sex(Long id)
    {
        mId = id;
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
}
