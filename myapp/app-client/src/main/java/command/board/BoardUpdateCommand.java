package command.board;


import command.Command;
import myapp.dao.BoardDao;
import myapp.vo.Board;
import util.Prompt;

public class BoardUpdateCommand implements Command {

  private BoardDao boardDao;

  public BoardUpdateCommand(BoardDao boardDao) {
    this.boardDao = boardDao;
  }

  @Override
  public void execute(String menuName) {
    System.out.printf("[%s]\n", menuName);
    int boardNo = Prompt.inputInt("게시글 번호?");

    try {
      Board board = boardDao.findBy(boardNo);
      if (board == null) {
        System.out.println("없는 게시글입니다.");
        return;
      }

      board.setViewCount(board.getViewCount() + 1);
      board.setTitle(Prompt.input("제목(%s)?", board.getTitle()));
      board.setContent(Prompt.input("내용(%s)?", board.getContent()));

      boardDao.update(board);
      System.out.println("변경 했습니다.");

    } catch (Exception e) {
      System.out.println("변경 중 오류 발생!");
    }
  }
}
