package program;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import source.com.maxmind.geoip.Location;
import source.com.maxmind.geoip.LookupService;
import source.com.maxmind.geoip.regionName;


public class LiaoNing {
	
	public void Extrac(String ipFile) throws IOException{
		// Reading input by lines:
	    BufferedReader in = new BufferedReader(
	      new FileReader(ipFile));
	    String s;
	    //StringBuilder sb = new StringBuilder();
	    while((s = in.readLine())!= null)
	    {
	    	
	    	preProData(s);
	    	//sb.append(s + "\n");
	    }  
	    in.close();
	}
	
	public void preProData(String arg) throws IOException{
		boolean flag=true;
		
		PrintWriter out = new PrintWriter(
			      new BufferedWriter(new FileWriter("E:/Host-VM/0128/data01/liaoNing01.txt",true)));
		
		LookupService cl = new LookupService("src/program/GeoLiteCity.dat",
				LookupService.GEOIP_MEMORY_CACHE );
		
		String [] rowElem;
		rowElem=arg.split("\\s+");
		
		boolean isLiaoning=false;
		
		if(rowElem[0].equals("T"))
		{

			String region=null;
			//System.out.println(rowElem[2]);
			Location l1 = cl.getLocation(rowElem[2]);
			
				try {
					if(l1.countryCode!=null&&l1.region!=null)
					{
						region=regionName.regionNameByCode(l1.countryCode, l1.region);
						if(region.equals("Liaoning")){
							isLiaoning=true;
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				if(!isLiaoning||rowElem[6].equals("N")){
					flag=false;
				}
				else{
					for(int i=13;i<rowElem.length;i++){
						if(rowElem[i].equals("q"))
						{
							flag=false;
						}
						else{
							for(int j=0;j<rowElem[i].length();j++)
							{
								if(rowElem[i].charAt(j)==';')
								{				
									flag=false;
								}
							}
						}
					}
				}
		}
		else{
			flag=false;//do nothing!
		}
		
		//System.out.println(flag);
		
		if(flag==true){
			for(int i=13;i<rowElem.length;i++){
				rowElem[i]=rowElem[i].replaceAll(",.*,."," ");
			}
			
			out.print(rowElem[1]);
			out.print(" ");
			for(int x=13;x<rowElem.length;x++)
			{
				out.print(rowElem[x]);
			}
			out.print(rowElem[2]);
			out.print("\n");
			out.close();
		}	
	}

	public static void main(String[] args) throws IOException {
		LiaoNing obj=new LiaoNing();
		obj.Extrac("E:/Host-VM/0128/data01/source01.txt");
		System.out.println("end!");
		
	}
}
