package com.example.navbotdialog;

public class GetRegisterationData {

    String getFName,getMName,getLName,getEmail,getBranch,getLearningYear,getCollegeName,
            getNumber,getEventName,getParticipantsCount, paymentMode;
    String getTransactionId="";
    String getPaymentReciever="";

    public GetRegisterationData(String getFName, String getMName, String getLName, String getEmail, String getBranch, String getLearningYear, String getCollegeName, String getNumber, String getEventName, String getParticipantsCount, String paymentMode, String getTransactionId, String getPaymentReciever) {
        this.getFName = getFName;
        this.getMName = getMName;
        this.getLName = getLName;
        this.getEmail = getEmail;
        this.getBranch = getBranch;
        this.getLearningYear = getLearningYear;
        this.getCollegeName = getCollegeName;
        this.getNumber = getNumber;
        this.getEventName = getEventName;
        this.getParticipantsCount = getParticipantsCount;
        this.paymentMode = paymentMode;
        this.getTransactionId = getTransactionId;
        this.getPaymentReciever = getPaymentReciever;
    }

    public String getGetFName() {
        return getFName;
    }

    public String getGetMName() {
        return getMName;
    }

    public String getGetLName() {
        return getLName;
    }

    public String getGetEmail() {
        return getEmail;
    }

    public String getGetBranch() {
        return getBranch;
    }

    public String getGetLearningYear() {
        return getLearningYear;
    }

    public String getGetCollegeName() {
        return getCollegeName;
    }

    public String getGetNumber() {
        return getNumber;
    }

    public String getGetEventName() {
        return getEventName;
    }

    public String getGetParticipantsCount() {
        return getParticipantsCount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public String getGetTransactionId() {
        return getTransactionId;
    }

    public String getGetPaymentReciever() {
        return getPaymentReciever;
    }
}
