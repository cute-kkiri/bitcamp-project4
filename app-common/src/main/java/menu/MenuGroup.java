package menu;

import util.Prompt;

import java.util.ArrayList;
import java.util.Stack;

public class MenuGroup extends AbstractMenu {

  private MenuGroup parent;
  private ArrayList<Menu> children = new ArrayList<>();
  private String exitMenuTitle = "이전";

  public MenuGroup(String title) {
    super(title);
  }

  @Override
  public void execute() {

    String menuPath = getMenuPath();

    printMenus();

    while (true) {
      String command = Prompt.input("%s>", menuPath);
      if (command.equals("menu")) {
        printMenus();
        continue;
      } else if (command.equals("0")) { // 이전 메뉴 선택
        return;
      }

      try {
        int menuNo = Integer.parseInt(command);
        Menu menu = getMenu(menuNo - 1);
        if (menu == null) {
          System.out.println("유효한 메뉴 번호가 아닙니다.");
          continue;
        }

        menu.execute();

      } catch (NumberFormatException ex) {
        System.out.println("숫자로 메뉴 번호를 입력하세요.");
      }
    }
  }

  public void setExitMenuTitle(String title) {
    exitMenuTitle = title;
  }

  private void printMenus() {
    System.out.printf("[%s]\n", title);
    int i = 1;
    for (Menu menu : children) {
      System.out.printf("%d. %s\n", i++, menu.getTitle());
    }
    System.out.printf("0. %s\n", exitMenuTitle);
  }

  private String getMenuPath() {
    // 현재 메뉴그룹에서 상위 메뉴그룹으로 따라 올라가면서 메뉴이름을 스택에 담는다.
    Stack<String> menuPathStack = new Stack<>();
    MenuGroup menuGroup = this;
    while (menuGroup != null) {
      menuPathStack.push(menuGroup.title);
      menuGroup = menuGroup.parent;
    }

    // 스택에 담겨 있는 메뉴이름을 꺼내서 메뉴 경로를 만든다.
    StringBuilder strBuilder = new StringBuilder();
    while (!menuPathStack.isEmpty()) {
      if (strBuilder.length() > 0) {
        strBuilder.append("/");
      }
      strBuilder.append(menuPathStack.pop());
    }

    return strBuilder.toString();
  }

  private void setParent(MenuGroup parent) {
    this.parent = parent;
  }

  public void add(Menu child) {
    if (child instanceof MenuGroup) {
      ((MenuGroup) child).setParent(this);
    }
    children.add(child);
  }

  public void remove(Menu child) {
    children.remove(child);
  }

  public Menu getMenu(int index) {
    if (index < 0 || index >= children.size()) {
      return null;
    }
    return children.get(index);
  }

  public int countMenus() {
    return children.size();
  }
}
