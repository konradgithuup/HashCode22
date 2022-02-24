import java.io.*;
import java.util.*;

public class TrashCan {
    static String INFILE = "resources/" + "f_find_great_mentors.txt";
    static String OUTFILE = "resources/" + "f_output.txt";

    public static void main(String[] args) throws IOException{
        BufferedReader input;
        String line;
        String[] words;
        String[] skills;
        int amountContributor;
        int amountProjects;
        String name;
        int duration;
        int score;
        int bestBefore;
        LinkedList<String> skillNames = new LinkedList<>();
        LinkedList<Integer> skillLevel = new LinkedList<>();
        Contributor[] contributors;
        Project[] projects;

        try {
            input = new BufferedReader(new FileReader(INFILE));
        } catch (FileNotFoundException e) {
            System.err.println("file not found");
            return;
        }

        try {                           //read in first line
            line = input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        words = line.split(" ");
        amountContributor = Integer.parseInt(words[0]);
        amountProjects = Integer.parseInt(words[1]);
        contributors = new Contributor[amountContributor];
        projects = new Project[amountProjects];

        for (int i = 0; i < amountContributor; i++) { //read Contributor

            try {
                line = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            if (line == null) break;

            words = line.split(" ");
            name = words[0];

            for (int j = 0; j < Integer.parseInt(words[1]); j++) {//read skills + level
                try {
                    line = input.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                if (line == null) break;

                skills = line.split(" ");
                skillNames.add(skills[0]);
                skillLevel.add(Integer.parseInt(skills[1]));

            }
            contributors[i] = new Contributor(name,
                    skillNames.toArray(new String[skillNames.size()]),
                    skillLevel.toArray(new Integer[skillLevel.size()]));
            skillNames.clear();
            skillLevel.clear();
        }

        for (int i = 0; i < amountProjects; i++) { //read Project

            try {
                line = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            if (line == null) break;

            words = line.split(" ");
            name = words[0];
            duration = Integer.parseInt(words[1]);
            score = Integer.parseInt(words[2]);
            bestBefore = Integer.parseInt(words[3]);

            for(int j = 0; j < Integer.parseInt(words[4]); j++){     //read required skills + level
                try {
                    line = input.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                if (line == null) break;

                skills = line.split(" ");
                skillNames.add(skills[0]);
                skillLevel.add(Integer.parseInt(skills[1]));
            }
            projects[i] = new Project(name, duration, score, bestBefore,
                    skillNames.toArray(new String[skillNames.size()]),
                    skillLevel.toArray(new Integer[skillLevel.size()]));
            skillNames.clear();
            skillLevel.clear();
        }

        input.close();

        generateOutput(solve(projects, contributors));
        //System.out.println(Arrays.toString(solve(projects, contributors)));
    }

    public static Team[] solve(Project[] projects, Contributor[] contributors) {

        int numProjects = projects.length;

        Arrays.sort(projects, 0, numProjects, new ProjectComparator());

        Team[] teams = new Team[numProjects];

        for (int i=0; i<numProjects; i++){
            Project project = projects[i];
            teams[i] = new Team(project);

            for (int j=0; j<project.getReqSkills().length; j++){

                String role = project.getReqSkills()[j];
                int level = project.getReqSkillLevels()[j];

                ArrayList<Contributor> candidates = new ArrayList<Contributor>();

                for (Contributor c : contributors){
                    if(c.getRoleQualification(role, level) >= 0){
                        candidates.add(c);
                    }
                }
                if (candidates.size() == 0) continue;

                //ArrayList umwandeln
                Contributor[] candidatesArray = candidates.toArray(new Contributor[0]);
                Arrays.sort(candidatesArray, 0, candidates.size(), new ContributorComparator(role, level));

                boolean candidateFound = false;
                int counter = 0;
                int index = 0;
                int minMark = Integer.MAX_VALUE;

                while(!candidateFound){
                    if(counter < candidatesArray.length) {
                        Contributor candidate = candidatesArray[counter];

                        if (candidate.getMark() == 0) {
                            teams[i].addContributor(candidate, j);
                            candidateFound = true;
                            candidate.learn(role);
                            candidate.setMark(project.getDuration());
                        }
                        else {
                            if(candidate.getMark() < minMark){
                                index = counter;
                                minMark = candidate.getMark();
                            }
                            counter++;
                        }
                    }
                    else {
                        teams[i].addContributor(candidatesArray[index], j);
                        candidateFound = true;
                        candidatesArray[index].learn(role);
                        candidatesArray[index].setMark(project.getDuration());
                    }
                }
            }
        }

        return teams;
    }

    public static void generateOutput(Team[] teams) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(OUTFILE));
        int i = 0;
        String contribString = "";

        for (Team team : teams) {
            if (team.getContributors().length == 0) continue;
            String contribsubString = "";
            for (Contributor contributor : team.getContributors()) {
                if (contributor == null) continue;
                contribsubString += (" " + contributor.getName());
            }
            if (contribsubString.length() == 0) continue;
            contribString += team.project.getName() + "\n";
            contribString += contribsubString.substring(1) + "\n";
            i++;
        }
        writer.write(i + "\n");
        writer.write(contribString);
        writer.close();
    }
}
