package menu;

public class TestMenu {

  public static void main(String[] args) {
    MenuGroup root = new MenuGroup("메인");

    MenuGroup file = new MenuGroup("파일");
    root.add(file);

    MenuGroup edit = new MenuGroup("편집");
    root.add(edit);

    MenuItem help = new MenuItem("도움말");
    root.add(help);

    root.execute();

  }
}
