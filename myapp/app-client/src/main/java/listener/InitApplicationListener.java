package listener;

import command.HelpCommand;
import command.HistoryCommand;
import command.user.*;
import context.ApplicationContext;
import menu.MenuGroup;
import menu.MenuItem;
import myapp.dao.UserDao;
import myapp.dao.stub.UserDaoStub;

public class InitApplicationListener implements ApplicationListener {

  UserDao userDao;

  @Override
  public void onStart(ApplicationContext ctx) throws Exception {

    String host = (String) ctx.getAttribute("host");
    int port = (int) ctx.getAttribute("port");

    userDao = new UserDaoStub(host, port, "users");

    MenuGroup mainMenu = ctx.getMainMenu();

    new UserAddCommand(userDao).execute("Bingo");

    ctx.getMainMenu().setExitMenuTitle("종료");
  }
}
