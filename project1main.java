import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

public class project1main {

	public static void main(String[] args) throws IOException {
		


		// Using TreeSet instead of arraylist because the need of countless sorting
	    TreeSet<House> emptyHouses = new TreeSet<House>();
	    TreeSet<Student> homelessStudents = new TreeSet<Student>();
	    // There is no need for them to be sorted 
		ArrayList<House> occupiedHouses = new ArrayList<House>();
		ArrayList<Student> studentsNeverFoundHouse = new ArrayList<Student>();
		ArrayList<Student> homeStudents = new ArrayList<Student>();
		
		// Number of the semester the code will run for 
		int maxSemester = 0;
		
		// Taking-scanning the input file and opening a writer
		String inputFileName = args[0];
		String outputFileName = args[1];
	    File input = new File(inputFileName);
	    FileWriter write = new FileWriter(outputFileName);
	    Scanner scan = new Scanner(input);
	    
	    while(scan.hasNextLine()) {
	    	try {
	    		String houseOrStudent = scan.next();
	    		// Deciding if student or house will be created
	    		switch(houseOrStudent) {
		    	case "h":
		    		// Taking the input from the text
		    		int houseID = scan.nextInt();
		    		int houseDuration = scan.nextInt();
		    		double houseRating = Double.parseDouble(scan.next());
		    		// Creating the house
		    		House house = new House(houseID,houseDuration,houseRating);
		    		// Adding created house to appropriate place
		    		if(houseDuration==0) {
		    			emptyHouses.add(house);
		    		}
		    		else {
		    			occupiedHouses.add(house);
		    		}
		    		break;
		    	case "s":
		    		// Taking the input from the text
		    		int studentID = scan.nextInt();
		    		String studentName = scan.next();
		    		int studentDuration = scan.nextInt();
		    		double studentRating = Double.parseDouble(scan.next());
		    		// Taking the max semester info
		    		maxSemester = Math.max(maxSemester, studentDuration);
		    		// Creating the student
		    		Student student = new Student(studentID,studentName,studentDuration,studentRating);
		    		// Adding student to appropriate place
		    		// If it has 0 semester left, no need for house 
		    		if(studentDuration>0) {
		    			homelessStudents.add(student);
		    		}
		    		else {
		    			studentsNeverFoundHouse.add(student);
		    		}
		    		break;
		    	}
	    	}
	    	catch(Exception e){
	    		continue;
	    	}
	    	
	    }
	    // Creating iterators to iterate over TreeSet - ArrayList
	    Iterator<Student> homelessStudentIterator = homelessStudents.iterator();
	    Iterator<House> occupiedHouseIterator = occupiedHouses.iterator();
    	Iterator<House> emptyHouseIterator = emptyHouses.iterator();
    	
    	// Semesters will run using for loop
	    for(int semester=0;semester<maxSemester;semester++) {
	    	// In order to iterate multiple times on students' TreeSet, resetting the iterator at the beginning of every semester
	    	homelessStudentIterator = homelessStudents.iterator();
	    	
		    while(homelessStudentIterator.hasNext()) {
		    	// Picking a student from the top of the TreeSet
		    	Student student = homelessStudentIterator.next();
		    	
		    	// In order to iterate multiple times on houses' TreeSet, resetting the iterator for every student picked
		    	emptyHouseIterator = emptyHouses.iterator();
		    	
		    	while(emptyHouseIterator.hasNext()) {
		    		// Picking a house from the top of the TreeSet
		    		House house = emptyHouseIterator.next();
		    		
		    		if(doTheyMatch(student,house)) {
		    			// Adding student and house to appropriate arraylist
		    			homeStudents.add(student);
		    			occupiedHouses.add(house);
		    			// Setting duration for house
		    			house.setDuration(student.getDuration());
		    			// Removing student and house from their TreeSet
		    			homelessStudentIterator.remove();
		    			emptyHouseIterator.remove();
		    			break;
		    			}
		    		}
		    }
		    // In order to iterate multiple times on houses' TreeSet, resetting the iterator for every semester
		    occupiedHouseIterator = occupiedHouses.iterator();
		    
		    // Checking if there are any house available on occupiedHouses
		    while(occupiedHouseIterator.hasNext()) {
		    	// Picking a house and decreasing its' duration
		    	House occupiedHouse = occupiedHouseIterator.next();
		    	occupiedHouse.decreaseDuration();
		    	//  Checking if the duration became zero
		    	if(occupiedHouse.isAvailable()) {
		    		// Removing house from occupiedHouse and adding to emptyHouse
		    		emptyHouses.add(occupiedHouse);
		    		occupiedHouseIterator.remove();
		    		
		    	}
		    }
		    // Checking if there are any graduated student
		    homelessStudentIterator = homelessStudents.iterator();
	    	// In order to iterate multiple times on students' TreeSet, resetting the iterator
		    while(homelessStudentIterator.hasNext()) {
		    	// Picking student and decreasing its' duration
		    	Student homelessStudent = homelessStudentIterator.next();
		    	homelessStudent.decreaseDuration();
		    	//  Checking if the duration became zero
		    	if(homelessStudent.getDuration()==0) {
		    		// Removing student from homelessStudent and adding to studentsNeverFoundHouse
		    		homelessStudentIterator.remove();
		    		studentsNeverFoundHouse.add(homelessStudent);
		    	}
		    }

		    
		    

	    }
	    // Adding studentsNeverFoundHouse to homelessStudents for sorting-printing purpose
	    for(Student student:studentsNeverFoundHouse) {
	    	homelessStudents.add(student);
	    }
	    // Printing the students using an iterator
	    homelessStudentIterator = homelessStudents.iterator();
	    while(homelessStudentIterator.hasNext()) {
	    	Student student = homelessStudentIterator.next();
	    	if(homelessStudentIterator.hasNext()) {
	    		write.write(student.getName());
	    		write.write("\n");
	    	}
	    	else {
	    		write.write(student.getName());
	    	}
	    }
	    
	    write.close();
	    scan.close();
	    

	}
	/**
	 * Using their ratings, matches student and house
	@param student and house to be matched
	@return true if they match
	*/
	static boolean doTheyMatch(Student student,House house) {
		if(house.getRating()>=student.getRating()) {
			return true;
		}
		
		return false;
	}
}