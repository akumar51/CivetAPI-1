package org.diceresearch.civetWebService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.dice_research.opal.civet.Civet;
import org.diceresearch.civetWebService.metrics.AvailabilityOfLicensesMetric;
import org.diceresearch.civetWebService.metrics.DataFormatMetric;
import org.diceresearch.civetWebService.metrics.DateFormatMetric;
import org.diceresearch.civetWebService.metrics.DescriptionMetric;
import org.diceresearch.civetWebService.metrics.ProviderIdentityMetric;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import net.minidev.json.JSONObject;

/**
 * This is a spring boot rest controller for reading RDF model and datasetUri
 * sent from postman and the returned result will be evaluated metric star.
 * 
 * @author Aamir Mohammed
 * 
 */

@RestController
public class MetricsController {

	public JSONObject readModelFile(MultipartFile file, String dataSet) throws IOException {

		/* Creating a Default model to Load the turtle file */
		Model model = ModelFactory.createDefaultModel();

		/* Reading the turtle file */
		model.read(new ByteArrayInputStream(file.getBytes()), null, "TTL");

		/* Reading the datasetUri */
		String datasetUri = new String(dataSet.getBytes(), StandardCharsets.UTF_8);

		/* If existing measurements should be removed */
		Civet civet = new Civet();
		civet.setRemoveMeasurements(true);

		/* If it should be logged, if a measurement could not be computed */
		civet.setLogNotComputed(true);

		/* Returns model and datasetUri as JSON Object */
		JSONObject object = new JSONObject();
		object.put("model", model);
		object.put("datasetUri", datasetUri);
		return object;

	}

	@PostMapping("/uploadFile/DateFormat")
	public int uploadFileDateFormat(@RequestParam("file") MultipartFile file, @RequestParam String dataSet)
			throws Exception {

		MetricsController controller = new MetricsController();
		JSONObject result = controller.readModelFile(file, dataSet);

		/* Compute model and datasetUri */
		DateFormatMetric metric = new DateFormatMetric();
		return metric.compute((Model) result.get("model"), (String) result.get("datasetUri"));
	}

	@PostMapping("/uploadFile/Description")
	public int uploadFileDescription(@RequestParam("file") MultipartFile file, @RequestParam String dataSet)
			throws Exception {

		MetricsController controller = new MetricsController();
		JSONObject result = controller.readModelFile(file, dataSet);

		/* Compute model and datasetUri */
		DescriptionMetric metric = new DescriptionMetric();
		return metric.compute((Model) result.get("model"), (String) result.get("datasetUri"));
	}

	@PostMapping("/uploadFile/ProviderIdentity")
	public int uploadFileProviderIdentity(@RequestParam("file") MultipartFile file, @RequestParam String dataSet)
			throws Exception {

		MetricsController controller = new MetricsController();
		JSONObject result = controller.readModelFile(file, dataSet);

		/* Compute model and datasetUri */
		ProviderIdentityMetric metric = new ProviderIdentityMetric();
		return metric.compute((Model) result.get("model"), (String) result.get("datasetUri"));
	}

	@PostMapping("/uploadFile/Licenses")
	public int uploadFileLicensesMetric(@RequestParam("file") MultipartFile file, @RequestParam String dataSet)
			throws Exception {

		MetricsController controller = new MetricsController();
		JSONObject result = controller.readModelFile(file, dataSet);

		/* Compute model and datasetUri */
		AvailabilityOfLicensesMetric metric = new AvailabilityOfLicensesMetric();
		return metric.compute((Model) result.get("model"), (String) result.get("datasetUri"));
	}

	@PostMapping("/uploadFile/DataFormat")
	public int uploadFileDataFormatMetric(@RequestParam("file") MultipartFile file, @RequestParam String dataSet)
			throws Exception {

		MetricsController controller = new MetricsController();
		JSONObject result = controller.readModelFile(file, dataSet);

		/* Compute model and datasetUri */
		DataFormatMetric metric = new DataFormatMetric();
		return metric.compute((Model) result.get("model"), (String) result.get("datasetUri"));
	}

}
