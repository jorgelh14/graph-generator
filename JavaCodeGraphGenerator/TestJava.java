/*
 * PaymentBean.java -  Class implementing applying payment
 *
 * Date           Author    Changes
 * ------------------------------------------------------------
 * 2002 Sep 04    Norbert   Initial draft
 * 2006 May 22    Lwood     Voiding escrow payments
 * 2007 Sep 07	  Luz		Installments
 *
 * FUNCTIONS:
 *
 * readOutStdNotInstFeesFines
 * readOutstandingFeesFines
 * readOutstandingNotInstallmentFeesFines
 * readOutstandingInstallmentFeesFines
 * processPayment
 * readPaymentData
 * readAllPayments
 * processEscrow
 * readPaidFeesFines
 * readCurrentEscrow
 * readPayment
 * readPaymentForReceipt
 * processRefund
 * processEscrowRefund
 * processVoid
 * readRefundData
 * readAllRefunds
 * processWaivefee
 * processWaivefeeTest
 * storeInstallment
 * readInstallment
 * readAllInstallments
 * updateInstallment
 * processInstallmentPayment
 * processWaiveInstallment
 * updateInstallmentCharge
 * updateInstallmentMaster
 *
 */


package beans;

import java.sql.*;

import javax.servlet.http.*;

import java.text.*;
import java.util.*;
import java.lang.*;
import java.text.ParseException;

public class TestJava
{
	// error codes
	public final static int OPERATION_FAILED = -1;
	public final static int NO_DATA_FOUND    = -2;

	// Installment flags
	public final static String INSTALLMENTS_NO =  "0";
	public final static String INSTALLMENTS_YES = "1";

	private ExceptionEmailBean exceptionEmail = new ExceptionEmailBean();

	private String party;
	private String partyDesc;
	private String amount;
	private String accountNo;
	private double currentBalance;
	private double [] totalIndFee;
	private double totalBCC;
	private double totalATTY;
	private double totalFINE;
	private double totalOTH;
	private String method;
	private String checkNumber;
	private String paymentDate;
	private String paidBy;
	private String partyRes;
	private String cashieredBy;
	private String partyResNo;
	private String refundDate;
	private String refundBy;
	private String voidBy;
	private String description;
	private String receipt;
	private int caseId;
	private int transactionId;
	private int sequenceNumber;
	private int paymentNumber;
	private int escrowNumber;
	private double paidBalance;
	private String outstandingFeesFinesArray[][];
	private String outstandingInstallmentFeesFinesArray[][];
	private String paidFeesFinesArray[][];
	private String paybreakArray[][];
	private double eachPaidAmount;
	private String paymentsArray[][];
	private String receiptArray[][];
	private String refundsArray[][];
	private String startDate;
	private String frequency;
	private String frequencyDesc;
	private String lastBillingDate;
	private String nextBillingDate;
	private String orgAmount;
	private String ytdAmount;
	private String paymentAmount;
	private int installmentNumber;
	private double chargedOutstandingBalance;
	private String installmentsArray[][];
	private String methodName;
	private String userName;
	private String sqlString;
	private String userPayment;
	private String [][] compliancePaymentDist;

	public TestJava()
	{
		super();

		party = "0";
		partyDesc = "";
		amount = "";
		accountNo = "";
		currentBalance = 0.0;
		totalIndFee = null;
		totalBCC = 0.0;
		totalATTY = 0.0;
		totalFINE = 0.0;
		totalOTH = 0.0;
		paymentDate = "";
		refundDate = "";
		method = "0";
		checkNumber = "";
		paidBy = "";
		partyRes = "";
		cashieredBy = "";
		partyResNo = "";
		refundBy = "";
		voidBy = "";
		description = "";
		receipt = "";
		startDate = "";
		frequency = "0";
		frequencyDesc = "";
		lastBillingDate = "";
		nextBillingDate = "";
		orgAmount = "";
		ytdAmount = "";
		paymentAmount = "";
		methodName = "";
		userName = "";
		sqlString = "";
		userPayment = "";

		caseId = 0;
		transactionId = 0;
		sequenceNumber = 0;
		paymentNumber = 0;
		escrowNumber = 0;
		paidBalance = 0.0;
		outstandingFeesFinesArray = null;
		outstandingInstallmentFeesFinesArray = null;
		paidFeesFinesArray = null;
		paybreakArray = null;
		eachPaidAmount = 0.0;
		paymentsArray = null;
		receiptArray = null;
		refundsArray = null;
		installmentNumber = 0;
		chargedOutstandingBalance = 0.0;
		installmentsArray = null;
		compliancePaymentDist = null;
	}

	public void reset()
	{
		party = "0";
		partyDesc = "";
		amount = "";
		accountNo = "";
		currentBalance = 0.0;
		totalIndFee = null;
		totalBCC = 0.0;
		totalATTY = 0.0;
		totalFINE = 0.0;
		totalOTH = 0.0;
		paymentDate = "";
		paymentNumber = 0;
		escrowNumber = 0;
		refundDate = "";
		method = "0";
		checkNumber = "";
		paidBy = "";
		partyRes = "";
		cashieredBy = "";
		partyResNo = "";
		refundBy = "";
		voidBy = "";
		description = "";
		receipt = "";
		startDate = "";
		frequency = "0";
		frequencyDesc = "";
		lastBillingDate = "";
		nextBillingDate = "";
		orgAmount = "";
		ytdAmount = "";
		paymentAmount = "";
		methodName = "";
		userName = "";
		sqlString = "";
		userPayment = "";

		caseId = 0;
		transactionId = 0;
		sequenceNumber = 0;
		paymentNumber = 0;
		paidBalance = 0.0;
		outstandingFeesFinesArray = null;
		outstandingInstallmentFeesFinesArray = null;
		paidFeesFinesArray = null;
		paybreakArray = null;
		eachPaidAmount = 0.0;
		paymentsArray = null;
		receiptArray = null;
		refundsArray = null;
		installmentNumber = 0;
		chargedOutstandingBalance = 0.0;
		installmentsArray = null;
		compliancePaymentDist = null;

		isDataSubmitted = false;
	}

	public void setParty(String x) { party = x; isDataSubmitted = true; }

	// !!! Why are we not summiting data when we set partyDesc?
	public void setPartyDesc(String x) { partyDesc = x; /* isDataSubmitted = true; */ }

	public void setAmount(String x) { amount = x; isDataSubmitted = true; }
	public void setAccountNo(String x) { accountNo = x; isDataSubmitted = true; }
	public void setMethod(String x) { method = x; isDataSubmitted = true; }
	public void setPaymentDate(String x) { paymentDate = x; isDataSubmitted = true; }
	public void setRefundDate(String x) { refundDate = x; isDataSubmitted = true; }
	public void setCheckNumber(String x) { checkNumber = x; isDataSubmitted = true; }
	public void setDescription(String x) { description = x; isDataSubmitted = true; }
	public void setReceipt(String x) { receipt = x; isDataSubmitted = true; }
	public void setPaidBy(String x) { paidBy = x; isDataSubmitted = true; }
	public void setPartyRes(String x) { partyRes = x; isDataSubmitted = true; }
	public void setCashieredBy(String x) { cashieredBy = x; isDataSubmitted = true; }
	public void setPartyResNo(String x) { partyResNo = x; isDataSubmitted = true; }
	public void setRefundBy(String x) { refundBy = x; isDataSubmitted = true; }
	public void setVoidBy(String x) { voidBy = x; isDataSubmitted = true; }
	public void setCaseId(int x) { caseId = x; }
	public void setTransactionId(int x) { transactionId = x; }
	public void setSequenceNumber(int x) { sequenceNumber = x; }
	public void setPaymentNumber(int x) { paymentNumber = x; }
	public void setEscrowNumber(int x) { escrowNumber = x; }
	public void setStartDate(String x) { startDate = x; isDataSubmitted = true; }
	public void setFrequency(String x) { frequency = x; isDataSubmitted = true; }
	public void setFrequencyDesc(String x) { frequencyDesc = x; isDataSubmitted = true; }
	public void setLastBillingDate(String x) { lastBillingDate = x; isDataSubmitted = true; }
	public void setNextBillingDate(String x) { nextBillingDate = x; isDataSubmitted = true; }
	public void setOrgAmount(String x) { orgAmount = x; isDataSubmitted = true; }
	public void setYtdAmount(String x) { ytdAmount = x; isDataSubmitted = true; }
	public void setPaymentAmount(String x) { paymentAmount = x; isDataSubmitted = true; }
	public void setInstallmentNumber(int x) { installmentNumber = x; }
	public void setUserPayment(String userPayment){this.userPayment= userPayment; }

	public String getParty() { return party; }
	public String getPartyDesc() { return partyDesc; }
	public String getAmount() { return amount; }
	public String getAccountNo() { return accountNo; }
	public double getCurrentBalance() { return currentBalance; }
	public double [] getTotalIndFee() { return totalIndFee; }
	public double getTotalBCC() { return totalBCC; }
	public double getTotalATTY() { return totalATTY; }
	public double getTotalFINE() { return totalFINE; }
	public double getTotalOTH() { return totalOTH; }
	public String getMethod() { return method; }
	public String getPaymentDate() { return paymentDate; }
	public String getRefundDate() { return refundDate; }
	public String getCheckNumber() { return checkNumber; }
	public String getDescription() { return description; }
	public String getReceipt() { return receipt; }
	public double getPaidBalance() { return paidBalance; }
	public String getPaidBy() { return paidBy; }
	public String getPartyRes() { return partyRes; }
	public String getCashieredBy() { return cashieredBy; }
	public String getPartyResNo() { return partyResNo; }
	public String getRefundBy() { return refundBy; }
	public String getVoidBy() { return voidBy; }
	public int getCaseId() { return caseId; }
	public int getTransactionId() { return transactionId; }
	public int getSequenceNumber() { return sequenceNumber; }
	public int getPaymentNumber() { return paymentNumber; }
	public int getEscrowNumber() { return escrowNumber; }
	public String[][] getOutstandingFeesFinesArray() { return outstandingFeesFinesArray; }
	public String[][] getOutstandingInstallmentFeesFinesArray() { return outstandingInstallmentFeesFinesArray; }
	public String[][] getPaidFeesFinesArray() { return paidFeesFinesArray; }
	public String[][] getPaybreakArray() { return paybreakArray; }
	public double getEachPaidAmount() { return eachPaidAmount; }
	public String[][] getPaymentsArray() { return paymentsArray; }
	public String[][] getReceiptArray() { return receiptArray; }
	public String[][] getRefundsArray() { return refundsArray; }
	public String getStartDate() { return startDate; }
	public String getFrequency() { return frequency; }
	public String getFrequencyDesc() { return frequency; }
	public String getLastBillingDate() { return lastBillingDate; }
	public String getNextBillingDate() { return nextBillingDate; }
	public String getOrgAmount() { return orgAmount; }
	public String getYtdAmount() { return ytdAmount; }
	public String getPaymentAmount() { return paymentAmount; }
	public int getInstallmentNumber() { return installmentNumber; }
	public double getChargedOutstandingBalance() { return chargedOutstandingBalance; }
	public String[][] getInstallmentsArray() { return installmentsArray; }
	public String getUserPayment(){return userPayment;}

	/**
	 * @return the compliancePaymentDist
	 */
	public String[][] getCompliancePaymentDist() {
		return compliancePaymentDist;
	}

	/**
	 * @param compliancePaymentDist the compliancePaymentDist to set
	 */
	public void setCompliancePaymentDist(String[][] compliancePaymentDist) {
		this.compliancePaymentDist = compliancePaymentDist;
	}

	/**
	 * reads outstanding not set to installment Fees/Fines
	 * <p>
	 * @param caseID as <code>int</code>
	 * @param sequence number as <code>int</code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="PaymentBean.html#NO_DATA_FOUND">NO_DATA_FOUND</a></code>
	 *   <li><code><a href="PaymentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 * <p>
	 * @throws none
	 * <p>
	 * <i> Comments: Checks for any fees (not in installments) that have pending amounts to be paid.
	 *			Activates "Make Payment" link in payments_history.jsp
	 * </i>
	 */
	public int readOutStdNotInstFeesFines(int csId, int seqNo)
	{
		int returnCode = SUCCESS;
		int i = 0;

		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet rset = null;

		try
		{
			conn = getConnection(jndiResource);
			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection


			SimpleDateFormat sdf = new SimpleDateFormat ("MM/dd/yyyy");
			DecimalFormat df = new DecimalFormat ("#0.00");
			DecimalFormat df1 = new DecimalFormat ("#,##0.00");

			selectStmt = conn.prepareStatement (
					"select FEES_FINES.amount"
							+ ", FEES_FINES.amount-(select sum(amount) from paybreak where fee_fine_no = FEES_FINES.fee_fine_no)"
							+ " from fees_fines"
							+ " where case_id=? and sequence_no=? and fee_type<>'CASHBOND'"
							+ " and (group_record Is Null or group_record=' ')"
							+ " and (installments IS NULL or installments=0)");
			selectStmt.setInt(1,csId);
			selectStmt.setInt(2,seqNo);
			rset = selectStmt.executeQuery();
			while ((rset.next()) && (i < MAX_SIZE))
			{

				// balance
				// if amount-SELECT SUM(amount) FROM PAYBREAK ...
				// is NULL it means there was no payments made
				// towards this fee - use amount
				double amt1 = rset.getDouble(1);

				double amt = rset.getDouble(2);

				if (rset.wasNull())
				{
					amt = amt1;
				}
				// check if balance > 0
				// if not don't include that fee
				if (amt != 0.00)
					i++;
			}
			if (i == 0) {
				System.out.println("SELECT from table FEES_FINES failed or no data found");
				returnCode = NO_DATA_FOUND;
			}
		}
		catch (SQLException e)
		{
			System.out.println("Caught SQLException in PaymentBean.readOutStdNotInstFeesFines()!");
			System.out.println(e);
			e.printStackTrace();
			isDataSubmitted = false;
			returnCode = DATABASE_EXCEPTION;
		}
		finally
		{
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.readOutStdNotInstFeesFines().");
			try
			{
				//  Didn't have rset.close
				if (rset != null) {  // Close rset
					try { rset.close(); } catch (SQLException e) { System.out.println("Caught rset not closed in PaymentBean.readOutStdNotInstFeesFines()!"); }
					rset = null;
				}

				if (selectStmt != null) {   // Close statement
					try { selectStmt.close(); } catch (SQLException e) { System.out.println("Caught selectStmt not closed in PaymentBean.readOutStdNotInstFeesFines()!"); }
					selectStmt = null;
				}

				if ((conn != null) && (!conn.isClosed())) {
					conn.commit();
					try { conn.close(); } catch (SQLException e) { System.out.println("Caught connection not closed in PaymentBean.readOutStdNotInstFeesFines()!"); }
					conn = null;
				}
				return returnCode;
			}
			catch (SQLException e)
			{
				System.out.println("Caught SQLException in PaymentBean.readOutStdNotInstFeesFines()!");
				System.out.println(e);
				return returnCode;
			}
		}
	}

	/**
	 * reads outstanding Fees/Fines to array
	 * <p>
	 * @param caseID as <code>int</code>
	 * @param sequence number as <code>int</code>
	 * @param case category as <code>String</code> (CIVIL or CRIMINAL)
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="PaymentBean.html#NO_DATA_FOUND">NO_DATA_FOUND</a></code>
	 *   <li><code><a href="PaymentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 * <p>
	 * @throws none
	 * <p>
	 * <i> Comments: reads outstanding fees / fines to array
	 *               index 0 - fee_fine_no
	 *               index 1 - fee_type
	 *               index 2 - date_due
	 *               index 3 - balance
	 *               index 4 - fee name (CODE_TABLE.value)
	 * </i>
	 */
	public int readOutstandingFeesFines(int csId, int seqNo, String partyBy, String caseCategory)
	{
		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int i = 0;
		String[][] tmpArray = new String[MAX_SIZE][7];
		double maxPr = 0.0;

		Connection conn = null;

		//System.out.println("Party Responsible is " + partyBy);
		PreparedStatement selectStmt = null;
		ResultSet rset = null;
		try
		{
			conn = getConnection(jndiResource);
			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection

			SimpleDateFormat sdf = new SimpleDateFormat ("MM/dd/yyyy");

			DecimalFormat df = new DecimalFormat ("#0.00");
			DecimalFormat df1 = new DecimalFormat ("#,##0.00");
			DecimalFormat df2 = new DecimalFormat ("00.00");

			selectStmt = conn.prepareStatement (
					"select fee_fine_no, fee_type, date_due, amount"
							+ ", amount-(select sum(amount) from paybreak where fee_fine_no=fees_fines.fee_fine_no)"
							+ ", priority, value, fees_fines.installments, if(fees_fines.fee_group is not null,'Y','') as fee_group "
							+ " from fees_fines, code_table"
							+ " where case_id=? and sequence_no=? and party_responsible=? and code=fee_type"
							+ " and fee_type<>'CASHBOND' and fee_type<>'OPMT' and fee_type<>'ESCROW'"
							+ " and (group_record Is Null or group_record=' ')"
							+ " and (code_type=? or code_type='fee_type_misc' or code_type='fine_type')"
							+ " order by priority");

			selectStmt.setInt(1, csId);
			selectStmt.setInt(2, seqNo);
			selectStmt.setInt(3, Integer.parseInt(partyBy));

			if ("CRIMINAL".equals(caseCategory))
				selectStmt.setString(4, "fee_type_criminal");
			if (!"CRIMINAL".equals(caseCategory))
				selectStmt.setString(4, "fee_type_civil");
			//System.out.println(selectStmt.toString());
			rset = selectStmt.executeQuery();

			while ((rset.next()) && (i < MAX_SIZE))
			{
				// fee_fine_no
				tmpArray[i][0] = String.valueOf(rset.getInt(1));

				// fee_type
				tmpArray[i][1] = rset.getString(2).trim();
				//System.out.println("Each fee is " + tmpArray[i][1]);

				// fee_group
				tmpArray[i][6] = rset.getString("fee_group").trim();

				// date_due
				Timestamp ts = rset.getTimestamp(3);
				if (ts != null)
					tmpArray[i][2] = sdf.format(ts);

				// balance
				// if amount-SELECT SUM(amount) FROM PAYBREAK ...
				// is NULL it means there was no payments made
				// towards this fee - use amount
				double amt1 = rset.getDouble(4);
				double amt = rset.getDouble(5);

				if (rset.wasNull())
				{
					tmpArray[i][3] = df.format(amt1);
					//System.out.println("each fee is " + tmpArray[i][3]);
					currentBalance = currentBalance + amt1;
				}
				else
				{
					tmpArray[i][3] = df.format(amt);
					//System.out.println("each fee is " + tmpArray[i][3]);
					currentBalance = currentBalance + amt;
				}
				try {
					//System.out.println("currentBalance = '"+currentBalance+"'");
					currentBalance = Double.valueOf(df2.format(currentBalance)).doubleValue();
					//System.out.println("currentBalance = '"+currentBalance+"'");
				} catch (Exception e) {
					System.out.println("Caught exception !!!!!");
					System.out.println(e);
				}
				//priority
				int pr = rset.getInt(6);
				//System.out.println("Priority of each fee is "+ pr);
				tmpArray[i][5] =  String.valueOf(pr);
				/*
				 * totalIndFee = new double [(int)maxPr/10];
				 * int j = 0;
				 * int tempPr = 10;
				 * while (tempPr <= maxPr)
				 * {
				 * if (pr == tempPr)
				 * totalIndFee[j] += Double.parseDouble(tmpArray[i][3]);
				 * tempPr += 10;
				 * j++;
				 * }
				 */
				if (pr == 10)
				{
					totalBCC += Double.parseDouble(tmpArray[i][3]);
					//totalBCC = Math.rint(Double.parseDouble(tmpArray[i][3])/totalBCC);
				}
				if (pr == 20)
				{
					totalATTY += Double.parseDouble(tmpArray[i][3]);
				}
				if (pr == 30)
				{
					//System.out.println("Test for fine");
					totalFINE += Double.parseDouble(tmpArray[i][3]);
					//System.out.println("Test for total fine "+ totalFINE);
				}
				if (pr > 30)
				{
					totalOTH += Double.parseDouble(tmpArray[i][3]);
				}

				// fee name
				tmpArray[i][4] = rset.getString(7).trim();

				//referance
				// check if reference != NULL
				// (means this is one of installment fees/fines
				if (rset.getInt(8) != 0)
					tmpArray[i][4] += " (Installment)";

				// check if balance > 0
				// if not don't include that fee
				//System.out.println("The fee type of outstanding fee is " + tmpArray[i][1]);
				//System.out.println("The amount of outstanding fee is " + tmpArray[i][3]);
				if (Double.parseDouble(tmpArray[i][3]) != 0.00)
					i++;
			}
			//currentBalance = Double.valueOf(df1.format(currentBalance)).doubleValue();
			//System.out.println("the outstanding balance is " + currentBalance);
			if (i == 0)
			{
				System.out.println("SELECT from table FEES_FINES failed or no data found");
				returnCode = NO_DATA_FOUND;
			}
			// copy data from temp array
			outstandingFeesFinesArray = new String[i][7];

			for (int j=0; j<i;j++)
				for (int k=0;k<7;k++)
					outstandingFeesFinesArray[j][k] = tmpArray[j][k];
		}
		catch (SQLException e)
		{
			System.out.println("Caught SQLException in PaymentBean.readOutstandingFeesFines()!");
			System.out.println(e);
			e.printStackTrace();
			isDataSubmitted = false;
			needToRollback = true;
			returnCode = DATABASE_EXCEPTION;
		}
		finally
		{
			//allow gc
			tmpArray = null;
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.readOutstandingFeesFines().");
			try
			{
				//Didn't have rset.close
				if (rset != null) {  // Close resultset
					System.out.println("Rset was open");
					try { rset.close(); } catch (SQLException e) { System.out.println("Caught rset not closed in PaymentBean.readOutstandingFeesFines()!"); }
					rset = null;
				}

				if (selectStmt != null) {   // Close statement
					try { selectStmt.close(); } catch (SQLException e) { System.out.println("Caught selectStmt not closed in PaymentBean.readOutstandingFeesFines()!"); }
					selectStmt = null;
				}

				if ((conn != null) && (!conn.isClosed())) {
					conn.commit();
					try { conn.close(); } catch (SQLException e) { System.out.println("Caught connection not closed in PaymentBean.readOutstandingFeesFines()!"); }
					conn = null;
				}
				return returnCode;
			}
			catch (SQLException e)
			{
				System.out.println("Caught SQLException in PaymentBean.readOutstandingFeesFines()!");
				System.out.println(e);
				return returnCode;
			}
		}

		//fee_fine_no, fee_type, amount, balance, fee_description

		//SELECT fee_fine_no, fee_type, amount, amount-(SELECT SUM(amount) FROM PAYBREAK WHERE fee_fine_no=FEES_FINES.fee_fine_no), value
		//FROM FEES_FINES, CODE_TABLE
		//WHERE case_id = 104 AND sequence_no = 1 AND code = fee_type AND fee_type <>  'BCC' AND fee_type <>  'BCC-V' AND fee_type <>  'BCC-F'
		//AND code_type = 'fee_type_criminal'
	}

	/**
	 * reads outstanding not installment Fees/Fines to array
	 * <p>
	 * @param caseID as <code>int</code>
	 * @param sequence number as <code>int</code>
	 * @param case category as <code>String</code> (CIVIL or CRIMINAL)
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="PaymentBean.html#NO_DATA_FOUND">NO_DATA_FOUND</a></code>
	 *   <li><code><a href="PaymentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 * <p>
	 * @throws none
	 * <p>
	 * <i> Comments: reads outstanding fees / fines to array
	 *               index 0 - fee_fine_no
	 *               index 1 - fee_type
	 *               index 2 - date_due
	 *               index 3 - balance
	 *               index 4 - fee name (CODE_TABLE.value)
	 * </i>
	 */
	public int readOutstandingNotInstallmentFeesFines(int csId, int seqNo, String personNo, String caseCategory)
	{
		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int i = 0;
		String[][] tmpArray = new String[MAX_SIZE][7];
		double maxPr = 0.0;
		int partyBy = 0;


		Connection conn = null;
		//System.out.println("Party Responsible is " + partyBy);
		PreparedStatement selectStmt = null;
		ResultSet rset = null;
		try
		{
			conn = getConnection(jndiResource);
			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection

			SimpleDateFormat sdf = new SimpleDateFormat ("MM/dd/yyyy");
			DecimalFormat df = new DecimalFormat ("#0.00");
			DecimalFormat df1 = new DecimalFormat ("#,##0.00");

			selectStmt = conn.prepareStatement (
					"SELECT party_no from parties where person_no=? ");

			selectStmt.setInt(1, Integer.parseInt(personNo));
			rset = selectStmt.executeQuery();
			if (rset.next()){
				partyBy=rset.getInt(1);
				this.partyResNo = "" + rset.getInt(1);
			}
			if (selectStmt != null)
				selectStmt.close();
			if (rset != null)
				rset.close();

			selectStmt = conn.prepareStatement (
					"select fee_fine_no, fee_type, date_due, amount"
							+ ", amount-(select sum(amount) from paybreak where fee_fine_no=fees_fines.fee_fine_no)"
							+ ", priority, value, fees_fines.installments, if(fees_fines.fee_group is not null,'Y','') as fee_group "
							+ " from fees_fines, code_table"
							+ " where case_id=? and sequence_no=? and party_responsible=? and code=fee_type"
							+ " and fee_type<>'CASHBOND' and fee_type<>'OPMT'"
							+ " and (group_record Is Null or group_record=' ')"
							+ " and (installments IS NULL or installments=0)"
							+ " and (code_type=? or code_type='fee_type_misc' or code_type='fine_type')"
							+ " order by priority");

			selectStmt.setInt(1, csId);
			selectStmt.setInt(2, seqNo);
			selectStmt.setInt(3, partyBy);

			if ("CRIMINAL".equals(caseCategory))
				selectStmt.setString(4, "fee_type_criminal");
			if (!"CRIMINAL".equals(caseCategory))
				selectStmt.setString(4, "fee_type_civil");

			rset = selectStmt.executeQuery();

			while ((rset.next()) && (i < MAX_SIZE))
			{
				// fee_fine_no
				tmpArray[i][0] = String.valueOf(rset.getInt(1));

				// fee_type
				tmpArray[i][1] = rset.getString(2).trim();
				//System.out.println("Each fee is " + tmpArray[i][1]);

				// fee_group
				tmpArray[i][6] = rset.getString("fee_group").trim();

				// date_due
				Timestamp ts = rset.getTimestamp(3);
				if (ts != null)
					tmpArray[i][2] = sdf.format(ts);

				// balance
				// if amount-SELECT SUM(amount) FROM PAYBREAK ...
				// is NULL it means there was no payments made
				// towards this fee - use amount
				double amt1 = rset.getDouble(4);
				double amt = rset.getDouble(5);

				if (rset.wasNull())
				{
					tmpArray[i][3] = df.format(amt1);
					//System.out.println("each fee is " + tmpArray[i][3]);
					currentBalance = currentBalance + amt1;
				}
				else
				{
					tmpArray[i][3] = df.format(amt);
					//System.out.println("each fee is " + tmpArray[i][3]);
					currentBalance = currentBalance + amt;
				}

				//priority
				int pr = rset.getInt(6);
				//System.out.println("Priority of each fee is "+ pr);
				tmpArray[i][5] =  String.valueOf(pr);
				/*
				 * totalIndFee = new double [(int)maxPr/10];
				 * int j = 0;
				 * int tempPr = 10;
				 * while (tempPr <= maxPr)
				 * {
				 * if (pr == tempPr)
				 * totalIndFee[j] += Double.parseDouble(tmpArray[i][3]);
				 * tempPr += 10;
				 * j++;
				 * }
				 */
				if (pr == 10)
				{
					totalBCC += Double.parseDouble(tmpArray[i][3]);
					//totalBCC = Math.rint(Double.parseDouble(tmpArray[i][3])/totalBCC);
				}
				if (pr == 20)
				{
					totalATTY += Double.parseDouble(tmpArray[i][3]);
				}
				if (pr == 30)
				{
					//System.out.println("Test for fine");
					totalFINE += Double.parseDouble(tmpArray[i][3]);
					//System.out.println("Test for total fine "+ totalFINE);
				}
				if (pr > 30)
				{
					totalOTH += Double.parseDouble(tmpArray[i][3]);
				}

				// fee name
				tmpArray[i][4] = rset.getString(7).trim();

				//referance
				// check if reference != NULL
				// (means this is one of installment fees/fines
				if (rset.getInt(8) != 0)
					tmpArray[i][4] += " (Installment)";

				// check if balance > 0
				// if not don't include that fee
				//System.out.println("The fee type of outstanding fee is " + tmpArray[i][1]);
				//System.out.println("The amount of outstanding fee is " + tmpArray[i][3]);
				if (Double.parseDouble(tmpArray[i][3]) != 0.00)
					i++;
			}
			//System.out.println("the outstanding balance is " + currentBalance);
			if (i == 0)
			{
				System.out.println("SELECT from table FEES_FINES failed or no data found");
				returnCode = NO_DATA_FOUND;
			}
			// copy data from temp array
			outstandingFeesFinesArray = new String[i][7];
			for (int j=0; j<i;j++)
				for (int k=0;k<7;k++)
					outstandingFeesFinesArray[j][k] = tmpArray[j][k];
		}
		catch (SQLException e)
		{
			System.out.println("Caught SQLException in PaymentBean.readOutstandingNotInstallmentFeesFines()!");
			System.out.println(e);
			e.printStackTrace();
			isDataSubmitted = false;
			needToRollback = true;
			returnCode = DATABASE_EXCEPTION;
		}
		finally
		{
			tmpArray = null;
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.readOutstandingNotInstallmentFeesFines().");
			try
			{
				// Did not have rset.close
				if (rset != null) {  // Close rset
					System.out.println("Rset was open");
					try { rset.close(); } catch (SQLException e) { System.out.println("Caught rset not closed in PaymentBean.readOutstandingNotInstallmentFeesFines()!"); }
					rset = null;
				}
				if (selectStmt != null) {   // Close statement
					try { selectStmt.close(); } catch (SQLException e) { System.out.println("Caught selectStmt not closed in PaymentBean.readOutstandingNotInstallmentFeesFines()!"); }
					selectStmt = null;
				}

				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					try { conn.close(); } catch (SQLException e) { System.out.println("Caught connection not closed in PaymentBean.readOutstandingNotInstallmentFeesFines()!"); }
					conn = null;
				}
				return returnCode;
			}
			catch (SQLException e)
			{
				System.out.println("Caught SQLException in PaymentBean.readOutstandingNotInstallmentFeesFines()!");
				System.out.println(e);
				return returnCode;
			}
		}

		//fee_fine_no, fee_type, amount, balance, fee_description

