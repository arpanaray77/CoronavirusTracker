package com.covidapp.coronavirustracker.services;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class CoronavirusDataService {
	
	public static String VIRUS_DATA_URL="https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";
	
	@PostConstruct
	@Scheduled(cron="******") //scheduled to run revery second
	public void fetchVirusData() throws IOException, InterruptedException
	{
	  HttpClient client=HttpClient.newHttpClient();
	  HttpRequest request=HttpRequest.newBuilder()
			  			.uri(URI.create(VIRUS_DATA_URL))
			  			.build();
	  
	  HttpResponse<String> httpresponse= client.send(request,HttpResponse.BodyHandlers.ofString());
	 // System.out.println(httpresponse.body());
	  
	  //getting csv file from string
	   StringReader csvBodyHeader = new StringReader(httpresponse.body());
	  Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyHeader);
	  for (CSVRecord record : records) {
	      String state = record.get("Province/State");
	      System.out.println(state);
//	      String customerNo = record.get("CustomerNo");
//	      String nam = record.get("Name");
	  }
	          
	}

}
