import java.util.Comparator;
import java.util.HashMap;

public class Contributor {
    String name;
    HashMap<String, Integer> skills;
    int mark;

    public Contributor(String name, String[] skillNames, Integer[] skillLevel) {
        this.name = name;
        this.skills = new HashMap<>();
        this.mark = 0;

        for (int i = 0; i < skillNames.length; i++) {
            this.skills.put(skillNames[i], skillLevel[i]);
        }
    }

    public String getName() {
        return name;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark += mark;
    }

    // returns over/underqualification for a specific task, -1 if underqualified
    public int getRoleQualification(String requiredSkill, int requiredLevel) {
        Integer currentLevel = skills.get(requiredSkill);
        if (currentLevel == null) return -1;
        return skills.values().stream().reduce(0, Integer::sum) - requiredLevel;
    }

    public void learn(String skillName) {
        Integer currentLevel = skills.get(skillName);
        if (currentLevel != null) {
            skills.replace(skillName, ++currentLevel);
            return;
        }
        skills.put(skillName, 1);
    }

    @Override
    public String toString() {
        return "Contributor{" +
                "name='" + name + '\'' +
                ", skills=" + skills +
                '}';
    }
}


class ContributorComparator implements Comparator<Contributor> {
    String reqRole;
    int reqLevel;

    public ContributorComparator(String role, int level) {
        reqRole = role;
        reqLevel = level;
    }

    @Override
    public int compare(Contributor c1, Contributor c2) {
        int t1 = c1.getRoleQualification(reqRole, reqLevel);
        int t2 = c2.getRoleQualification(reqRole, reqLevel);

        if (t1 == t2) return 0;
        return (t1 > t2? 1 : -1);
    }
}