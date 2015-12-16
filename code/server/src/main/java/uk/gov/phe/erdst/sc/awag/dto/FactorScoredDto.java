package uk.gov.phe.erdst.sc.awag.dto;

public class FactorScoredDto
{
    public Long factorId;
    public String factorName;
    public Long factorScore;
    public boolean isIgnored;

    public FactorScoredDto()
    {
    }

    public FactorScoredDto(Long factorId)
    {
        this.factorId = factorId;
    }

    public FactorScoredDto(Long factorId, String factorName, Long factorScore, boolean isIgnored)
    {
        this.factorId = factorId;
        this.factorName = factorName;
        this.factorScore = factorScore;
        this.isIgnored = isIgnored;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((factorId == null) ? 0 : factorId.hashCode());
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
        FactorScoredDto other = (FactorScoredDto) obj;
        if (factorId == null)
        {
            if (other.factorId != null)
            {
                return false;
            }
        }
        else if (!factorId.equals(other.factorId))
        {
            return false;
        }
        return true;
    }
}
