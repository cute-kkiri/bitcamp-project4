package command.project;


import command.Command;
import myapp.dao.ProjectDao;
import myapp.vo.Project;

public class ProjectListCommand implements Command {

  private ProjectDao projectDao;

  public ProjectListCommand(ProjectDao projectDao) {
    this.projectDao = projectDao;
  }

  @Override
  public void execute(String menuName) {
    System.out.printf("[%s]\n", menuName);
    System.out.println("번호 프로젝트 기간");

    try {
      for (Project project : projectDao.list()) {
        System.out.printf("%d %s %s ~ %s\n",
            project.getNo(), project.getTitle(), project.getStartDate(), project.getEndDate());
      }
    } catch (Exception e) {
      System.out.println("목록 조회 중 오류 발생!");
    }
  }

}
