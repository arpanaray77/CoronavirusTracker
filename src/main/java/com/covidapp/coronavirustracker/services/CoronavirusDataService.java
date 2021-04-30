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
	public static String COUNTRY_CAPITAL="https://raw.githubusercontent.com/icyrockcom/country-capitals/master/data/country-list.csv";
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
	  
	  HttpRequest request2=HttpRequest.newBuilder()
	  			.uri(URI.create(COUNTRY_CAPITAL))
	  			.build();

	HttpResponse<String> httpresponse2= client.send(request2,HttpResponse.BodyHandlers.ofString());
	//System.out.println(httpresponse2.body());

	  
	  //getting csv file from string
	   StringReader csvBodyHeader = new StringReader(httpresponse.body());
	   StringReader countryName = new StringReader(httpresponse2.body());
	   
	  Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyHeader);
	  Iterable<CSVRecord> countries = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(countryName);
	  
	  for (CSVRecord record : records) {
		  
		  LocationData data=new LocationData();
		  if(record.get("Province/State").length()>0) 
		      data.setState(record.get("Province/State"));
		  else
		  {
			  String countryname=record.get("Country/Region");
			  boolean flag=false;
			  for (CSVRecord country: countries)
			  {
				  if(country.get("country").equals(countryname))
				  {
					  flag =true;
					 // System.out.println(country.get("country") +" "+country.get("capital"));
					  data.setState(country.get("capital")); 
					  break;
				  }
			  }
			  if(!flag)
				  data.setState("Capital");
		  }
		  data.setCountry(record.get("Country/Region"));
		  data.setLatestTotalCases(Integer.parseInt(record.get(record.size()-1)));
		  data.setDiffrenceFromPrevDay(Math.abs(Integer.parseInt(record.get(record.size()-2))-Integer.parseInt(record.get(record.size()-1))));
          newStats.add(data);
	  }
	  this.allStats = newStats;
	          
	}
}
