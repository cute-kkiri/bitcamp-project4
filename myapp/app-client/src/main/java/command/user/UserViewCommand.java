package command.user;

import command.Command;
import myapp.dao.UserDao;
import myapp.vo.User;
import util.Prompt;

public class UserViewCommand implements Command {

  private UserDao userDao;

  public UserViewCommand(UserDao userDao) {
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

      System.out.printf("이름: %s\n", user.getName());
      System.out.printf("이메일: %s\n", user.getEmail());
      System.out.printf("연락처: %s\n", user.getTel());

    } catch (Exception e) {
      System.out.println("조회 중 오류 발생!");
    }
  }
}
