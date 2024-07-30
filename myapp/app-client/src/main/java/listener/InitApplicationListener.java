package listener;

import command.HelpCommand;
import command.HistoryCommand;
import command.board.*;
import command.project.*;
import command.user.*;
import context.ApplicationContext;
import menu.MenuGroup;
import menu.MenuItem;
import myapp.dao.BoardDao;
import myapp.dao.ProjectDao;
import myapp.dao.UserDao;
import myapp.dao.stub.BoardDaoStub;
import myapp.dao.stub.ProjectDaoStub;
import myapp.dao.stub.UserDaoStub;

public class InitApplicationListener implements ApplicationListener {

  UserDao userDao;
  BoardDao boardDao;
  ProjectDao projectDao;

  @Override
  public void onStart(ApplicationContext ctx) throws Exception {

    String host = (String) ctx.getAttribute("host");
    int port = (int) ctx.getAttribute("port");

    userDao = new UserDaoStub(host, port, "users");
    boardDao = new BoardDaoStub(host, port, "boards");
    projectDao = new ProjectDaoStub(host, port, "projects");

    MenuGroup mainMenu = ctx.getMainMenu();

    MenuGroup userMenu = new MenuGroup("회원");
    userMenu.add(new MenuItem("등록", new UserAddCommand(userDao)));
    userMenu.add(new MenuItem("목록", new UserListCommand(userDao)));
    userMenu.add(new MenuItem("조회", new UserViewCommand(userDao)));
    userMenu.add(new MenuItem("변경", new UserUpdateCommand(userDao)));
    userMenu.add(new MenuItem("삭제", new UserDeleteCommand(userDao)));
    mainMenu.add(userMenu);

    MenuGroup projectMenu = new MenuGroup("프로젝트");
    ProjectMemberHandler memberHandler = new ProjectMemberHandler(userDao);
    projectMenu.add(
        new MenuItem("등록", new ProjectAddCommand(projectDao, memberHandler)));
    projectMenu.add(new MenuItem("목록", new ProjectListCommand(projectDao)));
    projectMenu.add(new MenuItem("조회", new ProjectViewCommand(projectDao)));
    projectMenu.add(new MenuItem("변경", new ProjectUpdateCommand(projectDao, memberHandler)));
    projectMenu.add(new MenuItem("삭제", new ProjectDeleteCommand(projectDao)));
    mainMenu.add(projectMenu);

    MenuGroup boardMenu = new MenuGroup("게시판");
    boardMenu.add(new MenuItem("등록", new BoardAddCommand(boardDao)));
    boardMenu.add(new MenuItem("목록", new BoardListCommand(boardDao)));
    boardMenu.add(new MenuItem("조회", new BoardViewCommand(boardDao)));
    boardMenu.add(new MenuItem("변경", new BoardUpdateCommand(boardDao)));
    boardMenu.add(new MenuItem("삭제", new BoardDeleteCommand(boardDao)));
    mainMenu.add(boardMenu);

    mainMenu.add(new MenuItem("도움말", new HelpCommand()));
    mainMenu.add(new MenuItem("명령내역", new HistoryCommand()));

    mainMenu.setExitMenuTitle("종료");
  }
}
