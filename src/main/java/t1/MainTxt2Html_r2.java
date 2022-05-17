package t1;

import java.util.ArrayList;
import java.util.List;

import util.FileUtil;
import util.StringUtil;

public class MainTxt2Html_r2 {

	public static void main(String[] args) {
		new MainTxt2Html_r2().execute(args[0]);
	}

	private String header = "";
	private List<String[]> records = new ArrayList<>();

	public void execute(String filepath) {
		readTxt(filepath);
		System.out.println(header);
		for (String[] cols : records) {
			System.out.println(cols[0]);
		}
		List<String> list = new ArrayList<>();
		appendBefore(list);
		appendHeader(list);
		appendRecords(list);
		appendAfter(list);
		int idx = filepath.lastIndexOf(".");
		if (idx >= 0) {
			filepath = filepath.substring(0, idx);
		}
		filepath = filepath + ".html";
		FileUtil.writeAllLines(filepath, list);
	}

	private void readTxt(String filepath) {
		List<String> lines = FileUtil.readAllLines(filepath);
		int lineNo = 0;
		for (String s : lines) {
			lineNo++;
			if (lineNo == 1 && s.startsWith("#")) {
				header = s.substring(1);
				continue;
			}
			String[] cols = StringUtil.splitTab(s);
			records.add(cols);
		}
	}

	private void appendBefore(List<String> list) {
		list.add("<!DOCTYPE html>");
		list.add("<html lang=\"en\">");
		list.add("<head>");
		list.add("<meta charset=\"utf-8\"/>");
		list.add("<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0\">");
		list.add("</head>");
		list.add("<body>");
		list.add("<table>");
	}

	private void appendHeader(List<String> list) {
		String[] cols = StringUtil.splitTab(header);
		list.add("<tr>");
		for (String s : cols) {
			list.add("<th>" + s.trim() + "</th>");
		}
		list.add("</tr>");
	}

	private void appendRecords(List<String> list) {
		for (String[] cols : records) {
			list.add("<tr>");
			for (String s : cols) {
				s = s.trim();
				if (s.length() == 0) {
					list.add("<td>&nbsp;</td>");
				} else {
					list.add("<td>" + s + "</td>");
				}
			}
			list.add("</tr>");
		}
	}

	private void appendAfter(List<String> list) {
		list.add("</table>");
		list.add("</body>");
		list.add("</html>");
	}

}
