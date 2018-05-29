package com.office.app;

import java.text.ParseException;
import java.util.Scanner;

import com.office.app.domain.Office;
import com.office.app.service.CalendarService;
import com.office.app.service.ParserService;

/** The main class for the command line dialogue */
public class App {
	public static void main(String[] args) {
		System.out.println("Insert the office hours and the meeting requests. Type in 'END' when you finish");
		Scanner scanner = new Scanner(System.in);
		String line = scanner.nextLine();
		String inputText = new String();
		while (!line.equals("END")) {
			// TODO check that the line has a correct format
			inputText += line + "\n";
			line = scanner.nextLine();
		}
		ParserService parserService = new ParserService();
		CalendarService calendarService = new CalendarService();
		try {
			// TODO check the text format
			if (inputText != null) {
				// parse the office object from the input text
				Office parsed = parserService.readText(inputText);
				if (parsed != null) {
					// check the meeting requests of the parsed office
					Office checked = calendarService.checkMeetings(parsed);
					// create a calendar from the checked office object
					String outputText = parserService.createCalendar(checked);
					System.out.println(outputText);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			// it is also possible to use try with resource
			if (scanner != null)
				scanner.close();
		}
	}
}
