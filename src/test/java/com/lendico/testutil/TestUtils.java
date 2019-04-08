package com.lendico.testutil;

import static org.testng.Assert.assertEquals;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.lendico.utils.ApiUtils;

import org.apache.log4j.Logger;
import org.testng.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by neodevan
 */
public class TestUtils {

	
	private static final Logger log = Logger.getLogger(TestUtils.class);
	public void checkStatusIs200(Response res) {
		Assert.assertEquals(res.getStatusCode(), 200, "Status Check Failed!");
	}

	public void verifyAnnuity(Response responsePlan, Response responseAnnuity) {
		JsonPath jpPlan = ApiUtils.getJsonPath(responsePlan);
		JsonPath jpAnuity = ApiUtils.getJsonPath(responseAnnuity);
		ArrayList<String> borrowerPaymentList = jpPlan
				.get("borrowerPaymentAmount");
		String AnnuityCalculated = jpAnuity.get("annuity");
		for (int i = 0; i < borrowerPaymentList.size()-1; i++) {
			assertEquals(borrowerPaymentList.get(i), AnnuityCalculated, "Actuat Borrower Ammout: " + borrowerPaymentList.get(i)
					+ " is Not Matching Calculated Annuity: " + AnnuityCalculated);
			log.info("Actuat Borrower Ammout: " + borrowerPaymentList.get(i)
					+ " is Matching Calculated Annuity: " + AnnuityCalculated);
		}

	}

	public void verifPrincipleSumMatchLoanAmount(Response res,
			double LoanAmount) {
		JsonPath jp = ApiUtils.getJsonPath(res);
		ArrayList<String> principal = jp.get("principal");
		Double principalSum = 0.0;
		for (String principalAmount : principal) {
			Double pricipalperMonth = Double.parseDouble(principalAmount);
			principalSum += pricipalperMonth;
		}
		int actualAmount=(int)Math.round(principalSum);
		assertEquals(actualAmount, (int)LoanAmount, "Sum of Principal amount: " + actualAmount
				+ " is Not Matching Loan Amount: " + (int)LoanAmount);
		log.info("Sum of Principal amount: " + actualAmount
				+ " is Matching Loan Amount: " + (int)LoanAmount);

	}
	
	
	public void verifyDate(Response res,
			String startDate) throws ParseException {
	    Date ActualDate=null;
		JsonPath jp = ApiUtils.getJsonPath(res);
		ArrayList<String> dateSplits = jp.get("date");
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		Date ExpectedDate=simpleDateFormat.parse(startDate);
		for(String monthdate:dateSplits){
			ActualDate = simpleDateFormat.parse(monthdate);
			assertEquals(ActualDate, ExpectedDate, "Date in Borrower Split: " + ActualDate
					+ " is Not Matching Start Date increment By a month: " + ExpectedDate);
			ExpectedDate=addMonths(ExpectedDate,1);
			log.info("Date in Borrower Split: " + ActualDate
					+ " is Matching Start Date increment By a month: " + ExpectedDate);
		}
		
	}
	
	public Date addMonths(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }



	public void verifyPrincipleInterestCalculationByMonth(Response res, int duration, double nominalRate) {
		JsonPath jp = ApiUtils.getJsonPath(res);
		ArrayList<String> principal = jp.get("principal");
		ArrayList<String> interest = jp.get("interest");
		ArrayList<String> ROP = jp.get("remainingOutstandingPrincipal");
		ArrayList<String> BPA = jp.get("borrowerPaymentAmount");

		for (int i = 1; i < duration; i++) {
			double currentBPA = Double.parseDouble(BPA.get(i));
			double previousROP = Double.parseDouble(ROP.get(i - 1));
			double actualInterest = Double.parseDouble(interest.get(i));
			double actualPrincipal = Double.parseDouble(principal.get(i));
			double expectedInterest = (previousROP * nominalRate * 30) / 360;
			expectedInterest = (double) Math
					.round((expectedInterest / 100.0) * 100.0) / 100.0;
			double expectedPrinciple = (double) Math
					.round((currentBPA - expectedInterest)* 100.0) / 100.0;
			assertEquals(actualInterest, expectedInterest, "Actual Interest: " + actualInterest
					+ " is Not Matching Expected Interest: " + expectedInterest);
			assertEquals(actualPrincipal, expectedPrinciple, "Actual Principal: " + actualInterest
					+ " is Not Matching Expected Expected Principal: " + expectedPrinciple);
			log.info("Actual Interest: " + actualInterest
					+ " is Matching Expected Interest: " + expectedInterest);
			log.info("Actual Principal: " + actualInterest
					+ " is Matching Expected Expected Principal: " + expectedPrinciple);
		}
	}

	

}
