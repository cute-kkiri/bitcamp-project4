package myapp.dao;

import myapp.vo.User;

import java.util.List;

public interface UserDao {

  boolean insert(User user) throws Exception;

  List<User> list() throws Exception;

  User findBy(int no) throws Exception;

  boolean update(User user) throws Exception;

  boolean delete(int no) throws Exception;
}
