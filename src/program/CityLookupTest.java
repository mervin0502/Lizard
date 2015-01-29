package program;
/* CityLookupTest.java */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import source.com.maxmind.geoip.Location;
import source.com.maxmind.geoip.LookupService;
import source.com.maxmind.geoip.regionName;
import source.com.maxmind.geoip.timeZone;

/* sample of how to use the GeoIP Java API with GeoIP City database */
/* Usage: java CityLookupTest 64.4.4.4 */

class CityLookupTest {
			
    public static void main(String[] args) throws Exception {
	
    	try {
		
	    LookupService cl = new LookupService("src/program/GeoLiteCity.dat",
					LookupService.GEOIP_MEMORY_CACHE );
        Location l1 = cl.getLocation("202.118.0.0");
        Location l2 = cl.getLocation("222.36.47.115");
        //System.out.println("lin");
        
	    System.out.println("countryCode: " + l2.countryCode +
                               "\n countryName: " + l2.countryName +
                               "\n region: " + l2.region +
                               "\n regionName: " + regionName.regionNameByCode(l2.countryCode, l2.region) +
                               "\n city: " + l2.city +
                               "\n postalCode: " + l2.postalCode +
                               "\n latitude: " + l2.latitude +
                               "\n longitude: " + l2.longitude +
                               "\n distance: " + l2.distance(l1) +
                               "\n distance: " + l1.distance(l2) + 
 			       "\n metro code: " + l2.metro_code +
 			       "\n area code: " + l2.area_code +
                               "\n timezone: " + timeZone.timeZoneByCountryAndRegion(l2.countryCode, l2.region));

	    cl.close();
	}
	catch (IOException e) {
	    System.out.println("IO Exception");
	}
    }
}
