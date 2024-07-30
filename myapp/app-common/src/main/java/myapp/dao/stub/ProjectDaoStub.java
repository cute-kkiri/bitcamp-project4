package myapp.dao.stub;

import myapp.dao.ProjectDao;
import myapp.vo.Project;
import net.ResponseStatus;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ProjectDaoStub implements ProjectDao {

  private String host;
  private int port;
  private String dataName;

  public ProjectDaoStub(String host, int port, String dataName)
      throws Exception {
    this.host = host;
    this.port = port;
    this.dataName = dataName;
  }

  @Override
  public boolean insert(Project project) throws Exception {
    try (Socket socket = new Socket(host, port);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

      out.writeUTF(dataName);
      out.writeUTF("insert");
      out.writeObject(project);
      out.flush();

      if (in.readUTF().equals(ResponseStatus.SUCCESS)) {
        return true;
      }

      return false;
    }
  }

  @Override
  public List<Project> list() throws Exception {
    try (Socket socket = new Socket(host, port);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

      out.writeUTF(dataName);
      out.writeUTF("list");
      out.flush();

      if (in.readUTF().equals(ResponseStatus.SUCCESS)) {
        return (List<Project>) in.readObject();
      }

      return null;
    }
  }

  @Override
  public Project findBy(int no) throws Exception {
    try (Socket socket = new Socket(host, port);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

      out.writeUTF(dataName);
      out.writeUTF("get");
      out.writeInt(no);
      out.flush();

      if (in.readUTF().equals(ResponseStatus.SUCCESS)) {
        return (Project) in.readObject();
      }

      return null;
    }
  }

  @Override
  public boolean update(Project project) throws Exception {
    try (Socket socket = new Socket(host, port);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

      out.writeUTF(dataName);
      out.writeUTF("update");
      out.writeObject(project);
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
