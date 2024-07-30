package dao.skel;

import myapp.dao.UserDao;
import myapp.vo.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import static net.ResponseStatus.*;

public class UserDaoSkel {

  private UserDao userDao;

  public UserDaoSkel(UserDao userDao) {
    this.userDao = userDao;
  }

  public void service(ObjectInputStream in, ObjectOutputStream out) throws Exception {
    String command = in.readUTF();

    User user = null;
    int no = 0;

    switch (command) {
      case "insert":
        user = (User) in.readObject();
        userDao.insert(user);
        out.writeUTF(SUCCESS);
        break;
      case "list":
        List<User> list = userDao.list();
        out.writeUTF(SUCCESS);
        out.writeObject(list);
        break;
      case "get":
        no = in.readInt();
        user = userDao.findBy(no);
        if (user != null) {
          out.writeUTF(SUCCESS);
          out.writeObject(user);
        } else {
          out.writeUTF(FAILURE);
        }
        break;
      case "update":
        user = (User) in.readObject();
        if (userDao.update(user)) {
          out.writeUTF(SUCCESS);
        } else {
          out.writeUTF(FAILURE);
        }
        break;
      case "delete":
        no = in.readInt();
        if (userDao.delete(no)) {
          out.writeUTF(SUCCESS);
        } else {
          out.writeUTF(FAILURE);
        }
        break;
      default:
        out.writeUTF(ERROR);
        out.writeUTF("무효한 명령입니다.");
    }

    out.flush();
  }

}
