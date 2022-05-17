package t1;

import java.util.ArrayList;
import java.util.List;

import util.FileUtil;
import util.StringUtil;

public class MainTxt2Html {

	public static void main(String[] args) {
		new MainTxt2Html().execute(args[0]);
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
		for (String s : lines) {
			if (s.startsWith("#")) {
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
				list.add("<td>" + s.trim() + "</td>");
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
