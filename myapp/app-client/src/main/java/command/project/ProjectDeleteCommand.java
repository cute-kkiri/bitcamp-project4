package command.project;


import command.Command;
import myapp.dao.ProjectDao;
import myapp.vo.Project;
import util.Prompt;

public class ProjectDeleteCommand implements Command {

  private ProjectDao projectDao;

  public ProjectDeleteCommand(ProjectDao projectDao) {
    this.projectDao = projectDao;
  }

  @Override
  public void execute(String menuName) {
    System.out.printf("[%s]\n", menuName);
    int projectNo = Prompt.inputInt("프로젝트 번호?");

    try {
      Project deletedProject = projectDao.findBy(projectNo);
      if (deletedProject == null) {
        System.out.println("없는 프로젝트입니다.");
        return;
      }

      projectDao.delete(projectNo);
      System.out.printf("%d번 프로젝트를 삭제 했습니다.\n", deletedProject.getNo());

    } catch (Exception e) {
      System.out.println("삭제 중 오류 발생!");
    }
  }
}
