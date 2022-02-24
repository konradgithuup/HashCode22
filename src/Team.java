import java.util.Arrays;

public class Team {

    Project project;
    Contributor[] contributors;                     //ordnet Contributors einem Skill zu (Indizes übereinstimment mit Skill-Array von project)

    public Team(Project _project) {
        project = _project;
        contributors = new Contributor[project.getReqSkills().length];
    }

    public void addContributor(Contributor c, int i){
        contributors[i] = c;
    }

    public Project getProject(){
        return project;
    }

    public Contributor[] getContributors() {
        return contributors;
    }

    @Override
    public String toString() {
        return "Team{" +
                "project=" + project.getName() +
                ", contributors=" + Arrays.toString(contributors) +
                '}';
    }
}
