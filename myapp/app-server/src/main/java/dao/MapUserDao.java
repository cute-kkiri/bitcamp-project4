package dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myapp.dao.UserDao;
import myapp.vo.User;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MapUserDao implements UserDao {

  private static final String DEFAULT_DATANAME = "users";
  private int seqNo;
  private Map<Integer, User> userMap = new HashMap<>();
  private List<Integer> userNoList = new ArrayList<>();
  private String path;
  private String dataName;

  public MapUserDao(String path) {
    this(path, DEFAULT_DATANAME);
  }

  public MapUserDao(String path, String dataName) {
    this.path = path;
    this.dataName = dataName;

    try (XSSFWorkbook workbook = new XSSFWorkbook(path)) {
      XSSFSheet sheet = workbook.getSheet(dataName);

      for (int i = 1; i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        try {
          User user = new User();
          user.setNo(Integer.parseInt(row.getCell(0).getStringCellValue()));
          user.setName(row.getCell(1).getStringCellValue());

          userMap.put(user.getNo(), user);
          userNoList.add(user.getNo());

        } catch (Exception e) {
          System.out.printf("%s 번 회원의 데이터 형식이 맞지 않습니다.\n", row.getCell(0).getStringCellValue());
        }
      }

      seqNo = userNoList.getLast();

    } catch (Exception e) {
      System.out.println("회원 데이터 로딩 중 오류 발생!");
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
      String[] cellHeaders = {"no", "name", "email", "password", "tel"};
      Row headerRow = sheet.createRow(0);
      for (int i = 0; i < cellHeaders.length; i++) {
        headerRow.createCell(i).setCellValue(cellHeaders[i]);
      }

      // 데이터 저장
      int rowNo = 1;
      for (Integer userNo : userNoList) {
        User user = userMap.get(userNo);
        Row dataRow = sheet.createRow(rowNo++);
        dataRow.createCell(0).setCellValue(String.valueOf(user.getNo()));
        dataRow.createCell(1).setCellValue(user.getName());
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
  public boolean insert(User user) throws Exception {
    user.setNo(++seqNo);
    userMap.put(user.getNo(), user);
    userNoList.add(user.getNo());
    return true;
  }

  @Override
  public List<User> list() throws Exception {
    ArrayList<User> users = new ArrayList<>();
    for (Integer userNo : userNoList) {
      users.add(userMap.get(userNo));
    }
    return users;
  }

  @Override
  public User findBy(int no) throws Exception {
    return userMap.get(no);
  }

  @Override
  public boolean update(User user) throws Exception {
    if (!userMap.containsKey(user.getNo())) {
      return false;
    }

    userMap.put(user.getNo(), user);
    return true;
  }

  @Override
  public boolean delete(int no) throws Exception {
    if (userMap.remove(no) == null) {
      return false;
    }
    userNoList.remove(Integer.valueOf(no));
    return true;
  }
}
