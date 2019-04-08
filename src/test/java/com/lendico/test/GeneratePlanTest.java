package com.lendico.test;


import java.text.ParseException;

import org.testng.annotations.*;

import com.jayway.restassured.response.Response;
import com.lendico.utils.ApiUtils;

/**
 * Created by Vipin Alias Neo De Van
 */
public class GeneratePlanTest extends BaseTest{

    @Test(dataProvider="TestData")
    public void verifyAnnuityTest(String loanAmount, String nominalRate, String duration, String startDate) throws ParseException {
    	int durationData=Integer.parseInt(duration);
        Response resPlan = ApiUtils.postResponsebyBodyGeneratePlan(loanAmount, nominalRate, durationData,startDate);
        Response resAnnuity = ApiUtils.postResponsebyBodyCalculateAnnuity(loanAmount, nominalRate, durationData);
        testUtils.verifyAnnuity(resPlan, resAnnuity);
    }
    
    
    @Test(dataProvider="TestData")
    public void verifyDateTest(String loanAmount, String nominalRate, String duration, String startDate) throws ParseException {
    	int durationData=Integer.parseInt(duration);
        Response resPlan = ApiUtils.postResponsebyBodyGeneratePlan(loanAmount, nominalRate, durationData,startDate);
        testUtils.verifyDate(resPlan, startDate);
    }
    
    
    @Test(dataProvider="TestData")
    public void verifyPrincipalSumTest(String loanAmount, String nominalRate, String duration, String startDate) {
    	double loanAmountData=Double.parseDouble(loanAmount);
    	int durationData=Integer.parseInt(duration);
        Response res = ApiUtils.postResponsebyBodyGeneratePlan(loanAmount, nominalRate, durationData,startDate);
        testUtils.verifPrincipleSumMatchLoanAmount(res, loanAmountData);
    }
    
    
    @Test(dataProvider="TestData")
    public void verifyPrincipleInterestCalculationTest(String loanAmount, String nominalRate, String duration, String startDate) {
    	double nominalRateData=Double.parseDouble(nominalRate);
    	int durationData=Integer.parseInt(duration);
        Response res = ApiUtils.postResponsebyBodyGeneratePlan(loanAmount, nominalRate, durationData,startDate);
        testUtils.verifyPrincipleInterestCalculationByMonth(res, durationData, nominalRateData);
    }
    
    
    

    
}
