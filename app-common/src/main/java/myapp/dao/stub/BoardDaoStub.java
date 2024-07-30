package myapp.dao.stub;

import myapp.dao.BoardDao;
import myapp.vo.Board;
import net.ResponseStatus;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class BoardDaoStub implements BoardDao {

  private String host;
  private int port;
  private String dataName;

  public BoardDaoStub(String host, int port, String dataName)
      throws Exception {
    this.host = host;
    this.port = port;
    this.dataName = dataName;
  }

  @Override
  public boolean insert(Board board) throws Exception {
    try (Socket socket = new Socket(host, port);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

      out.writeUTF(dataName);
      out.writeUTF("insert");
      out.writeObject(board);
      out.flush();

      if (in.readUTF().equals(ResponseStatus.SUCCESS)) {
        return true;
      }

      return false;
    }
  }

  @Override
  public List<Board> list() throws Exception {
    try (Socket socket = new Socket(host, port);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

      out.writeUTF(dataName);
      out.writeUTF("list");
      out.flush();

      if (in.readUTF().equals(ResponseStatus.SUCCESS)) {
        return (List<Board>) in.readObject();
      }

      return null;
    }
  }

  @Override
  public Board findBy(int no) throws Exception {
    try (Socket socket = new Socket(host, port);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

      out.writeUTF(dataName);
      out.writeUTF("get");
      out.writeInt(no);
      out.flush();

      if (in.readUTF().equals(ResponseStatus.SUCCESS)) {
        return (Board) in.readObject();
      }

      return null;
    }
  }

  @Override
  public boolean update(Board board) throws Exception {
    try (Socket socket = new Socket(host, port);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

      out.writeUTF(dataName);
      out.writeUTF("update");
      out.writeObject(board);
      out.flush();

      if (in.readUTF().equals(ResponseStatus.SUCCESS)) {
        return true;
      }

      return false;
    }
  }

  @Override
  public boolean delete(int no) throws Exception {
    try (Socket socket = new Socket(host, port);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

      out.writeUTF(dataName);
      out.writeUTF("delete");
      out.writeInt(no);
      out.flush();

      if (in.readUTF().equals(ResponseStatus.SUCCESS)) {
        return true;
      }

      return false;
    }
  }
}
