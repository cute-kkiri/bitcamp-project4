package myapp.dao;

import myapp.vo.Board;

import java.util.List;

public interface BoardDao {

  boolean insert(Board board) throws Exception;

  List<Board> list() throws Exception;

  Board findBy(int no) throws Exception;

  boolean update(Board board) throws Exception;

  boolean delete(int no) throws Exception;
}
