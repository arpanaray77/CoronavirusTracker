package com.covidapp.coronavirustracker.services;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import com.covidapp.coronavirustracker.models.LocationData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class CoronavirusDataService {
	
	public static String VIRUS_DATA_URL="https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";
	private List<LocationData> allStats =new ArrayList<>();
	
	public List<LocationData> getAllStats() {
		return allStats;
	}

	public void setAllStats(List<LocationData> allStats) {
		this.allStats = allStats;
	}



	@PostConstruct
	@Scheduled(cron="* * * * * *") //scheduled to run every second
	public void fetchVirusData() throws IOException, InterruptedException
	{
	  List<LocationData> newStats =new ArrayList<>();
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
		  
		  LocationData data=new LocationData();
		  data.setState(record.get("Province/State"));
		  data.setCountry(record.get("Country/Region"));
		  data.setLatestTotalCases(Integer.parseInt(record.get(record.size()-1)));
	      //System.out.println(data.getCountry()+"  "+data.getLatestTotalCases());
          newStats.add(data);
	  }
	  this.allStats = newStats;
	          
	}
}
