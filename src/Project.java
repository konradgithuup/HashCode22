import java.util.Arrays;
import java.util.Comparator;

public class Project {

    String name;
    int duration;
    int score;
    int bestBefore;

    String[] reqSkills;
    Integer[] reqSkillLevels;

    public Project(String _name, int _duration, int _score, int _bestBefore, String[] _reqSkills, Integer[] _reqSkillLevels) {
        name = _name;
        duration = _duration;
        score = _score;
        bestBefore = _bestBefore;
        reqSkills = _reqSkills;
        reqSkillLevels = _reqSkillLevels;
    }

    public String getName(){
        return name;
    }

    public int getDuration(){
        return duration;
    }

    public int getPayOff(int day) {
        return (score < day? 0 : score-day);
    }

    public int getScore(){
        return score;
    }

    public int getBestBefore(){
        return bestBefore;
    }

    public String[] getReqSkills(){
        return reqSkills;
    }

    public Integer[] getReqSkillLevels(){
        return reqSkillLevels;
    }

    // determines project urgency
    public int getSpareProjectTime() {
        return bestBefore - duration;
    }

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                ", score=" + score +
                ", bestBefore=" + bestBefore +
                ", reqSkills=" + Arrays.toString(reqSkills) +
                ", reqSkillLevels=" + Arrays.toString(reqSkillLevels) +
                '}';
    }
}

class ProjectComparator implements Comparator<Project> {
    @Override
    public int compare(Project p1, Project p2) {
        int t1 = p1.getSpareProjectTime();
        int t2 = p2.getSpareProjectTime();

        if (t1 == t2) return 0;
        return (t1 > t2? 1 : -1);
    }
}
