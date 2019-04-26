package com.example.qidai.unit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.sf.json.JSONObject;

public  class GetWeatherUnit {
	private static final String[] USER_AGENTS = {
	         "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; AcooBrowser; .NET CLR 1.1.4322; .NET CLR 2.0.50727)",
	         "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; Acoo Browser; SLCC1; .NET CLR 2.0.50727; Media Center PC 5.0; .NET CLR 3.0.04506)",
	         "Mozilla/4.0 (compatible; MSIE 7.0; AOL 9.5; AOLBuild 4337.35; Windows NT 5.1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)",
	         "Mozilla/5.0 (Windows; U; MSIE 9.0; Windows NT 9.0; en-US)",
	         "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET CLR 2.0.50727; Media Center PC 6.0)",
	         "Mozilla/5.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET CLR 1.0.3705; .NET CLR 1.1.4322)",
	         "Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 5.2; .NET CLR 1.1.4322; .NET CLR 2.0.50727; InfoPath.2; .NET CLR 3.0.04506.30)",
	         "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN) AppleWebKit/523.15 (KHTML, like Gecko, Safari/419.3) Arora/0.3 (Change: 287 c9dfb30)",
	         "Mozilla/5.0 (X11; U; Linux; en-US) AppleWebKit/527+ (KHTML, like Gecko, Safari/419.3) Arora/0.6",
	         "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.2pre) Gecko/20070215 K-Ninja/2.1.1",
	         "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9) Gecko/20080705 Firefox/3.0 Kapiko/3.0",
	         "Mozilla/5.0 (X11; Linux i686; U;) Gecko/20070322 Kazehakase/0.4.5",
	         "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.8) Gecko Fedora/1.9.0.8-1.fc10 Kazehakase/0.5.6",
	         "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11",
	         "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_3) AppleWebKit/535.20 (KHTML, like Gecko) Chrome/19.0.1036.7 Safari/535.20",
	         "Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; fr) Presto/2.9.168 Version/11.52"
		};
	
	
	
