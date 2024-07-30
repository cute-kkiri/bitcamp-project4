package command.project;


import command.Command;
import myapp.dao.ProjectDao;
import myapp.vo.Project;
import util.Prompt;

public class ProjectAddCommand implements Command {

  private ProjectDao projectDao;
  private ProjectMemberHandler memberHandler;

  public ProjectAddCommand(ProjectDao projectDao, ProjectMemberHandler memberHandler) {
    this.projectDao = projectDao;
    this.memberHandler = memberHandler;
  }

  @Override
  public void execute(String menuName) {
    System.out.printf("[%s]\n", menuName);

    try {
      Project project = new Project();
      project.setTitle(Prompt.input("프로젝트명?"));
      project.setDescription(Prompt.input("설명?"));
      project.setStartDate(Prompt.input("시작일?"));
      project.setEndDate(Prompt.input("종료일?"));

      System.out.println("팀원:");
      memberHandler.addMembers(project);

      projectDao.insert(project);

      System.out.println("등록했습니다.");

    } catch (Exception e) {
      System.out.println("등록 중 오류 발생!");
    }
  }
}
