package com.sweetrain.crawler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



class TVInforCrawler {
	/* ķė */
	Document doc;
	Map <String, String> channels_public = new HashMap<>(); 
	String ads_public_front = "https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=5100&key_depth2=";
	String ads_public_back = "&key_depth3=";
	Elements cmm_boxs = null;
	String cur_time = "";
	String url = "";	
	Elements cm_tab_info_box = null;
	String characters = "";
	
	private String [] names= {"EBS", "SBS", "MBC", "KBS2", "KBS1", "tvN", "JTBC2", "Eģ±ė", "KBS joy", "SBS fun E", "XtvN", "SPOTV", "OCN", "OCN Movies", "Mnet", "JTBC2", "Tooniverse"};
	private String [] urls = {"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=5100&key_depth2=15&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=5100&key_depth2=14&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=5100&key_depth2=13&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=5100&key_depth2=12&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=5100&key_depth2=12&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=7600&key_depth2=872&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=7600&key_depth2=874&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=7600&key_depth2=886&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=7600&key_depth2=880&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=7600&key_depth2=882&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=7600&key_depth2=185&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=5900&key_depth2=125&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=5800&key_depth2=178&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=5800&key_depth2=187&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=6000&key_depth2=&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=7800&key_depth2=&key_depth3=",
			"https://m.skbroadband.com/content/realtime/Channel_List.do?key_depth1=6600&key_depth2=&key_depth3="
		};

	
	/* --------------------------------- ģ¤ėģ ģ±ė ķøģ±ķ ---------------------------- */
	String getCurTime() {
		Date time = new Date();
		SimpleDateFormat format_time = new SimpleDateFormat("HH:mm");
		cur_time = format_time.format(time);
		return cur_time;
	} 

	//////////////// ģ¤ė ė ģ§ ė¦¬ķ“ /////////////////
	String getDate() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format_date = new SimpleDateFormat("yyyyMMdd");
		String date = format_date.format(cal.getTime());
		
