package command.user;


import command.Command;
import myapp.dao.UserDao;
import myapp.vo.User;
import util.Prompt;

public class UserAddCommand implements Command {

  private UserDao userDao;

  public UserAddCommand(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public void execute(String menuName) {
    System.out.printf("[%s]\n", menuName);
    try {
      User user = new User();
      user.setName(Prompt.input("이름?"));

      userDao.insert(user);
    } catch (Exception e) {
      System.out.println("등록 중 오류 발생!");
    }
  }
}