	/*
	 * 获取天气
	 *	 参数类型 : String (例如,北京)
	 *	 返回类型 : Map<String,JSONObject> (例如 周四,{"空气质量":"良","实时温度":"16","日期":"04月11日 周四 农历三月初七 ","风力":"西风3-4级","天气情况":"阴转多云","温度范围":"6 ~ 18℃"})
	 * */
	public static Map<String,JSONObject> getWeather(String city) {
		Map<String,JSONObject> map = new HashMap<String,JSONObject>();
		
		String weatherUrl = "http://www.baidu.com/baidu?tn=monline_3_dg&ie=utf-8&wd="+city+"天气";
		String userAgent = getUserAgents();
		try {
			Document doc = Jsoup.connect(weatherUrl).userAgent(userAgent).timeout(5000).get();
			Elements a = doc.getElementsByClass("op_weather4_twoicon").get(0).getElementsByTag("a");
			for (Element element : a) {
				String quality = "";
				String current = "";
				String today = "";
 
				//只有当天才有实时温度
				if (!element.getElementsByClass("op_weather4_twoicon_shishi_title").isEmpty()) {
					current = element.getElementsByClass("op_weather4_twoicon_shishi_title").text();
				}
				//空气质量
				if (!element.getElementsByClass("op_weather4_twoicon_aqi_text_today").isEmpty()) {
					quality = element.getElementsByClass("op_weather4_twoicon_aqi_text_today").text();
				}else {
					quality = element.getElementsByClass("op_weather4_twoicon_aqi_text").text();
				}
				//日期
				if (!element.getElementsByClass("op_weather4_twoicon_date").isEmpty()) {
					today = element.getElementsByClass("op_weather4_twoicon_date").text();
				}else {
					today = element.getElementsByClass("op_weather4_twoicon_date_day").text();
				}
				//风
				String wind = element.getElementsByClass("op_weather4_twoicon_wind").text();				
				//天气
				String weath = element.getElementsByClass("op_weather4_twoicon_weath").text();
				//气温
				String temp = element.getElementsByClass("op_weather4_twoicon_temp").text();				
				JSONObject json = new JSONObject();
				json.put("空气质量", quality);
				json.put("实时温度", current);
				json.put("日期", today);
				json.put("风力", wind);
				json.put("天气情况", weath);
				json.put("温度范围", temp);
				map.put(today.substring(today.indexOf("周"), today.indexOf("周")+2), json);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/*
	 * 处理天气格式 参数类型 : String 返回类型 : Map<String,String>
	 */
	public static Map<String, String> manageWeather(String weather) {
		Map<String, String> map = new HashMap<String, String>();
		JSONObject json = JSONObject.fromObject(weather);
		String current = (String) json.get("实时温度");
		String quality = (String) json.get("空气质量");
		String wind = (String) json.get("风力");
		String weath = (String) json.get("天气情况");
		String temp = (String) json.get("温度范围");
		// {"空气质量":"优","实时温度":"","日期":"周四","风力":"南风3-4级","天气情况":"多云","温度范围":"10 ~ 23℃"}
		map.put("实时温度", current);
		map.put("空气质量", quality);
		map.put("风力", wind);
		map.put("天气情况", weath);
		map.put("温度范围", temp);
		return map;
	}

	/*
	 * 天气聊天回复 
	 * 	参数类型 :String,String
	  *      返回类型 :String
	 */
	public static String getWeatherAnswer(String content, String ipString) {
		String answer = "";
		System.out.println(ipString);
		String city = Unit.getAddress(ipString);
		System.out.println(city);
		Map<String, JSONObject> weather = getWeather(city);
		if (content.indexOf("明") != -1) {
			// 明天天气
			Date date = new Date();
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.add(calendar.DATE, 1);
			date = calendar.getTime();
			System.out.println(date);
			answer = answer + weather.get(Unit.getWeekOfDate(date));
			System.out.println(answer);
			Map<String, String> manageWeather = manageWeather(answer);
			String current = manageWeather.get("实时温度");
			String quality = manageWeather.get("空气质量");
			String wind = manageWeather.get("风力");
			String weath = manageWeather.get("天气情况");
			String temp = manageWeather.get("温度范围");
			answer = chat_tom("明天",current,quality,wind,weath,temp);

		} else if (content.indexOf("后") != -1) {
			// 后天天气
			Date date = new Date();
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.add(calendar.DATE, 2);
			date = calendar.getTime();
			answer = answer + weather.get(Unit.getWeekOfDate(date));
			Map<String, String> manageWeather = manageWeather(answer);
			String current = manageWeather.get("实时温度");
			String quality = manageWeather.get("空气质量");
			String wind = manageWeather.get("风力");
			String weath = manageWeather.get("天气情况");
			String temp = manageWeather.get("温度范围");
			answer = chat_tom("后天",current,quality,wind,weath,temp);

		} else {
			// 当天天气
			Date date = new Date();
			answer = answer + weather.get(Unit.getWeekOfDate(date));
			//System.out.println(answer);
			Map<String, String> manageWeather = manageWeather(answer);
			String current = manageWeather.get("实时温度");
			String quality = manageWeather.get("空气质量");
			String wind = manageWeather.get("风力");
			String weath = manageWeather.get("天气情况");
			String temp = manageWeather.get("温度范围");
			answer = chat_today(current,quality,wind,weath,temp);
		}
		return answer;
	}
	
	/*
	 * 天气聊天回复升级(今天) 
	 * 	参数类型 :String,String,String,String,String
	  *      返回类型 :String
	 */
	public static String chat_today(String current,String quality,String wind,String weath,String temp) {
		String answer = "";
		List<String> list_wennuan = new ArrayList<String>();
		list_wennuan.add("正所谓气候宜人，鸟语花香，正是旅游的好季节。");
		list_wennuan.add("这么好的天气，还不出去撩妹？话不多说，我要出门了，请看好你们的女朋友。");
		list_wennuan.add("天气正好，约吗？");
		
		List<String> list_yutian = new ArrayList<String>();
		list_yutian.add("清明时节雨纷纷，下句什么来着？");
		list_yutian.add("要是不下雨我就出去旅游了！");
		list_yutian.add("哎，下雨天，睡觉天，(。-ω-)zzz");
		list_yutian.add("雨天是我放声哭泣的时间。");
		
		if(wind.indexOf("级")!=-1&&wind.indexOf("-")!=-1) {
			wind = wind.substring(wind.indexOf("风")+1,wind.indexOf("-"));
			if(Integer.parseInt(wind)>=4) {
				answer="当前"+current+"℃，风有点大，没点体重别硬扛";
			}else {
				if(Integer.parseInt(current)>=18&&Integer.parseInt(current)<=25&&weath.indexOf("雨")==-1) {
					Random random = new Random();
					int n = random.nextInt(list_wennuan.size());
					answer="当前"+current+"℃，"+list_wennuan.get(n);
				}else if(Integer.parseInt(current)>=18&&Integer.parseInt(current)<=25&&weath.indexOf("雨")!=-1) {
					Random random = new Random();
					int n = random.nextInt(list_wennuan.size());
					answer="当前"+current+"℃，"+weath+"，"+list_yutian.get(n);				
				}else if(Integer.parseInt(current)>=26&&Integer.parseInt(current)<=32&&weath.indexOf("雨")!=-1){
					answer="当前"+current+"℃，"+weath+"，好，雨下的妙";
				}else if(Integer.parseInt(current)>=26&&Integer.parseInt(current)<=32&&weath.indexOf("雨")==-1){
					answer="当前"+current+"℃，"+weath+"，热啊";
				}else if(Integer.parseInt(current)>=32){
					answer="当前"+current+"℃，"+weath+"，啥意思，整这么热要烤乳猪？";
				}else if(Integer.parseInt(current)>=10&&Integer.parseInt(current)<=17) {
					answer="当前"+current+"℃，"+weath+"，秋裤在手，天下我有。";				
				}else if(Integer.parseInt(current)>=0&&Integer.parseInt(current)<=9&&weath.indexOf("雪")==-1) {
					answer="当前"+current+"℃，"+weath+"，毛衣穿起来，心里暖起来。";				
				}else if(Integer.parseInt(current)>=0&&Integer.parseInt(current)<=9&&weath.indexOf("雪")!=-1) {
					answer="当前"+current+"℃，"+weath+"，我要堆雪人，打雪仗！";				
				}else if(Integer.parseInt(current)<0&&weath.indexOf("雪")==-1) {
					answer="当前"+current+"℃，"+weath+"，建议不要外出，守着被窝和火炉。";				
				}else if(Integer.parseInt(current)<0&&weath.indexOf("雪")!=-1) {
					answer="当前"+current+"℃，"+weath+"，堆雪人需谨慎，小心自己成雪人！";				
				}
			}
		}else if(wind.indexOf("级")!=-1&&wind.indexOf("-")!=-1){
			wind = wind.substring(wind.indexOf("风")+1,wind.indexOf("级"));
			if(Integer.parseInt(wind)>=4) {
				answer="当前"+current+"℃，风有点大，没点体重别硬扛";
			}else {
				if(Integer.parseInt(current)>=18&&Integer.parseInt(current)<=25&&weath.indexOf("雨")==-1) {
					Random random = new Random();
					int n = random.nextInt(list_wennuan.size());
					answer="当前"+current+"℃，"+list_wennuan.get(n);
				}else if(Integer.parseInt(current)>=18&&Integer.parseInt(current)<=25&&weath.indexOf("雨")!=-1) {
					Random random = new Random();
					int n = random.nextInt(list_wennuan.size());
					answer="当前"+current+"℃，"+weath+"，"+list_yutian.get(n);				
				}else if(Integer.parseInt(current)>=26&&Integer.parseInt(current)<=32&&weath.indexOf("雨")!=-1){
					answer="当前"+current+"℃，"+weath+"，好，雨下的妙";
				}else if(Integer.parseInt(current)>=26&&Integer.parseInt(current)<=32&&weath.indexOf("雨")==-1){
					answer="当前"+current+"℃，"+weath+"，热啊";
				}else if(Integer.parseInt(current)>=32){
					answer="当前"+current+"℃，"+weath+"，啥意思，整这么热要烤乳猪？";
				}else if(Integer.parseInt(current)>=10&&Integer.parseInt(current)<=17) {
					answer="当前"+current+"℃，"+weath+"，秋裤在手，天下我有。";				
				}else if(Integer.parseInt(current)>=0&&Integer.parseInt(current)<=9&&weath.indexOf("雪")==-1) {
					answer="当前"+current+"℃，"+weath+"，毛衣穿起来，心里暖起来。";				
				}else if(Integer.parseInt(current)>=0&&Integer.parseInt(current)<=9&&weath.indexOf("雪")!=-1) {
					answer="当前"+current+"℃，"+weath+"，我要堆雪人，打雪仗！";				
				}else if(Integer.parseInt(current)<0&&weath.indexOf("雪")==-1) {
					answer="当前"+current+"℃，"+weath+"，建议不要外出，守着被窝和火炉。";				
				}else if(Integer.parseInt(current)<0&&weath.indexOf("雪")!=-1) {
					answer="当前"+current+"℃，"+weath+"，堆雪人需谨慎，小心自己成雪人！";				
				}
			}
		}else {
			if(Integer.parseInt(current)>=18&&Integer.parseInt(current)<=25&&weath.indexOf("雨")==-1) {
				Random random = new Random();
				int n = random.nextInt(list_wennuan.size());
				answer="当前"+current+"℃，"+list_wennuan.get(n);
			}else if(Integer.parseInt(current)>=18&&Integer.parseInt(current)<=25&&weath.indexOf("雨")!=-1) {
				Random random = new Random();
				int n = random.nextInt(list_wennuan.size());
				answer="当前"+current+"℃，"+weath+"，"+list_yutian.get(n);				
			}else if(Integer.parseInt(current)>=26&&Integer.parseInt(current)<=32&&weath.indexOf("雨")!=-1){
				answer="当前"+current+"℃，"+weath+"，好，雨下的妙";
			}else if(Integer.parseInt(current)>=26&&Integer.parseInt(current)<=32&&weath.indexOf("雨")==-1){
				answer="当前"+current+"℃，"+weath+"，热啊";
			}else if(Integer.parseInt(current)>=32){
				answer="当前"+current+"℃，"+weath+"，啥意思，整这么热要烤乳猪？";
			}else if(Integer.parseInt(current)>=10&&Integer.parseInt(current)<=17) {
				answer="当前"+current+"℃，"+weath+"，秋裤在手，天下我有。";				
			}else if(Integer.parseInt(current)>=0&&Integer.parseInt(current)<=9&&weath.indexOf("雪")==-1) {
				answer="当前"+current+"℃，"+weath+"，毛衣穿起来，心里暖起来。";				
			}else if(Integer.parseInt(current)>=0&&Integer.parseInt(current)<=9&&weath.indexOf("雪")!=-1) {
				answer="当前"+current+"℃，"+weath+"，我要堆雪人，打雪仗！";				
			}else if(Integer.parseInt(current)<0&&weath.indexOf("雪")==-1) {
				answer="当前"+current+"℃，"+weath+"，建议不要外出，守着被窝和火炉。";				
			}else if(Integer.parseInt(current)<0&&weath.indexOf("雪")!=-1) {
				answer="当前"+current+"℃，"+weath+"，堆雪人需谨慎，小心自己成雪人！";				
			}
		}
		
		
		
				
		return answer;
	}
	

	/*
	 * 天气聊天回复升级(明后) 
	 * 	参数类型 :String,String,String,String,String
	  *      返回类型 :String
	 */
	public static String chat_tom(String date,String current,String quality,String wind,String weath,String temp) {
		String answer = "";
		List<String> list_wennuan = new ArrayList<String>();
		list_wennuan.add("正所谓气候宜人，鸟语花香，正是旅游的好季节。");
		list_wennuan.add("这么好的天气，还不出去撩妹？话不多说，我要出门了，请看好你们的女朋友。");
		list_wennuan.add("天气正好，约吗？");
		
		List<String> list_yutian = new ArrayList<String>();
		list_yutian.add("清明时节雨纷纷，下句什么来着？");
		list_yutian.add("要是不下雨我就出去旅游了！");
		list_yutian.add("哎，下雨天，睡觉天，(。-ω-)zzz");
		list_yutian.add("雨天是我放声哭泣的时间。");
		temp = temp.replace(" ", "");
		int min = Integer.parseInt(temp.substring(0,temp.indexOf("~")));
		int max = Integer.parseInt(temp.substring(temp.indexOf("~")+1,temp.indexOf("℃")));
		int average = (min+max)/2;
		if(wind.indexOf("级")!=-1&&wind.indexOf("-")!=-1) {
			wind = wind.substring(wind.indexOf("风")+1,wind.indexOf("-"));
			if(Integer.parseInt(wind)>=4) {
				answer=date+temp+"，风有点大，没点体重别硬扛";
			}else {
				if(average>=12&&average<=25&&weath.indexOf("雨")==-1) {
					Random random = new Random();
					int n = random.nextInt(list_wennuan.size());
					answer=date+temp+"，"+weath+"，"+list_wennuan.get(n);
				}else if(average>=12&&average<=25&&weath.indexOf("雨")!=-1) {
					Random random = new Random();
					int n = random.nextInt(list_wennuan.size());
					answer=date+temp+"，"+weath+"，"+list_yutian.get(n);				
				}else if(average>=26&&average<=32&&weath.indexOf("雨")!=-1){
					Random random = new Random();
					int n = random.nextInt(list_wennuan.size());
					answer=date+temp+"，"+weath+"，"+list_yutian.get(n);	
				}else if(average>=26&&average<=32&&weath.indexOf("雨")==-1){
					answer=date+temp+"，"+weath+"，热啊";
				}else if(average>=32){
					answer=date+temp+"，"+weath+"，啥意思，整这么热要烤乳猪？";
				}else if(average>=8&&average<=11) {
					answer=date+temp+"，"+weath+"，秋裤在手，天下我有。";				
				}else if(average>=0&&average<=7&&weath.indexOf("雪")==-1) {
					answer=date+temp+"，"+weath+"，毛衣穿起来，心里暖起来。";				
				}else if(average>=0&&average<=7&&weath.indexOf("雪")!=-1) {
					answer=date+temp+"，"+weath+"，我要堆雪人，打雪仗！";				
				}else if(average<0&&weath.indexOf("雪")==-1) {
					answer=date+temp+"，"+weath+"，建议不要外出，守着被窝和火炉。";				
				}else if(average<0&&weath.indexOf("雪")!=-1) {
					answer=date+temp+"，"+weath+"，堆雪人需谨慎，小心自己成雪人！";				
				}
			}
		}else if(wind.indexOf("级")!=-1&&wind.indexOf("-")!=-1){
			wind = wind.substring(wind.indexOf("风")+1,wind.indexOf("级"));
			if(Integer.parseInt(wind)>=4) {
				answer=date+temp+"，风有点大，没点体重别硬扛";
			}else {
				if(average>=12&&average<=25&&weath.indexOf("雨")==-1) {
					Random random = new Random();
					int n = random.nextInt(list_wennuan.size());
					answer=date+temp+"，"+weath+"，"+list_wennuan.get(n);
				}else if(average>=12&&average<=25&&weath.indexOf("雨")!=-1) {
					Random random = new Random();
					int n = random.nextInt(list_wennuan.size());
					answer=date+temp+"，"+weath+"，"+list_yutian.get(n);				
				}else if(average>=26&&average<=32&&weath.indexOf("雨")!=-1){
					Random random = new Random();
					int n = random.nextInt(list_wennuan.size());
					answer=date+temp+"，"+weath+"，"+list_yutian.get(n);
				}else if(average>=26&&average<=32&&weath.indexOf("雨")==-1){
					answer=date+temp+"，"+weath+"，热啊";
				}else if(average>=32){
					answer=date+temp+"，"+weath+"，啥意思，整这么热要烤乳猪？";
				}else if(average>=8&&average<=11) {
					answer=date+temp+"，"+weath+"，秋裤在手，天下我有。";				
				}else if(average>=0&&average<=7&&weath.indexOf("雪")==-1) {
					answer=date+temp+"，"+weath+"，毛衣穿起来，心里暖起来。";				
				}else if(average>=0&&average<=7&&weath.indexOf("雪")!=-1) {
					answer=date+temp+"，"+weath+"，我要堆雪人，打雪仗！";				
				}else if(average<0&&weath.indexOf("雪")==-1) {
					answer=date+temp+"，"+weath+"，建议不要外出，守着被窝和火炉。";				
				}else if(average<0&&weath.indexOf("雪")!=-1) {
					answer=date+temp+"，"+weath+"，堆雪人需谨慎，小心自己成雪人！";				
				}
			}
		}else {
			if(average>=12&&average<=25&&weath.indexOf("雨")==-1) {
				Random random = new Random();
				int n = random.nextInt(list_wennuan.size());
				answer=date+temp+"，"+weath+"，"+list_wennuan.get(n);
			}else if(average>=12&&average<=25&&weath.indexOf("雨")!=-1) {
				Random random = new Random();
				int n = random.nextInt(list_wennuan.size());
				answer=date+temp+"，"+weath+"，"+list_yutian.get(n);				
			}else if(average>=26&&average<=32&&weath.indexOf("雨")!=-1){
				Random random = new Random();
				int n = random.nextInt(list_wennuan.size());
				answer=date+temp+"，"+weath+"，"+list_yutian.get(n);
			}else if(average>=26&&average<=32&&weath.indexOf("雨")==-1){
				answer=date+temp+"，"+weath+"，热啊";
			}else if(average>=32){
				answer=date+temp+"，"+weath+"，啥意思，整这么热要烤乳猪？";
			}else if(average>=8&&average<=11) {
				answer=date+temp+"，"+weath+"，秋裤在手，天下我有。";				
			}else if(average>=0&&average<=7&&weath.indexOf("雪")==-1) {
				answer=date+temp+"，"+weath+"，毛衣穿起来，心里暖起来。";				
			}else if(average>=0&&average<=7&&weath.indexOf("雪")!=-1) {
				answer=date+temp+"，"+weath+"，我要堆雪人，打雪仗！";				
			}else if(average<0&&weath.indexOf("雪")==-1) {
				answer=date+temp+"，"+weath+"，建议不要外出，守着被窝和火炉。";				
			}else if(average<0&&weath.indexOf("雪")!=-1) {
				answer=date+temp+"，"+weath+"，堆雪人需谨慎，小心自己成雪人！";				
			}
		}			
		return answer;
	}
	
	public static String getUserAgents() {
		Random random = new Random();
		int index = random.nextInt(USER_AGENTS.length);
		return USER_AGENTS[index];
	}
 
	public static String getUserAgents(int index) {
		if (index < 0 || index > USER_AGENTS.length) {
			return getUserAgents();
		}
		return USER_AGENTS[index];
	}
	
	
}
