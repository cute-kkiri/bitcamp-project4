package command.project;


import command.Command;
import myapp.dao.ProjectDao;
import myapp.vo.Project;
import util.Prompt;

public class ProjectUpdateCommand implements Command {

  private ProjectDao projectDao;
  private ProjectMemberHandler memberHandler;

  public ProjectUpdateCommand(ProjectDao projectDao,
      ProjectMemberHandler memberHandler) {
    this.projectDao = projectDao;
    this.memberHandler = memberHandler;
  }

  @Override
  public void execute(String menuName) {
    System.out.printf("[%s]\n", menuName);
    int projectNo = Prompt.inputInt("프로젝트 번호?");

    try {
      Project project = projectDao.findBy(projectNo);
      if (project == null) {
        System.out.println("없는 프로젝트입니다.");
        return;
      }

      project.setTitle(Prompt.input("프로젝트명(%s)?", project.getTitle()));
      project.setDescription(Prompt.input("설명(%s)?", project.getDescription()));
      project.setStartDate(Prompt.input("시작일(%s)?", project.getStartDate()));
      project.setEndDate(Prompt.input("종료일(%s)?", project.getEndDate()));

      System.out.println("팀원:");
      memberHandler.deleteMembers(project);
      memberHandler.addMembers(project);

      projectDao.update(project);
      System.out.println("변경 했습니다.");

    } catch (Exception e) {
      System.out.println("변경 중 오류 발생!");
    }
  }

}
