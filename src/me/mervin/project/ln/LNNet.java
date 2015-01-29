package me.mervin.project.ln;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import me.mervin.core.Global.NumberType;
import me.mervin.module.extract.EdgeDeweigh;
import me.mervin.module.extract.ExtractIPv4ByLink;
import me.mervin.util.D;
import me.mervin.util.FileTool;
import source.com.maxmind.geoip.Location;
import source.com.maxmind.geoip.LookupService;
import source.com.maxmind.geoip.regionName;

public class LNNet {
	
	
	@SuppressWarnings("resource")
	public static void main (String[] args){ 
		
		
		new ExtractIPv4ByLink("./data/02.txt", "./data/res02.txt").script();
		new EdgeDeweigh("./data/res02.txt","./data/res02-reduce.txt", NumberType.LONG).script();
		
		String srcFile = "./data/res02-reduce.txt";
		
		
		//
		
		//LNNet ln = new LNNet();
		//ln.filter(srcFile);
	
	}
	
	
	public void filter(String srcFile){ 
		try {
			LookupService cl = new LookupService("src/program/GeoLiteCity.dat",LookupService.GEOIP_MEMORY_CACHE );
			
			
			BufferedReader reader = null;
			
			StringBuffer sb = null;
			
			reader = new BufferedReader(new FileReader(srcFile));
			String line = null;
			String[] lineArr = null;
			long l, r;
			Location lo = null;
			sb = new StringBuffer();
			while((line = reader.readLine()) != null){ 
				D.p(line);
				lineArr = line.split("\\s+");
				l = Long.parseLong(lineArr[0]);
				r = Long.parseLong(lineArr[1]);
				
				lo = cl.getLocation(l);
				try{
					if (lo.countryCode.equals("CN") && lo.region.equals("19")){ 
						sb.append(line).append("\r\n");
						continue;
					}
					
					
					lo = cl.getLocation(r);
					if (lo.countryCode.equals("CN") && lo.region.equals("19")){
						
						sb.append(line).append("\r\n");
						continue;
					}
				}catch(Exception e){ 
					D.f("not lookup this ip");
					
				}
				
				
				
			}	
			new FileTool().write(sb, "./data/ln.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
