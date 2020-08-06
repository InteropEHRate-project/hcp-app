package eu.interopehrate.hcpapp.hapifhir;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class HapiTests {

    @Test
    void labResultsReferenceRangeParser() {
        String range;
        String[] cases = new String[4];
        cases[0] = "60 - 99 (Criteri interpretativi ADA 2014 $ 60 - 99 glicemia a\n\t\t\t\t\t\tdigiuno normale$ 100 - 125 glicemia a digiuno alterata$ >= 126\n\t\t\t\t\t\tindicativa diabete mellito,$ 2 riscontri)";
        cases[1] = "0,29 - 0,48";
        cases[2] = "< 100";
        cases[3] = "< 0,5 (Criteri interpretativi$ <0,5: basso rischio sepsi grave\n\t\t\t\t\t\te/o shock$ settico$ >2,0: alto rischio sepsi grave e/o shock$\n\t\t\t\t\t\tsettico)";
        for (int i = 0; i < 4; i++) {
            range = cases[i];
            range = range.replaceAll(",", ".");
            double upper;
            double lower;
            if (range.startsWith("<")) {
                range = range.substring(range.indexOf("<") + 2);
                if (range.contains(" ")) {
                    range = range.substring(0, range.indexOf(" "));
                }
                upper = Double.parseDouble(range);
                lower = 0;
            } else if (range.contains("(")) {
                range = range.substring(0, range.indexOf("(") - 1);
                lower = Double.parseDouble(range.substring(0, range.indexOf(" ")));
                upper = Double.parseDouble(range.substring(range.indexOf("-") + 2));
            } else {
                lower = Double.parseDouble(range.substring(0, range.indexOf(" ")));
                upper = Double.parseDouble(range.substring(range.indexOf("-") + 2));
            }
            switch (i) {
                case 0:
                    Assert.assertEquals(60, lower, 0.001);
                    Assert.assertEquals(99, upper, 0.001);
                    break;
                case 1:
                    Assert.assertEquals(0.29, lower, 0.001);
                    Assert.assertEquals(0.48, upper, 0.001);
                    break;
                case 2:
                    Assert.assertEquals(0, lower, 0.001);
                    Assert.assertEquals(100, upper, 0.001);
                    break;
                default:
                    Assert.assertEquals(0, lower, 0.001);
                    Assert.assertEquals(0.5, upper, 0.001);
                    break;
            }
        }
    }
}
