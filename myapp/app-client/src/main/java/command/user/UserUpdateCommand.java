package command.user;


import command.Command;
import myapp.dao.UserDao;
import myapp.vo.User;
import util.Prompt;

public class UserUpdateCommand implements Command {

  private UserDao userDao;

  public UserUpdateCommand(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public void execute(String menuName) {
    System.out.printf("[%s]\n", menuName);
    int userNo = Prompt.inputInt("회원번호?");

    try {
      User user = userDao.findBy(userNo);
      if (user == null) {
        System.out.println("없는 회원입니다.");
        return;
      }

      user.setName(Prompt.input("이름(%s)?", user.getName()));

      userDao.update(user);
      System.out.println("변경 했습니다.");

    } catch (Exception e) {
      System.out.println("변경 중 오류 발생!");
    }
  }

}
