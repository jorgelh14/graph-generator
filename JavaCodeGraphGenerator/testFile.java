/*
  CaseBean.java -  Class implementing entering new case and
                   editing an existing case

   Date     Author      Changes
   ------------------------------------------------------------
   1/08/02  norbert     Initial draft
   5/12/08	chrish		Updated SQL queries for MySQL 5
   7/11/10	luz			Adding DA functions
   8/15/12  luz			Removing DA functions

FUNCTIONS:

 * storeCaseData
 * readCaseData
 * updateCaseData
 * storeCaseActivityData //not used yet
 * searchNewCodeArrays
 * enterAssociatedCase
 * searchCause(cause)
 * getFeeGroup
 * getFeesTotal
 * hasFeesWithoutResponsibleParty
 */
package beans;

import com.Offense;
import com.CaseType;
import java.sql.*;
import javax.servlet.http.*;
import java.text.*;
import java.util.*;
import java.awt.Image;
//import com.idautomation.linear.*;
//import com.idautomation.linear.encoder.*;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;

// import log4j classes
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;

/**
 * new case bean
 * <p>
 * used by case_new.jsp and case_view.jsp
 * scope: page
 */

public class CaseBean extends GenericBean{

	// error codes
	public final static int OPERATION_FAILED = -1;
	public final static int NO_DATA_FOUND    = -2;
	public final static int DUPLICATE        = -3;
	public final static int NOT_ALLOWED      = -4;
	public final static int DUPLICATE_WARNING = -5;

	// constants describing case status
	public final static String NONE = "NONE";
	public final static String OPEN = "OPEN";
	public final static String DISPOSED = "DISPOSED";
	public final static String INACTIVE = "INACTIVE";

	// constants describing case category
	public final static String CRIMINAL = "CRIMINAL";
	public final static String CIVIL = "CIVIL";
	public final static String FAMILY = "FAMILY";
	public final static String PROBATE = "PROBATE";

	private ExceptionEmailBean exceptionEmail = new ExceptionEmailBean();

	//static Logger logger = Logger.getLogger(getClass().getName());

	// elements of the caseNewForm form
	// Note: in order for the exchange to work these
	// variables have to be private and named just like
	// elements of the form

	private String caseOffType;
	private String causeNumber;
	private String year;
	private String month;
	private String type;
	private String crt;
	private String volumePage;
	private String caseCategory;
	private String caseType;
	private String caseTypeDesc;
	private int caseTypesFound;
	private CaseType caseTypeArray[];
	private String offense;
	private String offenseDate;
	private String offenseDesc;
	private int offensesFound;
	private String subCategory;

	private Offense offenseArray[];
	private String[][] newOffensesArray;
	private String[][] newCaseTypesArray;
	private String[][] newOthersArray;
	private String[][] causeNumbersArray;
	private String court;
	private String location;
	private String facility;
	private String section;
	private String aisle;
	private String row;
	private String shelf;
	private String box;
	private int seqNum;
	private String courtDesc;
	private String filingDate;
	private String filedType;
	private String filedTypeDesc;
	private String caseStyle;
	private String dispositionDate;
	private String dispositionType;
	private String enteredDateTime;
	private String enteredUser;
	private String capias;
	private String dispositionTypeDesc;
	private String judgment;
	private String comments;
	private String jailedDate;
	private String notes;
	private String juryExamined;
	private String jurySworn;
	private String warrantOutstanding;
	private String destroyDate;
	private String nopp;
	private int caseId;
	private int sequenceNumber;
	private int autoNumber = 0;
	private String state;
	private String county;
	private String clerk;
	private String sealed;
	private String juryDemand;
	private String trnNumber;
	private String countyCourt;

	//These are DA only:
	private String agencyCaseNo;
	private String injuredParty;
	private String juvenile;
	private String dateIndicted;
	private String offenseManual;
	private String arrestDate;
	private String judgmentDate;
	private String sentenceDate;
	private String pleaType;
	private String pleaTypeDesc;
	private String examiningTrial;
	private String onAppeal;
	private String speedyTrial;
	private String existing;
	private String existingOffense;
	private String lastName;
	private String firstName;
	private String middleName;
	private String alias;
	private String suffix;
	private String dateOfBirth;
	private String driversLicenseNumber;
	private String driversLicenseExpDate;
	private String dlState;
	private String dlType;
	private String degree;

	/**
	 * image of data in CASES table
	 * after it was retrieved
	 * will be used when verification
	 * before update will be implemented
	 */
	// private ImageOfCases image;

	/**
	 * casuseArray
	 * Used to worn user for duplicate cause number.
	 */
	private String[] causeArray;

	private int caseFound;

	private int previousCaseId;
	private int nextCaseId;

	/**
	 * default constructor
	 * <p>
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public CaseBean()
	{
		super();
		caseOffType = "";
		causeNumber = "";
		year = "";
		month = "";
		type = "";
		crt = "";
		volumePage = "";
		//used to be caseCategory = "0";
		caseCategory = "";
		caseType = "0";
		caseTypeDesc = "";
		caseTypesFound = 0;
		caseTypeArray = null;
		offense = "0";
		offenseDate = "";
		offenseDesc = "";
		offensesFound = 0;
		offenseArray = null;
		newOffensesArray = null;
		newCaseTypesArray = null;
		newOthersArray = null;
		subCategory = "";

		court = "0";
		location = "";
		facility = "";
		section = "";
		aisle = "";
		row = "";
		shelf = "";
		box = "";
		seqNum = 0;
		courtDesc = "";
		filingDate = "";
		filedType = "0";
		filedTypeDesc = "";
		destroyDate = "";
		caseStyle = "";
		enteredDateTime = "";
		enteredUser = "";
		capias = "0";
		dispositionDate = "";
		dispositionType = "0";
		dispositionTypeDesc = "";
		judgment = "";
		jailedDate = "";
		comments = "";
		notes = "";
		jurySworn = "";
		juryExamined = "";
		warrantOutstanding = "";
		nopp = "";
		caseId = 0;
		sequenceNumber = 0;
		state = "";
		county = "";
		clerk = "";
		caseId = 0;
		sealed = "";
		juryDemand = "";
		trnNumber = "";
		countyCourt = "";
		//PropertyConfigurator.configure("log4j.properties");
		// image = null;
		causeArray = null;
		caseFound = 0;
		nextCaseId = 0;
		previousCaseId = 0;

		//These are DA only:
		agencyCaseNo = "";
		injuredParty = "";
		juvenile = "";
		dateIndicted = "";
		offenseManual = "";
		arrestDate = "";
		judgmentDate = "";
		sentenceDate = "";
		pleaType = "0";
		pleaTypeDesc = "";
		examiningTrial = "";
		onAppeal = "";
		speedyTrial = "";
		existing = "0";
		existingOffense = "0";
		lastName = "";
		firstName = "";
		middleName = "";
		alias = "";
		suffix = "";
		dateOfBirth = "";
		driversLicenseNumber = "";
		driversLicenseExpDate = "";
		dlState = "";
		dlType = "";
		degree = "";
	}

	/**
	 * resets values of the form to blank/default values
	 * <p>
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void reset()
	{
		caseOffType = "";
		causeNumber = "";
		year = "";
		month = "";
		type = "";
		crt = "";
		volumePage = "";
		//used to be caseCategory = "0";
		caseCategory = "";
		caseType = "0";
		caseTypeDesc = "";
		caseTypesFound = 0;
		caseTypeArray = null;
		offense = "0";
		offenseDate = "";
		offenseDesc = "";
		offensesFound = 0;
		offenseArray = null;
		newOffensesArray = null;
		newCaseTypesArray = null;
		newOthersArray = null;
		subCategory = "";

		court = "0";
		location = "";
		facility = "";
		section = "";
		aisle = "";
		row = "";
		shelf = "";
		box = "";
		seqNum = 0;
		courtDesc = "";
		filingDate = "";
		filedType = "0";
		filedTypeDesc = "";
		destroyDate = "";
		caseStyle = "";
		enteredDateTime = "";
		enteredUser = "";
		capias = "0";
		dispositionDate = "";
		dispositionType = "0";
		dispositionTypeDesc = "";
		judgment = "";
		jailedDate = "";
		comments = "";
		notes = "";
		jurySworn = "";
		juryExamined = "";
		warrantOutstanding = "";
		nopp = "";
		caseId = 0;
		sequenceNumber = 0;
		state = "";
		county = "";
		clerk = "";
		caseId = 0;
		causeArray = null;
		caseFound = 0;
		nextCaseId = 0;
		previousCaseId = 0;
		trnNumber = "";
		countyCourt = "";
		//These are DA only:
		agencyCaseNo = "";
		injuredParty = "";
		juvenile = "";
		dateIndicted = "";
		offenseManual = "";
		arrestDate = "";
		judgmentDate = "";
		sentenceDate = "";
		pleaType = "0";
		pleaTypeDesc = "";
		examiningTrial = "";
		onAppeal = "";
		speedyTrial = "";
		existing = "0";
		existingOffense = "0";
		lastName = "";
		firstName = "";
		middleName = "";
		alias = "";
		suffix = "";
		dateOfBirth = "";
		driversLicenseNumber = "";
		driversLicenseExpDate = "";
		dlState = "";
		dlType = "";
		degree = "";

		isDataSubmitted = false;
	}


	// getters/setters
	// Note: to allow getting and setting values
	// in the form
	public String getCaseOffType() {
		return caseOffType;
	}
	public void setCaseOffType(String x) {
		caseOffType = x;
		isDataSubmitted = true;
	}
	/**
	 * returns submitted cause number
	 * <p>
	 * @return cause number as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getCauseNumber() {
		return causeNumber;
	}

	/**
	 * sets cause number
	 * <p>
	 * @param submitted cause number
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setCauseNumber(String x) {
		causeNumber = x;
		isDataSubmitted = true;
	}

	/**
	 * returns year
	 * <p>
	 * @return year as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getYear() {
		return year;
	}


	/**
	 * sets year
	 * <p>
	 * @param new year value
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setYear(String x) {
		year = x;
	}

	/**
	 * returns month
	 * <p>
	 * @return month as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getMonth() {
		return month;
	}


	/**
	 * sets month
	 * <p>
	 * @param new month value
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setMonth(String x) {
		month = x;
	}

	/**
	 * returns submitted case type
	 * <p>
	 * @return case type as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getType() {
		return type;
	}


	/**
	 * sets case type
	 * <p>
	 * @param submitted case type
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setType(String x) {
		type = x;
		isDataSubmitted = true;
	}

	/**
	 * returns submitted court
	 * <p>
	 * @return court as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getCrt() {
		return crt;
	}


	/**
	 * sets court
	 * <p>
	 * @param submitted court
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setCrt(String x) {
		crt = x;
		isDataSubmitted = true;
	}

	/**
	 * returns submitted volume/page
	 * <p>
	 * @return volume/page as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getVolumePage() {
		return volumePage;
	}


	/**
	 * sets volume/page
	 * <p>
	 * @param submitted volume/page
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setVolumePage(String x) {
		volumePage = x;
		//System.out.println("in setVolumePage()");
		isDataSubmitted = true;
	}


	/**
	 * returns submitted case category
	 * <p>
	 * @return case category as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getCaseCategory() {
		return caseCategory;
	}


	/**
	 * sets case category
	 * <p>
	 * @param submitted case category
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setCaseCategory(String x) {
		caseCategory = x;
		isDataSubmitted = true;
	}

	public String getSubCategory(){
		return subCategory;
	}

	public void setSubCategory(String x){
		subCategory = x;
		isDataSubmitted = true;
	}

	/**
	 * returns submitted case type
	 * <p>
	 * @return case type as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getCaseType() {
		return caseType;
	}


	/**
	 * sets case type
	 * <p>
	 * @param submitted case type
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setCaseType(String x) {
		caseType = x;
		//isDataSubmitted = true;
	}

	/**
	 * returns submitted case type description
	 * <p>
	 * @return case type description as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getCaseTypeDesc() {
		return caseTypeDesc;
	}


	/**
	 * sets case type description
	 * <p>
	 * @param submitted case type description
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setCaseTypeDesc(String x) {
		caseTypeDesc = x;
		//isDataSubmitted = true;
	}

	/**
	 * returns number of caseTypes found
	 * <p>
	 * @return number of caseTypes found as <code>int</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public int getCaseTypesFound() {
		return caseTypesFound;
	}

	/**
	 * returns array of CaseType objects
	 * <p>
	 * @return results array as <code><a href="com/CaseType.html>CaseType</a>[]/code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public CaseType[] getCaseTypeArray() {
		return caseTypeArray;
	}

	/**
	 * returns submitted offense
	 * <p>
	 * @return offense as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getOffense() {
		return offense;
	}


	/**
	 * sets offense
	 * <p>
	 * @param submitted offense
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setOffense(String x) {
		offense = x;
		//isDataSubmitted = true;
	}

	/**
	 * returns submitted offense date
	 * <p>
	 * @return offense date as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getOffenseDate() {
		return offenseDate;
	}


	/**
	 * sets offense date
	 * <p>
	 * @param submitted offense date
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setOffenseDate(String x) {
		offenseDate = x;
		isDataSubmitted = true;
	}

	/**
	 * returns submitted offense description
	 * <p>
	 * @return offense description as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getOffenseDesc() {
		return offenseDesc;
	}


	/**
	 * sets offense description
	 * <p>
	 * @param submitted offense description
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setOffenseDesc(String x) {
		offenseDesc = x;
		//isDataSubmitted = true;
	}

	/**
	 * returns number of offenses found
	 * <p>
	 * @return number of offenses found as <code>int</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public int getOffensesFound() {
		return offensesFound;
	}


	/**
	 * returns array of Offense objects
	 * <p>
	 * @return results array as <code><a href="com/Offense.html>Offense</a>[]/code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public Offense[] getOffenseArray() {
		return offenseArray;
	}

	/**
	 * returns submitted court
	 * <p>
	 * @return court as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getCourt() {
		return court;
	}

	/**
	 * sets court
	 * <p>
	 * @param submitted court
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setCourt(String x) {
		court = x;

		isDataSubmitted = true;
	}

	/**
	 * returns array of New Offenses
	 * <p>
	 * @return results array as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String[][] getNewOffensesArray() {
		return newOffensesArray;
	}

	/**
	 * returns array of New Case Types (Civil only)
	 * <p>
	 * @return results array as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String[][] getNewCaseTypesArray() {
		return newCaseTypesArray;
	}

	/**
	 * returns array of New Family or Probate and Guardianship
	 * <p>
	 * @return results array as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String[][] getNewOthersArray() {
		return newOthersArray;
	}

	/**
	 * returns submitted loction
	 * <p>
	 * @return location as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getLocation() {
		return location;
	}


	/**
	 * sets location
	 * <p>
	 * @param submitted location
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setLocation(String x) {
		location = x;
		isDataSubmitted = true;
	}

	/**
	 * returns submitted facility
	 * <p>
	 * @return facility as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getFacility() {
		return facility;
	}


	/**
	 * sets facility
	 * <p>
	 * @param submitted facility
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setFacility(String x) {
		facility = x;
		isDataSubmitted = true;
	}

	/**
	 * returns submitted section
	 * <p>
	 * @return section as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getSection() {
		return section;
	}


	/**
	 * sets section
	 * <p>
	 * @param submitted section
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setSection(String x) {
		section = x;
		isDataSubmitted = true;
	}

	/**
	 * returns submitted aisle
	 * <p>
	 * @return aisle as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getAisle() {
		return aisle;
	}


	/**
	 * sets aisle
	 * <p>
	 * @param submitted aisle
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setAisle(String x) {
		aisle = x;
		isDataSubmitted = true;
	}

	/**
	 * returns submitted row
	 * <p>
	 * @return row as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getRow() {
		return row;
	}


	/**
	 * sets row
	 * <p>
	 * @param submitted row
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setRow(String x) {
		row = x;
		isDataSubmitted = true;
	}

	/**
	 * returns submitted shelf
	 * <p>
	 * @return shelf as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getShelf() {
		return shelf;
	}


	/**
	 * sets shelf
	 * <p>
	 * @param submitted shelf
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setShelf(String x) {
		shelf = x;
		isDataSubmitted = true;
	}

	/**
	 * returns submitted box
	 * <p>
	 * @return box as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getBox() {
		return box;
	}


	/**
	 * sets box
	 * <p>
	 * @param submitted box
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setBox(String x) {
		box = x;
		isDataSubmitted = true;
	}

	/**
	 * returns submitted seqNum
	 * <p>
	 * @return seqNum as <code>int</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public int getSeqNum() {
		return seqNum;
	}


	/**
	 * sets seqNum
	 * <p>
	 * @param submitted seqNum
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setSeqNum(int x) {
		seqNum = x;
		isDataSubmitted = true;
	}

	/**
	 * returns submitted court description
	 * <p>
	 * @return court description as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getCourtDesc() {
		return courtDesc;
	}


	/**
	 * sets court description
	 * <p>
	 * @param submitted court description
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setCourtDesc(String x) {
		courtDesc = x;
		isDataSubmitted = true;
	}

	/**
	 * returns submitted filing date
	 * <p>
	 * @return filing date as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getFilingDate() {
		return filingDate;
	}


	/**
	 * sets filing date
	 * <p>
	 * @param submitted filing date
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setFilingDate(String x) {
		filingDate = x;
		isDataSubmitted = true;
	}

	/**
	 * returns submitted filed type
	 * <p>
	 * @return filed type as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getFiledType() {
		return filedType;
	}


	/**
	 * sets filed type
	 * <p>
	 * @param submitted filed type
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setFiledType(String x) {
		filedType = x;
		isDataSubmitted = true;
	}

	/**
	 * returns submitted filed type desc
	 * <p>
	 * @return filed type desc as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getFiledTypeDesc() {
		return filedTypeDesc;
	}


	/**
	 * sets filed type desc
	 * <p>
	 * @param submitted filed type desc
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setFiledTypeDesc(String x) {
		filedTypeDesc = x;
		isDataSubmitted = true;
	}

	/**
	 * returns submitted destroy date
	 * <p>
	 * @return destroy date as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getDestroyDate() {
		return destroyDate;
	}


	/**
	 * sets destroy date
	 * <p>
	 * @param submitted destroy date
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setDestroyDate(String x) {
		destroyDate = x;
		isDataSubmitted = true;
	}

	/**
	 * returns submitted case style
	 * <p>
	 * @return case style as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getCaseStyle() {
		return caseStyle;
	}


	/**
	 * sets case style
	 * <p>
	 * @param submitted case style
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setCaseStyle(String x) {
		caseStyle = x;
		isDataSubmitted = true;
	}


	/**
	 * returns submitted entered date and time
	 * <p>
	 * @return entered date as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getEnteredDateTime() {
		return enteredDateTime;
	}


	/**
	 * sets entered date and time
	 * <p>
	 * @param submitted entered date and time
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setEnteredDateTime(String x) {
		enteredDateTime = x;
		isDataSubmitted = true;
	}

	/**
	 * returns submitted entered user
	 * <p>
	 * @return entered user as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getEnteredUser() {
		return enteredUser;
	}


	/**
	 * sets user who entered the data
	 * <p>
	 * @param submitted entered user
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setEnteredUser(String x) {
		enteredUser = x;
		isDataSubmitted = true;
	}

	/**
	 * returns submitted capias
	 * <p>
	 * @return capias as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getCapias() {
		return capias;
	}


	/**
	 * sets capias
	 * <p>
	 * @param submitted capias
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setCapias(String x) {
		capias = x;
		isDataSubmitted = true;
	}


	/**
	 * returns submitted disposition date
	 * <p>
	 * @return disposition date as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getDispositionDate() {
		return dispositionDate;
	}


	/**
	 * sets disposition date
	 * <p>
	 * @param submitted disposition date
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setDispositionDate(String x) {
		dispositionDate = x;
		isDataSubmitted = true;
	}


	/**
	 * returns submitted disposition type
	 * <p>
	 * @return disposition type as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getDispositionType() {
		return dispositionType;
	}


	/**
	 * sets disposition type
	 * <p>
	 * @param submitted disposition type
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setDispositionType(String x) {
		dispositionType = x;
		isDataSubmitted = true;
	}

	/**
	 * returns submitted disposition type description
	 * <p>
	 * @return disposition type description as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getDispositionTypeDesc() {
		return dispositionTypeDesc;
	}


	/**
	 * sets disposition type description
	 * <p>
	 * @param submitted disposition type description
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setDispositionTypeDesc(String x) {
		dispositionTypeDesc = x;
		isDataSubmitted = true;
	}


	/**
	 * returns submitted jailed date
	 * <p>
	 * @return jailed date as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getJailedDate() {
		return jailedDate;
	}


	/**
	 * sets jailed date
	 * <p>
	 * @param submitted jailed date
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setJailedDate(String x) {
		jailedDate = x;
		isDataSubmitted = true;
	}


	/**
	 * returns submitted judgment
	 * <p>
	 * @return judgment as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getJudgment() {
		return judgment;
	}


	/**
	 * sets judgment
	 * <p>
	 * @param submitted judgment
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setJudgment(String x) {
		judgment = x;
		isDataSubmitted = true;
	}


	/**
	 * returns submitted comments
	 * <p>
	 * @return comments as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getComments() {
		return comments;
	}


	/**
	 * sets comments
	 * <p>
	 * @param submitted comments
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setComments(String x) {
		comments = x;
		isDataSubmitted = true;
	}


	/**
	 * returns submitted notes
	 * <p>
	 * @return notes as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * sets notes
	 * <p>
	 * @param submitted notes
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setNotes(String x) {
		notes = x;
		isDataSubmitted = true;
	}

	/**
	 * returns submitted JuryExamined
	 * <p>
	 * @return JuryExamined as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getJuryExamined() {
		return juryExamined;
	}

	/**
	 * sets JuryExamined
	 * <p>
	 * @param submitted JuryExamined
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setJuryExamined(String x) {
		juryExamined = x;
		isDataSubmitted = true;
	}

	/**
	 * returns submitted JurySworn
	 * <p>
	 * @return JurySworn as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getJurySworn() {
		return jurySworn;
	}

	/**
	 * sets JurySworn
	 * <p>
	 * @param submitted JurySworn
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setJurySworn(String x) {
		jurySworn = x;
		isDataSubmitted = true;
	}

	/**
	 * returns submitted warrantOutstanding
	 * <p>
	 * @return warrantOutstanding as <code>String</code>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public String getWarrantOutstanding() {
		return warrantOutstanding;
	}

	/**
	 * sets warrantOutstanding
	 * <p>
	 * @param submitted warrantOutstanding
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: </i>
	 */
	public void setWarrantOutstanding(String x) {
		warrantOutstanding = x;
		isDataSubmitted = true;
	}

	public String getNopp() {
		return nopp;
	}

	public void setNopp(String x) {
		nopp = x;
		isDataSubmitted = true;
	}

	public String getAgencyCaseNo() {
		return agencyCaseNo;
	}

	public void setAgencyCaseNo(String x) {
		agencyCaseNo = x;
		isDataSubmitted = true;
	}

	public String getInjuredParty() {
		return injuredParty;
	}

	public void setInjuredParty(String x) {
		injuredParty = x;
		isDataSubmitted = true;
	}

	public String getJuvenile() {
		return juvenile;
	}

	public void setJuvenile(String x) {
		juvenile = x;
		isDataSubmitted = true;
	}

	public String getDateIndicted() {
		return dateIndicted;
	}

	public void setDateIndicted(String x) {
		dateIndicted = x;
		isDataSubmitted = true;
	}

	public String getOffenseManual() {
		return offenseManual;
	}

	public void setOffenseManual(String x) {
		offenseManual = x;
		isDataSubmitted = true;
	}

	public String getArrestDate() {
		return arrestDate;
	}

	public void setArrestDate(String x) {
		arrestDate = x;
		isDataSubmitted = true;
	}

	public String getJudgmentDate() {
		return judgmentDate;
	}

	public void setJudgmentDate(String x) {
		judgmentDate = x;

		isDataSubmitted = true;
	}

	public String getSentenceDate() {
		return sentenceDate;
	}

	public void setSentenceDate(String x) {
		sentenceDate = x;

		isDataSubmitted = true;
	}

	public String getPleaType() {
		return pleaType;
	}

	public void setPleaType(String x) {
		pleaType = x;
		isDataSubmitted = true;
	}

	public String getPleaTypeDesc() {
		return pleaTypeDesc;
	}

	public void setPleaTypeDesc(String x) {
		pleaTypeDesc = x;
		isDataSubmitted = true;
	}

	public String getExaminingTrial() {
		return examiningTrial;
	}

	public void setExaminingTrial(String x) {
		examiningTrial = x;
		//System.out.println("in setexaminingTrial()");
		isDataSubmitted = true;
	}

	public String getOnAppeal() {
		return onAppeal;
	}

	public void setOnAppeal(String x) {
		onAppeal = x;
		isDataSubmitted = true;
	}

	public String getSpeedyTrial() {
		return speedyTrial;
	}

	public void setSpeedyTrial(String x) {
		speedyTrial = x;
		isDataSubmitted = true;
	}

	public void setCountyCourt(String x) {
		countyCourt = x;
		isDataSubmitted = true;
	}

	public String getExisting() {
		return existing;
	}

	public void setExisting(String x) {
		existing = x;
		isDataSubmitted = true;
	}

	public String getExistingOffense() {
		return existingOffense;
	}

	public void setExistingOffense(String x) {
		existingOffense = x;
		isDataSubmitted = true;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String x) {
		lastName = x;
		isDataSubmitted = true;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String x) {
		firstName = x;
		isDataSubmitted = true;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String x) {
		middleName = x;
		isDataSubmitted = true;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String x) {
		alias = x;
		isDataSubmitted = true;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String x) {
		suffix = x;
		isDataSubmitted = true;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String x) {
		dateOfBirth = x;
		isDataSubmitted = true;
	}

	public String getDriversLicenseNumber() {
		return driversLicenseNumber;
	}

	public void setDriversLicenseNumber(String x) {
		driversLicenseNumber = x;

		isDataSubmitted = true;
	}

	public String getDriversLicenseExpDate() {
		return driversLicenseExpDate;
	}

	public void setDriversLicenseExpDate(String x) {
		driversLicenseExpDate = x;
		isDataSubmitted = true;
	}

	public String getDlState() {
		return dlState;
	}

	public void setDlState(String x) {
		dlState = x;
		isDataSubmitted = true;
	}

	public String getDlType() {
		return dlType;
	}

	public void setDlType(String x) {
		dlType = x;
		isDataSubmitted = true;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String x) {
		degree = x;
		isDataSubmitted = true;
	}

