package uk.gov.phe.erdst.sc.awag.webapi.response.assessment;

import java.util.ArrayList;
import java.util.Collection;

import uk.gov.phe.erdst.sc.awag.utils.Constants;
import uk.gov.phe.erdst.sc.awag.webapi.response.ResponseDto;

public final class PreviousAssessmentDto implements ResponseDto
{
    public String date = Constants.EMPTY_STRING;
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
