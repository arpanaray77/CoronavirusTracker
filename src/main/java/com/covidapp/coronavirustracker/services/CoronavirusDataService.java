package com.covidapp.coronavirustracker.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;


@Service
public class CoronavirusDataService {
	
	public static String VIRUS_DATA_URL="https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";
	
	@PostConstruct
	public void fetchVirusData() throws IOException, InterruptedException
	{
	  HttpClient client=HttpClient.newHttpClient();
	  HttpRequest request=HttpRequest.newBuilder()
			  			.uri(URI.create(VIRUS_DATA_URL))
			  			.build();
	  
	  HttpResponse<String> httpresponse= client.send(request,HttpResponse.BodyHandlers.ofString());
	  System.out.println(httpresponse.body());
	}

}