	public int getCaseId() {
		return caseId;
	}

	public void setCaseId(int x) {
		caseId = x;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int x) {
		sequenceNumber = x;
	}

	public String getState() {
		return state;
	}

	public void setState(String x) {
		state = x;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String x) {
		county = x;
	}

	public String getClerk() {
		return clerk;
	}

	public void setClerk(String x) {
		clerk = x;
	}

	public String getSealed() {
		return sealed;
	}

	public void setSealed(String x) {
		sealed = x;
	}

	public String getJuryDemand() {
		return juryDemand;
	}

	public void setJuryDemand(String x) {
		juryDemand = x;
	}

	public int getCaseFound() {
		return caseFound;
	}

	public String [] getCauseArray() {
		return causeArray;
	}

	public String getTrnNumber() {
		return trnNumber;
	}

	public void setTrnNumber(String x) {
		trnNumber = x;
		isDataSubmitted = true;
	}

	public int getPreviousCaseId() { return previousCaseId; }
	public int getNextCaseId() { return nextCaseId; }

	public void setPreviousCaseId(int p_case_id) { previousCaseId = p_case_id; }
	public void setNextCaseId(int p_case_id) { nextCaseId = p_case_id; }

	/**
	 * @return the causeNumbersArray
	 */
	public String[][] getCauseNumbersArray() {
		return causeNumbersArray;
	}

	/**
	 * @param causeNumbersArray the causeNumbersArray to set
	 */
	public void setCauseNumbersArray(String[][] causeNumbersArray) {
		this.causeNumbersArray = causeNumbersArray;
	}

