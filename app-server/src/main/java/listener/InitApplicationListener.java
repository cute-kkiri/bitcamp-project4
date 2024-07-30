package listener;

import context.ApplicationContext;
import dao.*;
import dao.skel.BoardDaoSkel;
import dao.skel.ProjectDaoSkel;
import dao.skel.UserDaoSkel;
import myapp.dao.BoardDao;
import myapp.dao.ProjectDao;
import myapp.dao.UserDao;

public class InitApplicationListener implements ApplicationListener {

  UserDao userDao;
  BoardDao boardDao;
  ProjectDao projectDao;

  @Override
  public void onStart(ApplicationContext ctx) throws Exception {
    userDao = new ListUserDao("data.xlsx");
    boardDao = new ListBoardDao("data.xlsx");
    projectDao = new ListProjectDao("data.xlsx", userDao);

    UserDaoSkel userDaoSkel = new UserDaoSkel(userDao);
    BoardDaoSkel boardDaoSkel = new BoardDaoSkel(boardDao);
    ProjectDaoSkel projectDaoSkel = new ProjectDaoSkel(projectDao);

    ctx.setAttribute("userDaoSkel", userDaoSkel);
    ctx.setAttribute("boardDaoSkel", boardDaoSkel);
    ctx.setAttribute("projectDaoSkel", projectDaoSkel);
  }

  @Override
  public void onShutdown(ApplicationContext ctx) throws Exception {
    try {
      ((ListUserDao) userDao).save();
    } catch (Exception e) {
      System.out.println("회원 데이터 저장 중 오류 발생!");
      e.printStackTrace();
      System.out.println();
    }

    try {
      ((ListBoardDao) boardDao).save();
    } catch (Exception e) {
      System.out.println("게시글 데이터 저장 중 오류 발생!");
      e.printStackTrace();
      System.out.println();
    }

    try {
      ((ListProjectDao) projectDao).save();
    } catch (Exception e) {
      System.out.println("프로젝트 데이터 저장 중 오류 발생!");
      e.printStackTrace();
      System.out.println();
    }
  }
}
