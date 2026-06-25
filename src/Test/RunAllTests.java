package Test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CentreSanteTest.class,
        RouteTest.class,
        CarteTest.class,
        MyPanelTest.class,
        ExporteCSVTest.class,
        NewSecteurWindowTest.class,
        NewTourneeWindowTest.class
})
public class RunAllTests {
}