	/**
	 * store case information in the database
	 * <p>
	 * @param HTTP request as <code>HttpServletRequest</code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *  <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *  <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *  <li><code><a href="CaseBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: data is stored in the database </i>
	 */
	public int storeCaseData(HttpServletRequest request, String causeNo, String key, String rmsDisp, String districtAttorney, String[][] civilCaseCategory, String pYear) {

		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int rowsInserted = 0;
		int rowsUpdated = 0;
		int tmpCaseId = 0;
		int tmpSequenceNo = 1; // always one for new case
		String fYear = "";
		String fMonth = "";
		String cType = "";

		String cNo = "";
		String caseCauseNo = "";
		String str = "";
		int partyNumber = 0;
		HttpSession session = null;
		HttpSession tmpSession = null;
		int ocaColumn = 0;
		String whereClause = "";
		String subcategory = "";


		String ipAdd = request.getRemoteAddr();

		Connection conn = getConnection(jndiResource);

		if (conn == null)
			return NO_DATABASE_CONNECTION; // no connection

		PreparedStatement insertStmt = null;
		PreparedStatement selectStmt = null;
		PreparedStatement updateStmt = null;
		ResultSet rset = null;

		try {

			// get current session based on HttpServletRequest)
			// and don't create a new on (create=false)
			session = request.getSession(false);

			// save session info in case it needs to be restored
			tmpSession = session;

			// define format used by form
			SimpleDateFormat sdf1 = new SimpleDateFormat ("MM/dd/yyyy");
			// define format for JDBC Timestamp
			SimpleDateFormat sdf2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

			java.util.Date dt = new java.util.Date();

			//System.out.println("Before insert into cases " + year);

			System.out.println("Case Category " + caseCategory);
			if ( (!key.equals("CYC")) && (!key.equals("BMC")) && (!key.equals("AWC") && (!key.equals("GQC")) && (!key.equals("CKC"))
				&& (!key.equals("GXC"))) && (!key.equals("END")) && (!key.equals("ENC")) && (!key.equals("FAC")) && (!key.equals("IY"))) {
				if (caseCategory.equals("F"))
					caseCategory = "V";
			}

			if (causeNo.equals("XXXXXXXX")) {
				System.out.println("This county has autonumbering set up!!"+key);

				//==============================================
				//================DALLAM DISTRICT===============


				if ( key.equals("CDD") ) {
					selectStmt = conn.prepareStatement(
							"SELECT MAX(auto_number)   " +
									"FROM CASES                " +
							"WHERE CASES.category = ? ");

					selectStmt.setString(1, caseCategory);
					rset = selectStmt.executeQuery();

					if (rset.next()) {
						// read max auto_number
						autoNumber = rset.getInt(1)+1;
						System.out.println("Retrieved auto number = " + autoNumber);
					}

					if (caseCategory.equals("C")) {	//criminal formatting
						caseCauseNo = "DCR"+autoNumber;
					} else { //civil or family
						caseCauseNo = "DCV"+autoNumber;
					}
				}


				//==============================================
				//================DALLAM COUNTY=================
				//==============================================
				if ( key.equals("CDC")) {

					if (caseCategory.equals("C")) {	//criminal formatting
						selectStmt = conn.prepareStatement(
								"SELECT MAX(auto_number)   " +
										"FROM CASES                " +
								"WHERE CASES.category = ? ");

						selectStmt.setString(1, caseCategory);
						rset = selectStmt.executeQuery();

						if (rset.next()) {
							// read max auto_number
							autoNumber = rset.getInt(1)+1;
							System.out.println("Retrieved auto number = " + autoNumber);
						}

						caseCauseNo = "CCR"+autoNumber;

					} else { //civil or family
						selectStmt = conn.prepareStatement(
								"SELECT county_oca   " +
										"FROM CODE_TABLE                " +
								"WHERE CODE_TABLE.CODE_TYPE = 'case_type' and code = ?");

						selectStmt.setString(1, caseOffType);
						rset = selectStmt.executeQuery();

						if (rset.next()) {
							// read max auto_number
							ocaColumn = rset.getInt(1);
						}
						// all PROBATES and GUARDIANSHIPS follow diff cause no format
						//CIVIL follow a different cause no format
						//DUE TO OCA being > 100 that gives us those casetypes that are probate or guardianship
						if (ocaColumn >= 100) {
							if (ocaColumn == 104 || ocaColumn == 105) { //guardianship
								whereClause = "WHERE CASES.category = 'V' and not cause_no like 'CCV%' and not cause_no like 'PRO%' and not cause_no like 'PSE%' ";
							} else if ("SE".equals(caseOffType)) { //estate proceedings (PSE)
								whereClause = "WHERE CASES.category = 'V' and cause_no like 'PSE%'  ";
								caseCauseNo = "PSE";
							} else {
								whereClause = "WHERE CASES.category = 'V' and cause_no like 'PRO%' ";
								caseCauseNo = "PRO";
							}
						}else{
							whereClause = "WHERE CASES.category = 'V' and cause_no like 'CCV%' ";
							caseCauseNo = "CCV";
						}
						selectStmt.close();
						rset.close();

						selectStmt = conn.prepareStatement(
								"SELECT MAX(auto_number)   " +
										"FROM CASES                " +
										whereClause +" ");

						System.out.println(whereClause);

						rset = selectStmt.executeQuery();

						if (rset.next()) {
							// read max auto_number
							autoNumber = rset.getInt(1)+1;

							selectStmt.close();
							rset.close();
							selectStmt = conn.prepareStatement(
									"SELECT cause_no FROM CASES " +
											whereClause +
									" AND auto_number = ? ");
							selectStmt.setInt(1, (autoNumber-1));
							rset = selectStmt.executeQuery();
							if (rset.next())
								str = rset.getString(1);

							System.out.println("Retrieved auto number = " + autoNumber);
							System.out.println("Last cause_no was = " + str);
						}
						else {
							autoNumber = 1;
						}

						pYear = sdf1.format(dt);
						fYear = pYear.substring(pYear.length()-4, pYear.length());
						System.out.println("Current year is " + fYear);

						if (ocaColumn == 104 || ocaColumn == 105) { //guardianship
							if (!fYear.equals(str.substring(0,4))) { //last entry not in same year, restart autonumbers
								autoNumber = 1;
								//caseCauseNo = fYear+"-01";
							}
							if ((String.valueOf(autoNumber)).length() == 1) {
								str = "0"+String.valueOf(autoNumber);
								caseCauseNo = fYear+"-"+str;
							} else {
								caseCauseNo = fYear + "-" + autoNumber;
							}



						} else {
							caseCauseNo = caseCauseNo+autoNumber;
						}
						System.out.println("New cause number is " + caseCauseNo);
					}
				}

				//==============================================
				//================  DALLAM JUVY  ===============
				//==============================================
				if ( key.equals("CDJ") ) {
					selectStmt = conn.prepareStatement(
							"SELECT MAX(auto_number)   " +
							"FROM CASES                " );

					rset = selectStmt.executeQuery();

					if (rset.next()) {
						// read max auto_number
						autoNumber = rset.getInt(1)+1;
						System.out.println("Retrieved auto number = " + autoNumber);
					}

					//Removing JUV prefix per Deleala's req FB Case 4276.
					//caseCauseNo = "JUV"+autoNumber;
					caseCauseNo = String.valueOf(autoNumber);

				}

				//==============================================
				//================  OLDHAM CC  =================
				//==============================================
				if ( key.equals("GXC") ) {


					if (caseCategory.equals("F")){
						subcategory = "P";// ONLY FOR THIS COUNTY SO FAR
						caseCategory = "V";
					}
					String subQuery = "";
					if(subcategory.equals("P"))
						subQuery = " AND CASES.cause_no like 'PR-%' ";
					else
						subQuery = " AND not CASES.cause_no like 'PR-%' ";

					selectStmt = conn.prepareStatement(
							"SELECT MAX(auto_number)   " +
									"FROM CASES                " +
									"WHERE CASES.category = ? " + subQuery);

					selectStmt.setString(1, caseCategory);
					rset = selectStmt.executeQuery();

					if (rset.next()) {
						// read max auto_number
						autoNumber = rset.getInt(1)+1;
					}else{
						autoNumber = 1;
					}

					System.out.println("Retrieved auto number = " + autoNumber);
					if(subcategory.equals("P"))
						caseCauseNo = "PR-"+autoNumber;
					else
						caseCauseNo = autoNumber+"";;

				}

				//==============================================
				//================  OLDHAM DC  =================
				//==============================================
				if ( key.equals("GXD") ) {

					int year = Integer.parseInt(filingDate.substring(filingDate.length()-4, filingDate.length())) % 100;
					int month = Integer.parseInt(filingDate.substring(0, 2)) - 1;

					//int year = Calendar.getInstance().get(Calendar.YEAR) % 100;
					//int month = Calendar.getInstance().get(Calendar.MONTH);
					String letter = "";
					String nextCause = "O";//STARTING POINT

					//PICKING THE RIGHT LETTER
					switch(month){
					case 0: letter="A";break;
					case 1: letter="B";break;
					case 2: letter="C";break;
					case 3: letter="D";break;
					case 4: letter="E";break;
					case 5: letter="F";break;
					case 6: letter="G";break;
					case 7: letter="H";break;
					case 8: letter="I";break;
					case 9: letter="J";break;
					case 10: letter="K";break;
					case 11: letter="L";break;
					}

					//FIRST PART OF THE CAUSE NUMBER AFTER YEAR AND MONTH
					if (caseCategory.equals("C")){
						nextCause+="CR-"+year;
					}else{
						nextCause+="CI-"+year;
					}

					selectStmt = conn.prepareStatement(
							"SELECT MAX(CAST(substring(cause_no,9) AS UNSIGNED))   " +
									"FROM CASES                " +
									"WHERE CASES.cause_no like '%"+nextCause+"%' ");

					//System.out.println(selectStmt.toString());

					rset = selectStmt.executeQuery();


					nextCause+=letter+"-";
					if (rset.next()) {
						// read max auto_number
						String currentCause = rset.getString(1);
						if(currentCause != null){// IF NULL = FIRST CASE OF THE MONTH
							autoNumber = Integer.parseInt(currentCause)+1;

							if(autoNumber < 100 && autoNumber >=10)
								nextCause+="0"+autoNumber;
							else if(autoNumber <10)
								nextCause+="00"+autoNumber;
							else
								nextCause+=autoNumber;
						}else{
							nextCause+="001";
						}
					}else{
						nextCause+="001";
					}

					caseCauseNo = nextCause;


					System.out.println("Retrieved auto causeNumber = " + caseCauseNo);
					//Removing JUV prefix per Deleala's req FB Case 4276.
					//caseCauseNo = "JUV"+autoNumber;

				}


				//=========================================================
				//================MOTLEY COUNTY BOTH CC & DC===============
				//=========================================================

				if(key.equals("GQC") || key.equals("GQD")){
					subcategory = "";
					if (caseCategory.equals("F")){
						subcategory = caseCategory;
						caseCategory = "V";
					}
					String subQuery = "";
					if(subcategory.equals("F"))
						subQuery = " AND CASES.subcategory = '"+ subcategory +"'";

					selectStmt = conn.prepareStatement(
							"SELECT MAX(auto_number)   " +
									"FROM CASES                " +
									"WHERE CASES.category = ? " + subQuery);

					selectStmt.setString(1, caseCategory);

					//System.out.println(selectStmt.toString());
					rset = selectStmt.executeQuery();

					if (rset.next()) // read max auto_number
						autoNumber = rset.getInt(1)+1;
					else
						autoNumber = 1;

					System.out.println("Retrieved auto number = " + autoNumber);

					caseCauseNo = ""+autoNumber;

				}

				//=========================================================
				//================KENEDY COUNTY CC===============
				//=========================================================
				if (key.equals("FAC")) {

					String nextCause = "";
					subcategory = "";
					String subQuery = "";
					if (caseCategory.equals("F")){
						subcategory = "P";
						caseCategory = "V";
						subQuery = " AND CASES.subcategory = '"+ subcategory +"'";
						nextCause = "PR-";
					}else{
						subQuery = " AND (CASES.subcategory = '' OR CASES.subcategory is null) ";
					}

					selectStmt = conn.prepareStatement(
							"SELECT MAX(auto_number)   " +
									"FROM CASES                " +
									"WHERE CASES.category = ? " + subQuery);

					selectStmt.setString(1, caseCategory);

					//System.out.println(selectStmt.toString());
					rset = selectStmt.executeQuery();

					if (rset.next()){ // read max auto_number
						nextCause += rset.getInt(1)+1;
						autoNumber = rset.getInt(1)+1;
					}
					else{
						nextCause += 1;
						autoNumber = 1;
					}


					caseCauseNo = nextCause;

				}

				//=========================================================
				//================KENEDY COUNTY DC===============
				//=========================================================
				if (key.equals("FAD")) {

					int year = Integer.parseInt(filingDate.substring(filingDate.length()-4, filingDate.length()));

					String nextCause = "";

					//FIRST PART OF THE CAUSE NUMBER AFTER YEAR
					if (caseCategory.equals("C")){
						nextCause+=year + "-CRF-";
					}else{
						nextCause+=year+"-CV-";
					}

					selectStmt = conn.prepareStatement(
							"SELECT MAX(CAST(substring(cause_no,10) AS UNSIGNED))   " +
									"FROM CASES                " +
									"WHERE CASES.cause_no like '%"+nextCause+"%' ");

					//System.out.println(selectStmt.toString());

					rset = selectStmt.executeQuery();


					if (rset.next()) {
						// read max auto_number
						String currentCause = rset.getString(1);
						if(currentCause != null){// IF NULL = FIRST CASE OF THE YEAR
							autoNumber = Integer.parseInt(currentCause)+1;

							if(autoNumber < 100 && autoNumber >=10)
								nextCause+="0"+autoNumber;
							else if(autoNumber <10)
								nextCause+="00"+autoNumber;
							else
								nextCause+=autoNumber;
						}else{
							nextCause+="001";
						}
					}else{
						nextCause+="001";
					}

					caseCauseNo = nextCause;


					System.out.println("Retrieved auto causeNumber = " + caseCauseNo);
					//Removing JUV prefix per Deleala's req FB Case 4276.
					//caseCauseNo = "JUV"+autoNumber;

					selectStmt.close();
					rset.close();

				}

				//==============================================
				//================KIMBLE DISTRICT=================
				//==============================================
				if (key.equals("FDD")) {
					selectStmt = conn.prepareStatement(
							"SELECT MAX(auto_number)   " +
									"FROM CASES                " +
									"WHERE CASES.category = '" + caseCategory + "' ");

					rset = selectStmt.executeQuery();

					if (rset.next()) {
						// read max auto_number
						autoNumber = rset.getInt(1)+1;
						System.out.println("Retrieved auto number = " + autoNumber);
					}

					cNo = String.valueOf(autoNumber);
					if (cNo.length() == 1) {
						cNo = "000" + cNo;
					}
					if (cNo.length() == 2) {
						cNo = "00" + cNo;
					}
					if (cNo.length() == 3) {
						cNo = "0" + cNo;
					}

					fYear = pYear.substring(pYear.length()-4, pYear.length());
					System.out.println("Current year is " + fYear);

					if (caseCategory.equals("C")) {
						caseCauseNo = fYear + "-DCR-" + cNo;
					}// end if case cat = C

					if (caseCategory.equals("V")) {
						caseCauseNo = "DCV-" + fYear + "-" + cNo;
					}//end if case cat = V

				} //end if key equals FDD
				//==============================================
				//================KIMBLE COUNTY=================
				//==============================================
				if (key.equals("FDC")) {

					if (caseCategory.equals("C")) {	//criminal formatting

						selectStmt = conn.prepareStatement(
								"SELECT MAX(auto_number)   " +
										"FROM CASES                " +
								"WHERE CASES.category = 'C' ");

						rset = selectStmt.executeQuery();

						if (rset.next()) {
							// read max auto_number
							autoNumber = rset.getInt(1)+1;
							System.out.println("Retrieved auto number = " + autoNumber);
						}

						cNo = String.valueOf(autoNumber);
						if (cNo.length() == 1) {
							cNo = "000" + cNo;
						}
						if (cNo.length() == 2) {
							cNo = "00" + cNo;
						}
						if (cNo.length() == 3) {
							cNo = "0" + cNo;
						}

						fYear = pYear.substring(pYear.length()-4, pYear.length());
						System.out.println("Current year is " + fYear);


						caseCauseNo = "CCR-" + fYear + "-" + cNo;
					} else { //civil formatting

						selectStmt = conn.prepareStatement(
								"SELECT county_oca   " +
										"FROM CODE_TABLE                " +
								"WHERE CODE_TABLE.CODE_TYPE = 'case_type' and code = ?");

						selectStmt.setString(1, caseOffType);
						rset = selectStmt.executeQuery();

						if (rset.next()) {
							// read max auto_number
							ocaColumn = rset.getInt(1);
						}
						//KIMBLE needs all PROBATES and GUARDIANSHIPS to follow one cause no format
						//CIVIL follow a different cause no format
						//DUE TO OCA being >100 that gives us those casetypes that are probate or guardianship
						if (ocaColumn >= 100) {
							whereClause = "WHERE CASES.category = 'V' and not cause_no like 'CCV%' ";
						}else{
							whereClause = "WHERE CASES.category = 'V' and cause_no like 'CCV%' ";
						}
						selectStmt.close();
						rset.close();

						selectStmt = conn.prepareStatement(
								"SELECT MAX(auto_number)   " +
										"FROM CASES                " +
										whereClause +" ");

						rset = selectStmt.executeQuery();

						if (rset.next()) {
							// read max auto_number
							autoNumber = rset.getInt(1)+1;
							System.out.println("Retrieved auto number = " + autoNumber);
						}
						if (ocaColumn >= 100) {
							//PROBATES & GUARD have NO PREFIX OR SUFIX, just the sequence autonumber
							caseCauseNo = String.valueOf(autoNumber);
						}else{
							//CIVIL have PREFIX and 4 digit sequence autonumber
							cNo = String.valueOf(autoNumber);
							if (cNo.length() == 1) {
								cNo = "000" + cNo;
							}
							if (cNo.length() == 2) {
								cNo = "00" + cNo;
							}
							if (cNo.length() == 3) {
								cNo = "0" + cNo;
							}
							caseCauseNo = "CCV-"+cNo;
						}
					}//end case cat = V
				} //end if key equals FDC
				//==============================================
				//===================SUTTON DISTRICT============
				//==============================================

				if (key.equals("IJD")) {
					selectStmt = conn.prepareStatement(
							"SELECT MAX(auto_number)   " +
									"FROM CASES                " +
									"WHERE CASES.category = '" + caseCategory + "' ");

					rset = selectStmt.executeQuery();

					if (rset.next()) {
						// read max auto_number
						autoNumber = rset.getInt(1)+1;
						System.out.println("Retrieved IJD auto number = " + autoNumber);
					}

					caseCauseNo = String.valueOf(autoNumber);

				} //end if key equals IJD
				//==============================================
				//================SUTTON COUNTY=================
				//==============================================
				if (key.equals("IJC")) {

					if (caseCategory.equals("C")) {	//criminal formatting

						selectStmt = conn.prepareStatement(
								"SELECT MAX(auto_number)   " +
										"FROM CASES                " +
								"WHERE CASES.category = 'C' ");

						rset = selectStmt.executeQuery();

						if (rset.next()) {
							// read max auto_number
							autoNumber = rset.getInt(1)+1;
							System.out.println("Retrieved ijc auto number = " + autoNumber);
						}

						caseCauseNo = String.valueOf(autoNumber);
					} else { //civil formatting

						selectStmt = conn.prepareStatement(
								"SELECT county_oca   " +
										"FROM CODE_TABLE                " +
								"WHERE CODE_TABLE.CODE_TYPE = 'case_type' and code = ?");

						selectStmt.setString(1, caseOffType);
						rset = selectStmt.executeQuery();

						if (rset.next()) {
							// read max auto_number
							ocaColumn = rset.getInt(1);
						}
						//SUTTON needs all PROBATES and GUARDIANSHIPS to follow one cause no format
						//CIVIL follow a different cause no format
						//DUE TO OCA being >100 that gives us those casetypes that are probate or guardianship
						if (ocaColumn >= 100) {
							whereClause = "where code_table.oca_code >= '100' order by cause_no";
						}else{
							whereClause = "where code_table.oca_code <= '100' order by cause_no; ";
						}
						selectStmt.close();
						rset.close();

						selectStmt = conn.prepareStatement(
								"select max(auto_number) "+
										"from cases inner join (case_types inner join code_table " +
										"on case_types.case_type = code_table.code and code_table.code_type = 'case_type') "+
										"on cases.case_id = case_types.case_id "+
										whereClause +" ");

						rset = selectStmt.executeQuery();

						if (rset.next()) {
							// read max auto_number
							autoNumber = rset.getInt(1)+1;
							System.out.println("Retrieved auto number = " + autoNumber);
						}
						if (ocaColumn >= 100) {
							caseCauseNo = "PR-"+String.valueOf(autoNumber);
						}else{
							//NO PREFIX OR SUFIX, just the sequence autonumber
							caseCauseNo = String.valueOf(autoNumber);
						}
					}//end case cat = V
				} //end if key equals IJC

				//==============================================
				//================JIM WELLS DISTRICT============
				//==============================================

				if (key.equals("JW")) {
					// retrieve max autonumber to generate the cause number

					if (caseCategory.equals("C")) {
						whereClause = "WHERE CASES.category = '" + caseCategory + "' ";
					}else{
						if (caseOffType.equals("TAX")) {
							whereClause = "WHERE CASES.category = '" + caseCategory + "' " +
									"AND CASES.subcategory = '" + caseOffType + "' ";
						} else {
							whereClause = "WHERE CASES.category = '" + caseCategory + "' " +
									"AND CASES.subcategory <> 'TAX' ";
						}
					}

					selectStmt = conn.prepareStatement(
							"SELECT MAX(auto_number)   " +
									"FROM CASES                " +
									whereClause +" ");

					rset = selectStmt.executeQuery();

					if (rset.next()) {
						// read max auto_number
						autoNumber = rset.getInt(1)+1;
						System.out.println("Retrieved auto number = " + autoNumber);
					}

					fYear = pYear.substring(pYear.length()-4, pYear.length());
					fYear = fYear.substring(2,4);
					fMonth = pYear.substring(0, 2);

					if (caseCategory.equals("C"))
						cType = "CR";
					if (caseCategory.equals("V")) {
						if (caseOffType.equals("TAX")) {
							if (((String)session.getAttribute("user_county")).equals("AX"))
								cType = "TX";
							else
								cType = "B";
						} else {
							cType = "CV";
						}
					}

					cNo = String.valueOf(autoNumber);
					if (cNo.length() == 1) {
						cNo = "0000" + cNo;
					}
					if (cNo.length() == 2) {
						cNo = "000" + cNo;
					}
					if (cNo.length() == 3) {
						cNo = "00" + cNo;
					}
					if (cNo.length() == 4) {
						cNo = "0" + cNo;
					}
					if (cNo.length() == 5) {
						cNo = cNo;
					}
					caseCauseNo = fYear + "-" + fMonth + "-" + cNo + "-" + cType;
				} //end of if causeNoFormat = 'JW'

				//==============================================
				//================WILLACY DISTRICT============
				//==============================================
				if (key.equals("WL")) {
					fYear = filingDate.substring(filingDate.length()-4, filingDate.length());
					fMonth = filingDate.substring(filingDate.length()-7, filingDate.length()-5);

					// retrieve max autonumber to generaye the cause number
					str =
							"SELECT max(auto_number)"
									+ " from cases"
									+ " where cases.category='" + caseCategory + "'"
									+ " and cases.cause_no like '" + fYear + "%'";
					System.out.println("WL Search for max cause number: '" + str + "'");
					selectStmt = conn.prepareStatement(str);

					rset = selectStmt.executeQuery();

					if (rset.next()) {
						// read max auto_number
						autoNumber = rset.getInt(1)+1;
						System.out.println("Retrieved auto number = " + autoNumber);
					}

					if (caseCategory.equals("C"))
						cType = "CR";
					if (caseCategory.equals("V")) {
						cType = "CV";
					}
					cNo = String.valueOf(autoNumber);
					if (cNo.length() == 1) {
						cNo = "000" + cNo;
					}
					if (cNo.length() == 2) {
						cNo = "00" + cNo;
					}
					if (cNo.length() == 3) {
						cNo = "0" + cNo;
					}
					if (cNo.length() == 4) {
						cNo = cNo;
					}

					String courtDesc = "";
					if ("0197".equals(court))
						courtDesc = "A";

					else if ("0107".equals(court))
						courtDesc = "B";

					else if ("0103".equals(court))
						courtDesc = "C";

					else if ("0357".equals(court))
						courtDesc = "D";

					else if ("0404".equals(court))
						courtDesc = "E";

					else if ("0138".equals(court))
						courtDesc = "G";

					caseCauseNo = fYear + "-" + cType + "-" + cNo + "-" + courtDesc;
				} //end of if causeNoFormat = 'WL'

				//==================================================
				//====================WILLACY COUNTY================
				//==================================================
				if(key.equals("JKC")){
					fYear = pYear.substring(pYear.length()-4, pYear.length());

					if(caseCategory.equals("P")){
						caseCategory = "V";
						subCategory = "PRB";
						whereClause = "WHERE CASES.category = '" + caseCategory + "' " +
								"AND CASES.cause_no like '" + fYear + "-PRB%'";
					}
					else if(caseCategory.equals("G")){
						caseCategory = "V";
						subCategory = "GDN";
						whereClause = "WHERE CASES.category = '" + caseCategory + "' " +
								"AND CASES.cause_no like '" + fYear + "-GDN%'";
					}
					else if(caseCategory.equals("C")){
						whereClause = "WHERE CASES.category = '" + caseCategory + "' " +
								"AND CASES.cause_no like '" + fYear + "-CCR%'";
					}
					else{
						whereClause = "WHERE CASES.category = '" + caseCategory + "' " +
								"AND CASES.cause_no like '" + fYear + "-CCV%'";
					}

					selectStmt = conn.prepareStatement(
							"SELECT MAX(auto_number)   " +
									"FROM CASES  " + whereClause);

					rset = selectStmt.executeQuery();
					System.out.print(selectStmt);

					if (rset.next()) {
						// read max auto_number
						autoNumber = rset.getInt(1)+1;
					}
					else{
						autoNumber = 1;
					}

					System.out.println("Retrieved auto number = " + autoNumber);

					if(caseCategory.equals("C")){
						cType = "CCR";
					}
					else if(caseCategory.equals("V")){
						if(subCategory.equals("PRB")){
							cType = "PRB";
						}
						else if(subCategory.equals("GDN")){
							cType = "GDN";
						}
						else{
							cType = "CCV";
						}
					}

					cNo = String.valueOf(autoNumber);
					if (cNo.length() == 1) {
						cNo = "000" + cNo;
					}
					if (cNo.length() == 2) {
						cNo = "00" + cNo;
					}
					if (cNo.length() == 3) {
						cNo = "0" + cNo;
					}
					if (cNo.length() == 4) {
						cNo = cNo;
					}

					caseCauseNo = fYear + "-" + cType + "-" + cNo;
				}

				//==================================================
				//=====================BROOKS COUNTY================
				//==================================================
				if (key.equals("AXC")) {
					// retrieve max autonumber to generate the cause number
					if (caseCategory.equals("C")) {
						selectStmt = conn.prepareStatement(
								"SELECT MAX(auto_number)   " +
										"FROM CASES                " +
										"WHERE CASES.category = '" + caseCategory + "' ");
					}
					if (caseCategory.equals("V")) {
						if ( (caseOffType.substring(0,2)).equals("PR") ){
							selectStmt = conn.prepareStatement(
									"SELECT MAX(auto_number)   						" +
											"FROM CASES                						" +
											"WHERE CASES.category ='" + caseCategory + "'	" +
									"AND cases.cause_no like '%PR%' ");
						} else {
							if ( (caseOffType.substring(0,2)).equals("GU") ){
								selectStmt = conn.prepareStatement(
										"SELECT MAX(auto_number)   					" +
												"FROM CASES, CASE_TYPES                		" +
												"WHERE CASES.CASE_ID = CASE_TYPES.CASE_ID   " +
												"AND CASES.category ='" + caseCategory + "'	" +
										"AND CASE_TYPES.CASE_TYPE LIKE 'GU%'		");
							} else {
								selectStmt = conn.prepareStatement(
										"SELECT MAX(auto_number)   " +
												"FROM CASES                " +
												"WHERE CASES.category = '" + caseCategory + "' "+
										"AND CASES.CAUSE_NO LIKE '%CV%'");
							}
						}
					}

					rset = selectStmt.executeQuery();

					if (rset.next()) {
						// read max auto_number
						autoNumber = rset.getInt(1)+1;
						System.out.println("Retrieved auto number = " + autoNumber);
					}

					if (caseCategory.equals("C"))
						cType = "CR";
					if (caseCategory.equals("V")) {
						if ( (caseOffType.substring(0,2)).equals("PR") )
							cType = "PR";
						else if ( (caseOffType.substring(0,2)).equals("GU") )
							cType = "";
						else
							cType = "CV";
					}

					cNo = String.valueOf(autoNumber);
					if (cNo.length() == 1) {
						cNo = "000" + cNo;
					}
					if (cNo.length() == 2) {
						cNo = "00" + cNo;
					}
					if (cNo.length() == 3) {
						cNo = "0" + cNo;
					}
					if (cNo.length() == 4) {
						cNo = cNo;
					}

					//no suffix for guardianship, or criminal cases
					if (caseCategory.equals("C") || (caseCategory.equals("V") && cType.equals("")))
						caseCauseNo = cNo;
					else
						caseCauseNo = cNo + "-" +cType;
				}//end of if causeNoFormat = 'AXC'

				//=================================================
				//==================VAL VERDE DC ==================
				//=================================================
				if (key.equals("IY")) {

					//IF STATEMET TO SEE IF FAMILY CASE BELONGS TO COUNTY COURT AT LAW WITH A RANDOM NUMBER
					if (caseCategory.equals("F")){
						Random rn = new Random();
						int max = 2;
						int min = 1;
						int range = max - min + 1;
						int randomNum =  rn.nextInt(range) + min;

						if(randomNum == 1)
							countyCourt = "Y";
						else
							countyCourt = "N";

						caseCategory = "V";
					}else{
						countyCourt = "N";
					}
					// retrieve max autonumber to generate the cause number
					if (caseCategory.equals("V")) {

						if (countyCourt.equals("Y")) {
							selectStmt = conn.prepareStatement(
									"SELECT MAX(left(cause_no,4))   						" +
											"FROM CASES                						" +
											"WHERE CASES.category ='V'	" +
									"AND cases.court = 'IY01' and cases.cause_no like '%CCL' and (cases.cause_no)>7 ");
							cType = "CCL";
							court = "IY01";

							rset = selectStmt.executeQuery();

							if (rset.next()) {
								// read max auto_number
								autoNumber = rset.getInt(1)+1;
								System.out.println("Retrieved auto number = " + autoNumber);
							}
						}
						else { //not county court
							selectStmt = conn.prepareStatement(
									"SELECT MAX(cause_no)						" +
											"FROM CASES                						" +
											"WHERE CASES.category ='V'	" +
									"AND NOT CASES.court = 'IY01' and length(cause_no)>=5  and cases.cause_no NOT REGEXP '[a-z]' ");

							rset = selectStmt.executeQuery();

							if (rset.next()) {
								// read max auto_number
								autoNumber = rset.getInt(1);
								System.out.println("Last case auto_number not county court = " + autoNumber);
							}
							//find out which court the last case went into
							selectStmt = conn.prepareStatement(
									"SELECT case_id, court FROM CASES WHERE cause_no = " + autoNumber +
									" and CASES.category ='V'" +
									"AND NOT CASES.court = 'IY01' and length(cause_no)>=5  and cases.cause_no NOT REGEXP '[a-z]' ");

							rset = selectStmt.executeQuery();
							if (rset.next()) {
								System.out.println("Last case_id = "+String.valueOf(rset.getInt(1)));
								System.out.println("Last case court = "+rset.getString(2));
								if (rset.getString(2).equals("0083"))
									court = "0063";
								else
									court = "0083";
								autoNumber++; //increment to set new cause_no
							}

						}
					}
					if (caseCategory.equals("C")) {

						int lastNoBill = 0;
						String lastCauseNo = "";
						String cYear;
						String cMonth;
						fYear = filingDate.substring(filingDate.length()-2, filingDate.length());
						fMonth = filingDate.substring(0,2);
						//08/01/2010
						//System.out.println("Current year is " + fYear);
						//System.out.println("Current month is " + fMonth);
						int mod = (Integer.parseInt(fMonth) % 2);
						//System.out.println("fMonth % 2 = "+mod+" (mod)");
						//mod is 0 or 1
						if (mod == 1)//Jan, Mar, May, Jul, Sep, Nov
							court = "0063";
						else 		 //Feb, Apr, Jun, Aug, Oct, Dec
							court = "0083";

						offense = request.getParameter("caseOffType");

						if (offense.equals("26")) { //NO BILL OFFENSE

							selectStmt = conn.prepareStatement(
									"SELECT MAX(case_id)				" +
											"FROM CASES WHERE cause_no 			" +
									"LIKE '%N%'							" );

							rset = selectStmt.executeQuery();

							if (rset.next()) {
								//read case_id of last NO BILL entered
								lastNoBill = rset.getInt(1);
								System.out.println("last case_id entered as NO BILL was "+lastNoBill);
							}

							selectStmt.close();
							rset.close();
							selectStmt = conn.prepareStatement(
									"SELECT cause_no, auto_number FROM CASES " +
											"WHERE case_id = "+lastNoBill );

							rset = selectStmt.executeQuery();
							if (rset.next()) {
								lastCauseNo = rset.getString(1);
								autoNumber = rset.getInt(2);
							}
							/*
								cause_no in following format:
								"N + (2 digit month) + (2 digit year) + (2 digit autoNumber)"
								read last cause_no, compare month & date
								then determine next or new seq#
							 */
							cYear = lastCauseNo.substring(3,5);
							cMonth = lastCauseNo.substring(1,3);
							//System.out.println("Year="+cYear+" Month="+cMonth);

							if ((cYear.equals(fYear)) && (cMonth.equals(fMonth))){
								//cSeqNo = lastCauseNo.substring(5, 7);
								System.out.println("new case and prev case in same month && year");
							} else {
								autoNumber = 0;
								System.out.println("not in same month/year!!");
							}
							autoNumber++;
							cType = "N"+fMonth+fYear;
						}
						else { //offense not 26
							selectStmt = conn.prepareStatement(
									"SELECT MAX(left(cause_no,5))						" +
											"FROM CASES                						" +
											"WHERE CASES.category ='C'	" +
											"AND cause_no LIKE '%CR' " +
											"AND length(cause_no)>=7 ");
							rset = selectStmt.executeQuery();
							if (rset.next()) {
								// read max auto_number
								autoNumber = rset.getInt(1)+1;
								System.out.println("Last case auto_number CR = " + autoNumber);
							}
							cType = "CR";
						}
					}

					cNo = String.valueOf(autoNumber);

					if ((caseCategory.equals("C")) && (offense.equals("26"))) {
						if (cNo.length()<2)
							cNo = "0"+cNo;
						caseCauseNo = cType + cNo; //backwards for "Nxxxxxx" series
					}
					else
						caseCauseNo = cNo + cType;

				}//end if causeNoFormat = 'IY'

				//===================================================
				//======================FLOYD COUNTY=================
				//===================================================

				if (key.equals("CYC")) {
					if (caseCategory.equals("F")) {

						//Probate Cases

						selectStmt = conn.prepareStatement(
								"SELECT MAX(auto_number)   						" +
										"FROM CASES                						" +
										"WHERE CASES.category ='V'	" +
								"AND CASES.subcategory = 'P' ");

						rset = selectStmt.executeQuery();

						if (rset.next()) {
							// read max auto_number
							autoNumber = rset.getInt(1)+1;
							System.out.println("Retrieved auto number = " + autoNumber);
							caseCauseNo = String.valueOf(autoNumber);
							subcategory = "P";
						}
					}
					if (caseCategory.equals("V")){
						//non probate cases
						selectStmt = conn.prepareStatement(
								"SELECT MAX(auto_number)   						" +
										"FROM CASES                						" +
										"WHERE CASES.category ='V'	" +
								"AND (CASES.subcategory IS NULL OR CASES.subcategory = '') ");

						rset = selectStmt.executeQuery();

						if (rset.next()) {
							// read max auto_number
							autoNumber = rset.getInt(1)+1;
							System.out.println("Retrieved auto number = " + autoNumber);
							caseCauseNo = String.valueOf(autoNumber);
						}

					}
					if (caseCategory.equals("C")) { //caseCategory = 'C'
						selectStmt = conn.prepareStatement(
								"SELECT MAX(auto_number)   						" +
										"FROM CASES                						" +
								"WHERE CASES.category ='C'	");

						rset = selectStmt.executeQuery();

						if (rset.next()) {
							// read max auto_number
							autoNumber = rset.getInt(1)+1;
							System.out.println("Retrieved auto number = " + autoNumber);
							caseCauseNo = String.valueOf(autoNumber);
						}

					}
					if (caseCategory.equals("F"))
						caseCategory = "V";
				}
				//=======================================================
				//================STARR COUNTY DISTRICT CLERK============
				//=======================================================

				if (key.equals("IFD")) {
					boolean randomFlag = false;
					int courtCount = 0;
					if (court.equalsIgnoreCase("RANDOM")) {
						try{
							randomFlag = true;
							//System.out.println("Need to generate random court for Starr DC!");
							Random generator = new Random();
							int a = generator.nextInt(100)+1;//+1 will give us 1-100 (inclusive)
							if (a % 2 == 0)
								court = "0229";
							else
								court = "0381";
							System.out.println("randomly selected court is " + court);
						}catch(Exception e){
							court = "0381";
							System.out.println("randomly selected court is " + court);
							this.exceptionEmail.emailException(request, e);
						}
					}
					fYear = filingDate.substring(filingDate.length()-2, filingDate.length());
					if (caseCategory.equals("C")) { //caseCategory = 'C'
						if (selectStmt != null)
							selectStmt.close();
						if (rset != null)
							rset.close();

						whereClause = "WHERE CASES.category ='C' 			" +
								"and LEFT(CAUSE_NO,2)='"+fYear+"' 	";

						selectStmt = conn.prepareStatement(
								"SELECT MAX(auto_number) " +
										"FROM CASES 			 " +
										whereClause +
										"AND COURT = '" + court + "' ");


						rset = selectStmt.executeQuery();

						if (rset.next()) {
							// read max auto_number
							// CRIMINAL CASES
							autoNumber = courtCount = rset.getInt(1);
							//System.out.println("Retrieved next auto number to be given in Starr DC= " + autoNumber);
						} else {
							if (court.equals("0229"))
								autoNumber = 2;
							else
								autoNumber = 1;
						}

						if (randomFlag) {
							//sets global variables court and autoNumber
							//to the court w/least number of cases
							//if difference is of more than 3 cases
							selectLowestCourt(selectStmt, rset, conn,
									courtCount, fYear, whereClause);
						}

						if (court.equals("0229")) {
							cType = "-CRS-";
						}
						else {
							cType = "-CR-";
						}

						caseCauseNo = fYear + cType +String.valueOf(autoNumber);
						//System.out.println("This is the criminal cause number for this:"+caseCauseNo);

					}
					if (court.equalsIgnoreCase("CS")) {
						//System.out.println("Need to generate 1 of 3 courts for a ATTY GEN CASE IN Starr DC!");
						if (selectStmt != null)
							selectStmt.close();
						if (rset != null)
							rset.close();
						selectStmt = conn.prepareStatement(
								"SELECT MAX(auto_number)				" +
										"FROM CASES                					" +
										"WHERE CASES.category ='V' 					" +
										"AND LEFT(CAUSE_NO,2)='CS' 					" +
										"AND RIGHT(LEFT(CAUSE_NO,5),2)='"+fYear+"'  ");

						rset = selectStmt.executeQuery();

						if (rset.next()) {
							// read max auto_number
							autoNumber = rset.getInt(1);
							//System.out.println("Retrieved next auto number to be given in CS- sequence Starr DC= " + autoNumber);

						} else {
							autoNumber = 1;
						}
						if (selectStmt != null)
							selectStmt.close();
						if (rset != null)
							rset.close();
						selectStmt = conn.prepareStatement(
								"SELECT court				" +
										"FROM CASES                					" +
										"WHERE CASES.category ='V' 					" +
										"AND LEFT(CAUSE_NO,2)='CS' 					" +
										"AND RIGHT(LEFT(CAUSE_NO,5),2)='"+fYear+"'  AND auto_number='"+autoNumber+"' ");

						rset = selectStmt.executeQuery();

						if (rset.next()) {
							// get court
							court =rset.getString(1);
						}

						if(court.equals("CS"))
							court = "0381";
						//removed per Brenda's req. CCL not in the District Court options from efiling...
						else if (court.equals("0381"))
							court = "0229";
						//else if (court.equals("IF00"))
						//	court = "0229";
						else if (court.equals("0229"))
							court = "0381";

						cType="CS-";
						++autoNumber;
						caseCauseNo = cType + fYear + "-" +String.valueOf(autoNumber);

					} else {
						//not CS and not criminal
						if (caseCategory.equals("V")) { //caseCategory = 'V'
							if (court.equals("IF00")){
								if (selectStmt != null)
									selectStmt.close();
								if (rset != null)
									rset.close();

								selectStmt = conn.prepareStatement(
										"SELECT MAX(auto_number)   						" +
												"FROM CASES                						" +
												"WHERE CASES.category='V' " +
												"AND RIGHT(LEFT(CAUSE_NO,5),2)='"+fYear+"'  " +
										"AND LEFT(CAUSE_NO,2)='CC'   ");

								rset = selectStmt.executeQuery();

								if (rset.next()) {
									// read max auto_number
									autoNumber = rset.getInt(1)+1;
									//System.out.println("Retrieved next auto number to be given in CIVIL CCL Starr DC= " + autoNumber);
								}
								cType="CC-";
								caseCauseNo = cType + fYear + "-" +String.valueOf(autoNumber);
							}else {

								if (caseOffType.equals("TAX")) {

									whereClause = "WHERE CASES.category='V' " 					  +
											"AND RIGHT(LEFT(CAUSE_NO,5),2)='"+fYear+"'  	" +
											"AND CASES.subcategory = '" + caseOffType + "' " +
											"AND LEFT(CAUSE_NO,2)='TS'  ";
									cType="TS-";
								} else {
									whereClause = "WHERE CASES.category='V' " 					  +
											"AND RIGHT(LEFT(CAUSE_NO,5),2)='"+fYear+"'  	" +
											"AND CASES.subcategory <> 'TAX' 	" +
											"AND LEFT(CAUSE_NO,2)='DC' ";
									cType="DC-";
								}
								if (selectStmt != null)
									selectStmt.close();
								if (rset != null)
									rset.close();

								//find max(auto_number) for court1
								String sql =
										"SELECT MAX(auto_number)   						" +
												"FROM CASES                						" +
												whereClause + " " +
												"AND COURT = '"+court+"'" ;

								//System.out.println(sql);
								selectStmt = conn.prepareStatement(sql);
								rset = selectStmt.executeQuery();

								if (rset.next()) {
									// read max auto_number
									// CIVIL CASES
									autoNumber = courtCount = rset.getInt(1);
									if (randomFlag == false)
										autoNumber = courtCount + 2;
									//System.out.println("Retrieved next auto number to be given in CIVIL Starr DC= " + autoNumber);
								} else {
									if (court.equals("0229"))
										autoNumber = 2;
									else
										autoNumber = 1;
								}

								if (randomFlag) {
									//sets global variables court and autoNumber
									//to the court w/least number of cases
									//if difference is of more than 3 cases
									selectLowestCourt(selectStmt, rset, conn,
											courtCount, fYear, whereClause);
								}

								caseCauseNo = cType + fYear + "-" +String.valueOf(autoNumber);
								System.out.println("This is the cause number for TS or DC :"+caseCauseNo);
							}
						}
					}
				}

				//=======================================================
				//================   CLAY COUNTY CLERK   ================
				//=======================================================

				if (key.equals("BMC")) {
					String lastCauseNo = "";
					String cYear = "";
					boolean caseIsGuardianship = false;
					fYear = filingDate.substring(filingDate.length()-2, filingDate.length());
					String guardWhereClause = "";

					if (caseCategory.equals("F")) {
						/*
						 * Read county_oca from code_table to determine
						 * if Probate or Guardianship 101, 102, 103 and 106 are probate
						 * 104 and 105 are Guardianship cases
						 */

						selectStmt = conn.prepareStatement(
								"SELECT county_oca   " +
										"FROM CODE_TABLE                " +
								"WHERE CODE_TABLE.CODE_TYPE = 'case_type' and code = ?");

						selectStmt.setString(1, caseOffType);
						rset = selectStmt.executeQuery();

						if (rset.next()) {
							// read county_oca
							ocaColumn = rset.getInt(1);
						}
						//System.out.println("county_oca code is "+ ocaColumn);
						if (ocaColumn == 104 || ocaColumn == 105) {
							//System.out.println("Entering Guardianship case");
							caseIsGuardianship = true;
							/*
							 * whereClause explained: sequence depends on file date
							 * for 2011, fYear = 11 if none found, means that the year changed
							 * and we need to restar sequence to 1
							 * by same token, any December case not filed on time can be
							 * entered in January of the following year and it will not
							 * alter either sequence -re
							 */
							whereClause = " AND SUBSTR(cause_no, LOCATE('-', cause_no)+1, 2) = '" +
									fYear + "' AND RIGHT(cause_no, 2) = '-G' ";
						}
						else if (ocaColumn == 101 || ocaColumn == 102 || ocaColumn == 103 || ocaColumn == 106) {
							//System.out.println("Entering Probate case");
							cType = "PB-";
							whereClause = " AND LEFT(cause_no, 3) = 'PB-' ";
						}
						else {
							//unknown - invalid county_oca column Cannot Proceed!!
							returnCode = OPERATION_FAILED;
							needToRollback = true;
						}

						caseCategory = "V";

						if (selectStmt != null)
							selectStmt.close();
						if (rset != null)
							rset.close();

					} else {
						//civil or criminal
						if (caseCategory.equals("V")) {
							//civil
							cType = "CV-";
							whereClause = " AND LEFT(cause_no, 3) = 'CV-' ";
						}

						if (caseCategory.equals("C")) {
							//criminal
							cType = "CR-";
							whereClause = "";
						}
					}
					selectStmt = conn.prepareStatement(
							"SELECT cause_no, MAX(auto_number)   " +
									"FROM CASES                " +
									"WHERE CASES.category = '" + caseCategory + "' "+
									whereClause +
							" GROUP BY category "); //added because when u have cause_no u need to group by on MIN/MAX functions

					rset = selectStmt.executeQuery();

					if (rset.next()) {
						// read max auto_number
						lastCauseNo = rset.getString(1);
						autoNumber = rset.getInt(2)+1;
						System.out.println("Retrieved auto number = " + autoNumber);
					} else
						autoNumber = 1;

					if (caseIsGuardianship) {
						if (autoNumber != 1) { //found match on lookup (lastCauseNo not empty)
							//System.out.println("tacking on another # to seq for year " + fYear);
							//System.out.println("lastCauseNo = " + lastCauseNo);
							cYear = lastCauseNo.substring(lastCauseNo.indexOf("-")+1 ,lastCauseNo.indexOf("-")+3);
							//System.out.println("cYear is " + cYear);
							//year changed, first case of new year
							if (Integer.parseInt(fYear) > Integer.parseInt(cYear))
								autoNumber = 1;
						}
						caseCauseNo = String.valueOf(autoNumber) + "-" + fYear + "-G";

					} else
						caseCauseNo = cType + String.valueOf(autoNumber);

				}

				//=======================================================
				//================COLLINSWORTH COUNTY CLERK============
				//=======================================================
				if (key.equals("BRC")) {

					int maxCrim = 0, maxCivil = 0;

					if (selectStmt != null)
						selectStmt.close();
					if (rset != null)
						rset.close();


					//Probate/Guardianship/Small Estate
					if (caseCategory.equals("F")) {
						selectStmt = conn.prepareStatement(
								"select max(auto_number) " +
										"FROM cases inner join case_types on cases.case_id = case_types.case_id " +
										"inner join code_table on case_types.case_type = code_table.code " +
										"where code_type = 'case_type' " +
										"AND cases.category = 'V' "+
										"and county_oca > 100;"
								);
						rset = selectStmt.executeQuery();
						if (rset.next())
							maxCivil = rset.getInt(1);

						autoNumber = maxCivil+1;
					}
					else {
						//all other civil & criminal

						//read highest criminal
						selectStmt = conn.prepareStatement(
								"select max(auto_number) " +
										"FROM cases inner join case_types on cases.case_id = case_types.case_id " +
										"inner join code_table on case_types.case_type = code_table.code " +
										"where code_type = 'case_type' " +
										"AND cases.category = 'V' "+
										"and county_oca < 100;"
								);
						rset = selectStmt.executeQuery();
						if (rset.next())
							maxCivil = rset.getInt(1);

						selectStmt.close();
						rset.close();

						//read highest criminal
						selectStmt = conn.prepareStatement(
								"select max(auto_number) from cases where category = 'C'"
								);
						rset = selectStmt.executeQuery();
						if (rset.next())
							maxCrim = rset.getInt(1);

						if (maxCrim > maxCivil)
							autoNumber = maxCrim+1;
						else
							autoNumber = maxCivil+1;

					}

					caseCauseNo = String.valueOf(autoNumber);
					if (caseCategory.equals("F"))
						caseCategory = "V";
				}

				//=======================================================
				//================COLLINGSWORTH DISTRICT CLERK============
				//=======================================================
				if (key.equals("BRD")) {

					if (selectStmt != null)
						selectStmt.close();
					if (rset != null)
						rset.close();
					selectStmt = conn.prepareStatement(
							"SELECT MAX(auto_number)				" +
									"FROM CASES                					" +
							"WHERE CASES.category = ? 					");
					//"AND CAUSE_NO LIKE ? 					");

					//civil or criminal
					if (caseCategory.equals("C")) {
						//criminal
						//cType = "DWIF-";
						selectStmt.setString(1, "C");
						//selectStmt.setString(2, cType+"%");
					}
					else {
						//civil
						//cType = "DIVN-";
						selectStmt.setString(1, "V");
						//selectStmt.setString(2, cType+"%");
					}
					rset = selectStmt.executeQuery();

					if (rset.next())
						autoNumber = rset.getInt(1)+1;

					//fYear = filingDate.substring(filingDate.length()-2, filingDate.length());
					caseCauseNo = String.valueOf(autoNumber);
				}


				//=======================================================
				//================BRISCOE COUNTY CLERK============
				//=======================================================
				if (key.equals("AWC")) {

					int maxCrim = 0, maxCivil = 0;

					if (selectStmt != null)
						selectStmt.close();
					if (rset != null)
						rset.close();


					//Probate/Guardianship/Small Estate
					if (caseCategory.equals("F")) {
						selectStmt = conn.prepareStatement(
								"select max(auto_number) " +
										"FROM cases inner join case_types on cases.case_id = case_types.case_id " +
										"inner join code_table on case_types.case_type = code_table.code " +
										"where code_type = 'case_type' " +
										"AND cases.category = 'V' "+
										"and county_oca > 100;"
								);
						rset = selectStmt.executeQuery();
						if (rset.next())
							maxCivil = rset.getInt(1);

						autoNumber = maxCivil+1;
					}
					else {
						//all other civil & criminal

						//read highest criminal
						selectStmt = conn.prepareStatement(
								"select max(auto_number) " +
										"FROM cases inner join case_types on cases.case_id = case_types.case_id " +
										"inner join code_table on case_types.case_type = code_table.code " +
										"where code_type = 'case_type' " +
										"AND cases.category = 'V' "+
										"and county_oca < 100;"
								);
						rset = selectStmt.executeQuery();
						if (rset.next())
							maxCivil = rset.getInt(1);

						selectStmt.close();
						rset.close();

						//read highest criminal
						selectStmt = conn.prepareStatement(
								"select max(auto_number) from cases where category = 'C'"
								);
						rset = selectStmt.executeQuery();
						if (rset.next())
							maxCrim = rset.getInt(1);

						if (maxCrim > maxCivil)
							autoNumber = maxCrim+1;
						else
							autoNumber = maxCivil+1;

					}

					caseCauseNo = String.valueOf(autoNumber);
					if (caseCategory.equals("F"))
						caseCategory = "V";
				}

				//=======================================================
				//================BRISCOE DISTRICT CLERK============
				//=======================================================
				if (key.equals("AWD")) {

					if (selectStmt != null)
						selectStmt.close();
					if (rset != null)
						rset.close();
					selectStmt = conn.prepareStatement(
							"SELECT MAX(auto_number)				" +
									"FROM CASES                					" +
							"WHERE CASES.category = ? 					");
					//"AND CAUSE_NO LIKE ? 					");

					//civil or criminal
					if (caseCategory.equals("C")) {
						//criminal
						//cType = "DWIF-";
						selectStmt.setString(1, "C");
						//selectStmt.setString(2, cType+"%");
					}
					else {
						//civil
						//cType = "DIVN-";
						selectStmt.setString(1, "V");
						//selectStmt.setString(2, cType+"%");
					}
					rset = selectStmt.executeQuery();

					if (rset.next())
						autoNumber = rset.getInt(1)+1;

					//fYear = filingDate.substring(filingDate.length()-2, filingDate.length());
					caseCauseNo = String.valueOf(autoNumber);
				}

				//=============================================
				//===============IRION DISTRICT================
				//=============================================
				if(key.equals("END")){

					fYear = pYear.substring(pYear.length()-4, pYear.length());
					System.out.println("Current year is " + fYear);
					fYear = fYear.substring(2,4);
					fMonth = pYear.substring(0, 2);
					System.out.println("Current month is " + fMonth);

					if(caseCategory.equals("F")){
						caseCategory = "V";
						subCategory = "F";
						whereClause = "WHERE CASES.category = '" + caseCategory + "' " +
								"AND CASES.cause_no like 'F" + fYear + "%'";
					}
					else if(caseCategory.equals("C")){
						whereClause = "WHERE CASES.category = '" + caseCategory + "' " +
								"AND CASES.cause_no like 'CR" + fYear + "%'";
					}
					else{
						whereClause = "WHERE CASES.category = '" + caseCategory + "' " +
								"AND CASES.cause_no like 'CV" + fYear + "%'";
					}

					selectStmt = conn.prepareStatement(
							"SELECT MAX(auto_number)   " +
									"FROM CASES  " + whereClause);

					rset = selectStmt.executeQuery();
					System.out.print(selectStmt);
					if (rset.next()) {
						// read max auto_number
						autoNumber = rset.getInt(1)+1;
					}
					else{
						autoNumber = 1;
					}
					System.out.println("Retrieved auto number = " + autoNumber);

					fYear = pYear.substring(pYear.length()-4, pYear.length());
					System.out.println("Current year is " + fYear);
					fYear = fYear.substring(2,4);
					fMonth = pYear.substring(0, 2);
					System.out.println("Current month is " + fMonth);

					if(caseCategory.equals("C")){
						cType = "CR";
					}
					if(caseCategory.equals("V")){
						if(subCategory.equals("F")){
							cType = "F";
						}
						else{
							cType = "CV";
						}
					}

					cNo = String.valueOf(autoNumber);
					if (cNo.length() == 1) {
						cNo = "00" + cNo;
					}
					if (cNo.length() == 2) {
						cNo = "0" + cNo;
					}
					if(cNo.length() == 3){
						cNo = cNo;
					}

					caseCauseNo = cType + fYear + "-" + cNo;

				}

				//=============================================
				//==================IRION COUNTY===============
				//=============================================

				if(key.equals("ENC")){

					fYear = pYear.substring(pYear.length()-4, pYear.length());
					System.out.println("Current year is " + fYear);
					fYear = fYear.substring(2,4);
					fMonth = pYear.substring(0, 2);
					System.out.println("Current month is " + fMonth);

					if(caseCategory.equals("F")){
						caseCategory = "V";
						subCategory = "PR";
						whereClause = "WHERE CASES.category = '" + caseCategory + "' " +
						"AND CASES.cause_no like 'PR%'";
					}
					else if(caseCategory.equals("C")){
						whereClause = "WHERE CASES.category = '" + caseCategory + "' ";
								//"AND CASES.cause_no like 'CR" + fYear + "%'"; WE ARE NOT DOING THIS IN IRION COUNTY, i'M NOT REMOVING IT INCASE WE NEED TO ADD IT AGAIN
					}
					else{
						whereClause = "WHERE CASES.category = '" + caseCategory + "' " +
								"AND CASES.cause_no like 'CCV" + fYear + "%'";
					}

					selectStmt = conn.prepareStatement(
							"SELECT MAX(auto_number)   " +
									"FROM CASES  " + whereClause);

					rset = selectStmt.executeQuery();
					System.out.print(selectStmt);
					if (rset.next()) {
						// read max auto_number
						autoNumber = rset.getInt(1)+1;
					}
					else{
						autoNumber = 1;
					}
					System.out.println("Retrieved auto number = " + autoNumber);



					if(caseCategory.equals("C")){
						cType = "CR";
					}
					else if(caseCategory.equals("V")){
						if(subCategory.equals("PR")){
							cType = "PR";
						}
						else{
							cType = "CCV";
						}
					}

					cNo = String.valueOf(autoNumber);
					if(caseCategory.equals("C")){
						if (cNo.length() == 1) {
							cNo = "000" + cNo;
						}
						if (cNo.length() == 2) {
							cNo = "00" + cNo;
						}
						if (cNo.length() == 3) {
							cNo = "0" + cNo;
						}
						if (cNo.length() == 4) {
							cNo = cNo;
						}
					}
					else{
						if (cNo.length() == 1) {
							cNo = "00" + cNo;
						}
						if (cNo.length() == 2) {
							cNo = "0" + cNo;
						}
						if(cNo.length() == 3){
							cNo = cNo;
						}
					}

					caseCauseNo = cType + fYear + "-" + cNo;

				}

				//============================================
				//================SHELBY COUNTY===============
				//============================================
				if (key.equals("IBC")){
					fYear = pYear.substring(pYear.length()-4, pYear.length());
					System.out.println("Current year is " + fYear);

					if(caseCategory.equals("P")){
						caseCategory = "V";
						subCategory = "PC";
						whereClause = "WHERE CASES.category = '" + caseCategory + "' " +
								"AND CASES.cause_no like '%PC'";
					}
					else if(caseCategory.equals("G")){
						caseCategory = "V";
						subCategory = "PG";
						whereClause = "WHERE CASES.category = '" + caseCategory + "' " +
								"AND CASES.cause_no like '%PG'";
					}
					else if(caseCategory.equals("C")){
						whereClause = "WHERE CASES.category = '" + caseCategory + "' " +
								"AND CASES.cause_no like '%CR'";
					}
					else{
						whereClause = "WHERE CASES.category = '" + caseCategory + "' " +
								"AND CASES.cause_no like '%CV'";
					}

					selectStmt = conn.prepareStatement(
							"SELECT MAX(auto_number)   " +
									"FROM CASES  " + whereClause);

					rset = selectStmt.executeQuery();
					System.out.print(selectStmt);

					if (rset.next()) {
						// read max auto_number
						autoNumber = rset.getInt(1)+1;
					}
					else{
						autoNumber = 1;
					}

					System.out.println("Retrieved auto number = " + autoNumber);

					if(caseCategory.equals("C")){
						cType = "CR";
					}
					else if(caseCategory.equals("V")){
						if(subCategory.equals("PC")){
							cType = "PC";
						}
						else if(subCategory.equals("PG")){
							cType = "PG";
						}
						else{
							cType = "CV";
						}
					}

					cNo = String.valueOf(autoNumber);

					caseCauseNo = fYear + "-" + cNo + cType;
					System.out.println(caseCauseNo);
				}

				//==============================================
				//===================DICKENS DISTRICT============
				//==============================================

				if (key.equals("CKD")) {
					if ("V".equals(caseCategory)){
						if (caseOffType.equals("TAX")) {
							whereClause = "WHERE CASES.category = '" + caseCategory + "' " +
									"AND CASES.subcategory = '" + caseOffType + "' ";
						} else {
							whereClause = "WHERE CASES.category = '" + caseCategory + "' " +
									"AND CASES.subcategory <> 'TAX' ";
						}
					} else
						whereClause = "WHERE CASES.category = '" + caseCategory + "' ";

					selectStmt = conn.prepareStatement(
							"SELECT cause_no, MAX(auto_number)   " +
									"FROM CASES                " +
									whereClause +
							"GROUP BY category ");

					rset = selectStmt.executeQuery();

					cType = "";
					if (rset.next()) {
						// read max auto_number
						autoNumber = rset.getInt(1)+1;
						System.out.println("Retrieved CKD auto number = " + autoNumber);
						if ("V".equals(caseCategory) && "TAX".equals(caseOffType))
							cType = "A";
					} else { //starting seeds
						if ("C".equals(caseCategory)) {
							autoNumber = 2475;
						}
						else {//caseCategory = "V"
							if (caseOffType.equals("TAX")) {
								autoNumber = 2149;
								cType = "A";
							}
							else {
								autoNumber = 4623;
							}
						}
					}

					caseCauseNo = String.valueOf(autoNumber) + cType;

				} //end if key equals CKD


				//=======================================================
				//================   DICKENS COUNTY CLERK   =============
				//=======================================================

				if (key.equals("CKC")) {
					String lastCauseNo = "";
					String cYear = "";
					boolean caseIsGuardianship = false;
					boolean caseIsProbate = false;
					fYear = filingDate.substring(filingDate.length()-4, filingDate.length());
					String guardWhereClause = "";

					if (caseCategory.equals("F")) {
						/*
						 * Read county_oca from code_table to determine
						 * if Probate or Guardianship 101, 102, 103 and 106 are probate
						 * 104 and 105 are Guardianship cases
						 */

						selectStmt = conn.prepareStatement(
								"SELECT county_oca   " +
										"FROM CODE_TABLE                " +
								"WHERE CODE_TABLE.CODE_TYPE = 'case_type' and code = ?");

						selectStmt.setString(1, caseOffType);
						rset = selectStmt.executeQuery();

						if (rset.next()) {
							// read county_oca
							ocaColumn = rset.getInt(1);
						}
						//System.out.println("county_oca code is "+ ocaColumn);
						if (ocaColumn == 104 || ocaColumn == 105) {
							//System.out.println("Entering Guardianship case");
							caseIsGuardianship = true;
							/*
							 * whereClause explained: sequence depends on file date
							 * for 2014, fYear = 2014 if none found set next to 2014-2 per clerk
							 * if not 2014: means that the year changed
							 * and we need to restar sequence to 1
							 * by same token, any December case not filed on time can be
							 * entered in January of the following year and it will not
							 * alter either sequence -re
							 */
							whereClause = " AND SUBSTR(cause_no, 1, LOCATE('-', cause_no)-1) = '" +fYear + "'  "+
									" AND (county_oca = '104' or county_oca = '105' ) ";
						}
						else if (ocaColumn == 101 || ocaColumn == 102 || ocaColumn == 103 || ocaColumn == 106) {
							//System.out.println("Entering Probate case");
							caseIsProbate = true;
							whereClause = " AND (county_oca = '101' or county_oca = '102' or county_oca = '103' or county_oca = '106')";
						}
						else {
							//unknown - invalid county_oca column Cannot Proceed!!
							returnCode = OPERATION_FAILED;
							needToRollback = true;
						}

						caseCategory = "V";

						if (selectStmt != null)
							selectStmt.close();
						if (rset != null)
							rset.close();

					} else {
						//civil or criminal
						if (caseCategory.equals("V")) {
							//civil
							whereClause = " AND NOT (county_oca = '101' or county_oca = '102' or county_oca = '103' " +
									" or county_oca = '106' or county_oca = '104' or county_oca = '105')";
						}

						if (caseCategory.equals("C")) {
							//criminal
							whereClause = "";
						}
					}
					if ("C".equals(caseCategory)) {
						//for criminal, use normal statement
						selectStmt = conn.prepareStatement(
								"SELECT cause_no, MAX(auto_number)   " +
										"FROM CASES                " +
										"WHERE CASES.category = '" + caseCategory + "' "+
								" GROUP BY category "); //added because when u have cause_no u need to group by on MIN/MAX functions

					} else {
						//use query with code_table and case_types
						selectStmt = conn.prepareStatement(
								"SELECT cause_no, MAX(auto_number)   " +
										"FROM CASES INNER JOIN CASE_TYPES ON CASES.CASE_ID = CASE_TYPES.CASE_ID " +
										"INNER JOIN CODE_TABLE ON CASE_TYPES.CASE_TYPE = CODE_TABLE.CODE WHERE CODE_TYPE = 'case_type' " +
										whereClause +
								" GROUP BY CASES.category") ;

					}

					rset = selectStmt.executeQuery();

					if (rset.next()) {
						// read max auto_number
						lastCauseNo = rset.getString(1);
						autoNumber = rset.getInt(2)+1;
						System.out.println("Retrieved auto number = " + autoNumber);
					} else {//starting seeds for 2014
						if ("2014".equals(fYear) && caseIsGuardianship)
							autoNumber = 2;
						else if ((!"2014".equals(fYear)) && caseIsGuardianship)
							autoNumber = 1;
						else if (caseIsProbate)
							autoNumber = 1961;
						else if ("C".equals(caseCategory))
							autoNumber = 7741;
						else //regular Civil Case
							autoNumber = 1148;
					}

					if (caseIsGuardianship) {
						if (autoNumber != 1) { //found match on lookup (lastCauseNo not empty)
							//System.out.println("tacking on another # to seq for year " + fYear);
							//System.out.println("lastCauseNo = " + lastCauseNo);
							try {
								cYear = lastCauseNo.substring(0, lastCauseNo.indexOf("-"));
								//System.out.println("cYear is " + cYear);
								//year changed, first case of new year
								if (Integer.parseInt(fYear) > Integer.parseInt(cYear))
									autoNumber = 1;
							}
							catch (Exception e) {

							}

						}
						caseCauseNo = fYear + "-" + String.valueOf(autoNumber);

					} else
						caseCauseNo = String.valueOf(autoNumber);

				} //end ckc

				//====================ZAPATA DC================//
				//============================================//
				if(key.equals("JSD")){
					selectStmt = conn.prepareStatement("SELECT max(auto_number) FROM cases");

					rset = selectStmt.executeQuery();

					if (rset.next()) {
						// read max auto_number
						autoNumber = rset.getInt(1)+1;
					}
					else{
						autoNumber = 1;
					}

					cNo = NumberFormat.getNumberInstance(Locale.US).format(autoNumber);

					caseCauseNo = cNo;
				}//end JSD

				//=========================JACK DC===================//
                //==================================================//
                if(key.equals("EOD")){
                    fYear = pYear.substring(pYear.length()-4, pYear.length());
                    fYear = fYear.substring(2,4);
                    fMonth = pYear.substring(0, 2);

					if ("V".equals(caseCategory)){
						if (caseOffType.equals("TAX")) {
							whereClause = "WHERE CASES.category = '" + caseCategory + "' " +
									" AND cause_no like '%-B'";
						} else {
							whereClause = "WHERE CASES.category = '" + caseCategory + "' " +
									" AND cause_no like '"+fYear+"-"+fMonth+"%' ";
						}
					} else
						whereClause = "WHERE CASES.category = '" + caseCategory + "' ";

					selectStmt = conn.prepareStatement(
							"SELECT MAX(auto_number)   " +
									"FROM CASES                " +
									whereClause +
							"GROUP BY category ");

                    rset = selectStmt.executeQuery();

                    if (rset.next()) {
                        // read max auto_number
                        autoNumber = rset.getInt(1)+1;
					}
					else{
                       autoNumber = 1;
					}
					//System.out.println("Retrieved auto number = " + autoNumber);

					cNo = String.valueOf(autoNumber);

                    if(caseCategory.equals("V") && !caseOffType.equals("TAX")){
                        if (cNo.length() == 1) {
							cNo = "00" + cNo;
                        }
                        if (cNo.length() == 2) {
							cNo = "0" + cNo;
                        }
                        if(cNo.length() == 3){
							cNo = cNo;
                        }
					}

                    if(caseCategory.equals("C")){
                        caseCauseNo = cNo;
                    }
					else if(caseCategory.equals("V")){
                        if(caseOffType.equals("TAX")){
                            caseCauseNo = cNo + "-B";
                        }
                        else{
                            caseCauseNo = fYear + "-" + fMonth + "-" + cNo;
                        }
                    }
                }


			} else {
				//=======================================================
				//================   ELSE IF NO AUTO CAUSE NUMBER  ================
				//=======================================================

				caseCauseNo = causeNo;

				//=========================================================
				//================COLLINGSWORTH COUNTY===============
				//=========================================================

				if(key.equals("BRC")){
					if (caseCategory.equals("F")){
						subcategory = "P";
						caseCategory = "V";
					}
				}

				//=========================================================
				//================COLLINGSWORTH DISTRICT===============
				//=========================================================

				if(key.equals("BRD")){
					if (caseCategory.equals("F")){
						subcategory = "P";
						caseCategory = "V";
					}
				}

				//=======================================================
				//================   CLAY COUNTY CLERK   ================
				//=======================================================
				if (key.equals("BMC")) {
					if (caseCategory.equals("F"))
						caseCategory = "V";
				}

				//==================================================
				//====================WILLACY COUNTY================
				//==================================================
				if(key.equals("JKC")){
					if(caseCategory.equals("P")){
						caseCategory = "V";
						subCategory = "PRB";
					}else if(caseCategory.equals("G")){
						caseCategory = "V";
						subCategory = "GDN";
					}
				}

				//=========================================================
				//================KENEDY COUNTY CC===============
				//=========================================================

				if(key.equals("FAC")){
					if (caseCategory.equals("F")){
						subcategory = "P";
						caseCategory = "V";
					}
				}

				//=========================================================
				//================MOTLEY COUNTY BOTH CC & DC===============
				//=========================================================

				if(key.equals("GQC") || key.equals("GQD")){
					if (caseCategory.equals("F")){
						subcategory = caseCategory;
						caseCategory = "V";
					}
				}

				//=========================================================
				//================SHELBY CC===============
				//=========================================================

				if(key.equals("IBC")){
					if (caseCategory.equals("P")){
						subcategory = "PC";
						caseCategory = "V";
					}else if(caseCategory.equals("G")){
						subCategory = "PG";
						caseCategory = "V";
					}


				}


			}
			//System.out.println("this is the clerk code in session "+ (String)session.getAttribute("user_clerk"));
			//System.out.println("this is the key%%%%%%%%%%%%%%% "+ key);

			if (key.equals("CYC") && caseCategory.equals("F")){
				caseCategory = "V";
			}
			if (!((String)session.getAttribute("user_clerk")).equals("DA")
					&& !((String)session.getAttribute("user_county")).equals("IJ")
					&& !((String)session.getAttribute("user_county")).equals("GX")
					&& !(((String)session.getAttribute("user_county")).equals("CY")
							&& ((String)session.getAttribute("user_clerk")).equals("D"))
							&& !(((String)session.getAttribute("user_county")).equals("BM")
									&& ((String)session.getAttribute("user_clerk")).equals("C"))
									&& !(((String)session.getAttribute("user_county")).equals("HY"))
									&& !(((String)session.getAttribute("user_county")).equals("HJ"))
					) {
				//System.out.println("going to check for dupes!!!!!!!!!!!!!!!!!!!");
				// close statement to be reused
				if(selectStmt != null)
					selectStmt.close();

				// search the CASES file for the cause number before storing the cas1e
				selectStmt = conn.prepareStatement(
						"SELECT CASES.cause_no " +
								"FROM CASES " +
								"WHERE (CASES.cause_no)="+"'"+ caseCauseNo + "' " +
								"AND (CASES.category)="+"'"+ caseCategory + "'");
				rset = selectStmt.executeQuery();

				while (rset.next()) {
					caseFound++;

				} // end of while

				if (caseFound != 0) {
					returnCode = DUPLICATE;
					needToRollback = true;
				}
			}
			if (needToRollback == false) {
				//System.out.println("rmsDisp = " + rmsDisp);

				if (rmsDisp.equals("Y")) {

					// insert row into CASES table
					insertStmt = conn.prepareStatement(
							"INSERT INTO CASES (" +
									"     state,          " +
									"     county,         " +
									"     category,       " +
									"     subcategory,    " +
									"     clerk,          " +
									"     court,          " +
									"     cause_no,       " +
									"     auto_number,    " +
									"     style,          " +
									"     sealed,         " +
									"     facility,       " +
									"     section,        " +
									"     aisle,          " +
									"     row,            " +
									"     shelf,          " +
									"     box,            " +
									"     seq_no,         " +
									"last_updated_by,     " +
									"last_updated_datetime, " +
									"last_updated_from,   " +
									"jury_demand)     " +
									"VALUES "          +
							"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

					insertStmt.setString(1, (String)session.getAttribute("user_state"));
					insertStmt.setString(2, (String)session.getAttribute("user_county"));
					insertStmt.setString(3, caseCategory);
					System.out.println("This is the compare to civilarray"+caseOffType);
					for (int i=0; i<civilCaseCategory.length; i++) {
						if (caseOffType.equals(civilCaseCategory[i][0])) {
							insertStmt.setString(4, caseOffType);
							break;
						} else {
							insertStmt.setString(4, "");
						}
					}

					insertStmt.setString(5, (String)session.getAttribute("user_clerk"));
					insertStmt.setString(6, court);
					insertStmt.setString(7, caseCauseNo);
					System.out.println("this is the casecauseno   "+caseCauseNo);
					System.out.println("this is the causeno   "+causeNo);
					if (causeNo.equals("XXXXXXXX"))
						insertStmt.setInt(8, autoNumber);
					else
						insertStmt.setInt(8, 0);
					insertStmt.setString(9, caseStyle);
					insertStmt.setString(10, sealed);
					insertStmt.setString(11, facility);
					insertStmt.setString(12, section);
					insertStmt.setString(13, aisle);
					insertStmt.setString(14, row);
					insertStmt.setString(15, shelf);
					insertStmt.setString(16, box);
					insertStmt.setInt(17, tmpSequenceNo);
					insertStmt.setString(18, (String)session.getAttribute("user_name"));
					insertStmt.setTimestamp(19, Timestamp.valueOf(sdf2.format(dt)));
					insertStmt.setString(20, ipAdd);
					insertStmt.setString(21, "NO"); //jury_demand default to 'NO'

				} else {

					if(key.equals("IBDA")){
						// insert row into CASES table
					insertStmt = conn.prepareStatement(
							"INSERT INTO CASES (" +
									"state,          " +
									"county,         " +
									"category,       " +
									"subcategory,    " +
									"clerk,          " +
									"court,          " +
									"agency_case_no,       " +
									"auto_number,    " +
									"style,          " +
									"sealed,         " +
									"seq_no,			" +
									"last_updated_by, " +
									"last_updated_datetime, " +
									"last_updated_from, " +
									"jury_demand) " +
									"VALUES "          +
							"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

					insertStmt.setString(1, (String)session.getAttribute("user_state"));
					insertStmt.setString(2, (String)session.getAttribute("user_county"));
					insertStmt.setString(3, caseCategory);
					if (subcategory.equals("F") || subcategory.equals("P")) {
						insertStmt.setString(4, subcategory);
					} else {
						for (int i=0; i<civilCaseCategory.length; i++) {
							if (caseType.equals(civilCaseCategory[i][0])) {
								insertStmt.setString(4, caseType);
								break;
							} else {
								insertStmt.setString(4, "");
							}
						}
					}

					insertStmt.setString(5, (String)session.getAttribute("user_clerk"));
					insertStmt.setString(6, court);
					insertStmt.setString(7, caseCauseNo);
					//System.out.println("this is the casecauseno   "+caseCauseNo);
					//System.out.println("this is the causeno   "+causeNo);
					if (causeNo.equals("XXXXXXXX"))
						insertStmt.setInt(8, autoNumber);
					else
						insertStmt.setInt(8, 0);
					insertStmt.setString(9, caseStyle);
					insertStmt.setString(10, sealed);
					insertStmt.setInt(11, tmpSequenceNo);
					insertStmt.setString(12, (String)session.getAttribute("user_name"));
					insertStmt.setTimestamp(13, Timestamp.valueOf(sdf2.format(dt)));
					insertStmt.setString(14, ipAdd);
					insertStmt.setString(15, "NO"); //jury_demand default to 'NO'
					}
					else{
					// insert row into CASES table
					insertStmt = conn.prepareStatement(
							"INSERT INTO CASES (" +
									"state,          " +
									"county,         " +
									"category,       " +
									"subcategory,    " +
									"clerk,          " +
									"court,          " +
									"cause_no,       " +
									"auto_number,    " +
									"style,          " +
									"sealed,         " +
									"seq_no,			" +
									"last_updated_by, " +
									"last_updated_datetime, " +
									"last_updated_from, " +
									"jury_demand) " +
									"VALUES "          +
							"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

					insertStmt.setString(1, (String)session.getAttribute("user_state"));
					insertStmt.setString(2, (String)session.getAttribute("user_county"));
					insertStmt.setString(3, caseCategory);
					if (subcategory.equals("F") || subcategory.equals("P")) {
						insertStmt.setString(4, subcategory);
					} else {
						for (int i=0; i<civilCaseCategory.length; i++) {
							if (caseType.equals(civilCaseCategory[i][0])) {
								insertStmt.setString(4, caseType);
								break;
							} else {
								insertStmt.setString(4, "");
							}
						}
					}

					insertStmt.setString(5, (String)session.getAttribute("user_clerk"));
					insertStmt.setString(6, court);
					insertStmt.setString(7, caseCauseNo);
					//System.out.println("this is the casecauseno   "+caseCauseNo);
					//System.out.println("this is the causeno   "+causeNo);
					if (causeNo.equals("XXXXXXXX"))
						insertStmt.setInt(8, autoNumber);
					else
						insertStmt.setInt(8, 0);
					insertStmt.setString(9, caseStyle);
					insertStmt.setString(10, sealed);
					insertStmt.setInt(11, tmpSequenceNo);
					insertStmt.setString(12, (String)session.getAttribute("user_name"));
					insertStmt.setTimestamp(13, Timestamp.valueOf(sdf2.format(dt)));
					insertStmt.setString(14, ipAdd);
					insertStmt.setString(15, "NO"); //jury_demand default to 'NO'
					}
				}

				rowsInserted = insertStmt.executeUpdate();

				if (rowsInserted != 1) {
					System.out.println("Insert to table CASES failed");
					// if insert failed - rollback to
					// release any locks
					needToRollback = true;
					returnCode = OPERATION_FAILED;
				} else {

					// close statement to be reused
					if(selectStmt != null)
						selectStmt.close();

					// insert to CASES successful
					// retrieve case_id for newly inserted case
					selectStmt = conn.prepareStatement(
							"SELECT MAX(case_id)       " +
									"FROM CASES                " +
							"WHERE last_updated_by = ? ");

					selectStmt.setString(1, (String)session.getAttribute("user_name"));

					rset = selectStmt.executeQuery();

					if (rset.next()) {
						// read newly inserted case_id
						tmpCaseId = rset.getInt(1);
						//System.out.println("Retrieved case_id = " + tmpCaseId);

						// close statement to be reused
						insertStmt.close();
						rowsInserted = 0;

						// insert row into OPEN_CASES
						insertStmt = conn.prepareStatement(
								"INSERT INTO OPEN_CASES (" +
										"case_id,        " +
										"sequence_no,    " +
										"filing_date,    " +
										"filed_type,     " +
										"volume_page,    " +
										"type,           " +
										"arrest_date,    " +
										"last_updated_by, " +
										"last_updated_datetime, " +
										"last_updated_from)" +
										"VALUES "          +
								"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

						insertStmt.setInt(1, tmpCaseId);
						insertStmt.setInt(2, tmpSequenceNo);

						insertStmt.setTimestamp(3, Timestamp.valueOf(sdf2.format(sdf1.parse(filingDate))));

						insertStmt.setString(4, filedType);

						insertStmt.setString(5, getVolumePage());
						insertStmt.setString(6, caseCategory);

						if (!"".equals(offenseDate))
							insertStmt.setTimestamp(7, Timestamp.valueOf(sdf2.format(sdf1.parse(offenseDate))));
						else
							insertStmt.setTimestamp(7, null);
						insertStmt.setString(8, (String)session.getAttribute("user_name"));
						insertStmt.setTimestamp(9, Timestamp.valueOf(sdf2.format(dt)));
						insertStmt.setString(10, ipAdd);

						rowsInserted = insertStmt.executeUpdate();

						if (rowsInserted != 1) {
							System.out.println("Insert to table OPEN_CASES failed");
							// if insert failed - rollback to
							// release any locks
							needToRollback = true;
							returnCode = OPERATION_FAILED;
						} else {
							// insert to OPEN_CASES successful

							// if civil case - insert
							if ("V".equals(caseCategory)) {

								// close statement to be reused
								insertStmt.close();
								rowsInserted = 0;

								// insert row into CASE_TYPES
								insertStmt = conn.prepareStatement(
										"INSERT INTO CASE_TYPES (" +
												"case_id,        " +
												"sequence_no,    " +
												"case_type,        " +
												"filing_date,      " +
												"last_updated_by, " +
												"last_updated_datetime, " +
												"last_updated_from)" +
												"VALUES "          +
										"(?, ?, ?, ?, ?, ?, ?)");

								insertStmt.setInt(1, tmpCaseId);
								insertStmt.setInt(2, tmpSequenceNo);
								insertStmt.setString(3, caseOffType);
								System.out.println("This is the civil casetype "+caseOffType);
								insertStmt.setTimestamp(4, Timestamp.valueOf(sdf2.format(sdf1.parse(filingDate))));
								insertStmt.setString(5, (String)session.getAttribute("user_name"));
								insertStmt.setTimestamp(6, Timestamp.valueOf(sdf2.format(dt)));
								insertStmt.setString(7, ipAdd);

								//System.out.println(insertStmt);
								rowsInserted = insertStmt.executeUpdate();

								if (rowsInserted != 1) {
									System.out.println("Insert to table CASE_TYPES failed");
									// if insert failed - rollback to
									// release any locks
									needToRollback = true;
									returnCode = OPERATION_FAILED;
								} else {

									// insert to CASE_TYPES successful
								}
							}
							// if criminall case - insert
							if ("C".equals(caseCategory)) {

								// close statement to be reused
								insertStmt.close();
								rowsInserted = 0;

								//degree class
								selectStmt = conn.prepareStatement("SELECT code, offense, degree_class"
									+ "  from state_cjis where code = ?");

								selectStmt.setString(1, caseOffType);
								rset = selectStmt.executeQuery();

								if(rset.next()){
									degree = rset.getString(1);
								}
								// insert row into OFFENSES
								insertStmt = conn.prepareStatement(
										"INSERT INTO OFFENSES (" +
												"case_id,        " +
												"sequence_no,    " +
												"offense,        " +
												"degree,		 " +
												"offense_date,    " +
												"last_updated_by, " +
												"last_updated_datetime, " +
												"last_updated_from)" +
												"VALUES "          +
										"(?, ?, ?, ?, ?, ?, ?, ?)");

								insertStmt.setInt(1, tmpCaseId);
								insertStmt.setInt(2, tmpSequenceNo);
								insertStmt.setString(3, caseOffType);
								insertStmt.setString(4, degree);
								System.out.println("This is the criminal offense"+caseOffType);
								if (!"".equals(filingDate))
									insertStmt.setTimestamp(5, Timestamp.valueOf(sdf2.format(sdf1.parse(filingDate))));
								else
									insertStmt.setTimestamp(5, null);
								insertStmt.setString(6, (String)session.getAttribute("user_name"));
								insertStmt.setTimestamp(7, Timestamp.valueOf(sdf2.format(dt)));
								insertStmt.setString(8, ipAdd);

								//System.out.println(insertStmt);
								rowsInserted = insertStmt.executeUpdate();

								if (rowsInserted != 1) {
									System.out.println("Insert to table OFFENSES failed");
									// if insert failed - rollback to
									// release any locks
									needToRollback = true;
									returnCode = OPERATION_FAILED;
								} else {

									// insert to OFFENSES successful
									// insert default plaintiff

									rowsInserted = 0;

									insertStmt = conn.prepareStatement(
											"INSERT INTO PARTIES (  " +
													"     case_id,          " +
													"     sequence_no,      " +
													"     party_code,       " +
													"     other_party_code, " +
													"     party_group,      " +
													"     related_party,    " +
													"     person_no,        " +
													"     last_updated_by,  " +
													"last_updated_datetime, " +
													"last_updated_from)     " +
													"VALUES "          +
											"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

									insertStmt.setInt(1, tmpCaseId);
									insertStmt.setInt(2, tmpSequenceNo);
									// plaintiff for criminal cases
									insertStmt.setString(3, "PLA");
									insertStmt.setNull(4, Types.VARCHAR);
									insertStmt.setString(5, "PLA");
									insertStmt.setNull(6, Types.INTEGER);
									insertStmt.setInt(7, Integer.parseInt((String)session.getAttribute("default_plaintiff_person_no")));
									insertStmt.setString(8, (String)session.getAttribute("user_name"));
									insertStmt.setTimestamp(9, Timestamp.valueOf(sdf2.format(dt)));
									insertStmt.setString(10, ipAdd);

									//THIS WILL PREVENT FROM ENTERING A AUTO PLAINTIFF, ONLY FOR DUVAL FOR NOW
									if((!session.getAttribute("user_county").toString().equals("CN")) || districtAttorney.equals("Y"))
										rowsInserted = insertStmt.executeUpdate();
									else
										rowsInserted = 1;

									if (rowsInserted != 1) {
										System.out.println("Insert to table PARTIES failed");
										// if insert failed - rollback to
										// release any locks
										needToRollback = true;
										returnCode = OPERATION_FAILED;
									} else {

										if (rset != null)
											rset.close();
										if (selectStmt != null)
											selectStmt.close();

										selectStmt = conn.prepareStatement(
												"SELECT MAX(party_no)      " +
														"FROM PARTIES              " +
												"WHERE last_updated_by = ? ");

										selectStmt.setString(1, (String)session.getAttribute("user_name"));

										rset = selectStmt.executeQuery();

										if (rset.next()) {
											// read newly inserted party_no
											// (not sure if this is needed)
											partyNumber = rset.getInt(1);
											//System.out.println("Retrieved party number = " + partyNumber);
										} else {
											// problem with retrieving party_no
											System.out.println("Failed to retrieve party_no");
											needToRollback = true;
											returnCode = OPERATION_FAILED;
										}
										//If constants table has a Y in the district_attorney field,
										//then we auto insert Default CA or DA for all criminal cases ONLY!!!.
										System.out.println("districtAttorney " + districtAttorney);
										if (districtAttorney.equals("Y")) {
											System.out.println("Session id for DA " + session.getAttribute("default_da_person_no"));
											rowsInserted = 0;
											insertStmt.setInt(1, tmpCaseId);
											insertStmt.setInt(2, tmpSequenceNo);
											// district attorney for criminal cases
											insertStmt.setString(3, "ATT");
											//Need to know if they are county or district because Default da for county clerks is
											//"COUNTY" in party_code and not "DIST"
											String clerk = (String)session.getAttribute("user_clerk");
											if (clerk.equals("D")){
												insertStmt.setString(4, "DIST");
											}else{
												insertStmt.setString(4, "COUNTY");
											}
											insertStmt.setString(5, "ATT");
											insertStmt.setInt(6, partyNumber);
											insertStmt.setInt(7, Integer.parseInt((String)session.getAttribute("default_da_person_no")));
											insertStmt.setString(8, (String)session.getAttribute("user_name"));
											insertStmt.setTimestamp(9, Timestamp.valueOf(sdf2.format(dt)));
											insertStmt.setString(10, ipAdd);

											//System.out.println(insertStmt.toString());

											rowsInserted = insertStmt.executeUpdate();
											System.out.println("rowsInserted in parties for dist att " + rowsInserted);
											if (rowsInserted != 1) {
												System.out.println("Insert to table PARTIES failed");
												// if insert failed - rollback to
												// release any locks
												needToRollback = true;
												returnCode = OPERATION_FAILED;
											}
										}
									}
								}
							} // end of if (!"0".equals(getOffense()))


							//Logging new case entered
							String msg = "";
							msg = (String)session.getAttribute("user_name") + ",entered "+
									" new case. case_id="+tmpCaseId+" causeNo="+caseCauseNo+" in court="+court+". Cat "+caseCategory+
									" subcat "+subcategory+" with type/off "+caseOffType;
							msgLogger log = new msgLogger();
							log.writer(msg, null, (String)session.getAttribute("user_county"), conn);


						} // end of insert to OPEN_CASES successful (rowsInserted == 1)
					} else {
						// problem with retrieving case_id
						System.out.println("Failed to retrieve case_id");
						needToRollback = true;
						returnCode = OPERATION_FAILED;
					}
					//if civil case we enter the fees from the feeGroup attached (fee_group_attached code in the code_table for the case type)
					if(caseCategory.equals("V") && causeNo.equals("XXXXXXXX") && (!"BM".equals((String)session.getAttribute("user_county")))){
						insertFeeGroupFeesForCaseType(tmpCaseId,caseOffType,(String)session.getAttribute("user_name"),conn,ipAdd, request,isCCLCourt(court,request));
					}


				} // end of insert to CASES successful (rowsInserted == 1)
			} // end of needToRollback == false
			if (returnCode != OPERATION_FAILED) {
				// everything OK
				// save newly inserted case_id
				// and sequence number
				caseId = tmpCaseId;
				sequenceNumber = tmpSequenceNo;
				session.setAttribute("case_id", (String)String.valueOf(tmpCaseId));
				session.setAttribute("seq_no", (String)String.valueOf(tmpSequenceNo));
			}
		} // end of try


		catch (Exception e) {
			// preparedStatement(), executeUpdate() throw this exception
			System.out.println("Caught Exception in CaseBean.storeCaseData()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			// restore session info
			session = tmpSession;

			needToRollback = true;
			returnCode = OPERATION_FAILED;

			//this.exceptionEmail.emailException(request, e);

		}
		finally {
			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in CaseBean.storeCaseData().");
			try {
				if (rset != null)         // Close ResultSet
					rset.close();
				if (insertStmt != null)    // Close statements
					insertStmt.close();
				if (selectStmt != null)
					selectStmt.close();

				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();

					conn.close();
				}

				return returnCode;
			}
			catch (Exception e) {
				// close() throw this exception
				System.out.println("Caught Exception in CaseBean.storeCaseData()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// inserts took place

				//this.exceptionEmail.emailException(request, e);
				return returnCode;
			}
		}
	}




	public String getFeeGroup(String caseType,boolean isCCL,HttpServletRequest request){

		String feeGroup = "";

		PreparedStatement selectStmt = null;
		ResultSet rset = null;
		Connection conn = getConnection(jndiResource);
		boolean needToRollback = false;

		String theField = "";

		if(isCCL)
			theField ="fee_group_attachedCCL";
		else
			theField ="fee_group_attached";


		try {
			conn = getConnection(jndiResource);



			selectStmt = conn.prepareStatement(
					"select "+theField+" from code_table where code ='"+caseType+"'  and code_type = 'case_type'" );



			rset = selectStmt.executeQuery();

			if (rset.next())
				if(rset.getString(theField)!=null)
					feeGroup = rset.getString(theField);
				else
					System.out.println("Failed to retrieve fee group in CaseBean.getFeeGroup!");


			//if the ccl fee group is null or blank we get the fee group from the regular fee_group_attached field
			if(feeGroup.equals("")){
				selectStmt = conn.prepareStatement(
						"select fee_group_attached from code_table where code ='"+caseType+"'  and code_type = 'case_type'" );

				rset = selectStmt.executeQuery();

				if (rset.next()) {
					feeGroup = rset.getString("fee_group_attached");
					//System.out.println("$$$$$$$$$$$$$$$THIS IS FEE GROUP ATTACHED"+feeGroup);

				} else {
					// problem with retrieving fee_fine_no
					System.out.println("Failed to retrieve fee group in CaseBean.getFeeGroup!");

				}
			}

		}//end try
		catch(Exception e){
			needToRollback = true;
			System.out.println("Caught exception in CaseBean.getFeeGroup!");
			e.printStackTrace();
			this.exceptionEmail.emailException(request, e);
		}

		finally {
			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in CaseBean.getFeeGroup().");
			try {

				if (rset != null) {  // Close recordset
					try { rset.close(); } catch (SQLException e) { System.out.println("Caught rset not closed in CaseBean.getFeeGroup()!"); }
					rset = null;
				}

				if (selectStmt != null) {   // Close statement
					try { selectStmt.close(); } catch (SQLException e) { System.out.println("Caught selectStmt not closed in CaseBean.getFeeGroup()!"); }
					selectStmt = null;
				}

				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					try { conn.close(); } catch (SQLException e) { System.out.println("Caught connection not closed in CaseBean.getFeeGroup()!"); }
					conn = null;
				}


			}
			catch (SQLException e) {
				// close() throw this exception
				System.out.println("Caught SQLException in CaseBean.getFeeGroup()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// inserts took place

			}
		}

		return feeGroup;
	}









	public double getFeesTotal(int caseId,Connection c,HttpServletRequest request){

		double total = 0.0;

		PreparedStatement selectStmt = null;
		ResultSet rset = null;
		Connection conn = getConnection(jndiResource);
		boolean needToRollback = false;
		try {


			selectStmt = c.prepareStatement(
					"select sum(amount) as total from fees_fines where case_id = "+caseId );


			//System.out.println(selectStmt);
			rset = selectStmt.executeQuery();

			if (rset.next()) {
				total = rset.getDouble("total");

			} else {
				// problem with retrieving fee group
				System.out.println("Failed to retrieve fee group in CaseBean.getFeesTotal!");

			}
			return total;
		}
		catch(Exception e){
			System.out.println("Caught exception in CaseBean.getFeesTotal!");
			e.printStackTrace();
			needToRollback = true;
			this.exceptionEmail.emailException(request, e);
		}
		finally {
			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in CaseBean.getFeesTotal().");
			try {

				if (rset != null) {  // Close recordset
					try { rset.close(); } catch (SQLException e) { System.out.println("Caught rset not closed in CaseBean.getFeesTotal()!"); }
					rset = null;
				}

				if (selectStmt != null) {   // Close statement
					try { selectStmt.close(); } catch (SQLException e) { System.out.println("Caught selectStmt not closed in CaseBean.getFeesTotal()!"); }
					selectStmt = null;
				}

				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					try { conn.close(); } catch (SQLException e) { System.out.println("Caught connection not closed in CaseBean.getFeesTotal()!"); }
					conn = null;
				}


			}
			catch (SQLException e) {
				// close() throw this exception
				System.out.println("Caught SQLException in CaseBean.getFeesTotal()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// inserts took place

			}
		}
		return total;
	}










	public boolean hasFeesWithoutResponsibleParty(int caseId,HttpServletRequest request,String caseCat){

		boolean result = false;
		int count = 0;
		PreparedStatement selectStmt = null;
		ResultSet rset = null;
		Connection conn = getConnection(jndiResource);
		boolean needToRollback = false;

		try {


			selectStmt = conn.prepareStatement(
					"select fee_fine_no  from fees_fines,CODE_TABLE " +
							"WHERE case_id = " + caseId + " " +
							"AND sequence_no = 1 " +
							"AND code = fee_type " +
							"AND fees_fines.fee_group IS NULL " +
							"AND party_responsible is NULL " +
							"AND (code_type = '"+caseCat+"' " +
							"OR code_type = 'fee_type_misc' " +
					"OR code_type = 'fine_type') ");


			//System.out.println(selectStmt);
			rset = selectStmt.executeQuery();

			if (rset.next()) {
				count += rset.getInt("fee_fine_no");

			}
			if (count >0)
				result = true;
		}
		catch(Exception e){
			System.out.println("Caught exception in CaseBean.hasFeesWithoutResponsibleParty!");
			e.printStackTrace();
			this.exceptionEmail.emailException(request, e);
			result = false;
		}
		finally {
			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in CaseBean.hasFeesWithoutResponsibleParty().");
			try {

				if (rset != null) {  // Close recordset
					try { rset.close(); } catch (SQLException e) { System.out.println("Caught rset not closed in CaseBean.hasFeesWithoutResponsibleParty()!"); }
					rset = null;
				}

				if (selectStmt != null) {   // Close statement
					try { selectStmt.close(); } catch (SQLException e) { System.out.println("Caught selectStmt not closed in CaseBean.hasFeesWithoutResponsibleParty()!"); }
					selectStmt = null;
				}

				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					try { conn.close(); } catch (SQLException e) { System.out.println("Caught connection not closed in CaseBean.hasFeesWithoutResponsibleParty()!"); }
					conn = null;
				}


			}
			catch (SQLException e) {
				// close() throw this exception
				System.out.println("Caught SQLException in CaseBean.hasFeesWithoutResponsibleParty()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// inserts took place

			}
		}
		return result;
	}










	/**
	 * inserts the fees for the fee group associated to the case type
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 * 	<li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 * 	<li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 * 	<li><code><a href="PaymentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: data is stored in the database </i>
	 */
	public int insertFeeGroupFeesForCaseType(int caseId,String caseType,String userName, Connection c,String ipAdd,HttpServletRequest request,boolean isCCLCourt) {

		int returnCode = SUCCESS;
		boolean needToRollback = false;
		HttpSession session = null;
		int rowsInserted = 0;
		int rowsUpdated = 0;
		Map paramMap = null;
		Iterator iter = null;
		Map.Entry me = null;
		String tmp[] = null;
		String feeGroup = "";
		String feeType = "";
		String str = "";
		String category = "";
		int noOfFee = 0;
		double feeValue = 0.0;

		String ft = "";
		String accNo = "";
		int prt = 0;

		int tmpArrayInt[] = new int[MAX_SIZE];

		category = "V";



		PreparedStatement insertStmt = null;
		PreparedStatement selectStmt = null;
		ResultSet rset = null;
		PreparedStatement selectStmt1 = null;
		PreparedStatement updateStmt = null;
		ResultSet rset1 = null;

		String w = "";
		String feeGp = getFeeGroup(caseType,isCCLCourt,request);
		int feeFineNumber = 0;

		try {


			if (c == null)
				return NO_DATABASE_CONNECTION; // no connection

			// define format used by form
			SimpleDateFormat sdf1 = new SimpleDateFormat ("MM/dd/yyyy");
			// define format for JDBC Timestamp
			SimpleDateFormat sdf2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

			//capture current date/time and format it
			java.util.Date dt = new java.util.Date();


			w="select '"+caseId+"','1','"+feeGp+"', fee_type, account_no, priority, null ,amount, '"
					+Timestamp.valueOf(sdf2.format(dt))+"','"+Timestamp.valueOf(sdf2.format(dt))+"', '"
					+userName+"','"+Timestamp.valueOf(sdf2.format(dt))+"', '"
					+ipAdd+"' ";

			insertStmt = c.prepareStatement(
					"INSERT INTO FEES_FINES (  " +
							"     case_id,          " +
							"     sequence_no,      " +
							"     fee_group,       " +
							"     fee_type, " +
							"     account_no,      " +
							"     priority,    " +
							"     party_responsible,        " +
							"	 amount,			" +
							"	 date_ordered,  " +
							"	 date_due,  " +
							"     last_updated_by,  " +
							"last_updated_datetime, " +
							"last_updated_from)     " +
							w+
							"from fee_schedule where fee_group= '"+feeGp+"' and not fee_group is null and not trim(fee_group)='' ");

			//System.out.println("################insertStmt:"+insertStmt);

			rowsInserted = insertStmt.executeUpdate();
			if (rowsInserted < 1) {
				//Could be that the fee_group_attached from code_table is set to "NONE" which is where
				//there will never be a fee group for this case type.  Therefore Skip....
				System.out.println("Will not insert any fees on this new case, CaseBean.insertFeeGroupFeesForCaseType caseId:"+caseId);
			} else {

				//Group record for bcc group inserted here
				//here it was putting this record no matter what, now we only do it if
				//it found a matching fee_group_attached from code_table that is in fee_group field of Fee_schedule table

				insertStmt = c.prepareStatement(
					"INSERT INTO FEES_FINES (  " +
							"case_id,                  " +
							"sequence_no,              " +
							"fee_group,                " +
							"fee_type,                 " +
							"account_no,               " +
							"priority,                 " +
							"party_responsible,        " +
							"amount,                   " +
							"group_record,             " +
							"description,              " +
							"date_ordered,             " +
							"date_due,                 " +
							"last_updated_by,          " +
							"last_updated_datetime,    " +
							"last_updated_from)        " +
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?)");

				// set parameters for INSERT query
				insertStmt.setInt(1, caseId);
				insertStmt.setInt(2, 1);
				insertStmt.setNull(3, Types.VARCHAR);
				insertStmt.setString(4, feeGp);
				insertStmt.setNull(5, Types.VARCHAR);
				insertStmt.setInt(6, 10);
				insertStmt.setNull(7, Types.VARCHAR);
				insertStmt.setDouble(8, getFeesTotal(caseId,c,request));
				insertStmt.setString(9, "1");
				insertStmt.setString(10, "");
				insertStmt.setTimestamp(11, Timestamp.valueOf(sdf2.format(dt)));

				insertStmt.setTimestamp(12, null);

				insertStmt.setString(13, userName);
				insertStmt.setTimestamp(14, Timestamp.valueOf(sdf2.format(dt)));
				insertStmt.setString(15, ipAdd);

				rowsInserted = insertStmt.executeUpdate();

				if (rowsInserted != 1) {
					System.out.println("Insert into table FEES_FINES failed");
					// if insert failed - rollback to
					// release any locks
					needToRollback = true;
					returnCode = OPERATION_FAILED;

				} else {

					// insert to FEES_FINES successful

					// retrieve fee_fine_no for newly inserted fee/fine
					selectStmt = c.prepareStatement(
							"SELECT MAX(fee_fine_no)     " +
									"FROM FEES_FINES             " +
									"WHERE case_id = ?           " +
									"AND sequence_no = ?         " +
							"AND last_updated_by = ?" );

					selectStmt.setInt(1, caseId);
					selectStmt.setInt(2, 1);
					selectStmt.setString(3, userName);

					rset = selectStmt.executeQuery();

					if (rset.next()) {
						feeFineNumber = rset.getInt(1);

					} else {
						// problem with retrieving fee_fine_no
						System.out.println("Failed to retrieve fee_fine_no for the bcc group record");
						needToRollback = true;
						returnCode = OPERATION_FAILED;
					}

					//System.out.println("Retrieved fee_fine_no = " + feeFineNumber);
				}

				if (needToRollback == false) {

					updateStmt = c.prepareStatement(
							"UPDATE FEES_FINES            " +
									"SET group_fee_fine_no = ?,   " +
									"    last_updated_by = ?,     " +
									"    last_updated_datetime = ?, " +
									"    last_updated_from = ?    " +
							"WHERE case_id = ? and fee_fine_no <>  ?" );


					updateStmt.setInt(1, feeFineNumber);
					updateStmt.setString(2, userName);
					updateStmt.setTimestamp(3, Timestamp.valueOf(sdf2.format(dt)));
					updateStmt.setString(4, ipAdd);
					updateStmt.setInt(5, caseId);
					updateStmt.setInt(6, feeFineNumber);

					//System.out.println(updateStmt);

					rowsUpdated = updateStmt.executeUpdate();

					if (rowsUpdated == 0) {
						System.out.println("Update in table FEES_FINES failed");
						// if update failed - rollback to
						// release any locks
						needToRollback = true;
						returnCode = OPERATION_FAILED;

					}
				}
			}

			return returnCode;

		}

		catch (Exception e) {
			// preparedStatement(), executeUpdate() throw this exception
			System.out.println("Caught Exception in CaseBean.insertFeeGroupFeesForCaseType()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;
			this.exceptionEmail.emailException(request, e);
			needToRollback = true;
			returnCode = DATABASE_EXCEPTION;
		}

		finally {
			//allow for gc
			tmp = null;
			tmpArrayInt = null;

			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in CaseBean.insertFeeGroupFeesForCaseType().");
			try {

				if (rset != null) {  // Close recordSet
					try { rset.close(); } catch (SQLException e) { System.out.println("Caught rset not closed in CaseBean.insertFeeGroupFeesForCaseType()!"); }
					rset = null;
				}

				if (selectStmt != null) {   // Close statement
					try { selectStmt.close(); } catch (SQLException e) { System.out.println("Caught selectStmt not closed in CaseBean.insertFeeGroupFeesForCaseType()!"); }
					selectStmt = null;
				}


				if (insertStmt != null) {   // Close statement
					try { insertStmt.close(); } catch (SQLException e) { System.out.println("Caught insertStmt not closed in CaseBean.insertFeeGroupFeesForCaseType()!"); }
					insertStmt = null;
				}
				if (updateStmt != null)    // Close statement
					updateStmt.close();
				return returnCode;
			} catch (Exception e) {
				return returnCode;
			}
		}
	}












	/**
	 * reads necessary information about the case
	 * <p>
	 * @param case ID as <code>int</code>
	 * @param sequence number as <code>int</code>
	 * @param HTTP request as <code>HttpServletRequest</code>
	 * @param code as <code><a href="CodeBean.html>CodeBean</a></code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *  <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *  <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *  <li><code><a href="CaseBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: data is read from the database and stored
	 *               in the bean </i>
	 */
	public int readCaseData(int csId, int seqNo, HttpServletRequest request, CodeBean codes)
	{
		//System.out.println("************************dbType.equalsIgnoreCase " + dbType);
		int returnCode = SUCCESS;
		boolean needToRollback = false;
		HttpSession session = null;
		HttpSession tmpSession = null;

		// temp variables to fetch data
		String str = "";
		int num = 0, allFound = 0;
		Timestamp ts = null;
		java.util.Date fDate = null;
		java.util.Date dDate = null;
		long diffInDates = 0;

		Connection conn = getConnection(jndiResource);

		if (conn == null)
			return NO_DATABASE_CONNECTION; // no connection

		PreparedStatement selectStmt = null;
		ResultSet rset = null;

		try
		{
			// get current session based on HttpServletRequest)
			// and don't create a new on (create=false)
			session = request.getSession(false);

			// save session info in case it needs to be restored
			tmpSession = session;

			// capture current date/time and format it
			java.util.Date dt = new java.util.Date();

			// define format used by form
			SimpleDateFormat sdf = new SimpleDateFormat ("MM/dd/yyyy");
			SimpleDateFormat sdf2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

			// clear values in the member variables (rest form)
			reset();
			session.setAttribute("case_category", "NONE");
			//MODIFIED FROM JULIO -ADDED AGENCY_CASE_NO
			// retrieve case data from CASES table
			selectStmt = conn.prepareStatement(
					"SELECT state,                 " +
							"       county,                " +
							"       clerk,                 " +
							"       category,              " +
							"       court,                 " +
							"       cause_no,              " +
							"       style,                 " +
							"       facility,              " +
							"       section,               " +
							"       aisle,                 " +
							"       row,                   " +
							"       shelf,                 " +
							"       box,                   " +
							"       seq_no,                " +
							"       sealed,                " +
							"       destroy_date,          " +
							"       NOPP,                  " +
							"       last_updated_datetime, " +
							"       last_updated_by,       " +
							"       jury_demand,            " +
							"       agency_case_no         " +
							"FROM CASES                    " +
					"WHERE case_id = ? ");

			selectStmt.setInt(1, csId);

			rset = selectStmt.executeQuery();


			if (rset.next())
			{ // expect only one row
				//System.out.println("Test after query and reading state ");
				str = rset.getString("state");
				if (str != null)
					state = str.trim();

				str = rset.getString("county");
				if (str != null)
					county = str.trim();

				str = rset.getString("clerk");
				if (str != null)
					clerk = str.trim();

				str = rset.getString("category");
				if (str != null)
					caseCategory = str.trim();

				str = rset.getString("court");
				if (str != null) {
					court = str.trim();
					courtDesc = (String)(codes.getCourtMap()).get(court);
				}

				str = rset.getString("cause_no");
				if (str != null)
					causeNumber = str.trim();
				System.out.println("CAUSE NUMBER: " + str);

				str = rset.getString("style");
				System.out.println("STYLE: " + str);
				if (str != null)
					caseStyle = str.trim();

				str = rset.getString("facility");
				if (str != null)
					facility = str.trim();

				str = rset.getString("section");
				if (str != null)
					section = str.trim();

				str = rset.getString("aisle");
				if (str != null)
					aisle = str.trim();

				str = rset.getString("row");
				if (str != null)
					row = str.trim();

				str = rset.getString("shelf");
				if (str != null)
					shelf = str.trim();
				//System.out.println("Test before reading seqNum ");

				str = rset.getString("box");
				if (str != null)
					box = str.trim();
				//System.out.println("Test before reading box ");

				seqNum = rset.getInt("seq_no");
				//System.out.println("Test after reading seqNum ");

				str = rset.getString("sealed");
				if (str != null){
					sealed = str.trim();
					session.removeAttribute("sealed_case");
					session.setAttribute("sealed_case", sealed);
				}else{
					session.removeAttribute("sealed_case");
					session.setAttribute("sealed_case", "NO");
				}
				//System.out.println("Is this case Sealed? " + sealed);

				ts = rset.getTimestamp("destroy_date");
				if (ts != null)
					destroyDate = sdf.format(ts);

				ts = rset.getTimestamp("NOPP");
				if (ts != null)
					nopp = sdf.format(ts);

				ts = rset.getTimestamp("last_updated_datetime");
				if (ts != null)
					enteredDateTime = sdf2.format(ts);

				str = rset.getString("last_updated_by");
				if (str != null)
					enteredUser = str.trim();

				str = rset.getString("jury_demand");
				if (str != null)
					juryDemand = str.trim();

				str = rset.getString("agency_case_no");
				if(str != null)
					agencyCaseNo = str.trim();

				// select from CASES completed
				// continue with OPEN_CASES

				// close result set and statement
				// so they can be reused
				rset.close();
				selectStmt.close();

				//System.out.println("Before running the select from open cases, csId: " + csId +"seqNo: " +seqNo);
				// retrieve case data from OPEN_CASES table
				selectStmt = conn.prepareStatement(
						"SELECT volume_page,       " +
								"       filing_date,       " +
								"       filed_type,        " +
								"       type,              " +
								"       capias,            " +
								"       judgment,          " +
								"       jailed_date,       " +
								"       comments,          " +
								"       jury_examined,     " +
								"       jury_sworn,        " +
								"       warrant_outstanding, " +
								"       arrest_date        " +
								"FROM OPEN_CASES           " +
						"WHERE case_id = ?       	");
				selectStmt.setInt(1, csId);

				rset = selectStmt.executeQuery();
				if (rset.next())
				{ // expect only one row
					str = rset.getString("volume_page");
					if (str != null)
						volumePage = str.trim();

					ts = rset.getTimestamp("filing_date");
					if (ts != null)
						filingDate = sdf.format(ts);
					System.out.println("FILING DATE:"+ filingDate);

					//System.out.println("test");
					str = rset.getString("filed_type");
					if (str != null)
					{
						filedType = str.trim();
						//System.out.println("Filed Type code is " + filedType);
						filedTypeDesc = (String)(codes.getFiledTypeMap()).get(filedType);
						//System.out.println("Filed Type Desc is " + filedTypeDesc);
					}

					str = rset.getString("type");
					if (str != null)
					{
						caseCategory = str.trim();
					}

					str = rset.getString("capias");
					if (str != null)
						capias = str.trim();

					str = rset.getString("judgment");
					if (str != null)
						judgment = str.trim();

					ts = rset.getTimestamp("jailed_date");
					if (ts != null)
						jailedDate = sdf.format(ts);

					str = rset.getString("comments");
					if (str != null)
						comments = str.trim();

					str = rset.getString("jury_examined");
					if (str != null)
						juryExamined = str.trim();

					str = rset.getString("jury_sworn");
					if (str != null)
						jurySworn = str.trim();

					str = rset.getString("warrant_outstanding");
					if (str != null)
						warrantOutstanding = str.trim();

					ts = rset.getTimestamp("arrest_date");
					if (ts != null)
						offenseDate = sdf.format(ts);
					//System.out.println("open_cases.arrest Date: "+ offenseDate);

					//System.out.println("Finished reading OPEN_CASES");
					// select from OPEN_CASES completed



					// set session attributes case_id and seq_no
					session.setAttribute("case_id", (String)String.valueOf(csId));
					session.setAttribute("seq_no", (String)String.valueOf(seqNo));


					// read PARTIES table to find out if primary parties
					// are added to this case

					session.setAttribute("plaintiff_present", "NO");
					session.setAttribute("defendant_present", "NO");

					// close result set and statement
					// so they can be reused
					rset.close();
					selectStmt.close();

					// get count of parties with party_code
					// PLA or PET
					selectStmt = conn.prepareStatement(
							"SELECT count(*)     " +
									"FROM PARTIES        " +
									"WHERE case_id = ?   " +
									"AND sequence_no = ? " +
							"AND party_group = 'PLA'");

					selectStmt.setInt(1, csId);
					selectStmt.setInt(2, seqNo);

					rset = selectStmt.executeQuery();

					// expect only one row
					if (rset.next())
						if (rset.getInt(1) > 0)
							session.setAttribute("plaintiff_present", "YES");


					// close result set and statement
					// so they can be reused
					rset.close();
					selectStmt.close();

					// get count of parties with party_code
					// DEF or RES
					selectStmt = conn.prepareStatement(
							"SELECT count(*), person_no     " +
									"FROM PARTIES        " +
									"WHERE case_id = ?   " +
									"AND sequence_no = ? " +
									"AND party_group = 'DEF' " +
							"GROUP BY person_no ");



					selectStmt.setInt(1, csId);
					selectStmt.setInt(2, seqNo);
					//System.out.println(selectStmt.toString());
					rset = selectStmt.executeQuery();

					// expect only one row
					if (rset.next())
						if (rset.getInt(1) > 0){
							session.setAttribute("defendant_present", "YES");
							session.setAttribute("person_no", (String)String.valueOf(rset.getInt(2)));
						}

					// close result set and statement
					// so they can be reused
					rset.close();
					selectStmt.close();

					// get count of parties with party_code
					// OTH
					selectStmt = conn.prepareStatement(
							"SELECT count(*)     " +
									"FROM PARTIES        " +
									"WHERE case_id = ?   " +
									"AND sequence_no = ? " +
							"AND party_group = 'OTH' ");

					selectStmt.setInt(1, csId);
					selectStmt.setInt(2, seqNo);

					rset = selectStmt.executeQuery();

					// expect only one row
					if (rset.next())
						if (rset.getInt(1) > 0)
							session.setAttribute("others_present", "YES");

					// continue with CASE_TYPES if civil case or a family case

					if ("V".equals(caseCategory))
					{
						// close result set and statement
						// so they can be reused
						rset.close();
						selectStmt.close();
						ts = null;
						caseTypesFound = 0;

						//System.out.println("CIVIL Case id "+ csId);


						// retrieve case data from CASE_TYPES table
						str = 	"SELECT case_type_no, 		" +
								"		case_type, 			" +
								"		filing_date,		" +
								"		disposition_date, 	" +
								"		disposition_code,	";
						if (dbType.equalsIgnoreCase(DB_TYPE_SQL_SERVER))
							str = str + " datediff(DAY,filing_date,disposition_date) as expr1, ";
						else if (dbType.equalsIgnoreCase("MYSQL"))
							str += " datediff(disposition_date,filing_date) as expr1, ";
						else
							str = str + " [disposition_date]-[filing_date] as expr1, ";

						str = str + " 	b.oca_code 			" +
									" FROM case_types inner join code_table b on case_types.case_type=b.code and code_type='case_type' " +
									" WHERE case_id = ? " +
                        			" ORDER BY filing_date desc, case_type_no desc ";

						selectStmt = conn.prepareStatement(str);

						selectStmt.setInt(1, csId);
						rset = selectStmt.executeQuery();

						// expect one or more rows
						while (rset.next())
							allFound++;
						if (allFound > 0){
							rset.beforeFirst();
							// initialize caseType array
							caseTypeArray = new CaseType[allFound];
							while ((rset.next()) && (caseTypesFound < allFound)) {

								caseTypeArray[caseTypesFound] = new CaseType();

								// case_type_no
								num = rset.getInt("case_type_no");
								caseTypeArray[caseTypesFound].setCaseTypeNo(num);

								// case_type
								str = rset.getString("case_type");
								if ( str!= null) {
									caseTypeArray[caseTypesFound].setCaseType(str.trim());
									caseTypeArray[caseTypesFound].setCaseTypeDesc((String)(codes.getCaseTypeMap()).get(caseType));
								}

								// filing_date
								ts = rset.getTimestamp("filing_date");
								fDate = ts;
								if (ts != null)
									caseTypeArray[caseTypesFound].setFilingDate(sdf.format(ts));
								else
									caseTypeArray[caseTypesFound].setFilingDate("No Date Found!");

								// disposition_date
								ts = rset.getTimestamp("disposition_date");
								dDate = ts;
								if (ts != null)
									caseTypeArray[caseTypesFound].setDispositionDate(sdf.format(ts));
								else
									caseTypeArray[caseTypesFound].setDispositionDate("&nbsp");

								str = rset.getString("disposition_code");

								if (str != null) {
									caseTypeArray[caseTypesFound].setDispositionType(str.trim());
									caseTypeArray[caseTypesFound].setDispositionTypeDesc((String)(codes.getDispositionTypeCriminalMap()).get("C"));
									//set attributes according to the last entered caseType
									if (caseTypesFound ==0){
										if (!"".equals(caseTypeArray[caseTypesFound].getDispositionType())) {
											// case is disposed, set case_status
											if (!"INAC".equals(caseTypeArray[caseTypesFound].getDispositionType()))
												session.setAttribute("case_status", CaseBean.DISPOSED);
											else
												session.setAttribute("case_status", CaseBean.INACTIVE);
											//System.out.println("Disposition not blank");
										}
										else {
											// case not disposed, set case_status
											session.setAttribute("case_status", CaseBean.OPEN);
											//System.out.println("Disposition blank");
										}
									}
								} else {
									session.setAttribute("case_status", CaseBean.OPEN);
									//System.out.println("Disposition null");
								}

								if (dDate != null) {
									caseTypeArray[caseTypesFound].setCaseAge(rset.getInt(6));
								} else {
									if(fDate != null){
										diffInDates = dt.getTime() - fDate.getTime();
										caseTypeArray[caseTypesFound].setCaseAge((int)((diffInDates/(1000*60*60*24))));
									}
									else{
										caseTypeArray[caseTypesFound].setCaseAge(0);
									}
								}

								//System.out.println("Finished reading CASE_TYPES");
								num = rset.getInt("b.oca_code");

								caseTypesFound++;
								System.out.println("Casetypes Found:" + caseTypesFound);
							}
						}
						if (caseTypesFound == 0)
						{
							System.out.println("SELECT from table CASE_TYPES failed");
							// if select failed - rollback to
							// release any locks
							needToRollback = true;
							returnCode = OPERATION_FAILED;
						} else {
							//read the last oca code of this array (which would be the original case type)
							// if >100, this case is family, otherwise it's civil
							//System.out.println("CASEBEAN: Oca Code:"+num);
							// set session attribute case_category
							if (num>=100){
								if (clerk.equals("D"))
									session.setAttribute("case_category", CaseBean.FAMILY);
								else
									session.setAttribute("case_category", CaseBean.PROBATE);
							} else
								session.setAttribute("case_category", CaseBean.CIVIL);
						}
					} // end of if ("V".equals(caseCategory))
					// continue with OFFENSES if criminal case
					if ("C".equals(caseCategory))
					{
						// close result set and statement
						// so they can be reused
						rset.close();
						selectStmt.close();
						ts = null;
						offensesFound = 0;

						//System.out.println("CRIMINAL Case id "+ csId);
						// initialize offense array
						offenseArray = new Offense[MAX_SIZE];

						// retrieve case data from OFFENSES table
						str = "select offense_no, offense, offense_date"
								+ ", disposition_date, disposition_code";
						if (dbType.equalsIgnoreCase(DB_TYPE_SQL_SERVER))
						{
							str = str + ", datediff(DAY,offense_date,disposition_date) as expr1";
						}
						else if (dbType.equalsIgnoreCase("MYSQL"))
						{
							str += ", datediff(disposition_date,offense_date) as expr1";
						}
						else
						{
							str = str + ", [disposition_date]-[offense_date] as expr1";
						}
						str = str + " from offenses"
								+ " where case_id = ?"

                          + " order by offense_date desc, offense_no desc";
						selectStmt = conn.prepareStatement(str);
						/*
                                        "SELECT offense_no,   " +
                                        "       offense,      " +
                                        "       offense_date, " +
                                        "       disposition_date, " +
                                        "       disposition_code,  " +
                                        "       [disposition_date]-[offense_date] AS Expr1 " +
                                        "FROM OFFENSES     " +
                                        "WHERE case_id = ? " +
                                        "  AND sequence_no = ? " +
                                        "ORDER BY offense_date DESC, offense_no DESC");
						 */
						selectStmt.setInt(1, csId);

						rset = selectStmt.executeQuery();

						// expect only one row
						// since only one offense
						// is supported at that time
						//if (rset.next())
						while ((rset.next()) && (offensesFound < MAX_SIZE))
						{
							offenseArray[offensesFound] = new Offense();

							// offense_no
							num = rset.getInt("offense_no");
							offenseArray[offensesFound].setOffenseNo(num);

							// offense
							str = rset.getString("offense");
							if ( str!= null) {
								offenseArray[offensesFound].setOffense(str.trim());
								offenseArray[offensesFound].setOffenseDesc((String)(codes.getOffenseMap()).get(offense));
							}

							// offense_date
							ts = rset.getTimestamp("offense_date");
							//System.out.println("FILING DATE in OFFENSES TABLE  (OFFENSES.offense_date): '" + ts + "'");
							fDate = ts;
							if (ts != null)
								offenseArray[offensesFound].setOffenseDate(sdf.format(ts));
							else
								offenseArray[offensesFound].setOffenseDate("No Date Found!");

							// disposition_date
							ts = rset.getTimestamp("disposition_date");
							dDate = ts;
							if (ts != null)
								offenseArray[offensesFound].setDispositionDate(sdf.format(ts));
							else
								offenseArray[offensesFound].setDispositionDate("&nbsp");

							str = rset.getString("disposition_code");
							if (str != null) {
								offenseArray[offensesFound].setDispositionType(str.trim());
								offenseArray[offensesFound].setDispositionTypeDesc((String)(codes.getDispositionTypeCriminalMap()).get("C"));
								if (offensesFound ==0){
									if (!"".equals(offenseArray[offensesFound].getDispositionType())) {
										// case is disposed, set case_status
										if (!"INAC".equals(offenseArray[offensesFound].getDispositionType()))
											session.setAttribute("case_status", CaseBean.DISPOSED);
										else
											session.setAttribute("case_status", CaseBean.INACTIVE);
										//System.out.println("Disposition not blank");
									}
									else {
										// case not disposed, set case_status
										session.setAttribute("case_status", CaseBean.OPEN);
										//System.out.println("Disposition blank");
									}
								}
							} else {
								session.setAttribute("case_status", CaseBean.OPEN);
								//System.out.println("Disposition null");
							}

							if (dDate != null) {
								offenseArray[offensesFound].setCaseAge(rset.getInt(6));
							} else {
								if(fDate != null){
									diffInDates = dt.getTime() - fDate.getTime();
									offenseArray[offensesFound].setCaseAge((int)((diffInDates/(1000*60*60*24))));
								}
								else {
									offenseArray[offensesFound].setCaseAge(0);
								}
							}

							//System.out.println("Finished reading OFFENSES");

							// set session attribute case_category
							session.setAttribute("case_category", CaseBean.CRIMINAL);

							offensesFound++;
							System.out.println("OFFENSES FOUND:" + offensesFound);

						}
						if (offensesFound == 0) {
							System.out.println("SELECT from table OFFENSES failed");
							// if select failed - rollback to
							// release any locks
							needToRollback = true;
							returnCode = OPERATION_FAILED;
							throw new Exception("SELECT from table OFFENSES failed while processing this case id: " +csId);
						}
					} // end of if ("C".equals(caseCategory))

					if (needToRollback == false)
					{
						// close ResultSet and statement to be reused
						if (rset != null)
							rset.close();

						if (selectStmt != null)
							selectStmt.close();

						selectStmt = conn.prepareStatement(
								"SELECT notes        " +
										"FROM NOTES          " +
										"WHERE activity_code = ? " +
								"AND detail_no = ?");

						selectStmt.setString(1, "case");
						selectStmt.setInt(2, csId);

						rset = selectStmt.executeQuery();

						// expect 1 row
						if (rset.next()) {

							str = rset.getString("notes");
							if (str != null)
								notes = str.trim();
							else
								notes = "";
						}
					}

					//Logging case read
					//System.out.println("Forming log of activity");
					String msg = "";
					String msgDetail = "";
					if ("C".equals(caseCategory)) {
						for (int i=0; i<offensesFound; i++)
							msgDetail += " offense_no='"+String.valueOf(offenseArray[i].getOffenseNo())+"' is '"+offenseArray[i].getOffense()+"' ";
					}

					if ("V".equals(caseCategory)) {
						for (int i=0; i<caseTypesFound; i++)
							msgDetail += " caseType_no='"+caseTypeArray[i].getCaseTypeNo()+"' is '"+caseTypeArray[i].getCaseType()+"' ";
					}
					msg = (String)session.getAttribute("user_name") + ",requested "+
							" case. case_id="+csId+" causeNo="+causeNumber+" in court="+court+". Cat "+caseCategory+msgDetail;
					msgLogger log = new msgLogger();
					log.writeCounty(msg, conn, (String)session.getAttribute("user_county"));
				}
				else
				{
					System.out.println("SELECT from table OPEN_CASES failed");
					// if select failed - rollback to
					// release any locks
					needToRollback = true;
					returnCode = OPERATION_FAILED;
				}
				if (causeNumber != "")
				{ // if we have a 'valid' cause number then fetch next and previous
					rset.close();
					selectStmt.close();
					selectStmt = conn.prepareStatement(
							"select a.case_id "
									+ " from cases as a "
									+ " where a.cause_no=(select max(cause_no) as cause_number from cases where cause_no<'"+causeNumber+"')");
					rset = selectStmt.executeQuery();
					//System.out.println(selectStmt.toString());
					if (rset.next())
					{ // we found previous case. Yippy!
						setPreviousCaseId(rset.getInt(1));
					}
					rset.close();
					selectStmt.close();
					selectStmt = conn.prepareStatement(
							"select a.case_id"
									+ " from cases as a,"
									+ " (select min(cause_no) as cause_number"
									+ " from cases"
									+ " where cause_no>'"+causeNumber+"') as b"
									+ " where a.cause_no=b.cause_number");
					rset = selectStmt.executeQuery();
					if (rset.next())
					{ // we found next case. Yippy!
						setNextCaseId(rset.getInt(1));
					}
				}
			}
			else
			{
				System.out.println("SELECT from table CASES failed");
				// if select failed - rollback to
				// release any locks
				needToRollback = true;
				returnCode = OPERATION_FAILED;
			}
		}
		catch (Exception e)
		{
			// preparedStatement(), executeQuery() throw this exception
			System.out.println("Caught SQLException in CaseBean.readCaseData()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;
			needToRollback = true;

			// restore session info
			session = tmpSession;

			this.exceptionEmail.emailException(request, e);


			returnCode = OPERATION_FAILED;
		}
		finally
		{
			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in CaseBean.readCaseData().");
			//logger.info("Statement/ResultSet related cleanup in CaseBean.readCaseData().");
			try
			{
				if (rset != null)
					rset.close();
				if (selectStmt != null)
					selectStmt.close();
				if ((conn != null) && (!conn.isClosed()))
				{
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					conn.close();
				}
				return returnCode;
			}
			catch (SQLException e)
			{
				// close() throw this exception
				System.out.println("Caught SQLException in CaseBean.readCaseData()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// select took place
				this.exceptionEmail.emailException(request, e);
				return returnCode;
			}
		}
	}

	/**
	 * updates information about the case
	 * <p>
	 * @param case ID as <code>int</code>
	 * @param sequence number as <code>int</code>
	 * @param HTTP request as <code>HttpServletRequest</code>
	 * @param case category as <code>String</code>
	 * @param code as <code><a href="CodeBean.html>CodeBean</a></code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *  <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *  <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *  <li><code><a href="CaseBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: data is updated in the database and stored
	 *               in the bean.
	 *
	 *               When using database supporting SELECT FOR UPDATE
	 *               lock the row first before executing UPDATE.
	 *
	 *               In the future release make sure that data originally
	 *               retrieved is unchanged before executing UPDATE.</i>
	 */
	public int updateCaseData(int csId, int seqNo,
			HttpServletRequest request,
			String caseCat,
			String key,
			String[][] civilCaseCategory,
			CodeBean codes) {

		int returnCode = SUCCESS;
		boolean needToRollback = false;
		HttpSession session = null;
		HttpSession tmpSession = null;
		int rowsUpdated = 0;
		int rowsInserted = 0;
		String str = "";
		int autoNo = 0;
		String cCat = "";
		String ipAdd = request.getRemoteAddr();

		//System.out.println("it is inside the updateCaseData....");
		if (caseCat.equals("CIVIL") || caseCat.equals("PROBATE") || caseCat.equals("FAMILY"))
			cCat = "V";
		if (caseCat.equals("CRIMINAL"))
			cCat = "C";

		Connection conn = getConnection(jndiResource);

		if (conn == null)
			return NO_DATABASE_CONNECTION; // no connection

		PreparedStatement updateStmt = null;
		PreparedStatement insertStmt = null;
		PreparedStatement selectStmt = null;
		ResultSet rset = null;

		try {

			// get current session based on HttpServletRequest)
			// and don't create a new on (create=false)
			session = request.getSession(false);

			// save session info in case it needs to be restored
			tmpSession = session;

			// define format used by form
			SimpleDateFormat sdf1 = new SimpleDateFormat ("MM/dd/yyyy");
			// define format for JDBC Timestamp
			SimpleDateFormat sdf2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

			// capture current date/time and format it
			java.util.Date dt = new java.util.Date();

			String type = "";
			System.out.println("CaseBean.updateCaseData: Cause Number: " + causeNumber + " AGENCY??? " + agencyCaseNo);


			selectStmt = conn.prepareStatement(
					"SELECT CASES.cause_no " +
							"FROM CASES " +
							"WHERE (CASES.cause_no)='"+ causeNumber + "' " +
							"AND CASES.case_id <> " + csId + " " +
							"AND (CASES.category)="+"'"+ cCat + "'");

			rset = selectStmt.executeQuery();

			while (rset.next()) {
				caseFound++;

			} // end of while

			if (caseFound != 0) {
				if (!(key.equals("WL") || (key.equals("IJC")) || (key.equals("BMC")) || (key.equals("IJD")) || (key.equals("HJ"))
						|| (((String)session.getAttribute("user_county")).equals("HY")) )) {
					returnCode = DUPLICATE;
					needToRollback = true;
				} else {
					returnCode = DUPLICATE_WARNING;
					needToRollback = false;
				}
			} else {
				needToRollback = false;
				System.out.println("no duplicate found...continue");
			}

			//WE NEED TO CHECK IF WE ARE MODIFYNG THE CAUSE NUMBER, WE NEED TO CORRECT THE AUTO NUMBERING IF THIS IS THE CASE
			String autoCauseUpdate ="";
			boolean causeChange = false;
			selectStmt.close();
			rset.close();

			selectStmt = conn.prepareStatement("SELECT cause_no FROM cases WHERE case_id = ?");

			selectStmt.setInt(1, csId);

			rset = selectStmt.executeQuery();

			if(rset.next()){
				String previousCause = rset.getString(1);
				if(previousCause != null && !previousCause.equals(causeNumber)){
					autoCauseUpdate = ",auto_number = ? ";
					causeChange = true;
				}
			}
			//END OF CAUSE NUMBER CHECK

			if (needToRollback == false) {
				// update row in CASES table
				updateStmt = conn.prepareStatement(
						"UPDATE CASES                   " +
								"SET court = ?,                 " +
								"    cause_no = ?,              " +
								"    style = ?,                 " +
								"    facility = ?,              " +
								"    section = ?,               " +
								"    aisle = ?,                 " +
								"    row = ?,                   " +
								"    shelf = ?,                 " +
								"    box = ?,                   " +
								"    seq_no = ?,                " +
								"    sealed = ?,                " +
								"    NOPP = ?,                  " +
								"    last_updated_datetime = ?, " +
								"    last_updated_by = ?,       " +
								"    last_updated_from = ?,     " +
								"    jury_demand = ?,           " +
								"    agency_case_no = ?	        " +
								autoCauseUpdate +
						"WHERE case_id = ?");

				//System.out.println("After the update in CASES table");
				if (!"0".equals(court))
					updateStmt.setString(1, court);
				else
					updateStmt.setString(1, (String)(codes.getCourtRevMap()).get(courtDesc));
				//System.out.println("After the first value update in CASES table");

				updateStmt.setString(2, causeNumber);

				StringTokenizer st = new StringTokenizer (causeNumber, "-");
				try {
					while(st.hasMoreElements())
						str = st.nextToken().trim();
					autoNo = Integer.parseInt(str);
				}
				catch (NumberFormatException e) {
					System.out.println ("Caught NumberFormatException in CaseBean.updateCaseData!");
					System.out.println(e);
				}
				catch (NoSuchElementException e) {
					System.out.println ("Caught NoSuchElementException in CaseBean.updateCaseData!");
					System.out.println(e);
				}

				updateStmt.setString(3, caseStyle);

				if (!"".equals(facility))
					updateStmt.setString(4, facility);
				else
					updateStmt.setNull(4, Types.VARCHAR);

				if (!"".equals(section))
					updateStmt.setString(5, section);
				else
					updateStmt.setNull(5, Types.VARCHAR);
				if (!"".equals(aisle))
					updateStmt.setString(6, aisle);
				else
					updateStmt.setNull(6, Types.VARCHAR);
				if (!"".equals(row))
					updateStmt.setString(7, row);
				else
					updateStmt.setNull(7, Types.VARCHAR);
				if (!"".equals(shelf))
					updateStmt.setString(8, shelf);
				else
					updateStmt.setNull(8, Types.VARCHAR);
				if (!"".equals(box))
					updateStmt.setString(9, box);
				else
					updateStmt.setNull(9, Types.VARCHAR);
				updateStmt.setInt(10, seqNum);
				if (!"".equals(sealed))
					updateStmt.setString(11, sealed);
				else
					updateStmt.setNull(11, Types.VARCHAR);

				if (!"".equals(nopp))
					updateStmt.setTimestamp(12, Timestamp.valueOf(sdf2.format(sdf1.parse(nopp))));
				else
					updateStmt.setNull(12, Types.TIMESTAMP);

				updateStmt.setTimestamp(13, Timestamp.valueOf(sdf2.format(dt)));
				updateStmt.setString(14, (String)session.getAttribute("user_name"));
				updateStmt.setString(15, ipAdd);
				//System.out.println("Before if !\"\".equals(juryDemand) - juryDemand = '" + juryDemand + "'");
				if (!"".equals(juryDemand))
					updateStmt.setString(16, juryDemand);
				else
					updateStmt.setNull(16, Types.VARCHAR);
				//System.out.println("After updateStmt.setString or Null(17,...)");

				System.out.println(agencyCaseNo);
				if(!"".equals(agencyCaseNo))
					updateStmt.setString(17, agencyCaseNo);
				else
					updateStmt.setNull(17, Types.VARCHAR);

				if(causeChange){
					updateStmt.setString(18, "0");// FORZING THE AUTO NUMBER TO CHANGE BACK TO ZERO
					updateStmt.setInt(19, csId);
				}else
					updateStmt.setInt(18, csId);

				rowsUpdated = updateStmt.executeUpdate();


				if (rowsUpdated != 1) {
					System.out.println("Update in table CASES failed");
					// if update failed - rollback to
					// release any locks
					needToRollback = true;
					returnCode = OPERATION_FAILED;
				} else {
					// update in CASES successful

					//Logging case UPDATE
					String msg = "";
					msg = (String)session.getAttribute("user_name") + ",UPDATED "+
							" case. case_id="+csId+" with VALUES causeNo="+causeNumber+" in court="+court+". style='"+caseStyle+"'";
					msgLogger log = new msgLogger();
					log.writeCounty(msg, conn, (String)session.getAttribute("user_county"));


					// close statement to be reused
					updateStmt.close();
					rowsUpdated = 0;

					// insert row into OPEN_CASES
					updateStmt = conn.prepareStatement(
							"UPDATE OPEN_CASES              " +
									"SET volume_page = ?,           " +
									"    filing_date = ?,           " +
									"    filed_type = ?,            " +
									"    type = ?,                  " +
									"    capias = ?,                " +
									"    judgment = ?,              " +
									"    jailed_date = ?,           " +
									"    comments = ?,              " +
									"    jury_examined = ?,         " +
									"    jury_sworn = ?,            " +
									"    warrant_outstanding = ?,   " +
									"    arrest_date = ?,           " +
									"    last_updated_datetime = ?, " +
									"    last_updated_by = ?,       " +
									"    last_updated_from = ?,     " +
									"	warrant_date = ?			" +
									"WHERE case_id = ?              " +
							"AND   sequence_no = ?");

					// volume/page
					updateStmt.setString(1, volumePage);

					// prepare required Timestamp format
					updateStmt.setTimestamp(2, Timestamp.valueOf(sdf2.format(sdf1.parse(filingDate))));
					System.out.println("Filing Date is " + filingDate);

					if (!"0".equals(filedType))
						updateStmt.setString(3, filedType);
					else
						updateStmt.setString(3, (String)(codes.getFiledTypeRevMap()).get(filedTypeDesc));

					// check case category
					if ((CaseBean.CIVIL).equals(caseCat) || (CaseBean.PROBATE).equals(caseCat) || (CaseBean.FAMILY).equals(caseCat))
						// civil case
						// set OPEN_CASES.type = 'V'
						updateStmt.setString(4, "V");

					if ((CaseBean.CRIMINAL).equals(caseCat))
						// criminal case
						// set OPEN_CASES.type = 'C'
						updateStmt.setString(4, "C");

					updateStmt.setString(5, capias);

					if (!"".equals(judgment))
						updateStmt.setString(6, judgment);
					else
						updateStmt.setNull(6, Types.VARCHAR);

					if (!"".equals(jailedDate))
						updateStmt.setTimestamp(7, Timestamp.valueOf(sdf2.format(sdf1.parse(jailedDate))));
					else
						updateStmt.setNull(7, Types.TIMESTAMP);


					if (!"".equals(comments))
						updateStmt.setString(8, comments);
					else
						updateStmt.setNull(8, Types.VARCHAR);

					//System.out.println("Jury examined is " + juryExamined);
					if (!"".equals(juryExamined))
						updateStmt.setString(9, juryExamined);
					else
						updateStmt.setNull(9, Types.VARCHAR);

					if (!"".equals(jurySworn))
						updateStmt.setString(10, jurySworn);
					else
						updateStmt.setNull(10, Types.VARCHAR);

					if (!"".equals(warrantOutstanding))
						updateStmt.setString(11, warrantOutstanding);
					else
						updateStmt.setNull(11, Types.VARCHAR);

					if (!"".equals(offenseDate))
						updateStmt.setTimestamp(12, Timestamp.valueOf(sdf2.format(sdf1.parse(offenseDate))));
					else
						updateStmt.setNull(12, Types.TIMESTAMP);

					updateStmt.setTimestamp(13, Timestamp.valueOf(sdf2.format(dt)));
					updateStmt.setString(14, (String)session.getAttribute("user_name"));
					updateStmt.setString(15, ipAdd);

					if(!"".equals(warrantOutstanding))
						updateStmt.setTimestamp(16, Timestamp.valueOf(sdf2.format(dt)));
					else
						updateStmt.setNull(16, Types.TIMESTAMP);


					updateStmt.setInt(17, csId);
					updateStmt.setInt(18, seqNo);

					System.out.println(updateStmt);
					rowsUpdated = updateStmt.executeUpdate();


					if (rowsUpdated != 1) {
						System.out.println("Update in table OPEN_CASES failed");
						// if update failed - rollback to
						// release any locks
						needToRollback = true;
						returnCode = OPERATION_FAILED;
					} else {
						// update in OPEN_CASES successful
						if (needToRollback == false) {
							// Is there a record in NOTES table

							selectStmt = conn.prepareStatement(
									"SELECT *        " +
											"FROM NOTES          " +
											"WHERE activity_code = ? " +
									"AND detail_no = ?");

							selectStmt.setString(1, "case");
							selectStmt.setInt(2, csId);

							rset = selectStmt.executeQuery();

							// expect 1 row
							if (rset.next()) {
								// If record found,
								// update notes record in NOTES

								// close statement to be reused
								if (updateStmt != null)
									updateStmt.close();
								rowsUpdated = 0;

								updateStmt = conn.prepareStatement(
										"UPDATE NOTES         " +
												"SET notes = ?,       " +
												"    last_updated_by = ?, " +
												"    last_updated_datetime = ?, " +
												"    last_updated_from = ? " +
												"WHERE activity_code = ? " +
										"AND detail_no = ?");


								updateStmt.setString(1, notes);
								updateStmt.setString(2, (String)session.getAttribute("user_name"));
								updateStmt.setTimestamp(3, Timestamp.valueOf(sdf2.format(dt)));
								updateStmt.setString(4, ipAdd);
								updateStmt.setString(5, "case");
								updateStmt.setInt(6, csId);

								rowsUpdated = updateStmt.executeUpdate();

								if (rowsUpdated < 1) {
									System.out.println("Update in table NOTES failed");
									// if update failed - rollback to
									// release any locks
									needToRollback = true;
									returnCode = OPERATION_FAILED;
								}
							} else {
								// If record not found
								// insert notes record in NOTES
								rowsInserted = 0;

								insertStmt = conn.prepareStatement(
										"INSERT INTO NOTES (    " +
												"     activity_code,    " +
												"     detail_no,        " +
												"     notes,            " +
												"     last_updated_by,  " +
												"     last_updated_datetime, " +
												"     last_updated_from)  " +
												"VALUES "          +
										"(?, ?, ?, ?, ?, ?)");

								insertStmt.setString(1, "case");
								insertStmt.setInt(2, csId);
								insertStmt.setString(3, notes);
								insertStmt.setString(4, (String)session.getAttribute("user_name"));
								insertStmt.setTimestamp(5, Timestamp.valueOf(sdf2.format(dt)));
								insertStmt.setString(6, ipAdd);

								rowsInserted = insertStmt.executeUpdate();

								if (rowsInserted != 1) {
									System.out.println("Insert to table NOTES failed");
									// if insert failed - rollback to
									// release any locks
									needToRollback = true;
									returnCode = OPERATION_FAILED;
								}
							}

						} // end of if (needToRollback == false)

					} // end of update in OPEN_CASES successful (rowsUpdated == 1)

				} // end of update in CASES successful (rowsUpdated == 1)
			} // end of else (caseFound != 0)
		} // end of if (needToRollback == false)

		catch (ParseException e) {
			// parse() throws this exception
			System.out.println("Caught ParseException in CaseBean.updateCaseData()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			// restore session info
			session = tmpSession;

			needToRollback = true;
			returnCode = DATABASE_EXCEPTION;
		}

		catch (SQLException e) {
			// preparedStatement(), executeUpdate() throw this exception
			System.out.println("Caught SQLException in CaseBean.updateCaseData()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			// restore session info
			session = tmpSession;

			needToRollback = true;
			returnCode = DATABASE_EXCEPTION;
		}
		finally {
			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in CaseBean.updateCaseData().");
			try {
				if (updateStmt != null)    // Close statement
					updateStmt.close();
				if (insertStmt != null)
					insertStmt.close();
				if (selectStmt != null)
					selectStmt.close();
				//if (rset != null)         // Close ResultSet
				//rset.close();
				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();

					conn.close();
				}

				return returnCode;
			}
			catch (SQLException e) {
				// close() throw this exception
				System.out.println("Caught SQLException in CaseBean.updateCaseData()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// updates took place
				return returnCode;
			}
		}
	}

	//This method is added to store the old location into the CASE_ACTIVITY table
	/**
	 * stores case information in the database
	 * <p>
	 * @param HTTP request as <code>HttpServletRequest</code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *  <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *  <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *  <li><code><a href="CaseBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: data is stored in the database </i>
	 */
	public int storeCaseActivityData(HttpServletRequest request) {

		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int rowsInserted = 0;

		HttpSession session = null;
		HttpSession tmpSession = null;

		Connection conn = getConnection(jndiResource);

		if (conn == null)
			return NO_DATABASE_CONNECTION; // no connection

		PreparedStatement insertStmt = null;
		PreparedStatement selectStmt = null;
		PreparedStatement updateStmt = null;
		ResultSet rset = null;

		try {

			// get current session based on HttpServletRequest)
			// and don't create a new on (create=false)
			session = request.getSession(false);

			// save session info in case it needs to be restored
			tmpSession = session;

			// define format used by form
			SimpleDateFormat sdf1 = new SimpleDateFormat ("MM/dd/yyyy");
			// define format for JDBC Timestamp
			SimpleDateFormat sdf2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
			System.out.println("Before insert into cases activity ");

			// insert row into CASE_ACTIVITY table
			insertStmt = conn.prepareStatement(
					"INSERT INTO CASE_ACTIVITY (      " +
							"       case_id,              " +
							"       facility,             " +
							"       section,              " +
							"       aisle,                " +
							"       row,                  " +
							"       shelf,                " +
							"       box,                  " +
							"       seq_no,               " +
							"       time_stamp,           " +
							"       user_id)              " +
							"       VALUES                " +
					"    (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			insertStmt.setInt(1, caseId);
			insertStmt.setString(2, facility);
			insertStmt.setString(3, section);
			insertStmt.setString(4, aisle);
			insertStmt.setString(5, row);
			insertStmt.setString(6, shelf);
			insertStmt.setString(7, box);
			insertStmt.setInt(8, seqNum);
			insertStmt.setTimestamp(9, Timestamp.valueOf(sdf2.format(sdf2.parse(enteredDateTime))));
			insertStmt.setString(10, enteredUser);

			rowsInserted = insertStmt.executeUpdate();
			System.out.println("Number of row inserted "+ rowsInserted);
			if (rowsInserted != 1) {
				System.out.println("Insert to table CASE_ACTIVITY failed");
				// if insert failed - rollback to
				// release any locks
				needToRollback = true;
				returnCode = OPERATION_FAILED;
			} else {
				System.out.println("insert to CASE_ACTIVITY successful ");
			} // end of insert to CASES successful (rowsInserted == 1)
		} // end of try

		catch (SQLException e) {
			// preparedStatement(), executeUpdate() throw this exception
			System.out.println("Caught SQLException in iTrackerBean.storeCaseActivityData()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			// restore session info
			session = tmpSession;

			needToRollback = true;
			returnCode = DATABASE_EXCEPTION;
		}
		finally {
			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in iTrackerBean.storeCaseActivityData().");
			try {
				if (rset != null)         // Close ResultSet
					rset.close();
				if (insertStmt != null)    // Close statements
					insertStmt.close();
				if (selectStmt != null)
					selectStmt.close();

				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();

					conn.close();
				}

				return returnCode;
			}
			catch (SQLException e) {
				// close() throw this exception
				System.out.println("Caught SQLException in CaseBean.storeCaseActivityData()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// inserts took place
				return returnCode;
			}
		}
	}
	/**
	 * **************************CURRENT OFFENSES / CASETYPES ON NEW CASES ONLY
	 * <p>
	 * @param code as <code><a href="CodeBean.html>CodeBean</a></code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *  <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *  <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *  <li><code><a href="caseBean.html#NO_DATA_FOUND">NO_DATA_FOUND</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments:  </i>
	 */
	public int searchNewCodeArrays(HttpServletRequest request) {

		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int recsFound = 0;

		// temp variables to fetch data
		String str = "";
		int i = 0;
		int j = 0;
		int num = 0;
		int crArraySize = 0;
		int cvArraySize = 0;
		int othArraySize = 0;

		Connection conn = getConnection(jndiResource);

		if (conn == null)
			return NO_DATABASE_CONNECTION; // no connection

		PreparedStatement selectStmt = null;
		Statement stmt = null;
		ResultSet rset = null;



		try {

			// define format used by form
			SimpleDateFormat sdf = new SimpleDateFormat ("MM/dd/yyyy");

			selectStmt = conn.prepareStatement(
					"SELECT COUNT(*) FROM CJIS					" +
							"WHERE not description like '*%'			" +
							"AND not oca_code = 0						" +
					"AND not oca_code is null					");
			rset = selectStmt.executeQuery();

			if (rset.next())
				crArraySize = rset.getInt(1);

			// initialize offense results array
			newOffensesArray = new String[crArraySize][3];

			// close ResultSet and Statement
			// so they can be reused
			if (rset != null)
				rset.close();
			if (selectStmt != null)
				selectStmt.close();

			selectStmt = conn.prepareStatement(
					"SELECT COUNT(*) FROM CODE_TABLE 			" +
							"WHERE CODE_TABLE.code_type = 'case_type' 	" +
							"and not CODE_TABLE.value like '*%'			" +
							"AND not CODE_TABLE.oca_code = 0			" +
							"AND not CODE_TABLE.oca_code is null		" +
					"AND CODE_TABLE.oca_code <100				");

			rset = selectStmt.executeQuery();

			if (rset.next())
				cvArraySize = rset.getInt(1);

			// initialize casetypes results array
			newCaseTypesArray = new String[cvArraySize][3];

			// close ResultSet and Statement
			// so they can be reused
			if (rset != null)
				rset.close();
			if (selectStmt != null)
				selectStmt.close();

			selectStmt = conn.prepareStatement(
					"SELECT COUNT(*) FROM CODE_TABLE 			" +
							"WHERE CODE_TABLE.code_type = 'case_type' 	" +
							"and not CODE_TABLE.value like '*%'			" +
							"AND not CODE_TABLE.oca_code = 0			" +
							"AND not CODE_TABLE.oca_code is null		" +
					"AND CODE_TABLE.oca_code >100 and CODE_TABLE.oca_code <112 ");

			rset = selectStmt.executeQuery();

			if (rset.next())
				othArraySize = rset.getInt(1);

			// initialize casetypes results array
			newOthersArray = new String[othArraySize][3];

			// close ResultSet and Statement
			// so they can be reused
			if (rset != null)
				rset.close();
			if (selectStmt != null)
				selectStmt.close();

			stmt = conn.createStatement();

			StringBuffer query = new StringBuffer(
					"SELECT                  					" +
							"cjis_code,              					" +
							"description,            					" +
							"oca_code                					" +
							"FROM CJIS         						" +
							"WHERE not description like '*%' 			" +
							"AND not oca_code = 0						" +
							"AND not oca_code is null					" +
					"order by description ");


			//System.out.println(query);
			num = 0;
			rset = stmt.executeQuery(query.toString());

			while (rset.next()) {

				str = "";
				//cjis code
				str = rset.getString(1);
				if (str != null)
					newOffensesArray[num][0] = str.trim();
				else
					newOffensesArray[num][0] = "";
				//description
				str = rset.getString(2);
				if (str != null)
					newOffensesArray[num][1] = str.trim();
				else
					newOffensesArray[num][1] = "";
				//oca_code
				str = rset.getString(3);
				if (str != null)
					newOffensesArray[num][2] = str.trim();
				else
					newOffensesArray[num][2] = "";

				num++;
			}

			// close ResultSet and Statement
			// so they can be reused
			if (rset != null)
				rset.close();
			if (selectStmt != null)
				selectStmt.close();

			selectStmt = conn.prepareStatement(
					"SELECT                    				" +
							"code,                  					" +
							"value,              						" +
							"oca_code                					" +
							"FROM CODE_TABLE         					" +
							"WHERE CODE_TABLE.code_type = 'case_type' 	" +
							"AND not CODE_TABLE.value like '*%' 		" +
							"AND not CODE_TABLE.oca_code = 0			" +
							"AND not CODE_TABLE.oca_code is null		" +
							"AND CODE_TABLE.oca_code <100				" +
					"order by code_table.value ");

			rset = selectStmt.executeQuery();
			while (rset.next()) {

				str = "";
				//cjis code
				str = rset.getString(1);
				//System.out.println("this is the  code:  "+str);
				if (str != null)
					newCaseTypesArray[i][0] = str.trim();
				else
					newCaseTypesArray[i][0] = "";
				//description
				str = rset.getString(2);
				if (str != null)
					newCaseTypesArray[i][1] = str.trim();
				else
					newCaseTypesArray[i][1] = "";
				//oca_code
				str = rset.getString(3);
				if (str != null)
					newCaseTypesArray[i][2] = str.trim();
				else
					newCaseTypesArray[i][2] = "";

				i++;

			}

			// close ResultSet and Statement
			// so they can be reused
			if (rset != null)
				rset.close();
			if (selectStmt != null)
				selectStmt.close();

			selectStmt = conn.prepareStatement(
					"SELECT                    				" +
							"code,                  					" +
							"value,              						" +
							"oca_code                					" +
							"FROM CODE_TABLE         					" +
							"WHERE CODE_TABLE.code_type = 'case_type' 	" +
							"AND not CODE_TABLE.value like '*%' 		" +
							"AND not CODE_TABLE.oca_code = 0			" +
							"AND not CODE_TABLE.oca_code is null		" +
							"AND CODE_TABLE.oca_code > 100 and CODE_TABLE.oca_code < 112	" +
					"ORDER BY code_table.value ");
			j = 0;
			rset = selectStmt.executeQuery();
			while (rset.next()) {

				str = "";
				//cjis code
				str = rset.getString(1);
				//System.out.println("this is the family/probate code:  "+str);
				if (str != null)
					newOthersArray[j][0] = str.trim();
				else
					newOthersArray[j][0] = "";
				//description
				str = rset.getString(2);
				if (str != null)
					newOthersArray[j][1] = str.trim();
				else
					newOthersArray[j][1] = "";
				//oca_code
				str = rset.getString(3);
				if (str != null)
					newOthersArray[j][2] = str.trim();
				else
					newOthersArray[j][2] = "";

				j++;

			}

		}
		catch (SQLException e) {
			// createStatement(), executeQuery() throw this exception
			System.out.println("Caught SQLException in caseBean.searchNewCodeArrays()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;
			needToRollback = true;

			returnCode = DATABASE_EXCEPTION;
		}
		finally {
			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in caseBean.searchNewCodeArrays().");
			try {
				if (rset != null)         // Close ResultSet
					rset.close();
				if (stmt != null)   // Close statement
					stmt.close();

				// Note: If AutoCommit is false
				// before closing a connection need to
				// issue commit() or rollback() even though
				// no UPDATE/INSERT/DELETE operations were
				// issued
				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();

					conn.close();
				}

				return returnCode;
			}
			catch (SQLException e) {
				// close() throw this exception
				System.out.println("Caught SQLException in caseBean.searchNewCodeArrays()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// select took place
				return returnCode;
			}
		}
	}








	/**
	 * <p>
	 * @param code as <code><a href="CodeBean.html>CodeBean</a></code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *  <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *  <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *  <li><code><a href="caseBean.html#NO_DATA_FOUND">NO_DATA_FOUND</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments:  </i>
	 */
	public int searchAllCodeArrays() {

		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int recsFound = 0;

		// temp variables to fetch data
		String str = "";
		int i = 0;
		int j = 0;
		int num = 0;
		int crArraySize = 0;
		int cvArraySize = 0;
		int othArraySize = 0;

		Connection conn = getConnection(jndiResource);

		if (conn == null)
			return NO_DATABASE_CONNECTION; // no connection

		PreparedStatement selectStmt = null;
		Statement stmt = null;
		ResultSet rset = null;



		try {

			// define format used by form
			SimpleDateFormat sdf = new SimpleDateFormat ("MM/dd/yyyy");

			selectStmt = conn.prepareStatement(
					"SELECT COUNT(*) FROM CJIS					" +
							"WHERE not description like '*%'			" +
							"AND not oca_code = 0						" +
					"AND not oca_code is null					");
			rset = selectStmt.executeQuery();

			if (rset.next())
				crArraySize = rset.getInt(1);

			// initialize offense results array
			newOffensesArray = new String[crArraySize][3];

			// close ResultSet and Statement
			// so they can be reused
			if (rset != null)
				rset.close();
			if (selectStmt != null)
				selectStmt.close();

			selectStmt = conn.prepareStatement(
					"SELECT COUNT(*) FROM CODE_TABLE 			" +
							"WHERE CODE_TABLE.code_type = 'case_type' 	" +
							"AND not CODE_TABLE.oca_code = 0			" +
							"AND not CODE_TABLE.oca_code is null		" +
					"AND CODE_TABLE.oca_code <100				");

			rset = selectStmt.executeQuery();

			if (rset.next())
				cvArraySize = rset.getInt(1);

			// initialize casetypes results array
			newCaseTypesArray = new String[cvArraySize][3];

			// close ResultSet and Statement
			// so they can be reused
			if (rset != null)
				rset.close();
			if (selectStmt != null)
				selectStmt.close();

			selectStmt = conn.prepareStatement(
					"SELECT COUNT(*) FROM CODE_TABLE 			" +
							"WHERE CODE_TABLE.code_type = 'case_type' 	" +
							"AND not CODE_TABLE.oca_code = 0			" +
							"AND not CODE_TABLE.oca_code is null		" +
					"AND CODE_TABLE.oca_code >100 and CODE_TABLE.oca_code <112 ");

			rset = selectStmt.executeQuery();

			if (rset.next())
				othArraySize = rset.getInt(1);

			// initialize casetypes results array
			newOthersArray = new String[othArraySize][3];

			// close ResultSet and Statement
			// so they can be reused
			if (rset != null)
				rset.close();
			if (selectStmt != null)
				selectStmt.close();

			stmt = conn.createStatement();

			StringBuffer query = new StringBuffer(
					"SELECT                  					" +
							"cjis_code,              					" +
							"description,            					" +
							"oca_code                					" +
							"FROM CJIS         						" +
							"WHERE " +
							"not oca_code = 0						" +
							"AND not oca_code is null					" +
					"order by description ");


			//System.out.println(query);
			num = 0;
			rset = stmt.executeQuery(query.toString());

			while (rset.next()) {

				str = "";
				//cjis code
				str = rset.getString(1);
				if (str != null)
					newOffensesArray[num][0] = str.trim();
				else
					newOffensesArray[num][0] = "";
				//description
				str = rset.getString(2);
				if (str != null)
					newOffensesArray[num][1] = str.trim();
				else
					newOffensesArray[num][1] = "";
				//oca_code
				str = rset.getString(3);
				if (str != null)
					newOffensesArray[num][2] = str.trim();
				else
					newOffensesArray[num][2] = "";

				num++;
			}

			// close ResultSet and Statement
			// so they can be reused
			if (rset != null)
				rset.close();
			if (selectStmt != null)
				selectStmt.close();

			selectStmt = conn.prepareStatement(
					"SELECT                    				" +
							"code,                  					" +
							"value,              						" +
							"oca_code                					" +
							"FROM CODE_TABLE         					" +
							"WHERE CODE_TABLE.code_type = 'case_type' 	" +
							"AND not CODE_TABLE.oca_code = 0			" +
							"AND not CODE_TABLE.oca_code is null		" +
							"AND CODE_TABLE.oca_code <100				" +
					"order by code_table.value ");

			rset = selectStmt.executeQuery();
			while (rset.next()) {

				str = "";
				//cjis code
				str = rset.getString(1);
				//System.out.println("this is the  code:  "+str);
				if (str != null)
					newCaseTypesArray[i][0] = str.trim();
				else
					newCaseTypesArray[i][0] = "";
				//description
				str = rset.getString(2);
				if (str != null)
					newCaseTypesArray[i][1] = str.trim();
				else
					newCaseTypesArray[i][1] = "";
				//oca_code
				str = rset.getString(3);
				if (str != null)
					newCaseTypesArray[i][2] = str.trim();
				else
					newCaseTypesArray[i][2] = "";

				i++;

			}

			// close ResultSet and Statement
			// so they can be reused
			if (rset != null)
				rset.close();
			if (selectStmt != null)
				selectStmt.close();

			selectStmt = conn.prepareStatement(
					"SELECT                    				" +
							"code,                  					" +
							"value,              						" +
							"oca_code                					" +
							"FROM CODE_TABLE         					" +
							"WHERE CODE_TABLE.code_type = 'case_type' 	" +
							"AND not CODE_TABLE.oca_code = 0			" +
							"AND not CODE_TABLE.oca_code is null		" +
							"AND CODE_TABLE.oca_code > 100 and CODE_TABLE.oca_code < 112	" +
					"ORDER BY code_table.value ");
			j = 0;
			rset = selectStmt.executeQuery();
			while (rset.next()) {

				str = "";
				//cjis code
				str = rset.getString(1);
				//System.out.println("this is the family/probate code:  "+str);
				if (str != null)
					newOthersArray[j][0] = str.trim();
				else
					newOthersArray[j][0] = "";
				//description
				str = rset.getString(2);
				if (str != null)
					newOthersArray[j][1] = str.trim();
				else
					newOthersArray[j][1] = "";
				//oca_code
				str = rset.getString(3);
				if (str != null)
					newOthersArray[j][2] = str.trim();
				else
					newOthersArray[j][2] = "";

				j++;

			}

		}
		catch (SQLException e) {
			// createStatement(), executeQuery() throw this exception
			System.out.println("Caught SQLException in caseBean.searchNewCodeArrays()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;
			needToRollback = true;

			returnCode = DATABASE_EXCEPTION;
		}
		finally {
			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in caseBean.searchNewCodeArrays().");
			try {
				if (rset != null)         // Close ResultSet
					rset.close();
				if (stmt != null)   // Close statement
					stmt.close();

				// Note: If AutoCommit is false
				// before closing a connection need to
				// issue commit() or rollback() even though
				// no UPDATE/INSERT/DELETE operations were
				// issued
				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();

					conn.close();
				}

				return returnCode;
			}
			catch (SQLException e) {
				// close() throw this exception
				System.out.println("Caught SQLException in caseBean.searchNewCodeArrays()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// select took place
				return returnCode;
			}
		}
	}












	public int enterAssociatedCase(HttpServletRequest request) {

		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int rowsInserted = 0;
		int autoNumber = 0;
		int tmpCaseId = 0;
		String fYear = "";
		String cType = "";
		String origCaseId = request.getParameter("origCaseId");
		String caseCauseNo = "";
		HttpSession session = null;
		HttpSession tmpSession = null;
		session = request.getSession(false);

		String ipAdd = request.getRemoteAddr();

		// define format used by form
		SimpleDateFormat sdf1 = new SimpleDateFormat ("MM/dd/yyyy");
		// define format for JDBC Timestamp
		SimpleDateFormat sdf2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

		// capture current date/time and format it
		java.util.Date dt = new java.util.Date();

		Connection conn = getConnection(jndiResource);

		if (conn == null)
			return NO_DATABASE_CONNECTION; // no connection

		PreparedStatement insertStmt = null;
		PreparedStatement selectStmt = null;
		ResultSet rset = null;

		try {

			fYear = filingDate.substring(filingDate.length()-2, filingDate.length());
			System.out.println("fYear = "+fYear);
			if (caseCategory.equals("C")) { //caseCategory = 'C'

				selectStmt = conn.prepareStatement(
						"SELECT MAX(auto_number)   						" +
								"FROM CASES                						" +
								"WHERE CASES.category ='C' and court='"+court+"'" +
								"and LEFT(CAUSE_NO,2)='"+fYear+"' ");

				rset = selectStmt.executeQuery();

				if (rset.next()) {
					// read max auto_number
					autoNumber = rset.getInt(1)+2;
					//System.out.println("Retrieved next auto number to be given in Starr DC= " + autoNumber);
				} else {
					if (court.equals("0229"))
						autoNumber = 2;
					else
						autoNumber = 1;
				}

				if (court.equals("0229"))
					cType = "-CRS-";
				else
					cType = "-CR-";

				caseCauseNo = fYear + cType +String.valueOf(autoNumber);

				String sql =
						"INSERT INTO CASES (state, county, category, subcategory, 			"+
								"clerk, court, cause_no, auto_number, style, last_updated_datetime, "+
								"last_updated_by, last_updated_from, Sealed, Seq_no, jury_demand )	"+
								"SELECT state, county, category, subcategory, clerk, court, 		"+
								"?, ?, style, ?, ?, ?, Sealed, Seq_no, jury_demand FROM CASES WHERE	"+
								"case_id = ?";

				//System.out.println(sql);
				insertStmt = conn.prepareStatement(sql);
				insertStmt.setString(1, caseCauseNo);
				insertStmt.setInt(2, autoNumber);
				insertStmt.setTimestamp(3, Timestamp.valueOf(sdf2.format(dt)));
				insertStmt.setString(4, (String)session.getAttribute("user_name"));
				insertStmt.setString(5, ipAdd);
				insertStmt.setString(6, origCaseId);

				rowsInserted = insertStmt.executeUpdate();

				if (rowsInserted != 1) {

					System.out.println("Couldn't enter associated case into cases table...");
					// in case of exception set isDataSubmitted flag to false
					isDataSubmitted = false;
					needToRollback = true;

					returnCode = DATABASE_EXCEPTION;
				} else {
					System.out.println("WOHOOO! ENTERED "+rowsInserted+" into cases table.(ass case)");

					selectStmt = conn.prepareStatement(
							"SELECT MAX(case_id)      " +
									"FROM CASES                " +
									"WHERE last_updated_by = ? " +
							"GROUP BY last_updated_by ");

					selectStmt.setString(1, (String)session.getAttribute("user_name"));
					rset = selectStmt.executeQuery();
					if (rset.next()) {
						// read newly inserted case_id
						tmpCaseId = rset.getInt(1);
						seqNum = 0; //rset.getInt(2);
						System.out.println("Retrieved newly inserted case_id = " + tmpCaseId);
						//Now to enter into open_cases, including all fields for other may be used in future

						if (insertStmt != null)
							insertStmt.close();

						insertStmt = conn.prepareStatement(
								"insert into open_cases (case_id, sequence_no, filing_date, 	" +
										"filed_type, type, volume_page, disposition_date, 				" +
										"disposition_code, judgment, comments, jury_examined, 			" +
										"jury_sworn, warrant_no, warrant_date, warrant_outstanding,		" +
										"arrest_date, jailed_date, capias, last_updated_datetime,		" +
										"last_updated_by, last_updated_from)					" +
										"SELECT ?, ?, filing_date, filed_type, type, volume_page,		" +
										"disposition_date, disposition_code, judgment, comments,		" +
										"jury_examined, jury_sworn, warrant_no, warrant_date,			" +
										"warrant_outstanding, arrest_date, jailed_date, capias,			" +
								"?, ?, ? FROM OPEN_CASES WHERE CASE_ID = ?			"	);

						insertStmt.setInt(1, tmpCaseId); //new case_id
						insertStmt.setInt(2, seqNum); //seq_no
						insertStmt.setTimestamp(3, Timestamp.valueOf(sdf2.format(dt)));
						insertStmt.setString(4, (String)session.getAttribute("user_name"));
						insertStmt.setString(5, ipAdd);
						insertStmt.setString(6, origCaseId); //case_id

						rowsInserted = insertStmt.executeUpdate();

						if (rowsInserted != 1) {

							System.out.println("Couldn't enter associated case into open_cases table...");
							// in case of exception set isDataSubmitted flag to false
							isDataSubmitted = false;
							needToRollback = true;

							returnCode = DATABASE_EXCEPTION;
						} else {
							System.out.println("GOT IT, entered open_cases record");

							// now insert into offenses
							// close statement to be reused
							insertStmt.close();
							rowsInserted = 0;

							// insert row into OFFENSES
							insertStmt = conn.prepareStatement(
									"INSERT INTO OFFENSES (case_id, sequence_no, offense,	"	+
											"offense_date, disposition_date, disposition_code,		"	+
											"oca_judgment, lesser_offend, last_updated_datetime,	"	+
											"last_updated_by, last_updated_from)					"	+
											"SELECT ?, ?, ?, offense_date, disposition_date,		"	+
											"disposition_code, oca_judgment, lesser_offend,	?, ?, ?	"	+
									"FROM OFFENSES WHERE CASE_ID = ?	");

							insertStmt.setInt(1, tmpCaseId);
							insertStmt.setInt(2, seqNum);
							insertStmt.setString(3, caseOffType);
							insertStmt.setTimestamp(4, Timestamp.valueOf(sdf2.format(dt)));
							insertStmt.setString(5, (String)session.getAttribute("user_name"));
							insertStmt.setString(6, ipAdd);
							insertStmt.setString(7, origCaseId);

							rowsInserted = insertStmt.executeUpdate();

							if (rowsInserted != 1) {
								System.out.println("Insert to table OFFENSES failed");
								// if insert failed - rollback to
								// release any locks
								needToRollback = true;
								returnCode = OPERATION_FAILED;
							} else {
								// insert to OFFENSES successful
								// close statement to be reused
								insertStmt.close();
								rowsInserted = 0;

								insertStmt = conn.prepareStatement(
										"INSERT INTO PARTIES (case_id, sequence_no, party_code, 	"	+
												"other_party_code, party_group, related_party, 				"	+
												"person_no, relation, suppress_notice, 						"	+
												"last_updated_datetime, last_updated_by, 					"	+
												"last_updated_from, date_on, date_off, migration) 			"	+
												"SELECT ?, ?, party_code, other_party_code, party_group, 	"	+
												"related_party, person_no, relation, suppress_notice, 		"	+
												"?, ?, ?, date_on, date_off, migration 						"	+
										"FROM PARTIES WHERE CASE_ID = ? 							");

								insertStmt.setInt(1, tmpCaseId);
								insertStmt.setInt(2, seqNum);
								insertStmt.setTimestamp(3, Timestamp.valueOf(sdf2.format(dt)));
								insertStmt.setString(4, (String)session.getAttribute("user_name"));
								insertStmt.setString(5, ipAdd);
								insertStmt.setString(6, origCaseId);

								rowsInserted = insertStmt.executeUpdate();

								if (rowsInserted == 0) {
									System.out.println("Insert to table PARTIES failed");
									// if insert failed - rollback to
									// release any locks
									needToRollback = true;
									returnCode = OPERATION_FAILED;
								} else {
									System.out.println("Entered duplicate case successfully!");
									System.out.println("Dupped case_id "+origCaseId+" into "+tmpCaseId+"...");

									//Logging new case entered
									String msg = "";
									msg = (String)session.getAttribute("user_name") + ",ENTER-DUPLICATE "+
											"  original case_id "+origCaseId+" - new case_id "+ tmpCaseId +"causeNo="+caseCauseNo+" in court="+court+
											". Cat "+caseCategory + " with type/off "+caseOffType;
									msgLogger log = new msgLogger();
									log.writeCounty(msg, conn, (String)session.getAttribute("user_county"));

									//setting variables for case_view
									caseId = tmpCaseId;
									sequenceNumber = seqNum;
								}
							}
						}
					}
				}
			}
		}

		catch (SQLException e) {
			// close() throw this exception
			System.out.println("Caught SQLException in CaseBean.enterAssociatedCase()!");
			System.out.println(e);
			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;
			needToRollback = true;

			returnCode = DATABASE_EXCEPTION;
		}

		finally {
			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in CaseBean.enterAssociatedCase().");
			try {
				if (rset != null)         // Close ResultSet
					rset.close();
				if (selectStmt != null)   // Close statement
					selectStmt.close();
				if (insertStmt != null)   // Close statement
					insertStmt.close();

				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					conn.close();
				}
				return returnCode;
			}
			catch (SQLException e) {
				// close() throw this exception
				System.out.println("Caught SQLException in CaseBean.enterAssociatedCase()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// select took place
				return returnCode;
			}
		}
	}
	private void selectLowestCourt(PreparedStatement selectStmt, ResultSet rset, Connection conn,
			int courtCount, String fYear, String whereClause) {
		try {
			String court2 = "";
			int court2Count = 0;

			if (court.equals("0229"))
				court2 = "0381";
			else
				court2 = "0229";

			if (selectStmt != null)
				selectStmt.close();
			if (rset != null)
				rset.close();

			//find max(auto_number) for court2
			String sql =
					"SELECT MAX(auto_number)   						" +
							"FROM CASES                						" +
							whereClause + " " +
							"AND COURT = '"+court2+"'";

			System.out.println(sql);
			selectStmt = conn.prepareStatement(sql);
			rset = selectStmt.executeQuery();

			if (rset.next()) {
				// read max auto_number
				court2Count = rset.getInt(1);
				//System.out.println("Retrieved next auto number to be given in CIVIL Starr DC= " + autoNumber);
			}

			System.out.println(court +" is at " + courtCount);
			System.out.println(court2 + " is at " + court2Count);

			int diff = courtCount - court2Count;
			System.out.println("\nThe difference is: "+diff);

			//here is the fun part

			if (courtCount > court2Count) {
				if (Math.abs(diff) > 4) {
					//if court1 is more than 3 cases ahead
					System.out.println(court + " is " + diff + " ahead of " + court2);
					//change courts
					court = court2;
					if(court.equals("0381") && court2Count == 0)
						autoNumber = 1;
					else
						autoNumber = court2Count + 2;
				} else {
					//keep same court
					if(court.equals("0381") && courtCount == 0)
						autoNumber = 1;
					else
						autoNumber = courtCount + 2;
				}
			}

			else if (court2Count > courtCount) {
				if (Math.abs(diff) > 4) {
					//if court2 is more than 3 cases ahead
					System.out.println(court2 + " is " + diff + " ahead of " + court);
					//switch courts
					//instead of court2, stay with court1(court)
					if(court.equals("0381") && courtCount == 0)
						autoNumber = 1;
					else
						autoNumber = courtCount + 2;
				} else {
					//if not so far ahead
					//use court2 info
					court = court2;
					if(court.equals("0381") && court2Count == 0)
						autoNumber = 1;
					else
						autoNumber = court2Count + 2;
				}
			}

			else {
				if (court.equals("0229"))
					autoNumber = 2;
				else
					autoNumber = 1;
			}

			System.out.println("Value of vars at the end of SLC func");
			System.out.println("court = " + court);
			System.out.println("autoNumber = " + autoNumber);
		} //end try

		catch (SQLException e) {
			// close() throw this exception
			System.out.println("Caught SQLException in caseBean.selectLowestCourt!");
			System.out.println(e);
		}
	}

	/**
	 * looks for case_id then calls readCaseData (just because it has been created already) - used for Class Act: Display Case Info button
	 * <p>
	 */
	public int searchCause(String cause) {

		int returnCode = SUCCESS;

		// temp variables to fetch data
		String str = "";
		int num = 0;
		Timestamp ts = null;

		Connection conn = getConnection(jndiResource);

		if (conn == null)
			return NO_DATABASE_CONNECTION; // no connection

		PreparedStatement selectStmt = null;
		ResultSet rset = null;


		try {

			// retrieve case data from CASES table
			selectStmt = conn.prepareStatement(
					"select case_id"
							+ " from cases"
							+ " where cause_no = ?");

			selectStmt.setString(1, cause);

			rset = selectStmt.executeQuery();

			// expect only one row
			if (rset.next()) {
				caseId = rset.getInt(1);
			} else {
				System.out.println("SELECT from table CASES failed");
				returnCode = OPERATION_FAILED;
			}
		}

		catch (SQLException e) {
			System.out.println("Caught SQLException in CaseBean.searchCause()!");
			System.out.println(e);
			e.printStackTrace();
			isDataSubmitted = false;
			returnCode = DATABASE_EXCEPTION;
		}
		finally {
			System.out.println("Statement/ResultSet related cleanup in CaseBean.searchCause().");
			try {
				if (rset != null)         // Close ResultSet
					rset.close();
				if (selectStmt != null)   // Close statement
					selectStmt.close();

				if ((conn != null) && (!conn.isClosed())) {
					conn.commit();

					conn.close();
				}

				return returnCode;
			}
			catch (SQLException e) {
				System.out.println("Caught SQLException in CaseBean.searchCause()!");
				System.out.println(e);
				return returnCode;
			}
		}
	}





	/**
	 * looks for the mailing address of the given person_no
	 * <p>
	 */
	public String getMailingAddress(String person_no) {

		String returnCode = "";

		// temp variables to fetch data
		String str = "";
		String mName = "";
		String address1 = "";
		String address2 = "";
		String city1 = "";
		String state1 = "";
		String zip1 = "";
		int num = 0;


		Connection conn = getConnection(jndiResource);

		if (conn == null)
			return "No database connection for retrival of mailing address"; // no connection

		PreparedStatement selectStmt = null;
		ResultSet rset = null;


		try {

			// retrieve case data from persons table
			selectStmt = conn.prepareStatement(
					"select *"
							+ " from persons"
							+ " where person_no = ?");

			selectStmt.setString(1, person_no);

			rset = selectStmt.executeQuery();

			// expect only one row
			if (rset.next()) {

				if(rset.getString("middle_name")!= null)
					mName = rset.getString("middle_name");
				if(rset.getString("address1")!= null)
					address1 = rset.getString("address1");
				if(rset.getString("address2")!= null)
					address2 = rset.getString("address2");
				if(rset.getString("city1")!= null)
					city1 = rset.getString("city1");
				if(rset.getString("state1")!= null && rset.getString("state1")!= "0")
					state1 = rset.getString("state1");
				if(rset.getString("zip1")!= null)
					zip1 = rset.getString("zip1");
				returnCode = rset.getString("last_name")+","+ rset.getString("first_name")  + " " + mName +"<br/>" +
						address1 + " " + address2 + "<br/>" +
						city1+","+ state1 + " " + zip1;
			} else {
				System.out.println("SELECT from table persons failed");
				returnCode = "Address retrieval failed";
			}
		}

		catch (SQLException e) {
			System.out.println("Caught SQLException in CaseBean.getMailingAddress()!");
			System.out.println(e);
			e.printStackTrace();
			isDataSubmitted = false;
			returnCode = "Address retrieval failed";
		}
		finally {
			System.out.println("Statement/ResultSet related cleanup in CaseBean.getMailingAddress().");
			try {
				if (rset != null)         // Close ResultSet
					rset.close();
				if (selectStmt != null)   // Close statement
					selectStmt.close();

				if ((conn != null) && (!conn.isClosed())) {
					conn.commit();

					conn.close();
				}

				return returnCode;
			}
			catch (SQLException e) {
				System.out.println("Caught SQLException in CaseBean.getMailingAddress()!");
				System.out.println(e);
				return returnCode;
			}
		}
	}


	public int findCauseNumber(String causeNo){


		int returnCode = SUCCESS;
		int i = 0;
		String str = "";
		String[] causeNos = causeNo.split(";");

		if(causeNo != null && !causeNo.equals("") && !causeNo.substring(causeNo.length()-1, causeNo.length()).equals(";") && causeNos.length>1){
			causeNo = causeNos[causeNos.length-1];
		}



		Connection conn = getConnection(jndiResource);

		if (conn == null)
			return this.NO_DATABASE_CONNECTION; // no connection

		PreparedStatement selectStmt = null;
		ResultSet rset = null;


		try {
			selectStmt = conn.prepareStatement("SELECT * FROM cases WHERE cause_no = ?");
			selectStmt.setString(1, causeNo);
			rset = selectStmt.executeQuery();

			while(rset.next()){
				i++;
			}
			if(i == 0){
				System.out.println("No Cause number(s) found for causeNo: " + causeNo);
			}else{
				this.causeNumbersArray = new String[i][4];
				rset.beforeFirst();
				i = 0;
				while(rset.next() && i< this.causeNumbersArray.length){

					//case_id
					str = rset.getString("case_id");
					if(str != null && !str.equals(""))
						this.causeNumbersArray[i][0] = str.trim();
					else
						this.causeNumbersArray[i][0] = "";
					//Case Category
					str = rset.getString("category");
					if(str != null && !str.equals("")){
						if(str.equals("V"))
							this.causeNumbersArray[i][1] = "Civil";
						else
							this.causeNumbersArray[i][1] = "Criminal";
					}
					else this.causeNumbersArray[i][1] = "N/A";

					//Cause Number
					str = rset.getString("cause_no");
					if(str != null && !str.equals(""))
						this.causeNumbersArray[i][2] = str.trim();
					else
						this.causeNumbersArray[i][2] = "";

					//Style
					str = rset.getString("style");
					if(str != null && !str.equals(""))
						this.causeNumbersArray[i][3] = str.trim();
					else
						this.causeNumbersArray[i][3] = "";

					i++;
				}

			}
		}

		catch (Exception e) {
			System.out.println("Caught Exception in CaseBean.findCauseNumber()!");
			System.out.println(e);
			e.printStackTrace();
			isDataSubmitted = false;
			returnCode = this.OPERATION_FAILED;
		}
		finally {
			System.out.println("Statement/ResultSet related cleanup in CaseBean.findCauseNumber().");
			try {
				if (rset != null)         // Close ResultSet
					rset.close();
				if (selectStmt != null)   // Close statement
					selectStmt.close();

				if ((conn != null) && (!conn.isClosed())) {
					conn.commit();

					conn.close();
				}

				return returnCode;
			}
			catch (SQLException e) {
				System.out.println("Caught SQLException in CaseBean.findCauseNumber()!");
				System.out.println(e);
				return returnCode;
			}
		}

	}


	public boolean isCCLCourt(String courtCode,HttpServletRequest request) {

		PreparedStatement selectStmt = null;
		ResultSet rset = null;

		Connection conn = null;

		String feeGroup = "";
		conn = getConnection(jndiResource);
		boolean result = false;


		try {

			selectStmt = conn.prepareStatement("select * from code_table where code_type='court' and code='"+courtCode+"' and reference ='CCL';");


			rset = selectStmt.executeQuery();
			int count = 0;
			while(rset.next())
				count++;


			if(count> 0)
				result =  true;
			else
				result = false;


		}
		catch (SQLException e) {
			System.out.println("Caught SQLException in CaseBean.isCCLCourt()!");
			e.printStackTrace();
			this.exceptionEmail.emailException(request, e);
		}
		catch (Exception e) {
			System.out.println("Caught Exception in CaseBean.isCCLCourt()!");
			e.printStackTrace();
			this.exceptionEmail.emailException(request, e);
		}
		finally {

			try {
				if (rset != null)
					rset.close();
				if (selectStmt != null)
					selectStmt.close();
				if ((conn != null) && !(conn.isClosed()))
					conn.close();

			}
			catch (SQLException e) {
				System.out.println("Caught SQLException while closing statements/resultsets/connections in CaseBean.isCCLCourt()!");
				this.exceptionEmail.emailException(request, e);
			}
			return result;
		}
	}//end function



}//end bean
