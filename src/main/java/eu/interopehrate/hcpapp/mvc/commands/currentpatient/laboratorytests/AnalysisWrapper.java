package eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class AnalysisWrapper {
    private String analysis = "-";
    private String analysisTranslated = "-";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnalysisWrapper that = (AnalysisWrapper) o;
        return Objects.equals(analysis, that.analysis) && Objects.equals(analysisTranslated, that.analysisTranslated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(analysis, analysisTranslated);
    }
}