		return date;
	}
	
	////////////////// (ģ±ė, URL) MAP ķØģ ė§ė¤ģ“ģ£¼ė ķØģ //////////////////////
	Map<String, String> makeChansAdsList(String[] names, String[] urls) {
		for(int i=0; i<names.length; i++) {
			channels_public.put(names[i], urls[i]);
		}
		return channels_public;
	}

	
	////////////////// ģ¬ģ©ģź° ģė „ķ ģ±ėź³¼ ė§¤ģ¹­ķģ¬ url ė°ķ ////////////////////////////
	String matchInputChans(String channel) {
		channels_public = makeChansAdsList(names, urls);
		if (channels_public.containsKey(channel)) {
			////////////////////// ģė „ channelģ urlģ ė°ķ ////////////////////////
			return channels_public.get(channel);
		}
		return "ģ±ė ģģ";
	}
	


	///////////////// ģģ§, ģ ģ  ķ ģ±ė ķøģ± ģ ė³“ ė³“ģ¬ģ£¼ė ķØģ /////////////////////////
	String showChannelSchedule(String channel, String date) {
		String infor = "";
		url = matchInputChans(channel) + date;
		try {
			while(true) {
				String cur_time = getCurTime(); // ķģ¬ ģź°
				System.out.println(url);
				doc = Jsoup.connect(url).get();
				
				cmm_boxs = doc.select("div.cmm_boxs");
				for(Element e: cmm_boxs.select("ol li")) {
					Elements flag_box = e.select("span.flag_box");
					flag_box.remove();
					Elements time=e.select("p.time");

					
					
					String today = getDate();

					if (today.equals(date)) {

					//////////////// ėÆøėģ ė°©ģ”ķ  ķė”ź·øėØķ  ė³“ģ¬ģ£¼źø° (ģ“ėÆø ė°©ģ”ķ ķė”ź·øėØ ģ ź±°) ///////////////
						if(time.text().compareTo(cur_time)>0) {
							if(e.className().equals("list on")) continue;
							infor += (e.text());
							infor += "\n";
						}
						if(e.className().equals("list on")) { 
							Elements bar_wrap=e.select("div.bar_wrap");
							bar_wrap.remove();
							infor += (e.text()) + "- !!!!! ķģ¬ ė°©ģ” ģ¤ !!!!!! ";
							infor += "\n";
						}
					}
					
					//////////////// ģ¬ģ©ģź° ģķė ė ģ§ģ ģ¤ė ė ģ§ź° ź°ģ§ ģģ¼ė©“ ģ ģ ķ  ķģź° ģģ //////////
					else {
						infor += (e.text());
						infor += "\n";
					}
					
				}
				return infor;
			}
		}
		catch (IOException e) {
				return "error";
			}
	}
	
	
	
	/* --------------------------------- ķė”ź·øėØ ė±ģ„ģøė¬¼ ķØģ ---------------------------- */
	String showCharactersInfor(String program) {
		int per = 0;
		url = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=" + program + "%EC%B6%9C%EC%97%B0%EC%A7%84";
		try {
			doc = Jsoup.connect(url).get();
			cm_tab_info_box = doc.select("div.cm_tab_info_box");
			// ģė„ ķė”ź·øėØģ strong ķź·øģ ģ°ģģø ģ“ė¦ģ“ ģź³ , span ķź·øģ ģ§ķ, ģ¶ģ° ėØģ“ź° ģģ
			// ėė¼ė§ė strong ķź·øģ ģ­ķ  ģ“ė¦ģ“ ģź³ , span ė°°ģ° ģ“ė¦ģ“ ģźø° ėė¬øģ ķģķ ģ½ė 
			for(Element e:cm_tab_info_box.select("ul li div.title_box")) {
				Elements strong = e.select("strong");
				Elements span = e.select("span");
				/////////////////////// ėė¼ė§ģ¼ ė ////////////////////////////
				if(span.text().contains("ģ§ķ")||span.text().contains("ģ¶ģ°")) 
					characters += "' " + strong.text() + " '";
				/////////////////////// ģė„ģ¼ ė /////////////////////////////
				else
					characters += "' " + span.text() + " '"; 
				
				///////////////////// 4ėŖź¹ģ§ė§ ė³“ģ¬ģ£¼źø° ///////////////////////
				if (per > 4) break;
				System.out.println();
				per += 1;
			}
		characters += "šØāšØ\n\n"; 
		characters += "ė±ģ“ ģ¶ģ°ķ©ėė¤~ šØāšØāš¦āš¦ ā \n ģ“ėģ?! ėėģ± " + program + "ģ“ ė³“ź³  ģ¶ģ¼ģģ£  ?? š¤­šš§";
			
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
		return characters;
	
}



/* --------------------------------- ķė”ź·øėØ ģ ė³“ ķØģ ---------------------------- */
	String showProgramInfor(String program) {
		String program_infor = "";
		Elements cm_info_box = null;
		try {
			String url = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=" + program;
			doc = Jsoup.connect(url).get();
			cm_info_box = doc.select("div.cm_info_box");
			
			for(Element e:cm_info_box.select("div.detail_info div")) {
				if(e.hasClass("text_expand")&&e.hasClass("_ellipsis")&&e.hasClass("_img_ellipsis")) {
						Elements button=e.select("button");
						button.remove();
						program_infor = e.text();
						program_infor += " \n\n";
						program_infor += "ķØź» ė§ė ė³“ģ¤ėģ ??? ššāŗ šš¼šš¼ššš¤";
					} 
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return program_infor;
	}



	/* --------------------------------- ķė”ź·øėØ ķøģ±ķ ķØģ ---------------------------- */
	String showProgramSchedule(String program) {
		
		Document doc;
		Elements cm_content_wrap = null;

		String str = "";
		String[] date=new String[100];
		String[] channel=new String[100];
		
		
		try {
			url = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=" + program + "%ED%8E%B8%EC%84%B1%ED%91%9C";
			doc = Jsoup.connect(url).get();
			cm_content_wrap =doc.select("div.pack_group");
			int j=0;
			for(Element e:cm_content_wrap.select("div.table_scroll_wrap")) {// ģ ģ²“
				if(e.hasClass("_scroll")) {
					for(Element e_channel:e.select("div.table_top_area>table.cm_table>thead>tr>th")) { // ģ±ė
							if(channel[e_channel.elementSiblingIndex()]==null) channel[e_channel.elementSiblingIndex()]=e_channel.text();
							else channel[e_channel.elementSiblingIndex()]+=e_channel.text();
							//System.out.println(channel[e_channel.elementSiblingIndex()]+e_channel.elementSiblingIndex());
						}
						for(Element e_date:e.select("div.table_body_area>table.cm_table>tbody>tr")) {
							Elements number=e_date.select("span.number");
							number.remove();
							Element first=e_date.firstElementSibling();
							//System.out.println(first);
							for(Element f:first.select("td.align_left")) {
								if(date[f.elementSiblingIndex()]==null) date[f.elementSiblingIndex()]=f.text();
								else date[f.elementSiblingIndex()]+=f.text();

							}
							j+=1;
							if(j==1) break;
						}	
					}	
				}
			str = "ģ¤ėģ " + program + " ķøģ±ķė„¼ ģė ¤ėė¦“ź²ģ !! š©šæāš» \n\n";
			for(int i=0; date[i]!=null; i++) {
				if(!date[i].equals("")) str += "š" + channel[i] + "š" + "\n" + "ā°" + date[i] + "ā°"+"\n";
				//System.out.println(channel[i]+"-"+date[i]);
			} return str;
		}
		catch(Exception e) {
			return "error";
		}
		
	}

}

	
