package com.nbrown.techtask.repository;

import java.util.List;
import org.jsoup.nodes.Element;

public interface FormulaOneRepository {

  List<Element> getStandingsData(String sourceUrl, String tableClass, String nameClass,
      int count);
}