		//SELECT fee_fine_no, fee_type, amount, amount-(SELECT SUM(amount) FROM PAYBREAK WHERE fee_fine_no=FEES_FINES.fee_fine_no), value
		//FROM FEES_FINES, CODE_TABLE
		//WHERE case_id = 104 AND sequence_no = 1 AND code = fee_type AND fee_type <>  'BCC' AND fee_type <>  'BCC-V' AND fee_type <>  'BCC-F'
		//AND code_type = 'fee_type_criminal'
	}

	/**
	 * reads outstanding installment Fees/Fines to array
	 * <p>
	 * @param caseID as <code>int</code>
	 * @param sequence number as <code>int</code>
	 * @param case category as <code>String</code> (CIVIL or CRIMINAL)
	 * @param installmentNo as <code>int</code>
	 * @param payAmount as <code>String</code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="PaymentBean.html#NO_DATA_FOUND">NO_DATA_FOUND</a></code>
	 *   <li><code><a href="PaymentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 * <p>
	 * @throws none
	 * <p>
	 * <i> Comments: reads outstanding fees / fines to array
	 *               index 0 - fee_fine_no
	 *               index 1 - fee_type
	 *               index 2 - date_due
	 *               index 3 - balance
	 *               index 4 - fee name (CODE_TABLE.value)
	 * </i>
	 */
	public int readOutstandingInstallmentFeesFines(int csId, int seqNo, String partyBy, String caseCategory, int installNo)
	{
		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int i = 0;
		String[][] tmpArray = new String[MAX_SIZE][7];
		double maxPr = 0.0;

		Connection conn = null;

		//System.out.println("Party Responsible is " + partyBy);
		PreparedStatement selectStmt = null;
		ResultSet rset = null;
		try
		{
			conn = getConnection(jndiResource);
			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection


			SimpleDateFormat sdf = new SimpleDateFormat ("MM/dd/yyyy");

			DecimalFormat df = new DecimalFormat ("#0.00");
			DecimalFormat df1 = new DecimalFormat ("#,##0.00");

			selectStmt = conn.prepareStatement (
					"select fee_fine_no, fee_type, date_due, amount"
							+ ", amount-(select sum(amount) from paybreak where fee_fine_no=fees_fines.fee_fine_no)"
							+ ", priority, value, fees_fines.reference, if(fees_fines.fee_group is not null,'Y','') as fee_group "
							+ " from fees_fines, code_table"
							+ " where case_id=? and sequence_no=? and party_responsible=?"
							+ " and code=fee_type and fee_type<>'CASHBOND'"
							+ " and (group_record Is Null or not group_record='1')"
							+ " and installments=?"
							+ " and (code_type=? or code_type='fee_type_misc' or code_type='fine_type')"
							+ " order by priority");

			selectStmt.setInt(1,csId);
			selectStmt.setInt(2,seqNo);
			selectStmt.setInt(3,Integer.parseInt(partyBy));
			selectStmt.setInt(4,installNo);

			if ("CRIMINAL".equals(caseCategory))
				selectStmt.setString(5,"fee_type_criminal");
			if (!"CRIMINAL".equals(caseCategory))
				selectStmt.setString(5,"fee_type_civil");
			rset = selectStmt.executeQuery();
			while ((rset.next()) && (i < MAX_SIZE))
			{
				// fee_fine_no
				tmpArray[i][0] = String.valueOf(rset.getInt(1));

				// fee_type
				tmpArray[i][1] = rset.getString(2).trim();
				//System.out.println("Each fee is " + tmpArray[i][1]);

				// fee_group
				tmpArray[i][6] = rset.getString("fee_group").trim();

				// date_due
				Timestamp ts = rset.getTimestamp(3);
				if (ts != null)
					tmpArray[i][2] = sdf.format(ts);

				// balance
				// if amount-SELECT SUM(amount) FROM PAYBREAK ...
				// is NULL it means there was no payments made
				// towards this fee - use amount
				double amt1 = rset.getDouble(4);
				double amt = rset.getDouble(5);

				if (rset.wasNull())
				{
					tmpArray[i][3] = df.format(amt1);
					//System.out.println("each fee is " + tmpArray[i][3]);
					currentBalance = currentBalance + amt1;
				}
				else
				{
					tmpArray[i][3] = df.format(amt);
					//System.out.println("each fee is " + tmpArray[i][3]);
					currentBalance = currentBalance + amt;
				}
				//priority
				int pr = rset.getInt(6);
				//System.out.println("Priority of each fee is "+ pr);
				tmpArray[i][5] =  String.valueOf(pr);
				/*
				 * totalIndFee = new double [(int)maxPr/10];
				 * int j = 0;
				 * int tempPr = 10;
				 * while (tempPr <= maxPr)
				 * {
				 * if (pr == tempPr)
				 * totalIndFee[j] += Double.parseDouble(tmpArray[i][3]);
				 * tempPr += 10;
				 * j++;
				 * }
				 */
				if (pr == 10)
				{
					totalBCC += Double.parseDouble(tmpArray[i][3]);
					//totalBCC = Math.rint(Double.parseDouble(tmpArray[i][3])/totalBCC);
				}
				if (pr == 20)
				{
					totalATTY += Double.parseDouble(tmpArray[i][3]);
				}
				if (pr == 30)
				{
					//System.out.println("Test for fine");
					totalFINE += Double.parseDouble(tmpArray[i][3]);
					//System.out.println("Test for total fine "+ totalFINE);
				}
				if (pr > 30)
				{
					totalOTH += Double.parseDouble(tmpArray[i][3]);
				}

				// fee name
				tmpArray[i][4] = rset.getString(7).trim();

				//referance
				// check if reference != NULL
				// (means this is one of installment fees/fines
				if (rset.getInt(8) != 0)
					tmpArray[i][4] += " (Installment)";

				// check if balance > 0
				// if not don't include that fee
				//System.out.println("The fee type of outstanding fee is " + tmpArray[i][1]);
				//System.out.println("The amount of outstanding fee is " + tmpArray[i][3]);
				if (Double.parseDouble(tmpArray[i][3]) != 0.00)
					i++;
			}
			//System.out.println("the outstanding balance is " + currentBalance);
			if (i == 0)
			{
				System.out.println("SELECT from table FEES_FINES failed or no data found");
				returnCode = NO_DATA_FOUND;
			}
			// copy data from temp array
			outstandingFeesFinesArray = new String[i][7];

			for (int j=0; j<i;j++)
				for (int k=0;k<7;k++)
					outstandingFeesFinesArray[j][k] = tmpArray[j][k];

		}
		catch (SQLException e)
		{
			System.out.println("Caught SQLException in PaymentBean.readOutstandingInstallmentFeesFines()!");
			System.out.println(e);
			e.printStackTrace();
			isDataSubmitted = false;
			needToRollback = true;
			returnCode = DATABASE_EXCEPTION;
		}
		finally
		{
			tmpArray = null;
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.readOutstandingInstallmentFeesFines().");
			try
			{
				//Did not have rset.close
				if (rset != null) {  // Close recordSet
					try { rset.close(); } catch (SQLException e) { System.out.println("Caught rset not closed in PaymentBean.readOutstandingInstallmentFeesFines()!"); }
					rset = null;
				}
				if (selectStmt != null) {   // Close statement
					try { selectStmt.close(); } catch (SQLException e) { System.out.println("Caught selectStmt not closed in PaymentBean.readOutstandingInstallmentFeesFines()!"); }
					selectStmt = null;
				}

				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					try { conn.close(); } catch (SQLException e) { System.out.println("Caught connection not closed in PaymentBean.readOutstandingInstallmentFeesFines()!"); }
					conn = null;
				}
				return returnCode;
			}
			catch (SQLException e)
			{
				// close() throw this exception
				System.out.println("Caught SQLException in PaymentBean.readOutstandingInstallmentFeesFines()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// inserts took place
				return returnCode;
			}
		}
	}


	/**
	 * processes and stores payment information in the database
	 * <p>
	 * @param HTTP request as <code>HttpServletRequest</code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="PaymentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: data is stored in the database </i>
	 */

	public int processPayment(String partyBy, String caseCategory, String escrowAccount, HttpServletRequest request)
	{
		methodName = "processPayment(String partyBy, String caseCategory, String escrowAccount, HttpServletRequest request)";
		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int rowsInserted = 0;
		HttpSession session = null;
		Map paramMap = null;
		Iterator iter = null;
		Map.Entry me = null;
		String feeGroup = "";
		String feeType = "";
		String str = "";
		String category = "";
		String waiveAccNo = "";
		String oPmtAccNo = "";
		String escrowAccNo = "";
		int feeFineNumber = 0;
		double paidValue = 0.0;
		rollbackDone rbd = null;

		//System.out.println("PaymentBean.processPayment('"+partyBy+"','"+caseCategory+"','"+escrowAccount+"',request)");
		if (!"CRIMINAL".equals(caseCategory))
			category = "V";
		if ("CRIMINAL".equals(caseCategory))
			category = "C";

		Connection conn = null;
		PreparedStatement insertStmt = null;
		PreparedStatement selectStmt = null;
		ResultSet rset = null;
		String ipAdd = request.getRemoteAddr();
		String unParsedPaymentDate = "";

		try
		{
			conn = getConnection(jndiResource);
			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection

			if (request.getParameter("paymentDate") != null)
				unParsedPaymentDate = request.getParameter("paymentDate");



			session = request.getSession(false);
			caseId = Integer.parseInt((String)session.getAttribute("case_id"));
			sequenceNumber = Integer.parseInt((String)session.getAttribute("seq_no"));
			userName = (String)session.getAttribute("user_name");

			double totalAppliedPayments = 0.0;
			// calculate desired amounts to be applied to fees
			//System.out.println("Calculating amount applied to fees - caseId:" + caseId);
			paramMap = request.getParameterMap();
			iter = (paramMap.entrySet()).iterator();
			while (iter.hasNext())
			{
				me = (Map.Entry)iter.next();
				String strParam = (String)me.getKey();
				String strValue[] = (String[])me.getValue();
				if (("fee".equals(strParam.substring(0,3))) && (!"".equals(strValue[0])))
				{
					paidValue = Double.parseDouble(strValue[0]);
					paidValue = Math.floor(paidValue * 100.0 + 0.5) / 100.0;
					//System.out.println("Amount applied fee:'"+strParam+"' paid:'"+strValue[0]+"' (rounded:"+paidValue+")");
					totalAppliedPayments = totalAppliedPayments + paidValue;
				}
			}
			totalAppliedPayments = Math.floor(totalAppliedPayments * 100.0 + 0.5) / 100.0;

			java.util.Date dt = new java.util.Date();
			DecimalFormat df1 = new DecimalFormat("#,##0.00");
			SimpleDateFormat sdf1 = new SimpleDateFormat ("MM/dd/yyyy");
			SimpleDateFormat sdf2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

			java.util.Date payDate = new java.util.Date();
			payDate = sdf1.parse(unParsedPaymentDate);
			unParsedPaymentDate = sdf2.format(payDate);

			//currentBalance = Double.valueOf(df1.format(currentBalance)).doubleValue();
			//System.out.println("Outstanding balance " + currentBalance);
			//System.out.println("Amount applied to fees " + totalAppliedPayments);
			//System.out.println("Amount paid " + amount);
			//System.out.println("Paid By " + paidBy + " Or " + partyBy);
			if (((String)session.getAttribute("user_clerk")).equals("COL") ){
				if (selectStmt != null)
					selectStmt.close();
				if (rset != null)
					rset.close();
				selectStmt = conn.prepareStatement(
						" SELECT max(allocid) FROM PAYMENTS ");
				rset = selectStmt.executeQuery();

				if (rset.next())
					transactionId = rset.getInt(1);
				transactionId+=1;
				System.out.println("this is the transactionId"+transactionId);
			}
			if (Double.parseDouble(amount) > totalAppliedPayments)
			{

				if(escrowAccount.equals("ES"))
				{
					selectStmt = conn.prepareStatement(
							"select fee_fine_no, account_no"
									+ " from fees_fines"
									+ " where case_id=? and sequence_no=?"
									+ " and party_responsible=? and fee_type='ESCROW'");
					selectStmt.setInt(1, caseId);
					selectStmt.setInt(2, sequenceNumber);
					selectStmt.setInt(3, Integer.parseInt(partyBy));
					rset = selectStmt.executeQuery();
					if (rset.next())
					{
						// Escrow is already entered for this party
						System.out.println("Escrow fee is already entered.  Do not need to insert.");
						feeFineNumber = rset.getInt(1);
						escrowAccNo = rset.getString(2).trim();
						//System.out.println("The fee_fine_no is= " + feeFineNumber);
					}
					else
					{
						//System.out.println("Needs to enter escrow fee in the FEES_FINES. " );
						// close select to reuse stmt
						if (selectStmt != null){
							selectStmt.close();
							selectStmt = null;
						}
						// get the account number from the FEE_SCHEDULE
						selectStmt = conn.prepareStatement(
								"select account_no"
										+ " from fee_schedule"
										+ " where fee_group Is Null and case_category='" + category + "'"
										+ " and fee_type='ESCROW'");
						rset = selectStmt.executeQuery();
						if (!(rset.next()))
						{
							// problem with retrieving account_no
							System.out.println("Failed to read account_no");
							needToRollback = true;
							returnCode = OPERATION_FAILED;
						}
						else
						{
							escrowAccNo = rset.getString(1).trim();
							//System.out.println("The escrow account_no is= " + escrowAccNo);
							rowsInserted = 0;
							sqlString = "insert into fees_fines"
									+ " (case_id, sequence_no, fee_group, fee_type, account_no"
									+ ", party_responsible, amount, description, date_ordered"
									+ ", date_due, last_updated_by, last_updated_datetime, last_updated_from)"
									+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
							insertStmt = conn.prepareStatement(sqlString);

							insertStmt.setInt(1, caseId);
							insertStmt.setInt(2, sequenceNumber);
							insertStmt.setString(3, " ");
							insertStmt.setString(4, "ESCROW");
							insertStmt.setString(5, escrowAccNo);
							insertStmt.setInt(6, Integer.parseInt(partyBy));
							insertStmt.setDouble(7, 0.0);
							insertStmt.setString(8, "Escrow on the fees/fines");
							insertStmt.setTimestamp(9, Timestamp.valueOf(sdf2.format(sdf1.parse(paymentDate))));
							insertStmt.setTimestamp(10, Timestamp.valueOf(sdf2.format(sdf1.parse(paymentDate))));
							insertStmt.setString(11, (String)session.getAttribute("user_name"));
							insertStmt.setTimestamp(12, Timestamp.valueOf(sdf2.format(dt)));
							insertStmt.setString(13, ipAdd);

							rowsInserted = insertStmt.executeUpdate();

							if (rowsInserted != 1)
							{
								System.out.println("Insert into table FEES_FINES failed");
								// if insert failed - rollback to
								// release any locks
								needToRollback = true;
								rbd = new rollbackDone(userName, methodName, sqlString);
								returnCode = OPERATION_FAILED;
							}
							else
							{
								// insert to FEES_FINES successful
								//System.out.println("Insert to FEES_FINES successful");

								// close statement to be reused
								if (selectStmt != null){
									selectStmt.close();
									selectStmt = null;
								}
								// retrieve fee_fine_no for newly inserted fee/fine
								selectStmt = conn.prepareStatement(
										"select max(fee_fine_no)"
												+ " from fees_fines"
												+ " where case_id=? and sequence_no=?"
												+ " and last_updated_by=?");
								selectStmt.setInt(1, caseId);
								selectStmt.setInt(2, sequenceNumber);
								selectStmt.setString(3, (String)session.getAttribute("user_name"));
								rset = selectStmt.executeQuery();
								if (rset.next())
								{
									feeFineNumber = rset.getInt(1);
									//System.out.println("The fee_fine_no is= " + feeFineNumber);
								}
								else
								{
									// problem with retrieving fee_fine_no
									System.out.println("Failed to retrieve fee_fine_no");
									needToRollback = true;
									returnCode = OPERATION_FAILED;
								}
							}
						}
					}
				}
				else
				{
					selectStmt = conn.prepareStatement(
							"select fee_fine_no, account_no"
									+ " from fees_fines"
									+ " where case_id=? and sequence_no=?"
									+ " and fee_type='OPMT'");
					selectStmt.setInt(1, caseId);
					selectStmt.setInt(2, sequenceNumber);
					rset = selectStmt.executeQuery();
					if (rset.next())
					{
						// Over payment is already entered for this case
						//System.out.println("Over Payment fee is already entered.  Do not need to insert.");
						feeFineNumber = rset.getInt(1);
						oPmtAccNo = rset.getString(2).trim();
						//System.out.println("The fee_fine_no is= " + feeFineNumber);
					}
					else
					{
						//System.out.println("Needs to enter Over Payment fee in the FEES_FINES. " );

						if (selectStmt != null) {
							selectStmt.close();
							selectStmt = null;
						}

						// get the account number from the FEE_SCHEDULE
						selectStmt = conn.prepareStatement(
								"select account_no"
										+ " from fee_schedule"
										+ " where fee_group Is Null and case_category='" + category + "'"
										+ " and fee_type='OPMT'");
						rset = selectStmt.executeQuery();
						if (!(rset.next()))
						{
							// problem with retrieving account_no
							System.out.println("Failed to read account_no");
							needToRollback = true;
							returnCode = OPERATION_FAILED;
						}
						else
						{
							oPmtAccNo = rset.getString(1).trim();
							//System.out.println("The over payment account_no is= " + oPmtAccNo);
							rowsInserted = 0;
							sqlString = "insert into fees_fines"
									+ " (case_id, sequence_no, fee_group, fee_type, account_no, party_responsible"
									+ ", amount, description, date_ordered, date_due, last_updated_by, last_updated_datetime, last_updated_from)"
									+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
							insertStmt = conn.prepareStatement(sqlString);
							insertStmt.setInt(1, caseId);
							insertStmt.setInt(2, sequenceNumber);
							insertStmt.setString(3, " ");
							insertStmt.setString(4, "OPMT");
							insertStmt.setString(5, oPmtAccNo);
							insertStmt.setInt(6, Integer.parseInt(partyBy));
							insertStmt.setDouble(7, 0.0);
							insertStmt.setString(8, "Over Payment on the fees/fines");
							insertStmt.setTimestamp(9, Timestamp.valueOf(sdf2.format(sdf1.parse(paymentDate))));
							insertStmt.setTimestamp(10, Timestamp.valueOf(sdf2.format(sdf1.parse(paymentDate))));
							insertStmt.setString(11, (String)session.getAttribute("user_name"));
							insertStmt.setTimestamp(12, Timestamp.valueOf(sdf2.format(dt)));
							insertStmt.setString(13, ipAdd);

							rowsInserted = insertStmt.executeUpdate();
							if (rowsInserted != 1)  {
								System.out.println("Insert into table FEES_FINES failed");
								// if insert failed - rollback to
								// release any locks
								needToRollback = true;
								returnCode = OPERATION_FAILED;
								rbd = new rollbackDone(userName, methodName, sqlString);
							}else{
								// insert to FEES_FINES successful
								//System.out.println("Insert to FEES_FINES successful");
								if (selectStmt != null){
									selectStmt.close();
									selectStmt = null;
								}

								// retrieve fee_fine_no for newly inserted fee/fine
								selectStmt = conn.prepareStatement(
										"select max(fee_fine_no)"
												+ " from fees_fines"
												+ " where case_id=? and sequence_no = ?"
												+ " and last_updated_by = ?");
								selectStmt.setInt(1,caseId);
								selectStmt.setInt(2,sequenceNumber);
								selectStmt.setString(3,(String)session.getAttribute("user_name"));
								rset = selectStmt.executeQuery();
								if (rset.next())
								{
									feeFineNumber = rset.getInt(1);
									//System.out.println("The fee_fine_no is= " + feeFineNumber);
								}
								else
								{
									// problem with retrieving fee_fine_no
									//System.out.println("Failed to retrieve fee_fine_no");
									needToRollback = true;
									returnCode = OPERATION_FAILED;
								}
							}
						}
					}
				}
			}
			// if no problems reported (needToRollback == false)
			// continue with inserting to PAYMENTS
			if (needToRollback == false)
			{
				if (insertStmt != null){
					insertStmt.close();
					insertStmt = null;
				}
				rowsInserted = 0;
				// insert row in PAYMENTS
				if (((String)session.getAttribute("user_clerk")).equals("COL") ){
					sqlString =  "insert into payments"
							+ " (case_id, sequence_no, amount, payment_date, method, paid_by"
							+ ", description, receipt, check_no, allocid, last_updated_by, last_updated_datetime, last_updated_from)"
							+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				} else {
					sqlString =  "insert into payments"
							+ " (case_id, sequence_no, amount, payment_date, method, paid_by"
							+ ", description, receipt, check_no, last_updated_by, last_updated_datetime, last_updated_from)"
							+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				}
				insertStmt = conn.prepareStatement(sqlString);
				insertStmt.setInt(1, caseId);
				insertStmt.setInt(2, sequenceNumber);
				insertStmt.setDouble(3, Double.parseDouble(amount));
				insertStmt.setTimestamp(4, Timestamp.valueOf(sdf2.format(sdf1.parse(paymentDate))));
				insertStmt.setString(5, method);
				insertStmt.setString(6, paidBy);
				insertStmt.setString(7, description);
				insertStmt.setString(8, receipt);
				insertStmt.setString(9, checkNumber);
				if (((String)session.getAttribute("user_clerk")).equals("COL") ){
					insertStmt.setInt(10, transactionId);
					if(request.getParameter("received_by") != null && !(request.getParameter("received_by").toString().equals("")))
						insertStmt.setString(11, request.getParameter("received_by").toString());
					else
						insertStmt.setString(11, (String)session.getAttribute("user_name"));
					insertStmt.setTimestamp(12, Timestamp.valueOf(sdf2.format(dt)));
					insertStmt.setString(13, ipAdd);

				} else {
					insertStmt.setString(10, (String)session.getAttribute("user_name"));
					insertStmt.setTimestamp(11, Timestamp.valueOf(sdf2.format(dt)));
					insertStmt.setString(12, ipAdd);
				}
				rowsInserted = insertStmt.executeUpdate();
				if (rowsInserted != 1)
				{
					System.out.println("Insert into table PAYMENTS failed");
					// if insert failed - rollback to
					// release any locks
					needToRollback = true;
					rbd = new rollbackDone(userName, methodName, sqlString);
					returnCode = OPERATION_FAILED;
				}
				else
				{
					// insert to PAYMENTS successful

					// retrieve payment_no for newly inserted payment
					selectStmt = conn.prepareStatement(
							"select max(payment_no)"
									+ " from payments"
									+ " where case_id=? and sequence_no=?"
									+ " and last_updated_by=?");
					selectStmt.setInt(1,caseId);
					selectStmt.setInt(2,sequenceNumber);
					if(request.getParameter("received_by") != null && !(request.getParameter("received_by").toString().equals("")))
						selectStmt.setString(3, request.getParameter("received_by").toString());
					else
						selectStmt.setString(3, (String)session.getAttribute("user_name"));
					rset = selectStmt.executeQuery();
					if (rset.next())
					{
						paymentNumber = rset.getInt(1);
						//System.out.println("Retrieved payment_no = " + paymentNumber);

						paramMap = request.getParameterMap();
						iter = (paramMap.entrySet()).iterator();

						if (insertStmt !=  null){
							insertStmt.close();
							insertStmt = null;
						}
						if (selectStmt != null){
							selectStmt.close();
							selectStmt = null;
						}
						sqlString = "insert into paybreak"
								+ " (fee_fine_no, payment_no, amount"
								+ ", account_no, last_updated_by, last_updated_datetime, last_updated_from)"
								+ " values (?, ?, ?, ?, ?, ?, ?)";
						insertStmt = conn.prepareStatement(sqlString);
						//System.out.println("Test after the insert statement in PAYBREAK");
						while (iter.hasNext())
						{
							me = (Map.Entry)iter.next();
							String param = (String)me.getKey();
							String value[] = (String[])me.getValue();
							rowsInserted = 0;

							//System.out.println("String parameter '" + param + "' = '" + value[0] + "'");
							//System.out.println("Test after the while");
							// if request parameter  has feeXXX format (XXX is fee_fine_no)
							// and value for this parameter != ""

							if ( ("fee".equals(param.substring(0,3))) && (!"".equals(value[0])) )
							{
								String feeNo = param.substring(3);
								//System.out.println("fee number is "+ feeNo);

								// close ResultSet and statement to be reused
								//System.out.println("Test!" + caseCategory);

								// get the account number from the FEE_SCHEDULE for the fee_type
								/*
								 * selectStmt = conn.prepareStatement(
								 * "SELECT FEES_FINES.account_no                " +
								 * "FROM FEE_SCHEDULE, FEES_FINES    " +
								 * "WHERE FEES_FINES.fee_type = FEE_SCHEDULE.fee_type    " +
								 * "AND FEES_FINES.account_no = FEE_SCHEDULE.account_no  " +
								 * "AND FEES_FINES.fee_fine_no = " + feeNo + " " +
								 * "AND FEE_SCHEDULE.case_category = "+"'" + category + "' " );
								 */
								selectStmt = conn.prepareStatement(
										"select account_no"
												+ " from fees_fines"
												+ " where fee_fine_no=" + feeNo);
								rset = selectStmt.executeQuery();
								if (!(rset.next()))
								{
									// problem with retrieving account_no
									System.out.println("Failed to read account_no");
									needToRollback = true;
									returnCode = OPERATION_FAILED;
								}
								else
								{
									str = rset.getString(1);
									if (str != null)
										accountNo = str.trim();
								}
								//System.out.println("The account_no is= " + accountNo);
								insertStmt.setInt(1, Integer.parseInt(feeNo));
								insertStmt.setInt(2, paymentNumber);
								insertStmt.setDouble(3, Double.parseDouble(value[0]));
								insertStmt.setString(4, accountNo);
								if(request.getParameter("received_by") != null && !(request.getParameter("received_by").toString().equals("")))
									insertStmt.setString(5, request.getParameter("received_by").toString());
								else
									insertStmt.setString(5, (String)session.getAttribute("user_name"));
								insertStmt.setTimestamp(6, Timestamp.valueOf(sdf2.format(dt)));
								insertStmt.setString(7, ipAdd);

								rowsInserted = insertStmt.executeUpdate();
								if (rowsInserted != 1)
								{
									System.out.println("Insert into table PAYBREAK failed");
									returnCode = OPERATION_FAILED;
									break; // leave while loop if we have err'd
								}
							}
						}
					}
					else
					{
						System.out.println("Failed to retrieve payment_no");
						needToRollback = true;
						returnCode = OPERATION_FAILED;
					}
				}
			}
			if (Double.parseDouble(amount) > totalAppliedPayments)
			{
				if (needToRollback == false)
				{
					if (insertStmt != null){
						insertStmt.close();
						insertStmt = null;
					}

					rowsInserted = 0;
					if(escrowAccount.equals("ES"))
					{
						sqlString = "insert into paybreak"
								+ " (fee_fine_no, payment_no, amount"
								+ ", account_no, last_updated_by, last_updated_datetime, last_updated_from)"
								+ " values (?, ?, ?, ?, ?, ?, ?)";
						insertStmt = conn.prepareStatement(sqlString);
						insertStmt.setInt(1, feeFineNumber);
						insertStmt.setInt(2, paymentNumber);
						insertStmt.setDouble(3, Double.parseDouble(amount)-totalAppliedPayments);
						insertStmt.setString(4, escrowAccNo);
						if(request.getParameter("received_by") != null && !(request.getParameter("received_by").toString().equals("")))
							insertStmt.setString(5, request.getParameter("received_by").toString());
						else
							insertStmt.setString(5, (String)session.getAttribute("user_name"));
						insertStmt.setTimestamp(6, Timestamp.valueOf(sdf2.format(dt)));
						insertStmt.setString(7, ipAdd);

						rowsInserted = insertStmt.executeUpdate();
						if (rowsInserted != 1)
						{
							System.out.println("Insert into table PAYBREAK failed");
							returnCode = OPERATION_FAILED;
							needToRollback = true;
							rbd = new rollbackDone(userName, methodName, sqlString);
						}
						else
						{
							//System.out.println("Insert to PAYBREAK successful");
						}
						// if no problems reported (needToRollback == false)
						// continue with inserting to ESCROW
						if (needToRollback == false)
						{
							//System.out.println("Attempting to add escrow for over payment");
							// close statement to be reused
							if (insertStmt != null) {
								insertStmt.close();
								insertStmt =null;
							}
							rowsInserted = 0;
							// insert row in ESCROW
							sqlString = "INSERT INTO ESCROW (   " +
									"case_id,                  " +
									"sequence_no,              " +
									"party_no,                 " +
									"fee_fine_no,              " +
									"paid_by,                  " +
									"payment_no, " +
									"escrow_amount,            " +
									"current_balance,          " +
									"date_of_escrow,           " +
									"receipt,                  " +
									"description,              " +
									"last_updated_by,          " +
									"last_updated_datetime,    " +
									"last_updated_from)        " +
									"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
							insertStmt = conn.prepareStatement(sqlString);

							// set parameters for INSERT query
							insertStmt.setInt(1, caseId);
							insertStmt.setInt(2, sequenceNumber);
							insertStmt.setInt(3, Integer.parseInt(partyBy));
							insertStmt.setInt(4, feeFineNumber);
							insertStmt.setString(5, paidBy);
							insertStmt.setInt(6, paymentNumber);
							insertStmt.setDouble(7, Double.parseDouble(amount)-totalAppliedPayments);
							insertStmt.setDouble(8, Double.parseDouble(amount)-totalAppliedPayments);
							insertStmt.setTimestamp(9, Timestamp.valueOf(sdf2.format(sdf1.parse(paymentDate))));
							insertStmt.setString(10, receipt);
							insertStmt.setString(11, "In Escrow!");
							insertStmt.setString(12, (String)session.getAttribute("user_name"));
							insertStmt.setTimestamp(13, Timestamp.valueOf(sdf2.format(dt)));
							insertStmt.setString(14, ipAdd);

							rowsInserted = insertStmt.executeUpdate();
							if (rowsInserted != 1)
							{
								//System.out.println("Insert into table ESCROW failed");
								// if insert failed - rollback to
								// release any locks
								needToRollback = true;
								rbd = new rollbackDone(userName, methodName, sqlString);
								returnCode = OPERATION_FAILED;
							}
							else
							{
								//added this select close to be able to reuse - Luz
								if (selectStmt != null){
									selectStmt.close();
									selectStmt = null;
								}

								//System.out.println("Insert into table ESCROW successful");
								// insert to ESCROW successful
								// retrieve escrow_no for newly inserted Escrow
								selectStmt = conn.prepareStatement(
										"SELECT MAX(escrow_no)     " +
												"FROM ESCROW               " +
												"WHERE case_id = ?          " +
												"AND sequence_no = ?       " +
										"AND last_updated_by = ?" );

								selectStmt.setInt(1, caseId);
								selectStmt.setInt(2, sequenceNumber);
								selectStmt.setString(3, (String)session.getAttribute("user_name"));

								rset = selectStmt.executeQuery();

								if (rset.next()) {
									escrowNumber = rset.getInt(1);
									//System.out.println("Retrieved escrow_no = " + escrowNumber);
								} else {
									// problem with retrieving cash_bond_no
									System.out.println("Failed to retrieve escrow_no");
									needToRollback = true;
									returnCode = OPERATION_FAILED;
								}
							}
						}
					}
					else
					{
						sqlString = "INSERT INTO PAYBREAK (  " +
								"fee_fine_no,            " +
								"payment_no,             " +
								"amount,                 " +
								"account_no,             " +
								"last_updated_by,        " +
								"last_updated_datetime,  " +
								"last_updated_from)      " +
								"VALUES (?, ?, ?, ?, ?, ?, ?)";
						insertStmt = conn.prepareStatement(sqlString);

						insertStmt.setInt(1, feeFineNumber);
						insertStmt.setInt(2, paymentNumber);
						insertStmt.setDouble(3, Double.parseDouble(amount)-totalAppliedPayments);
						insertStmt.setString(4, oPmtAccNo);
						if(request.getParameter("received_by") != null && !(request.getParameter("received_by").toString().equals("")))
							insertStmt.setString(5, request.getParameter("received_by").toString());
						else
							insertStmt.setString(5, (String)session.getAttribute("user_name"));
						insertStmt.setTimestamp(6, Timestamp.valueOf(sdf2.format(dt)));
						insertStmt.setString(7, ipAdd);

						rowsInserted = insertStmt.executeUpdate();

						if (rowsInserted != 1) {
							System.out.println("Insert into table PAYBREAK failed");
							returnCode = OPERATION_FAILED;
							needToRollback = true;
							rbd = new rollbackDone(userName, methodName, sqlString);
						} else {

							// insert to PAYBREAK successful
							//System.out.println("Insert to PAYBREAK successful");
						}
					}
				}
			}
			if (((String)session.getAttribute("user_clerk")).equals("COL") ){
				if(request.getParameter("received_by") != null && !(request.getParameter("received_by").toString().equals(""))){
					//do nothing, we don't want to create debtor notes of payments that happened already
				}else{
					if (needToRollback == false) {
						if (insertStmt != null)
							insertStmt.close();
						rowsInserted=0;

						insertStmt = conn.prepareStatement(
								"INSERT INTO DEBTORS_NOTES (   " +
										"person_no,          		" +
										"subtype,           		" +
										"description,			" +
										"entered_date,     		" +
										"entered_by, 			" +
										"entered_from)      		" +
										"VALUES              	" +
								"(?, ?, ?, ?, ?, ?)");
						insertStmt.setInt(1,Integer.parseInt((String)session.getAttribute("person_no")));
						insertStmt.setString(2,"PYMT");
						String checkReference = "";
						String commentReference = "";
						if (!"".equals(checkNumber))
							checkReference = " | Ref:"+checkNumber;
						if (!"".equals(description))
							commentReference = " | Comment:"+description;
						insertStmt.setString(3,"A "+method+" payment of $"+df1.format(Double.parseDouble(amount))+" was made by "+paidBy+checkReference+commentReference + " | Payment made for Payment No: " + paymentNumber);
						insertStmt.setTimestamp(4, Timestamp.valueOf(sdf2.format(dt)));
						insertStmt.setString(5, (String)session.getAttribute("user_name"));
						insertStmt.setString(6, ipAdd);

						rowsInserted = insertStmt.executeUpdate();

						if (rowsInserted != 1){
							System.out.println("Insert into table CASE DETAILS for delete of Installment failed");
							// if insert failed - rollback to
							// release any locks
							needToRollback = true;
							returnCode = OPERATION_FAILED;
						}


					}
				}
			}
		}

		catch (NumberFormatException e) {
			// parseDouble() throws this exception
			System.out.println("Caught NumberFormatException in PaymentBean.processPayment()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "NumberFormatException caught");
			returnCode = DATABASE_EXCEPTION;
		}

		catch (ParseException e) {
			// parse() throws this exception
			System.out.println("Caught ParseException in PaymentBean.processPayment()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "ParseException caught");
			returnCode = DATABASE_EXCEPTION;
		}
		catch (SQLException e) {
			// preparedStatement(), executeUpdate() throw this exception
			System.out.println("Caught SQLException in PaymentBean.processPayment()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;
			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "SQLException caught");
			returnCode = DATABASE_EXCEPTION;
		}

		finally {

			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.processPayment().");
			try {
				//was commented out rset.close
				if (rset != null) {  // Close resultSet
					try { rset.close(); } catch (SQLException e) { System.out.println("Caught rset not closed in PaymentBean.processPayment()!"); }
					rset = null;
				}
				if (selectStmt != null) {   // Close statement
					try { selectStmt.close(); } catch (SQLException e) { System.out.println("Caught selectStmt not closed in PaymentBean.processPayment()!"); }
					selectStmt = null;
				}
				if (insertStmt != null) {   // Close insert
					try { insertStmt.close(); } catch (SQLException e) { System.out.println("Caught selectStmt not closed in PaymentBean.processPayment()!"); }
					insertStmt = null;
				}
				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					try { conn.close(); } catch (SQLException e) { System.out.println("Caught connection not closed in PaymentBean.processPayment()!"); }
					conn = null;
				}

				return returnCode;
			}
			catch (SQLException e) {
				// close() throw this exception
				System.out.println("Caught SQLException in PaymentBean.processPayment()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// inserts took place
				return returnCode;
			}
		}
	}

	/**
	 * reads payment information from the database
	 * <p>
	 * @param payment number as <code>int</code>
	 * @param case category as <code>String</code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="PaymentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: payment data is read from the database
	 *               with focus on payment distribution </i>
	 */

	public int readPaymentData(int paymentNo, String caseCategory, String clerk)
	{
		System.out.println("before readPaymentData:"+paymentNo+caseCategory+clerk);
		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int i = 0;
		String str = "";
		String[][] tmpArray = new String[MAX_SIZE][3];
		String lName = "";
		String mName = "";
		String fName = "";
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet rset = null;
		String selectFields = "";
		try {
			conn = getConnection(jndiResource);

			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection

			// clear values in the member variables (rest form)
			reset();

			// define format used by form
			SimpleDateFormat sdf = new SimpleDateFormat ("MM/dd/yyyy");

			// define currency format
			DecimalFormat df = new DecimalFormat ("#0.00");


			//selectStmt = conn.prepareStatement (
			//"SELECT * FROM PAYMENTS    " +
			//"WHERE payment_no = ?");

			if (caseCategory == null || caseCategory.equals("NONE")) {
				selectStmt = conn.prepareStatement(
						"SELECT cases.case_id, sequence_no, category FROM cases INNER JOIN payments " +
								"ON cases.case_id = payments.case_id " +
						"WHERE payment_no = ? " );
				selectStmt.setInt(1, paymentNo);
				rset = selectStmt.executeQuery();
				if (rset.next()) {
					if ("C".equalsIgnoreCase(rset.getString("category")))
						caseCategory = "CRIMINAL";
					else
						caseCategory = "CIVIL";
					//read these here for reference
					caseId = rset.getInt("case_id");
					sequenceNumber = rset.getInt("sequence_no");
				}
				selectStmt.close();
				rset.close();
			}
			if ( clerk.equals("COL") ){
				selectFields = "       PAYMENTS.voided_by,       " +
						"		PAYMENTS.allocid		  ";
			} else {
				selectFields = "       PAYMENTS.voided_by       ";
			}
			System.out.println("before selectFields:"+selectFields);
			selectStmt = conn.prepareStatement (
					"SELECT PAYMENTS.amount,           " +
							"       code_table.`code` as method,                    " +
							"       check_no,                  " +
							"       payment_date,              " +
							"       paid_by,                   " +
							"       PAYMENTS.description,      " +
							"       receipt,                   " +
							"       PAYMENTS.last_updated_by,   " +
							"       party_responsible,         " +
							"       persons.last_name,         " +
							"       persons.first_name,        " +
							"       persons.middle_name,       " +
							"CASE WHEN users.first_name IS NULL THEN '' ELSE USERS.FIRST_NAME END AS X, " +
							"CASE WHEN USERS.LAST_NAME IS NULL THEN '' ELSE USERS.LAST_NAME END AS Y, " +
							selectFields+
							",payments.last_updated_by " +
							"FROM (PAYMENTS left join USERS ON PAYMENTS.last_updated_by=USERS.name "
							+ "             left join code_table ON PAYMENTS.method = code_table.code "
							+ ") , PAYBREAK, FEES_FINES, PARTIES, PERSONS    " +
							"WHERE PAYMENTS.case_id = FEES_FINES.case_id        " +
							"AND PAYMENTS.sequence_no = FEES_FINES.sequence_no  " +
							"AND PAYBREAK.payment_no = PAYMENTS.payment_no      " +
							"AND PAYBREAK.fee_fine_no = FEES_FINES.fee_fine_no  " +
							"AND FEES_FINES.party_responsible = PARTIES.party_no " +
							"AND PARTIES.person_no = PERSONS.person_no          " +
					"AND PAYMENTS.payment_no = ?");


			selectStmt.setInt(1, paymentNo);
			//System.out.println( " ********************* " + selectStmt.toString());
			rset = selectStmt.executeQuery();

			//System.out.println("after executing payment");
			// expect one row
			if (rset.next()) {
				//System.out.println("in the rset");
				amount = df.format(rset.getDouble("amount"));

				str = rset.getString("method");
				if (str != null)
					method = str.trim();

				str = rset.getString("check_no");

				if (str != null)
					checkNumber = str.trim();

				Timestamp ts = rset.getTimestamp("payment_date");
				paymentDate = sdf.format(ts);
				paidBy = rset.getString("paid_by").trim();
				str = rset.getString("description");
				if (str != null)
					description = str.trim();

				str = rset.getString("receipt");
				if (str != null)
					receipt = str.trim();

				partyResNo = String.valueOf(rset.getInt("party_responsible"));

				str = rset.getString("voided_by");
				if (str != null)
					voidBy = str.trim();
				else
					voidBy = "";

				lName = rset.getString(10);
				if (lName != null)
					lName = lName.trim();
				else
					lName = "";

				fName = rset.getString(11);
				if (fName != null)
					fName = fName.trim();
				else
					fName = "";

				mName = rset.getString(12);
				if (mName != null)
					mName = mName.trim();
				else
					mName = "";

				partyRes = lName + " " + fName + " " + mName;

				fName = "";
				lName = "";

				fName = rset.getString(13);
				if (fName != null)
					fName = fName.trim();
				else
					fName = "";

				lName = rset.getString(14);
				if (lName != null)
					lName = lName.trim();
				else
					lName = "";

				str = rset.getString("last_updated_by");
				if(str != null)
					userPayment = str.trim();
				cashieredBy = fName + " " + lName ;
				if ( clerk.equals("COL") ){
					transactionId = rset.getInt("allocid");
				}
				// read paybreak info to paybreakArray
				//        rset.close();
				selectStmt.close();
				selectStmt = null;

				selectStmt = conn.prepareStatement (
						"SELECT value,                  " +
								"PAYBREAK.amount,               " +
								"PAYBREAK.fee_fine_no           " +
								"FROM PAYBREAK,                 " +
								"FEES_FINES, CODE_TABLE         " +
								"WHERE payment_no = ?           " +
								"AND PAYBREAK.fee_fine_no = FEES_FINES.fee_fine_no " +
								"AND fee_type = code            " +
								"AND (code_type = ?             " +
								"OR code_type = 'fee_type_misc' " +
						"OR code_type = 'fine_type')");

				selectStmt.setInt(1, paymentNo);

				if ("CRIMINAL".equals(caseCategory))
					selectStmt.setString(2, "fee_type_criminal");

				if (!"CRIMINAL".equals(caseCategory))
					selectStmt.setString(2, "fee_type_civil");

				rset = selectStmt.executeQuery();

				while ( (rset.next()) && (i < MAX_SIZE)) {
					System.out.println("in the while");

					tmpArray[i][0] = rset.getString(1).trim();

					double amt = rset.getDouble(2);
					tmpArray[i][1] = df.format(amt);
					eachPaidAmount += amt;

					System.out.println("Test");
					tmpArray[i][2] = String.valueOf(rset.getInt(3));


					i++;
				}

				if (i == 0) {
					System.out.println("SELECT from table PAYBREAK failed nn");
					// if select failed - rollback to
					// release any locks
					needToRollback = true;
					returnCode = OPERATION_FAILED;
				} else {

					// create paybreakArray
					paybreakArray = new String[i][3];

					// copy data from tmp array
					for (int j=0; j<i;j++) {
						paybreakArray[j][0] = tmpArray[j][0];
						paybreakArray[j][1] = tmpArray[j][1];
						paybreakArray[j][2] = tmpArray[j][2];
					}
				}
			} else {
				throw new Exception("SELECT from table PAYMENTS failed");
			}
		}
		catch (Exception e) {
			// preparedStatement(), executeUpdate() throw this exception
			System.out.println("Caught Exception in PaymentBean.readPaymentData()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			returnCode = OPERATION_FAILED;
		}
		finally {

			// allow gc
			tmpArray = null;

			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.readPaymentData().");
			try {
				// rset.close was commented out
				//	if (rset != null) {  // Close resultSet
				//		try { rset.close(); } catch (SQLException e) { System.out.println("Caught rset not closed in PaymentBean.readPaymentData()!"); }
				//	rset = null;
				//	}
				if (selectStmt != null) {   // Close statement
					try { selectStmt.close(); } catch (SQLException e) { System.out.println("Caught selectStmt not closed in PaymentBean.readPaymentData()!"); }
					selectStmt = null;
				}

				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					try { conn.close(); } catch (SQLException e) { System.out.println("Caught connection not closed in PaymentBean.readPaymentData()!"); }
					conn = null;
				}


			}
			catch (SQLException e) {
				// close() throw this exception
				System.out.println("Caught SQLException in PaymentBean.readPaymentData()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// inserts took place
				return returnCode;
			}
		}

		return returnCode;
	}

	/**
	 * reads payment information from the database
	 * <p>
	 * @param ID of the case payments are read as <code>int</code>
	 * @param sequence number of the case payments are read as <code>int</code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="PaymentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 *   <li><code><a href="PaymentBean.html#NO_DATA_FOUND">NO_DATA_FOUND</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: payment data is read from the database
	 *               with focus on payments history </i>
	 */

	public int readAllPayments(int csId, int seqNo)
	{

		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int i = 0;
		String str = "";
		String[][] tmpArray = new String[MAX_SIZE][7];

		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet rset = null;

		try {
			conn = getConnection(jndiResource);

			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection

			// clear values in the member variables (rest form)
			reset();

			// define format used by form
			SimpleDateFormat sdf = new SimpleDateFormat ("MM/dd/yyyy");

			// define currency format
			DecimalFormat df = new DecimalFormat ("####0.00");

			selectStmt = conn.prepareStatement (
					"SELECT DISTINCT PAYMENTS.payment_no,      " +
							"PAYMENTS.payment_date,           " +
							"PAYMENTS.amount,                 " +
							"PAYMENTS.paid_by,                " +
							"PAYMENTS.receipt,                " +
							//"FEES_FINES.party_responsible,    " +
							"PAYMENTS.method                  " +
							"FROM PAYMENTS left join " +
							//"("
							"PAYBREAK " +
							//+ "left join fees_fines on paybreak.fee_fine_no=fees_fines.fee_fine_no) " +
							"on payments.payment_no=paybreak.payment_no " +
							"WHERE (PAYMENTS.method Is Not Null) " +
							"AND PAYMENTS.case_id = ?    " +
							//+ "AND FEES_FINES.party_responsible is not null " +
							"AND PAYMENTS.sequence_no = ?  " +
					"ORDER BY payment_date, payment_no ASC");
			/*
			 * selectStmt = conn.prepareStatement (
			 * "SELECT DISTINCT PAYMENTS.payment_no,      " +
			 * "PAYMENTS.payment_date,           " +
			 * "PAYMENTS.amount,                 " +
			 * "PAYMENTS.paid_by,                " +
			 * "PAYMENTS.receipt,                " +
			 * "FEES_FINES.party_responsible,    " +
			 * "PAYMENTS.method                  " +
			 * "FROM PAYMENTS, PAYBREAK, FEES_FINES " +
			 * "WHERE PAYMENTS.payment_no = PAYBREAK.payment_no " +
			 * "AND PAYBREAK.fee_fine_no = FEES_FINES.fee_fine_no " +
			 * "AND (PAYMENTS.method Is Not Null) " +
			 * "AND PAYMENTS.case_id = ?     " +
			 * "AND PAYMENTS.sequence_no = ?  " +
			 * "ORDER BY payment_date, PAYMENTS.method ASC");
			 */
			selectStmt.setInt(1, csId);
			selectStmt.setInt(2, seqNo);

			rset = selectStmt.executeQuery();
			//System.out.println(selectStmt.toString());

			while ((rset.next()) && (i<MAX_SIZE)) {

				tmpArray[i][0] = String.valueOf(rset.getInt(1));

				Timestamp ts = rset.getTimestamp(2);
				tmpArray[i][1] = sdf.format(ts);;

				tmpArray[i][2] = df.format(rset.getDouble(3));

				tmpArray[i][3] = rset.getString(4).trim();

				str = rset.getString(5);
				if (str != null)
					tmpArray[i][4] = str.trim();
				else
					tmpArray[i][4] = "";
				tmpArray[i][5] = "N/A";//THIS USED TO BE THE PARTY RESPONSABLE, BUT WE DON'T USE THIS ANYMORE
				str = rset.getString(6);
				if (str != null)
					tmpArray[i][6] = str.trim();
				else
					tmpArray[i][6] = "";

				i++;
			}

			//System.out.println("Number of payments found: "+i);

			if (i > 0) {

				// create paymentsArray
				paymentsArray = new String[i][7];

				// copy data to paymentsArray
				for (int j=0; j<i; j++)
					for (int k=0; k<7; k++)
						paymentsArray[j][k] = tmpArray[j][k];
			} else
				returnCode = NO_DATA_FOUND;
		}
		catch (SQLException e) {
			// preparedStatement(), executeUpdate() throw this exception
			System.out.println("Caught SQLException in PaymentBean.readAllPayments()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			returnCode = DATABASE_EXCEPTION;
		}
		finally {

			// allow gc
			tmpArray = null;
			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.readAllPayments().");
			try {

				//did not have rset.close it was commented out
				if (rset != null) {  // Close resultSet
					try { rset.close(); } catch (SQLException e) { System.out.println("Caught rset not closed in PaymentBean.readAllPayments()!"); }
					rset = null;
				}
				if (selectStmt != null) {   // Close statement
					try { selectStmt.close(); } catch (SQLException e) { System.out.println("Caught selectStmt not closed in PaymentBean.readAllPayments!"); }
					selectStmt = null;
				}

				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					try { conn.close(); } catch (SQLException e) { System.out.println("Caught connection not closed in PaymentBean.readAllPayments()!"); }
					conn = null;
				}

				return returnCode;
			}
			catch (SQLException e) {
				// close() throw this exception
				System.out.println("Caught SQLException in PaymentBean.readAllPayments()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// inserts took place
				return returnCode;
			}
		}
	}

	/**
	 * processes and stores Escrow information in the database
	 * <p>
	 * @param HTTP request as <code>HttpServletRequest</code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="PaymentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: data is stored in the database </i>
	 */

	public int processEscrow(String partyBy, String caseCategory, int escrowNo, String escrowAmount, int escrowFeeNo, HttpServletRequest request)
	{
		methodName = "processEscrow(String partyBy, String caseCategory, int escrowNo, String escrowAmount, int escrowFeeNo, HttpServletRequest request)";
		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int rowsInserted = 0;
		int rowsUpdated = 0;
		HttpSession session = null;
		Map paramMap = null;
		Iterator iter = null;
		Map.Entry me = null;

		String feeGroup = "";
		String feeType = "";
		String str = "";
		String category = "";
		String escrowAccNo = "";
		String waiveAccNo = "";
		String oPmtAccNo = "";
		int feeFineNumber = 0;

		if (!"CRIMINAL".equals(caseCategory))
			category = "V";
		if ("CRIMINAL".equals(caseCategory))
			category = "C";

		Connection conn = null;
		PreparedStatement insertStmt = null;
		PreparedStatement selectStmt = null;
		PreparedStatement updateStmt = null;
		ResultSet rset = null;
		//System.out.println("Party By in processEscrow " + partyBy);

		String ipAdd = request.getRemoteAddr();
		rollbackDone rbd = null;
		try
		{
			conn = getConnection(jndiResource);
			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection

			// get current session based on HttpServletRequest
			// and don't create a new on (create=false)
			session = request.getSession(false);

			// get case_id and sequence_no from session object
			caseId = Integer.parseInt((String)session.getAttribute("case_id"));
			sequenceNumber = Integer.parseInt((String)session.getAttribute("seq_no"));

			userName = (String)session.getAttribute("user_name");
			// capture current date/time and format it
			java.util.Date dt = new java.util.Date();

			// define format used by form
			SimpleDateFormat sdf1 = new SimpleDateFormat ("MM/dd/yyyy");
			// define format for JDBC Timestamp
			SimpleDateFormat sdf2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

			if (Double.parseDouble(amount) > currentBalance) {
				selectStmt = conn.prepareStatement(
						"SELECT FEES_FINES.fee_fine_no,        " +
								"       FEES_FINES.account_no          " +
								"FROM FEES_FINES                       " +
								"WHERE case_id = ?                     " +
								"AND sequence_no = ?                   " +
								"AND fee_type = " + "'" + "OPMT" + "' " );

				selectStmt.setInt(1, caseId);
				selectStmt.setInt(2, sequenceNumber);

				rset = selectStmt.executeQuery();

				if (rset.next()) {
					// Over payment is already entered for this case
					//System.out.println("Over Payment fee is already entered.  Do not need to insert.");
					feeFineNumber = rset.getInt(1);
					oPmtAccNo = rset.getString(2).trim();
					//System.out.println("The fee_fine_no is= " + feeFineNumber);

				} else {
					//System.out.println("Needs to enter Over Payment fee in the FEES_FINES. " );

					// close statement to be reused
					if (selectStmt != null){
						selectStmt.close();
						selectStmt = null;
					}

					// get the account number from the FEE_SCHEDULE
					selectStmt = conn.prepareStatement(
							"SELECT FEE_SCHEDULE.account_no        " +
									"FROM FEE_SCHEDULE                     " +
									"WHERE fee_group Is Null               " +
									"AND case_category = " + "'" + category + "' " +
									"AND fee_type = " + "'" + "OPMT" + "'");

					rset = selectStmt.executeQuery();

					if (!(rset.next())) {
						// problem with retrieving account_no
						System.out.println("Failed to read account_no");
						needToRollback = true;
						returnCode = OPERATION_FAILED;
					} else {
						oPmtAccNo = rset.getString(1).trim();

						//System.out.println("The over payment account_no is= " + oPmtAccNo);

						rowsInserted = 0;
						sqlString = "INSERT INTO FEES_FINES (  " +
								"case_id,                  " +
								"sequence_no,              " +
								"fee_group,                " +
								"fee_type,                 " +
								"account_no,               " +
								"party_responsible,        " +
								"amount,                   " +
								"description,              " +
								"date_ordered,             " +
								"date_due,                 " +
								"last_updated_by,          " +
								"last_updated_datetime,    " +
								"last_updated_from)        " +
								"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
						insertStmt = conn.prepareStatement(sqlString);

						// set parameters for INSERT query
						insertStmt.setInt(1, caseId);
						insertStmt.setInt(2, sequenceNumber);
						insertStmt.setString(3, " ");
						insertStmt.setString(4, "OPMT");
						insertStmt.setString(5, oPmtAccNo);
						insertStmt.setInt(6, Integer.parseInt(partyBy));
						insertStmt.setDouble(7, 0.0);
						insertStmt.setString(8, "Over Payment on the fees/fines");
						insertStmt.setTimestamp(9, Timestamp.valueOf(sdf2.format(sdf1.parse(paymentDate))));
						insertStmt.setTimestamp(10, Timestamp.valueOf(sdf2.format(sdf1.parse(paymentDate))));
						insertStmt.setString(11, (String)session.getAttribute("user_name"));
						insertStmt.setTimestamp(12, Timestamp.valueOf(sdf2.format(dt)));
						insertStmt.setString(13, ipAdd);

						rowsInserted = insertStmt.executeUpdate();

						if (rowsInserted != 1) {
							//System.out.println("Insert into table FEES_FINES failed");
							// if insert failed - rollback to
							// release any locks
							needToRollback = true;
							rbd = new rollbackDone(userName, methodName, sqlString);
							returnCode = OPERATION_FAILED;
						} else {

							// insert to FEES_FINES successful
							//System.out.println("Insert to FEES_FINES successful");

							// close statement to be reused
							if (selectStmt != null){
								selectStmt.close();
								selectStmt = null;
							}

							// retrieve fee_fine_no for newly inserted fee/fine
							selectStmt = conn.prepareStatement(
									"SELECT MAX(fee_fine_no)     " +
											"FROM FEES_FINES             " +
											"WHERE case_id = ?           " +
											"AND sequence_no = ?         " +
									"AND last_updated_by = ?" );

							selectStmt.setInt(1, caseId);
							selectStmt.setInt(2, sequenceNumber);
							selectStmt.setString(3, (String)session.getAttribute("user_name"));

							rset = selectStmt.executeQuery();

							if (rset.next()) {
								feeFineNumber = rset.getInt(1);
								//System.out.println("The fee_fine_no is= " + feeFineNumber);

							} else {
								// problem with retrieving fee_fine_no
								System.out.println("Failed to retrieve fee_fine_no");
								needToRollback = true;
								returnCode = OPERATION_FAILED;
							}
						}
					}
				}
			}
			// if no problems reported (needToRollback == false)
			// continue with inserting to PAYMENTS
			if (needToRollback == false) {

				// close statement to be reused
				if (insertStmt != null){
					insertStmt.close();
					insertStmt = null;
				}

				rowsInserted = 0;
				// insert row in PAYMENTS
				sqlString = "insert into payments"
						+ " (case_id, sequence_no, amount, payment_date, method, paid_by"
						+ ", description, receipt, check_no, escrow_no, last_updated_by"
						+ ", last_updated_datetime, last_updated_from)"
						+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				insertStmt = conn.prepareStatement(sqlString);
				insertStmt.setInt(1, caseId);
				insertStmt.setInt(2, sequenceNumber);
				insertStmt.setDouble(3, -Double.parseDouble(amount));
				insertStmt.setTimestamp(4, Timestamp.valueOf(sdf2.format(sdf1.parse(paymentDate))));
				insertStmt.setString(5, "ESCROW");
				insertStmt.setString(6, "Escrow Out!");
				insertStmt.setString(7, "Payment from escrow");
				insertStmt.setString(8, receipt);
				insertStmt.setString(9, checkNumber);
				insertStmt.setInt(10, escrowNo);
				insertStmt.setString(11, (String)session.getAttribute("user_name"));
				insertStmt.setTimestamp(12, Timestamp.valueOf(sdf2.format(dt)));
				insertStmt.setString(13, ipAdd);

				rowsInserted = insertStmt.executeUpdate();
				if (rowsInserted != 1)
				{
					System.out.println("Insert into table PAYMENTS failed");
					// if insert failed - rollback to
					// release any locks
					needToRollback = true;
					rbd = new rollbackDone(userName, methodName, sqlString);
					returnCode = OPERATION_FAILED;
				}
				else
				{ // insert to PAYMENTS successful
					// retrieve payment_no for newly inserted payment
					selectStmt = conn.prepareStatement(
							"SELECT MAX(payment_no)   " +
									"FROM PAYMENTS            " +
									"WHERE case_id = ?        " +
									"AND sequence_no = ?      " +
							"AND last_updated_by = ?" );
					selectStmt.setInt(1, caseId);
					selectStmt.setInt(2, sequenceNumber);
					selectStmt.setString(3, (String)session.getAttribute("user_name"));
					rset = selectStmt.executeQuery();
					if (rset.next()) {
						paymentNumber = rset.getInt(1);
						//System.out.println("Retrieved payment_no = " + paymentNumber);

						// retrieve dynamically created parameterts
						paramMap = request.getParameterMap();
						iter = (paramMap.entrySet()).iterator();

						// close insert statement to be reused
						if (selectStmt != null){
							selectStmt.close();
							selectStmt = null;
						}
						if (insertStmt != null){
							insertStmt.close();
							insertStmt = null;
						}

						rowsInserted = 0;

						// get the account number from the FEE_SCHEDULE for fee_group ES and fee_type ESCROW
						selectStmt = conn.prepareStatement(
								"SELECT FEE_SCHEDULE.account_no        " +
										"FROM FEE_SCHEDULE                     " +
										"WHERE fee_group Is Null               " +
										"AND case_category = " + "'" + category + "' " +
										"AND fee_type = " + "'" + "ESCROW" + "'");

						rset = selectStmt.executeQuery();

						if (!(rset.next()))
						{
							// problem with retrieving account_no
							System.out.println("Failed to read account_no");
							needToRollback = true;
							returnCode = OPERATION_FAILED;
						}
						else
						{
							escrowAccNo = rset.getString(1).trim();
							//System.out.println("The account_no is= " + escrowAccNo);
							sqlString = "INSERT INTO PAYBREAK (  " +
									"fee_fine_no,            " +
									"payment_no,             " +
									"amount,                 " +
									"account_no,             " +
									"last_updated_by,        " +
									"last_updated_datetime,  " +
									"last_updated_from)      " +
									"VALUES (?, ?, ?, ?, ?, ?, ?)";
							insertStmt = conn.prepareStatement(sqlString);
							insertStmt.setInt(1, escrowFeeNo);
							insertStmt.setInt(2, paymentNumber);
							insertStmt.setDouble(3, -Double.parseDouble(amount));
							insertStmt.setString(4, escrowAccNo);
							insertStmt.setString(5, (String)session.getAttribute("user_name"));
							insertStmt.setTimestamp(6, Timestamp.valueOf(sdf2.format(dt)));
							insertStmt.setString(7, ipAdd);

							rowsInserted = insertStmt.executeUpdate();
							if (rowsInserted != 1)
							{
								System.out.println("Insert into table PAYBREAK failed");
								returnCode = OPERATION_FAILED;
								// leave while loop
							}
						}
					}
					else
					{
						// problem with retrieving payment_no
						System.out.println("Failed to retrieve payment_no");
						needToRollback = true;
						returnCode = OPERATION_FAILED;
					}
				}
			}

			// if no problems reported (needToRollback == false)
			// continue with inserting to ESCROW
			if (needToRollback == false)
			{
				//System.out.println("Escrow fee number " + escrowFeeNo);
				//System.out.println("Escrow number " + escrowNo);

				rowsUpdated = 0;
				if (dbType.equalsIgnoreCase("MYSQL")) {
					// update escrow amount in ESCROW table
					updateStmt = conn.prepareStatement(
							"update escrow"
									+ " set current_balance = ?"
									+ ", date_of_escrow = ?"
									+ ", description = Concat(description,',',?,' payment')"
									+ ", last_updated_datetime = ?"
									+ ", last_updated_by = ?"
									+ ", last_updated_from = ?"
									+ " where escrow_no=" + escrowNo);
				} else {
					// update escrow amount in ESCROW table
					updateStmt = conn.prepareStatement(
							"update escrow"
									+ " set current_balance = ?"
									+ ", date_of_escrow = ?"
									+ ", description = description & ',' & ? & ' payment'"
									+ ", last_updated_datetime = ?"
									+ ", last_updated_by = ?"
									+ ", last_updated_from = ?"
									+ " where escrow_no=" + escrowNo);
				}
				updateStmt.setDouble(1, Double.parseDouble(escrowAmount)-Double.parseDouble(amount));
				updateStmt.setTimestamp(2, Timestamp.valueOf(sdf2.format(sdf1.parse(paymentDate))));
				updateStmt.setString(3, amount + " to fees");
				updateStmt.setTimestamp(4, Timestamp.valueOf(sdf2.format(dt)));
				updateStmt.setString(5, (String)session.getAttribute("user_name"));
				updateStmt.setString(6, ipAdd);

				rowsUpdated = updateStmt.executeUpdate();

				if (rowsUpdated != 1)
				{
					System.out.println("Update in table ESCROW failed");
					// if update failed - rollback to
					// release any locks
					needToRollback = true;
					returnCode = OPERATION_FAILED;
				}
			}
			// if no problems reported (needToRollback == false)
			// continue with inserting to PAYMENTS
			if (needToRollback == false)
			{
				// close statement to be reused
				if (insertStmt != null){
					insertStmt.close();
					insertStmt = null;
				}
				rowsInserted = 0;
				// insert row in PAYMENTS
				sqlString = "insert into payments"
						+ " (case_id, sequence_no, amount, payment_date, method, paid_by"
						+ ", description, receipt, check_no, escrow_no, last_updated_by"
						+ ", last_updated_datetime, last_updated_from)"
						+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				insertStmt = conn.prepareStatement(sqlString);
				insertStmt.setInt(1, caseId);
				insertStmt.setInt(2, sequenceNumber);
				insertStmt.setDouble(3, Double.parseDouble(amount));
				insertStmt.setTimestamp(4, Timestamp.valueOf(sdf2.format(sdf1.parse(paymentDate))));
				insertStmt.setString(5, method);
				insertStmt.setString(6, "Payment from escrow");
				insertStmt.setString(7, description);
				insertStmt.setString(8, receipt);
				insertStmt.setString(9, checkNumber);
				insertStmt.setInt(10, escrowNo);
				insertStmt.setString(11, (String)session.getAttribute("user_name"));
				insertStmt.setTimestamp(12, Timestamp.valueOf(sdf2.format(dt)));
				insertStmt.setString(13, ipAdd);

				rowsInserted = insertStmt.executeUpdate();
				if (rowsInserted != 1)
				{
					System.out.println("Insert into table PAYMENTS failed");
					// if insert failed - rollback to
					// release any locks
					needToRollback = true;
					rbd = new rollbackDone(userName, methodName, sqlString);
					returnCode = OPERATION_FAILED;
				}
				else
				{
					// insert to PAYMENTS successful

					// retrieve payment_no for newly inserted payment
					selectStmt = conn.prepareStatement(
							"SELECT MAX(payment_no)   " +
									"FROM PAYMENTS            " +
									"WHERE case_id = ?        " +
									"AND sequence_no = ?      " +
							"AND last_updated_by = ?" );

					selectStmt.setInt(1, caseId);
					selectStmt.setInt(2, sequenceNumber);
					selectStmt.setString(3, (String)session.getAttribute("user_name"));

					rset = selectStmt.executeQuery();
					if (rset.next())
					{
						paymentNumber = rset.getInt(1);
						//System.out.println("Retrieved payment_no = " + paymentNumber);

						// retrieve dynamically created parameterts
						paramMap = request.getParameterMap();
						iter = (paramMap.entrySet()).iterator();

						// close insert statement to be reused
						insertStmt.close();
						insertStmt = null;

						if (selectStmt != null)
							selectStmt.close();
						sqlString = "INSERT INTO PAYBREAK (  " +
								"fee_fine_no,            " +
								"payment_no,             " +
								"amount,                 " +
								"account_no,             " +
								"last_updated_by,        " +
								"last_updated_datetime,  " +
								"last_updated_from)      " +
								"VALUES (?, ?, ?, ?, ?, ?, ?)";
						insertStmt = conn.prepareStatement(sqlString);
						//System.out.println("Test after the insert statement in PAYBREAK");
						while (iter.hasNext())
						{
							me = (Map.Entry)iter.next();
							String param = (String)me.getKey();
							String value[] = (String[])me.getValue();
							rowsInserted = 0;

							//System.out.println("String param is "+ param);
							//System.out.println("Test after the while");
							// if request parameter  has feeXXX format (XXX is fee_fine_no)
							// and value for this parameter != ""

							if ( ("fee".equals(param.substring(0,3))) && (!"".equals(value[0])) ) {
								String feeNo = param.substring(3);
								//System.out.println("fee number is "+ feeNo);

								// close ResultSet and statement to be reused
								//System.out.println("Test!" + caseCategory);

								// get the account number from the FEE_SCHEDULE for the fee_type
								/*
								 * selectStmt = conn.prepareStatement(
								 * "SELECT FEES_FINES.account_no                " +
								 * "FROM FEE_SCHEDULE, FEES_FINES    " +
								 * "WHERE FEES_FINES.fee_type = FEE_SCHEDULE.fee_type    " +
								 * "AND FEES_FINES.account_no = FEE_SCHEDULE.account_no  " +
								 * "AND FEES_FINES.fee_fine_no = " + feeNo + " " +
								 * "AND FEE_SCHEDULE.case_category = "+"'" + category + "' " );
								 */
								selectStmt = conn.prepareStatement(
										"SELECT account_no                " +
												"FROM FEES_FINES    " +
												"WHERE FEES_FINES.fee_fine_no = " + feeNo);

								//System.out.println("Test!");
								rset = selectStmt.executeQuery();

								if (!(rset.next()))
								{
									// problem with retrieving account_no
									System.out.println("Failed to read account_no");
									needToRollback = true;
									returnCode = OPERATION_FAILED;
								}
								else
								{
									str = rset.getString(1);
									if (str != null)
										accountNo = str.trim();
								}
								//System.out.println("The account_no is= " + accountNo);
								insertStmt.setInt(1, Integer.parseInt(feeNo));
								insertStmt.setInt(2, paymentNumber);
								insertStmt.setDouble(3, Double.parseDouble(value[0]));
								insertStmt.setString(4, accountNo);
								insertStmt.setString(5, (String)session.getAttribute("user_name"));
								insertStmt.setTimestamp(6, Timestamp.valueOf(sdf2.format(dt)));
								insertStmt.setString(7, ipAdd);

								rowsInserted = insertStmt.executeUpdate();
								if (rowsInserted != 1)
								{
									System.out.println("Insert into table PAYBREAK failed");
									returnCode = OPERATION_FAILED;

									// leave while loop
									break;
								}
							}
						}
					}
					else
					{
						// problem with retrieving payment_no
						System.out.println("Failed to retrieve payment_no");
						needToRollback = true;
						returnCode = OPERATION_FAILED;
					}
				}
			}
			if (Double.parseDouble(amount) > currentBalance)
			{
				if (needToRollback == false)
				{
					// close statement to be reused
					if (insertStmt != null)
						insertStmt.close();

					rowsInserted = 0;
					sqlString = "INSERT INTO PAYBREAK (  " +
							"fee_fine_no,            " +
							"payment_no,             " +
							"amount,                 " +
							"account_no,             " +
							"last_updated_by,        " +
							"last_updated_datetime,  " +
							"last_updated_from)      " +
							"VALUES (?, ?, ?, ?, ?, ?, ?)";
					insertStmt = conn.prepareStatement(sqlString);

					insertStmt.setInt(1, feeFineNumber);
					insertStmt.setInt(2, paymentNumber);
					insertStmt.setDouble(3, Double.parseDouble(amount)-currentBalance);
					insertStmt.setString(4, oPmtAccNo);
					insertStmt.setString(5, (String)session.getAttribute("user_name"));
					insertStmt.setTimestamp(6, Timestamp.valueOf(sdf2.format(dt)));
					insertStmt.setString(7, ipAdd);

					rowsInserted = insertStmt.executeUpdate();

					if (rowsInserted != 1)
					{
						System.out.println("Insert into table PAYBREAK failed");
						returnCode = OPERATION_FAILED;
					}
					else
					{ // insert to PAYBREAK successful
						//System.out.println("Insert to PAYBREAK successful");
					}
				}
			}
		}
		catch (NumberFormatException e)
		{
			// parseDouble() throws this exception
			System.out.println("Caught NumberFormatException in PaymentBean.processEscrow()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "NumberFormatException caught");
			returnCode = DATABASE_EXCEPTION;
		}
		catch (ParseException e)
		{
			// parse() throws this exception
			System.out.println("Caught ParseException in PaymentBean.processEscrow()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "ParseException caught");
			returnCode = DATABASE_EXCEPTION;
		}
		catch (SQLException e)
		{
			// preparedStatement(), executeUpdate() throw this exception
			System.out.println("Caught SQLException in PaymentBean.processEscrow()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;
			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "SQLException caught");
			returnCode = DATABASE_EXCEPTION;
		}
		finally
		{
			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.processEscrow().");
			try
			{
				//rset.close was commented out
				if (rset != null) {  // Close rset
					try { rset.close(); } catch (SQLException e) { System.out.println("Caught rset not closed in PaymentBean.processEscrow()!"); }
					rset = null;
				}
				if (selectStmt != null) {   // Close statement
					try { selectStmt.close(); } catch (SQLException e) { System.out.println("Caught selectStmt not closed in PaymentBean.processEscrow()!"); }
					selectStmt = null;
				}

				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					try { conn.close(); } catch (SQLException e) { System.out.println("Caught connection not closed in PaymentBean.processEscrow()!"); }
					conn = null;
				}
				return returnCode;
			}
			catch (SQLException e)
			{
				// close() throw this exception
				System.out.println("Caught SQLException in PaymentBean.processEscrow()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// inserts took place
				return returnCode;
			}
		}
	}

	/**
	 * reads paid Fees/Fines to array
	 * <p>
	 * @param caseID as <code>int</code>
	 * @param sequence number as <code>int</code>
	 * @param case category as <code>String</code> (CIVIL or CRIMINAL)
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="PaymentBean.html#NO_DATA_FOUND">NO_DATA_FOUND</a></code>
	 *   <li><code><a href="PaymentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 * <p>
	 * @throws none
	 * <p>
	 * <i> Comments: reads outstanding fees / fines to array
	 *               index 0 - fee_fine_no
	 *               index 1 - fee_type
	 *               index 2 - date_due
	 *               index 3 - balance
	 *               index 4 - fee name (CODE_TABLE.value)
	 * </i>
	 */

	public int readPaidFeesFines(int csId, int seqNo, String partyTo, String caseCategory)
	{

		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int i = 0;
		String[][] tmpArray = new String[MAX_SIZE][6];

		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet rset = null;

		try {
			conn = getConnection(jndiResource);

			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection

			// define format used by form
			SimpleDateFormat sdf = new SimpleDateFormat ("MM/dd/yyyy");

			// define formats for currency
			DecimalFormat df = new DecimalFormat ("#0.00");
			DecimalFormat df1 = new DecimalFormat ("#,##0.00");

			selectStmt = conn.prepareStatement (
					"SELECT fee_fine_no,            " +
							"fee_type,                      " +
							"date_due,                      " +
							"(SELECT SUM(amount)            " +
							" FROM PAYBREAK                 " +
							" WHERE fee_fine_no = FEES_FINES.fee_fine_no), " +
							"value,                         " +
							"installments                   " +
							"FROM FEES_FINES, CODE_TABLE    " +
							"WHERE case_id = ?              " +
							"AND sequence_no = ?            " +
							"AND party_responsible = ?      " +
							"AND code = fee_type            " +
							"AND fee_type <> 'CASHBOND'     " +
							"AND fee_type <> 'REFUND'     " +
							"AND (code_type = ?             " +
							"OR code_type = 'fee_type_misc' " +
							"OR code_type = 'fine_type')    " +
					"ORDER BY installments ASC");

			selectStmt.setInt(1, csId);
			selectStmt.setInt(2, seqNo);
			selectStmt.setInt(3, Integer.parseInt(partyTo));

			if ("CRIMINAL".equals(caseCategory))
				selectStmt.setString(4, "fee_type_criminal");
			if (!"CRIMINAL".equals(caseCategory))
				selectStmt.setString(4, "fee_type_civil");

			rset = selectStmt.executeQuery();

			while ((rset.next()) && (i < MAX_SIZE)) {

				// fee_fine_no
				tmpArray[i][0] = String.valueOf(rset.getInt(1));

				// fee_type
				tmpArray[i][1] = rset.getString(2).trim();

				// date_due
				Timestamp ts = rset.getTimestamp(3);
				if (ts != null)
					tmpArray[i][2] = sdf.format(ts);
				else
					tmpArray[i][2] = " ";

				// balance
				double amt = rset.getDouble(4);
				tmpArray[i][3] = df.format(amt);
				paidBalance = paidBalance + amt;

				// fee name
				tmpArray[i][4] = rset.getString(5).trim();

				// check if installments != 0
				// (means this is one of installment fees/fines
				tmpArray[i][5] = String.valueOf(rset.getInt(6));


				// check if balance > 0
				// if not don't include that fee
				if (Double.parseDouble(tmpArray[i][3]) != 0.00)
					i++;
			}
			//System.out.println("the paid balance is " + paidBalance);
			if (i == 0)
				returnCode = NO_DATA_FOUND;

			// copy data from temp array
			paidFeesFinesArray = new String[i][6];

			for (int j=0; j<i;j++)
				for (int k=0;k<6;k++)
					paidFeesFinesArray[j][k] = tmpArray[j][k];

		}

		catch (SQLException e) {
			// preparedStatement(), executeQuery() throw this exception
			System.out.println("Caught SQLException in PaymentBean.readPaidFeesFines()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			returnCode = DATABASE_EXCEPTION;
		}
		finally {

			// allow gc
			tmpArray = null;

			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.readPaidFeesFines().");
			try {
				//rset.close was commented out
				if (rset != null) {  // Close resultSet
					try { rset.close(); } catch (SQLException e) { System.out.println("Caught rset not closed in PaymentBean.readPaidFeesFines()!"); }
					rset = null;
				}
				if (selectStmt != null) {   // Close statement
					try { selectStmt.close(); } catch (SQLException e) { System.out.println("Caught selectStmt not closed in PaymentBean.readPaidFeesFines()!"); }
					selectStmt = null;
				}

				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					try { conn.close(); } catch (SQLException e) { System.out.println("Caught connection not closed in PaymentBean.readPaidFeesFines()!"); }
					conn = null;
				}

				return returnCode;
			}
			catch (SQLException e) {
				// close() throw this exception
				System.out.println("Caught SQLException in PaymentBean.readPaidFeesFines()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// inserts took place
				return returnCode;
			}
		}

		//fee_fine_no, fee_type, amount, balance, fee_description

		//SELECT fee_fine_no, fee_type, amount, amount-(SELECT SUM(amount) FROM PAYBREAK WHERE fee_fine_no=FEES_FINES.fee_fine_no), value
		//FROM FEES_FINES, CODE_TABLE
		//WHERE case_id = 104 AND sequence_no = 1 AND code = fee_type AND fee_type <>  'BCC' AND fee_type <>  'BCC-V' AND fee_type <>  'BCC-F'
		//AND code_type = 'fee_type_criminal'
	}

	/**
	 * reads current Escrow to array
	 * <p>
	 * @param caseID as <code>int</code>
	 * @param sequence number as <code>int</code>
	 * @param case category as <code>String</code> (CIVIL or CRIMINAL)
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="PaymentBean.html#NO_DATA_FOUND">NO_DATA_FOUND</a></code>
	 *   <li><code><a href="PaymentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 * <p>
	 * @throws none
	 * <p>
	 * <i> Comments: reads current escrow to array
	 *               index 0 - fee_fine_no
	 *               index 1 - fee_type
	 *               index 2 - date_due
	 *               index 3 - balance
	 *               index 4 - fee name (CODE_TABLE.value)
	 * </i>
	 */

	public int readCurrentEscrow(int csId, int seqNo, String partyTo, String caseCategory)
	{

		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int i = 0;
		String[][] tmpArray = new String[MAX_SIZE][6];

		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet rset = null;

		try {
			conn = getConnection(jndiResource);

			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection

			// define format used by form
			SimpleDateFormat sdf = new SimpleDateFormat ("MM/dd/yyyy");

			// define formats for currency
			DecimalFormat df = new DecimalFormat ("#0.00");
			DecimalFormat df1 = new DecimalFormat ("#,##0.00");

			selectStmt = conn.prepareStatement (
					"SELECT fee_fine_no,            " +
							"fee_type,                      " +
							"date_due,                      " +
							"(SELECT SUM(amount)            " +
							" FROM PAYBREAK                 " +
							" WHERE fee_fine_no = FEES_FINES.fee_fine_no), " +
							"value,                         " +
							"installments                   " +
							"FROM FEES_FINES, CODE_TABLE    " +
							"WHERE case_id = ?              " +
							"AND sequence_no = ?            " +
							"AND party_responsible = ?      " +
							"AND code = fee_type            " +
							"AND fee_type = 'ESCROW'        " +
					"AND code_type = 'fee_type_misc' " );

			selectStmt.setInt(1, csId);
			selectStmt.setInt(2, seqNo);
			selectStmt.setInt(3, Integer.parseInt(partyTo));

			rset = selectStmt.executeQuery();

			paidBalance = 0;

			while ((rset.next()) && (i < MAX_SIZE)) {

				// fee_fine_no
				tmpArray[i][0] = String.valueOf(rset.getInt(1));

				// fee_type
				tmpArray[i][1] = rset.getString(2).trim();

				// date_due
				Timestamp ts = rset.getTimestamp(3);
				if (ts != null)
					tmpArray[i][2] = sdf.format(ts);
				else
					tmpArray[i][2] = " ";

				// balance
				double amt = rset.getDouble(4);
				tmpArray[i][3] = df.format(amt);
				paidBalance = paidBalance + amt;

				// fee name
				tmpArray[i][4] = rset.getString(5).trim();

				// check if installments != 0
				// (means this is one of installment fees/fines
				tmpArray[i][5] = String.valueOf(rset.getInt(6));


				// check if balance > 0
				// if not don't include that fee
				if (Double.parseDouble(tmpArray[i][3]) != 0.00)
					i++;
			}
			//System.out.println("the paid balance is " + paidBalance);
			if (i == 0)
				returnCode = NO_DATA_FOUND;

			// copy data from temp array
			paidFeesFinesArray = new String[i][6];

			for (int j=0; j<i;j++)
				for (int k=0;k<6;k++)
					paidFeesFinesArray[j][k] = tmpArray[j][k];

		}

		catch (SQLException e) {
			// preparedStatement(), executeQuery() throw this exception
			System.out.println("Caught SQLException in PaymentBean.readCurrentEscrow()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			returnCode = DATABASE_EXCEPTION;
		}
		finally {

			// allow gc
			tmpArray = null;

			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.readCurrentEscrow().");
			try {
				//rset.close was commented out
				if (rset != null) {  // Close resultSet
					try { rset.close(); } catch (SQLException e) { System.out.println("Caught rset not closed in PaymentBean.readCurrentEscrow()!"); }
					rset = null;
				}
				if (selectStmt != null) {   // Close statement
					try { selectStmt.close(); } catch (SQLException e) { System.out.println("Caught selectStmt not closed in PaymentBean.readCurrentEscrow()!"); }
					selectStmt = null;
				}

				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					try { conn.close(); } catch (SQLException e) { System.out.println("Caught connection not closed in PaymentBean.readCurrentEscrow()!"); }
					conn = null;
				}

				return returnCode;
			}
			catch (SQLException e) {
				// close() throw this exception
				System.out.println("Caught SQLException in PaymentBean.readCurrentEscrow()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// inserts took place
				return returnCode;
			}
		}
	}

	/**
	 * reads payment information from the database for the specific payment
	 * <p>
	 * @param payment number as <code>int</code>
	 * @param case category as <code>String</code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="PaymentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: payment data is read from the database
	 *               with focus on payment distribution to
	 *               process void payment </i>
	 */

	public int readPayment(int paymentNo, String caseCategory)
	{

		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int i = 0;
		String str = "";
		String[][] tmpArray = new String[MAX_SIZE][3];
		String lName = "";
		String mName = "";
		String fName = "";
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet rset = null;

		try {
			conn = getConnection(jndiResource);

			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection

			// clear values in the member variables (reset form)
			//reset();

			// define format used by form
			SimpleDateFormat sdf = new SimpleDateFormat ("MM/dd/yyyy");

			// define currency format
			DecimalFormat df = new DecimalFormat ("#0.00");

			selectStmt = conn.prepareStatement (
					"SELECT value,                  " +
							"PAYBREAK.amount,               " +
							"PAYBREAK.fee_fine_no,          " +
							"FEES_FINES.installments        " +
							"FROM PAYBREAK,                 " +
							"FEES_FINES, CODE_TABLE         " +
							"WHERE payment_no = ?           " +
							"AND PAYBREAK.fee_fine_no = FEES_FINES.fee_fine_no " +
							"AND fee_type = code            " +
							"AND (code_type = ?             " +
							"OR code_type = 'fee_type_misc' " +
					"OR code_type = 'fine_type')");

			paymentNumber = paymentNo;
			selectStmt.setInt(1, paymentNo);

			if ("CRIMINAL".equals(caseCategory))
				selectStmt.setString(2, "fee_type_criminal");

			if (!"CRIMINAL".equals(caseCategory))
				selectStmt.setString(2, "fee_type_civil");

			rset = selectStmt.executeQuery();

			while ( (rset.next()) && (i < MAX_SIZE)) {

				tmpArray[i][0] = rset.getString(1).trim();

				double amt = rset.getDouble(2);
				tmpArray[i][1] = df.format(amt);
				eachPaidAmount += amt;
				tmpArray[i][2] = String.valueOf(rset.getInt(3));

				installmentNumber = rset.getInt(4);
				//System.out.println("Installment Number is "+ installmentNumber);
				i++;
			}

			if (i == 0) {
				System.out.println("SELECT from table PAYBREAK failed nn");
				// if select failed - rollback to
				// release any locks
				needToRollback = true;
				returnCode = OPERATION_FAILED;
			} else {

				// create paybreakArray
				paybreakArray = new String[i][3];

				// copy data from tmp array
				for (int j=0; j<i;j++) {
					paybreakArray[j][0] = tmpArray[j][0];
					paybreakArray[j][1] = tmpArray[j][1];
					paybreakArray[j][2] = tmpArray[j][2];
				}
			}
		}
		catch (SQLException e) {
			// preparedStatement(), executeUpdate() throw this exception
			System.out.println("Caught SQLException in PaymentBean.readPayment()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			returnCode = DATABASE_EXCEPTION;
		}
		finally {

			// allow gc
			tmpArray = null;

			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.readPayment().");
			try {
				//rset.close was commented out
				if (rset != null) {  // Close resultSet
					try { rset.close(); } catch (SQLException e) { System.out.println("Caught rset not closed in PaymentBean.readPayment()!"); }
					rset = null;
				}
				if (selectStmt != null) {   // Close statement
					try { selectStmt.close(); } catch (SQLException e) { System.out.println("Caught selectStmt not closed in PaymentBean.readPayment()!"); }
					selectStmt = null;
				}

				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					try { conn.close(); } catch (SQLException e) { System.out.println("Caught connection not closed in PaymentBean.readPayment()!"); }
					conn = null;
				}

				return returnCode;
			}
			catch (SQLException e) {
				// close() throw this exception
				System.out.println("Caught SQLException in PaymentBean.readPayment()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// inserts took place
				return returnCode;
			}
		}
	}

	/**
	 * reads from the database payment information for
	 * payment receipt to show what is paid and what is outstanding at this point
	 * <p>
	 * @param case ID as <code>int</code>
	 * @param sequence number as <code>int</code>
	 * @param payment number as <code>int</code>
	 * @param case category as <code>String</code> (CIVIL or CRIMINAL)
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="FeeFineBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: fees/fines data is read from the database
	 *               to be presented in receipt fashion </i>
	 */
	public int readPaymentForReceipt(int csId, int seqNo, int payNo, String caseCategory)
	{

		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int i = 0;
		int feeFineNo = 0;
		int install = 0;
		String feeFineTy = "";
		String gpRec = "";
		double totalBalance = 0.0;
		double paymtAmt = 0.0;

		// temp variables to fetch data
		String str = "";
		Timestamp ts = null;

		String tmpArray[][] = new String[MAX_SIZE][4];

		Connection conn = null;
		PreparedStatement selectStmt = null;
		PreparedStatement selectStmt1 = null;
		ResultSet rset = null;
		ResultSet rset1 = null;

		// define format used by form
		SimpleDateFormat sdf = new SimpleDateFormat ("MM/dd/yyyy");

		// define format for currency
		DecimalFormat df = new DecimalFormat ("###0.00");

		// clear values in the member variables (rest form)
		reset();

		try
		{
			conn = getConnection(jndiResource);

			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection

			// retrieve data from FEES_FINES and CODE_TABLE table
			// sorted by date_due
			selectStmt = conn.prepareStatement(
					"SELECT fee_fine_no,      " +
							"amount,        " +
							"group_record,        " +
							"value                " +
							"FROM FEES_FINES, CODE_TABLE    " +
							"WHERE case_id = ?              " +
							"AND sequence_no = ?            " +
							"AND code = fee_type            " +
							"AND fee_group IS NULL          " +
							"AND (FEES_FINES.reference IS NULL         " +
							"OR FEES_FINES.reference = 0)              " +
							"AND (code_type = ?             " +
							"OR code_type = 'fee_type_misc' " +
							"OR code_type = 'fine_type')    " +
					"ORDER BY date_due ASC");

			selectStmt.setInt(1, csId);
			selectStmt.setInt(2, seqNo);

			if ("CRIMINAL".equals(caseCategory))
				selectStmt.setString(3, "fee_type_criminal");

			if (!"CRIMINAL".equals(caseCategory))
				selectStmt.setString(3, "fee_type_civil");

			rset = selectStmt.executeQuery();

			// expect 0 or more rows
			while ( (rset.next()) && (i < MAX_SIZE) )
			{

				str = "";
				ts = null;

				feeFineNo = rset.getInt(1);

				tmpArray[i][0] = df.format(rset.getDouble(2));

				str = rset.getString(3);
				if (str != null)
					gpRec = str.trim();
				else
					gpRec = "";

				str = rset.getString(4);
				if (str != null)
					tmpArray[i][1] = str.trim();
				else
					tmpArray[i][1] = "";

				// check fee/fine type

				// BCC fee
				// (assumption: cannot be paid in installments [yet])
				//System.out.println("The group record is " + gpRec);
				if ( gpRec.equals("1") ) {

					selectStmt1 = conn.prepareStatement (
							"SELECT SUM(amount)      " +
									"FROM PAYBREAK           " +
									"WHERE fee_fine_no IN    " +
									"(SELECT fee_fine_no     " +
									"FROM FEES_FINES         " +
									"WHERE fee_group = 'BCC' " +
									"AND case_id = ?         " +
									"AND sequence_no = ?)    " +
							"AND payment_no = ? ");

					selectStmt1.setInt(1, csId);
					selectStmt1.setInt(2, seqNo);
					selectStmt1.setInt(3, payNo);

					rset1 = selectStmt1.executeQuery();

					if (rset1.next()) {
						tmpArray[i][2] = String.valueOf(rset1.getDouble(1));

					} else {
						System.out.println("Select from PAYBREAK failed.");
						needToRollback = true;
					}

					if (needToRollback == false) {
						//close the statement to reuse
						if (selectStmt1 != null)
							selectStmt1.close();

						selectStmt1 = conn.prepareStatement (
								"SELECT SUM(amount)      " +
										"FROM PAYBREAK           " +
										"WHERE fee_fine_no IN    " +
										"(SELECT fee_fine_no     " +
										"FROM FEES_FINES         " +
										"WHERE fee_group = 'BCC' " +
										"AND case_id = ?         " +
								"AND sequence_no = ?)    " );

						selectStmt1.setInt(1, csId);
						selectStmt1.setInt(2, seqNo);

						rset1 = selectStmt1.executeQuery();

						if (rset1.next()) {
							paymtAmt = rset1.getDouble(1);

						} else {
							System.out.println("Select from PAYBREAK failed.");
							needToRollback = true;
						}
					}

					if (needToRollback == false) {
						//close the statement to reuse
						if (selectStmt1 != null)
							selectStmt1.close();

						selectStmt1 = conn.prepareStatement (
								"SELECT amount             "+
										"FROM FEES_FINES           " +
								"WHERE fee_fine_no = ?");

						selectStmt1.setInt(1, feeFineNo);
					}
				}
				else
				{
					// not BCC fee and not to be paid in installments
					selectStmt1 = conn.prepareStatement (
							"SELECT SUM(PAYBREAK.amount)  " +
									"FROM PAYBREAK, FEES_FINES    " +
									"WHERE FEES_FINES.fee_fine_no = PAYBREAK.fee_fine_no " +
									"AND FEES_FINES.fee_fine_no = ? " +
							"AND PAYBREAK.payment_no = ? ");

					selectStmt1.setInt(1, feeFineNo);
					selectStmt1.setInt(2, payNo);

					rset1 = selectStmt1.executeQuery();

					if (rset1.next())
					{
						tmpArray[i][2] = String.valueOf(rset1.getDouble(1));
					}
					else
					{
						System.out.println("Select from PAYBREAK failed.");
						needToRollback = true;
					}
					if (needToRollback == false)
					{
						if (selectStmt1 != null)
							selectStmt1.close();
						selectStmt1 = conn.prepareStatement (
								"SELECT SUM(PAYBREAK.amount)           " +
										"FROM PAYBREAK, FEES_FINES    " +
										"WHERE FEES_FINES.fee_fine_no = PAYBREAK.fee_fine_no " +
								"AND FEES_FINES.fee_fine_no = ? ");
						selectStmt1.setInt(1, feeFineNo);
						rset1 = selectStmt1.executeQuery();

						if (rset1.next())
						{
							paymtAmt = rset1.getDouble(1);

						}
						else
						{
							System.out.println("Select from PAYBREAK failed.");
							needToRollback = true;
						}
					}
					if (needToRollback == false)
					{
						if (selectStmt1 != null){
							selectStmt1.close();
							selectStmt1 = null;
						}
						selectStmt1 = conn.prepareStatement (
								"SELECT amount             " +
										"FROM FEES_FINES           " +
								"WHERE fee_fine_no = ?");
						selectStmt1.setInt(1, feeFineNo);
					}
				}
				rset1 = selectStmt1.executeQuery();

				rset1.next();
				double feeAmt = rset1.getDouble(1);
				double amt = feeAmt-paymtAmt;

				if (rset1.wasNull())
					tmpArray[i][3]  = tmpArray[i][2] ;
				else
					tmpArray[i][3] = df.format(amt);

				//outstandingBalance += Double.parseDouble(tmpArray[i][4]);

				i++;
			}
			if (i > 0)
			{
				// create feesFinesAllArray
				receiptArray = new String[i][4];
				for (int j=0; j<i; j++)
					for (int k=0; k<4; k++)
						receiptArray[j][k] = tmpArray[j][k];
			}
			else
			{
				System.out.println("Failed to retrieve fees / fines");
				needToRollback = true;
				returnCode = OPERATION_FAILED;
			}
		}
		catch (SQLException e)
		{
			//used to say "Caught SQLException in FeeFineBean.ReadAllFeesFines - Luz
			System.out.println("Caught SQLException in PaymentBean.readPaymentForReceipt()!");
			System.out.println(e);
			e.printStackTrace();
			isDataSubmitted = false;
			needToRollback = true;
			returnCode = DATABASE_EXCEPTION;
		}
		finally
		{
			tmpArray = null;
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.readPaymentForReceipt().");
			try
			{
				//rset.close and rset1.close were both commented out
				if (rset != null) {  // Close rset
					try { rset.close(); } catch (SQLException e) { System.out.println("Caught rset not closed in PaymentBean.readPaymentForReceipt()!"); }
					rset = null;
				}
				if (selectStmt != null) {   // Close statement
					try { selectStmt.close(); } catch (SQLException e) { System.out.println("Caught selectStmt not closed in PaymentBean.readPaymentForReceipt()!"); }
					selectStmt = null;
				}

				if (rset1 != null) {  // Close rset
					try { rset1.close(); } catch (SQLException e) { System.out.println("Caught rset1 not closed in PaymentBean.readPaymentForReceipt()!"); }
					rset = null;
				}
				if (selectStmt1 != null) {   // Close statement
					try { selectStmt1.close(); } catch (SQLException e) { System.out.println("Caught selectStmt1 not closed in PaymentBean.readPaymentForReceipt()!"); }
					selectStmt = null;
				}

				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					try { conn.close(); } catch (SQLException e) { System.out.println("Caught connection not closed in PaymentBean.readPaymentForReceipt()!"); }
					conn = null;
				}
				return returnCode;
			}
			catch (SQLException e)
			{
				System.out.println("Caught SQLException in PaymentBean.readPaymentForReceipt()!");
				System.out.println(e);
				return returnCode;
			}
		}
	}

	/**
	 * processes and stores refund information in the database
	 * <p>
	 * @param HTTP request as <code>HttpServletRequest</code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="PaymentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: data is stored in the database </i>
	 */

	public int processRefund(String partyTo, String caseCategory, String form, int escrowNo, String escrowAmount, HttpServletRequest request)
	{
		methodName = "processRefund(String partyTo, String caseCategory, String form, int escrowNo, String escrowAmount, HttpServletRequest request)";
		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int rowsInserted = 0;
		int rowsUpdated = 0;
		int i = 0;
		HttpSession session = null;
		Map paramMap = null;
		Iterator iter = null;
		Map.Entry me = null;
		String tmp[] = null;
		String feeInstNo = "";
		String feeGroup = "";
		String feeType = "";
		String feeNo = "";
		int installmentNo = 0;
		String str = "";
		String category = "";
		String refundAccNo = "";
		int feeFineNumber = 0;

		if (!"CRIMINAL".equals(caseCategory))
			category = "V";
		if ("CRIMINAL".equals(caseCategory))
			category = "C";

		Connection conn = null;
		PreparedStatement insertStmt = null;
		PreparedStatement selectStmt = null;
		PreparedStatement updateStmt = null;
		ResultSet rset = null;

		String ipAdd = request.getRemoteAddr();
		rollbackDone rbd = null;
		try
		{
			conn = getConnection(jndiResource);

			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection

			session = request.getSession(false);
			caseId = Integer.parseInt((String)session.getAttribute("case_id"));
			sequenceNumber = Integer.parseInt((String)session.getAttribute("seq_no"));
			java.util.Date dt = new java.util.Date();
			userName = (String) session.getAttribute("user_name");

			SimpleDateFormat sdf1 = new SimpleDateFormat ("MM/dd/yyyy");
			SimpleDateFormat sdf2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

			// get the account number from the FEE_SCHEDULE
			selectStmt = conn.prepareStatement(
					"SELECT FEE_SCHEDULE.account_no        " +
							"FROM FEE_SCHEDULE                     " +
							"WHERE fee_group Is Null               " +
							"AND fee_type = " + "'" + "REFUND" + "'");

			rset = selectStmt.executeQuery();

			if (!(rset.next()))
			{
				// problem with retrieving account_no
				System.out.println("Failed to read account_no");
				needToRollback = true;
				returnCode = OPERATION_FAILED;
			}
			else
			{
				refundAccNo = rset.getString(1).trim();
				System.out.println("The account_no is= " + refundAccNo);
			}

			// if no problems reported (needToRollback == false)
			// continue with inserting to PAYMENTS
			if (needToRollback == false)
			{
				rowsInserted = 0;

				// insert row in PAYMENTS
				sqlString = "INSERT INTO PAYMENTS ( " +
						"case_id,               " +
						"sequence_no,           " +
						"amount,                " +
						"payment_date,          " +
						"method,                " +
						"paid_by,               " +
						"description,           " +
						"last_updated_by,       " +
						"last_updated_datetime, " +
						"last_updated_from)     " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				insertStmt = conn.prepareStatement(sqlString);

				insertStmt.setInt(1, caseId);
				insertStmt.setInt(2, sequenceNumber);
				insertStmt.setDouble(3, - Double.parseDouble(amount));
				insertStmt.setTimestamp(4, Timestamp.valueOf(sdf2.format(sdf1.parse(refundDate))));
				insertStmt.setString(5, "REFUND");
				insertStmt.setString(6, refundBy);
				insertStmt.setString(7, description);
				insertStmt.setString(8, (String)session.getAttribute("user_name"));
				insertStmt.setTimestamp(9, Timestamp.valueOf(sdf2.format(dt)));
				insertStmt.setString(10, ipAdd);

				rowsInserted = insertStmt.executeUpdate();
				if (rowsInserted != 1)
				{
					System.out.println("Insert into table PAYMENTS failed");
					// if insert failed - rollback to
					// release any locks
					needToRollback = true;
					rbd = new rollbackDone(userName, methodName, sqlString);
					returnCode = OPERATION_FAILED;
				}
				else
				{
					// insert to PAYMENTS successful
					System.out.println("Insert into table PAYMENTS successful");

					// close statement to be reused
					if (selectStmt != null){
						selectStmt.close();
						selectStmt = null;
					}
					// retrieve payment_no for newly inserted payment
					selectStmt = conn.prepareStatement(
							"SELECT MAX(payment_no)   " +
									"FROM PAYMENTS            " +
									"WHERE case_id = ?        " +
									"AND sequence_no = ?      " +
							"AND last_updated_by = ?" );

					selectStmt.setInt(1, caseId);
					selectStmt.setInt(2, sequenceNumber);
					selectStmt.setString(3, (String)session.getAttribute("user_name"));

					rset = selectStmt.executeQuery();

					if (rset.next()) {
						paymentNumber = rset.getInt(1);
						System.out.println("Retrieved payment_no = " + paymentNumber);

						// retrieve dynamically created parameterts
						paramMap = request.getParameterMap();
						iter = (paramMap.entrySet()).iterator();

						//close insert statement to be reused
						insertStmt.close();
						insertStmt = null;

						if (selectStmt != null)
							selectStmt.close();
						sqlString = "INSERT INTO PAYBREAK (  " +
								"fee_fine_no,            " +
								"payment_no,             " +
								"amount,                 " +
								"account_no,             " +
								"last_updated_by,        " +
								"last_updated_datetime,  " +
								"last_updated_from)      " +
								"VALUES (?, ?, ?, ?, ?, ?, ?)";
						insertStmt = conn.prepareStatement(sqlString);

						//System.out.println("Test after the insert statement in PAYBREAK");
						i = 0;
						while (iter.hasNext())
						{
							me = (Map.Entry)iter.next();
							String param = (String)me.getKey();
							String value[] = (String[])me.getValue();
							rowsInserted = 0;
							double instAmt = 0.0;

							// if request parameter  has feeXXX format (XXX is fee_fine_no)
							// and value for this parameter != ""

							if ( ("fee".equals(param.substring(0,3))) && (!"".equals(value[0])) )
							{
								feeInstNo = param.substring(3);
								StringTokenizer st = new StringTokenizer(feeInstNo, "X");
								try
								{
									feeNo = st.nextToken();
									//System.out.println("fee number is "+ feeNo);
									installmentNo = Integer.parseInt(st.nextToken());
									//System.out.println("installment number is "+ installmentNo);
								}
								catch (NoSuchElementException e)
								{
									System.out.println ("Caught NoSuchElementException in PaymentBean.processRefund()!");
									System.out.println(e);
								} catch (NumberFormatException e) {
									System.out.println ("Caught NumberFormatException in PaymentBean.processRefund()!");
									System.out.println(e);
								}
								selectStmt = conn.prepareStatement(
										"SELECT account_no                " +
												"FROM FEES_FINES    " +
										"WHERE FEES_FINES.fee_fine_no = ? " );

								selectStmt.setInt(1, Integer.parseInt(feeNo));

								rset = selectStmt.executeQuery();

								if (!(rset.next()))
								{
									// problem with retrieving account_no
									System.out.println("Failed to read account_no");
									needToRollback = true;
									returnCode = OPERATION_FAILED;
								}
								else
								{

									str = rset.getString(1);
									if (str != null)
										accountNo = str.trim();
									else
										accountNo = "";
								}

								System.out.println("The account_no is= " + accountNo);

								insertStmt.setInt(1, Integer.parseInt(feeNo));
								insertStmt.setInt(2, paymentNumber);
								insertStmt.setDouble(3, - Double.parseDouble(value[0]));
								insertStmt.setString(4, accountNo);
								insertStmt.setString(5, (String)session.getAttribute("user_name"));
								insertStmt.setTimestamp(6, Timestamp.valueOf(sdf2.format(dt)));
								insertStmt.setString(7, ipAdd);

								rowsInserted = insertStmt.executeUpdate();

								if (rowsInserted != 1)
								{
									System.out.println("Insert into table PAYBREAK failed");
									returnCode = OPERATION_FAILED;

									// leave while loop
									break;
								}
								if (installmentNo > 0)
								{
									// close statement to be reused
									if (selectStmt != null){
										selectStmt.close();
										selectStmt = null;
									}

									int chNo = 0;
									double payAmt = 0.0;
									double refundedAmt = Double.parseDouble(value[0]);
									//System.out.println("Refunded amount is "+ refundedAmt);

									selectStmt = conn.prepareStatement(
											"SELECT charge_no,                     " +
													"       payment_amount                 " +
													"FROM INSTALLMENT_CHARGE               " +
													"WHERE INSTALLMENT_CHARGE.installment_no = ? " +
													"AND payment_amount <> 0 " +
											"ORDER BY charge_no DESC ");

									selectStmt.setInt(1, installmentNo);

									rset = selectStmt.executeQuery();

									while (rset.next())
									{
										chNo = rset.getInt(1);
										payAmt = rset.getDouble(2);

										//System.out.println("Refunded amount in while is "+ refundedAmt);
										//Update INSTALLMENT_CHARGE for the payment amount
										// perform update
										updateStmt = conn.prepareStatement(
												"UPDATE INSTALLMENT_CHARGE            " +
														"SET payment_amount = ?,              " +
														"    payment_no = ?,                  " +
														"    last_updated_datetime = ?,       " +
														"    last_updated_by = ?,             " +
														"    last_updated_from = ?            " +
												"WHERE charge_no = ?");

										// set parameters for update statement
										if (payAmt >= refundedAmt)
										{
											updateStmt.setDouble(1, payAmt-refundedAmt);
											updateStmt.setInt(2, 0);
											updateStmt.setTimestamp(3, Timestamp.valueOf(sdf2.format(dt)));
											updateStmt.setString(4, (String)session.getAttribute("user_name"));
											updateStmt.setString(5, ipAdd);
											updateStmt.setInt(6, chNo);

											rowsUpdated = updateStmt.executeUpdate();

											break;
										}
										else
										{
											updateStmt.setDouble(1, 0);
											refundedAmt -= payAmt;
											updateStmt.setInt(2, 0);
											updateStmt.setTimestamp(3, Timestamp.valueOf(sdf2.format(dt)));
											updateStmt.setString(4, (String)session.getAttribute("user_name"));
											updateStmt.setString(5, ipAdd);
											updateStmt.setInt(6, chNo);

											rowsUpdated = updateStmt.executeUpdate();
										}
										if (rowsUpdated != 1)
										{
											System.out.println("Update in table INSTALLMENT_CHARGE failed");
											// if update failed - rollback to
											// release any locks
											needToRollback = true;
											returnCode = OPERATION_FAILED;
										}
										else
										{
											System.out.println("Update in table INSTALLMENT_CHARGE successful");
										}
									}
								}
							}
						}
					}
					else
					{
						// problem with retrieving payment_no
						System.out.println("Failed to retrieve payment_no");
						needToRollback = true;
						returnCode = OPERATION_FAILED;
					}
				}
			}

			// if no problems reported (needToRollback == false)
			// continue with inserting to REFUNDS
			if (needToRollback == false)
			{
				if (insertStmt != null){
					insertStmt.close();
					insertStmt = null;
				}

				rowsInserted = 0;
				sqlString = "INSERT INTO REFUNDS ( " +
						"party_no,             " +
						"case_id,              " +
						"sequence_no,          " +
						"payment_no,           " +
						"amount,               " +
						"account_no,           " +
						"refund_datetime,      " +
						"refund_by,            " +
						"last_updated_by,      " +
						"last_updated_datetime, " +
						"last_updated_from)     " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				insertStmt = conn.prepareStatement(sqlString);

				insertStmt.setInt(1, Integer.parseInt(partyTo));
				insertStmt.setInt(2, caseId);
				insertStmt.setInt(3, sequenceNumber);
				//insertStmt.setInt(4, feeFineNumber);
				insertStmt.setInt(4, paymentNumber);
				insertStmt.setDouble(5, Double.parseDouble(amount));
				insertStmt.setString(6, refundAccNo);
				insertStmt.setTimestamp(7, Timestamp.valueOf(sdf2.format(sdf1.parse(refundDate))));
				insertStmt.setString(8, refundBy);
				insertStmt.setString(9, (String)session.getAttribute("user_name"));
				insertStmt.setTimestamp(10, Timestamp.valueOf(sdf2.format(dt)));
				insertStmt.setString(11, ipAdd);

				rowsInserted = insertStmt.executeUpdate();
				if (rowsInserted != 1)
				{
					System.out.println("Insert into table REFUNDS failed");
					returnCode = OPERATION_FAILED;
					needToRollback = true;
					rbd = new rollbackDone(userName, methodName, sqlString);
				}
			}
			//System.out.println("Form is " + form);
			if (form.equals("ESCROW"))
			{
				//System.out.println("Test in Escrow update for refund");
				// if no problems reported (needToRollback == false)
				// continue with inserting to REFUNDS
				if (needToRollback == false) {

					// close statement to be reused
					if (updateStmt != null){
						updateStmt.close();
						updateStmt = null;
					}

					rowsUpdated = 0;

					// update escrow amount in ESCROW table
					String tempStr = "UPDATE ESCROW                  " +
							"SET current_balance = ?,       " +
							"    date_of_escrow = ?,        ";
					if (dbType.equalsIgnoreCase("MYSQL")) {
						tempStr += "description = concat(description, ',', ?), ";
					} else {
						tempStr += "description = description & ',' & ?, ";
					}

					tempStr += "    last_updated_datetime = ?, " +
							"    last_updated_by = ?,       " +
							"    last_updated_from = ?      " +
							"WHERE escrow_no = " + escrowNo;
					//System.out.println("tempStr = " + tempStr);

					updateStmt = conn.prepareStatement(tempStr);

					//System.out.println("the escrowAmount variable is ****************"+escrowAmount);
					updateStmt.setDouble(1, Double.parseDouble(escrowAmount)-Double.parseDouble(amount));

					updateStmt.setTimestamp(2, Timestamp.valueOf(sdf2.format(sdf1.parse(refundDate))));
					updateStmt.setString(3, amount + " refunded from Escrow");
					updateStmt.setTimestamp(4, Timestamp.valueOf(sdf2.format(dt)));
					updateStmt.setString(5, (String)session.getAttribute("user_name"));
					updateStmt.setString(6, ipAdd);

					rowsUpdated = updateStmt.executeUpdate();

					if (rowsUpdated != 1)
					{
						System.out.println("Update in table ESCROW failed");
						// if update failed - rollback to
						// release any locks
						needToRollback = true;
						returnCode = OPERATION_FAILED;
					}
				}
			}
		}
		catch (NumberFormatException e)
		{
			// parseDouble() throws this exception
			System.out.println("Caught NumberFormatException in PaymentBean.processRefund()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "NumberFormatException caught");
			returnCode = DATABASE_EXCEPTION;
		}
		catch (ParseException e)
		{
			// parse() throws this exception
			System.out.println("Caught ParseException in PaymentBean.processRefund()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "ParseException caught");
			returnCode = DATABASE_EXCEPTION;
		}
		catch (SQLException e)
		{
			// preparedStatement(), executeUpdate() throw this exception
			System.out.println("Caught SQLException in PaymentBean.processRefund()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "SQLException caught");
			returnCode = DATABASE_EXCEPTION;
		}
		finally
		{
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.processRefund().");
			try
			{
				if (selectStmt != null){
					selectStmt.close();
					selectStmt = null;
				}
				if (insertStmt != null){
					insertStmt.close();
					insertStmt = null;
				}
				if (updateStmt != null){
					updateStmt.close();
					updateStmt = null;
				}
				if ((conn != null) && (!conn.isClosed()))
				{
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					conn.close();
					conn = null;
				}
				return returnCode;
			}
			catch (SQLException e)
			{
				System.out.println("Caught SQLException in PaymentBean.processRefund()!");
				System.out.println(e);
				return returnCode;
			}
		}
	}

	/**
	 * processes and stores refund information in the database
	 * <p>
	 * @param HTTP request as <code>HttpServletRequest</code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="PaymentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: data is stored in the database </i>
	 */
	public int processEscrowRefund(String partyTo, String caseCategory, String form, int escrowNo, String escrowAmount, HttpServletRequest request)
	{
		methodName = "processEscrowRefund(String partyTo, String caseCategory, String form, int escrowNo, String escrowAmount, HttpServletRequest request)";
		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int rowsInserted = 0;
		int rowsUpdated = 0;
		int i = 0;
		HttpSession session = null;
		Map paramMap = null;
		Iterator iter = null;
		Map.Entry me = null;
		String tmp[] = null;
		String feeInstNo = "";
		String feeGroup = "";
		String feeType = "";
		String feeNo = "";
		int installmentNo = 0;
		String str = "";
		String category = "";
		String refundAccNo = "";
		int feeFineNumber = 0;

		if (!"CRIMINAL".equals(caseCategory))
			category = "V";
		if ("CRIMINAL".equals(caseCategory))
			category = "C";

		Connection conn = null;
		PreparedStatement insertStmt = null;
		PreparedStatement selectStmt = null;
		PreparedStatement updateStmt = null;
		ResultSet rset = null;

		String ipAdd = request.getRemoteAddr();
		rollbackDone rbd = null;

		try {
			conn = getConnection(jndiResource);

			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection


			// get current session based on HttpServletRequest
			// and don't create a new on (create=false)
			session = request.getSession(false);

			// get case_id and sequence_no from session object
			caseId = Integer.parseInt((String)session.getAttribute("case_id"));
			sequenceNumber = Integer.parseInt((String)session.getAttribute("seq_no"));

			userName = (String)session.getAttribute("user_name");
			// capture current date/time and format it
			java.util.Date dt = new java.util.Date();

			// define format used by form
			SimpleDateFormat sdf1 = new SimpleDateFormat ("MM/dd/yyyy");
			// define format for JDBC Timestamp
			SimpleDateFormat sdf2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

			// get the account number from the FEE_SCHEDULE
			selectStmt = conn.prepareStatement(
					"SELECT FEE_SCHEDULE.account_no        " +
							"FROM FEE_SCHEDULE                     " +
							"WHERE fee_group Is Null               " +
							"AND fee_type = " + "'" + "REFUND" + "'");

			rset = selectStmt.executeQuery();

			if (!(rset.next())) {
				// problem with retrieving account_no
				System.out.println("Failed to read account_no");
				needToRollback = true;
				returnCode = OPERATION_FAILED;
			} else {
				refundAccNo = rset.getString(1).trim();

				//System.out.println("The account_no is= " + refundAccNo);
			}

			// if no problems reported (needToRollback == false)
			// continue with inserting to PAYMENTS
			if (needToRollback == false) {

				rowsInserted = 0;

				// insert row in PAYMENTS
				sqlString = "INSERT INTO PAYMENTS ( " +
						"case_id,               " +
						"sequence_no,           " +
						"amount,                " +
						"payment_date,          " +
						"method,                " +
						"paid_by,               " +
						"description,           " +
						"last_updated_by,       " +
						"last_updated_datetime, " +
						"last_updated_from)     " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				insertStmt = conn.prepareStatement(sqlString);

				insertStmt.setInt(1, caseId);
				insertStmt.setInt(2, sequenceNumber);
				insertStmt.setDouble(3, - Double.parseDouble(amount));
				insertStmt.setTimestamp(4, Timestamp.valueOf(sdf2.format(sdf1.parse(refundDate))));
				insertStmt.setString(5, "REFUND from Escrow");
				insertStmt.setString(6, refundBy);
				insertStmt.setString(7, description);
				insertStmt.setString(8, (String)session.getAttribute("user_name"));
				insertStmt.setTimestamp(9, Timestamp.valueOf(sdf2.format(dt)));
				insertStmt.setString(10, ipAdd);


				rowsInserted = insertStmt.executeUpdate();

				if (rowsInserted != 1) {
					System.out.println("Insert into table PAYMENTS failed");
					// if insert failed - rollback to
					// release any locks
					needToRollback = true;
					rbd = new rollbackDone(userName, methodName, sqlString);
					returnCode = OPERATION_FAILED;
				} else {
					// insert to PAYMENTS successful
					//System.out.println("Insert into table PAYMENTS successful");

					// close statement to be reused
					if (selectStmt != null)
						selectStmt.close();

					// retrieve payment_no for newly inserted payment
					selectStmt = conn.prepareStatement(
							"SELECT MAX(payment_no)   " +
									"FROM PAYMENTS            " +
									"WHERE case_id = ?        " +
									"AND sequence_no = ?      " +
							"AND last_updated_by = ?" );

					selectStmt.setInt(1, caseId);
					selectStmt.setInt(2, sequenceNumber);
					selectStmt.setString(3, (String)session.getAttribute("user_name"));

					rset = selectStmt.executeQuery();

					if (rset.next()) {
						paymentNumber = rset.getInt(1);
						//System.out.println("Retrieved payment_no = " + paymentNumber);

						// retrieve dynamically created parameterts
						paramMap = request.getParameterMap();
						iter = (paramMap.entrySet()).iterator();

						//close insert statement to be reused
						insertStmt.close();
						insertStmt = null;

						if (selectStmt != null){
							selectStmt.close();
							selectStmt = null;
						}
						sqlString = "INSERT INTO PAYBREAK (  " +
								"fee_fine_no,            " +
								"payment_no,             " +
								"amount,                 " +
								"account_no,             " +
								"last_updated_by,        " +
								"last_updated_datetime,  " +
								"last_updated_from)      " +
								"VALUES (?, ?, ?, ?, ?, ?, ?)";
						insertStmt = conn.prepareStatement(sqlString);

						i = 0;
						while (iter.hasNext()) {
							me = (Map.Entry)iter.next();
							String param = (String)me.getKey();
							String value[] = (String[])me.getValue();
							rowsInserted = 0;
							double instAmt = 0.0;

							//System.out.println("Test after the while");
							// if request parameter  has feeXXX format (XXX is fee_fine_no)
							// and value for this parameter != ""

							if ( ("fee".equals(param.substring(0,3))) && (!"".equals(value[0])) ) {
								feeInstNo = param.substring(3);
								StringTokenizer st = new StringTokenizer(feeInstNo, "X");

								try {
									feeNo = st.nextToken();
									//System.out.println("fee number is "+ feeNo);
									installmentNo = Integer.parseInt(st.nextToken());
									//System.out.println("installment number is "+ installmentNo);
								} catch (NoSuchElementException e) {
									System.out.println ("Caught NoSuchElementException in PaymentBean.processRefund()!");
									System.out.println(e);
								} catch (NumberFormatException e) {
									System.out.println ("Caught NumberFormatException in PaymentBean.processRefund()!");
									System.out.println(e);
								}

								selectStmt = conn.prepareStatement(
										"SELECT account_no                " +
												"FROM FEES_FINES    " +
										"WHERE FEES_FINES.fee_fine_no = ? " );

								selectStmt.setInt(1, Integer.parseInt(feeNo));

								rset = selectStmt.executeQuery();

								if (!(rset.next())) {
									// problem with retrieving account_no
									System.out.println("Failed to read account_no");
									needToRollback = true;
									returnCode = OPERATION_FAILED;
								} else {

									str = rset.getString(1);
									if (str != null)
										accountNo = str.trim();
									else
										accountNo = " ";
								}

								//System.out.println("The account_no is= " + accountNo);

								insertStmt.setInt(1, Integer.parseInt(feeNo));
								insertStmt.setInt(2, paymentNumber);
								insertStmt.setDouble(3, - Double.parseDouble(value[0]));
								insertStmt.setString(4, accountNo);
								insertStmt.setString(5, (String)session.getAttribute("user_name"));
								insertStmt.setTimestamp(6, Timestamp.valueOf(sdf2.format(dt)));
								insertStmt.setString(7, ipAdd);

								rowsInserted = insertStmt.executeUpdate();

								if (rowsInserted != 1) {
									System.out.println("Insert into table PAYBREAK failed");
									returnCode = OPERATION_FAILED;

									// leave while loop
									break;
								}

								if (installmentNo > 0) {
									// close statement to be reused
									if (selectStmt != null)
										selectStmt.close();

									int chNo = 0;
									double payAmt = 0.0;
									double refundedAmt = Double.parseDouble(value[0]);
									//System.out.println("Refunded amount is "+ refundedAmt);

									selectStmt = conn.prepareStatement(
											"SELECT charge_no,                     " +
													"       payment_amount                 " +
													"FROM INSTALLMENT_CHARGE               " +
													"WHERE INSTALLMENT_CHARGE.installment_no = ? " +
													"AND payment_amount <> 0 " +
											"ORDER BY charge_no DESC ");

									selectStmt.setInt(1, installmentNo);

									rset = selectStmt.executeQuery();

									while (rset.next()) {

										chNo = rset.getInt(1);
										payAmt = rset.getDouble(2);

										//System.out.println("Refunded amount in while is "+ refundedAmt);
										//Update INSTALLMENT_CHARGE for the payment amount
										// perform update
										updateStmt = conn.prepareStatement(
												"UPDATE INSTALLMENT_CHARGE            " +
														"SET payment_amount = ?,              " +
														"    payment_no = ?,                  " +
														"    last_updated_datetime = ?,       " +
														"    last_updated_by = ?,             " +
														"    last_updated_from = ?            " +
												"WHERE charge_no = ?");

										// set parameters for update statement
										if (payAmt >= refundedAmt) {
											updateStmt.setDouble(1, payAmt-refundedAmt);
											updateStmt.setInt(2, 0);
											updateStmt.setTimestamp(3, Timestamp.valueOf(sdf2.format(dt)));
											updateStmt.setString(4, (String)session.getAttribute("user_name"));
											updateStmt.setString(5, ipAdd);
											updateStmt.setInt(6, chNo);

											rowsUpdated = updateStmt.executeUpdate();

											break;
										} else {
											updateStmt.setDouble(1, 0);
											refundedAmt -= payAmt;
											updateStmt.setInt(2, 0);
											updateStmt.setTimestamp(3, Timestamp.valueOf(sdf2.format(dt)));
											updateStmt.setString(4, (String)session.getAttribute("user_name"));
											updateStmt.setString(5, ipAdd);
											updateStmt.setInt(6, chNo);

											rowsUpdated = updateStmt.executeUpdate();
										}
										if (rowsUpdated != 1) {
											System.out.println("Update in table INSTALLMENT_CHARGE failed");
											// if update failed - rollback to
											// release any locks
											needToRollback = true;
											returnCode = OPERATION_FAILED;
										} else {
											System.out.println("Update in table INSTALLMENT_CHARGE successful");

										}
									}

								}
							}
						}

					} else {
						// problem with retrieving payment_no
						System.out.println("Failed to retrieve payment_no");
						needToRollback = true;
						returnCode = OPERATION_FAILED;
					}

				}
			}

			// if no problems reported (needToRollback == false)
			// continue with inserting to REFUNDS
			if (needToRollback == false) {

				// close statement to be reused
				if (insertStmt != null)
					insertStmt.close();
				//if (selectStmt != null)
				//selectStmt.close();

				rowsInserted = 0;

				// insert row in PAYMENTS
				sqlString = "INSERT INTO REFUNDS ( " +
						"party_no,             " +
						"case_id,              " +
						"sequence_no,          " +
						"payment_no,           " +
						"amount,               " +
						"account_no,           " +
						"refund_datetime,      " +
						"refund_by,            " +
						"last_updated_by,      " +
						"last_updated_datetime, " +
						"last_updated_from)     " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				insertStmt = conn.prepareStatement(sqlString);

				insertStmt.setInt(1, Integer.parseInt(partyTo));
				insertStmt.setInt(2, caseId);
				insertStmt.setInt(3, sequenceNumber);
				//insertStmt.setInt(4, feeFineNumber);
				insertStmt.setInt(4, paymentNumber);
				insertStmt.setDouble(5, Double.parseDouble(amount));
				insertStmt.setString(6, refundAccNo);
				insertStmt.setTimestamp(7, Timestamp.valueOf(sdf2.format(sdf1.parse(refundDate))));
				insertStmt.setString(8, refundBy);
				insertStmt.setString(9, (String)session.getAttribute("user_name"));
				insertStmt.setTimestamp(10, Timestamp.valueOf(sdf2.format(dt)));
				insertStmt.setString(11, ipAdd);

				rowsInserted = insertStmt.executeUpdate();

				if (rowsInserted != 1) {
					System.out.println("Insert into table REFUNDS failed");
					returnCode = OPERATION_FAILED;
					needToRollback = true;
					rbd = new rollbackDone(userName, methodName, sqlString);
				}
			}

			if (form.equals("ESCROW")) {
				// if no problems reported (needToRollback == false)
				// continue with inserting to REFUNDS
				if (needToRollback == false) {

					// close statement to be reused
					if (updateStmt != null)
						updateStmt.close();

					rowsUpdated = 0;

					// update escrow amount in ESCROW table

					updateStmt = conn.prepareStatement(
							"UPDATE ESCROW                  " +
									"SET current_balance = ?,       " +
									"    date_of_escrow = ?,        " +
									"    description = description & ',' & ?, " +
									"    last_updated_datetime = ?, " +
									"    last_updated_by = ?,        " +
									"    last_updated_from = ?       " +
									"WHERE escrow_no = " + escrowNo );


					updateStmt.setDouble(1, Double.parseDouble(escrowAmount)-Double.parseDouble(amount));

					updateStmt.setTimestamp(2, Timestamp.valueOf(sdf2.format(sdf1.parse(refundDate))));
					updateStmt.setString(3, amount + " refunded from Escrow");
					updateStmt.setTimestamp(4, Timestamp.valueOf(sdf2.format(dt)));
					updateStmt.setString(5, (String)session.getAttribute("user_name"));
					updateStmt.setString(6, ipAdd);

					rowsUpdated = updateStmt.executeUpdate();

					if (rowsUpdated != 1) {
						System.out.println("Update in table ESCROW failed");
						// if update failed - rollback to
						// release any locks
						needToRollback = true;
						returnCode = OPERATION_FAILED;
					}
				}
			}
		}

		catch (NumberFormatException e) {
			// parseDouble() throws this exception
			System.out.println("Caught NumberFormatException in PaymentBean.processRefund()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "NumberFormatException caught");
			returnCode = DATABASE_EXCEPTION;
		}

		catch (ParseException e) {
			// parse() throws this exception
			System.out.println("Caught ParseException in PaymentBean.processRefund()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "ParseException caught");
			returnCode = DATABASE_EXCEPTION;
		}
		catch (SQLException e) {
			// preparedStatement(), executeUpdate() throw this exception
			System.out.println("Caught SQLException in PaymentBean.processRefund()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "SQLException caught");
			returnCode = DATABASE_EXCEPTION;
		}

		finally {
			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.processRefund().");
			try {

				//        if (rset != null)    // Close statements
				//          rset.close();

				if (selectStmt != null)
					selectStmt.close();

				if (insertStmt != null)
					insertStmt.close();

				if (updateStmt != null)
					updateStmt.close();

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
				System.out.println("Caught SQLException in PaymentBean.processRefund()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// inserts took place
				return returnCode;
			}
		}
	}

	/**
	 * processes and stores void information in the database
	 * <p>
	 * @param HTTP request as <code>HttpServletRequest</code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="PaymentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: data is stored in the database </i>
	 */

	public int processVoid(String partyTo, String caseCategory, int installmentNo, int payNo, HttpServletRequest request, String paymentMethod)
	{
		methodName = "processVoid(String partyTo, String caseCategory, int installmentNo, HttpServletRequest request)";
		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int rowsInserted = 0;
		int rowsUpdated = 0;
		HttpSession session = null;
		Map paramMap = null;
		Iterator iter = null;
		Map.Entry me = null;
		String tmp[] = null;
		String feeGroup = "";
		String feeType = "";
		String feeNo = "";
		String str = "";
		String category = "";
		String refundAccNo = "";
		String escrowAccNo = "";
		int feeFineNumber = 0;
		int paymentNumber2 = 0;
		int oldPaymentNumber = 0;
		double escrowAmount = 0;
		int escrowNo = 0;

		if (!"CRIMINAL".equals(caseCategory))
			category = "V";
		if ("CRIMINAL".equals(caseCategory))
			category = "C";

		Connection conn = getConnection(jndiResource);

		if (conn == null)
			return NO_DATABASE_CONNECTION; // no connection

		PreparedStatement insertStmt = null;
		PreparedStatement selectStmt = null;
		PreparedStatement updateStmt = null;
		ResultSet rset = null;

		String ipAdd = request.getRemoteAddr();
		rollbackDone rbd = null;
		try
		{
			// get current session based on HttpServletRequest
			// and don't create a new on (create=false)
			session = request.getSession(false);

			// get case_id and sequence_no from session object
			caseId = Integer.parseInt((String)session.getAttribute("case_id"));
			sequenceNumber = Integer.parseInt((String)session.getAttribute("seq_no"));

			userName = (String)session.getAttribute("user_name");
			// capture current date/time and format it
			java.util.Date dt = new java.util.Date();

			// define format used by form
			SimpleDateFormat sdf1 = new SimpleDateFormat ("MM/dd/yyyy");
			// define format for JDBC Timestamp
			SimpleDateFormat sdf2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");


			// get the account number from the FEE_SCHEDULE
			selectStmt = conn.prepareStatement(
					"SELECT FEE_SCHEDULE.account_no        " +
							"FROM FEE_SCHEDULE                     " +
							"WHERE fee_group Is Null               " +
					"AND fee_type = 'REFUND'");
			rset = selectStmt.executeQuery();
			if (!(rset.next()))
			{
				// problem with retrieving account_no
				System.out.println("Failed to read account_no");
				needToRollback = true;
				returnCode = OPERATION_FAILED;
			}
			else
			{
				refundAccNo = rset.getString(1).trim();

				System.out.println("************************The account_no is= '" + refundAccNo+"'");
			}
			// if no problems reported (needToRollback == false)
			// continue with inserting to PAYMENTS
			if (needToRollback == false)
			{
				rowsInserted = 0;
				sqlString = "INSERT INTO PAYMENTS ( " +
						"case_id,               " +
						"sequence_no,           " +
						"amount,                " +
						"payment_date,          " +
						"method,                " +
						"paid_by,               " +
						"description,           " +
						"voided_by,			  " +
						"last_updated_by,       " +
						"last_updated_datetime, " +
						"last_updated_from)     " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				insertStmt = conn.prepareStatement(sqlString);

				insertStmt.setInt(1, caseId);
				insertStmt.setInt(2, sequenceNumber);
				insertStmt.setDouble(3, - Double.parseDouble(amount));
				insertStmt.setTimestamp(4, Timestamp.valueOf(sdf2.format(sdf1.parse(refundDate))));
				insertStmt.setString(5, paymentMethod);
				insertStmt.setString(6, paidBy);
				insertStmt.setString(7, description);
				insertStmt.setInt(8, payNo);
				insertStmt.setString(9, (String)session.getAttribute("user_name"));
				insertStmt.setTimestamp(10, Timestamp.valueOf(sdf2.format(dt)));
				insertStmt.setString(11, ipAdd);

				rowsInserted = insertStmt.executeUpdate();
				if (rowsInserted != 1)
				{
					System.out.println("Insert into table PAYMENTS failed");
					// if insert failed - rollback to
					// release any locks
					needToRollback = true;
					rbd = new rollbackDone(userName, methodName, sqlString);
					returnCode = OPERATION_FAILED;
				}
				else
				{
					// insert to PAYMENTS successful
					System.out.println("Insert into table PAYMENTS successful");

					// close statement to be reused
					if (selectStmt != null)
						selectStmt.close();

					// retrieve payment_no for newly inserted payment
					selectStmt = conn.prepareStatement(
							"SELECT MAX(payment_no)   " +
									"FROM PAYMENTS            " +
									"WHERE case_id = ?        " +
									"AND sequence_no = ?      " +
							"AND last_updated_by = ?" );

					selectStmt.setInt(1, caseId);
					selectStmt.setInt(2, sequenceNumber);
					selectStmt.setString(3, (String)session.getAttribute("user_name"));

					oldPaymentNumber = paymentNumber;
					rset = selectStmt.executeQuery();

					if (rset.next())
					{
						paymentNumber = rset.getInt(1);
						//System.out.println("Retrieved payment_no = " + paymentNumber);

						// retrieve dynamically created parameterts
						paramMap = request.getParameterMap();
						iter = (paramMap.entrySet()).iterator();

						//close insert statement to be reused
						insertStmt.close();

						sqlString = "INSERT INTO PAYBREAK (  " +
								"fee_fine_no,            " +
								"payment_no,             " +
								"amount,                 " +
								"account_no,             " +
								"last_updated_by,        " +
								"last_updated_datetime,  " +
								"last_updated_from)      " +
								"VALUES (?, ?, ?, ?, ?, ?, ?)";
						insertStmt = conn.prepareStatement(sqlString);

						//System.out.println("Test after the insert statement in PAYBREAK");
						double fee = 0.0;
						double payment_total = 0.0;
						while (iter.hasNext())
						{
							me = (Map.Entry)iter.next();
							String param = (String)me.getKey();
							String value[] = (String[])me.getValue();
							rowsInserted = 0;

							// if request parameter  has feeXXX format (XXX is fee_fine_no)
							// and value for this parameter != ""

							if ( ("fee".equals(param.substring(0,3))) && (!"".equals(value[0])) )
							{
								feeNo = param.substring(3);
								//System.out.println("fee number is "+ feeNo);

								selectStmt = conn.prepareStatement(
										"SELECT account_no                " +
												"FROM FEES_FINES    " +
										"WHERE FEES_FINES.fee_fine_no = ? " );

								selectStmt.setInt(1, Integer.parseInt(feeNo));

								rset = selectStmt.executeQuery();

								if (!(rset.next()))
								{
									// problem with retrieving account_no
									System.out.println("Failed to read account_no");
									needToRollback = true;
									returnCode = OPERATION_FAILED;
								}
								else
								{

									str = rset.getString(1);
									if (str != null)
										accountNo = str.trim();
									else
										accountNo = "";
								}

								//System.out.println("The account_no is= " + accountNo);

								insertStmt.setInt(1, Integer.parseInt(feeNo));
								insertStmt.setInt(2, paymentNumber);
								fee = Double.parseDouble(value[0]);
								payment_total += fee;
								insertStmt.setDouble(3, -fee);
								insertStmt.setString(4, accountNo);
								insertStmt.setString(5, (String)session.getAttribute("user_name"));
								insertStmt.setTimestamp(6, Timestamp.valueOf(sdf2.format(dt)));
								insertStmt.setString(7, ipAdd);

								rowsInserted = insertStmt.executeUpdate();
								if (rowsInserted != 1)
								{
									System.out.println("Insert into table PAYBREAK failed");
									needToRollback = true;
									returnCode = OPERATION_FAILED;

									// leave while loop
									break;
								}
							}
						}
						if (needToRollback == false)
						{
							if (updateStmt != null)
								updateStmt.close();

							//Update PAYMENT record of that which was voided
							updateStmt = conn.prepareStatement(
									"update payments"
											+ " set voided_by=? "
											+ " where case_id=? and sequence_no=? and payment_no=?");
							updateStmt.setInt(1, paymentNumber);
							updateStmt.setInt(2, caseId);
							updateStmt.setInt(3, sequenceNumber);
							updateStmt.setInt(4, payNo);
							rowsUpdated = updateStmt.executeUpdate();
							if (rowsUpdated < 1)
							{
								System.out.println("Update in table PAYMENTS of the voided_by failed");
								needToRollback = true;
								returnCode = OPERATION_FAILED;
							}


							// void any escrow accounts this payment created by zeroing balance in the account
							if (selectStmt != null)
								selectStmt.close();
							System.out.println("PaymentBean.ProcessVoid(): oldPaymentNumber="+oldPaymentNumber);
							selectStmt = conn.prepareStatement(
									"select escrow_no, escrow_amount"
											+ " from escrow"
											+ " where payment_no=?");
							selectStmt.setInt(1, oldPaymentNumber);
							rset = selectStmt.executeQuery();
							if (rset.next())
							{
								escrowNumber = rset.getInt(1);
								fee = rset.getDouble(2);
								//System.out.println("Retrieved escrow_no = " + escrowNumber);
								//System.out.println("Retrieved escrow_amount = " + fee);
								// update escrow account to set balance = 0 and update description

								if (updateStmt != null)
									updateStmt.close();
								updateStmt = conn.prepareStatement(
										"update escrow set current_balance="
												+ "0" // (fee - payment_total)
												+ ", description='Payment to escrow voided'"
												+ ",last_updated_by = ?, last_updated_datetime=?, last_updated_from=?"
												+ " where escrow_no=?");
								updateStmt.setString(1, (String)session.getAttribute("user_name"));
								updateStmt.setTimestamp(2, Timestamp.valueOf(sdf2.format(dt)));
								updateStmt.setString(3, ipAdd);
								updateStmt.setInt(4,escrowNumber);

								rowsUpdated = updateStmt.executeUpdate();
								if (rowsUpdated != 1)
								{
									System.out.println("Update of table ESCROW failed");
									needToRollback = true;
									returnCode = OPERATION_FAILED;
								}
							}
						}
						if (needToRollback == false)
						{ // check to see if we are voiding a payment that was made from escrow
							if (selectStmt != null)
								selectStmt.close();
							//System.out.println("PaymentBean.ProcessPayment(): oldPaymentNumber="+oldPaymentNumber);
							selectStmt = conn.prepareStatement(
									"select amount, escrow_no"
											+ " from payments"
											+ " where payment_no=?");
							selectStmt.setInt(1, oldPaymentNumber);
							rset = selectStmt.executeQuery();
							while (rset.next())
							{ // will retrieve two rows if this payment is from an escrow account (one is positive, one is negative)
								escrowAmount = rset.getDouble(1);
								escrowNo = rset.getInt(2);
								if ((escrowAmount > 0.0) && (escrowNo != 0))
								{ // we found the positive payment, now add the value to the escrow account
									if (updateStmt != null)
										updateStmt.close();
									updateStmt = conn.prepareStatement(
											"update escrow"
													+ " set current_balance=current_balance+?"
													+ ", description=concat(description, ?, 'voided payment')"
													+ ", last_updated_by=?, last_updated_datetime=?, last_updated_from=?"
													+ " where escrow_no=?");
									updateStmt.setDouble(1,escrowAmount);
									updateStmt.setDouble(2,escrowAmount);
									updateStmt.setString(3, (String)session.getAttribute("user_name"));
									updateStmt.setTimestamp(4, Timestamp.valueOf(sdf2.format(dt)));
									updateStmt.setString(5, ipAdd);
									updateStmt.setInt(6,escrowNo);

									rowsUpdated = updateStmt.executeUpdate();
									if (rowsUpdated != 1)
									{
										System.out.println("Update of table ESCROW failed on re-adding funds");
										needToRollback = true;
										returnCode = OPERATION_FAILED;
									}
									else
									{ // now add the payment into the escrow account to the payments
										if (updateStmt != null)
											updateStmt.close();
										rowsInserted = 0;
										sqlString = "insert into payments"
												+ " (case_id, sequence_no, amount, payment_date"
												+ ", method, paid_by, description, last_updated_by"
												+ ", last_updated_datetime, last_updated_from)"
												+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
										insertStmt = conn.prepareStatement(sqlString);
										insertStmt.setInt(1, caseId);
										insertStmt.setInt(2, sequenceNumber);
										insertStmt.setDouble(3, Double.parseDouble(amount));
										insertStmt.setTimestamp(4, Timestamp.valueOf(sdf2.format(sdf1.parse(refundDate))));
										insertStmt.setString(5, "ESCROW");
										insertStmt.setString(6, refundBy + " voided escrow payment returned");
										insertStmt.setString(7, "Escrow IN from voided payment");
										insertStmt.setString(8, (String)session.getAttribute("user_name"));
										insertStmt.setTimestamp(9, Timestamp.valueOf(sdf2.format(dt)));
										insertStmt.setString(10, ipAdd);

										rowsInserted = insertStmt.executeUpdate();
										if (rowsInserted != 1)
										{
											System.out.println("Insert into table PAYMENTS failed");
											needToRollback = true;
											rbd = new rollbackDone(userName, methodName, sqlString);
											returnCode = OPERATION_FAILED;
										}
										else
										{
											//System.out.println("Insert into table PAYMENTS successful");
											if (selectStmt != null)
												selectStmt.close();
											// find the payment number for the last payment added so we can stuff it in paybreak too
											selectStmt = conn.prepareStatement(
													"select max(payment_no)"
															+ " from payments"
															+ " where case_id=? and sequence_no=? and last_updated_by=?");
											selectStmt.setInt(1, caseId);
											selectStmt.setInt(2, sequenceNumber);
											selectStmt.setString(3, (String)session.getAttribute("user_name"));
											rset = selectStmt.executeQuery();
											if (rset.next())
											{ // add the payment to the paybreak too
												paymentNumber2 = rset.getInt(1);
												//System.out.println("Retrieved payment_no = " + paymentNumber2);
												if (selectStmt != null)
													selectStmt.close();
												// get the account number from the FEE_SCHEDULE
												selectStmt = conn.prepareStatement(
														"select fee_schedule.account_no"
																+ " from fee_schedule"
																+ " where fee_group Is Null"
																+ " and case_category='" + category + "'"
																+ " and fee_type='ESCROW'");
												rset = selectStmt.executeQuery();
												if (!(rset.next()))
												{
													// problem with retrieving account_no
													System.out.println("Failed to read account_no");
													needToRollback = true;
													returnCode = OPERATION_FAILED;
												}
												else
												{
													escrowAccNo = rset.getString(1).trim();
													//System.out.println("The escrow account_no is= " + escrowAccNo);
													if (selectStmt != null)
														selectStmt.close();
													// get the fee_number from an existing escrow fee
													selectStmt = conn.prepareStatement(
															"select fee_fine_no"
																	+ " from fees_fines"
																	+ " where fee_type='ESCROW' and account_no=?"
																	+ " and case_id=? and sequence_no=?");
													selectStmt.setString(1,escrowAccNo);
													selectStmt.setInt(2,caseId);
													selectStmt.setInt(3,sequenceNumber);
													rset = selectStmt.executeQuery();
													if (!(rset.next()))
													{
														// problem with retrieving account_no
														System.out.println("Failed to read account_no");
														needToRollback = true;
														returnCode = OPERATION_FAILED;
													}
													else
													{
														accountNo = rset.getString(1);
														//System.out.println("The account_no is= " + accountNo);
														if (insertStmt != null)
															insertStmt.close();
														sqlString = "insert into paybreak"
																+ " (fee_fine_no, payment_no, amount"
																+ ", account_no, last_updated_by"
																+ ", last_updated_datetime, last_updated_from)"
																+ " values (?, ?, ?, ?, ?, ?, ?)";
														insertStmt = conn.prepareStatement(sqlString);

														insertStmt.setInt(1, Integer.parseInt(accountNo));
														insertStmt.setInt(2, paymentNumber2);
														insertStmt.setDouble(3, Double.parseDouble(amount));
														insertStmt.setString(4, accountNo);
														insertStmt.setString(5, (String)session.getAttribute("user_name"));
														insertStmt.setTimestamp(6, Timestamp.valueOf(sdf2.format(dt)));
														insertStmt.setString(7, ipAdd);

														rowsInserted = insertStmt.executeUpdate();
														if (rowsInserted != 1)
														{
															System.out.println("Insert into table PAYBREAK failed");
															needToRollback = true;
															rbd = new rollbackDone(userName, methodName, sqlString);
															returnCode = OPERATION_FAILED;
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
					else
					{
						// problem with retrieving payment_no
						System.out.println("Failed to retrieve payment_no");
						needToRollback = true;
						returnCode = OPERATION_FAILED;
					}
				}
			}
			// if no problems reported (needToRollback == false)
			// continue with inserting to REFUNDS
			if (needToRollback == false) {

				// close statement to be reused
				if (insertStmt != null)
					insertStmt.close();
				//if (selectStmt != null)
				//selectStmt.close();

				rowsInserted = 0;

				// insert row in PAYMENTS
				sqlString = "INSERT INTO REFUNDS ( " +
						"party_no,             " +
						"case_id,              " +
						"sequence_no,          " +
						"payment_no,           " +
						"amount,               " +
						"account_no,           " +
						"refund_datetime,      " +
						"refund_by,            " +
						"last_updated_by,      " +
						"last_updated_datetime, " +
						"last_updated_from)     " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				insertStmt = conn.prepareStatement(sqlString);

				insertStmt.setInt(1, Integer.parseInt(partyTo));
				insertStmt.setInt(2, caseId);
				insertStmt.setInt(3, sequenceNumber);
				//insertStmt.setInt(4, feeFineNumber);
				insertStmt.setInt(4, paymentNumber);
				insertStmt.setDouble(5, Double.parseDouble(amount));
				insertStmt.setString(6, refundAccNo);
				insertStmt.setTimestamp(7, Timestamp.valueOf(sdf2.format(sdf1.parse(refundDate))));
				insertStmt.setString(8, refundBy);
				insertStmt.setString(9, (String)session.getAttribute("user_name"));
				insertStmt.setTimestamp(10, Timestamp.valueOf(sdf2.format(dt)));
				insertStmt.setString(11, ipAdd);

				rowsInserted = insertStmt.executeUpdate();

				if (rowsInserted != 1) {
					System.out.println("Insert into table REFUNDS failed");
					returnCode = OPERATION_FAILED;
					needToRollback = true;
					rbd = new rollbackDone(userName, methodName, sqlString);
				}
			}

			// if no problems reported (needToRollback == false)
			// continue with update to INSTALLMENT_CHARGES if there is one
			if (needToRollback == false)
			{
				if (installmentNo > 0)
				{
					// close statement to be reused
					if (selectStmt != null)
						selectStmt.close();

					int chNo = 0;
					double payAmt = 0.0;
					double refundedAmt = Double.parseDouble(amount);
					//System.out.println("Refunded amount is "+ refundedAmt);

					selectStmt = conn.prepareStatement(
							"SELECT charge_no,                     " +
									"       payment_amount                 " +
									"FROM INSTALLMENT_CHARGE               " +
									"WHERE INSTALLMENT_CHARGE.installment_no = ? " +
							"ORDER BY charge_no DESC ");

					selectStmt.setInt(1, installmentNo);

					rset = selectStmt.executeQuery();

					while ((rset.next()) && (refundedAmt >= 0.0))
					{

						chNo = rset.getInt(1);
						payAmt = rset.getDouble(2);

						//System.out.println("Refunded amount in while is "+ refundedAmt);
						//Update INSTALLMENT_CHARGE for the payment amount
						// perform update
						if (updateStmt != null)
							updateStmt.close();
						updateStmt = conn.prepareStatement(
								"UPDATE INSTALLMENT_CHARGE            " +
										"SET payment_amount = ?,              " +
										"    payment_no = ?,                  " +
										"    last_updated_datetime = ?,       " +
										"    last_updated_by = ?,             " +
										"    last_updated_from = ?            " +
								"WHERE charge_no = ?");

						// set parameters for update statement
						if (payAmt >= refundedAmt) {
							updateStmt.setDouble(1, payAmt-refundedAmt);
							refundedAmt -= payAmt;
						} else {
							updateStmt.setDouble(1, 0);
							refundedAmt -= payAmt;
						}
						updateStmt.setInt(2, 0);
						updateStmt.setTimestamp(3, Timestamp.valueOf(sdf2.format(dt)));
						updateStmt.setString(4, (String)session.getAttribute("user_name"));
						updateStmt.setString(5, ipAdd);
						updateStmt.setInt(6, chNo);

						rowsUpdated = updateStmt.executeUpdate();
						if (rowsUpdated != 1)
						{
							System.out.println("Update in table INSTALLMENT_CHARGE failed");
							// if update failed - rollback to
							// release any locks
							needToRollback = true;
							returnCode = OPERATION_FAILED;
						}
						else
						{
							System.out.println("Update in table INSTALLMENT_CHARGE successful");
						}
					}
				}
			}

			DecimalFormat df1 = new DecimalFormat("#,##0.00");
			if (((String)session. getAttribute("user_clerk")).equals("COL") ){
				System.out.println("about to log collections in debtors_notes");
				if (insertStmt != null)
					insertStmt.close();
				rowsInserted=0;

				insertStmt = conn.prepareStatement(
						"INSERT INTO DEBTORS_NOTES (   " +
								"person_no,          		" +
								"subtype,           		" +
								"description,			" +
								"entered_date,     		" +
								"entered_by, 			" +
								"entered_from)      		" +
								"VALUES              	" +
						"(?, ?, ?, ?, ?, ?)");
				insertStmt.setInt(1,Integer.parseInt((String)session.getAttribute("person_no")));
				insertStmt.setString(2,"PYMT");
				String commentReference = "";
				if (!"".equals(description))
					commentReference = " | Comment:"+description;
				insertStmt.setString(3,"VOID of -$"+df1.format(Double.parseDouble(amount))+" Reason: VOID " + commentReference);
				insertStmt.setTimestamp(4, Timestamp.valueOf(sdf2.format(dt)));
				insertStmt.setString(5, (String)session.getAttribute("user_name"));
				insertStmt.setString(6, ipAdd);

				rowsInserted = insertStmt.executeUpdate();

				if (rowsInserted != 1){
					System.out.println("Insert into table DEBTORS_NOTES for creating a multi install payment VOID failed!");
					// if insert failed - rollback to
					// release any locks
					needToRollback = true;
					returnCode = OPERATION_FAILED;
				}
			}
		}
		catch (NumberFormatException e)
		{
			// parseDouble() throws this exception
			System.out.println("Caught NumberFormatException in PaymentBean.processVoid()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "NumberFormatException caught");
			returnCode = DATABASE_EXCEPTION;
		}
		catch (ParseException e)
		{
			// parse() throws this exception
			System.out.println("Caught ParseException in PaymentBean.processVoid()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "ParseException caught");
			returnCode = DATABASE_EXCEPTION;
		}
		catch (SQLException e)
		{
			// preparedStatement(), executeUpdate() throw this exception
			System.out.println("Caught SQLException in PaymentBean.processVoid()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "SQLException caught");
			returnCode = DATABASE_EXCEPTION;
		}
		finally
		{
			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.processVoid().");
			try
			{
				if (rset != null)    // Close statements
					rset.close();

				if (selectStmt != null){
					selectStmt.close();
					selectStmt = null;
				}
				if (insertStmt != null){
					insertStmt.close();
					insertStmt = null;
				}
				if (updateStmt != null){
					updateStmt.close();
					updateStmt = null;
				}
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
				System.out.println("Caught SQLException in PaymentBean.processVoid()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// inserts took place
				return returnCode;
			}
		}
	}

	/**
	 * reads refund information from the database
	 * <p>
	 * @param refund number as <code>int</code>
	 * @param case category as <code>String</code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="PaymentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: payment data is read from the database
	 *               with focus on payment distribution </i>
	 */

	public int readRefundData(int refundNo, String caseCategory)
	{

		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int i = 0;
		String str = "";
		String[][] tmpArray = new String[MAX_SIZE][2];
		String lName = "";
		String mName = "";
		String fName = "";
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet rset = null;

		try {
			conn = getConnection(jndiResource);

			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection

			// clear values in the member variables (rest form)
			reset();

			// define format used by form
			SimpleDateFormat sdf = new SimpleDateFormat ("MM/dd/yyyy");

			// define currency format
			DecimalFormat df = new DecimalFormat ("#0.00");


			selectStmt = conn.prepareStatement (
					"SELECT PAYMENTS.amount,           " +
							"       method,                    " +
							"       check_no,                  " +
							"       payment_date,              " +
							"       paid_by,                   " +
							"       PAYMENTS.description,      " +
							"       PAYMENTS.last_updated_by,  " +
							"       last_name,                 " +
							"       first_name,                " +
							"       middle_name                " +
							"FROM PAYMENTS, PAYBREAK, FEES_FINES, PARTIES, PERSONS        " +
							"WHERE PAYMENTS.case_id = FEES_FINES.case_id        " +
							"AND PAYMENTS.sequence_no = FEES_FINES.sequence_no  " +
							"AND PAYBREAK.payment_no = PAYMENTS.payment_no      " +
							"AND PAYBREAK.fee_fine_no = FEES_FINES.fee_fine_no  " +
							"AND FEES_FINES.party_responsible = PARTIES.party_no " +
							"AND PARTIES.person_no = PERSONS.person_no          " +
					"AND PAYMENTS.payment_no = ?");

			selectStmt.setInt(1, refundNo);

			rset = selectStmt.executeQuery();

			// expect one row
			if (rset.next()) {
				amount = df.format(rset.getDouble("amount"));
				//System.out.println("The amount is " + amount);
				str = rset.getString("method");
				if (str != null)
					method = str.trim();
				//System.out.println("The method of payment is " + method);
				str = rset.getString("check_no");
				//System.out.println(str);
				if (str != null)
					checkNumber = str.trim();
				//System.out.println("The check number is " + checkNumber);
				Timestamp ts = rset.getTimestamp("payment_date");
				paymentDate = sdf.format(ts);
				paidBy = rset.getString("paid_by").trim();
				str = rset.getString("description");
				if (str != null)
					description = str.trim();
				//System.out.println("The description is " + description);

				lName = rset.getString("last_name");
				if (lName != null)
					lName = lName.trim();
				//System.out.println("The last name is " + lName);
				fName = rset.getString("first_name");
				if (fName != null)
					fName = fName.trim();
				//System.out.println("The first name is " + fName);
				mName = rset.getString("middle_name");
				if (mName != null)
					mName = mName.trim();
				//System.out.println("The middle name is " + mName);
				partyRes = lName + " " + fName + " " + mName;

				selectStmt.close();
				selectStmt = null;

				selectStmt = conn.prepareStatement (
						"SELECT value,                  " +
								"PAYBREAK.amount,               " +
								"FEES_FINES.reference                      " +
								"FROM PAYBREAK,                 " +
								"FEES_FINES, CODE_TABLE         " +
								"WHERE payment_no = ?           " +
								"AND PAYBREAK.fee_fine_no = FEES_FINES.fee_fine_no " +
								"AND fee_type = code            " +
								"AND (code_type = ?             " +
								"OR code_type = 'fee_type_misc' " +
						"OR code_type = 'fine_type')");

				selectStmt.setInt(1, refundNo);

				if ("CRIMINAL".equals(caseCategory))
					selectStmt.setString(2, "fee_type_criminal");

				if (!"CRIMINAL".equals(caseCategory))
					selectStmt.setString(2, "fee_type_civil");

				rset = selectStmt.executeQuery();

				while ( (rset.next()) && (i < MAX_SIZE)) {

					tmpArray[i][0] = rset.getString(1).trim();

					// check if reference != NULL
					// (means this is one of installment fees/fines
					if (rset.getInt(3) != 0)
						tmpArray[i][0] += " (Installment)";

					tmpArray[i][1] = df.format(rset.getDouble(2));

					i++;
				}

				if (i == 0) {
					System.out.println("SELECT from table PAYBREAK failed nn");
					// if select failed - rollback to
					// release any locks
					needToRollback = true;
					returnCode = OPERATION_FAILED;
				} else {

					// create paybreakArray
					paybreakArray = new String[i][2];

					// copy data from tmp array
					for (int j=0; j<i;j++) {
						paybreakArray[j][0] = tmpArray[j][0];
						paybreakArray[j][1] = tmpArray[j][1];
					}
				}
			} else {
				System.out.println("SELECT from table PAYMENTS failed");
				// if select failed - rollback to
				// release any locks
				needToRollback = true;
				returnCode = OPERATION_FAILED;
			}
		}
		catch (SQLException e) {
			// preparedStatement(), executeUpdate() throw this exception
			System.out.println("Caught SQLException in PaymentBean.readRefundData()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			returnCode = DATABASE_EXCEPTION;
		}
		finally {

			// allow gc
			tmpArray = null;

			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.readRefundData().");
			try {
				//used to be commented out
				if (rset != null){    // Close statements
					rset.close();
					rset = null;
				}
				if (selectStmt != null){
					selectStmt.close();
					selectStmt = null;
				}
				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();

					conn.close();
					conn = null;
				}

				return returnCode;
			}
			catch (SQLException e) {
				// close() throw this exception
				System.out.println("Caught SQLException in PaymentBean.readRefundData()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// inserts took place
				return returnCode;
			}
		}
	}

	/**
	 * reads refund information from the database
	 * <p>
	 * @param ID of the case refunds are read as <code>int</code>
	 * @param sequence number of the case efunds are read as <code>int</code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="PaymentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 *   <li><code><a href="PaymentBean.html#NO_DATA_FOUND">NO_DATA_FOUND</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: payment data is read from the database
	 *               with focus on payments history </i>
	 */

	public int readAllRefunds(int csId, int seqNo)
	{

		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int i = 0;
		String str = "";
		String[][] tmpArray = new String[MAX_SIZE][5];

		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet rset = null;

		try {
			conn = getConnection(jndiResource);

			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection

			// clear values in the member variables (rest form)
			reset();

			// define format used by form
			SimpleDateFormat sdf = new SimpleDateFormat ("MM/dd/yyyy");

			// define currency format
			DecimalFormat df = new DecimalFormat ("#,##0.00");

			selectStmt = conn.prepareStatement (
					"SELECT REFUNDS.payment_no, " +
							"refund_datetime,           " +
							"REFUNDS.amount,                    " +
							"refund_by,                 " +
							"method                     " +
							"FROM REFUNDS, PAYMENTS     " +
							"WHERE REFUNDS.payment_no = PAYMENTS.payment_no " +
							"AND REFUNDS.case_id = ?     " +
							"AND REFUNDS.sequence_no = ? " +
					"ORDER BY refund_datetime");

			selectStmt.setInt(1, csId);
			selectStmt.setInt(2, seqNo);

			rset = selectStmt.executeQuery();

			while ((rset.next()) && (i<MAX_SIZE)) {

				tmpArray[i][0] = String.valueOf(rset.getInt(1));
				Timestamp ts = rset.getTimestamp(2);
				tmpArray[i][1] = sdf.format(ts);;
				tmpArray[i][2] = df.format(rset.getDouble(3));
				tmpArray[i][3] = rset.getString(4).trim();
				str = rset.getString(5);
				if (str != null)
					tmpArray[i][4] = str.trim();

				i++;
			}

			System.out.println("Found refunds "+i);

			if (i > 0) {

				// create paymentsArray
				refundsArray = new String[i][5];

				// copy data to refundsArray
				for (int j=0; j<i; j++)
					for (int k=0; k<5; k++)
						refundsArray[j][k] = tmpArray[j][k];
			} else
				returnCode = NO_DATA_FOUND;
		}
		catch (SQLException e) {
			// preparedStatement(), executeUpdate() throw this exception
			System.out.println("Caught SQLException in PaymentBean.readAllRefunds!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			returnCode = DATABASE_EXCEPTION;
		}
		finally {

			// allow gc
			tmpArray = null;

			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.readAllRefunds.");
			try {

				if (rset != null)    // Close statements
					rset.close();

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
				System.out.println("Caught SQLException in PaymentBean.readAllRefunds()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// inserts took place
				return returnCode;
			}
		}
	}

	/**
	 * processes and stores waivefee information in the database
	 * <p>
	 * @param HTTP request as <code>HttpServletRequest</code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="PaymentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: data is stored in the database </i>
	 */

	public int processWaivefee(int installNo, String caseCategory, HttpServletRequest request)
	{
		methodName = "processWaivefee(String caseCategory, HttpServletRequest request)";
		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int rowsInserted = 0, rowsUpdated = 0;
		HttpSession session = null;
		Map paramMap = null;
		Iterator iter = null;
		Map.Entry me = null;
		int transactionId = 0;
		String feeGroup = "";
		String feeType = "";
		String str = "";
		String category = "";
		String waiveAccNo = "";

		if (!"CRIMINAL".equals(caseCategory))
			category = "V";
		if ("CRIMINAL".equals(caseCategory))
			category = "C";
		String installRef[][] = new String[MAX_SIZE][2];
		Connection conn = null;
		PreparedStatement insertStmt = null;
		PreparedStatement updateStmt = null;
		PreparedStatement selectStmt = null;
		ResultSet rset = null;
		PreparedStatement selectStmt2 = null;
		ResultSet rset2 = null;
		String ipAdd = request.getRemoteAddr();
		rollbackDone rbd = null;
		String unParsedPaymentDate = "";
		try {
			conn = getConnection(jndiResource);

			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection

			// get current session based on HttpServletRequest
			// and don't create a new on (create=false)
			session = request.getSession(false);

			// get case_id and sequence_no from session object
			caseId = Integer.parseInt((String)session.getAttribute("case_id"));
			sequenceNumber = Integer.parseInt((String)session.getAttribute("seq_no"));

			userName = (String)session.getAttribute("user_name");
			// define format used by form
			SimpleDateFormat sdf1 = new SimpleDateFormat ("MM/dd/yyyy");
			// define format for JDBC Timestamp
			SimpleDateFormat sdf2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
			DecimalFormat df = new DecimalFormat("####0.00");

			java.util.Date dt = new java.util.Date();

			if (request.getParameter("paymentDate") != null)
				unParsedPaymentDate = request.getParameter("paymentDate");


			java.util.Date payDate = new java.util.Date();
			payDate = sdf1.parse(unParsedPaymentDate);
			unParsedPaymentDate = sdf2.format(payDate);

			if (((String)session.getAttribute("user_clerk")).equals("COL") ){
				if (selectStmt != null)
					selectStmt.close();
				if (rset != null)
					rset.close();
				selectStmt = conn.prepareStatement(
						" SELECT max(allocid) FROM PAYMENTS ");
				rset = selectStmt.executeQuery();

				if (rset.next())
					transactionId = rset.getInt(1);
				transactionId+=1;
				System.out.println("this is the transactionId"+transactionId);
			}
			if (((String)session.getAttribute("user_clerk")).equals("COL") ){
				// insert row in PAYMENTS
				sqlString = "INSERT INTO PAYMENTS ( " +
						"case_id,               " +
						"sequence_no,           " +
						"amount,                " +
						"payment_date,          " +
						"method,                " +
						"paid_by,               " +
						"description,           " +
						"check_no,              " +
						"allocid,				" +
						"last_updated_by,       " +
						"last_updated_datetime, " +
						"last_updated_from)     " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			} else {
				// insert row in PAYMENTS
				sqlString = "INSERT INTO PAYMENTS ( " +
						"case_id,               " +
						"sequence_no,           " +
						"amount,                " +
						"payment_date,          " +
						"method,                " +
						"paid_by,               " +
						"description,           " +
						"check_no,              " +
						"last_updated_by,       " +
						"last_updated_datetime, " +
						"last_updated_from)     " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			}
			insertStmt = conn.prepareStatement(sqlString);

			insertStmt.setInt(1, caseId);
			insertStmt.setInt(2, sequenceNumber);
			insertStmt.setDouble(3, Double.parseDouble(amount));
			insertStmt.setTimestamp(4, Timestamp.valueOf(sdf2.format(sdf1.parse(paymentDate))));
			insertStmt.setString(5, method);
			insertStmt.setString(6, "CREDIT");
			insertStmt.setString(7, description);
			insertStmt.setString(8, checkNumber);
			if (((String)session.getAttribute("user_clerk")).equals("COL") ){
				insertStmt.setInt(9, transactionId);
				if(request.getParameter("received_by") != null && !(request.getParameter("received_by").toString().equals("")))
					insertStmt.setString(10, request.getParameter("received_by").toString());
				else
					insertStmt.setString(10, (String)session.getAttribute("user_name"));
				insertStmt.setTimestamp(11, Timestamp.valueOf(sdf2.format(dt)));
				insertStmt.setString(12, ipAdd);
			} else {
				insertStmt.setString(9, (String)session.getAttribute("user_name"));
				insertStmt.setTimestamp(10, Timestamp.valueOf(sdf2.format(dt)));
				insertStmt.setString(11, ipAdd);
			}

			rowsInserted = insertStmt.executeUpdate();

			if (rowsInserted != 1) {
				System.out.println("Insert into table PAYMENTS failed");
				// if insert failed - rollback to
				// release any locks
				needToRollback = true;
				rbd = new rollbackDone(userName, methodName, sqlString);
				returnCode = OPERATION_FAILED;
			} else {
				// insert to PAYMENTS successful

				// retrieve payment_no for newly inserted payment
				selectStmt = conn.prepareStatement(
						"SELECT MAX(payment_no)   " +
								"FROM PAYMENTS            " +
								"WHERE case_id = ?        " +
								"AND sequence_no = ?      " +
						"AND last_updated_by = ?" );

				selectStmt.setInt(1, caseId);
				selectStmt.setInt(2, sequenceNumber);
				if(request.getParameter("received_by") != null && !(request.getParameter("received_by").toString().equals("")))
					selectStmt.setString(3, request.getParameter("received_by").toString());
				else
					selectStmt.setString(3, (String)session.getAttribute("user_name"));

				rset = selectStmt.executeQuery();

				if (rset.next())
					paymentNumber = rset.getInt(1);
				//System.out.println("Retrieved payment_no = " + paymentNumber);

				// ***********************  same as waive except params and only one charge record is created ....
				System.out.println("install number = " + installNo);
				if (!((String)session.getAttribute("user_clerk")).equals("COL") ){
					if (installNo != 0) {
						//Insert the installment charge record
						sqlString = "insert into installment_charge"
								+ " (installment_no, charge_amount, charge_date, payment_amount"
								+ ", payment_no, last_updated_datetime, last_updated_by, last_updated_from)"
								+ " values (?, ?, ?, ?, ?, ?, ?, ?)";
						insertStmt = conn.prepareStatement(sqlString);

						insertStmt.setInt(1, installNo);
						insertStmt.setDouble(2, Double.parseDouble(amount));
						insertStmt.setTimestamp(3, Timestamp.valueOf(sdf2.format(dt)));
						insertStmt.setDouble(4, Double.parseDouble(amount));
						insertStmt.setInt(5, paymentNumber);
						insertStmt.setTimestamp(6, Timestamp.valueOf(sdf2.format(dt)));
						insertStmt.setString(7, (String)session.getAttribute("user_name"));
						insertStmt.setString(8, ipAdd);

						rowsInserted = insertStmt.executeUpdate();

						if (rowsInserted != 1)
						{
							System.out.println("Insert into table INSTALLMENT_CHARGE failed");
							needToRollback = true;
							rbd = new rollbackDone(userName, methodName, sqlString);
							returnCode = OPERATION_FAILED;
							//				break;
						}

					}
				} else {
					System.out.println("this is about to update adjustment to installmentcharge");
					// ********************************************
					// adjust on INSTALLMENT_CHARGE
					// ********************************************
					double pdAmt=0.0, chargeAmt=0.0;
					int noOfInstallments=0,instNo=0, pdNo=0, chargeNo=0;
					Timestamp pdDate = null;
					Timestamp nextPayDue = null;
					double noOfChargeUpdates=0.0, paidHold=0.0, balance=0.0;
					if (selectStmt != null)
						selectStmt.close();
					if (rset != null)
						rset.close();
					// retrieve installment no  for newly inserted payment
					selectStmt = conn.prepareStatement(
							" SELECT installments, coalesce(sum(paybreak.amount), 0.0) " +
									"FROM ((fees_fines f inner join paybreak on f.fee_fine_no=paybreak.fee_fine_no) " +
									"inner join PAYMENTS on paybreak.payment_no=PAYMENTS.payment_no) " +
									"inner join installment_cases ic on PAYMENTS.case_id=ic.case_id "+
									"where PAYMENTS.payment_no = ? " +
									//" AND (group_record is null or group_record = '') "+
									"GROUP BY f.installments "+
							"ORDER BY f.installments ");

					selectStmt.setInt(1, paymentNumber);
					rset = selectStmt.executeQuery();
					System.out.println("this is after execute query"+paymentNumber);
					noOfInstallments=0;
					while (rset.next()) {
						installRef[noOfInstallments][0] = rset.getString("installments");
						installRef[noOfInstallments][1] = df.format(rset.getDouble(2));
						System.out.println("this is the installment_No"+rset.getString(1));
						noOfInstallments++;
					}

					System.out.println("this is the installment_No count:"+noOfInstallments);
					for (int k=0; k<noOfInstallments; k++){
						instNo=Integer.parseInt(installRef[k][0]);
						pdAmt=Double.parseDouble(installRef[k][1]);
						//pdNo=rset.getInt(2);
						pdDate=Timestamp.valueOf(sdf2.format(dt));

						double sumCharges=0.0;
						if (selectStmt2 != null)
							selectStmt2.close();
						if (rset2 != null)
							rset2.close();
						//now get the amount paid before the installment payment
						selectStmt2 = conn.prepareStatement (
								"SELECT paidhold, payment_amount, balance, status  "+
										"FROM installments " +
										"WHERE installment_no = '"+instNo+"'  ");

						rset2 = selectStmt2.executeQuery();
						paidHold=0;
						if (rset2.next()){
							paidHold = rset2.getDouble(1);
							//chargeAmt = rset2.getDouble(2);
							balance = rset2.getDouble(3);
						}
						pdAmt=pdAmt+paidHold;
						double amtLeftToProcess=0.0;
						amtLeftToProcess=pdAmt;
						//do while there's more money on payment to process
						boolean hasCharge=true;
						do{
							if (selectStmt2 != null)
								selectStmt2.close();
							if (rset2 != null)
								rset2.close();
							//now get the amount of the next charge record topay
							selectStmt2 = conn.prepareStatement (
									"SELECT charge_no, payment_amount  "+
											"FROM installment_charge " +
											"WHERE installment_no = '"+instNo+"' AND not payment_amount=0 order by charge_date LIMIT 1");
							rset2 = selectStmt2.executeQuery();
							if (rset2.next()){
								chargeNo = rset2.getInt(1);
								chargeAmt = rset2.getDouble(2);
								System.out.println("'"+instNo+"','"+pdDate+"','"+pdAmt+"','"+paidHold+"','"+chargeAmt+"','"+balance+"','"+chargeNo+"','"+noOfChargeUpdates+"'");
								System.out.println("amtLeftToProcess"+amtLeftToProcess+" charge_amt:"+chargeAmt);
								if (amtLeftToProcess>=chargeAmt){
									hasCharge=true;
									if (updateStmt != null)
										updateStmt.close();
									rowsUpdated=0;
									updateStmt = conn.prepareStatement (
											"UPDATE INSTALLMENT_CHARGE set payment_amount=payment_amount-?, " + //should set to zero!!
													"payment_no= 0, paid_date='"+pdDate+"', last_updated_datetime='"+pdDate+"' where charge_no=? ");

									updateStmt.setDouble(1, chargeAmt);
									amtLeftToProcess-=chargeAmt;
									updateStmt.setInt(2, chargeNo);
									sumCharges+=chargeAmt;
									System.out.println("sumCharges:"+sumCharges);
									rowsUpdated = updateStmt.executeUpdate();
									if (rowsUpdated != 1){
										System.out.println("....update error on  INSTALLMENT_CHARGE for update ");
										// if insert failed - rollback to
										// release any locks
										needToRollback = true;
										returnCode = OPERATION_FAILED;
									}
								} else {
									hasCharge=false;
								}
							}else{
								hasCharge=false;
							}//end of if there's a charge record to process

						} while ((amtLeftToProcess>0) && (hasCharge == true));

						//paid amt already includes old hold
						double newHold=0.0;
						newHold=pdAmt-sumCharges;
						balance=balance+paidHold-sumCharges-newHold;
						//update here
						if (updateStmt != null)
							updateStmt.close();
						rowsUpdated=0;
						System.out.println("this is the sumcharges"+balance);
						updateStmt = conn.prepareStatement (
								"UPDATE INSTALLMENTS set balance=?, " +
								"paidHold= ? where installment_no=? ");
						updateStmt.setDouble(1, balance);
						updateStmt.setDouble(2, newHold);
						//updateStmt.setTimestamp(2, pdDate);
						updateStmt.setInt(3, instNo);

						rowsUpdated = updateStmt.executeUpdate();
						if (rowsUpdated != 1){
							System.out.println("....update error on  INSTALLMENTS for balance, paidHold update ");
							// if insert failed - rollback to
							// release any locks
							needToRollback = true;
							returnCode = OPERATION_FAILED;
						}
						// ********************************************
						// adjust last_billing_date INSTALLMENTS
						// ********************************************
						//if this is the last payment installments last_billing_date will be null
						if (updateStmt != null)
							updateStmt.close();
						rowsUpdated=0;
						updateStmt = conn.prepareStatement (
								"update installments a set a.last_billing_date=(select min(charge_date) " +
										"from installment_charge where installment_no=a.installment_no " +
										"and not payment_amount=0 group by installment_no) " +
										"where a.installment_no='"+instNo+"' ");

						rowsUpdated = updateStmt.executeUpdate();
						if (rowsUpdated != 1){
							System.out.println("....update error on  INSTALLMENT for update of last_billing_date");
							// if insert failed - rollback to
							// release any locks
							needToRollback = true;
							returnCode = OPERATION_FAILED;
						}
						// ********************************************
						// adjust days_late INSTALLMENTS
						// ********************************************
						int dayslate=0;
						if (selectStmt2 != null)
							selectStmt2.close();
						if (rset2 != null)
							rset2.close();

						selectStmt2 = conn.prepareStatement (
								"SELECT days_late=datediff(now(), last_billing_date) from  installments a inner join installment_charge b on a.installment_no=b.installment_no " +
										"and a.last_billing_date = b.charge_date " +
										"where a.installment_no='"+instNo+"' ");

						rset2 = selectStmt2.executeQuery();

						if (rset2.next())
							dayslate=rset2.getInt(1);
						if (dayslate>0){
							if (updateStmt != null)
								updateStmt.close();
							rowsUpdated=0;
							updateStmt = conn.prepareStatement (
									"update  installments a inner join installment_charge b on a.installment_no=b.installment_no " +
											"and a.last_billing_date = b.charge_date " +
											"set days_late=datediff(now(), last_billing_date) " +
											"where a.installment_no='"+instNo+"' ");

							rowsUpdated = updateStmt.executeUpdate();
							if (rowsUpdated != 1){
								System.out.println("....update error on  INSTALLMENT for update of payments_missed");
								// if insert failed - rollback to
								// release any locks
								needToRollback = true;
								returnCode = OPERATION_FAILED;
							}
						} else {
							//he's not late
							if (updateStmt != null)
								updateStmt.close();
							rowsUpdated=0;
							updateStmt = conn.prepareStatement (
									"update  installments set days_late=0 " +
											"where installment_no='"+instNo+"' ");

							rowsUpdated = updateStmt.executeUpdate();
							if (rowsUpdated != 1){
								System.out.println("....update error on  INSTALLMENT for update of payments_missed");
								// if insert failed - rollback to
								// release any locks
								needToRollback = true;
								returnCode = OPERATION_FAILED;
							}
						}
						// ********************************************
						// adjust payments_missed INSTALLMENTS
						// ********************************************
						int paymissed=0;
						if (selectStmt2 != null)
							selectStmt2.close();
						if (rset2 != null)
							rset2.close();

						selectStmt2 = conn.prepareStatement (
								"SELECT count(installment_no) from  installment_charge " +
										"where not payment_amount=0 and charge_date<=now() and installment_no='"+instNo+"' ");

						rset2 = selectStmt2.executeQuery();

						if (rset2.next())
							paymissed=rset2.getInt(1);
						if (paymissed>0){
							if (updateStmt != null)
								updateStmt.close();
							rowsUpdated=0;
							updateStmt = conn.prepareStatement (
									"update installments a set a.payments_missed=(select count(installment_no) " +
											"from installment_charge where installment_no=a.installment_no " +
											"and not payment_amount=0 and charge_date<=now()) " +
											"where a.installment_no='"+instNo+"' ");

							rowsUpdated = updateStmt.executeUpdate();
							if (rowsUpdated != 1){
								System.out.println("....update error on  INSTALLMENT for update of payments_missed");
								// if insert failed - rollback to
								// release any locks
								needToRollback = true;
								returnCode = OPERATION_FAILED;
							}
							// ********************************************
							// adjust arrearage INSTALLMENTS
							// ********************************************

							if (updateStmt != null)
								updateStmt.close();
							rowsUpdated=0;
							updateStmt = conn.prepareStatement (
									"update installments a set a.arrearage=(select sum(payment_amount) " +
											"from installment_charge where installment_no=a.installment_no " +
											"and not payment_amount=0 and charge_date<=now()) " +
											"where a.installment_no='"+instNo+"' ");

							rowsUpdated = updateStmt.executeUpdate();
							if (rowsUpdated != 1){
								System.out.println("....update error on  INSTALLMENT for update of arrearage");
								// if insert failed - rollback to
								// release any locks
								needToRollback = true;
								returnCode = OPERATION_FAILED;
							}
						} else {
							//no payments missed
							if (updateStmt != null)
								updateStmt.close();
							rowsUpdated=0;
							updateStmt = conn.prepareStatement (
									"update installments a set a.payments_missed=0, a.arrearage=0 " +
											"where a.installment_no='"+instNo+"' ");

							rowsUpdated = updateStmt.executeUpdate();
							if (rowsUpdated != 1){
								System.out.println("....update error on  INSTALLMENT for update of payments_missed");
								// if insert failed - rollback to
								// release any locks
								needToRollback = true;
								returnCode = OPERATION_FAILED;
							}
						}

					}
				}
				// ***********************
				// retrieve dynamically created parameters
				paramMap = request.getParameterMap();
				iter = (paramMap.entrySet()).iterator();

				// close insert statement to be reused
				insertStmt.close();
				insertStmt = null;

				if (selectStmt != null){
					selectStmt.close();
					selectStmt = null;
				}
				// get the account number from the FEE_SCHEDULE
				selectStmt = conn.prepareStatement(
						"SELECT FEE_SCHEDULE.account_no        " +
								"FROM FEE_SCHEDULE                     " +
								"WHERE fee_group Is Null               " +
								"AND fee_type = " + "'" + "WAIVE" + "'");

				rset = selectStmt.executeQuery();

				if (!(rset.next())) {
					// problem with retrieving account_no
					System.out.println("Failed to read account_no");
					returnCode = OPERATION_FAILED;
				} else {
					waiveAccNo = rset.getString(1).trim();

					//System.out.println("The account_no is= " + waiveAccNo);
					sqlString = "INSERT INTO PAYBREAK (  " +
							"fee_fine_no,            " +
							"payment_no,             " +
							"amount,                 " +
							"account_no,             " +
							"last_updated_by,        " +
							"last_updated_datetime,  " +
							"last_updated_from)      " +
							"VALUES (?, ?, ?, ?, ?, ?, ?)";
					insertStmt = conn.prepareStatement(sqlString);
					//System.out.println("Test after the insert statement in PAYBREAK");
					while (iter.hasNext()) {
						me = (Map.Entry)iter.next();
						String param = (String)me.getKey();
						String value[] = (String[])me.getValue();
						rowsInserted = 0;

						//System.out.println("String param is "+ param);

						// if request parameter  has feeXXX format (XXX is fee_fine_no)
						// and value for this parameter != ""

						if ( ("fee".equals(param.substring(0,3))) && (!"".equals(value[0])) ) {
							String feeNo = param.substring(3);
							//System.out.println("fee number is "+ feeNo);

							// close ResultSet and statement to be reused
							//System.out.println("Test!" + caseCategory);

							insertStmt.setInt(1, Integer.parseInt(feeNo));
							insertStmt.setInt(2, paymentNumber);
							insertStmt.setDouble(3, Double.parseDouble(value[0]));
							insertStmt.setString(4, waiveAccNo); //all get the same account number for the waive!!!
							if(request.getParameter("received_by") != null && !(request.getParameter("received_by").toString().equals("")))
								insertStmt.setString(5, request.getParameter("received_by").toString());
							else
								insertStmt.setString(5, (String)session.getAttribute("user_name"));
							insertStmt.setTimestamp(6, Timestamp.valueOf(sdf2.format(dt)));
							insertStmt.setString(7, ipAdd);

							rowsInserted = insertStmt.executeUpdate();

							if (rowsInserted != 1) {
								System.out.println("Insert into table PAYBREAK failed");
								returnCode = OPERATION_FAILED;

								// leave while loop
								break;
							}

						}
					}
				}
			}

			DecimalFormat df1 = new DecimalFormat("#,##0.00");
			if (((String)session.getAttribute("user_clerk")).equals("COL") ){
				if(request.getParameter("received_by") != null && !(request.getParameter("received_by").toString().equals(""))){
					//do nothing, we don't want to create debtor notes of payments that happened already
				}else{
					if (needToRollback == false) {
						if (insertStmt != null)
							insertStmt.close();
						rowsInserted=0;

						insertStmt = conn.prepareStatement(
								"INSERT INTO DEBTORS_NOTES (   " +
										"person_no,          		" +
										"subtype,           		" +
										"description,			" +
										"entered_date,     		" +
										"entered_by, 			" +
										"entered_from)      		" +
										"VALUES              	" +
								"(?, ?, ?, ?, ?, ?)");
						insertStmt.setInt(1,Integer.parseInt((String)session.getAttribute("person_no")));
						insertStmt.setString(2,"PYMT");
						String reason = "";
						String commentReference = "";
						if (!"".equals(method))
							reason = " | Reason: "+method;
						if (!"".equals(description))
							commentReference = " | Comment:"+description;
						insertStmt.setString(3,"Adjustment CREDIT of -$"+df1.format(Double.parseDouble(amount))+reason+commentReference);
						insertStmt.setTimestamp(4, Timestamp.valueOf(sdf2.format(dt)));
						insertStmt.setString(5, (String)session.getAttribute("user_name"));
						insertStmt.setString(6, ipAdd);

						rowsInserted = insertStmt.executeUpdate();

						if (rowsInserted != 1){
							System.out.println("Insert into table DEBTORS_NOTES for the waive payment");
							// if insert failed - rollback to
							// release any locks
							needToRollback = true;
							returnCode = OPERATION_FAILED;
						}


					}
				}
			}
		}

		catch (NumberFormatException e) {
			// parseDouble() throws this exception
			System.out.println("Caught NumberFormatException in PaymentBean.processWaivefee()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "NumberFormatException caught");
			returnCode = DATABASE_EXCEPTION;
		}

		catch (ParseException e) {
			// parse() throws this exception
			System.out.println("Caught ParseException in PaymentBean.processWaivefee()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "ParseException caught");
			returnCode = DATABASE_EXCEPTION;
		}
		catch (SQLException e) {
			// preparedStatement(), executeUpdate() throw this exception
			System.out.println("Caught SQLException in PaymentBean.processWaivefee()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "SQLException caught");
			returnCode = DATABASE_EXCEPTION;
		}

		finally {
			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.processWaivefee().");
			try {
				//rset.close was commented out
				if (rset != null){    // Close statements
					rset.close();
					rset = null;
				}

				if (selectStmt != null){
					selectStmt.close();
					selectStmt = null;
				}
				if (rset2 != null){    // Close statements
					rset2.close();
					rset2 = null;
				}

				if (selectStmt2 != null){
					selectStmt2.close();
					selectStmt2 = null;
				}
				if (insertStmt != null){
					insertStmt.close();
					insertStmt = null;
				}
				if (updateStmt != null){
					updateStmt.close();
					updateStmt = null;
				}
				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();

					conn.close();
					conn = null;
				}

				return returnCode;
			}
			catch (SQLException e) {
				// close() throw this exception
				System.out.println("Caught SQLException in PaymentBean.processWaivefee()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// inserts took place
				return returnCode;
			}
		}
	}

	/**
	 * processes and stores waivefee information in the database
	 * <p>
	 * @param HTTP request as <code>HttpServletRequest</code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="PaymentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: data is stored in the database </i>
	 */

	public int processWaivefeeTest(String partyTo, String caseCategory, HttpServletRequest request)
	{
		methodName = "processWaivefeeTest(String partyTo, String caseCategory, HttpServletRequest request)";
		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int rowsInserted = 0;
		HttpSession session = null;
		Map paramMap = null;
		Iterator iter = null;
		Map.Entry me = null;
		String tmp[] = null;
		String feeGroup = "";
		String feeType = "";
		String feeNo = "";
		String str = "";
		String category = "";
		String waivefeeAccNo = "";
		int feeFineNumber = 0;

		if (!"CRIMINAL".equals(caseCategory))
			category = "V";
		if ("CRIMINAL".equals(caseCategory))
			category = "C";

		Connection conn = null;
		PreparedStatement insertStmt = null;
		PreparedStatement selectStmt = null;
		ResultSet rset = null;

		String ipAdd = request.getRemoteAddr();

		rollbackDone rbd = null;
		try {
			conn = getConnection(jndiResource);

			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection

			// get current session based on HttpServletRequest
			// and don't create a new on (create=false)
			session = request.getSession(false);

			// get case_id and sequence_no from session object
			caseId = Integer.parseInt((String)session.getAttribute("case_id"));
			sequenceNumber = Integer.parseInt((String)session.getAttribute("seq_no"));

			// define format used by form
			SimpleDateFormat sdf1 = new SimpleDateFormat ("MM/dd/yyyy");
			// define format for JDBC Timestamp
			SimpleDateFormat sdf2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

			userName = (String)session.getAttribute("user_name");

			java.util.Date dt = new java.util.Date();

			// get the account number from the FEE_SCHEDULE
			selectStmt = conn.prepareStatement(
					"SELECT FEE_SCHEDULE.account_no        " +
							"FROM FEE_SCHEDULE                     " +
							"WHERE fee_group Is Null               " +
							"AND fee_type = " + "'" + "WAIVE" + "'");

			rset = selectStmt.executeQuery();

			if (!(rset.next())) {
				// problem with retrieving account_no
				System.out.println("Failed to read account_no");
				needToRollback = true;
				returnCode = OPERATION_FAILED;
			} else {
				waivefeeAccNo = rset.getString(1).trim();

				//System.out.println("The account_no is= " + waivefeeAccNo);

				rowsInserted = 0;

				sqlString = "INSERT INTO FEES_FINES (  " +
						"case_id,                  " +
						"sequence_no,              " +
						"fee_group,                " +
						"fee_type,                 " +
						"account_no,               " +
						"party_responsible,        " +
						"amount,                   " +
						"description,              " +
						"date_ordered,             " +
						"date_due,                 " +
						"last_updated_by,          " +
						"last_updated_datetime,    " +
						"last_updated_from)        " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				insertStmt = conn.prepareStatement(sqlString);

				// set parameters for INSERT query
				insertStmt.setInt(1, caseId);
				insertStmt.setInt(2, sequenceNumber);
				insertStmt.setString(3, " ");
				insertStmt.setString(4, "WAIVE");
				insertStmt.setString(5, waivefeeAccNo);
				insertStmt.setInt(6, Integer.parseInt(partyTo));
				insertStmt.setDouble(7, -Double.parseDouble(amount));
				insertStmt.setString(8, "Waiving the fees/fines");
				insertStmt.setTimestamp(9, Timestamp.valueOf(sdf2.format(sdf1.parse(paymentDate))));
				insertStmt.setTimestamp(10, Timestamp.valueOf(sdf2.format(sdf1.parse(paymentDate))));
				insertStmt.setString(11, (String)session.getAttribute("user_name"));
				insertStmt.setTimestamp(12, Timestamp.valueOf(sdf2.format(dt)));
				insertStmt.setString(13, ipAdd);

				rowsInserted = insertStmt.executeUpdate();

				if (rowsInserted != 1) {
					System.out.println("Insert into table FEES_FINES failed");
					// if insert failed - rollback to
					// release any locks
					needToRollback = true;
					rbd = new rollbackDone(userName, methodName, sqlString);
					returnCode = OPERATION_FAILED;
				} else {

					// insert to FEES_FINES successful
					//System.out.println("Insert to FEES_FINES successful");

					// close statement to be reused
					if (selectStmt != null){
						selectStmt.close();
						selectStmt = null;
					}
					// retrieve fee_fine_no for newly inserted fee/fine
					selectStmt = conn.prepareStatement(
							"SELECT MAX(fee_fine_no)     " +
									"FROM FEES_FINES             " +
									"WHERE case_id = ?           " +
									"AND sequence_no = ?         " +
							"AND last_updated_by = ?" );

					selectStmt.setInt(1, caseId);
					selectStmt.setInt(2, sequenceNumber);
					selectStmt.setString(3, (String)session.getAttribute("user_name"));

					rset = selectStmt.executeQuery();

					if (rset.next()) {
						feeFineNumber = rset.getInt(1);
						//System.out.println("The fee_fine_no is= " + feeFineNumber);

					} else {
						// problem with retrieving fee_fine_no
						System.out.println("Failed to retrieve fee_fine_no");
						needToRollback = true;
						returnCode = OPERATION_FAILED;
					}
				}
			}

			// if no problems reported (needToRollback == false)
			// continue with inserting to PAYMENTS
			if (needToRollback == false) {

				// close statement to be reused
				if (insertStmt != null){
					insertStmt.close();
					insertStmt = null;
				}
				//if (selectStmt != null)
				//selectStmt.close();

				rowsInserted = 0;

				// insert row in PAYMENTS
				sqlString = "INSERT INTO PAYMENTS ( " +
						"case_id,               " +
						"sequence_no,           " +
						"amount,                " +
						"payment_date,          " +
						"paid_by,               " +
						"description,           " +
						"last_updated_by,       " +
						"last_updated_datetime, " +
						"last_updated_from)     " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				insertStmt = conn.prepareStatement(sqlString);

				insertStmt.setInt(1, caseId);
				insertStmt.setInt(2, sequenceNumber);
				insertStmt.setDouble(3, Double.parseDouble(amount));
				insertStmt.setTimestamp(4, Timestamp.valueOf(sdf2.format(sdf1.parse(paymentDate))));
				insertStmt.setString(5, "Waive");
				insertStmt.setString(6, description);
				insertStmt.setString(7, (String)session.getAttribute("user_name"));
				insertStmt.setTimestamp(8, Timestamp.valueOf(sdf2.format(dt)));
				insertStmt.setString(9, ipAdd);


				rowsInserted = insertStmt.executeUpdate();

				if (rowsInserted != 1) {
					System.out.println("Insert into table PAYMENTS failed");
					// if insert failed - rollback to
					// release any locks
					needToRollback = true;
					rbd = new rollbackDone(userName, methodName, sqlString);
					returnCode = OPERATION_FAILED;
				} else {
					// insert to PAYMENTS successful

					// retrieve payment_no for newly inserted payment
					selectStmt = conn.prepareStatement(
							"SELECT MAX(payment_no)   " +
									"FROM PAYMENTS            " +
									"WHERE case_id = ?        " +
									"AND sequence_no = ?      " +
							"AND last_updated_by = ?" );

					selectStmt.setInt(1, caseId);
					selectStmt.setInt(2, sequenceNumber);
					selectStmt.setString(3, (String)session.getAttribute("user_name"));

					rset = selectStmt.executeQuery();

					if (rset.next()) {
						paymentNumber = rset.getInt(1);
						System.out.println("Retrieved payment_no = " + paymentNumber);

						// retrieve dynamically created parameterts
						paramMap = request.getParameterMap();
						iter = (paramMap.entrySet()).iterator();

						//close insert statement to be reused

						if (selectStmt != null){
							selectStmt.close();
							selectStmt = null;
						}
						if (insertStmt != null){
							insertStmt.close();
							insertStmt = null;
						}

						sqlString = "INSERT INTO PAYBREAK (  " +
								"fee_fine_no,            " +
								"payment_no,             " +
								"amount,                 " +
								"account_no,             " +
								"last_updated_by,        " +
								"last_updated_datetime,  " +
								"last_updated_from)      " +
								"VALUES (?, ?, ?, ?, ?, ?, ?)";
						insertStmt = conn.prepareStatement(sqlString);

						//System.out.println("Test after the insert statement in PAYBREAK");
						while (iter.hasNext()) {
							me = (Map.Entry)iter.next();
							String param = (String)me.getKey();
							String value[] = (String[])me.getValue();
							rowsInserted = 0;


							// if request parameter  has feeXXX format (XXX is fee_fine_no)
							// and value for this parameter != ""

							if ( ("wfee".equals(param.substring(0,4))) && (!"".equals(value[0])) ) {
								feeNo = param.substring(4);
								//System.out.println("fee number is "+ feeNo);

								selectStmt = conn.prepareStatement(
										"SELECT account_no                " +
												"FROM FEES_FINES    " +
										"WHERE FEES_FINES.fee_fine_no = ? " );

								selectStmt.setInt(1, Integer.parseInt(feeNo));
								//System.out.println("Test!");
								rset = selectStmt.executeQuery();

								if (!(rset.next())) {
									// problem with retrieving account_no
									System.out.println("Failed to read account_no");
									needToRollback = true;
									returnCode = OPERATION_FAILED;
								} else {

									accountNo = rset.getString(1).trim();
								}

								//System.out.println("The account_no is= " + accountNo);
								//System.out.println("The feeFineNumber is " + feeFineNumber);

								insertStmt.setInt(1, Integer.parseInt(feeNo));
								insertStmt.setInt(2, paymentNumber);
								insertStmt.setDouble(3, Double.parseDouble(value[0]));
								insertStmt.setString(4, accountNo);
								insertStmt.setString(5, (String)session.getAttribute("user_name"));
								insertStmt.setTimestamp(6, Timestamp.valueOf(sdf2.format(dt)));
								insertStmt.setString(7, ipAdd);

								//System.out.println("The feeFineNumber is test1" + feeFineNumber);
								rowsInserted = insertStmt.executeUpdate();
								//System.out.println("The feeFineNumber is test2" + feeFineNumber);

								if (rowsInserted != 1) {
									System.out.println("Insert into table PAYBREAK failed");
									returnCode = OPERATION_FAILED;

									// leave while loop
									break;
								}

							}
						}

					} else {
						// problem with retrieving payment_no
						System.out.println("Failed to retrieve payment_no");
						needToRollback = true;
						returnCode = OPERATION_FAILED;
					}
				}
			}

			// if no problems reported (needToRollback == false)
			// continue with inserting to PAYMENTS
			if (needToRollback == false) {

				// close statement to be reused
				if (insertStmt != null){
					insertStmt.close();
					insertStmt = null;
				}

				rowsInserted = 0;

				// insert row in PAYMENTS
				sqlString = "INSERT INTO PAYMENTS ( " +
						"case_id,               " +
						"sequence_no,           " +
						"amount,                " +
						"payment_date,          " +
						"paid_by,               " +
						"description,           " +
						"last_updated_by,       " +
						"last_updated_datetime, " +
						"last_updated_from)     " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				insertStmt = conn.prepareStatement(sqlString);

				insertStmt.setInt(1, caseId);
				insertStmt.setInt(2, sequenceNumber);
				insertStmt.setDouble(3, -Double.parseDouble(amount));
				insertStmt.setTimestamp(4, Timestamp.valueOf(sdf2.format(sdf1.parse(paymentDate))));
				insertStmt.setString(5, "Waive");
				insertStmt.setString(6, description);
				insertStmt.setString(7, (String)session.getAttribute("user_name"));
				insertStmt.setTimestamp(8, Timestamp.valueOf(sdf2.format(dt)));
				insertStmt.setString(9, ipAdd);

				rowsInserted = insertStmt.executeUpdate();

				if (rowsInserted != 1) {
					System.out.println("Insert into table PAYMENTS failed");
					// if insert failed - rollback to
					// release any locks
					needToRollback = true;
					rbd = new rollbackDone(userName, methodName, sqlString);
					returnCode = OPERATION_FAILED;
				} else {
					// insert to PAYMENTS successful

					// retrieve payment_no for newly inserted payment
					selectStmt = conn.prepareStatement(
							"SELECT MAX(payment_no)   " +
									"FROM PAYMENTS            " +
									"WHERE case_id = ?        " +
									"AND sequence_no = ?      " +
							"AND last_updated_by = ?" );

					selectStmt.setInt(1, caseId);
					selectStmt.setInt(2, sequenceNumber);
					selectStmt.setString(3, (String)session.getAttribute("user_name"));

					rset = selectStmt.executeQuery();

					if (rset.next()) {
						paymentNumber = rset.getInt(1);
						System.out.println("Retrieved payment_no = " + paymentNumber);

						// close statement to be reused
						if (insertStmt != null)
							insertStmt.close();

						sqlString = "INSERT INTO PAYBREAK (  " +
								"fee_fine_no,            " +
								"payment_no,             " +
								"amount,                 " +
								"account_no,             " +
								"last_updated_by,        " +
								"last_updated_datetime,  " +
								"last_updated_from)      " +
								"VALUES (?, ?, ?, ?, ?, ?, ?)";
						insertStmt = conn.prepareStatement(sqlString);

						insertStmt.setInt(1, feeFineNumber);
						insertStmt.setInt(2, paymentNumber);
						insertStmt.setDouble(3, -Double.parseDouble(amount));
						insertStmt.setString(4, waivefeeAccNo);
						insertStmt.setString(5, (String)session.getAttribute("user_name"));
						insertStmt.setTimestamp(6, Timestamp.valueOf(sdf2.format(dt)));
						insertStmt.setString(7, ipAdd);

						rowsInserted = insertStmt.executeUpdate();

						if (rowsInserted != 1) {
							System.out.println("Insert into table PAYBREAK failed");
							returnCode = OPERATION_FAILED;
						}

					}
				}
			}


		}

		catch (NumberFormatException e) {
			// parseDouble() throws this exception
			System.out.println("Caught NumberFormatException in PaymentBean.processWaivefee()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "NumberFormatException caught");
			returnCode = DATABASE_EXCEPTION;
		}

		catch (ParseException e) {
			// parse() throws this exception
			System.out.println("Caught ParseException in PaymentBean.processWaivefee()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "ParseException caught");
			returnCode = DATABASE_EXCEPTION;
		}
		catch (SQLException e) {
			// preparedStatement(), executeUpdate() throw this exception
			System.out.println("Caught SQLException in PaymentBean.processWaivefee()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "SQLException caught");
			returnCode = DATABASE_EXCEPTION;
		}

		finally {
			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.processWaivefee().");
			try {
				//rset was commented out here
				if (rset != null){    // Close statements
					rset.close();
					rset = null;
				}

				if (selectStmt != null){
					selectStmt.close();
					selectStmt = null;
				}
				if (insertStmt != null){
					insertStmt.close();
					insertStmt = null;
				}

				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();

					conn.close();
					conn = null;
				}

				return returnCode;
			}
			catch (SQLException e) {
				// close() throw this exception
				System.out.println("Caught SQLException in PaymentBean.processWaivefee()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// inserts took place
				return returnCode;
			}
		}
	}

	/**
	 * stores installment information in the database
	 * <p>
	 * @param HTTP request as <code>HttpServletRequest</code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="FeeFineBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 *   <li><code><a href="FeeFineBean.html#DUPLICATE_FEE">DUPLICATE_FEE</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: data is stored in the database </i>
	 */

	public int storeInstallment(HttpServletRequest request, int cId, int sNo)
	{
		methodName = "storeInstallment(HttpServletRequest request, int cId, int sNo)";
		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int rowsInserted = 0;
		int rowsUpdated = 0;
		HttpSession session = null;

		String str = "";
		int num = 0;
		double totalFee = 0.0;
		String feeFineNo = "";
		String feeGp = "";

		Map paramMap = null;
		Iterator iter = null;
		Map.Entry me = null;

		Connection conn = null;

		PreparedStatement insertStmt = null;
		PreparedStatement selectStmt = null;
		PreparedStatement updateStmt = null;
		ResultSet rset = null;
		String ipAdd = request.getRemoteAddr();

		rollbackDone rbd = null;
		try
		{
			conn = getConnection(jndiResource);
			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection

			session = request.getSession(false);
			caseId = Integer.parseInt((String)session.getAttribute("case_id"));
			sequenceNumber = Integer.parseInt((String)session.getAttribute("seq_no"));

			SimpleDateFormat sdf1 = new SimpleDateFormat ("MM/dd/yyyy");
			SimpleDateFormat sdf2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

			java.util.Date dt = new java.util.Date();
			userName = (String)session.getAttribute("user_name");

			paramMap = request.getParameterMap();
			iter = (paramMap.entrySet()).iterator();

			sqlString = "INSERT INTO INSTALLMENTS (  " +
					"case_id,                    " +
					"sequence_no,                " +
					"date_created,               " +
					"start_date,                 " +
					"frequency,                  " +
					"next_billing_date,          " +
					"original_amount,            " +
					"payment_amount,          " +
					"last_updated_datetime,      " +
					"last_updated_by,            " +
					"last_updated_from)          " +
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			insertStmt = conn.prepareStatement(sqlString);

			while (iter.hasNext())
			{
				me = (Map.Entry)iter.next();
				String param = (String)me.getKey();
				String value[] = (String[])me.getValue();
				if ("fee".equals(param.substring(0,3)))
				{
					totalFee += Double.parseDouble(value[0]);
					//System.out.println("Total fee amount set for installment is " + totalFee);
				}
			}

			// set parameters for INSERT query
			insertStmt.setInt(1, cId);
			insertStmt.setInt(2, sNo);
			insertStmt.setTimestamp(3, Timestamp.valueOf(sdf2.format(dt)));
			insertStmt.setTimestamp(4, Timestamp.valueOf(sdf2.format(sdf1.parse(startDate))));
			insertStmt.setString(5, frequency);
			insertStmt.setTimestamp(6, Timestamp.valueOf(sdf2.format(sdf1.parse(startDate))));
			insertStmt.setDouble(7, totalFee);
			insertStmt.setDouble(8, Double.parseDouble(paymentAmount));
			insertStmt.setTimestamp(9, Timestamp.valueOf(sdf2.format(dt)));
			insertStmt.setString(10, (String)session.getAttribute("user_name"));
			insertStmt.setString(11, ipAdd);

			rowsInserted = insertStmt.executeUpdate();

			if (rowsInserted != 1)
			{
				System.out.println("Insert into table INSTALLMENTS failed");
				// if insert failed - rollback to
				// release any locks
				needToRollback = true;
				rbd = new rollbackDone(userName, methodName, sqlString);
				returnCode = OPERATION_FAILED;

			}
			else
			{

				// insert to INSTALLMENTS successful

				// retrieve installment_no for newly inserted installment record
				selectStmt = conn.prepareStatement(
						"SELECT MAX(installment_no)     " +
								"FROM INSTALLMENTS              " +
								"WHERE case_id = ?              " +
								"AND sequence_no = ?            " +
						"AND last_updated_by = ?" );

				selectStmt.setInt(1, cId);
				selectStmt.setInt(2, sNo);
				selectStmt.setString(3, (String)session.getAttribute("user_name"));

				rset = selectStmt.executeQuery();

				if (rset.next())
				{
					installmentNumber = rset.getInt(1);
					//System.out.println("Retrieved installment_no = " + installmentNumber);

					// retrieve dynamically created parameterts
					paramMap = request.getParameterMap();
					iter = (paramMap.entrySet()).iterator();

					//Update FEES_FINES record to reflect the installment.
					updateStmt = conn.prepareStatement(
							"update fees_fines"
									+ " set installments=?, last_updated_datetime=?, last_updated_by=?, last_updated_from=?"
									+ " where fee_fine_no=?");
					while (iter.hasNext())
					{
						me = (Map.Entry)iter.next();
						String param = (String)me.getKey();
						//System.out.println("Param is " + param);
						if ("fee".equals(param.substring(0,3)))
						{
							feeFineNo = param.substring(3);
							//System.out.println("FeeFineNo is " + feeFineNo);
							updateStmt.setInt(1, installmentNumber);
							updateStmt.setTimestamp(2, Timestamp.valueOf(sdf2.format(dt)));
							updateStmt.setString(3, (String)session.getAttribute("user_name"));
							updateStmt.setString(4, ipAdd);
							updateStmt.setInt(5, Integer.parseInt(feeFineNo));
							rowsUpdated = updateStmt.executeUpdate();
							if (rowsUpdated < 1)
							{
								System.out.println("Update in table FEES_FINES failed");
								needToRollback = true;
								returnCode = OPERATION_FAILED;
							}
							if (needToRollback == false)
							{
								//Read fees fines record to check for group fee and update the group fee entry.
								selectStmt = conn.prepareStatement(
										"select fee_group"
												+ " from fees_fines"
												+ " where fee_fine_no=?");
								selectStmt.setInt(1, Integer.parseInt(feeFineNo));
								rset = selectStmt.executeQuery();
								if (rset.next())
								{
									str = rset.getString(1);
									if (str != null)
										feeGp = str.trim();
								}
								else
								{
									System.out.println("Failed to read FEES_FINES.");
									needToRollback = true;
									returnCode = OPERATION_FAILED;
								}
							}
						}
					}
					if ((needToRollback == false) && (feeGp != ""))
					{
						if (updateStmt != null){
							updateStmt.close();
							updateStmt = null;
						}

						//Update FEES_FINES record of feetype BCC to reflect the installment.
						updateStmt = conn.prepareStatement(
								"update fees_fines"
										+ " set installments=?, last_updated_datetime=?, last_updated_by=?, last_updated_from=?"
										+ " where case_id=? and sequence_no=? and group_record=?");
						updateStmt.setInt(1, installmentNumber);
						updateStmt.setTimestamp(2, Timestamp.valueOf(sdf2.format(dt)));
						updateStmt.setString(3, (String)session.getAttribute("user_name"));
						updateStmt.setString(4, ipAdd);
						updateStmt.setInt(5, cId);
						updateStmt.setInt(6, sNo);
						updateStmt.setString(7, "1");

						rowsUpdated = updateStmt.executeUpdate();
						if (rowsUpdated < 1)
						{
							System.out.println("Update in table FEES_FINES failed");
							// if update failed - rollback to
							// release any locks
							needToRollback = true;
							returnCode = OPERATION_FAILED;
						}
					}
				}
				else
				{
					System.out.println("Failed to retrieve installment_no");
					needToRollback = true;
					returnCode = OPERATION_FAILED;
				}
			}
		}
		catch (NumberFormatException e)
		{
			System.out.println("Caught NumberFormatException in InstallmentBean.storeInstallment()!");
			System.out.println(e);
			e.printStackTrace();
			isDataSubmitted = false;
			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "NumberFormatException caught");
			returnCode = DATABASE_EXCEPTION;
		}
		catch (ParseException e)
		{
			System.out.println("Caught ParseException in InstallmentBean.storeInstallment()!");
			System.out.println(e);
			e.printStackTrace();
			isDataSubmitted = false;
			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "ParseException caught");
			returnCode = DATABASE_EXCEPTION;
		}
		catch (SQLException e)
		{
			System.out.println("Caught SQLException in InstallmentBean.storeInstallment()!");
			System.out.println(e);
			e.printStackTrace();
			isDataSubmitted = false;
			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "SQLException caught");
			returnCode = DATABASE_EXCEPTION;
		}
		finally
		{
			System.out.println("Statement/ResultSet related cleanup in InstallmentBean.storeInstallment().");
			try
			{
				//did not have rset close
				if (rset != null){
					rset.close();
					rset = null;
				}

				if (selectStmt != null){
					selectStmt.close();
					selectStmt = null;
				}
				if (insertStmt != null){
					insertStmt.close();
					insertStmt = null;
				}
				if ((conn != null) && (!conn.isClosed()))
				{
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					conn.close();
					conn = null;
				}
				if (updateStmt != null){
					updateStmt.close();
					updateStmt = null;
				}

				return returnCode;
			}
			catch (SQLException e)
			{
				System.out.println("Caught SQLException in InstallmentBean.storeInstallment()!");
				System.out.println(e);
				return returnCode;
			}
		}
	}

	/**
	 * reads installment information from the database
	 * <p>
	 * @param installment no is read as <code>int</code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="PaymentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 *   <li><code><a href="PaymentBean.html#NO_DATA_FOUND">NO_DATA_FOUND</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: installment data is read from the database
	 *               with focus on installment display </i>
	 */

	public int readInstallment(int installNo)
	{

		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int i = 0;
		String str = "";
		double num = 0.0;
		double num2 = 0.0;
		String mos = "";
		Timestamp ts = null;

		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet rset = null;

		try
		{
			reset();
			conn = getConnection(jndiResource);
			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection


			SimpleDateFormat sdf = new SimpleDateFormat ("MM/dd/yyyy");
			DecimalFormat df = new DecimalFormat ("#,##0.00");
			DecimalFormat df1 = new DecimalFormat ("#0.00");

			selectStmt = conn.prepareStatement(
					"select start_date, frequency, last_billing_date, next_billing_date"
							+ ", original_amount, ytd_billed_amount, payment_amount"
							+ ", (select sum(payment_amount)"
							+ " from installment_charge"
							+ " where installment_no=installments.installment_no)"
							+ " from installments"
							+ " where installment_no=?");
			selectStmt.setInt(1,installNo);
			rset = selectStmt.executeQuery();
			if (rset.next())
			{
				ts = rset.getTimestamp(1);
				if (ts != null)
					startDate = sdf.format(ts);
				str = rset.getString(2);
				if (str != null)
					frequency = str.trim();
				ts = rset.getTimestamp(3);
				if (ts != null)
					lastBillingDate = sdf.format(ts);
				ts = rset.getTimestamp(4);
				if (ts != null)
					nextBillingDate = sdf.format(ts);
				orgAmount = df1.format(rset.getDouble(5));

				String firstTwo = startDate.substring(0,2);
				String midTwo = startDate.substring(3,5);
				String lastFour = startDate.substring(startDate.length()-4,startDate.length());
				// capture today's date
				java.util.Date dt = new java.util.Date();
				str = sdf.format(dt);

				String tdy2 = str.substring(0,2);
				String tdyMid = str.substring(3,5);
				String tdy4 = str.substring(str.length()-4,str.length());

				if (Integer.parseInt(tdy2) > Integer.parseInt(firstTwo)){
					str = "";
					str = String.valueOf(Integer.parseInt(tdy4) - Integer.parseInt(lastFour));
					mos = String.valueOf(Integer.parseInt(str) * 12);
					str = "";
					str = String.valueOf(Integer.parseInt(tdy2) - Integer.parseInt(firstTwo));
					mos = String.valueOf(Integer.parseInt(str) + Integer.parseInt(mos));
				}
				else if (Integer.parseInt(tdy2) < Integer.parseInt(firstTwo)){
					str = "";
					str = String.valueOf(Integer.parseInt(tdy4) - Integer.parseInt(lastFour));
					mos = String.valueOf(Integer.parseInt(str) * 12);
					str = "";
					str = String.valueOf(Integer.parseInt(firstTwo) - Integer.parseInt(tdy2));
					mos = String.valueOf(Integer.parseInt(mos) - Integer.parseInt(str));
				}
				else if	(Integer.parseInt(tdy2) == Integer.parseInt(firstTwo)){
					str = "";
					str = String.valueOf(Integer.parseInt(tdy4) - Integer.parseInt(lastFour));
					mos = String.valueOf(Integer.parseInt(str) * 12);
				}
				if (Integer.parseInt(tdyMid) < Integer.parseInt(midTwo)){
					mos = String.valueOf(Integer.parseInt(mos) -1);
				}
				// ytdAmount = df.format(rset.getDouble(6));
				paymentAmount = df.format(rset.getDouble(7));
				num = Double.parseDouble(mos) * Double.parseDouble(paymentAmount);
				//System.out.println("Ytd Amount due is "+ num);

				num2 = Double.parseDouble(orgAmount);
				//System.out.println("Original Amount of installment is "+ num2);
				if (num > num2){
					num = num2;
					//System.out.println("Year to date is greater than FULL amount");
				}
				ytdAmount = df1.format(num);
				//System.out.println("year to date amount  " + ytdAmount);
				num2 = 0.0;
				num2 = rset.getDouble(8);
				//System.out.println("Payments applied to the INSTALLMENT  " + num2);
				chargedOutstandingBalance = num - num2;
				//System.out.println("CHARGED OUTSTANDING BALANCE IN INSTALLMENT IS  " +chargedOutstandingBalance);
			}
			else
			{
				System.out.println("Failed to read from table INSTALLMENTS");
				returnCode = OPERATION_FAILED;
				needToRollback = true;
			}
		}
		catch (SQLException e)
		{
			System.out.println("Caught SQLException in InstallmentBean.readInstallments()!");
			System.out.println(e);
			e.printStackTrace();
			isDataSubmitted = false;
			needToRollback = true;
			returnCode = DATABASE_EXCEPTION;
		}
		finally
		{
			System.out.println("Statement/ResultSet related cleanup in InstallmentBean.readInstallments().");
			try
			{
				//did not have rset.close at all
				if (rset != null){
					rset.close();
					rset = null;
				}

				if (selectStmt != null){
					selectStmt.close();
					selectStmt = null;
				}
				if ((conn != null) && (!conn.isClosed()))
				{
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					conn.close();
					conn = null;
				}
				return returnCode;
			}
			catch (SQLException e)
			{
				System.out.println("Caught SQLException in InstallmentBean.readInstallments()!");
				System.out.println(e);
				return returnCode;
			}
		}
	}

	/**
	 * reads all installments from the database
	 * <p>
	 * @param ID of the case installments are read as <code>int</code>
	 * @param sequence number of the case installments are read as <code>int</code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="PaymentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 *   <li><code><a href="PaymentBean.html#NO_DATA_FOUND">NO_DATA_FOUND</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: installment data is read from the database
	 *               with focus on payments history </i>
	 */
	public int readAllInstallments(int csId, int seqNo)
	{
		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int i = 0;
		double num = 0.0;
		double num2 = 0.0;
		String str = "";
		String mos = "";
		Timestamp ts = null;


		String[][] tmpArray = new String[MAX_SIZE][10];

		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet rset = null;
		PreparedStatement selectStmt2 = null;
		ResultSet rset2 = null;

		try
		{
			reset();
			conn = getConnection(jndiResource);
			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection

			SimpleDateFormat sdf = new SimpleDateFormat ("MM/dd/yyyy");
			DecimalFormat df = new DecimalFormat ("###0.00");
			DecimalFormat df1 = new DecimalFormat ("#0.00");

			selectStmt2 = conn.prepareStatement(""
					+ "select installment_no from installment_cases where case_id = ?");
			selectStmt2.setInt(1, csId);
			rset2 = selectStmt2.executeQuery();
			while(rset2.next()){



				selectStmt = conn.prepareStatement (
						"select installment_no, start_date, installments.frequency, last_billing_date"
								+ ", next_billing_date, original_amount, ytd_billed_amount, payment_amount"
								+ ", (select sum(payment_amount)"
								+ " from installment_charge"
								+ " where installment_no=installments.installment_no)"
								+ ", (select distinct party_responsible"
								+ " from fees_fines"
								+ " where installments=INSTALLMENTS.installment_no)"
								+ " from installments"
								+ " where installments.installment_no=? and installments.sequence_no=?"
								+ " order by start_date");
				selectStmt.setInt(1,rset2.getInt(1));
				selectStmt.setInt(2,seqNo);
				rset = selectStmt.executeQuery();
				//System.out.println(selectStmt.toString());
				while ((rset.next()) && (i<MAX_SIZE))
				{
					//installment number
					tmpArray[i][0] = String.valueOf(rset.getInt(1));
					//start date
					ts = rset.getTimestamp(2);
					tmpArray[i][1] = sdf.format(ts);
					if (ts != null)
						startDate = sdf.format(ts);
					//System.out.println("START DATE IN READ ALL INSTALLMENT IS "+ startDate);
					//frequency
					str = rset.getString(3);
					if (str != null)
						tmpArray[i][2] = str.trim();
					//last billing date
					ts = rset.getTimestamp(4);
					if (ts != null)
						tmpArray[i][3] = sdf.format(ts);
					else
						tmpArray[i][3] = " ";
					//next billing date
					ts = rset.getTimestamp(5);
					if (ts != null)
						tmpArray[i][4] = sdf.format(ts);
					else
						tmpArray[i][4] = " ";
					//original amount
					orgAmount = df1.format(rset.getDouble(6));
					tmpArray[i][5] = orgAmount;
					//System.out.println("Original Amount in the Read All Installment "+ orgAmount);

					String firstTwo = startDate.substring(0,2);
					String midTwo = startDate.substring(3,5);
					String lastFour = startDate.substring(startDate.length()-4,startDate.length());
					// capture today's date
					java.util.Date dt = new java.util.Date();
					str = sdf.format(dt);

					String tdy2 = str.substring(0,2);
					String tdyMid = str.substring(3,5);
					String tdy4 = str.substring(str.length()-4,str.length());

					if (Integer.parseInt(tdy2) > Integer.parseInt(firstTwo)){
						str = "";
						str = String.valueOf(Integer.parseInt(tdy4) - Integer.parseInt(lastFour));
						mos = String.valueOf(Integer.parseInt(str) * 12);
						str = "";
						str = String.valueOf(Integer.parseInt(tdy2) - Integer.parseInt(firstTwo));
						mos = String.valueOf(Integer.parseInt(str) + Integer.parseInt(mos));
					}
					else if (Integer.parseInt(tdy2) < Integer.parseInt(firstTwo)){
						str = "";
						str = String.valueOf(Integer.parseInt(tdy4) - Integer.parseInt(lastFour));
						mos = String.valueOf(Integer.parseInt(str) * 12);
						str = "";
						str = String.valueOf(Integer.parseInt(firstTwo) - Integer.parseInt(tdy2));
						mos = String.valueOf(Integer.parseInt(mos) - Integer.parseInt(str));
					}
					else if	(Integer.parseInt(tdy2) == Integer.parseInt(firstTwo)){
						str = "";
						str = String.valueOf(Integer.parseInt(tdy4) - Integer.parseInt(lastFour));
						mos = String.valueOf(Integer.parseInt(str) * 12);
					}
					if (Integer.parseInt(tdyMid) < Integer.parseInt(midTwo)){
						mos = String.valueOf(Integer.parseInt(mos) -1);
					}

					//System.out.println("Month's since the start of installment"+ mos);
					//payment amount
					paymentAmount = df.format(rset.getDouble(8));
					tmpArray[i][7] = paymentAmount;
					//System.out.println("Payment amount in the installment is "+ paymentAmount);

					num = Double.parseDouble(mos) * Double.parseDouble(paymentAmount);
					//System.out.println("Ytd Amount due is "+ num);
					num2 = Double.parseDouble(orgAmount);
					//System.out.println("Original Amount of installment is "+ num2);
					if (num > num2){
						num = num2;
						//System.out.println("Year to date is greater than FULL amount");
					}
					//ytd_billed_amount (IGNORE because it's incorrect)
					tmpArray[i][6] = df.format(num);

					//sum of payments made so far
					num2 = 0.0;
					num2 = rset.getDouble(9);
					//System.out.println("Payments applied to the INSTALLMENT  " + num2);
					chargedOutstandingBalance = num - num2;
					//System.out.println("CHARGED OUTSTANDING BALANCE IN INSTALLMENT IS  " +chargedOutstandingBalance);
					tmpArray[i][8] = df.format(chargedOutstandingBalance);
					//Party Responsible
					tmpArray[i][9] = String.valueOf(rset.getInt(10));
					//System.out.println("PartyResponsible IN INSTALLMENT IS  " +(tmpArray[i][9]));
					i++;
				}
			}
			//System.out.println("Found installments "+i);
			if (i > 0)
			{
				// create paymentsArray
				installmentsArray = new String[i][10];
				for (int j=0; j<i; j++)
					for (int k=0; k<10; k++)
						installmentsArray[j][k] = tmpArray[j][k];
			}
			else
			{
				returnCode = NO_DATA_FOUND;
			}
		}
		catch (SQLException e)
		{
			System.out.println("Caught SQLException in InstallmentBean.readAllInstallments()!");
			System.out.println(e);
			e.printStackTrace();
			isDataSubmitted = false;
			needToRollback = true;
			returnCode = DATABASE_EXCEPTION;
		}
		finally
		{
			tmpArray = null;

			System.out.println("Statement/ResultSet related cleanup in InstallmentBean.readAllInstallments().");
			try
			{
				if (rset != null){
					rset.close();
					rset = null;
				}

				if (selectStmt != null){
					selectStmt.close();
					selectStmt = null;
				}
				if (rset2 != null){
					rset2.close();
					rset2 = null;
				}

				if (selectStmt2 != null){
					selectStmt2.close();
					selectStmt2 = null;
				}

				if ((conn != null) && (!conn.isClosed()))
				{
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					conn.close();
					conn = null;
				}
				return returnCode;
			}
			catch (SQLException e)
			{
				System.out.println("Caught SQLException in InstallmentBean.readAllInstallments()!");
				System.out.println(e);
				return returnCode;
			}
		}
	}

	/**
	 * updates installment information in the database
	 * <p>
	 * @param HTTP request as <code>HttpServletRequest</code>
	 * @param installment number as <code>int</code>
	 * @param code as <code><a href="CodeBean.html>CodeBean</a></code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="InstallmentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: updates installment data in the database
	 *               </i>
	 */
	public int updateInstallment(HttpServletRequest request, int csId,
			int seqNo, int installNo, String orgAmt, CodeBean codes)
	{
		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int rowsUpdated = 0;
		HttpSession session = null;
		double totalAmount = 0.0;
		String feeFineNo = "";
		String feeGp = "";
		String str = "";

		Map paramMap = null;
		Iterator iter = null;
		Map.Entry me = null;

		Connection conn = null;
		PreparedStatement updateStmt = null;
		PreparedStatement selectStmt = null;
		ResultSet rset = null;

		String ipAdd = request.getRemoteAddr();

		try
		{
			conn = getConnection(jndiResource);
			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection

			session = request.getSession(false);
			caseId = Integer.parseInt((String)session.getAttribute("case_id"));
			sequenceNumber = Integer.parseInt((String)session.getAttribute("seq_no"));

			SimpleDateFormat sdf1 = new SimpleDateFormat ("MM/dd/yyyy");
			SimpleDateFormat sdf2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
			java.util.Date dt = new java.util.Date();

			paramMap = request.getParameterMap();
			iter = (paramMap.entrySet()).iterator();

			totalAmount = Double.parseDouble(orgAmt);

			updateStmt = conn.prepareStatement(
					"update installments"
							+ " set start_date=?, frequency=?, next_billing_date=?, original_amount=?"
							+ ", payment_amount=?, last_updated_datetime=?, last_updated_by=?, last_updated_from=?"
							+ " where installment_no=?");

			while (iter.hasNext())
			{
				me = (Map.Entry)iter.next();
				String param = (String)me.getKey();
				String value[] = (String[])me.getValue();
				if ("fee".equals(param.substring(0,3)))
				{
					totalAmount = totalAmount + Double.parseDouble(value[0]);
					//System.out.println("Total fee amount set for installment is " + totalAmount);
				}
			}

			// set parameters for update statement
			//System.out.println("Start date " + startDate);
			if (startDate != "")
				updateStmt.setTimestamp(1, Timestamp.valueOf(sdf2.format(sdf1.parse(startDate))));
			else
				updateStmt.setTimestamp(1, null);

			//System.out.println("The frequency is " + frequency);
			//System.out.println("The frequency Description is " + frequencyDesc);
			if ("0".equals(frequency))
			{
				if (!"".equals(frequencyDesc))
					updateStmt.setString(2, (String)(codes.getPaymentFrequencyRevMap()).get(frequencyDesc));
				else
					updateStmt.setNull(2, Types.VARCHAR);
			}
			else
			{
				updateStmt.setString(2, frequency);
			}
			//System.out.println("Last Billing date " + lastBillingDate);
			//System.out.println("Next Billing date " + nextBillingDate);
			if (nextBillingDate != "")
			{
				updateStmt.setTimestamp(3, Timestamp.valueOf(sdf2.format(sdf1.parse(nextBillingDate))));
				//System.out.println("Next Billing date " + Timestamp.valueOf(sdf2.format(sdf1.parse(nextBillingDate))));
			}
			else
			{
				updateStmt.setTimestamp(3, null);
			}


			//System.out.println("Original Amount " + orgAmount);
			updateStmt.setDouble(4, totalAmount);

			//System.out.println("Payment Amount " + paymentAmount);
			updateStmt.setDouble(5, Double.parseDouble(paymentAmount));

			updateStmt.setTimestamp(6, Timestamp.valueOf(sdf2.format(dt)));
			updateStmt.setString(7, (String)session.getAttribute("user_name"));
			updateStmt.setString(8, ipAdd);
			updateStmt.setInt(9, installNo);

			rowsUpdated = updateStmt.executeUpdate();


			if (rowsUpdated != 1)
			{
				System.out.println("Update in table INSTALLMENTS failed");
				// if update failed - rollback to
				// release any locks
				needToRollback = true;
				returnCode = OPERATION_FAILED;
			}
			else
			{
				System.out.println("Finished Update in table INSTALLMENTS");

				// retrieve dynamically created parameterts
				paramMap = request.getParameterMap();
				iter = (paramMap.entrySet()).iterator();

				//Update FEES_FINES record to reflect the installment.
				updateStmt = conn.prepareStatement(
						"update fees_fines"
								+ " set installments=?, last_updated_datetime=?, last_updated_by=?, last_updated_from=?"
								+ " where fee_fine_no=?");
				while (iter.hasNext())
				{
					me = (Map.Entry)iter.next();
					String param = (String)me.getKey();
					if ("fee".equals(param.substring(0,3)))
					{
						feeFineNo = param.substring(3);
						//System.out.println("FeeFineNo is " + feeFineNo);
						updateStmt.setInt(1, installNo);
						updateStmt.setTimestamp(2, Timestamp.valueOf(sdf2.format(dt)));
						updateStmt.setString(3, (String)session.getAttribute("user_name"));
						updateStmt.setString(4, ipAdd);
						updateStmt.setInt(5, Integer.parseInt(feeFineNo));
						rowsUpdated = updateStmt.executeUpdate();
						if (rowsUpdated < 1)
						{
							System.out.println("Update in table FEES_FINES failed");
							needToRollback = true;
							returnCode = OPERATION_FAILED;
						}
						if (needToRollback == false)
						{
							//Read fees fines record to check for group fee and update the group fee entry.
							selectStmt = conn.prepareStatement(
									"select fee_group"
											+ " from fees_fines"
											+ " where fee_fine_no=?");
							selectStmt.setInt(1, Integer.parseInt(feeFineNo));
							rset = selectStmt.executeQuery();
							if (rset.next())
							{
								str = rset.getString(1);
								if (str != null)
									feeGp = str.trim();
							}
							else
							{
								System.out.println("Failed to read FEES_FINES.");
								needToRollback = true;
								returnCode = OPERATION_FAILED;
							}
						}
					}
				}
				if ((needToRollback == false) && (feeGp != ""))
				{
					if (updateStmt != null)
						updateStmt.close();
					//Update FEES_FINES record of feetype BCC to reflect the installment.
					updateStmt = conn.prepareStatement(
							"update fees_fines"
									+ " set installments=?, last_updated_datetime=?, last_updated_by=?, last_updated_from=?"
									+ " where case_id=? and sequence_no=? and group_record=?");
					updateStmt.setInt(1, installNo);
					updateStmt.setTimestamp(2, Timestamp.valueOf(sdf2.format(dt)));
					updateStmt.setString(3, (String)session.getAttribute("user_name"));
					updateStmt.setString(4, ipAdd);
					updateStmt.setInt(5, csId);
					updateStmt.setInt(6, seqNo);
					updateStmt.setString(7, "1");
					rowsUpdated = updateStmt.executeUpdate();
					if (rowsUpdated < 1)
					{
						System.out.println("Update in table FEES_FINES failed");
						needToRollback = true;
						returnCode = OPERATION_FAILED;
					}
				}
			}
		}
		catch (NumberFormatException e)
		{
			System.out.println("Caught NumberFormatException in InstallmentBean.updateInstallment()!");
			System.out.println(e);
			e.printStackTrace();
			isDataSubmitted = false;
			needToRollback = true;
			returnCode = DATABASE_EXCEPTION;
		}
		catch (ParseException e)
		{
			System.out.println("Caught ParseException in InstallmentBean.updateInstallment()!");
			System.out.println(e);
			e.printStackTrace();
			isDataSubmitted = false;
			returnCode = DATABASE_EXCEPTION;
		}
		catch (SQLException e)
		{
			System.out.println("Caught SQLException in InstallmentBean.updateInstallment()!");
			System.out.println(e);
			e.printStackTrace();
			isDataSubmitted = false;
			needToRollback = true;
			returnCode = DATABASE_EXCEPTION;
		}
		finally
		{
			System.out.println("Statement/ResultSet related cleanup in InstallmentBean.updateInstallment().");
			try
			{
				//did not have rset.close or selectstmt close
				if (rset != null){
					rset.close();
					rset = null;
				}

				if (selectStmt != null){
					selectStmt.close();
					selectStmt = null;
				}

				if (updateStmt != null){
					updateStmt.close();
					updateStmt = null;
				}
				if ((conn != null) && (!conn.isClosed()))
				{
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					conn.close();
					conn = null;

				}
				return returnCode;
			}
			catch (SQLException e)
			{
				System.out.println("Caught SQLException in InstallmentBean.updateInstallment()!");
				System.out.println(e);
				needToRollback = true;
				return returnCode;
			}
		}
	}

	/**
	 * processes and stores installment payment information in the database
	 * <p>
	 * @param HTTP request as <code>HttpServletRequest</code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="PaymentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: data is stored in the database </i>
	 */
	public int processInstallmentPayment(int installNo, String installAmt, String paymentAmt, String partyBy, String caseCategory, String escrowAccount, HttpServletRequest request)
	{
		methodName = "processInstallmentPayment(int installNo, String installAmt, String paymentAmt, String partyBy, String caseCategory, String escrowAccount, HttpServletRequest request)";
		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int rowsInserted = 0;
		int rowsUpdated = 0;
		HttpSession session = null;
		double payAmount = 0.0;
		double chargeAmount = 0.0;
		int chargeNo = 0;
		int payNo = 0;
		int tempPayNo = 0;
		double pendingAmount = 0.0;
		double currentAmt = 0.0;
		double chargedAmt = 0.0;
		String chargedAmtAsStr = "";
		double cAmt = 0.0;
		String str = "";
		int i = 0;

		Connection conn = null;
		PreparedStatement insertStmt = null;
		PreparedStatement updateStmt = null;
		PreparedStatement selectStmt = null;
		ResultSet rset = null;
		String ipAdd = request.getRemoteAddr();

		rollbackDone rbd = null;
		try
		{
			conn = getConnection(jndiResource);
			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection

			session = request.getSession(false);
			caseId = Integer.parseInt((String)session.getAttribute("case_id"));
			sequenceNumber = Integer.parseInt((String)session.getAttribute("seq_no"));

			DecimalFormat df = new DecimalFormat ("#0.00");

			SimpleDateFormat sdf1 = new SimpleDateFormat ("MM/dd/yyyy");
			SimpleDateFormat sdf2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

			java.util.Date dt = new java.util.Date();

			userName = (String)session.getAttribute("user_name");

			//System.out.println("Outstanding balance " + currentBalance);
			//System.out.println("Amount paid " + amount);
			//System.out.println("Paid By " + paidBy + " Or " + partyBy);

			amount = paymentAmt;
			processPayment(partyBy, caseCategory, escrowAccount, request);
			//System.out.println("Payment number after process payment is " + paymentNumber);
			tempPayNo = paymentNumber;
			double payAmt = Double.parseDouble(paymentAmt);
			//System.out.println("Original Amount is " + currentBalance);
			if (payAmt > currentBalance)
				payAmt = currentBalance;
			cAmt = currentBalance;

			if (needToRollback == false)
			{
				//Read the installment charge record first.
				selectStmt = conn.prepareStatement(
						"select charge_no, payment_amount, charge_amount"
								+ " from installment_charge"
								+ " where installment_no=?"
								+ " and payment_amount<charge_amount"
								+ " order by charge_date asc");
				selectStmt.setInt(1, installNo);
				rset = selectStmt.executeQuery();

				currentAmt = cAmt;
				while ((rset.next()) && (i<MAX_SIZE))
				{
					i++;
					chargeNo = rset.getInt("charge_no");
					payAmount = rset.getDouble("payment_amount");
					//System.out.println("payment amount in the charge record " + paymentAmount);
					chargeAmount = rset.getDouble("charge_amount");
					pendingAmount = chargeAmount - payAmount;
					//System.out.println("Pending Amount for this charge record is " + pendingAmount);

					if (payAmt <= pendingAmount)
					{
						updateInstallmentCharge(installNo, paymentNumber, payAmount, payAmt, chargeNo, request);
						payAmt = payAmt - pendingAmount;
						break;
					}
					else
					{
						updateInstallmentCharge(installNo, paymentNumber, payAmount, pendingAmount, chargeNo, request);
						payAmt = payAmt - pendingAmount;
						currentAmt = cAmt - pendingAmount;
					}
				}
				//System.out.println("PayAmt is " + payAmt);
				//System.out.println("i= " + i);
				if ((i == 0) || (payAmt > 0))
				{
					//Insert the installment charge record first
					sqlString = "insert into installment_charge"
							+ " (installment_no, charge_amount, charge_date, payment_amount"
							+ ", payment_no, last_updated_datetime, last_updated_by, last_updated_from)"
							+ " values (?, ?, ?, ?, ?, ?, ?, ?)";
					insertStmt = conn.prepareStatement(sqlString);

					//currentAmt = cAmt;
					//if (payAmt <= Double.parseDouble(installAmt))
					//  i = 1;
					while (payAmt > Double.parseDouble(installAmt))
					{
						insertStmt.setInt(1, installNo);
						insertStmt.setDouble(2, Double.parseDouble(installAmt));
						chargedAmt += Double.parseDouble(installAmt);
						insertStmt.setTimestamp(3, Timestamp.valueOf(sdf2.format(dt)));
						insertStmt.setDouble(4, Double.parseDouble(installAmt));
						payAmt = payAmt - Double.parseDouble(installAmt);
						insertStmt.setInt(5, paymentNumber);
						insertStmt.setTimestamp(6, Timestamp.valueOf(sdf2.format(dt)));
						insertStmt.setString(7, (String)session.getAttribute("user_name"));
						insertStmt.setString(8, ipAdd);

						rowsInserted = insertStmt.executeUpdate();

						if (rowsInserted != 1)
						{
							System.out.println("Insert into table INSTALLMENT_CHARGE failed");
							needToRollback = true;
							rbd = new rollbackDone(userName, methodName, sqlString);
							returnCode = OPERATION_FAILED;
							payAmt = payAmt - Double.parseDouble(installAmt);
							break;
						}
						currentAmt -= Double.parseDouble(installAmt);
						i++;
					}

					//System.out.println("Current amount is " + currentAmt);
					//System.out.println("Installment amount is " + Double.parseDouble(installAmt));
					i++;
					insertStmt.setInt(1, installNo);
					if (currentAmt < Double.parseDouble(installAmt))
					{
						insertStmt.setDouble(2, currentAmt);
						chargedAmt += currentAmt;
					}
					else
					{
						insertStmt.setDouble(2, Double.parseDouble(installAmt));
						chargedAmt += Double.parseDouble(installAmt);
					}
					insertStmt.setTimestamp(3, Timestamp.valueOf(sdf2.format(dt)));
					insertStmt.setDouble(4, payAmt);
					insertStmt.setInt(5, paymentNumber);
					insertStmt.setTimestamp(6, Timestamp.valueOf(sdf2.format(dt)));
					insertStmt.setString(7, (String)session.getAttribute("user_name"));
					insertStmt.setString(8, ipAdd);

					rowsInserted = insertStmt.executeUpdate();

					if (rowsInserted != 1)
					{
						System.out.println("Insert into table INSTALLMENT_CHARGE failed");
						needToRollback = true;
						rbd = new rollbackDone(userName, methodName, sqlString);
						returnCode = OPERATION_FAILED;
					}
					chargedAmtAsStr = df.format(chargedAmt);
					System.out.println("Payment number after process payment is " + paymentNumber);
					System.out.println("Charged Amount is " + chargedAmtAsStr);
					if (needToRollback == false)
					{
						readInstallment(installNo);
						//Update Installment master file after the payment is done.
						updateInstallmentMaster(installNo, installAmt, chargedAmtAsStr, i, request);
					}
				}
			}
			paymentNumber = tempPayNo;
			System.out.println("Payment number after process payment is " + paymentNumber);
		}
		catch (NumberFormatException e)
		{
			System.out.println("Caught NumberFormatException in PaymentBean.processInstallmentPayment()!");
			System.out.println(e);
			e.printStackTrace();
			isDataSubmitted = false;
			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "NumberFormatException caught");
			returnCode = DATABASE_EXCEPTION;
		}
		/*
		 * catch (ParseException e)
		 * {
		 * // parse() throws this exception
		 * System.out.println("Caught ParseException in PaymentBean.processInstallmentPayment()!");
		 * System.out.println(e);
		 * e.printStackTrace();
		 *
		 * // in case of exception set isDataSubmitted flag to false
		 * isDataSubmitted = false;
		 *
		 * needToRollback = true;
		 * returnCode = DATABASE_EXCEPTION;
		 * }
		 */
		catch (SQLException e)
		{
			System.out.println("Caught SQLException in PaymentBean.processInstallmentPayment()!");
			System.out.println(e);
			e.printStackTrace();
			isDataSubmitted = false;
			needToRollback = true;
			rbd = new rollbackDone(userName, methodName, "SQLException caught");
			returnCode = DATABASE_EXCEPTION;
		}
		finally
		{
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.processInstallmentPayment().");
			try
			{
				//did not have rset closed
				if (rset != null){
					rset.close();
					rset = null;
				}

				if (selectStmt != null){
					selectStmt.close();
					selectStmt = null;
				}
				if (insertStmt != null){
					insertStmt.close();
					insertStmt = null;
				}
				if ((conn != null) && (!conn.isClosed()))
				{
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					conn.close();
					conn = null;
				}
				return returnCode;
			}
			catch (SQLException e)
			{
				System.out.println("Caught SQLException in PaymentBean.processInstallmentPayment()!");
				System.out.println(e);
				return returnCode;
			}
		}
	}

	/**
	 * updates installment_charge record in the database
	 * <p>
	 * @param HTTP request as <code>HttpServletRequest</code>
	 * @param installment number as <code>int</code>
	 * @param code as <code><a href="CodeBean.html>CodeBean</a></code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="InstallmentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: updates installment data in the database
	 *               </i>
	 */
	public int updateInstallmentCharge(int installNo, int paymentNo, double payAmount,
			double payAmt, int chNo, HttpServletRequest request)
	{
		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int rowsUpdated = 0;
		HttpSession session = null;
		String str = "";
		int i = 0;

		Connection conn = null;
		PreparedStatement updateStmt = null;
		String ipAdd = request.getRemoteAddr();

		try
		{
			conn = getConnection(jndiResource);
			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection

			session = request.getSession(false);
			caseId = Integer.parseInt((String)session.getAttribute("case_id"));
			sequenceNumber = Integer.parseInt((String)session.getAttribute("seq_no"));
			System.out.println("UpdateInstallmentCharge installNo:"+installNo+" paymentno:"+paymentNo+" payAmount:"+payAmount+" payAmt:"+payAmt+" chNo:"+chNo);
			DecimalFormat df = new DecimalFormat ("#0.00");

			SimpleDateFormat sdf1 = new SimpleDateFormat ("MM/dd/yyyy");
			SimpleDateFormat sdf2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

			java.util.Date dt = new java.util.Date();

			updateStmt = conn.prepareStatement(
					"update installment_charge"
							+ " set payment_amount=?, payment_no=?"
							+ ", last_updated_datetime=?, last_updated_by=?, last_updated_datetime=?"
							+ " where installment_no=? and charge_no=?");

			updateStmt.setDouble(1, payAmt + payAmount);
			updateStmt.setInt(2, paymentNo);
			updateStmt.setTimestamp(3, Timestamp.valueOf(sdf2.format(dt)));
			updateStmt.setString(4, (String)session.getAttribute("user_name"));
			updateStmt.setString(5, ipAdd);

			//System.out.println("Installment no is " + installNo);
			updateStmt.setInt(6, installNo);

			//System.out.println("Charge no is " + chNo);
			updateStmt.setInt(7, chNo);

			rowsUpdated = updateStmt.executeUpdate();
			if (rowsUpdated != 1)
			{
				System.out.println("Update in table INSTALLMENT_CHARGE failed");
				needToRollback = true;
				returnCode = OPERATION_FAILED;
			}
		}
		catch (NumberFormatException e)
		{
			System.out.println("Caught NumberFormatException in PaymentBean.updateInstallmentCharge()!");
			System.out.println(e);
			e.printStackTrace();
			isDataSubmitted = false;
			needToRollback = true;
			returnCode = DATABASE_EXCEPTION;
		}
		catch (SQLException e)
		{
			System.out.println("Caught SQLException in PaymentBean.updateInstallmentCharge()!");
			System.out.println(e);
			e.printStackTrace();
			isDataSubmitted = false;
			needToRollback = true;
			returnCode = DATABASE_EXCEPTION;
		}
		finally
		{
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.updateInstallmentCharge().");
			try
			{
				if (updateStmt != null){
					updateStmt.close();
					updateStmt = null;
				}
				if ((conn != null) && (!conn.isClosed()))
				{
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					conn.close();
					conn = null;
				}
				return returnCode;
			}
			catch (SQLException e)
			{
				System.out.println("Caught SQLException in PaymentBean.updateInstallmentCharge()!");
				System.out.println(e);
				needToRollback = true;
				return returnCode;
			}
		}
	}

	/**
	 * updates installment master file record in the database
	 * <p>
	 * @param HTTP request as <code>HttpServletRequest</code>
	 * @param installment number as <code>int</code>
	 * @param code as <code><a href="CodeBean.html>CodeBean</a></code>
	 * <p>
	 * @return
	 * <lo>
	 *  <li><code>0</code> if success or error codes:
	 *   <li><code><a href="GenericBean.html#NO_DATABASE_CONNECTION">NO_DATABASE_CONNECTION</a></code>
	 *   <li><code><a href="GenericBean.html#DATABASE_EXCEPTION">DATABASE_EXCEPTION</a></code>
	 *   <li><code><a href="InstallmentBean.html#OPERATION_FAILED">OPERATION_FAILED</a></code>
	 * </lo>
	 *
	 * @throws none
	 * <p>
	 * <i> Comments: updates installment data in the database
	 *               </i>
	 */
	public int updateInstallmentMaster(int installNo, String instAmt, String cAmt, int i, HttpServletRequest request)
	{
		int returnCode = SUCCESS;
		boolean needToRollback = false;
		int rowsUpdated = 0;
		HttpSession session = null;
		String str = "";
		double ytdAmt = 0.0;

		Connection conn = null;
		PreparedStatement updateStmt = null;
		String ipAdd = request.getRemoteAddr();

		try
		{
			conn = getConnection(jndiResource);
			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection

			session = request.getSession(false);
			caseId = Integer.parseInt((String)session.getAttribute("case_id"));
			sequenceNumber = Integer.parseInt((String)session.getAttribute("seq_no"));

			DecimalFormat df = new DecimalFormat ("#0.00");

			SimpleDateFormat sdf1 = new SimpleDateFormat ("MM/dd/yyyy");
			SimpleDateFormat sdf2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");

			java.util.Date dt = new java.util.Date();

			java.util.Date dt1 = Timestamp.valueOf(sdf2.format(sdf1.parse(nextBillingDate)));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dt1);

			updateStmt = conn.prepareStatement(
					"update installments"
							+ " set ytd_billed_amount=?"
							+ ", last_billing_date=?"
							+ ", next_billing_date=?"
							+ ", last_updated_datetime=?"
							+ ", last_updated_by=?"
							+ ", last_updated_from=?"
							+ " where installment_no=?");
			//System.out.println("Charged Amount is " + Double.parseDouble(cAmt));
			ytdAmt = Double.parseDouble(ytdAmount) + Double.parseDouble(cAmt);
			updateStmt.setDouble(1, ytdAmt);
			//System.out.println("ytd_billed_ammount="+ytdAmt);
			updateStmt.setTimestamp(2, Timestamp.valueOf(sdf2.format(sdf1.parse(nextBillingDate))));
			//System.out.println("last_billing_date='"+nextBillingDate+"'");

			if ("M".equals(frequency))
			{
				calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)+i);
			}
			else if ("W".equals(frequency))
			{
				calendar.set(Calendar.WEEK_OF_MONTH, calendar.get(Calendar.WEEK_OF_MONTH)+i);
			}
			else
			{
				//System.out.println("Frequency " + frequency);
				calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+i*(Integer.parseInt(frequency)));
			}

			Timestamp newLBDate = new Timestamp(calendar.getTime().getTime());

			if (Double.parseDouble(orgAmount) > ytdAmt)
			{
				updateStmt.setTimestamp(3, newLBDate);
				//System.out.println("next_billing_date='"+newLBDate+"'");
			}
			else
			{
				updateStmt.setNull(3, Types.VARCHAR);
				//System.out.println("next_billing_date='null'");
			}

			updateStmt.setTimestamp(4, Timestamp.valueOf(sdf2.format(dt)));
			//System.out.println("last_updated_datetime='"+ dt + "'");
			updateStmt.setString(5, (String)session.getAttribute("user_name"));
			//System.out.println("last_update_by='"+(String)session.getAttribute("user_name")+"'");
			updateStmt.setString(6, ipAdd);
			//System.out.println("last_update_from='"+ipAdd+"'");

			updateStmt.setInt(7, installNo);
			//System.out.println("installment_no='" + installNo + "'");

			//System.out.println("Test");
			rowsUpdated = updateStmt.executeUpdate();
			//System.out.println("Test " + rowsUpdated);
			if (rowsUpdated != 1)
			{
				System.out.println("Update in table INSTALLMENTS failed");
				needToRollback = true;
				returnCode = OPERATION_FAILED;
			}
		}
		catch (NumberFormatException e)
		{
			System.out.println("Caught NumberFormatException in PaymentBean.updateInstallmentMaster()!");
			System.out.println(e);
			e.printStackTrace();
			isDataSubmitted = false;
			needToRollback = true;
			returnCode = DATABASE_EXCEPTION;
		}
		catch (SQLException e)
		{
			System.out.println("Caught SQLException in PaymentBean.updateInstallmentMaster()!");
			System.out.println(e);
			e.printStackTrace();
			isDataSubmitted = false;
			needToRollback = true;
			returnCode = DATABASE_EXCEPTION;
		}
		finally
		{
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.updateInstallmentMaster().");
			try
			{
				if (updateStmt != null){
					updateStmt.close();
					updateStmt = null;
				}
				if ((conn != null) && (!conn.isClosed()))
				{
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					conn.close();
					conn = null;
				}
				return returnCode;
			}
			catch (SQLException e)
			{
				System.out.println("Caught SQLException in PaymentBean.updateInstallmentMaster()!");
				System.out.println(e);
				needToRollback = true;
				return returnCode;
			}
		}
	}

	public String getVictim(int caseId){

		System.out.println("retrieving victim information...");
		boolean needToRollback = false;
		String str = "";
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet rset = null;
		String victimInfo = "N/A";
		try {
			conn = getConnection(jndiResource);

			if (conn == null)
				return victimInfo; // no connection

			selectStmt = conn.prepareStatement("" +
					"SELECT notes " +
					"FROM notes " +
					"WHERE activity_code = 'case' " +
					"AND detail_no = ?");

			selectStmt.setInt(1, caseId);
			rset = selectStmt.executeQuery();
			// expect one row
			if (rset.next()) {
				str = rset.getString(1);
				if(str != null)
					victimInfo = str.trim();
			} else {
				System.out.println("SELECT from table notes failed");
				// if select failed - rollback to
				// release any locks
				needToRollback = true;
				//returnCode = OPERATION_FAILED;
			}
		}
		catch (SQLException e) {
			// preparedStatement(), executeUpdate() throw this exception
			System.out.println("Caught SQLException in PaymentBean.getVictim()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			//returnCode = DATABASE_EXCEPTION;
		}
		finally {


			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.getVictim().");
			try {

				if (selectStmt != null) {   // Close statement
					try { selectStmt.close(); } catch (SQLException e) { System.out.println("Caught selectStmt not closed in PaymentBean.getVictim()!"); }
					selectStmt = null;
				}

				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					try { conn.close(); } catch (SQLException e) { System.out.println("Caught connection not closed in PaymentBean.readPaymentData()!"); }
					conn = null;
				}

				return victimInfo;
			}
			catch (SQLException e) {
				// close() throw this exception
				System.out.println("Caught SQLException in PaymentBean.readPaymentData()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// inserts took place
				return victimInfo;
			}
		}

	}

	public int readCompliancePaymentDistribution(String startDate, String endDate){

		System.out.println("in readCompliancePaymentDistribution Method!");
		int returnCode = SUCCESS;
		boolean needToRollback = false;
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet rset = null;
		int recordsFound = 0;
		String str = "";
		try {
			conn = getConnection(jndiResource);

			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection

			// clear values in the member variables (rest form)
			reset();

			// define format used by form
			SimpleDateFormat sdf = new SimpleDateFormat ("MM/dd/yyyy");
			SimpleDateFormat sdf2 = new SimpleDateFormat ("yyyy-MM-dd");

			startDate = sdf2.format(sdf.parse(startDate));
			endDate = sdf2.format(sdf.parse(endDate));

			selectStmt = conn.prepareStatement("select c.court, " +
					"if((select value from code_table where code = b.fee_type and code_type = 'fee_group_criminal') is null, (select value from code_table where code = b.fee_type), (select value from code_table where code = b.fee_type and code_type = 'fee_group_criminal')) as fee_type, FORMAT(sum(a.amount),2) as total_payments, sum(a.amount) as double_payments " +
					"from (payments a inner join fees_fines b on a.case_id = b.case_id) " +
					"inner join cases c on c.case_id = a.case_id where a.last_updated_datetime >= '"+startDate+" 00:00:00' and a.last_updated_datetime <= '" +endDate + " 23:59:59' and a.method != 'WAIVE' and a.method != 'ERROR' and a.method != '0' and a.method != 'CANCEL' and a.method != 'JTCR' " +
					"and (b.fee_type = 'ATTYF' or b.fee_type = 'FINE' or b.fee_type like 'BCC%' or b.fee_type = 'COURT') and SUBSTR(c.court,1,2) != 'JP' " +
					"group by fee_type,c.court order by c.court, fee_type");

			System.out.println(selectStmt.toString());

			rset = selectStmt.executeQuery();

			while(rset.next())
				recordsFound++;

			this.compliancePaymentDist = new String[recordsFound][4];
			rset.beforeFirst();
			int i = 0;
			while(rset.next() && i < this.compliancePaymentDist.length){
				str = rset.getString(1);
				if(str != null)
					this.compliancePaymentDist[i][0] = str.trim();
				else
					this.compliancePaymentDist[i][0] = "";

				str = rset.getString(2);
				if(str != null)
					this.compliancePaymentDist[i][1] = str.trim();
				else
					this.compliancePaymentDist[i][1] = "";

				str = rset.getString(3);
				if(str != null)
					this.compliancePaymentDist[i][2] = str.trim();
				else
					this.compliancePaymentDist[i][2] = "";

				str = rset.getString(4);
				if(str != null)
					this.compliancePaymentDist[i][3] = str.trim();
				else
					this.compliancePaymentDist[i][3] = "";


				i++;
			}


		}
		catch (SQLException e) {
			// preparedStatement(), executeUpdate() throw this exception
			System.out.println("Caught SQLException in PaymentBean.readCompliancePaymentDistribution()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			returnCode = DATABASE_EXCEPTION;
		}
		finally {


			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.readCompliancePaymentDistribution().");
			try {

				if (selectStmt != null)    // Close statement
					selectStmt = null;
				if (rset != null)    // Close statement
					rset = null;


				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					try { conn.close(); } catch (SQLException e) { System.out.println("Caught connection not closed in PaymentBean.readCompliancePaymentDistribution()!"); }
					conn = null;
				}

				return returnCode;
			}
			catch (SQLException e) {
				// close() throw this exception
				System.out.println("Caught SQLException in PaymentBean.readPaymentData()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// inserts took place
				return returnCode;
			}
		}

	}

	public int getAllCasesByPaymentDate(String startDate,String endDate){

		int returnCode = SUCCESS;
		boolean needToRollback = false;
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet rset = null;
		int recordsFound = 0;
		String str = "";

		SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat ("MM/dd/yyyy");
		try {

			conn = getConnection(jndiResource);

			if (conn == null)
				return NO_DATABASE_CONNECTION; // no connection

			startDate = sdf.format(sdf1.parse(startDate));
			endDate = sdf.format(sdf1.parse(endDate));

			selectStmt = conn.prepareStatement(""
					+ "SELECT case_id,last_updated_datetime, "
					+ "(select concat(if(fname is not null,concat(fname,' '),''),if(mname is not null,concat(mname,' '),''), if(lname is not null,lname,'')) "
					+ "from debtors where person_no = (select person_no from parties where party_code = 'DEF' and case_id = payments.case_id)) "
					+ "FROM payments WHERE last_updated_datetime >= '" + startDate +" 00:00:00' AND last_updated_datetime <= '" + endDate +" 23:59:59' and method <> 'ERROR' and method <> 'WAIVE' and method <> 'CANCEL'");

			rset = selectStmt.executeQuery();

			while(rset.next())
				recordsFound++;
			if(recordsFound > 0){
				this.paymentsArray = new String[recordsFound][3];
				rset.beforeFirst();

				for(int i = 0; rset.next() && i < this.paymentsArray.length;i++){

					str = rset.getString(1);
					if(str != null)
						this.paymentsArray[i][0] = str.trim();
					else
						this.paymentsArray[i][0] = "";

					str = rset.getString(2);
					if(str != null)
						this.paymentsArray[i][1] = str.trim();
					else
						this.paymentsArray[i][1] = "";

					str = rset.getString(3);
					if(str != null)
						this.paymentsArray[i][2] = str.trim();
					else
						this.paymentsArray[i][2] = "";
				}
			}

		}
		catch (Exception e) {
			// preparedStatement(), executeUpdate() throw this exception
			System.out.println("Caught Exception in PaymentBean.getAllCasesByPaymentDate()!");
			System.out.println(e);
			e.printStackTrace();

			// in case of exception set isDataSubmitted flag to false
			isDataSubmitted = false;

			needToRollback = true;
			returnCode = DATABASE_EXCEPTION;
		}
		finally {


			// close statement(s) in one place
			System.out.println("Statement/ResultSet related cleanup in PaymentBean.getAllCasesByPaymentDate().");
			try {

				if (selectStmt != null)    // Close statement
					selectStmt = null;
				if (rset != null)    // Close statement
					rset = null;


				if ((conn != null) && (!conn.isClosed())) {
					if (needToRollback)
						conn.rollback();
					else
						conn.commit();
					try { conn.close(); } catch (SQLException e) { System.out.println("Caught connection not closed in PaymentBean.getAllCasesByPaymentDate()!"); }
					conn = null;
				}

				return returnCode;
			}
			catch (SQLException e) {
				// close() throw this exception
				System.out.println("Caught SQLException in PaymentBean.getAllCasesByPaymentDate()!");
				System.out.println(e);
				// if error occured closing statement - don't report it
				// inserts took place
				return returnCode;
			}
		}


	}
}
