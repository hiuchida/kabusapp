package t2;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import util.DateTimeUtil;
import util.DateUtil;
import util.FileUtil;

public class MainSplitLog_r2 {

	public static void main(String[] args) {
		new MainSplitLog_r2().execute(args[0]);
	}

	private List<String> remain = new ArrayList<>();
	private Map<String, List<String>> map = new TreeMap<>();

	public void execute(String filepath) {
		List<String> lines = FileUtil.readAllLines(filepath);
		System.out.println("read  " + filepath + " " + lines.size() + " lines");
		String todayStr = DateUtil.nowToString().replaceAll("/", "");
		for (String s : lines) {
			if (s.length() < 19) {
				throw new RuntimeException("s.length=" + s.length());
			}
			String dateStr = s.substring(0, 19) + ".000";
			dateStr = dateStr.replaceAll("-", "/");
			Date date = DateTimeUtil.parseString(dateStr);
			if (date == null) {
				throw new RuntimeException("dateStr=" + dateStr);
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			if (cal.get(Calendar.HOUR_OF_DAY) < 7) {
				cal.add(Calendar.DAY_OF_MONTH, -1);
				date = cal.getTime();
			}
			String yyyymmdd = DateUtil.toString(date).replaceAll("/", "");
			if (todayStr.compareTo(yyyymmdd) <= 0) {
				remain.add(s);
				continue;
			}
			List<String> ls = map.get(yyyymmdd);
			if (ls == null) {
				ls = new ArrayList<>();
				map.put(yyyymmdd, ls);
			}
			ls.add(s);
		}
		for (String key : map.keySet()) {
			writeArchive(filepath, key);
		}
		FileUtil.writeAllLines(filepath, remain);
		System.out.println("write " + filepath + " " + remain.size() + " lines");
	}

	private void writeArchive(String filepath, String date) {
		String outfilepath = getOutfilepath(filepath, date);
		List<String> lines = FileUtil.readAllLines(outfilepath);
		for (String s : map.get(date)) {
			lines.add(s);
		}
		FileUtil.writeAllLines(outfilepath, lines);
		System.out.println("write " + outfilepath + " " + lines.size() + " lines");
	}

	private String getOutfilepath(String filepath, String date) {
		File f = new File(filepath);
		File pf = f.getParentFile();
		String name = f.getName();
		int idx = name.lastIndexOf(".");
		if (idx >= 0) {
			name = name.substring(0, idx) + "_" + date + name.substring(idx);
		} else {
			name = name + "_" + date;
		}
		File cf = new File(pf, "archive");
		cf.mkdirs();
		File of = new File(cf, name);
		return of.getPath();
	}
}
