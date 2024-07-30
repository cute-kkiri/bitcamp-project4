package dao.skel;

import myapp.dao.BoardDao;
import myapp.vo.Board;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import static net.ResponseStatus.*;

public class BoardDaoSkel {

  private BoardDao boardDao;

  public BoardDaoSkel(BoardDao boardDao) {
    this.boardDao = boardDao;
  }

  public void service(ObjectInputStream in, ObjectOutputStream out) throws Exception {
    String command = in.readUTF();

    Board board = null;
    int no = 0;

    switch (command) {
      case "insert":
        board = (Board) in.readObject();
        boardDao.insert(board);
        out.writeUTF(SUCCESS);
        break;
      case "list":
        List<Board> list = boardDao.list();
        out.writeUTF(SUCCESS);
        out.writeObject(list);
        break;
      case "get":
        no = in.readInt();
        board = boardDao.findBy(no);
        if (board != null) {
          out.writeUTF(SUCCESS);
          out.writeObject(board);
        } else {
          out.writeUTF(FAILURE);
        }
        break;
      case "update":
        board = (Board) in.readObject();
        if (boardDao.update(board)) {
          out.writeUTF(SUCCESS);
        } else {
          out.writeUTF(FAILURE);
        }
        break;
      case "delete":
        no = in.readInt();
        if (boardDao.delete(no)) {
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
