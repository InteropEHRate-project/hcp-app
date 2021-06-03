package eu.interopehrate.hcpapp.mvc.controllers.pdfdownload;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.CurrentDiseaseService;
import eu.interopehrate.hcpapp.services.currentpatient.HospitalDischargeReportService;
import eu.interopehrate.hcpapp.services.currentpatient.VitalSignsService;
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
    private final HospitalDischargeReportService hospitalDischargeReportService;

    public PDFController(ServletContext servletContext, TemplateEngine templateEngine, PrescriptionService prescriptionService, VitalSignsService vitalSignsService,
                         CurrentDiseaseService currentDiseaseService, HospitalDischargeReportService hospitalDischargeReportService) {
        this.servletContext = servletContext;
        this.templateEngine = templateEngine;
        this.prescriptionService = prescriptionService;
        this.vitalSignsService = vitalSignsService;
        this.currentDiseaseService = currentDiseaseService;
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

        return this.getPDF(TemplateNames.CURRENT_PATIENT_OUTPATIENT_REPORT_DOCUMENT, context, "Outpatient-report.pdf");
    }

    @GetMapping
    @RequestMapping("/hospital-discharge-report-pdf")
    public ResponseEntity<?> hospitalDischargeReport(HttpServletRequest request, HttpServletResponse response) {
        WebContext context = new WebContext(request, response, this.servletContext);
        context.setVariable("listPrescriptions", this.prescriptionService.getPrescriptionRepository().findAll());
        context.setVariable("vitalSignsUpload", this.vitalSignsService.vitalSignsUpload());
        context.setVariable("listCurrentDiseases", this.currentDiseaseService.listNewCurrentDiseases());
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
}