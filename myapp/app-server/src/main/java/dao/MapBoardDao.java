package dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myapp.dao.BoardDao;
import myapp.vo.Board;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MapBoardDao implements BoardDao {

  private static final String DEFAULT_DATANAME = "boards";
  private int seqNo;
  private Map<Integer, Board> boardMap = new HashMap<>();
  private List<Integer> boardNoList = new ArrayList<>();
  private String path;
  private String dataName;

  public MapBoardDao(String path) {
    this(path, DEFAULT_DATANAME);
  }

  public MapBoardDao(String path, String dataName) {
    this.path = path;
    this.dataName = dataName;

    try (XSSFWorkbook workbook = new XSSFWorkbook(path)) {
      XSSFSheet sheet = workbook.getSheet(dataName);

      for (int i = 1; i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        try {
          Board board = new Board();
          board.setNo(Integer.parseInt(row.getCell(0).getStringCellValue()));
          board.setTitle(row.getCell(1).getStringCellValue());
          board.setContent(row.getCell(2).getStringCellValue());

          SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          board.setCreatedDate(formatter.parse(row.getCell(3).getStringCellValue()));

          board.setViewCount(Integer.parseInt(row.getCell(4).getStringCellValue()));

          boardMap.put(board.getNo(), board);
          boardNoList.add(board.getNo());

        } catch (Exception e) {
          System.out.printf("%s 번 게시글의 데이터 형식이 맞지 않습니다.\n", row.getCell(0).getStringCellValue());
        }
      }

      seqNo = boardNoList.getLast();

    } catch (Exception e) {
      System.out.println("게시글 데이터 로딩 중 오류 발생!");
      e.printStackTrace();
    }
  }

  public void save() throws Exception {
    try (FileInputStream in = new FileInputStream(path);
        XSSFWorkbook workbook = new XSSFWorkbook(in)) {

      int sheetIndex = workbook.getSheetIndex(dataName);
      if (sheetIndex != -1) {
        workbook.removeSheetAt(sheetIndex);
      }

      XSSFSheet sheet = workbook.createSheet(dataName);

      // 셀 이름 출력
      String[] cellHeaders = {"no", "title", "content", "created_date", "view_count"};
      Row headerRow = sheet.createRow(0);
      for (int i = 0; i < cellHeaders.length; i++) {
        headerRow.createCell(i).setCellValue(cellHeaders[i]);
      }

      // 데이터 저장
      int rowNo = 1;
      for (Integer boardNo : boardNoList) {
        Board board = boardMap.get(boardNo);
        Row dataRow = sheet.createRow(rowNo++);
        dataRow.createCell(0).setCellValue(String.valueOf(board.getNo()));
        dataRow.createCell(1).setCellValue(board.getTitle());
        dataRow.createCell(2).setCellValue(board.getContent());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dataRow.createCell(3).setCellValue(formatter.format(board.getCreatedDate()));

        dataRow.createCell(4).setCellValue(String.valueOf(board.getViewCount()));
      }

      // 엑셀 파일로 데이터를 출력하기 전에
      // workbook을 위해 연결한 입력 스트림을 먼저 종료한다.
      in.close();

      try (FileOutputStream out = new FileOutputStream(path)) {
        workbook.write(out);
      }
    }
  }

  @Override
  public boolean insert(Board board) throws Exception {
    board.setNo(++seqNo);
    boardMap.put(board.getNo(), board);
    boardNoList.add(board.getNo());
    return true;
  }

  @Override
  public List<Board> list() throws Exception {
    ArrayList<Board> boards = new ArrayList<>();
    for (Integer boardNo : boardNoList) {
      boards.add(boardMap.get(boardNo));
    }
    return boards;
  }

  @Override
  public Board findBy(int no) throws Exception {
    return boardMap.get(no);
  }

  @Override
  public boolean update(Board board) throws Exception {
    if (!boardMap.containsKey(board.getNo())) {
      return false;
    }

    boardMap.put(board.getNo(), board);
    return true;
  }

  @Override
  public boolean delete(int no) throws Exception {
    if (boardMap.remove(no) == null) {
      return false;
    }
    boardNoList.remove(Integer.valueOf(no));
    return true;
  }
}
