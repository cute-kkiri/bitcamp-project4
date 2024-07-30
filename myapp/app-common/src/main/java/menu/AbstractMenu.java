package menu;

import java.util.Objects;

public abstract class AbstractMenu implements Menu {

  protected String title;

  public AbstractMenu(String title) {
    this.title = title;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AbstractMenu menuItem)) {
      return false;
    }
    return Objects.equals(title, menuItem.title);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(title);
  }

  @Override
  public String getTitle() {
    return title;
  }
}
