package myapp.dao;

import myapp.vo.Project;

import java.util.List;

public interface ProjectDao {

  boolean insert(Project project) throws Exception;

  List<Project> list() throws Exception;

  Project findBy(int no) throws Exception;

  boolean update(Project project) throws Exception;

  boolean delete(int no) throws Exception;
}
