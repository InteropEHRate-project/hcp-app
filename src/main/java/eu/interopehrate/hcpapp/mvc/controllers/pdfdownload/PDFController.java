package eu.interopehrate.hcpapp.mvc.controllers.pdfdownload;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.*;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.PrescriptionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;

@Controller
@RequestMapping("/reports")
public class PDFController {
    @Value("${server.port}")
    private int port;
    private final ServletContext servletContext;
    private final TemplateEngine templateEngine;
    private final PrescriptionService prescriptionService;
    private final VitalSignsService vitalSignsService;
    private final CurrentDiseaseService currentDiseaseService;
    private final AllergyService allergyService;
    private final DiagnosticConclusionService diagnosticConclusionService;
    private final HospitalDischargeReportService hospitalDischargeReportService;

    public PDFController(ServletContext servletContext, TemplateEngine templateEngine, PrescriptionService prescriptionService, VitalSignsService vitalSignsService,
                         CurrentDiseaseService currentDiseaseService, AllergyService allergyService, DiagnosticConclusionService diagnosticConclusionService, HospitalDischargeReportService hospitalDischargeReportService) {
        this.servletContext = servletContext;
        this.templateEngine = templateEngine;
        this.prescriptionService = prescriptionService;
        this.vitalSignsService = vitalSignsService;
        this.currentDiseaseService = currentDiseaseService;
        this.allergyService = allergyService;
        this.diagnosticConclusionService = diagnosticConclusionService;
        this.hospitalDischargeReportService = hospitalDischargeReportService;
    }

//    @GetMapping
//    @RequestMapping("/view-outpatient-report-page")
//    public String getPage(Model model) {
//        model.addAttribute("listPrescriptions", this.prescriptionService.getPrescriptionRepository().findAll());
//        model.addAttribute("vitalSignsUpload", this.vitalSignsService.vitalSignsUpload());
//        return TemplateNames.CURRENT_PATIENT_OUTPATIENT_REPORT_DOCUMENT;
//    }

    @GetMapping
    @RequestMapping("/outpatient-report-pdf")
    public ResponseEntity<?> outpatientReport(HttpServletRequest request, HttpServletResponse response) {
        WebContext context = new WebContext(request, response, this.servletContext);
        context.setVariable("listPrescriptions", this.prescriptionService.getPrescriptionRepository().findAll());
        context.setVariable("vitalSignsUpload", this.vitalSignsService.vitalSignsUpload());
        context.setVariable("listCurrentDiseases", this.currentDiseaseService.listNewCurrentDiseases());
        context.setVariable("listAllergies", this.allergyService.listOfNewAllergies());
        context.setVariable("listConclusionTreatment", this.diagnosticConclusionService.getNewConclusion());
        return this.getPDF(TemplateNames.CURRENT_PATIENT_OUTPATIENT_REPORT_DOCUMENT, context, "Outpatient-report.pdf");
    }

    @GetMapping
    @RequestMapping("/hospital-discharge-report-pdf")
    public ResponseEntity<?> hospitalDischargeReport(HttpServletRequest request, HttpServletResponse response) {
        WebContext context = new WebContext(request, response, this.servletContext);
        context.setVariable("listPrescriptions", this.prescriptionService.getPrescriptionRepository().findAll());
        context.setVariable("vitalSignsUpload", this.vitalSignsService.vitalSignsUpload());
        context.setVariable("listCurrentDiseases", this.currentDiseaseService.listNewCurrentDiseases());
        context.setVariable("listAllergies", this.allergyService.listOfNewAllergies());
        context.setVariable("listConclusionTreatment", this.diagnosticConclusionService.getNewConclusion());
        context.setVariable("hospitalDischargeReport", this.hospitalDischargeReportService.hospitalDischargeReportCommand());
        return this.getPDF(TemplateNames.CURRENT_PATIENT_HOSPITAL_DISCHARGE_REPORT_DOCUMENT, context, "Hospital-discharge-report.pdf");
    }

    public ResponseEntity<?> getPDF(String template, WebContext context, String fileName) {
        /* Create HTML using Thymeleaf template Engine */
        String reportHtml = templateEngine.process(template, context);

        /* Setup Source and target I/O streams */
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:" + this.port);
        /* Call convert method */
        HtmlConverter.convertToPdf(reportHtml, target, converterProperties);

        /* extract output as bytes */
        byte[] bytes = target.toByteArray();

        /* Send the response as downloadable PDF */
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
    }

    public byte[] retrievePdfAsBytes(HttpServletRequest request, HttpServletResponse response) {
        WebContext context = new WebContext(request, response, this.servletContext);
        context.setVariable("listPrescriptions", this.prescriptionService.getPrescriptionRepository().findAll());
        context.setVariable("vitalSignsUpload", this.vitalSignsService.vitalSignsUpload());
        context.setVariable("listCurrentDiseases", this.currentDiseaseService.listNewCurrentDiseases());
        context.setVariable("listAllergies", this.allergyService.listOfNewAllergies());
        context.setVariable("listConclusionTreatment", this.diagnosticConclusionService.getNewConclusion());
        context.setVariable("hospitalDischargeReport", this.hospitalDischargeReportService.hospitalDischargeReportCommand());

        /* Create HTML using Thymeleaf template Engine */
        String reportHtml = templateEngine.process(TemplateNames.CURRENT_PATIENT_HOSPITAL_DISCHARGE_REPORT_DOCUMENT, context);

        /* Setup Source and target I/O streams */
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:" + this.port);
        /* Call convert method */
        HtmlConverter.convertToPdf(reportHtml, target, converterProperties);

        /* extract output as bytes */
        return target.toByteArray();
    }
}