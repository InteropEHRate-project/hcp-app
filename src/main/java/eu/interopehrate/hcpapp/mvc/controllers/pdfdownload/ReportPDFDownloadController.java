package eu.interopehrate.hcpapp.mvc.controllers.pdfdownload;

import eu.interopehrate.hcpapp.converters.entity.EntityToCommandPrescription;
import eu.interopehrate.hcpapp.converters.entity.EntityToCommandVitalSigns;
import eu.interopehrate.hcpapp.jpa.entities.PrescriptionEntity;
import eu.interopehrate.hcpapp.jpa.entities.VitalSignsEntity;
import eu.interopehrate.hcpapp.jpa.repositories.PrescriptionRepository;
import eu.interopehrate.hcpapp.jpa.repositories.VitalSignsRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionInfoCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.vitalsigns.VitalSignsInfoCommand;
import lombok.SneakyThrows;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@RestController
public class ReportPDFDownloadController {
	private final PrescriptionRepository prescriptionRepository;
	private final EntityToCommandPrescription entityToCommandPrescription;
	private final VitalSignsRepository vitalSignsRepository;
	private final EntityToCommandVitalSigns entityToCommandVitalSigns;

	public ReportPDFDownloadController(PrescriptionRepository prescriptionRepository, EntityToCommandPrescription entityToCommandPrescription,
									   VitalSignsRepository vitalSignsRepository, EntityToCommandVitalSigns entityToCommandVitalSigns) {
		this.prescriptionRepository = prescriptionRepository;
		this.entityToCommandPrescription = entityToCommandPrescription;
		this.vitalSignsRepository = vitalSignsRepository;
		this.entityToCommandVitalSigns = entityToCommandVitalSigns;
	}

	@GetMapping
	@RequestMapping("/download-report-pdf")
	public ResponseEntity<Object> downloadFile() throws IOException {
		generatePdfFromHtml(this.createPDFContent());
		String filename = System.getProperty("user.home") + File.separator + "Outpatient-report.pdf";
		File file = new File(filename);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		return ResponseEntity
				.ok()
				.headers(headers)
				.contentLength(file.length())
				.contentType(MediaType.parseMediaType("application/txt")).body(resource);
	}

	@SneakyThrows
	private static void generatePdfFromHtml(String html) {
		String outputFolder = System.getProperty("user.home") + File.separator + "Outpatient-report.pdf";
		OutputStream outputStream = new FileOutputStream(outputFolder);

		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocumentFromString(html);
		renderer.layout();
		renderer.createPDF(outputStream);

		outputStream.close();
	}

	private String createPDFContent() {
		StringBuilder content = new StringBuilder();
		content.append("<html>");
		content.append("\n");
		if (this.prescriptionRepository.findAll().size() > 0) {
			content.append("<h3>Prescription</h3>");
			content.append("\n");
		}
		int indexPrescription = 1;
		for (PrescriptionEntity p : this.prescriptionRepository.findAll()) {
			PrescriptionInfoCommand prescriptionInfoCommand = this.entityToCommandPrescription.convert(p);
			content.append(indexPrescription++ + ":");
			content.append("\n");
			content.append("<p>" + "drug name: " + prescriptionInfoCommand.getDrugName());
			content.append("</p>");
			content.append("\n");

			content.append("<p>" + "drug dosage: " + prescriptionInfoCommand.getDrugDosage());
			content.append("</p>");
			content.append("\n");

			content.append("<p>" + "timings: " + prescriptionInfoCommand.getTimings() + " times per day");
			content.append("</p>");
			content.append("\n");

			content.append("<p>" + "status: " + prescriptionInfoCommand.getStatus());
			content.append("</p>");
			content.append("\n");

			content.append("<p>" + "date of prescription: " + prescriptionInfoCommand.getDateOfPrescription());
			content.append("</p>");
			content.append("\n");

			content.append("<p>" + "author: " + prescriptionInfoCommand.getAuthor());
			content.append("</p>");
			content.append("\n");

			content.append("<p>" + "notes: " + prescriptionInfoCommand.getNotes());
			content.append("</p>");
			content.append("\n");

			content.append("<p>" + "start: " + prescriptionInfoCommand.getStart());
			content.append("</p>");
			content.append("\n");

			if (Objects.nonNull(prescriptionInfoCommand.getEnd())) {
				content.append("<p>" + "end: " + prescriptionInfoCommand.getEnd());
				content.append("</p>");
				content.append("\n");
			}
			content.append("<br></br>");
			content.append("\n");
		}

		if (this.vitalSignsRepository.findAll().size() > 0) {
			content.append("<h3>Vital Signs</h3>");
			content.append("\n");
		}
		int indexVitalSigns = 1;
		for (VitalSignsEntity p : this.vitalSignsRepository.findAll()) {
			VitalSignsInfoCommand vitalSignsInfoCommand = this.entityToCommandVitalSigns.convert(p);
			content.append(indexVitalSigns++ + ":");
			content.append("\n");
			content.append("<p>" + "analysis: " + vitalSignsInfoCommand.getAnalysisName());
			content.append("</p>");
			content.append("\n");

			content.append("<p>" + "value: " + vitalSignsInfoCommand.getVitalSignsInfoCommandSample().getCurrentValue());
			content.append("</p>");
			content.append("\n");

			content.append("<p>" + "Date and Time: " + vitalSignsInfoCommand.getVitalSignsInfoCommandSample().getLocalDateOfVitalSign().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
			content.append("</p>");
			content.append("\n");

			content.append("<br></br>");
			content.append("\n");
		}
		content.append("</html>");
		return content.toString();
	}
}
