package uk.gov.phe.erdst.sc.awag.dto.assessment;

import java.util.ArrayList;
import java.util.Collection;

public final class PreviousAssessmentDto
{
    public String date = "";
    public boolean isComplete = false;
    public Collection<Score> scores = new ArrayList<Score>();

    public static class Score
    {
        public String name;
        public String avgScore;

        public Score(String name, String avgScore)
        {
            this.name = name;
            this.avgScore = avgScore;
        }
    }
}
